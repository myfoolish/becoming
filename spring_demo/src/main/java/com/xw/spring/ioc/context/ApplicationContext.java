package com.xw.spring.ioc.context;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/4
 */
public interface ApplicationContext {
    public Object getBean(String name);
}
