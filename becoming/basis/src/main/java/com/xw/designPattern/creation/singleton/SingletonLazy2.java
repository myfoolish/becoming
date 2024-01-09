package com.xw.designPattern.creation.singleton;

/**
 * @author liuxiaowei
 * @description 单例-懒汉模式：实例在第一次使用时进行创建
 * 线程不安全 在多线程下，可能会发生指令重排序
 * Double-checked locking   双重同步锁单例模式
 * @date 2022/3/11
 */
public class SingletonLazy2 {
    // 私有本类的构造函数
    private SingletonLazy2() {
        System.out.println("单例---懒汉模式");
    }

    /**
     * CPU指令层面
     * 当执行 singleton = new SingletonLazy2()时，主要分三步
     * 1、memory = allocate() 分配对象内存空间
     * 2、ctorInstance() 初始化对象
     * 3、singleton = memory 设置singleton指向刚分配的空间
     * 在多线程下，可能会发生指令重排序（JVM和CPU优化进行指令重排序），即由1-2-3变成1-3-2
     */

    // 创建本类对象
    private static SingletonLazy2 singleton = null;

    public static SingletonLazy2 getSingleton() {
        // Double-checked locking
        if (singleton == null) {    // 双层检测机制
            synchronized (SingletonLazy2.class) {   // 锁
                if (singleton == null) {
                    singleton = new SingletonLazy2();
                }
            }
        }
        System.out.println("对外提供获取本类对象的方法");
        return singleton;
    }
}
