package com.xw.test;

import org.junit.Test;

import java.io.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/8/10 14:27
 */
public class IOProTest {
    /**
     * 缓冲流,也叫高效流，是对 4 个基本的 FileXxx 流的增强，所以也是 4 个流，按照数据类型分类：
     *      字节缓冲流： BufferedInputStream ， BufferedOutputStream
     *      字符缓冲流： BufferedReader ， BufferedWriter
     * 缓冲流的基本原理，是在创建流对象时，会创建一个内置的默认大小的缓冲区数组，通过缓冲区读写，减少系统 IO 次数，从而提高读写的效率
     *
     * 字节缓冲流
     *  public BufferedOutputStream(OutputStream out) ：创建一个新的缓冲输出流。
     *  public BufferedInputStream(InputStream in) ：创建一个 新的缓冲输入流。
     * 字符缓冲流
     *  public BufferedReader(Reader in) ：创建一个 新的缓冲输入流。
     *  public BufferedWriter(Writer out) ：创建一个新的缓冲输出流。
     */
    @Test
    public void BufferedConstructor() throws IOException {
//        字节缓冲流
        // 创建字节缓冲输出流
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("Passerby.txt"));
        // 创建字节缓冲输入流
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("Passerby.txt"));

//        字符缓冲流
        // 创建字符缓冲输出流
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Passerby.txt"));
        // 创建字符缓冲输入流
        BufferedReader bufferedReader = new BufferedReader(new FileReader("Passerby.txt"));
    }

//    效率测试----------==========----------==========----------==========
//    基本流
    @Test
    public void BufferedTest01() throws IOException {
        // 记录开始时间
        long start = System.currentTimeMillis();
        // 创建流对象
        try (
                FileInputStream fileInputStream = new FileInputStream("passerby.jpg");
                FileOutputStream fileOutputStream = new FileOutputStream("copy.jpg");
        ) {
            int state;  //定义变量保存数据
            while ((state = fileInputStream.read()) != -1) {
                fileOutputStream.write(state);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("普通流复制时间:"+(end - start)+" 毫秒");
    }

//    缓冲流
    @Test
    public void BufferedTest02() {
        // 记录开始时间
        long start = System.currentTimeMillis();
        // 创建流对象
        try (
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("passerby.jpg"));
                final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("copy.jpg"))
        ) {
            int state;  //定义变量保存数据
            while ((state = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(state);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("普通流复制时间:" + (end - start) + " 毫秒");
    }

//    使用数组的方式
    @Test
    public void BufferedTest03() {
        // 记录开始时间
        long start = System.currentTimeMillis();
        // 创建流对象
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream("passerby.jpg"));
             final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("copy.jpg"))) {
            // 读写数据
            int len;
            byte[] bytes = new byte[8*1024];
            while ((len = bufferedInputStream.read(bytes)) != -1) {
                bufferedOutputStream.write(bytes,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 记录结束时间
        long end = System.currentTimeMillis();
        System.out.println("普通流复制时间:" + (end - start) + " 毫秒");
    }

    // readLine 方法演示
    @Test
    public void BufferedReaderTest() throws IOException {
        // 创建流对象
        BufferedReader bufferedReader = new BufferedReader(new FileReader("passerby.txt"));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        bufferedReader.close();
    }
    
    // newLine 方法演示
    @Test
    public void BufferedWriterTest() throws IOException {
        // 创建流对象
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("Passerby1.txt"));
        // 写出数据
        bufferedWriter.write("坏脾气");
        bufferedWriter.newLine();
        bufferedWriter.write("先生");
        bufferedWriter.newLine();
        bufferedWriter.write("L");

        bufferedWriter.close();
    }

    /**
     * java.io.OutputStreamWriter ，是 Writer 的子类，是从字符流到字节流的桥梁。
     * 使用指定的字符集将字符编码为字节。它的字符集可以由名称指定，也可以接受平台的默认字符集。
     * OutputStreamWriter(OutputStream in) : 创建一个使用默认字符集的字符流。
     * OutputStreamWriter(OutputStream in, String charsetName) : 创建一个指定字符集的字符流。
     */
    @Test
    public void OutputStreamWriterConstructor() throws IOException {
        // 创建流对象,默认 UTF8 编码
        OutputStreamWriter outputStreamWriter01 = new OutputStreamWriter(new FileOutputStream("MyFoolish.txt"));
        //  创建流对象,指定 GBK 编码
        OutputStreamWriter outputStreamWriter02 = new OutputStreamWriter(new FileOutputStream("MyFoolish_GBK.txt"), "GBK");

        //写出数据
        outputStreamWriter01.write("坏脾气先生L");
        outputStreamWriter01.close();

        outputStreamWriter02.write("坏脾气先生L");
        outputStreamWriter02.close();
    }

    /**
     * java.io.InputStreamReader 是 Reader 的子类是从字节流到字符流的桥梁。
     * 它读取字节，并使用指定的字符集将其解码为字符。它的字符集可以由名称指定，也可以接受平台的默认字符集。
     *      InputStreamReader(InputStream in) : 创建一个使用默认字符集的字符流。
     *      InputStreamReader(InputStream in, String charsetName) : 创建一个指定字符集的字符流。
     */
    @Test
    public void InputStreamReaderConstructor() throws IOException {
        // 创建流对象,默认 UTF8 编码
        InputStreamReader inputStreamReader01 = new InputStreamReader(new FileInputStream("MyFoolish_GBK.txt"));
        // 创建流对象,指定 GBK 编码
        InputStreamReader inputStreamReader02 = new InputStreamReader(new FileInputStream("MyFoolish_GBK.txt") , "GBK");
        int state;  //定义变量保存数据
        while ((state = inputStreamReader01.read()) != -1) {
            // 使用默认编码字符流读取,乱码
            System.out.println((char) state);
        }
        inputStreamReader01.close();
        while ((state = inputStreamReader02.read()) != -1) {
            // 使用指定编码字符流读取,正常解析
            System.out.println((char) state);
        }
        inputStreamReader02.close();
    }

//    将 GBK 编码的文本文件，转换为 UTF-8 编码的文本文件。
    @Test
    public void GBK_UTF8Test()throws IOException {
        // 创建流对象,指定 GBK 编码
        InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream("MyFoolish_GBK.txt") , "GBK");
        // 创建流对象,默认 UTF8 编码
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream("MyFoolish.txt"));

        int len;
        // 定义数组
        char[] chars = new char[1024];
        while ((len = inputStreamReader.read(chars)) != -1) {
            // 循环写出
            outputStreamWriter.write(chars,0,len);
        }
        outputStreamWriter.close();
        inputStreamReader.close();
    }
}
