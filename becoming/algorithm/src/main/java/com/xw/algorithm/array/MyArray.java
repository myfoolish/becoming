package com.xw.algorithm.array;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/22
 */
public class MyArray {

    private int size;   // 数组长度
    private int data[];
    private int index;  // 当前已存在数据的大小

    /**
     * 数组初始化过程
     * @param size
     */
    public MyArray(int size) {
        this.size = size;
        this.data = new int[size];  // 分配的内存空间{0,0,0,0,0}
        index = 0;
    }

    public void insert(int location, int arr) {
        if (index++ < size) {
            for (int i = size - 1; i > location; i--) {
                data[i] = data[i - 1];  // 数组后移
            }
            data[location] = arr;
        }   // todo 扩容
    }

    public void delete(int location) {
        for (int i = location; i < size; i++) {
            if (i != size - 1) {    //怕越界，所以加一个判断
                data[i] = data[i + 1];
            } else {
                data[i] = 0;        // 0默认没存数据
            }
        }
        index--;
    }

    public void update(int location, int arr) {
        data[location] = arr;
    }

    public int get(int location) {
        return data[location];
    }

    public void print() {
        System.out.println("index: " + index);
        for (int i = 0; i < index; i++) {
            System.out.println(data[i] + " ");
        }
    }

    public static void main(String[] args) {
        MyArray list = new MyArray(5);
        list.insert(0, 1);
        list.insert(1, 5);
        list.insert(2, 3);
        System.out.println(list.get(0));
        System.out.println(list.get(1));
        list.print();
    }
}
