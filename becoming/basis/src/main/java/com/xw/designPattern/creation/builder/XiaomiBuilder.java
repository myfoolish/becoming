package com.xw.designPattern.creation.builder;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
public class XiaomiBuilder extends AbstractBuilder{

    public XiaomiBuilder() {
//        phone = new Phone();
        phone = Phone.builder().build();
    }

    @Override
    void customCpu(String cpu) {
        phone.setCpu(cpu);
    }

    @Override
    void customMemory(String memory) {
        phone.setMemory(memory);
    }

    @Override
    void customDisk(String disk) {
        phone.setDisk(disk);
    }

    @Override
    void customCamera(String camera) {
        phone.setCamera(camera);
    }
}
