package com.xw.publish;

import com.xw.annoations.ThreadNotSafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liuxiaowei
 * @description 对象溢出
 * @date 2023/6/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ThreadNotSafe
public class EscapeTest {
    private int thisCanBeEscape = 0;

    public EscapeTest() {
        new InnerClass();
    }

    private class InnerClass {
        private InnerClass() {
            System.out.println("这是一个内部类：" + EscapeTest.this.thisCanBeEscape);
        }
    }

    @Test
    public void name() {
        new EscapeTest();
    }
}
