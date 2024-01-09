package com.xw.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class StringUtil {
    private StringUtil() {
    }

    public static boolean isNotNull(String str) {
        boolean flag = false;
        if (str != null && !"".equals(str) && !"null".equals(str)) {
            flag = true;
        }

        return flag;
    }

    public static boolean isNullOrBlank(String str) {
        boolean flag = false;
        if (str == null || "".equals(str) || "null".equals(str)) {
            flag = true;
        }

        return flag;
    }

    public static String demicSeq(String head, String bu, int num) {
        if (bu == null) {
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            bu = sdf.format(date);
        }

        return head + bu + Math.round(Math.random() * Math.pow(10.0D, (double)num));
    }

    public static String getMsgContent(String templet, Map<String, String> paramsMap) {
        String newTemplet = templet;
        if (templet != null && templet != "") {
            Iterator iterator = paramsMap.entrySet().iterator();
            StringBuilder sb = new StringBuilder();

            while(iterator.hasNext()) {
                Entry entry = (Entry)iterator.next();
                String key = (String)entry.getKey();
                sb.delete(0, sb.length());
                sb.append("{").append(key).append("}");
                if (newTemplet.contains(sb.toString())) {
                    sb.delete(0, sb.length());
                    sb.append("\\{").append(key).append("\\}");
                    newTemplet = newTemplet.replaceAll(sb.toString(), (String)entry.getValue());
                }
            }

            return newTemplet;
        } else {
            return "";
        }
    }

    public static boolean isNumeric(String str) {
        if (isNullOrBlank(str)) {
            return false;
        } else {
            Pattern pattern1 = Pattern.compile("^-?[1-9]\\d*$");
            Pattern pattern2 = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$");
            return pattern1.matcher(str).matches() || pattern2.matcher(str).matches();
        }
    }
}
