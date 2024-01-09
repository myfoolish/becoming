package com.xw.designPattern.creation.singleton;

/**
 * @author liuxiaowei
 * @description 单例-e汉模式：实例在类装载时进行创建
 * 线程安全
 *  若私有构造函数有太多处理，可能会引起性能问题
 *  若没有使用则会造成资源浪费
 * 使用静态块，注意静态块的顺序
 * @date 2022/3/11
 */
public class SingletonHungry1 {
    // 私有本类的构造函数
    private SingletonHungry1() {
        System.out.println("单例---e汉模式");
    }

    // 创建本类对象
    private static SingletonHungry1 singleton = null;

    static {
        singleton = new SingletonHungry1();
    }

    public static SingletonHungry1 getSingleton() {
        System.out.println("对外提供获取本类对象的方法");
        return singleton;
    }
}
