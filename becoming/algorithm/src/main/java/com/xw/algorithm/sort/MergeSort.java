package com.xw.algorithm.sort;

import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description 插入排序 ==> 希尔排序 ==> 《归并排序》
 *                稳定       不稳定         稳定
 * @date 2023/10/25
 */
public class MergeSort {
    private static int[] temp;

    public static void main(String[] args) {
        int[] data = {9, 5, 6, 8, 0, 3, 7, 1};
        temp = new int[data.length];
        mergeSort(data, 0, data.length - 1);
        System.out.println(Arrays.toString(data));
    }

    /**
     *
     * @param data  数组
     * @param left  数组左端
     * @param right 数组右端
     */
    public static void mergeSort(int[] data, int left, int right) {
        if (left < right) { // 相等了表示只有一个数了，也就不再需要再拆了
            // 拆分 《递》
            int middle = (left + right) / 2;
            mergeSort(data, left, middle);
            mergeSort(data, middle + 1, right);
            // 合并 《归》
            merge(data, left, middle, right);
        }
    }

    public static void merge(int[] data, int left, int middle, int right) {
//        int[] temp = new int[data.length];

        int left1 = left;           // 表示左边的第一个数的位置
        int right1 = middle + 1;    // 表示右边的第一个数的位置

        int location = left;        // 表示当前所在位置

        while (left1 <= middle && right1 <= right) {
            if (data[left1] < data[right1]) {
                temp[location] = data[left1];
                left1++;
                location++;
            } else {
                temp[location] = data[right1];
                right1++;
                location++;
            }
        }
        while (left1 <= middle) {
            temp[location++] = data[left1++];
        }
        while (right1 <= right) {
            temp[location++] = data[right1++];
        }

        for (int i = left; i <= right; i++) {
            data[i] = temp[i];
        }
    }
}
