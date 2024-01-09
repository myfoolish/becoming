package com.xw.designPattern.creation.singleton;

/**
 * @author liuxiaowei
 * @description 单例-e汉模式：实例在类装载时进行创建
 * 线程安全
 *  若私有构造函数有太多处理，可能会引起性能问题
 *  若没有使用则会造成资源浪费
 * @date 2022/3/11
 */
public class SingletonHungry0 {
    // 私有本类的构造函数
    private SingletonHungry0() {
        System.out.println("单例---e汉模式");
    }

    // 创建本类对象
    private static SingletonHungry0 singleton = new SingletonHungry0();

    public static SingletonHungry0 getSingleton() {
        System.out.println("对外提供获取本类对象的方法");
        return singleton;
    }
}
