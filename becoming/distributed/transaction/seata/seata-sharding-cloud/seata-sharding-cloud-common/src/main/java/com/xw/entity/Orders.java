package com.xw.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@Data
// 创建对象使用链式调用
@Accessors(chain = true)
public class Orders {
    private Long id;
    private Long accountId;
    private Long itemId;
    private int itemNum;
    private String orderStatus;
    private double orderAmount;
}
