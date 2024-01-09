package com.xw.designPattern.creation.factory.abstractfactory;

import com.xw.designPattern.creation.factory.car.Car;
import com.xw.designPattern.creation.factory.noodles.Noodles;
import com.xw.designPattern.creation.factory.sender.MailSender;
import com.xw.designPattern.creation.factory.sender.Sender;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
public class MailAbstractFactory extends AbstractFactory{
    @Override
    Car createCar() {
        return null;
    }

    @Override
    Noodles createNoodles() {
        return null;
    }

    @Override
    Sender send() {
        return new MailSender();
    }
}
