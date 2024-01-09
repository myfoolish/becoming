package com.xw.thread;

/**
 * @author liuxiaowei
 * @description java.lang.Thread.State 这个枚举中给出了六种线程状态：
 * @date 2020/7/30 17:46
 */

/**
 * NEW(新建)              线程刚被创建，但是并未启动。还没调用start方法。
 * Runnable(可运行)        线程可以在java虚拟机中运行的状态，可能正在运行自己代码，也可能没有，这取决于操作系统处理器。
 * Blocked(锁阻塞)         当一个线程试图获取一个对象锁，而该对象锁被其他的线程持有，则该线程进入Blocked状态；当该线程持有锁时，该线程将变成Runnable状态。
 * Waiting(无限等待)        一个线程在等待另一个线程执行一个（唤醒）动作时，该线程进入Waiting状态。进入这个状态后是不能自动唤醒的，必须等待另一个线程调用notify或者notifyAll方法才能够唤醒。
 * Timed Waiting(计时等待)    同waiting状态，有几个方法有超时参数，调用他们将进入Timed Waiting状态。这一状态将一直保持到超时期满或者接收到唤醒通知。带有超时参数的常用方法有Thread.sleep 、Object.wait。
 * Teminated(被终止)           因为run方法正常退出而死亡，或者因为没有捕获的异常终止了run方法而死亡。
 */
public class ThreadState {

    public static Object object = new Object();

    public static void main(String[] args) {

    // Waiting（无限等待）------------------------------------------------------------
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (object) {
                        try {
                            System.out.println(Thread.currentThread().getName() + "====== 获取到锁对象，调用wait方法，进入waiting状态，释放锁对象");
                            object.wait();  //无限等待
                            // object.wait(5000);  //计时等待 5秒 时间到 自动醒来
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName() + "====== 从waiting状态醒来，获取到锁对象，继续执行了");
                    }
                }
            }
        }, "等待线程").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                while (true) {    // 每隔3秒 唤醒一次
                    try {
                        System.out.println(Thread.currentThread().getName() + "----- 等待三秒钟");
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (object) {
                        System.out.println(Thread.currentThread().getName() + "----- 获取到锁对象,调用notify方法，释放锁对象");
                        object.notify();
                    }
//                }
            }
        }, "唤醒线程").start();


        // Timed Waiting(计时等待) ------------------------------------------------------------
        Thread counter = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 21; i++) {
                    if (i % 10 == 0) {
                        System.out.println("..." + i);
                    }
                    System.out.println(i);
                    try {
                        Thread.sleep(1000);
                        System.out.print("    线程睡眠1秒！\n");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // 计数器线程启动
//        counter.start();
    }
}
