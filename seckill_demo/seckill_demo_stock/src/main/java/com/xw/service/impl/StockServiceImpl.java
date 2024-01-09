package com.xw.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xw.dao.StockDao;
import com.xw.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
@Service
public class StockServiceImpl implements StockService {

    @Autowired
    private StockDao stockDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Map<String, Object> getStockList() {
        Map<String, Object> resultMap = new HashMap<String, Object>();

        //1、取自 StockDao 的方法
        ArrayList<Map<String, Object>> list = stockDao.getStockList();

        //2、如果没有取出来，返回一个错误信息
        if (list == null || list.size() == 0) {
            resultMap.put("result", false);
            resultMap.put("msg", "您没有取出商品信息！");
            return resultMap;
        }

        //3、取redis政策
        resultMap = getLimitPolicy(list);

        //4、返回正常信息
        resultMap.put("sku_list", list);
//        resultMap.put("result", true);
//        resultMap.put("msg", "");
        return resultMap;
    }

    @Override
    public Map<String, Object> getStock(String sku_id) {
        Map<String, Object> resultMap = new HashMap<>();

        // 1、判断传入的参数
        if (sku_id == null || sku_id.equals("")) {
            resultMap.put("result", false);
            resultMap.put("msg", "您传入的参数有误！");
            return resultMap;
        }

        // 2、取自 stockDao 的方法
        ArrayList<Map<String, Object>> list = stockDao.getStock(sku_id);

        // 3、如果没有取出数据，返回一个错误信息
        if (list==null||list.size()==0){
            resultMap.put("result", false);
            resultMap.put("msg", "您没有取出商品信息！");
            return resultMap;
        }

        //4、取redis政策
        resultMap = getLimitPolicy(list);

        //5、返回正常信息
        resultMap.put("sku", list);
//        resultMap.put("result", true);
//        resultMap.put("msg", "");
        return resultMap;

    }

    @Override
    @Transactional
    public Map<String, Object> insertLimitPolicy(Map<String, Object> policyInfo) {

        Map<String, Object> resultMap = new HashMap<>();
        //1、传入的参数判断
        if (policyInfo == null || policyInfo.isEmpty()) {
            resultMap.put("result", false);
            resultMap.put("msg", "您传入的参数有误！");
            return resultMap;
        }

        //2、取自 stockDao 的 insertLimitPolicy 方法
        boolean result = stockDao.insertLimitPolicy(policyInfo);

        //3、如果没有执行成功，返回错误信息
        if (!result){
            resultMap.put("result", false);
            resultMap.put("msg", "数据库写入政策时失败！");
            return resultMap;
        }

        //4、如果成功，写入redis，需要写入有效期，key取名：LIMIT_POLICY_{sku_id} StringRedisTemplate
        //4.1、取名  key: LIMIT_POLICY_{sku_id}， value: policyInfo --> String
        //4.2、redis有效期，有效期：结束时间减去当前时间

        long diff = 0;
        String now = restTemplate.getForObject("http://seckill-demo-time-server/time_server/getTime", String.class);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date end_time = simpleDateFormat.parse(policyInfo.get("end_time").toString());
            Date now_time = simpleDateFormat.parse(now);
            diff = (end_time.getTime()-now_time.getTime())/1000;
            if (diff<=0){
                resultMap.put("result", false);
                resultMap.put("msg", "结束时间不能小于当前时间！");
                return resultMap;
            }
        } catch (ParseException e) {
//            e.printStackTrace();
            resultMap.put("result", false);
            resultMap.put("msg", "日期转换又失败了");
            return resultMap;
        }

        String sku_id = policyInfo.get("sku_id").toString();
        stringRedisTemplate.delete("LIMIT_POLICY" + policyInfo.get("sku_id").toString());

        String policy = JSON.toJSONString(policyInfo);
        stringRedisTemplate.opsForValue().set("LIMIT_POLICY_"+policyInfo.get("sku_id").toString(), policy, diff, TimeUnit.SECONDS);

        //商品存入redis
        stringRedisTemplate.delete("SKU" + policyInfo.get("sku_id").toString());
        ArrayList<Map<String, Object>> list = stockDao.getStock(policyInfo.get("sku_id").toString());
        String sku = JSON.toJSONString(list.get(0));
        stringRedisTemplate.opsForValue().set("SKU_"+policyInfo.get("sku_id").toString(), sku, diff, TimeUnit.SECONDS);

        //5、返回正常信息
        resultMap.put("result", true);
        resultMap.put("msg", "政策写入完毕！");
        return resultMap;
    }

    private Map<String, Object> getLimitPolicy(ArrayList<Map<String, Object>> list){

        Map<String, Object> resultMap = new HashMap<>();

        for (Map<String, Object> skuMap: list){
            // 3.1、从 redis 取出政策，如果取到政策，才给商品赋值
            String policy = stringRedisTemplate.opsForValue().get("LIMIT_POLICY_"+skuMap.get("sku_id").toString());
            if (policy != null && !policy.equals("")) {
                Map<String, Object> policyInfo = JSONObject.parseObject(policy, Map.class);

                // 3.2、开始时间 小于等于 当前时间，并且当前时间 小于等于 结束时间
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String now = restTemplate.getForObject("http://seckill-demo-time-server/time_server/getTime", String.class);
                try {
                    Date begin_time = simpleDateFormat.parse(policyInfo.get("begin_time").toString());
                    Date end_time = simpleDateFormat.parse(policyInfo.get("end_time").toString());
                    Date now_time = simpleDateFormat.parse(now);

                    if (begin_time.getTime() <= now_time.getTime() && now_time.getTime() <= end_time.getTime()) {
                        // 赋值：limitPrice  limitQuanty  limitBeginTime  limitEndTime   nowTime
                        skuMap.put("limitPrice", policyInfo.get("price"));  // 政策上的价格
                        skuMap.put("limitQuanty", policyInfo.get("quanty"));
                        skuMap.put("limitBeginTime", policyInfo.get("begin_time")); // 政策上开始时间
                        skuMap.put("limitEndTime", policyInfo.get("end_time")); // 政策上结束时间
                        skuMap.put("nowTime", now); // 当前时间
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        resultMap.put("result", true);
        resultMap.put("msg", "");
        return resultMap;
    }
}
