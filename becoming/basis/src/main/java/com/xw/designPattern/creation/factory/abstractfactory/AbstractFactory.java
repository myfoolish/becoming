package com.xw.designPattern.creation.factory.abstractfactory;

import com.xw.designPattern.creation.factory.car.Car;
import com.xw.designPattern.creation.factory.noodles.Noodles;
import com.xw.designPattern.creation.factory.sender.Sender;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
public abstract class AbstractFactory {
    abstract Car createCar();
    abstract Noodles createNoodles();
    abstract Sender send();

    public static void main(String[] args) {
        FerrariAbstractFactory ferrariAbstractFactory = new FerrariAbstractFactory();
        Car car = ferrariAbstractFactory.createCar();
        car.desc();

        SmsAbstractFactory smsAbstractFactory = new SmsAbstractFactory();
        Sender send = smsAbstractFactory.send();
        send.send();
    }
}
