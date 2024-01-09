package com.xw.thread.thread;

/**
 * @author liuxiaowei
 * @description
 *      当一个线程希望等待另外一个或多个线程运行结束时可以使用。
 *      public final void join() throws InterruptedException
 *      public void join(long millis) throws InterruptedException  等待多少秒
 *      public final void join(long millis, int nanos) throws InterruptedException
 * @date 2022/7/5
 */
public class JoinDemo {
    public class Boss implements Runnable{
        @Override
        public void run() {
            System.out.println("当前线程为：" + Thread.currentThread().getName() + "老板给工人发工资");
        }
    }

    public static class Worker implements Runnable{
        @Override
        public void run() {
            System.out.println("当前线程为：" + Thread.currentThread().getName() + "工人准备干活喽*****");
            try {
                //模拟工人干活所花费的时间
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("当前线程为：" + Thread.currentThread().getName() + "工人干活结束*****");
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread worker1 = new Thread(new Worker(), "工人1线程");
        Thread worker2 = new Thread(new Worker(), "工人2线程");
        Thread worker3 = new Thread(new Worker(), "工人3线程");
        // worker 与 boss 一个为 static，一个不为 static，只是为了展示下不同的写法
        Thread boss = new Thread(new JoinDemo().new Boss(), "boss线程");
        worker1.start();
        worker2.start();
        worker3.start();
        // 等待工人干完活，老板才能给钱啊，可恶的资本主义。。。
        worker1.join();
        worker2.join();
        worker3.join();
        boss.start();
    }
}
