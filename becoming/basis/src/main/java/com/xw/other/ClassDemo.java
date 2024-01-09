package com.xw.other;

import java.lang.annotation.ElementType;

/**
 * @author liuxiaowei
 * @description 哪些类型对应有 Class 的对象
 *  (1) class:
 *      外部类，成员（成员内部类，静态内部类），局部内部类，匿名内部类
 *  (2) interface: 接口
 *  (3) []: 数组
 *  (4) enum: 枚举
 *  (5) annotation: 注解 @interface
 *  (6) primitive type: 基本数据类型
 *  (7) void
 * @date 2021/9/25
 */
public class ClassDemo {
    public static void main(String[] args) {
        Class c1 = Object.class;
        Class c2 = Comparable.class;
        Class c3 = String[].class;
        Class c4 = int[][].class;
        Class c5 = ElementType.class;
        Class c6 = Override.class;
        Class c7 = int.class;
        Class c8 = void.class;
        Class c9 = Class.class;

        int[] a = new int[10];
        int[] b = new int[100];
        Class c10 = a.getClass();
        Class c11 = b.getClass();
        // 只要元素类型与维度一样，就是同一个 Class
        System.out.println(c10 == c11);
    }
}
