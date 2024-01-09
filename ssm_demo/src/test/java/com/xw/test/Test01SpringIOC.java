package com.xw.test;

import com.xw.service.AccountService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author liuxiaowei
 * @description 测试 Spring 的 IOC 环境搭建
 * @date 2021/6/2
 */
public class Test01SpringIOC {
    public static void main(String[] args) {

        // 获取容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        // 根据 id 获取对象
        AccountService accountService = applicationContext.getBean("accountService", AccountService.class);
        accountService.findAll();
    }
}
