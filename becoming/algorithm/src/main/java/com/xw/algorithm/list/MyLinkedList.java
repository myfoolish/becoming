package com.xw.algorithm.list;

/**
 * @author liuxiaowei
 * @description 单向链表
 *      设计一个LRU缓存淘汰算法？
 *      只需要维护一个有序（插入的时间排序）的单向链表
 * @date 2023/9/22
 */
public class MyLinkedList<T> {

    private ListNode head;
//    private int size;
//
//    public MyLinkedList() {
//        size = 0;
//    }

    /**
     * 插入链表头部
     */
    public void insertHead(int date) {
        ListNode newNode = new ListNode(date);
        // 若原来有数据
        newNode.next = head;
        head = newNode;
    }

    /**
     * 插入链表中间
     */
    public void insertNth(int position, int date) {
        if (position == 0) {
            insertHead(date);
        } else {
            ListNode curr = head;
            for (int i = 1; i < position; i++) {
                curr = curr.next;
            }
            ListNode newNode = new ListNode(date);
            newNode.next = curr.next;   // 新加的点指向后面，保证不断链
            curr.next = newNode;        // 把当前点指向新加的点
        }
    }

    /**
     * 删除链表头部
     */
    public void deleteHead() {
        head = head.next;
    }

    /**
     * 删除链表中间
     */
    public void deleteNth(int position) {
        if (position == 0) {
            deleteHead();
        } else {
            ListNode curr = head;
            for (int i = 1; i < position; i++) {
                curr = curr.next;
            }
            curr.next = curr.next.next;
        }
    }

    public void find(int data) {
        ListNode curr = head;
        while (curr != null) {
            if (curr.value == data) {
                break;
            }
            curr = curr.next;
        }
        System.out.println();
    }

    public void print() {
        ListNode curr = head;
        while (curr != null) {
            System.out.print(curr.value + " ");
            curr = curr.next;
        }
        System.out.println();
    }

    public class ListNode {
        int value;
        ListNode next;

        public ListNode(int value) {
            this.value = value;
            this.next = null;
        }

        public ListNode(int value, ListNode next) {
            this.value = value;
            this.next = next;
        }
    }
}
