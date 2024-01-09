package com.xw.test;

import org.junit.Test;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/8/17 9:58
 */
public class FunctionalInterfaceTest {

    @FunctionalInterface
    public interface MessageBuilder {
        String buildMessage();
    }

    private static void log(int level, MessageBuilder messageBuilder) {
        if (level == 1) {
            System.out.println(messageBuilder.buildMessage());
        }
    }

    @Test
    public void FunctionalInterfaceTest01() {
        String msgA = "Hello";
        String msgB = "World";
//        log(1, new MessageBuilder() {
//            @Override
//            public String buildMessage() {
//                return msgA + msgB;
//            }
//        });
        log(1, () -> msgA + msgB);
        log(2,()->{
            System.out.println("Lambda执行！");
            return msgA + msgB;
        });
    }
}
