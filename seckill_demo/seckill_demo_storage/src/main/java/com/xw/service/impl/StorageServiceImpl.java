package com.xw.service.impl;

import com.xw.dao.StorageDao;
import com.xw.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/7/14
 */
@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageDao storageDao;

    public Map<String, Object> getStockStorage(String sku_id) {
        //1、先取得一个商品的库存
        ArrayList<Map<String ,Object>> list = new ArrayList<Map<String, Object>>();
        list = storageDao.getStockStorage(sku_id);

        //2、判断如果stockDao取出的商品为空，返回一个提示
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (list==null||list.isEmpty()){
            resultMap.put("result", false);
            resultMap.put("msg", "完了，服务器挂了，数据没取出来！");
            return resultMap;
        }

        //3、判断如果取出的商品不为空，返回数据
        resultMap.put("storage", list);
        resultMap.put("result", true);
        resultMap.put("msg", "");
        return resultMap;
    }
}
