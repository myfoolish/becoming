package com.xw.thread;

/**
 * @author liuxiaowei
 * @description 模拟买票的一个例子 对 synchronized 关键字的底层进行分析
 *      synchronized【对象锁】采用互斥的方式让同一时刻至多只有一个线程能持有【对象锁】，它的底层由monitor（监视器）实现的，
 *      Monitor是由jvm提供，c++语言实现，线程获取到锁需要使用锁对象锁关联monitor
 *      在monitor内部有三个属性：
 *          waitSet：关联调用了wait方法的线程，处于waiting状态的线程
 *          EntryList：关联处于阻塞状态的线程，处于Blocked状态的线程
 *          Owner：关联当前获取锁的线程，并且只有一个线程可以获取
 *
 *      当一个线程进入到synchronized代码块之后，获取到的对象锁与monitor进行关联，
 *      检查monitor中的owner是否为null，若为null，则为当前线程所持有，
 *      若不为null，则进入到EntryList中等待，即阻塞；
 *      若线程调用了wait方法，则进入到waitSet
 * @date 2023/9/6
 */
public class SynchronizedDemo {
    static final Object lock = new Object();
    static int ticketNum = 10;
    public void getTicket() {
        synchronized (lock) {
            if (ticketNum <= 0) {
                return;
            }
            System.out.println(Thread.currentThread().getName() + "抢到一张票，剩余：" + ticketNum);
            // 非原子操作
            ticketNum--;
        }
    }

    public static void main(String[] args) {
        SynchronizedDemo synchronizedDemo = new SynchronizedDemo();
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                synchronizedDemo.getTicket();
            }).start();

            // 使用lambda优化
//            new Thread(ticketDemo::getTicket).start();
        }
    }
}
