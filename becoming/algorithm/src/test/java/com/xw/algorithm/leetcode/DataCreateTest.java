package com.xw.algorithm.leetcode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/11/1
 */
public class DataCreateTest {
    public static void main(String[] args) {
//		String filepath = System.getProperty("user.dir");
//		filepath += "\\data.txt";
//		System.out.println(filepath);

        try {
            File file = new File("data.txt");
            if (!file.exists()) {
                //如果不存在data.txt文件则创建
                file.createNewFile();
                System.out.println("data.txt创建完成");
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i <= 2000000; i++) {
                double floor = Math.round((new Random().nextDouble() * 9000));
//			 	bw.write(i + "," + new Random().nextInt() + "\n");
                bw.write(String.valueOf(floor) + "\n");
                // "\n" 和 bw.newLine() 都可换行
//				bw.newLine();	// 新的一行
            }
            bw.close();
            fw.close();
            System.out.println("执行完毕");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
