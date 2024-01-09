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
public class Order {
    private int id;
    // 订单状态 1:待支付
    private int orderStatus;
    // 收货人姓名
    private String receiverName;
    // 收货人电话
    private String receiverMobile;
    // 订单金额
    private BigDecimal orderAmount;
    // 创建时间
    private Date createTime;
    // 创建人
    private String createUser;
    // 更新时间
    private Date updateTime;
    // 更新人
    private String updateUser;
}
