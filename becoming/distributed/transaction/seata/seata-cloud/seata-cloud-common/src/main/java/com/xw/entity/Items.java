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
public class Items {
    private Long id;
    private String itemName;
    private double itemPrice;
    private int itemStock;
}
