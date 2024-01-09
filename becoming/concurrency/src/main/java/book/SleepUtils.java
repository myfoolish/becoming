package book;

import java.util.concurrent.TimeUnit;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/14
 */
public class SleepUtils {
    public static final void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }
}
