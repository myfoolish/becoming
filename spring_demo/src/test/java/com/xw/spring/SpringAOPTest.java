package com.xw.spring;

import com.xw.spring.aop.service.UserService;
import com.xw.spring.ioc.eg3.Order;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/2
 */
@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:aop.xml")
public class SpringAOPTest {

    @Resource
    private UserService userService;
    @Test
    public void aop() {
        userService.insert();
        userService.generateRandomPassword("MD6", 16);
    }

    /**
     * 反射的三种方式
     */
    @Test
    public void reflect() {
        Order order = new Order();

        try {
            // 1、获取该对象的Class对象
            Class<?> aClass1 = order.getClass();
            // 获取类名称
            String name = aClass1.getName();
            System.out.println(name);   // com.xw.spring.ioc.eg3.Order

            // 2、获取该对象的Class对象
            Class<Order> aClass2 = Order.class;
            name = aClass2.getName();
            System.out.println(name);   // com.xw.spring.ioc.eg3.Order

            // 3、通过类的全路径名获取对象的Class对象
            Class<?> aClass3 = Class.forName("com.xw.spring.ioc.eg3.Order");
            name = aClass3.getName();
            System.out.println(name);   // com.xw.spring.ioc.eg3.Order
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
