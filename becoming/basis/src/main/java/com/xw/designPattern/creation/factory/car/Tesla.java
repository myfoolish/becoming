package com.xw.designPattern.creation.factory.car;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
public class Tesla extends Car{
    public Tesla() {
        this.name = "特斯拉";
    }

    @Override
    public void desc() {
        System.out.println("这里是外观好看的" + name);
    }
}
