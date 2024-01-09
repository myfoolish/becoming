package com.xw.other;

import java.util.HashMap;

/**
 * @author liuxiaowei
 * @description
 * @date 2021-5-13
 */
public class DataString {

    // 当final修饰一个基本数据类型时，表示该基本数据类型的值一旦在初始化后便不能发生变化。
    final int age = 26;
//        age = 25;   //  Cannot assign a value to final variable 'age'

    // 如果final修饰一个引用类型时，则在对其初始化之后便不能再让其指向其他对象了，但该引用所指向的对象的内容是可以发生变化的。
    final StringBuilder name = new StringBuilder("晓威"); // StringBuilder 线程不安全 但速度快 (验证情况见下方 line 23)

    // 本质上是一回事，因为引用的值是一个地址，final要求值，即地址的值不发生变化。

    // 另外final修饰一个成员变量（属性），必须要显示初始化。这里有两种初始化方式，（1.在申明的时候给其赋值，否则必须在其类的所有构造方法中都要为其赋值）
//        private final String name;  // Variable 'name' might not have been initialized
    private final String gender = "男";

    public static void main(String[] args) {

        final StringBuilder name = new StringBuilder("晓威");
        System.out.println(name.hashCode() + " --- " + name);
        name.append("刘");
        System.out.println(name.hashCode() + " --- " + name);
        // 结果表明：hashcode值是一样的，说明操作的是同一对象的引用

        // String 对象一旦创建，就不可改变，是一个不可变类。因为它的源码内部维护着一个final修饰的char数组，final修饰的变量不可以被改变，方法不可以被重写，类不可以被继承
        // JVM 为了提高性能和减少内存的开销，在实例化字符串的时候进行了一些优化：使用字符串常量池。
        // 每当我们创建字符串常量时，JVM 会首先检查字符串常量池，如果该字符串已经存在常量池中，那么就直接返回常量池中的实例引用。如果字符串不存在常量池中，就会实例化该字符串并且将其放到常量池中。
        // 由于 String 字符串的不可变性我们可以十分肯定常量池中一定不存在两个相同的字符串。
        String a = "刘晓威";
        a = "晓威";   // 这里先去JVM给常量池里找，找不到会重新创建一个对象，然后把对象的引用地址赋给a
        String b = "晓威";    // 找到了就不用创建对象了，直接把对象的引用地址赋给b。
        System.out.println(a.equals(b));   // 此时地址一样，内容一样

        // 拼接、截取或者重新赋值都是在重新建对象
        System.out.println(b.hashCode());
        b.concat("刘");
        System.out.println(b.concat("刘").hashCode());
        // String对象一旦被创建就是固定不变的了，对String对象的任何改变都不影响到原对象，相关的任何变化性的操作都会生成新的对象。
        HashMap hashMap = new HashMap();
    }
}
