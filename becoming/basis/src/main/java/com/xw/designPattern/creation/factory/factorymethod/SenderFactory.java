package com.xw.designPattern.creation.factory.factorymethod;

import com.xw.designPattern.creation.factory.sender.Sender;

/**
 * @author liuxiaowei
 * @description 工厂方法模式
 * @date 2023/6/16
 */
public interface SenderFactory {
    Sender send();

    public static void main(String[] args) {
        SenderFactory sendFactory = new MailSenderFactory();
        Sender sender = sendFactory.send();
        sender.send();
    }
}
