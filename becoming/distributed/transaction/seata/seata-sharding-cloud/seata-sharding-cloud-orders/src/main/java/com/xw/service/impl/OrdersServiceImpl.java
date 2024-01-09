package com.xw.service.impl;

import com.xw.entity.Orders;
import com.xw.mapper.OrdersMapper;
import com.xw.service.OrdersService;
import io.seata.core.context.RootContext;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
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
        // 在分库分表的服务中，执行业务之前，使用编码的方式指定事务为BASE
        TransactionTypeHolder.set(TransactionType.BASE);
        ordersMapper.createOrder(orders);
    }

    @Override
    public void updateOrder(Orders orders) {
        System.out.println(RootContext.inGlobalTransaction());
        System.out.println(RootContext.getXID());
        ordersMapper.updateOrder(orders);
    }
}
