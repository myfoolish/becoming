package com.xw.service;

import com.xw.entity.Orders;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
public interface OrdersService {
    public void createOrder(Orders orders);

    void updateOrder(Orders orders);
}
