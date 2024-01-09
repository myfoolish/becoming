package com.xw.custom;

/**
 * @author liuxiaowei
 * @description 多线程
 *          并行：指两个或多个事件在同一时刻发生（同时发生）。
 *          并发：指两个或多个事件在同一个时间段内发生。
 *      进程：进程指正在运行的程序。确切的来说，当一个程序进入内存运行，即变成一个进程，进程是处于运行过程中的程序，并且具有一定独立功能。
 *      线程：线程是进程中的一个执行单元，负责当前进程中程序的执行，一个进程中至少有一个线程。一个进程中是可以有多个线程的，这个应用程序也可以称之为多线程程序。
 * @date 2020/7/28 11:13
 */

/**
 * Java中通过继承Thread类来创建并启动多线程的步骤如下：
 * 1. 定义Thread类的子类，并重写该类的run()方法，该run()方法的方法体就代表了线程需要完成的任务,因此把run()方法称为线程执行体。
 * 2. 创建Thread子类的实例，即创建了线程对象
 * 3. 调用线程对象的start()方法来启动该线程
 *
 * public String getName() :获取当前线程名称。
 * public void start() :导致此线程开始执行; Java虚拟机调用此线程的run方法。
 * public void run() :此线程要执行的任务在此处定义代码。
 * public static void sleep(long millis) :使当前正在执行的线程以指定的毫秒数暂停（暂时停止执行）。
 * public static Thread currentThread() :返回对当前正在执行的线程对象的引用。
 */
public class CustomThread extends Thread{
    // 定义指定线程名称的构造方法
    public CustomThread(String name) {
        // /调用父类的String参数的构造方法，指定线程的名称
        super(name);
    }

    // 重写run方法，完成该线程执行的逻辑
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(getName() + "正在执行！" + i);
        }
    }

    /**
     * public Thread() :分配一个新的线程对象。
     * public Thread(String name) :分配一个指定名字的新的线程对象。
     * @param args
     */
    public static void main(String[] args) {
        CustomThread customThread = new CustomThread("创建自定义线程！");
        // 开启新线程
        customThread.start();
        for (int i = 0; i < 10; i++) {
            System.out.println("main线程！" + i);
        }
    }
}
