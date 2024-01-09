package com.xw.other;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/3/9
 */
public class DateUtils {
//    public static void main(String[] args) throws ParseException {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String data = "2022-03-08 10:00:00";
//        long now = new Date().getTime();
//        long time = format.parse(data).getTime();
//        long day = (now - time) / ( 60 * 60 * 1000);
//        System.out.println(day);
//    }

    public static final int getDaysByTwoDates(Date startDate, Date endDate) {
        if (Objects.isNull(startDate)) {
            throw new IllegalArgumentException("传入的开始日期为空");
        } else if (Objects.isNull(endDate)) {
            throw new IllegalArgumentException("传入的结束日期为空");
        } else {
            return (int)((startDate.getTime() - endDate.getTime()) / 1000L / 60L / 60L / 24L);
        }
    }

    public static final int getDaysByTwoDates(String startDate, String endDate, String pattern) {
        return getDaysByTwoDates(startDate, pattern, endDate, pattern);
    }

    public static final int getDaysByTwoDates(String startDate, String startPattern, String endDate, String endPattern) {
        return getDaysByTwoDates(parseDate(startDate, startPattern), parseDate(endDate, endPattern));
    }

    public static final int getDaysByTwoDatesDef(String startDate, String endDate) {
        return getDaysByTwoDates(startDate, endDate, "yyyy-MM-dd");
    }

    public static final Date parseDate(String date, String pattern) {
        try {
            return (new SimpleDateFormat(pattern)).parse(date);
        } catch (Exception var3) {
            return null;
        }
    }


    public static void main(String[] args) {
        int days = getDaysByTwoDatesDef(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), "2023-08-08");
        System.out.println(days);
    }
}
