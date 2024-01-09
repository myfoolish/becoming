package com.xw.springinaction.test;

import com.xw.springinaction.knights.Knight;
import com.xw.springinaction.knights.config.KnightConfig;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author liuxiaowei
 * @description Spring自带了多种类型的应用上下文
 *              AnnotationConfigApplicationContext：从一个或多个基于Java的配置类中加载Spring应用上下文。
 *              AnnotationConfigWebApplicationContext：从一个或多个基于Java的配置类中加载Spring Web应用上下文。
 *              ClassPathXmlApplicationContext：从类路径下的一个或多个XML配置文件中加载上下文定义，把应用上下文的定义文件作为类资源。
 *              FileSystemXmlApplicationContext：从文件系统下的一个或多个XML配置文件中加载上下文定义。
 *              XmlWebApplicationContext：从Web应用下的一个或多个XML配置文件中加载上下文定义。”
 * @date 2022/1/10
 */
public class KnightTest {

    @Test
    public void knightXmlTest() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("knights.xml");  // 加载 spring 上下文
        Knight knight = context.getBean(Knight.class);  // 获取 knight bean
        knight.embark();
        context.close();
    }

    @Test
    public void knightXmlTest1() {
        ApplicationContext context = new ClassPathXmlApplicationContext("knights.xml");  // 加载 spring 上下文
        Knight knight = (Knight) context.getBean("knight");  // 获取 knight bean
        knight.embark();
    }

    @Test
    public void knightJavaConfigurationTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(KnightConfig.class);
        Knight knight = context.getBean(Knight.class);
        knight.embark();
        context.close();
    }
}