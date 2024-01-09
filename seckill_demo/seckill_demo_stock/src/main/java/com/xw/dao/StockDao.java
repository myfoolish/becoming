package com.xw.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
public interface StockDao {

    ArrayList<Map<String, Object>> getStockList();

    ArrayList<Map<String, Object>> getStock(String sku_id);

    boolean insertLimitPolicy(Map<String, Object> policyInfo);
}
