package com.xw.algorithm.sort;

import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description 冒泡排序
 *  不使用第三个变量进行数值交换原理
 *      假设a = 2, b = 3, 使之达到a = 3, b = 2
 *      用加减
 *      a = a + b => a = 2 + 3 = 5
 *      b = a - b => b = 5 - 3 = 2
 *      a = a - b => a = 5 - 2 = 3
 * @date 2023/10/27
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] data = {7, 4, 5, 6, 3, 2,8, 1};
        for (int i = 0; i < data.length - 1; i++) { // 排序的次数
            boolean flag = false;   // 在原先基础上优化
            for (int j = 0; j < data.length - 1 - i; j++) { // data.length - 1 - i 前面i个不用排序
                if (data[j] > data[j + 1]) {
                    // 使用了第三个变量 temp
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;

                    // 不使用第三个变量
//                    data[j] = data[j] + data[j + 1];
//                    data[j + 1] = data[j] - data[j + 1];
//                    data[j] = data[j] - data[j + 1];

                    flag = true;
                }
            }
            if (!flag) break;
        }
        System.out.println(Arrays.toString(data));
    }
}
