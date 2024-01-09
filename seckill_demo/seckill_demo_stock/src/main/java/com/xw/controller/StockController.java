package com.xw.controller;

import com.alibaba.fastjson.JSONObject;
import com.xw.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/getStockList")
    public Map<String, Object> getStockList(){
        return stockService.getStockList();
    }

    @RequestMapping(value = "/getStock/{sku_id}")
    public Map<String, Object> getStock(@PathVariable("sku_id") String sku_id){
        return stockService.getStock(sku_id);
    }


    @RequestMapping(value = "/insertLimitPolicy/{json}")
    public Map<String, Object> insertLimitPolicy(@PathVariable("json") String json){
        Map<String, Object> policyInfo = JSONObject.parseObject(json, Map.class);
        return stockService.insertLimitPolicy(policyInfo);
    }
}
