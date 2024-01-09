package com.xw.controller;

import com.xw.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/26
 */
@RestController
@RequestMapping("business")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping("placeOrder")
    public String placeOrder(Long accountId, Long itemId, int num) {
        businessService.placeOrder(accountId, itemId, num);
        return "下单成功";
    }

    @GetMapping("placeOrderByFeign")
    public String placeOrderByFeign(Long accountId, Long itemId, int num) {
        businessService.placeOrderByFeign(accountId, itemId, num);
        return "下单成功";
    }
}
