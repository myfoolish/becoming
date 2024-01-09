package com.xw.designPattern.creation.factory.car;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
public class Ferrari extends Car {
    public Ferrari() {
        this.name = "法拉利";
    }

    @Override
    public void desc() {
        System.out.println("这里是超级跑车" + name);
    }
}
