package com.xw.algorithm.sort;

import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description 希尔排序
 * @date 2023/10/27
 */
public class ShellSort {
    public static void main(String[] args) {
        int[] data = {9, 8, 7, 0, 1, 3, 2};
        shellSort(data);
        System.out.println(Arrays.toString(data));
    }

    public static void shellSort(int[] arrays) {
        for (int gap = arrays.length / 2; gap >= 1; gap /= 2) {   // 分组
            for (int i = gap; i < arrays.length; i++) {
                int data = arrays[i];
                int j = i - gap;
                for (; j >= 0; j -= gap) {   // 从尾到头
                    if (arrays[j] > data) {
                        arrays[j + gap] = arrays[j];    // 数据往后移动
                    } else {
                        break;  // 前面已经是排好序的，所以找到一个比它小的就不用找了，前面肯定更小
                    }
                }
                arrays[j + gap] = data;
            }
        }
    }
}
