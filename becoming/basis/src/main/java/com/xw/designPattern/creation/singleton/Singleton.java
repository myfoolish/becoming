package com.xw.designPattern.creation.singleton;

/**
 * @author liuxiaowei
 * @description 使用枚举初始化实例
 * 线程安全且推荐使用，相比懒汉模式，安全有保证；相比饿汉模式，使用时才加载
 * @date 2022/3/11
 */
public class Singleton {
    // 私有本类的构造函数
    private Singleton() {
        System.out.println("使用枚举初始化实例");
    }

    public static Singleton getSingleton() {
        System.out.println("对外提供获取本类对象的方法");
        return SingletonEnum.SINGLETON.getSingleton();
    }

    private enum SingletonEnum {
        SINGLETON;

        private Singleton singleton;

        // JVM 保证该方法只调用一次
        SingletonEnum() {
            singleton = new Singleton();
        }

        public Singleton getSingleton() {
            return singleton;
        }
    }
}
