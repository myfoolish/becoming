package com.xw.designPattern.structural.proxy;

import java.lang.reflect.*;

/**
 * @author liuxiaowei
 * @description 接口：租房
 * @date 2022/8/12
 */
public interface RentingHouse {
    public void rentingHouse();

    public static void main(String[] args) {
        RentingHouse rentingHouse = new RentingHouseImpl();
        // 自己要租用一个一室一厅的房子
//        rentingHouse.rentingHouse();
        // 静态代理
//        RentingHouse proxy = new RentingHouseProxy(rentingHouse);

        /**
         * JDK动态代理 基于接口动态创建指定的代理类
         * public static Object newProxyInstance(ClassLoader loader, Class<?>[] interfaces, InvocationHandler h){}
         */
//        RentingHouse proxy = (RentingHouse) Proxy.newProxyInstance(RentingHouse.class.getClassLoader(), new Class[]{RentingHouse.class}, new JDKProxy(rentingHouse));
        // 好像还可以这样
//        RentingHouse proxy = (RentingHouse) Proxy.newProxyInstance(RentingHouse.class.getClassLoader(), rentingHouse.getClass().getInterfaces(), new JDKProxy(rentingHouse));
        // 一般不使用此方法
        RentingHouse proxy = getProxyClass();

        // CGLib动态代理
//        CglibProxy cglibProxy = new CglibProxy();
//        RentingHouseImpl proxy = (RentingHouseImpl) cglibProxy.getInstance(rentingHouse);
//
        proxy.rentingHouse();
    }

    static RentingHouse getProxyClass() {
        RentingHouse proxy = null;
        try {
            Class<?> proxyClass = Proxy.getProxyClass(RentingHouse.class.getClassLoader(), RentingHouse.class);
            Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
            proxy = (RentingHouse) constructor.newInstance((InvocationHandler) (proxy1, method, args) -> null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return proxy;
    }
}
