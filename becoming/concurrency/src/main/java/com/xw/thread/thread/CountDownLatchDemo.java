package com.xw.thread.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * @author liuxiaowei
 * @description
 *      public CountDownLatch(int count);   // 指定计数的次数，只能被设置1次
 *      public void countDown();            // 调用此方法则计数减1
 *      public void await() throws InterruptedException     // 调用此方法会一直阻塞当前线程，直到计时器的值为0，除非线程被中断。
 *      public Long getCount();             // 得到当前的计数
 *      public boolean await(long timeout, TimeUnit unit)   // 调用此方法会一直阻塞当前线程，直到计时器的值为0，除非线程被中断或者计数器超时，返回false代表计数器超时。
 * @date 2022/7/6
 */
public class CountDownLatchDemo {
    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static class Worker extends Thread {
        String workerName;
        int workTime;
        CountDownLatch latch;

        public Worker(String workerName, int workTime, CountDownLatch latch) {
            this.workerName = workerName;
            this.workTime = workTime;
            this.latch = latch;
        }

        @Override
        public void run() {
            System.out.println("Worker " + workerName + " do work begin at " + format.format(new Date()));
            doWork();   // 工作了
            System.out.println("Worker " + workerName + " do work complete at " + format.format(new Date()));
            latch.countDown();  // 工人完成工作，计数器减一
        }

        private void doWork() {
            try {
                Thread.sleep(workTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);   // 两个工人的协作
        Worker worker1 = new Worker("xw", 5000, latch);
        Worker worker2 = new Worker("coding", 8000, latch);
        worker1.start();
        worker2.start();
        latch.await();  // 等待所有工人完成工作
        System.out.println("all work done at " + format.format(new Date()));
    }
}
