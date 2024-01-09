package com.xw.designPattern.creation.builder;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/8/16
 */
public class Director {
    public void construct(Builder builder){
        builder.cpu();
        builder.memory();
        builder.disk();
        builder.main_board();
        builder.display_card();
    }
}
