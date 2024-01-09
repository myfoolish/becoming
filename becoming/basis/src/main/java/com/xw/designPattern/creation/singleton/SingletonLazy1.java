package com.xw.designPattern.creation.singleton;

/**
 * @author liuxiaowei
 * @description 单例-懒汉模式：实例在第一次使用时进行创建
 * 线程安全 <synchronized>可以保证，其原理是同一时间内只允许一个线程访问，可能会引起性能问题，不推荐
 * @date 2022/3/11
 */
public class SingletonLazy1 {
    // 私有本类的构造函数
    private SingletonLazy1() {
        System.out.println("单例---懒汉模式");
    }

    // 创建本类对象
    private static SingletonLazy1 singleton = null;

    public static synchronized SingletonLazy1 getSingleton() {
        if (singleton == null) {
            singleton = new SingletonLazy1();
        }
        System.out.println("对外提供获取本类对象的方法");
        return singleton;
    }
}
