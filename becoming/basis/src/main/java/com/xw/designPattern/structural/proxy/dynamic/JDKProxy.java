package com.xw.designPattern.structural.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author liuxiaowei
 * @description JDK动态代理【通过反射】
 *  JDK提供了java.lang.reflect.InvocationHandler接口和 java.lang.reflect.Proxy类，这两个类相互配合，入口是Proxy
 *  Proxy类【jkd1.2】根据已有接口生成对应的代理类
 * @date 2023/12/1
 */
public class JDKProxy implements InvocationHandler {
    private Object target;  // 目标对象

    public JDKProxy(Object target) {
        this.target = target;
    }

    /**
     * 对目标方法进行增强
     * @param proxy the proxy instance that the method was invoked on
     *
     * @param method the {@code Method} instance corresponding to
     * the interface method invoked on the proxy instance.  The declaring
     * class of the {@code Method} object will be the interface that
     * the method was declared in, which may be a superinterface of the
     * proxy interface that the proxy class inherits the method through.
     *
     * @param args an array of objects containing the values of the
     * arguments passed in the method invocation on the proxy instance,
     * or {@code null} if interface method takes no arguments.
     * Arguments of primitive types are wrapped in instances of the
     * appropriate primitive wrapper class, such as
     * {@code java.lang.Integer} or {@code java.lang.Boolean}.
     *
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("JDK动态（代理）满大街寻找匹配房源");
        // 与spring aop的环绕通知类似 都是基于反射 只不过这里经过aop封装 Object result = proceedingJoinPoint.proceed();
        Object result = method.invoke(target, args);    // 使用java底层的反射机制 调用目标方法
        System.out.println("JDK动态（代理）售后服务，免费家电维修一年");
        return result;
    }
}
