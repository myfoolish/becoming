package com.xw.thread;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/9
 */
public class ThreadPoolExecutorDemo {
    static class MyTask implements Runnable{
        private final String name;
        private final long duration;

        public MyTask(String name) {
            this(name, 0);
        }

        public MyTask(String name, long duration) {
            this.name = name;
            this.duration = duration;
        }

        @Override
        public String toString() {
            return "MyTask{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override
        public void run() {
            try {
                LoggerUtils.get("mtThread").debug("running..." + this);
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * public ThreadPoolExecutor(int corePoolSize,          // 核心线程数目
     *                               int maximumPoolSize,   // 最大线程数目 = （核心线程数目 + 救急线程的最大数目）
     *                               long keepAliveTime,    // 生存时间 -> 救急线程的生存时间。生存时间内没有新任务，此线程资源会释放
     *                               TimeUnit unit,         // 时间单位 -> 救急线程的生存时间单位，如秒、毫秒等
     *                               BlockingQueue<Runnable> workQueue, // 当没有空闲核心线程时，新来任务会加入到此队列排队，队列满会创建救急线程执行任务
     *                                  线程池中常见的阻塞队列
     *                                      1、ArrayBlockingQueue<Runnable>：基于数组结构的有界（可以设置大小）阻塞队列，FIFO
     *                                      2、LinkedBlockingQueue<Runnable>：基于链表（单向链表）结构的有界（可以设置大小）阻塞队列，FIFO
     *                                      3、DelayedWorkQueue<Runnable>：优先级队列，可以保证每次出队的任务都是当前队列中执行时间最靠前的
     *                                      4、SynchronousQueue<Runnable>：不存储元素的阻塞队列，每个插入操作都必须等待一个移出操作
     *
     *                               ThreadFactory threadFactory,       // 线程工厂 -> 可以定制线程对象的创建，例如设置线程名字，是否是守护线程等
     *                               RejectedExecutionHandler handler)  // 拒绝策略 -> 当所有线程都在繁忙，workQueue也放满时，会触发拒绝策略
     *                                  new ThreadPoolExecutor.AbortPolicy()            直接抛出异常，默认拒绝策略
     *                                  new ThreadPoolExecutor.CallerRunsPolicy()       用调用者所在的线程来执行任务
     *                                  new ThreadPoolExecutor.DiscardOldestPolicy()    丢弃阻塞队列中最靠前的任务，并执行当前任务
     *                                  new ThreadPoolExecutor.DiscardPolicy()          直接丢弃任务
     *
     */

    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(1);

        // 阻塞队列
        ArrayBlockingQueue<Runnable> arrayBlockingQueue = new ArrayBlockingQueue<>(2);
        LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>(2);
        SynchronousQueue<Runnable> synchronousQueue = new SynchronousQueue<>();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 3, 0,
                TimeUnit.SECONDS,
                arrayBlockingQueue,
                r -> new Thread(r, "myThread" + atomicInteger.getAndIncrement()),
                new ThreadPoolExecutor.CallerRunsPolicy());
        showState(arrayBlockingQueue, threadPool);
        // 以下依次放开两个测试
        threadPool.submit(new MyTask("1", 3600000));
        showState(arrayBlockingQueue, threadPool);
        threadPool.submit(new MyTask("2", 3600000));
        showState(arrayBlockingQueue, threadPool);
        threadPool.submit(new MyTask("3"));
        showState(arrayBlockingQueue, threadPool);
        threadPool.submit(new MyTask("4"));
        showState(arrayBlockingQueue, threadPool);
        threadPool.submit(new MyTask("5", 3600000));
        showState(arrayBlockingQueue, threadPool);
        threadPool.submit(new MyTask("6"));
        showState(arrayBlockingQueue, threadPool);
    }

    /**
     * 打印当前线程池的状态
     * @param queue
     * @param threadPool
     */
    private static void showState(ArrayBlockingQueue<Runnable> queue, ThreadPoolExecutor threadPool) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        List<Object> tasks = new ArrayList<>();
        for (Runnable runnable : queue) {
            try {
                Field callable = FutureTask.class.getDeclaredField("callable");
                callable.setAccessible(true);
                Object adapter = callable.get(runnable);
                Class<?> clazz = Class.forName("java.util.concurrent.Executors$RunnableAdapter");
                Field task = clazz.getDeclaredField("task");
                task.setAccessible(true);
                Object o = task.get(adapter);
                tasks.add(o);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        LoggerUtils.main.debug("pool size: {}, queue: {}", threadPool.getPoolSize(), tasks);
    }
}
