package com.xw.other;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxiaowei
 * @description 如何证明Java的泛型是假的
 *              如果一个整形集合输出出了字符串元素, 则表示此泛型为伪泛型
 * @date 2021/8/3
 */
public class TDemo {
    public static void main(String[] args) {
        List<Integer> list = initList();
        System.out.println("list = " + list);   // list = [hello, world]
    }

    private static <T> List<T> initList() {
        List<T> list = new ArrayList<>();
        list.add((T) "hello, world");
        return list;
    }
}
