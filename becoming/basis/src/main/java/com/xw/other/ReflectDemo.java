package com.xw.other;

/**
 * @author liuxiaowei
 * @description 反射
 * 什么是反射？
 *      Java反射说的是在运行状态中，对于任何一个类，我们都能够知道这个类有哪些方法和属性。对于任何一个对象，我们都能够对它的方法和属性进行调用。
 *      我们把这种动态获取对象信息和调用对象方法的功能称之为反射机制。
 *
 * 反射的三种方式
 *      这里需要跟大家说一下，所谓反射其实是获取类的字节码文件，也就是.class文件，那么我们就可以通过Class这个对象进行获取。
 *      第一种方式：
 *          Class<?> getClass()   返回此 Object 的运行时类    这个方法其实是Object的一个方法，Class 继承了 Object，可以直接使用
 * @date 2021/5/31
 */
public class ReflectDemo {

    public static void main(String[] args) {
//        第一种方式
        // 创建一个对象
        DataString demo01 = new DataString();
        // 获取该对象的 Class 对象
        Class<? extends DataString> aClass01 = demo01.getClass();
        Class c01 = demo01.getClass();
        // 获取类名称
        System.out.println(c01.getName());

//        第二种方式
        Class<DataString> aClass02 = DataString.class;
        Class c02 = DataString.class;
        // 获取类名称
        System.out.println(c02.getName());

//        第三种   这里需要注意，通过类的全路径名获取Class对象会抛出一个异常，如果根据类路径找不到这个类那么就会抛出这个异常。
        try {
            // 根据类的全路径名获取
            Class<?> aClass03 = Class.forName("com.xw.other.DataString");
            Class c03 = Class.forName("com.xw.other.DataString");
            // 获取类名称
            System.out.println(c02.getName());
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

//        第一种已经创建了对象，那么这个时候就不需要去进行反射了，显得有点多此一举。第二种需要导入类的包，依赖性太强。所以我们一般选中第三种方式。
    }
}
