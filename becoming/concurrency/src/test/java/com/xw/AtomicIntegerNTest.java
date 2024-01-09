package com.xw;

import com.xw.annoations.ThreadSafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/3/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ThreadSafe
public class AtomicIntegerNTest {

    private final AtomicReference<Integer> count1 = new AtomicReference<>(0);

    @Test
    public void AtomicReferenceTest() {
        count1.compareAndSet(0, 1);
        count1.compareAndSet(0, 2);
        count1.compareAndSet(1, 3);
        count1.compareAndSet(2, 4);
        System.out.printf("count1: " + count1.get());
    }

    /**
     * ==========**********==========**********==========**********==========**********==========**********==========
     * 必须 volatile 修饰且不能是 static
     */
    public volatile int count = 100;

    private static final AtomicIntegerFieldUpdater<AtomicIntegerNTest> count2 = AtomicIntegerFieldUpdater
            .newUpdater(AtomicIntegerNTest.class, "count");

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private static final AtomicIntegerNTest updaterTest = new AtomicIntegerNTest();

    @Test
    public void AtomicIntegerFieldUpdaterTest() {
        if (count2.compareAndSet(updaterTest, 100, 120)) {
            System.out.printf("update success." + updaterTest.getCount() + "\n");
        }
        if (count2.compareAndSet(updaterTest, 100, 120)) {
            System.out.printf("update success。" + updaterTest.getCount());
        } else {
            System.out.printf("update failed." + updaterTest.getCount());
        }
    }
}
