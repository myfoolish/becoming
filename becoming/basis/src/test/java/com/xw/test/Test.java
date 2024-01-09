package com.xw.test;

/**
 * @Program:
 * @Author: liuxiaowei
 * @Description:
 * @Date: 2019/5/23 16:08
 * @Version:
 */

/**
 *     完成下列题目要求：
 *     ①定义方法filter
 *         要求如下：
 *         形参：String [] arr，String  str
 *         返回值类型：String []
 *         实现：遍历arr，将数组中以参数str的元素开头且长度>5的元素存入另一个String 数组中并返回
 *         PS：返回的数组长度需要用代码获取
 *     ②在main方法中完成以下要求：
 *     定义一个String数组arr，数组元素有："itcast","itheima","baitdu","weixin","zhifubao"
 *     调用filter方法传入arr数组和字符串”it”，输出返回的String数组中所有元素
 *     示例如下
 */
public class Test {

    public static void main(String[] args) {
        //定义一个String数组arr，数组元素有："itcast","itheima","baitdu","weixin","zhifubao"
        String[] arr = {"itcast","itheima","baitdu","weixin","zhifubao"};
        //调用filter方法传入arr数组和字符串”it”，输出返回的String数组中所有元素
        String[] newArr = filter(arr, "it");
        //for遍历输出
        for (int i = 0; i < newArr.length; i++) {
            System.out.println(newArr[i]);
        }
    }
    /*
        ①定义方法filter
        要求如下：
        形参：String [] arr，String  str
        返回值类型：String []
        实现：遍历arr，将数组中以参数str的元素开头且长度>5的元素存入另一个String 数组中并返回
        PS：返回的数组长度需要用代码获取
     */
    public static String[] filter(String[] arr,String str) {
        //1.定义int变量count,用来统计原数组中满足条件的字符串的数量
        int count = 0;
        //2.使用for遍历arr
        for (int i = 0; i < arr.length; i++) {
            //2.1调用方法,判断是否满足条件
            if(isManZu(arr[i], str)) {
                count++;
            }
        }
        //3.创建新的字符串数组
        String[] newArr = new String[count];
        //3.1定义int变量index,用来向新数组中存储元素,代表的索引
        int index = 0;
        //4.使用for把arr中满足条件的字符串存入新数组中
        //5.使用for遍历arr
        for (int i = 0; i < arr.length; i++) {
            //5.1调用方法,判断是否满足条件
            if(isManZu(arr[i], str)) {
                newArr[index] = arr[i];
                index++;
            }
        }
        //6.返回新的字符串数组
        return newArr;
    }
    /*
        定义一个方法,判断某个字符串是否以str开头并且长度>5
        如果满足以上条件,返回true
        否则,返回false
     */
    public static boolean isManZu(String str1,String str2) {
        if(str1.indexOf(str2)==0 && str1.length()>5) {
            return true;
        }
        return false;
    }
}
