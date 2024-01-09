package com.xw.algorithm.stack;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/21
 */
public interface MyStack<Item> {

    MyStack<Item> push(Item item);  // 入栈
    Item pop();                     // 出栈
    int size();                     // 大小
    boolean isEmpty();              // 判断是否为空
}
