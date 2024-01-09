package com.xw.algorithm.leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/5/25
 */
public class LeetCodeDemo {
    /**
     * 两数之和
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        int n = nums.length;
        for (int i = 0; i < n; ++i) {
            for (int j = i + 1; j < n; ++j) {
                if (nums[i] + nums[j] == target) {
                    System.out.println(i + "==" + j);
                    return new int[]{i, j};
                }
            }
        }
        return new int[0];
    }

    public static void main(String[] args) {
//        int[] ints={2,3,4,5,6,7};
//        twoSum(ints,9);
        int i = lengthOfLongestSubstring("qwere");
    }

    public static int lengthOfLongestSubstring(String s) {
        int start = 0, length = 0;
        Map<Character,Integer> map = new HashMap<>();
        // int sLen = s.length();
        for (int i = 0; i < s.length(); i++) {
            char at = s.charAt(i);
            if (map.containsKey(at)) {
                start= Math.max(start, map.get(at) + 1);
                int integer = map.get(at);
            }
            length = Math.max(length, i - start + 1);
            map.put(at, i);
        }

        return length;
    }
}
