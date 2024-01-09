package com.xw.controller;

import com.xw.entity.Orders;
import com.xw.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@RestController
@RequestMapping("orders")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("createOrder")
    public String createOrder(@RequestBody Orders orders) {
        ordersService.createOrder(orders);
        return "创建订单成功";
    }

    @PutMapping("updateOrder")
    public String updateOrder(@RequestBody Orders orders) {
        ordersService.updateOrder(orders);
        return "更新订单成功";
    }
}
