package com.xw.custom;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/7/29 14:58
 */

/**
 * 1. 定义Runnable接口的实现类，并重写该接口的run()方法，该run()方法的方法体同样是该线程的线程执行体。
 * 2. 创建Runnable实现类的实例，并以此实例作为Thread的target来创建Thread对象，该Thread对象才是真正的线程对象。
 * 3. 调用线程对象的start()方法来启动线程。
 */
public class CustomRunnable implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
        }
    }

    /**
     * public Thread(Runnable target) :分配一个带有指定目标新的线程对象。
     * public Thread(Runnable target,String name) :分配一个带有指定目标新的线程对象并指定名字。
     * @param args
     */
    public static void main(String[] args) {
        // 创建自定义类对象
        CustomRunnable runnable = new CustomRunnable();
        // 线程任务对象
        Thread thread = new Thread(runnable, "Passerby");
        //开启线程
        thread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("MyFoolish" + i);
        }
    }
}
