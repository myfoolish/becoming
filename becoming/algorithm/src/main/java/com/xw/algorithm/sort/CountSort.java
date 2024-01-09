package com.xw.algorithm.sort;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description 计数排序
 * @date 2023/11/1
 */
public class CountSort {
    public static void main(String[] args) throws Exception {
        String str = null;
        String filename = "/Users/liuxiaowei/workspace/java/demo/becoming/algorithm/src/main/resources/data.txt";
        InputStreamReader inputStreamReader = new InputStreamReader(Files.newInputStream(Paths.get(filename)), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        int[] data = new int[2000002];
        int i = 0;
        while ((str = bufferedReader.readLine()) != null) {
            double value = Double.parseDouble(str);
            value = value * 10;
            data[i++] = (int) value;
//            System.out.println((int)value);
        }
//        int[] data = {2, 1, 5, 3};
        System.out.println("数据读取完毕，size为：" + i);
        long start = System.currentTimeMillis();
        countSort(data, 0, 100000);
        System.out.println("消耗时间为：" + (System.currentTimeMillis() - start) + "ms");
    }

    public static void countSort(int[] data, int min, int max) throws Exception {
        File file = new File("/Users/liuxiaowei/workspace/java/demo/becoming/algorithm/src/main/resources/data-sort.txt");
        Writer writer = new FileWriter(file);

        int[] counts = new int[max + 1];
        for (int i = 0; i < data.length; i++) {
            counts[data[i]]++;
        }

        for (int i = 0; i <= max; i++) {
            if (counts[i] > 0) {
                for (int j = 0; j < counts[i]; j++) {
//                    System.out.println(i);
                    writer.write((double) (i / 10.0) + "\r\n");
                }
            }
        }
        writer.close();
    }
}
