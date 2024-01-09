package com.xw.thread.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/7/6
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        // 线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        // 只能5个线程同时访问
        Semaphore semaphore = new Semaphore(5);
        // 模拟20个客户端访问
        for (int i = 0; i < 20; i++) {
            int num = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        // 获取许可
                        semaphore.acquire();
                        System.out.println("Accessing: " + num);
                        Thread.sleep((long) (Math.random() * 6000));
                        // 释放许可
                        semaphore.release();
                        // 查看还剩多少个许可
                        int permits = semaphore.availablePermits();
                        System.out.println("-----------------" + permits);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            executor.execute(runnable);
        }
        // 退出线程池
        executor.shutdown();
    }
}
