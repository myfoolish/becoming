package com.xw.springinaction.beanscope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author liuxiaowei
 * @description 会话（Session）：在Web应用中，为每个会话创建一个bean实例。
 * @date 2022/1/25
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION,proxyMode = ScopedProxyMode.INTERFACES)  // ScopedProxyMode.INTERFACES 解决了将会话或请求作用域的bean注入到单例bean中所遇到的问题。
public class BeanScopeSession {
}
