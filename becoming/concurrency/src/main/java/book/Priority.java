package book;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/13
 */
public class Priority {
    private static volatile boolean notStart = true;
    private static volatile boolean notEnd = true;

    public static void main(String[] args) throws Exception {
        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int priority = i < 5 ? Thread.MIN_PRIORITY : Thread.MAX_PRIORITY;
            Job job = new Job(priority);
            jobs.add(job);
            Thread thread = new Thread(job, "Thread:" + i);
            thread.setPriority(priority);
            thread.start();
        }
        notStart = false;
        TimeUnit.SECONDS.sleep(10);
        notEnd = false;
        for (Job job : jobs) {
            System.out.println("Job Priority : " + job.priority + ", Count : " + job.jobCount);
        }
    }

    static class Job implements Runnable {
        private int priority;
        private long jobCount;

        public Job(int priority) {
            this.priority = priority;
        }

        public void run() {
            while (notStart) {
                Thread.yield();
            }
            while (notEnd) {
                Thread.yield();
                jobCount++;
            }
        }
    }

    // Job Priority : 1, Count : 1259592
    // Job Priority : 1, Count : 1260717
    // Job Priority : 1, Count : 1264510
    // Job Priority : 1, Count : 1251897
    // Job Priority : 1, Count : 1264060
    // Job Priority : 10, Count : 1256938
    // Job Priority : 10, Count : 1267663
    // Job Priority : 10, Count : 1260637
    // Job Priority : 10, Count : 1261705
    // Job Priority : 10, Count : 1259967
}
