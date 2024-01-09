package com.xw.spring;

import com.xw.spring.ioc.eg1.Apple;
import com.xw.spring.ioc.eg1.Person;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/2
 */
//@RunWith(SpringRunner.class)    // 将junit的执行权交给spring管理，在测试前自动初始化ioc容器
//@ContextConfiguration("classpath:ioc.xml")
public class SpringIOCTest {

    @Test
    public void original() {
        Apple apple1 = new Apple("青苹果", "青色", "中亚");
        Apple apple2 = new Apple("红富士", "红色", "欧洲");
        Person child = new Person("child", apple1);
        child.eat();
        Person adult = new Person("adult", apple2);
        adult.eat();
    }

    @Test
    public void ioc() {
        // 创建Spring ioc容器，并根据配置文件在容器中实例化对象
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:ioc.xml");
//        Apple apple = context.getBean("cyanApple", Apple.class);
//        System.out.println(apple);
//        Person child = context.getBean("child", Person.class);
//        child.eat();
//        Company company = context.getBean("company", Company.class);

        // 查看容器内对象数量
//        int beanCount = context.getBeanDefinitionCount();
        // 查看容器内都有哪些对象
//        String[] beanNames = context.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
        // scope="prototype" 每次使用时都是创建一个实例
//        Apple apple2 = context.getBean("apple2", Apple.class);
//        Apple apple3 = context.getBean("apple2", Apple.class);

//        Order order1 = context.getBean("order1", Order.class);
//        order1.pay();
//        ((ClassPathXmlApplicationContext) context).registerShutdownHook();
    }

    @Test
    public void iocCustom() {
        com.xw.spring.ioc.context.ApplicationContext context = new com.xw.spring.ioc.context.ClassPathXmlApplicationContext("/iocCustom.xml");
        Apple apple = (Apple) context.getBean("apple");
        System.out.println(apple);
    }
}
