/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/26
 */
public class FinalExample {
    int i;          // 普通变量
    final int j;    // final变量
    static FinalExample obj;

    public FinalExample() {     // 构造函数
        i = 1;                  // 写普通域
        j = 2;                  // 写final域
    }

    public static void writer() {   // 写线程A执行
        obj = new FinalExample();
        System.out.println("i: " + obj.i + " j: " + obj.j);
    }

    public static void reader() {   // 读线程B执行
        FinalExample object = obj;  // 读对象引用
        int a = object.i;           // 读普通域
        int b = object.j;           // 读final域
        System.out.println("a: " + a + " b: " + b);
    }

    static Thread thread1 = new Thread(() -> {
        FinalExample.writer();
        System.out.println(Thread.currentThread().getName());
    },"thread1");

    static Thread thread2 = new Thread(() -> {
        FinalExample.reader();
        System.out.println(Thread.currentThread().getName());
    },"thread2");
    public static void main(String[] args) {
        thread1.start();
        thread2.start();
    }
}
