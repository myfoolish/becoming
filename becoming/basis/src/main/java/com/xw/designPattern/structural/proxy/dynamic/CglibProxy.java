package com.xw.designPattern.structural.proxy.dynamic;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author liuxiaowei
 * @description CGLib (Code Generation Library) 运行时字节码增强技术
 * 当一个类没有实现接口时，Spring AOP会使用CGLib在运行时生成目标继承类字节码的方式进行行为扩展
 * pubic class xxx$$EnhancerBySpringCGLib extend xxx {}
 * @date 2023/12/1
 */
public class CglibProxy implements MethodInterceptor {

    private Object target;

    public Object getInstance(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("CGLib（代理）满大街寻找匹配房源");
        Object result = methodProxy.invokeSuper(object, objects);
        System.out.println("CGLib（代理）售后服务，免费家电维修一年");
        return result;
    }
}
