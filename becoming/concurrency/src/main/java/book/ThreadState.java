package book;

/**
 * @author liuxiaowei
 * @description 运行该示例，打开终端或者命令提示符，键入“jps”
 *              再键入“jstack ID”（这里的进程ID为上面键入jps得出的ID一致）
 * @date 2023/7/14
 */
public class ThreadState {
    public static void main(String[] args) {
        new Thread(new TimeWaiting(),"TimeWaitingThread").start();  // TimeWaitingThread线程处于超时等待
        new Thread(new Waiting(), "WaitingThread").start();         // WaitingThread线程在Waiting实例上等待
        // 使用两个Blocked线程，一个获取锁成功，另一个被阻塞
        new Thread(new Blocked(),"BlockedThread-1").start();    // BlockedThread-1线程获取到了Blocked.class的锁
        new Thread(new Blocked(),"BlockedThread-2").start();    // BlockedThread-2线程阻塞在获取Blocked.class示例的锁上
    }

    // 该线程不断地进行睡眠
    static class TimeWaiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                SleepUtils.second(100);
            }
        }
    }

    // 该线程在Waiting.class实例上等待
    static class Waiting implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (Waiting.class) {
                    try {
                        Waiting.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 该线程在Blocked.class实例上加锁后，不会释放该锁
    static class Blocked implements Runnable {
        @Override
        public void run() {
            synchronized (Blocked.class) {
                while (true) {
                    SleepUtils.second(100);
                }
            }
        }
    }
}
