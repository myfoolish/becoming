package com.xw.other;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/9/27
 */
public class SuperLotto {
    public static void main(String[] args) {
        List<Integer> list01 = new ArrayList<>();
        List<Integer> list02 = new ArrayList<>();
        while (list01.size() < 5) {
            int a = (int) (Math.random() * 35 + 1);
            if (!list01.contains(a)) {
                list01.add(a);
            }
        }
        Collections.sort(list01);
        while (list02.size() < 2) {
            int b = (int) (Math.random() * 12 + 1);
            if (!list02.contains(b)) {
                list02.add(b);
            }
        }
        Collections.sort(list02);
        System.out.println(list01 + "" + list02);
    }
}
