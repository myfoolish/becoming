package com.xw.designPattern.creation.builder;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/8/12
 */
public interface Builder {
    void cpu();
    void memory();
    void disk();
    void main_board();
    void display_card();
    Computer getComputer();


    /**
     * 使用建造者模式可以使客户端不必知道产品内部组成的细节
     * @param args
     */
    public static void main(String[] args) {
        // 模拟组装电脑
        ComputerBuilder builder = new ComputerBuilder();
        Director director = new Director();
        director.construct(builder);
        Computer computer = builder.getComputer();
        computer.show();
    }
}
