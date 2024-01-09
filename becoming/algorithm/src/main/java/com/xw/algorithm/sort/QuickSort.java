package com.xw.algorithm.sort;

import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description 快速排序 简称：快排
 * @date 2023/10/29
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] data = {9, 5, 6, 8, 0, 3, 7, 1};
        quickSort(data, 0, data.length - 1);
        System.out.println(Arrays.toString(data));
    }

    public static void quickSort(int[] data, int left, int right) {
        int base = data[left];  // 基准数，此处取序列第一个
        int left1 = left;   // 表示从左边开始查找的位置
        int right1 = right; // 表示从右边开始查找的位置
        while (left1 < right1) {
            // 从后面往前找比基准数小的数
            while (left1 < right1 && data[right1] >= base) {
                right1--;
            }
            // 此处表示有找到比基准数大的数
            if (left1 < right1) {
                int temp = data[right1];
                data[right1] = data[left1];
                data[left1] = temp;
                left1++;
            }
            // 从前面往前找比基准数大的数
            while (left1 < right1 && data[left1] <= base) {
                left1++;
            }
            if (left1 < right1) {
                int temp = data[right1];
                data[right1] = data[left1];
                data[left1] = temp;
                right1--;
            }
        }

        // 此处递归已被基准数分成了三部分
        if (left < left1) {
            quickSort(data, left, left1 - 1);
        }
        if (left1 < right) {
            quickSort(data, left1 + 1, right);
        }
    }
}
