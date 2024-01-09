package com.xw.algorithm.list;

/**
 * @author liuxiaowei
 * @description 循环链表    （用途：Mysql b+树）
 * @date 2023/9/22
 */
public class MyDoubleLinkedList {

    private DoubleListNode head;    // 头
    private DoubleListNode tail;    // 尾

    public MyDoubleLinkedList() {
        this.head = null;
        this.tail = null;
    }

    /**
     * 插入链表头部
     */
    public void insertHead(int date) {
        DoubleListNode newNode = new DoubleListNode(date);
        // 若原来有数据
        if (head == null) {
//            head = newNode;
            tail = newNode;
        } else {
            head.pre = newNode;
            newNode.next = head;
//            head = newNode;
        }
        head = newNode;
    }

    /**
     * 插入链表中间 todo
     */
    public void insertNth(int position, int date) {
        if (position == 0) {
            insertHead(date);
        } else {
            DoubleListNode curr = head;
            for (int i = 1; i < position; i++) {
                curr = curr.next;
            }
            DoubleListNode newNode = new DoubleListNode(date);
            newNode.next = curr.next;   // 新加的点指向后面，保证不断链
            curr.next.pre = newNode;

            curr.next = newNode;        // 把当前点指向新加的点
            newNode.pre = curr;
        }
    }

    /**
     * 插入链表尾部 todo
     */
    public void insertTail(int date) {
        DoubleListNode newNode = new DoubleListNode(date);
        // 若原来有数据
        if (head == null) {
//            head = newNode;
            tail = newNode;
        } else {
            head.pre = newNode;
            newNode.next = head;
//            head = newNode;
        }
        head = newNode;
    }

    /**
     * 删除链表头部
     */
    public void deleteHead() {
        if (head == null) {         // 没有数据
            return;
        }
        if (head.next == null) {    // 就一个节点
            tail = null;
        } else {
            head.next.pre = null;
        }
        head = head.next;
    }

    public void deleteKey(int data) {
        DoubleListNode current = head;
        while (current.value != data) {
            if (current.next == null) {
                System.out.println("没有找到节点");
                return;
            }
            current = current.next;
        }
        if (current == head) {  // 指向下个就表示删除第一个
            deleteHead();
        } else {
            current.pre.next = current.next;
            if (current == tail) {  // 删除的是尾部
                tail = current.pre;
                current.pre = null;
            } else {
                current.pre.next = current.pre;
            }
        }
    }

    public class DoubleListNode {
        int value;              // 值
        DoubleListNode pre;     // 指向前一个指针
        DoubleListNode next;    // 指向后一个指针

        public DoubleListNode(int value) {
            this.value = value;
            this.pre = null;
            this.next = null;
        }

        public DoubleListNode(int value, DoubleListNode pre, DoubleListNode next) {
            this.value = value;
            this.pre = pre;
            this.next = next;
        }
    }
}
