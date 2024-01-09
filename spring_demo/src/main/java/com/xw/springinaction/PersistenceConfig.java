package com.xw.springinaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jndi.JndiObjectFactoryBean;

import javax.sql.DataSource;

/**
 * @author liuxiaowei
 * @description 在Spring 3.1中，只能在类级别上使用@Profile注解，如：@Profile("dev")、@Profile("prod")。不过，
 *              从Spring 3.2开始，你也可以在方法级别上使用@Profile注解，与@Bean注解一同使用。这样的话，就能将这两个bean的声明放到同一个配置类之中，如下所示：
 *
 *              这里有个问题需要注意，尽管每个DataSource bean都被声明在一个profile中，并且只有当规定的profile激活时，相应的bean才会被创建，
 *              但是可能会有其他的bean并没有声明在一个给定的profile范围内。没有指定profile的bean始终都会被创建，与激活哪个profile没有关系。
 * @date 2022/1/14
 */
@Configuration
public class PersistenceConfig {

    @Bean   // 开发环境
    @Profile("dev")
    public DataSource dataSource00() {
        return null;
    }

    @Bean   // 生产环境 通过JNDI获取DataSource能够让容器决定该如何创建这个DataSource，甚至包括切换为容器管理的连接池。
    @Profile("prod")
    public DataSource dataSource01() {
        JndiObjectFactoryBean jndi = new JndiObjectFactoryBean();
        jndi.setJndiName("jdbc/myDS");
        jndi.setResourceRef(true);
        jndi.setProxyInterface(DataSource.class);
        return (DataSource) jndi.getObject();
    }

    @Bean   // qa环境
    @Profile("qa")
    public DataSource dataSource02() {
        return null;
    }
}
