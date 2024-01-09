package com.xw.algorithm.list;

/**
 * @author liuxiaowei
 * @description LRU 算法
 *  1) 使用单向链表即可实现
 *  2) 查询数据是否存在，如果存在，删除，然后加入到头部
 *  3) 不存在，插入到头部
 *  4) 如果超过最大长度限制，则删除最后一个，新的数据插入到头部
 * @date 2023/9/22
 */
public class LruAlgorithm<T> {
    private MyLinkedList<T> linkedList = new MyLinkedList<>();

    private int maxSize = 10;

    public void inputData(T data) {

    }
}
