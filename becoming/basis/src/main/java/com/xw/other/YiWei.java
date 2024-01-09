package com.xw.other;

/**
 * @author liuxiaowei
 * @description 移位运算符
 *  移位运算符是位操作运算符的一种，可以在二进制的基础上对数字进行平移。按照平移的方向和填充数字的规则分为三种：
 *      << : 左移运算符，num <<1,相当于num乘以2
 *      >> : 右移运算符，num >>1,相当于num除以2
 *      >>> : 无符号右移，忽略符号位，空位都以0补齐（计算机中数字以补码存储，首位为符号位）
 *
 * 如：a = 00110111，则a>>2 = 00001101，b=11010011，则b>>2 = 11110100；
 * 如：a = 00110111，则a>>>2 = 00001101，b=11010011，则b>>>2 = 00110100。
 */
public class YiWei {
    public static void main(String[] args) {
        //Integer.toBinaryString()是将数字用二进制格式显示
        int i = -10;
        System.out.println(i + "==" + Integer.toBinaryString(i));
        //左移两位
        int j = -10<<2;
        System.out.println(j + "==" + Integer.toBinaryString(j));
        //右移两位
        int m = -10>>2;
        System.out.println(m + "==" + Integer.toBinaryString(m));
        //无符号右移
        int n = -10>>>2;
        System.out.println(n + "==" + Integer.toBinaryString(n));

        /*运算结果为：
            11111111111111111111111111110110
            11111111111111111111111111011000
            11111111111111111111111111111101
            111111111111111111111111111101（省略了首位两个0）
        */
        // 左移补0，右移补符号位，无符号右移补0
        // 无符号右移的规则只记住一点：数字右移，左侧空出来的高位用0补齐。
    }
}
