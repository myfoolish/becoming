package com.xw.service.impl;

import com.xw.entity.Orders;
import com.xw.mapper.OrdersMapper;
import com.xw.service.OrdersService;
import io.seata.core.context.RootContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Override
    public void createOrder(Orders orders) {
        System.out.println(RootContext.inGlobalTransaction());
        System.out.println(RootContext.getXID());
        ordersMapper.createOrder(orders);
    }

    @Override
    public void updateOrder(Orders orders) {
        System.out.println(RootContext.inGlobalTransaction());
        System.out.println(RootContext.getXID());
        ordersMapper.updateOrder(orders);
    }
}
