package com.xw.algorithm.sort;

/**
 * @author liuxiaowei
 * @description 《插入排序》 ==> 希尔排序 ==> 归并排序
 * @date 2023/10/12
 */
public class InsertionSort {
    public static void main(String[] args) {
        int[] a = {9, 8, 7, 0, 1, 3, 2};
        for (int i = 1; i < a.length; i++) {    // 为什么i要从1开始？是因为第一个不用排序，把数组从i分开，0～I认为是已经排好序
            int data = a[i];
            int j = i - 1;
            for (; j >= 0; j--) {   // 从尾到头
                if (a[j] > data) {
                    a[j + 1] = a[j];    // 数据往后移动
                } else {
                    break;  // 前面已经是排好序的，所以找到一个比它小的就不用找了，前面肯定更小
                }
            }
            a[j + 1] = data;
            System.out.print("第" + i + "次的排序结果为：");
            for (j = 0; j < a.length; j++) {
                System.out.print(a[j] + " ");
            }
            System.out.println();
        }
    }
}
