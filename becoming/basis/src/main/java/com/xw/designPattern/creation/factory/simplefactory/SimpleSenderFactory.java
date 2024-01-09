package com.xw.designPattern.creation.factory.simplefactory;

import com.xw.designPattern.creation.factory.sender.MailSender;
import com.xw.designPattern.creation.factory.sender.Sender;
import com.xw.designPattern.creation.factory.sender.SmsSender;

/**
 * @author liuxiaowei
 * @description 简单工厂模式
 * @date 2022/8/16
 */
public class SimpleSenderFactory {
    public static final int TYPE_MAIL = 1;   // 邮件
    public static final int TYPE_SMS = 2;    // 短信

    /**
     * 如果生产对象的方法是 static 的，这种简单工厂也叫做静态工厂
     * @param type
     * @return
     */
    public static Sender produce(int type) {
        switch (type) {
            case 1:
                return new MailSender();
            case 2:
                return new SmsSender();
            default:
                System.out.println("输入发送方式有误，默认采用邮件!");
                return new MailSender();
        }
    }

    /**
     * 如果生产对象的方法不是 static 的，这种简单工厂也叫做实例工厂
     * @param type
     * @return
     */
    public Sender produce(String type) {
        if ("mail".equals(type)) {
            return new MailSender();
        } else if ("sms".equals(type)) {
            return new SmsSender();
        } else {
            System.out.println("输入发送方式有误，默认采用邮件!");
            return new MailSender();
        }
    }

    public static void main(String[] args) {
        Sender sender0 = SimpleSenderFactory.produce(1);
        sender0.send();

        SimpleSenderFactory factory = new SimpleSenderFactory();
        Sender sender = factory.produce("mail");
        sender.send();
    }
}
