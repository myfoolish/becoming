package com.xw;

import java.util.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/9/14
 */
public class BasicDemo {

    public static class StringDemo{

        @Override
        public boolean equals(Object obj) {
            // 比较字符串的内容是否相同
            return super.equals(obj);
        }

        public static void main(String[] args) {
            // 通过字符数组构造
            char[] chars = {'a', 'b', 'c'};
            String stringByChar = new String(chars);
//            System.out.println(stringByChar);
            // 通过字符数组构造
            byte[] bytes = new byte[3];
            String stringByByte = new String(bytes);
//            System.out.println(stringByByte);

            // 判断功能的方法
            String s1 = "hello", s2 = "hello", s3 = "HELLO";
//            System.out.println("比较：" + s1.equals(s2));  // true
//            System.out.println("忽略大小写比较：" + s1.equalsIgnoreCase(s3));    // true

            // 获取功能的方法
            String s4 = s1.concat(" world");
            System.out.println("将指定的字符连接到当前字符串的结尾：" + s4);
            char charAt = s1.charAt(1);
            System.out.println("获取指定索引处的字符：" + charAt);
            int indexOf = s1.indexOf("l");
            System.out.println("获取字符在当前字符串第一次出现的位置：" + indexOf);
            String substring = s1.substring(2);
            System.out.println("从指定索引开始截取字符串到结尾：" + substring);
            String substring1 = s1.substring(2, 3);
            System.out.println("从指定索引开始截取字符串到指定索引结束（含开始，不含结束）：" + substring1);

            // 转换功能的方法
            char[] charArray = s1.toCharArray();
            System.out.println("把字符串转换为字符数组：" + Arrays.toString(charArray));
            byte[] bytes1 = s1.getBytes();
            System.out.println("把字符串转换为字节数组：" + Arrays.toString(bytes1));
            String replace = s1.replace("h", "H");
            System.out.println("替换字符串中指定字符：" + replace);

            // 分割功能的方法
            String[] split = s1.split("");
            System.out.println("将字符串按照给定的regex（规则）拆分为字符串数组：" + Arrays.toString(split));
        }
    }

    public static class ArraysDemo {
        public static void main(String[] args) {
//            int[] hello = {'h', 'e', 'l', 'l', 'o'};
            int[] array = {1, 2, 3, 4, 0};
            // 操作数组的方法
            System.out.println("返回地址值：" + array);
            System.out.println("返回指定数组内容的字符串表示形式："+Arrays.toString(array));
            System.out.println("排序前的数组：" + Arrays.toString(array));
            // 排序
            Arrays.sort(array);
            System.out.println("排序后的数组：" + Arrays.toString(array));
        }
    }

    public static class MathDemo {
        public static void main(String[] args) {
            double abs = Math.abs(-6);
            System.out.println("返回当前数值（类型）的绝对值：" + abs);
            double ceil = Math.ceil(8.8);
            System.out.println("返回大于等于当前值的最小整数：" + ceil);
            double floor = Math.floor(8.8);
            System.out.println("返回小于等于当前值的最大整数：" + floor);
        }
    }

    public static class Other {
        // 一个方法的返回值类型是接口，则这个方法一定会返回它的实现类对象
        List<String> list = new ArrayList<>();
        // 静态代码块，类加载的时候运行
        static {
            // todo
        }
        // Nginx 5万并发
        // Proxy 代理
        // sql 优化、mysql 的隔离级别、数据库事物、Spring事物的传播机制
        // 框架做的事其实一件都不少，原生JDBC的那一套，框架底层就是原生JDBC
        // 设计模式中的工厂模式 => 问工厂要对象，代码里只能看到接口，看不到具体的实现类

        /**
         * 电商系统支付是如何实现的？
         *  我们在电商系统中可以实现微信、支付宝、翼支付等第三方支付平台。
         *  我做的模块是微信支付，按照微信支付的官方文档来进行开发，（NATIVE - > 扫码支付）每个API有一个对应的URL
         *  （统一下单、查询订单、退款）通过对URL发送请求获取响应得到结果。
         */

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("请输入一个10-50之间的整数：");
            int num = scanner.nextInt();


        }
    }
}
