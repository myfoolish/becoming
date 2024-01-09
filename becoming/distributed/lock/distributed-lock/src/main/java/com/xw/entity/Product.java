package com.xw.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/9
 */
@Data
public class Product {
    private int id;
    // 商品名称
    private int productName;
    // 商品金额
    private BigDecimal price;
    // 商品数量（库存）
    private int count;
    // 商品描述
    private String productDesc;
    // 创建时间
    private Date createTime;
    // 创建人
    private String createUser;
    // 更新时间
    private Date updateTime;
    // 更新人
    private String updateUser;
}
