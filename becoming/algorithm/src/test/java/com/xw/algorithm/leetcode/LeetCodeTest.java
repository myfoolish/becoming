package com.xw.algorithm.leetcode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/8/28
 */
@Slf4j
public class LeetCodeTest {
    @Test
    public void leetCode0001() {
        int[] nums = {2, 7, 11, 15};
        int target = 17;
        LeetCode0001 l = new LeetCode0001();
        log.info(Arrays.toString(l.twoSum(nums, target)));
    }

    @Test
    public void leetCode0001_1() {
        int[] nums = {2, 7, 11, 15};
        int target = 17;
        LeetCode0001 l = new LeetCode0001();
        log.info(Arrays.toString(l.twoSum1(nums, target)));
    }

    @Test
    public void leetCode0020() {
        String s = "([)]";
        LeetCode0020 l20 = new LeetCode0020();
        boolean valid = l20.isValid(s);
        log.info("valid: {}", valid);
    }
}
