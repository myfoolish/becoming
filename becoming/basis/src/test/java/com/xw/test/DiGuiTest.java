package com.xw.test;

import org.junit.Test;

import java.io.File;
import java.io.FileFilter;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/8/5 13:18
 */
public class DiGuiTest {
    @Test
    public void sum() {
        int num = 5;

        // 计算1~num的和，使用递归完成
        int sum = getSum(num);
        System.out.println(sum);

        // 计算n的阶乘，使用递归完成    factorial阶乘
        int value = getValue(num);
        System.out.println(value);
    }

    /**
     * 通过递归算法实现
     * @param num
     * @return
     */
    private int getSum(int num) {
        // num为1时,方法返回1,相当于是方法的出口,num总有是1的情况
        if (num == 1) {
            return 1;
        }
        // num不为1时,方法返回 num +(num-1)的累和，递归调用getSum方法
        return num + getSum(num - 1);
    }

    /**
     * 通过递归算法实现
     * @param num
     * @return
     */
    private int getValue(int num) {
        // 1的阶乘为1
        if (num == 1) {
            return 1;
        }
        // n不为1时,方法返回 n! = n*(n-1)!，递归调用getValue方法
        return num * getValue(num - 1);
    }

    @Test
    public void printDir() {
        // 创建File对象
        File dir = new File("D:\\file");
        // 调用打印目录方法
        printDir(dir);
    }

    private void printDir(File dir) {
        // 获取子文件和目录
        File[] files = dir.listFiles();
        /**
         * 循环打印并判断:
         *      当是文件时,打印绝对路径.
         *      当是目录时,继续调用打印目录的方法,形成递归调用.
         */
        for (File file : files) {
            if (file.isFile()) {
                // 是文件,输出文件绝对路径
                System.out.println("文件名：" + file.getAbsolutePath());
            } else {
                // 是目录,输出目录绝对路径
                System.out.println("目录："+file.getAbsolutePath());
                // 继续遍历,调用printDir,形成递归
                printDir(file);
            }
        }
    }

    /**
     * 1、目录搜索，无法判断多少级目录，所以使用递归，遍历所有目录。
     * 2. 遍历目录时，获取的子文件，通过文件名称，判断是否符合条件。
     */
    @Test
    public void fileSearch() {
        // 创建File对象
        File dir = new File("D:\\file");
        // 调用打印目录方法
        fileSearch(dir);
        fileFilter(dir);
        fileFilterWithLambda(dir);
    }

    /**
     * 文件搜索
     * @param dir
     */
    private void fileSearch(File dir) {
        // 获取子文件和目录
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                //是文件，判断文件名并输出文件绝对路径
                if (file.getName().endsWith(".txt")) {
                    System.out.println("文件名：" + file.getAbsolutePath());
                } else {
                    //是目录，继续遍历形成递归
                    printDir(file);
                }
            }
        }
    }

    /**
     * 文件过滤器优化
     * @param dir
     */
    private void fileFilter(File dir) {
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith("txt") || pathname.isDirectory();
            }
        });
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("文件名:" + file.getAbsolutePath());
            } else {
                fileFilter(file);
            }
        }
    }

    /**
     * 文件过滤器优化WithLambda优化
     * @param dir
     */
    private void fileFilterWithLambda(File dir) {
        File[] files = dir.listFiles((File pathname) -> pathname.getName().endsWith("txt") || pathname.isDirectory());
        for (File file : files) {
            if (file.isFile()) {
                System.out.println("文件名:" + file.getAbsolutePath());
            } else {
                fileFilter(file);
            }
        }
    }
}
