package com.xw.algorithm.leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @author liuxiaowei
 * @description 有效的括号
 * @date 2023/8/28
 */
public class LeetCode0020 {
    public boolean isValid(String s) {
        // 1.判断空字符串
        if (s.isEmpty()) return true;
        // 2.创建辅助栈
        Stack<Character> stack = new Stack<>();
        // 3.仅遍历一次
        for (char c : s.toCharArray()) {
            if (c == '(') {
                stack.push(')');
            }else if(c == '['){
                stack.push(']');
            }else if(c == '{') {
                stack.push('}');
            }else if(stack.isEmpty() || c != stack.pop()){
                return false;
            }
        }
        // 4.返回
        return stack.isEmpty();
    }
}