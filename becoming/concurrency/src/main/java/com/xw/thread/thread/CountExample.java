package com.xw.thread.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/1/16
 */
public class CountExample {
    private static final int threadTotal = 200;
    private static final int clientTotal = 5000;
    private static long count = 0;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(threadTotal);
        for (int index = 0; index < clientTotal; index++) {
            executor.execute(()->{
                try {
                    semaphore.acquire();
                    count++;
                    semaphore.release();
                } catch (Exception e) {
                    System.out.println("exception:" + e);
                }
            });
        }
        executor.shutdown();
        System.out.println("count:" + count);
    }
}
