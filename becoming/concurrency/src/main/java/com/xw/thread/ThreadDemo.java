package com.xw.thread;

import java.util.concurrent.Callable;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/7/29 16:56
 *
 * 线程的start()方法只能调用一次，多次调用会抛出IllegalThreadStateException异常。
 * 当调用start方法之后，由jvm决定何时运行线程的run()，当run方法执行结束(正常结束或抛出异常中止)，线程的运行也就结束了。
 */
public class ThreadDemo {
    public static void main(String[] args) throws Exception {

        // 使用匿名内部类及 Lambda 方式实现线程的创建
//        threadDemo();

        Thread extendsThread = new ExtendsThread();
        Thread implementsRunnable = new Thread(new ImplementsRunnable());

        extendsThread.start();
        implementsRunnable.start();
        // 输出"当前线程"的线程名称
        System.out.printf("0.Welcome! I'm %s %n", Thread.currentThread().getName());
    }

    public static void threadDemo() {
        // 传统方式创建线程
        Thread tradition = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                }
            }
        }, "传统方式创建的线程");
        tradition.start();

        /**
         * 函数式编程思想
         * 使用前提
         * 		必须有接口 并且接口中只有一个方法
         * 		参数类型或局部变量必须对应
         * 	标准格式
         * 		(参数类型 变量名)->{代码语句}
         * 		():小括号中写的参数的数据类型和变量名，没有参数空着不写，多个参数用,隔开
         * 		->:固定写法 代表指向动作
         *      {}:	方法体 和传统写法一致
         * 	省略规则
         * 		参数类型可以省略
         * 		参数只有一个小括号可以省略
         * 		大括号中只有一条语句时，可以省略大括号 分号及return
         */

        // 使用匿名内部类及 Lambda表达式实现线程的创建
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
            }
        }, "noNameInnerClass&lambda").start();
    }

    /**
     * 1、继承Thread类的方式创建线程
     */
    public static class ExtendsThread extends Thread {
        // 在该方法中实现线程的任务处理逻辑
        @Override
        public void run() {
            // 输出当前线程的线程名称
            System.out.printf("1.Welcome! I'm %s %n", Thread.currentThread().getName());
        }
    }

    /**
     * 2、实现Runnable接口的方式创建线程
     */
    public static class ImplementsRunnable implements Runnable{
        // 在该方法中实现线程的任务处理逻辑
        @Override
        public void run() {
            // 输出当前线程的线程名称
            System.out.printf("2.Welcome! I'm %s %n", Thread.currentThread().getName());
        }
    }

    /**
     * 3、实现Callable接口的方式创建线程
     */
    public static class ImplementsCallable implements Callable<Object> {
        // 在该方法中实现线程的任务处理逻辑
        @Override
        public String call() throws Exception {
            // 输出当前线程的线程名称
            System.out.printf("3.Welcome! I'm %s %n", Thread.currentThread().getName());
            return null;
        }
    }
}
