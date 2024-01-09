package com.xw.dao;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/7/14
 */
public interface StorageDao {

    ArrayList<Map<String, Object>> getStockStorage(String sku_id);
}
