package com.xw.springinaction.beanscope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/1/25
 */
public class BeanScopes {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public BeanScope beanScope() {
        return new BeanScope();
    }
}
