package com.xw.algorithm.stack;

/**
 * @author liuxiaowei
 * @description 栈 eg: 1、括号匹配；2、浏览器的前进后退（两个栈，一个前进、一个后退）；3、方法嵌套调用；4、计算期的简单实现（两个栈，一个数字、一个符号）
 * @date 2023/9/21
 */
public class ArrayStack<Item> implements MyStack<Item>{

    private Item[] lArray;  // 数组
    private int lSize = 0;  // 大小（初始元素个数）

    public ArrayStack(int capacity) {
        // 最好开始时设置大小
        lArray = (Item[]) new Object[capacity];
    }

    @Override
    public MyStack<Item> push(Item item) {
        judgeSize();
        lArray[lSize++] = item;
        return null;
    }

    /**
     * 判断是否应该扩容
     */
    private void judgeSize() {
        // 如果元素个数超出数组长度
        if (lSize >= lArray.length) {
            resize(2 * lArray.length);
        } else if (lSize > 0 && lSize <= lArray.length / 2) {
            resize(lArray.length / 2);
        }
    }

    /**
     * 数组扩容
     * @param size
     */
    private void resize(int size) {
        Item[] temp = (Item[]) new Object[size];
        for (int i = 0; i < lSize; i++) {
            temp[i] = lArray[i];
        }
        lArray = temp;
    }

    @Override
    public Item pop() {
        if (isEmpty()) {
            return null;
        }
        Item item = lArray[--lSize];
        // --lSize: 先减了再用；lSize--: 先用了再减
        lArray[lSize] = null;
        return item;
    }

    @Override
    public int size() {
        return lSize;
    }

    @Override
    public boolean isEmpty() {
        return lSize == 0;
    }
}
