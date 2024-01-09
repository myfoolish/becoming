package com.xw;

import com.xw.annoations.ThreadSafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/5/25
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ThreadSafe
public class SynchronizedTest {

    // 修饰代码块，其作用范围是当前 {}，作用对象是调用代码块的对象，不同调用对象之间互不影响
    public void Syn01(int j) {
        synchronized (this) {
            for (int i = 0; i < 100; i++) {
                System.out.println("Syn01 " + j + "==" + i);
            }
        }
    }

    // 修饰方法，其作用范围是整个方法，作用对象是调用代码块的对象
    public synchronized void Syn02(int j) {
        for (int i = 0; i < 100; i++) {
            System.out.println("Syn02 " + j + "==" + i);
        }
    }

    // 修饰静态方法，其作用范围是整个静态方法，作用对象是调用代码块的类的所有对象
    public static synchronized void Syn03(int j) {
        for (int i = 0; i < 100; i++) {
            System.out.println("Syn03 " + j + "==" + i);
        }
    }

    // 修饰一个类，其作用范围是当前 {}，作用对象是调用代码块的类的所有对象
    public static void Syn04(int j) {
        synchronized (SynchronizedTest.class) {
            for (int i = 0; i < 100; i++) {
                System.out.println("Syn04 " + j + "==" + i);
            }
        }
    }

    @Test
    public void test01() {
        SynchronizedTest syn01 = new SynchronizedTest();
        SynchronizedTest syn02 = new SynchronizedTest();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            syn01.Syn03(1);
        });
        executorService.execute(() -> {
            syn02.Syn03(2);
        });
    }

    public class SynchronizedChildTest extends SynchronizedTest {
    }
}
