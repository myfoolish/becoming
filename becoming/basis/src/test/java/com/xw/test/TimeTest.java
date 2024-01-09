package com.xw.test;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/6/12 17:23
 */
public class TimeTest {
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    @Test
    public void fun1() {
        Date date = new Date();
        System.out.println(format.format(date));
    }

    /**
     * public int get(int field) ：返回给定日历字段的值。
     * public void set(int field, int value) ：将给定的日历字段设置为给定值。
     * public abstract void add(int field, int amount) ：根据日历的规则，为给定的日历字段添加或减
     * 去指定的时间量。
     * public Date getTime() ：返回一个表示此Calendar时间值（从历元到现在的毫秒偏移量）的Date对象。
     * **********在Calendar类中，月份的表示是以0-11代表1-12月。
     */
    @Test
    public void fun2() {
        // 创建Calendar对象
        Calendar calendar = Calendar.getInstance();

        //get方法用来获取指定字段的值，
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        System.out.println(year + "年" + month + "月" + dayOfMonth + "日");

        //set方法用来设置指定字段的值
        calendar.set(Calendar.YEAR, 2020);
//        calendar.set(2020, 4, 20);
//        calendar.setTime(new Date());
        System.out.print(year + "年" + month + "月" + dayOfMonth + "日");

        // Calendar中的getTime方法并不是获取毫秒时刻，而是拿到对应的Date对象。
        Date time = calendar.getTime();
        System.out.println(time);
    }

    @Test
    public void fun3() {
        Calendar calendar = Calendar.getInstance();
        // add方法可以对指定日历字段的值进行加减操作，如果第二个参数为正数则加上偏移量，如果为负数则减去偏移
        calendar.add(Calendar.DAY_OF_MONTH, -30);
        String endDate = format.format(calendar.getTime());
        System.out.println(endDate);
    }

    /**
     * 验证for循环打印数字1-99所需要使用的时间（毫秒）
     */
    @Test
    public void fun4() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            System.out.println(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("共耗时" + (end - start) + "毫秒");
    }
}
