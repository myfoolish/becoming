package com.xw.designPattern.creation.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/8/12
 */
public class Computer {
    List<String> parts = new ArrayList<>();

    public void show() {
        for (String part : parts) {
            System.out.println(part);
        }
    }
}
