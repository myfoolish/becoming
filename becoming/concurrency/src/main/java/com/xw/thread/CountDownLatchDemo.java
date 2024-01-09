package com.xw.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/13
 */
public class CountDownLatchDemo {
    public static void main(String[] args) {
        // 初始化一个倒计时锁，参数为3
        CountDownLatch latch = new CountDownLatch(3);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "- begin...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 计数--
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + "- end..." + latch.getCount());
        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "- begin...");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 计数--
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + "- end..." + latch.getCount());
        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "- begin...");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 计数--
            latch.countDown();
            System.out.println(Thread.currentThread().getName() + "- end..." + latch.getCount());
        }).start();
        System.out.println(Thread.currentThread().getName() + "- waiting...");
        // 等待其他线程完成
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Thread.currentThread().getName() + "- wait end...");
    }
}
