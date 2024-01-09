package com.xw.other;

import com.xw.entity.Person;

/**
 * @author liuxiaowei
 * @description https://www.runoob.com/w3cnote/sort-algorithm-summary.html
 * @date 2020/7/10 15:49
 */
public class SortDemo {
    public static void main(String[] args) {
//        int[] arr = {6, 3, 8, 2, 9, 1};
//        System.out.println("排序前数组为：");
//        for (int num : arr) {
//            System.out.println(num + "");
//        }
//        Calendar calendar = Calendar.getInstance();
//        System.out.println(System.currentTimeMillis());
        Person person = new Person();
        person.setName("Passerby");
        person.setAge(26);
        System.out.println(person.toString());

        /**
         * public static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) ：将数组中指定的数据拷贝到另一个数组中。
         *      src  Object  源数组
         *      srcPos   int     源数组索引起始位置
         *      dest     Object  目标数组
         *      destPos  int     目标数组索引起始位置
         *      length   int   复制元素个数
         */
        // 将src数组中前3个元素，复制到dest数组的前3个位置上
        // 复制元素前：src数组元素[1,2,3,4,5]，dest数组元素[6,7,8,9,10]
        // 复制元素后：src数组元素[1,2,3,4,5]，dest数组元素[1,2,3,9,10]
        int[] src = {1, 2, 3, 4, 5};
        int[] dest = new int[]{6, 7, 8, 9, 10};
        System.arraycopy(src, 0, dest, 0, 3);

        StringBuilder builder1 = new StringBuilder();
        System.out.println(builder1);
//        StringBuilder builder2 = new StringBuilder("passerby");
        StringBuilder builder2 = builder1.append("passerby");
        System.out.println(builder2);
        System.out.println(builder1 == builder2);// true
        String s = builder2.toString();
        System.out.println(s);

        /**
         * 装箱与拆箱
         * 基本类型与对应的包装类对象之间，来回转换的过程称为”装箱“与”拆箱“：
         * 装箱：从基本类型转换为对应的包装类对象。
         * 拆箱：从包装类对象转换为对应的基本类型。
         */
        // 用Integer与 int为例：
        // 基本数值---->包装对象
        Integer integer1 = new Integer(1);   //  使用构造函数函数
        Integer integer2 = Integer.valueOf(2);
        // 包装对象---->基本数值
        int i = integer1.intValue();

        String s1 = 4 + "";
        float parseFloat = Float.parseFloat(s1);
        System.out.println(parseFloat);

    }
}
