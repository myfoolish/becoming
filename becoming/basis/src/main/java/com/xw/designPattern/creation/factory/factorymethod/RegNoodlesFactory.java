package com.xw.designPattern.creation.factory.factorymethod;

import com.xw.designPattern.creation.factory.noodles.Noodles;
import com.xw.designPattern.creation.factory.noodles.RegNoodles;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/16
 */
public class RegNoodlesFactory implements NoodlesFactory{
    @Override
    public Noodles createNoodles() {
        return new RegNoodles();
    }
}
