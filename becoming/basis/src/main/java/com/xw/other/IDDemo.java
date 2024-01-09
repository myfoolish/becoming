package com.xw.other;

import java.util.UUID;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/16
 */
public class IDDemo {
    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        UUID uuid1 = UUID.randomUUID();
        System.out.println(uuid);
        System.out.println(uuid1);
    }
}
