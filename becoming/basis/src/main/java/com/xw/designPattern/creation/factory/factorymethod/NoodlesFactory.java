package com.xw.designPattern.creation.factory.factorymethod;

import com.xw.designPattern.creation.factory.noodles.Noodles;

/**
 * @author liuxiaowei
 * @description 工厂方法模式
 * @date 2023/6/16
 */
public interface NoodlesFactory {
    Noodles createNoodles();

    public static void main(String[] args) {
        NoodlesFactory noodlesFactory = new HuiNoodlesFactory();
        Noodles noodles = noodlesFactory.createNoodles();
        noodles.desc();
    }
}
