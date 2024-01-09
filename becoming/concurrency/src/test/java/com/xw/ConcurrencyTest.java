package com.xw;

import com.xw.annoations.ThreadNotSafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ThreadNotSafe
public class ConcurrencyTest {

    // 请求总数
    public static int clientTotal = 5000;
    // 允许同时并发执行的线程数
    public static int threadTotal = 200;
    // 定义线程池
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    // 定义信号量（传入允许并发的数目）
    private final Semaphore semaphore = new Semaphore(threadTotal);
    private final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
    
    public static int count = 0;
//    public static volatile int count = 0; // volatile 不能保证线程安全

    public static void add() {
        count++;
    }

    @Test
    public void name() throws InterruptedException {
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(()->{
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("count:{}" + count);
    }
}
