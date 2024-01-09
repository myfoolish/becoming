package com.xw.algorithm.algorithm;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/26
 */
public class Fibonacci {
    public static void main(String[] args) {
        for (int i = 3; i <= 45; i++) {
            long start = System.currentTimeMillis();
//            fab(i);
//            noFab(i);
            noFabUseArray(i);
            long end = System.currentTimeMillis();
            System.out.println(i + ":" + "所花费的时间为：" + (end - start) + "ms");
        }
    }

    // 1 1 2 3 5 8 13
    // f(n)=f(n-1)+f(n-2)
    public static int fab(int n) {
        if (n <= 2) {
            return 1;   // 递归的终止条件
        }
        return fab(n - 1) + fab(n - 2); // 继续递归的过程
    }

    public static int noFab(int n) {    // 不使用递归
        if (n <= 2) {
            return 1;
        }
        int a = 1;
        int b = 1;
        int c = 0;
        for (int i = 3; i <= n; i++) {
            c = a + b;
            a = b;
            b = c;
        }
        return c;
    }

    public static int noFabUseArray(int n) {    // 不使用递归
        if (n <= 2) {
            return 1;
        }
        int[] data = new int[n];
        data[1] = 1;
        data[2] = 1;
        int c = 0;
        for (int i = 3; i <= n; i++) {
            data[i] = data[i - 1] + data[i - 2];
            c = data[i];
        }
        return c;
    }
}
