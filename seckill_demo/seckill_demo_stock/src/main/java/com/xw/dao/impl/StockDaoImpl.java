package com.xw.dao.impl;

import com.xw.dao.StockDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
@Repository
public class StockDaoImpl implements StockDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public ArrayList<Map<String, Object>> getStockList() {
        //1、创建商品查询的SQL
        String sql = "select id AS sku_id, title, images, stock, price, indexes, own_spec " +
                "from seckill_demo_tb_sku";
        //2、执行这个SQL
        ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) jdbcTemplate.queryForList(sql);
        //3、返回数据
        return list;
    }

    @Override
    public ArrayList<Map<String, Object>> getStock(String sku_id) {
        //1、创建商品查询的SQL
        String sql = "select seckill_demo_tb_sku.spu_id, seckill_demo_tb_sku.title, seckill_demo_tb_sku.images, seckill_demo_tb_sku.stock, seckill_demo_tb_sku.price, seckill_demo_tb_sku.indexes, " +
                "seckill_demo_tb_sku.own_spec, seckill_demo_tb_sku.enable, seckill_demo_tb_sku.create_time, seckill_demo_tb_sku.update_time,seckill_demo_tb_spu_detail.description," +
                "seckill_demo_tb_sku.id AS sku_id,seckill_demo_tb_spu_detail.special_spec " +
                "from seckill_demo_tb_sku " +
                "INNER JOIN seckill_demo_tb_spu_detail ON seckill_demo_tb_spu_detail.spu_id=seckill_demo_tb_sku.spu_id " +
                "where seckill_demo_tb_sku.id = ?";
        //2、执行SQL
        ArrayList<Map<String, Object>> list = (ArrayList<Map<String, Object>>) jdbcTemplate.queryForList(sql, sku_id);
        //3、返回信息
        return list;
    }

    @Override
    public boolean insertLimitPolicy(Map<String, Object> policyInfo) {
        //1、创建写入政策的语句
        String sql = "insert into seckill_demo_tb_limit_policy (sku_id, quanty, price, begin_time, end_time) " +
                "Values (?, ?, ?, ?, ?)";
        //2、执行
        boolean result = jdbcTemplate.update(sql, policyInfo.get("sku_id"), policyInfo.get("quanty"), policyInfo.get("price"),
                policyInfo.get("begin_time"), policyInfo.get("end_time")) == 1;
        //3、返回信息
        return result;
    }
}
