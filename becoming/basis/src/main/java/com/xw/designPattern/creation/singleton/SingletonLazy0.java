package com.xw.designPattern.creation.singleton;

/**
 * @author liuxiaowei
 * @description 单例-懒汉模式：实例在第一次使用时进行创建
 * 线程不安全
 * @date 2022/3/11
 */
public class SingletonLazy0 {
    // 私有本类的构造函数
    private SingletonLazy0() {
        System.out.println("单例---懒汉模式");
    }

    // 创建本类对象
    private static SingletonLazy0 singleton = null;

    public static SingletonLazy0 getSingleton() {
        // 19-21行在多线程下可能会出现线程安全问题
        if (singleton == null) {
            singleton = new SingletonLazy0();
        }
        System.out.println("对外提供获取本类对象的方法");
        return singleton;
    }
}
