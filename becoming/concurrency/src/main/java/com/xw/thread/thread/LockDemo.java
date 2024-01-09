package com.xw.thread.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/6/29
 */
public class LockDemo {

    // 创建一个 Lock 接口实例
    private static final Lock lock = new ReentrantLock(true);
//    private static final Lock lock = new ReentrantLock();

    public static class lockDemo{
        public void test() {
            // 申请锁
            lock.lock();
            try {
                lock.tryLock(100, TimeUnit.SECONDS);
                // 在此对共享数据进行访问
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                // 总是在 finally 块中释放锁，以避免锁泄漏
                lock.unlock();  // 释放锁
            }
        }

        public static void main(String[] args) {
            for (int i = 0; i < 3; i++) {
                System.out.println("测试1");
                lock.lock();
                System.out.println("测试2");
            }
            for (int i = 0; i < 3; i++) {
                try {
                    System.out.println("测试3");
                }finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class fairOrNoFair{
        static class myRunnable implements Runnable {
            Integer id;

            public myRunnable(Integer id) {
                this.id = id;
            }

            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < 2; i++) {
                    lock.lock();
                    System.out.println("获得锁的线程：" + id);
                    lock.unlock();
                }
            }
        }

        public static void main(String[] args) {
            for (int i = 0; i < 5; i++) {
                new Thread(new myRunnable(i), "fair").start();
                // 开启5个线程,让每个线程都获取释放锁两次。为了能更好的观察到结果,在每次获取锁前让线程休眠10毫秒。
                // 可以看到线程几乎是轮流的获取到了锁。如果我们改成非公平锁,再看下结果
//            new Thread(new myRunnable(i), "noFair").start();
                // 线程会重复获取锁。
                // 如果申请获取锁的线程足够多,那么可能会造成某些线程长时间得不到锁。这就是非公平锁的“饥饿”问题。
            }
        }
    }

    /**
     * 当使用 synchronized 实现锁时,阻塞在锁上的线程除非获得锁否则将一直等待下去，也就是说这种无限等待获取锁的行为无法被中断。
     * 而ReentrantLock给我们提供了一个可以响应中断的获取锁的方法 lockInterruptibly()，该方法可以用来解决死锁问题。
     */
    public static class lockInterruptibly{
        static Lock lock1 = new ReentrantLock();
        static Lock lock2 = new ReentrantLock();

        public static void main(String[] args) {
            Thread thread = new Thread(new myRunnable(lock1, lock2));   // 该线程先获取锁1,再获取锁2
            Thread thread1 = new Thread(new myRunnable(lock2, lock1));  // 该线程先获取锁2,再获取锁1
            thread.start();
            thread1.start();
            thread.interrupt(); // 第一个线程中断
        }

        static class myRunnable implements Runnable{
            Lock firstLock;
            Lock secondLock;

            public myRunnable(Lock firstLock, Lock secondLock) {
                this.firstLock = firstLock;
                this.secondLock = secondLock;
            }

            @Override
            public void run() {
                try {
                    firstLock.lockInterruptibly();
                    TimeUnit.MILLISECONDS.sleep(10);    // 更好的触发死锁
                    secondLock.lockInterruptibly();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    firstLock.unlock();
                    secondLock.unlock();
                    System.out.println(Thread.currentThread().getName() + "正常结束!");
                }
            }
        }
    }
}
