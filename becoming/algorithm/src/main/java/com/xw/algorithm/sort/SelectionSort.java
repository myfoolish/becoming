package com.xw.algorithm.sort;

/**
 * @author liuxiaowei
 * @description 选择排序
 * @date 2023/10/27
 */
public class SelectionSort {
    public static void selectionSort(int[] data) {
        for (int i = 0; i < data.length; i++) {
            int location = i;
            for (int j = i + 1; j < data.length - 1; j++) {
                if (data[j] < data[i]) {
                    location = j;
                }
            }
//            swap(data[i], data[location]);
        }
    }
}
