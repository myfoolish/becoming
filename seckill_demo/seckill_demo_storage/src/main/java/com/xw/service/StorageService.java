package com.xw.service;

import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/7/14
 */
public interface StorageService {
    Map<String, Object> getStockStorage(String sku_id);
}
