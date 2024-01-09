package com.xw.algorithm.queue;

/**
 * @author liuxiaowei
 * @description 队列
 * @date 2023/9/22
 */
public class ArrayQueue<Q> implements MyQueue<Q> {
    private int data[];
    private int head;
    private int tail;
    private int capacity;  // 当前已存在数据的大小

    public ArrayQueue(int capacity) {
        this.data = new int[capacity];
        this.head = 0;
        this.tail = 0;
        this.capacity = capacity;
    }

    /**
     * 入队列
     * @param m
     */
    public void push(int m) {
        // 判断这个队列是不是满了
        if (tail == capacity) {
            return;
        }
        data[tail] = m;
        tail++;
    }

    /**
     * 出队列
     * @return
     */
    public int pop() {
        if (isEmpty()) {
            return -1;  // 表示空
        }
        int m = data[head];
        head++;
        return m;
    }

    public boolean isEmpty() {
        if (head == tail) {
            return true;
        }
        return false;
    }
}
