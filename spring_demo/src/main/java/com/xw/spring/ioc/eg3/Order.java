package com.xw.spring.ioc.eg3;

import lombok.Data;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/4
 */
@Data
public class Order {
    private Float price;        // 价格
    private Integer quantity;   // 数量
    private Float total;        // 总价

    public void pay() {
        System.out.println("订单价格为：" + total);
    }

    public void init() {
        System.out.println("执行init()");
        total = price * quantity;
    }

    public void destroy() {
        System.out.println("释放与订单相关资源");
    }
}
