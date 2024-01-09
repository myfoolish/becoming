package com.xw.algorithm.packaged;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2024/1/7
 */
public class DataList {
    public static void main(String[] args) {
        arrayList();
//        linkedList();
    }

    private static void arrayList() {
        List<String> arrayList = new ArrayList<>(2);
        arrayList.add("xw1");
        arrayList.add("xw2");
        arrayList.add("xw3");
        System.out.println(arrayList);
    }

    private static void linkedList() {
        List<String> linkedList = new LinkedList<>();
//        linkedList.add("xw1");
//        linkedList.add("xw2");
//        linkedList.add("xw3");
        System.out.println(linkedList);
    }
}
