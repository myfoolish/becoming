package com.xw.designPattern.structural.proxy;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/8/12
 */
public class RentingHouseProxy implements RentingHouse{

    private RentingHouse rentingHouse;

    public RentingHouseProxy(RentingHouse rentingHouse) {
        this.rentingHouse = rentingHouse;
    }

    @Override
    public void rentingHouse() {
        System.out.println("静态（代理）满大街寻找匹配房源");
        rentingHouse.rentingHouse();
        System.out.println("静态（代理）售后服务，免费家电维修一年");
    }
}
