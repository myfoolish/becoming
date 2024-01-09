package com.xw.designPattern.creation.factory.factorymethod;

import com.xw.designPattern.creation.factory.sender.Sender;
import com.xw.designPattern.creation.factory.sender.SmsSender;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/16
 */
public class SmsSenderFactory implements SenderFactory {
    @Override
    public Sender send() {
        return new SmsSender();
    }
}
