package com.xw.thread;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/7
 */
@JCStressTest
@Outcome(id = {"0, 0", "0, 1", "1, 1"}, expect = Expect.ACCEPTABLE, desc = "ACCEPTABLE")
@Outcome(id = "1, 0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "ACCEPTABLE_INTERESTING")
@State
public class VolatileDemo {
    int x;
    // volatile 禁止指令重排序
    volatile int y;

    @Actor
    public void actor1() {
        x = 1;
        y = 1;
    }

    @Actor
    public void actor2(II_Result result) {
        result.r1 = y;
        result.r2 = x;
    }
}
