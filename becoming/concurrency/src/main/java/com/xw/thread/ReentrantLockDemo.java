package com.xw.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/7
 */
public class ReentrantLockDemo {
    // 创建锁对象
    static ReentrantLock lock = new ReentrantLock();
    // 条件1
    static Condition c1 = lock.newCondition();
    // 条件2
    static Condition c2 = lock.newCondition();

    public static void main(String[] args) {

        // 可中断的锁
//        lockInterrupt();

        // 可超时的锁
//        timeOutLock();

        // 多条件变量
        conditionT();
    }

    public static void lockInterrupt() {
        Thread t1 = new Thread(() -> {
            try {
                // 开启可中断的锁
                lock.lockInterruptibly();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("等待的过程中被打断");
                return;
            }
            try {
                System.out.println(Thread.currentThread().getName() + "，获得了锁");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        System.out.println("主线程获得了锁");

        t1.start();

        try {
            Thread.sleep(1000);
            t1.interrupt();
            System.out.println("执行打断");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void timeOutLock() {
        Thread t1 = new Thread(() -> {
            try {
                // 尝试获取锁，如果获取锁成功，返回true，否则返回false
                if (!lock.tryLock(3, TimeUnit.SECONDS)) {
                    System.out.println("t1线程-获取锁失败");
                    return;
                }
                System.out.println("t1线程-获取锁成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock();
        System.out.println("主线程获得了锁");

        t1.start();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void conditionT() {
        new Thread(() -> {
            lock.lock();
            try {
                // 进入c1条件的等待
                c1.await();
                System.out.println(Thread.currentThread().getName() + ", acquire lock...");
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        new Thread(() -> {
            lock.lock();
            try {
                // 进入c1条件的等待
                c2.await();
                System.out.println(Thread.currentThread().getName() + ", acquire lock...");
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t2 ").start();

        new Thread(() -> {
            lock.lock();
            try {
                // 唤醒c1条件的线程
                c1.signal();
                // 唤醒c2条件的线程
                c2.signal();
                System.out.println(Thread.currentThread().getName() + ", acquire lock...");
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "t3").start();
    }
}
