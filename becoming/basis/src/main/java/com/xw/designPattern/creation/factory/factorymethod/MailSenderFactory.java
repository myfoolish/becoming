package com.xw.designPattern.creation.factory.factorymethod;

import com.xw.designPattern.creation.factory.sender.MailSender;
import com.xw.designPattern.creation.factory.sender.Sender;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/16
 */
public class MailSenderFactory implements SenderFactory {

    @Override
    public Sender send() {
        return new MailSender();
    }
}
