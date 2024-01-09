package com.xw.service;

import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
public interface StockService {

    public Map<String, Object> getStockList();

    public Map<String, Object> getStock(String sku_id);

    public Map<String, Object> insertLimitPolicy(Map<String, Object> policyInfo);
}
