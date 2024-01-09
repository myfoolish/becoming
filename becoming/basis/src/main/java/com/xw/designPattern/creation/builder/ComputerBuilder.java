package com.xw.designPattern.creation.builder;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/8/16
 */
public class ComputerBuilder implements Builder{

    private final Computer computer = new Computer();

    @Override
    public void cpu() {
        computer.parts.add("采用Intel的CPU");
    }

    @Override
    public void memory() {
        computer.parts.add("16G 内存");
    }

    @Override
    public void disk() {
        computer.parts.add("1TB 硬盘");
    }

    @Override
    public void main_board() {
        computer.parts.add("顶配主板");
    }

    @Override
    public void display_card() {
        computer.parts.add("顶配显卡");
    }

    @Override
    public Computer getComputer() {
        return computer;
    }
}
