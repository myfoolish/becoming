package com.xw.algorithm.array;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/21
 */
public interface MyList<E> {

    void add(E e);
    void remove(int i);
    E get(int i);
    int size();                     // 大小
    boolean isEmpty();              // 判断是否为空
    void remove(Object o);
}
