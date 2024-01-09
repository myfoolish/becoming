package com.xw.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liuxiaowei
 * @description 订单明细
 * @date 2023/6/9
 */
@Data
public class OrderItem {
    private int id;
    // 订单id
    private int orderId;
    // 商品id
    private int produceId;
    // 购买金额
    private BigDecimal purchasePrice;
    // 购买数量
    private int purchaseCount;
    // 创建时间
    private Date createTime;
    // 创建人
    private String createUser;
    // 更新时间
    private Date updateTime;
    // 更新人
    private String updateUser;
}
