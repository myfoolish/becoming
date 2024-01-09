package com.xw.algorithm.sort;

/**
 * @author liuxiaowei
 * @description 动态规划
 * @date 2023/11/7
 */
public class DynamicProgramming {
    public static void main(String[] args) {
        int[] value = {60, 100, 120};
        int[] weight = {10, 20, 40};
//        bagDynamicProgramming(value, weight);

        int[] shopCart = {1, 2, 3, 4, 5, 9};
        cartDynamicProgramming(shopCart);
    }

    /**
     * 背包问题
     * @param value
     * @param weight
     */
    public static void bagDynamicProgramming(int[] value, int[] weight) {
        int n = 3;  // 物品
        int w = 50; // 重量

        int[][] dp = new int[n + 1][w + 1];

        for (int i = 1; i <= n; i++) {  // 每次加的物品
            for (int cw = 1; cw <= w; cw++) {  // 分割的背包
                if (weight[i - 1] <= cw) {  // 能装
                    dp[i][cw] = Math.max(value[i - 1] + dp[i - 1][cw - weight[i - 1]], dp[i - 1][cw]);
                } else {    // 不能装
                    dp[i][cw] = dp[i - 1][cw];
                }
            }
        }
        System.out.println(dp[n][w]);
    }

    public static void cartDynamicProgramming(int[] value) {
        int n = 6;  // 7个物品
        int w = 8;  // 最高8块

        int[][] dp = new int[n + 1][w + 1];

        for (int i = 1; i <= n; i++) {  // 每次加的物品
            for (int cw = 1; cw <= w; cw++) {
                if (value[i - 1] <= cw) {
                    dp[i][cw] = Math.max(value[i - 1] + dp[i - 1][cw - value[i - 1]], dp[i - 1][cw]);
                } else {
                    dp[i][cw] = dp[i - 1][cw];
                }
            }
        }
        System.out.println(dp[n][w]);
    }
}
