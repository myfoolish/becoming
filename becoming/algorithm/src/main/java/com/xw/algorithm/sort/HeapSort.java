package com.xw.algorithm.sort;

import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description 堆排序 用途：1、优先队列；2、TOP K 问题（用户热搜排行榜功能，即微博热搜）
 * @date 2023/12/10
 */
public class HeapSort {

    /**
     * 大顶堆
     * @param data
     * @param start
     * @param end
     */
    public static void maxHeap(int[] data, int start, int end) {
        int parent = start;
        // 下标从0开始的就需要加1，从1开始就不需要
        int left = parent * 2 + 1;   // 左节点、右节点: left+1
        while (left < end) {
            // 定义max表示左右节点大的那一个
            int max = left;
            if (left + 1 < end && data[left] < data[left + 1]) {
                // 表示右节点比左节点大
                max = left + 1;
            }
            // 比较左右节点大的那一个和父节点的大小
            if (data[parent] > data[max]) {
                // 不需要交换直接返回
                return;
            } else {
                int temp = data[parent];
                data[parent] = data[max];
                data[max] = temp;

                parent = max;   // 继续堆化
                left = parent * 2 + 1;
            }
        }
        return;
    }

    /**
     * 堆排序：把最后一个点和堆顶进行交换，然后进行堆化
     * @param data
     */
    public static void heapSort(int[] data) {
        int len = data.length;
        for (int i = len / 2 - 1; i >= 0; i--) {
            maxHeap(data, i, len);
        }
        for (int i = len - 1; i > 0; i--) {
            int temp = data[0];
            data[0] = data[i];
            data[i] = temp;
            maxHeap(data, 0, i);
        }
    }

    public static void main(String[] args) {
        int[] data = {8, 4, 20, 7, 3, 1, 25, 14, 17};
        heapSort(data);
        System.out.println(Arrays.toString(data));
    }
}
