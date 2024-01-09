package com.xw.test;

import com.xw.custom.CustomException;
import org.junit.Test;

import java.io.FileNotFoundException;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/7/24 10:37
 */

/**
 * 运行时异常被抛出可以不处理。即不捕获也不声明抛出。
 * 如果父类抛出了多个异常,子类覆盖父类方法时,只能抛出相同的异常或者是他的子集。
 * 父类方法没有抛出异常，子类覆盖父类该方法时也不可抛出异常。此时子类产生该异常，只能捕获处理，不能声明抛出
 * 当多异常处理时，捕获处理，前边的类不能是后边类的父类
 *  在try/catch后可以追加finally代码块，其中的代码一定会被执行，通常用于资源回收。
 */
public class ExceptionTest {

    @Test
    public void throwDemo() {
        int[] arr = {23, 56, 78};
        int num = getElement(arr, 3);
        System.out.println("num=" + num);
    }

    // 对给定的数组通过给定的角标获取元素
//    private int getElement(int[] arr, int index) {
//        return arr[index];
//    }

    // throw new 异常类名(参数);
    public static int getElement(int[] arr,int index){
        //判断 索引是否越界
        if(index<0 || index>arr.length-1){
            /*
            判断条件如果满足，当执行完throw抛出异常对象后，方法已经无法继续运算。
            这时就会结束当前方法的执行，并将异常告知给调用者。这时就需要通过异常来解决。
            */
            throw new ArrayIndexOutOfBoundsException("索引越界了~~~");
        }
        return arr[index];
    }

    @Test
    public void throwsDemo() throws FileNotFoundException {
        read("a.txt");
    }

    //如果定义功能时有问题发生需要报告给调用者。可以通过在方法上使用throws关键字进行声明
    public void read(String path) throws FileNotFoundException {
        if (!path.equals("a.txt")) {
            throw new FileNotFoundException("文件不存在");
        }
    }

    @Test
    public void tryCatchDemo() {
        try {   //当产生异常时，必须有处理方式。要么捕获，要么声明
            read("a.txt");
        } catch (FileNotFoundException e) {  //try中抛出的是什么异常，在括号中就定义什么异常类型
            e.printStackTrace();
        }
    }

    @Test
    public void finallyDemo() {
        try {   //当产生异常时，必须有处理方式。要么捕获，要么声明
            read("a.txt");
        } catch (FileNotFoundException e) {  //try中抛出的是什么异常，在括号中就定义什么异常类型
            e.printStackTrace();
        }finally {
            System.out.println("无关对错");
        }
    }

    //模拟数据库中已存在账号
    private static String[] names = {"MyFoolish", "Passerby"};
    @Test
    public void LoginDemo() {
        try {
            checkUserName("XwCoding");
            System.out.println("注册成功");
        } catch (CustomException e) {
            e.printStackTrace();
        }
    }
    //判断当前注册账号是否存在
    //因为是编译期异常，又想调用者去处理 所以声明该异常
    public boolean checkUserName(String userName) throws CustomException {
        for (String name : names) {
            if (name.equals(userName)) {
                throw new CustomException(name + "已经被注册了！");
            }
        }
        return true;
    }
}
