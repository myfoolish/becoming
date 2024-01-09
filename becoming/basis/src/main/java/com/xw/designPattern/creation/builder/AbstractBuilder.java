package com.xw.designPattern.creation.builder;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
public abstract class AbstractBuilder {

    Phone phone;

    abstract void customCpu(String cpu);
    abstract void customMemory(String memory);
    abstract void customDisk(String disk);
    abstract void customCamera(String camera);

    Phone getPhone() {
        return phone;
    }

    public static void main(String[] args) {
        XiaomiBuilder builder = new XiaomiBuilder();
        builder.customCpu("骁龙835");
        builder.customMemory("6G");
        builder.customDisk("64G");
        builder.customCamera("6000w");
        Phone pho = builder.getPhone();
        System.out.println(pho);

        Phone ph = Phone.builder().cpu("骁龙835").memory("6G").disk("64G").camera("6000W").build();
        System.out.println(ph);
    }
}
