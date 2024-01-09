package com.xw.thread.thread;

/**
 * @author liuxiaowei
 * @description
 *      1、void wait() 导致当前的线程等待，直到其他线程调用此对象的notify() 方法或 notifyAll() 方法
 *      2、void wait(long timeout) 导致当前的线程等待，直到其他线程调用此对象的notify() 方法或 notifyAll() 方法，或者指定的时间过完
 *      3、void notify() 唤醒在此对象监视器上等待的单个线程
 *      4、void notifyAll() 唤醒在此对象监视器上等待的所有线程
 * @date 2022/7/5
 */
public class WaitNotifyDemo {
    public static void main(String[] args) {
        Thread waitNotifyThread = new WaitNotifyThread("waitNotifyThread");
        // 通过 synchronized(waitNotifyThread) 获取“对象 waitNotifyThread 的同步锁”
        synchronized(waitNotifyThread) {
            try {
                System.out.println(Thread.currentThread().getName()+" start waitNotifyThread");
                waitNotifyThread.start();

                System.out.println(Thread.currentThread().getName()+" block");
                // 等待
                waitNotifyThread.wait();

                System.out.println(Thread.currentThread().getName()+" continue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class WaitNotifyThread extends Thread {
        public WaitNotifyThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            // 通过 synchronized(this) 获取“当前对象的同步锁”。这个this代表是内部类 waitNotifyThread 的对象。
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() + " wakeup others");
                // 唤醒“当前对象上的等待线程”
                notify();
            }
        }
    }
    // notify() 与 notifyAll() 区别：
    // notify() 会使等待获取某对象锁的一个线程到 Runnable 状态，但是 notifyAll() 会使所有的线程到 Runnable 状态，
    // 虽然 notifyAll() 方法性能开销略大，但是不存在信号丢失问题，因此优先推荐使用notifyAll()。
}
