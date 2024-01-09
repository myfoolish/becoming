package com.xw.thread;

/**
 * @author liuxiaowei
 * @description 死锁：线程t1持有lock1，等待lock2，线程t2吃鱼lock2，等待lock1
 * 1、如何进行死锁诊断？
 *  当程序出现死锁时，
 *  1.1 可以通过jdk自带工具：jps和jstack
 *      jps：输出所有JVM中运行的进程状态信息
 *      jstack：查看java进程中线程的堆栈信息
 *
 *  1.2 其他解决工具，可视化工具：jconsole和visualVM
 *      jconsole：用于对JVM的内存、线程、类的监控，是一个基于jmx的GUI性能监控工具
 *                打开方式：java安装目录bin目录下，直接启动jconsole.exe即可
 *      visualVM：故障处理工具，能够监控线程、内存情况、查看方法的cpu时间和内存中的对象、已被GC的对象、反向查看分配的堆栈
 *                打开方式：java安装目录bin目录下，直接启动jvisualvm.exe即可
 * @date 2023/9/8
 */
public class DeadLockDemo {
    static final Object lock1 = new Object();
    static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " - lock1");
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " - lock2");
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " - lock2");
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + " - lock1");
                }
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}
