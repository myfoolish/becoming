package com.xw.springinaction.beanscope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author liuxiaowei
 * @description 单例（Singleton）：在整个应用中，只创建bean的一个实例。
 * @date 2022/1/25
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BeanScopeSingleton {
}
