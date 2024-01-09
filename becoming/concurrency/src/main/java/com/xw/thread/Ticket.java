package com.xw.thread;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/7/29 16:57
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程安全问题都是由全局变量及静态变量引起的。
 *      若每个线程中对全局变量、静态变量只有读操作，而无写操作，一般来说，这个全局变量是线程安全的；
 *      若有多个线程同时执行写操作，一般都需要考虑线程同步，否则的话就可能影响线程安全。
 */

public class Ticket implements Runnable {

    // 模拟卖票
    private int ticket = 100;
//    @Override
//    public void run() {
//        while (true) {
//            if (ticket > 0) {
//                try {
//                    // 使用sleep模拟一下出票时间
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                //获取当前线程的名字
//                String name = Thread.currentThread().getName();
//                System.out.println(name + "正在卖：" + ticket--);
//            }
//        }
//    }

    /**
     * 线程同步
     * 当我们使用多个线程访问同一资源的时候，且多个线程中对资源有写的操作，就容易出现线程安全问题。
     * 要解决上述多线程并发访问一个资源的安全性问题:也就是解决重复票与不存在票问题，Java中提供了同步机制(synchronized)来解决。
     *      1. 同步代码块：synchronized 关键字可以用于方法中的某个区块中，表示只对这个区块的资源实行互斥访问
     *          格式：synchronized(同步锁){
     *                  需要同步操作的代码
     *                   }
     *         同步锁: 对象的同步锁只是一个概念,可以想象为在对象上标记了一个锁.
     *              1. 锁对象 可以是任意类型。
     *              2. 多个线程对象 要使用同一把锁。
     *      2. 同步方法:使用synchronized修饰的方法,就叫做同步方法,保证A线程执行该方法的时候,其他线程只能在方法外等着。
     *          public synchronized void method(){
     *              可能会产生线程安全问题的代码
     *          }
     *          同步锁是谁?
     *              对于非static方法,同步锁就是this。
     *              对于static方法,我们使用当前方法所在类的字节码对象(类名.class)。
     *      3. 锁机制  java.util.concurrent.locks.Lock 机制提供了比synchronized代码块和synchronized方法更广泛的锁定操作,同步代码块/同步方法具有的功能Lock都有,除此之外更强大,更体现面向对象。
     *          Lock锁也称同步锁，加锁与释放锁方法化了，如下：
     *              public void lock() :加同步锁。
     *              public void unlock() :释放同步锁。
     */

// 1、同步代码块  ----------------------------------------------------------------------------------------------------
/**    Object lock = new Object();
    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                if (ticket > 0) {
                    try {
                        // 使用sleep模拟一下出票时间
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //获取当前线程的名字
                    String name = Thread.currentThread().getName();
                    System.out.println("同步代码块：" + name + "正在卖：" + ticket--);
                }
            }
        }
    }*/

// 2、同步方法   ----------------------------------------------------------------------------------------------------
/**
    @Override
    public void run() {
        while (true) {
            sellTicket();
        }
    }

    public synchronized void sellTicket() {
        if (ticket > 0) {
            try {
                // 使用sleep模拟一下出票时间
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //获取当前线程的名字
            String name = Thread.currentThread().getName();
            System.out.println("同步方法：" + name + "正在卖：" + ticket--);
        }
    }
*/

// 3、Lock锁  ----------------------------------------------------------------------------------------------------
    Lock lock = new ReentrantLock();
    @Override
    public void run() {
        while (true) {
            lock.lock();
            if (ticket > 0) {
                try {
                    // 使用sleep模拟一下出票时间
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //获取当前线程的名字
                String name = Thread.currentThread().getName();
                System.out.println("Lock锁:" + name + "正在卖：" + ticket--);
            }
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        // 创建线程任务对象
        Ticket ticket = new Ticket();
        // 创建三个窗口对象
        Thread thread1 = new Thread(ticket, "窗口1");
        Thread thread2 = new Thread(ticket, "窗口2");
        Thread thread3 = new Thread(ticket, "窗口3");

        // 同时卖票
        thread1.start();
        thread2.start();
        thread3.start();
    }
}
