package com.xw.designPattern.creation.factory.factorymethod;

import com.xw.designPattern.creation.factory.noodles.BanNoodles;
import com.xw.designPattern.creation.factory.noodles.Noodles;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/16
 */
public class BanNoodlesFactory implements NoodlesFactory{
    @Override
    public Noodles createNoodles() {
        return new BanNoodles();
    }
}
