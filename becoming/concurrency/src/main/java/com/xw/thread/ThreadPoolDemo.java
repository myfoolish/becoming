package com.xw.thread;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/7/28
 *
 * Java里面线程池的顶级接口是 java.util.concurrent.Executor，但是严格意义上讲 Executor 并不是一个线程池，而只是一个执行线程的工具。
 * 真正的线程池接口是 java.util.concurrent.ExecutorService
 *
 * 在 java.util.concurrent.Executors类中提供了大量创建连接池的静态方法，常见的四种：
 *  1、创建指定工作线程数量的线程池
 * 线程工厂类里面提供了一些静态工厂，生成一些常用的线程池。官方建议使用Executors工程类来创建线程池对象。
 * Executors类中有个创建线程池的方法如下：
 *      public static ExecutorService newFixedThreadPool(int nThreads) ：返回线程池对象。(创建的是有界线程池,也就是池中的线程个数可以指定最大数量)
 *      public Future<?> submit(Runnable task) :获取线程池中的某一个线程对象，并执行
 */
public class ThreadPoolDemo {

    // 可缓存线程池
    public static void newCacheThreadPool() {
        // 创建一个可缓存线程池，没有核心线程数，最大线程数为Integer.MAX_VALUE，都是用临时线程来执行任务，
        // 来了新任务之后，首先会判断临时线程是否还存活，若有存活就用临时线程来执行，若没有存活，则去创建临时线程去执行当前的任务
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            cachedThreadPool.execute(() -> {
                System.out.println(Thread.currentThread().getName() + "正在被执行");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        cachedThreadPool.shutdown();
    }

    // 创建指定工作线程数量的线程池
    public static void newFixedThreadPool() {
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + "正在被执行");
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };

        // 创建一个固定大小的线程池，核心线程数和最大线程数都是3，无救急线程
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++) {
            fixedThreadPool.submit(runnable);
            fixedThreadPool.execute(runnable);
        }
        fixedThreadPool.shutdown();
    }

    // 定长的线程池，支持”延迟“及”周期性“任务执行——延迟执行
    public static void newScheduledThreadPool() {
        Runnable runnable = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + "，开始：" + new Date());
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "，结束：" + new Date());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        // 按照周期执行的线程池，核心线程数为传入的参数3，最大线程数是Integer.MAX_VALUE
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(3);
        System.out.println("程序开始：" + new Date());

        scheduledThreadPool.schedule(runnable, 0, TimeUnit.SECONDS);
        scheduledThreadPool.schedule(runnable, 1, TimeUnit.SECONDS);
        scheduledThreadPool.schedule(runnable, 5, TimeUnit.SECONDS);
        scheduledThreadPool.schedule(() -> System.out.println("延迟5秒执行"), 5, TimeUnit.SECONDS);

//        scheduledThreadPool.scheduleWithFixedDelay(() -> {
//            System.out.println("延迟1秒后每3秒执行一次");
//        }, 1, 3, TimeUnit.SECONDS);

        scheduledThreadPool.shutdown();
    }

    // 创建一个单线程化的线程池
    public static void newSingleThreadExecutor() {
        // 创建一个单线程化的线程池，核心线程数和最大线程数都是1，无救急线程
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++) {
            final int index = i;
            singleThreadExecutor.execute(()->{
                try {
                    // 结果依次输出，相当于顺序执行各个任务
                    System.out.println(Thread.currentThread().getName() + "正在被执行,打印的值是:" + index);
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        singleThreadExecutor.shutdown();
    }


    // 自定义连接池
    public static void customThreadPoolExecutor() {
        // 创建数组型缓冲等待队列
        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(10);
        // ThreadPoolExecutor:创建自定义线程池，池中保存的线程数为3，允许最大的线程数为6
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3, 6, 50, TimeUnit.MICROSECONDS, blockingQueue);

        Thread thread1 = new Thread(() -> {
            // 打印正在执行的缓存线程信息
            System.out.println(Thread.currentThread().getName() + "正在被执行");
            try {
                // sleep一秒保证3个任务在分别在3个线程上执行
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            // 打印正在执行的缓存线程信息
            System.out.println(Thread.currentThread().getName() + "正在被执行");
            try {
                // sleep一秒保证3个任务在分别在3个线程上执行
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread3 = new Thread(() -> {
            // 打印正在执行的缓存线程信息
            System.out.println(Thread.currentThread().getName() + "正在被执行");
            try {
                // sleep一秒保证3个任务在分别在3个线程上执行
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 3个任务在分别在3个线程上执行
        threadPoolExecutor.execute(thread1);
        threadPoolExecutor.execute(thread2);
        threadPoolExecutor.execute(thread3);
        // 关闭自定义线程池
        threadPoolExecutor.shutdown();
    }

    public static void threadPoolDemo() {
        /**
         * 使用线程池中线程对象的步骤：
         *      1. 创建线程池对象。
         *      ExecutorService executorService = Executors.newFixedThreadPool(线程数量);
         *      2. 创建Runnable接口子类对象。(task)
         *      3. 提交Runnable接口子类对象。(take task)
         *      4. 关闭线程池(一般不做)。
         */

        // 创建包含2个线程的线程池对象
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 创建Runnable实例对象
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + ": 我要一个教练");
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + ": 教练来了，教我游泳，结束后教练回到了游泳池");
        };

        // 自己创建线程对象
//        Thread thread = new Thread(runnable, "自己创建线程对象");
//        thread.start();

        // 从线程池中获取线程对象，然后调用MyRunnable中的run()
        executorService.submit(runnable);
        // 再获取个线程对象，调用MyRunnable中的run()
        executorService.submit(runnable);
        executorService.submit(runnable);

        // 注意：submit方法调用结束后，程序并不终止，是因为线程池控制了线程的关闭。
        // 将使用完的线程又归还到了线程池中
        // 关闭线程池
        executorService.shutdown();
    }

    public static void main(String[] args) {
//        newCacheThreadPool();
//        newFixedThreadPool();
        newScheduledThreadPool();
//        newSingleThreadExecutor();
//        customThreadPoolExecutor();
//        threadPoolDemo();
        // 查看机器的CPU核数
//        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}
