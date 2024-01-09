package com.xw.dao.impl;

import com.xw.dao.StorageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/7/14
 */
@Repository
public class StorageDaoImpl implements StorageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 创建查询库存方法
     * @param sku_id
     * @return
     */
    public ArrayList<Map<String, Object>> getStockStorage(String sku_id) {
        String sql = "SELECT sku_id, quanty FROM tb_stock_storage WHERE sku_id = ?";
        ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) jdbcTemplate.queryForList(sql, sku_id);
        return list;
    }
}