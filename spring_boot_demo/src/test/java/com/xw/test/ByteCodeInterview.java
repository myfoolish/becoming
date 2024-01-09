package com.xw.test;

import org.junit.Test;

import java.util.Calendar;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/9/25
 */
public class ByteCodeInterview {
    // 面试题：i++ 和 ++i 有什么区别？

    @Test
    public void test01() {
        int i = 10;
//        i++;
        ++i;
        System.out.println(i);
    }
}
