package com.xw.designPattern.structural.proxy;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/8/12
 */
public class RentingHouseImpl implements RentingHouse {
    @Override
    public void rentingHouse() {
        System.out.println("我要租用一室一厅的房子");
    }
}
