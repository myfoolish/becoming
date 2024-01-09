package com.xw.designPattern.creation.factory.sender;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/8/16
 */
public class SmsSender implements Sender{
    @Override
    public void send() {
        System.out.println("这里是短信！");
    }
}
