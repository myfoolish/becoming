package com.xw.custom;

import java.util.BitSet;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/8/9
 */
public class CustomBloomFilter {
    // 布隆过滤器长度
    private static final int SIZE = 2 << 10;
    // 模拟实现不同的哈希函数
    private static final int[] num= new int[] {5, 19, 23, 31,47, 71};
    // 初始化位数组
    private BitSet bits = new BitSet(SIZE);
    // 用于存储哈希函数
    private CustomHash[] function = new CustomHash[num.length];
    // 初始化哈希函数
    public CustomBloomFilter() {
        for (int i = 0; i < num.length; i++) {
            function [i] = new CustomHash(SIZE, num[i]);
        }
    }

    // 存值Api
    public void add(String value) {
        // 对存入得值进行哈希计算
        for (CustomHash f: function) {
            // 将为数组对应的哈希下标得位置得值改为1
            bits.set(f.hash(value), true);
        }
    }

    // 判断是否存在该值得Api
    public boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean result= true;
        for (CustomHash f : function) {
            result= result&& bits.get(f.hash(value));
        }
        return result;
    }


    public static class CustomHash{
        private int cap;
        private int seed;

        // 初始化数据
        public CustomHash(int cap, int seed) {
            this.cap = cap;
            this.seed = seed;
        }

        // 哈希函数
        public int hash(String value) {
            int result = 0;
            for (int i = 0; i < value.length(); i++) {
                result = seed * result + value.charAt(i);
            }
            return (cap - 1) & result;
        }
    }

    public static void main(String[] args) {
        String value = "4243212355312";
        CustomBloomFilter bloomFilter = new CustomBloomFilter();
        System.out.println(bloomFilter.contains(value));
        bloomFilter.add(value);
        System.out.println(bloomFilter.contains(value));
    }
}
