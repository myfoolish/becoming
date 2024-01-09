package com.xw.thread.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxiaowei
 * @description
 *      wait()/notify()存在的问题：1.过于底层，并且不好控制、2.存在过早唤醒的情况、3.wait(long) 无法区分是等待超时还是被通知线程唤醒
 *      因此出现了Condition接口，它作为wait/notify的替代品，解决了过早唤醒的情况，并且解决了wait(long)不能区分其返回是否由等待超时而导致的问题
 *      Condition中的await()、signal()以及signalAll()分别替代wait()、notify()以及notifyAll()
 *      不同的是，Object中的wait(),notify(),notifyAll()方法是和"同步锁"(synchronized 关键字)捆绑使用的；而Condition是需要与"互斥锁"/"共享锁"捆绑使用的
 *
 *      void await()    造成当前线程在接到信号或被中断之前一直处于等待状态
 *      boolean await(long time, TimeUnit unit) 造成当前线程在接到信号、被中断或到达指定等待时间之前一直处于等待状态
 *      long awaitNanos(long nanosTimeout)  造成当前线程在接到信号、被中断或到达指定等待时间之前一直处于等待状态
 *      void awaitUninterruptibly() 造成当前线程在接到信号之前一直处于等待状态
 *      boolean awaitUntil(Date deadline)   造成当前线程在接到信号、被中断或到达指定最后期限之前一直处于等待状态
 *      void signal()   唤醒一个等待线程
 *      void signalAll()    唤醒所有等待线程
 * @date 2022/7/5
 */
public class AwaitSignalDemo {

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) {
        Thread awaitSignalThread = new AwaitSignalThread("awaitSignalThread");
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName()+" start awaitSignalThread");
            awaitSignalThread.start();

            System.out.println(Thread.currentThread().getName()+" block");
            condition.await();

            System.out.println(Thread.currentThread().getName()+" continue");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static class AwaitSignalThread extends Thread{
        public AwaitSignalThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            // 获取锁🔒
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + " wakeup others");
                // 唤醒“condition所在锁上的其它线程”
                condition.signal();
            }finally {
                // 释放锁🔒
                lock.unlock();
            }
        }
    }
}
