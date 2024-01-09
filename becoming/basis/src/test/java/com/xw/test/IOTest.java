package com.xw.test;

import org.junit.Test;

import java.io.*;
import java.util.Properties;
import java.util.Set;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/8/7 9:35
 */
public class IOTest {
    /**
     * java.io.OutputStream 抽象类是表示字节输出流的所有类的超类，将指定的字节信息写出到目的地。它定义了字节输出流的基本共性功能方法。
     *      public void close() ：关闭此输出流并释放与此流相关联的任何系统资源。
     *      public void flush() ：刷新此输出流并强制任何缓冲的输出字节被写出。
     *      public void write(byte[] b) ：将 b.length字节从指定的字节数组写入此输出流。
     *      public void write(byte[] b, int off, int len) ：从指定的字节数组写入 len字节，从偏移量 off开始输出到此输出流。
     *      public abstract void write(int b) ：将指定的字节输出流。
     * java.io.FileOutputStream 类是 OutputStream 的子类，文件输出流，用于将数据写出到文件
     *      public FileOutputStream(File file) ：创建文件输出流以写入由指定的 File对象表示的文件。
     *      public FileOutputStream(String name) ： 创建文件输出流以指定的名称写入文件。
     *      当你创建一个流对象时，必须传入一个文件路径。该路径下，如果没有这个文件，会创建该文件。如果有这个文件，会清空这个文件的数据。
     *
     *      public FileOutputStream(File file, boolean append) ： 创建文件输出流以写入由指定的 File对象表示的文件。
     *      public FileOutputStream(String name, boolean append) ： 创建文件输出流以指定的名称写入文件。
     *      这两个构造方法，参数中都需要传入一个boolean类型的值， true 表示追加数据， false 表示清空原有数据
     * @throws IOException
     */
    @Test
    public void FileOutputStreamConstructor() throws IOException {
        // 使用File对象创建流对象
        File file = new File("Passerby.txt");
        FileOutputStream fileOutputStream01 = new FileOutputStream(file);
        // 使用文件名称创建流对象
        FileOutputStream fileOutputStream02 = new FileOutputStream("Passerby.txt");
        FileOutputStream fileOutputStream03 = new FileOutputStream("Passerby.txt", true);
    }

    /**
     * 回车符 \r 和换行符 \n ：
     *      回车符：回到一行的开头（return）。
     *      换行符：下一行（newline）。
     * 系统中的换行：
     *      Windows系统里，每行结尾是 回车+换行 ，即 \r\n ；
     *      Unix系统里，每行结尾只有 换行 ，即 \n ；
     *      Mac系统里，每行结尾是 回车 ，即 \r 。从 Mac OS X开始与Linux统一。
     * @throws IOException
     */
    @Test
    public void FileOutputStreamTest() throws IOException {
        // 写出字节数组：
        FileOutputStream fileOutputStream = new FileOutputStream("Passerby.txt");
//        FileOutputStream fileOutputStream = new FileOutputStream("Passerby.txt",true);
        // 字符串转换为字节数组
        byte[] bytes = "MyFoolish".getBytes();
        fileOutputStream.write(bytes);    // 写出字节数组数据
        fileOutputStream.write("\r\n".getBytes());  // 写出一个换行，换行符号转成数组写出

        // 写出从索引2开始，2个字节
        fileOutputStream.write(bytes,2,2);
        fileOutputStream.write("\r\n".getBytes());  // 写出一个换行，换行符号转成数组写出

        // 写出数据
        fileOutputStream.write(97);
        fileOutputStream.write(98);
        fileOutputStream.write(99);

        fileOutputStream.close();
    }

    /**
     * java.io.InputStream 抽象类是表示字节输入流的所有类的超类，可以读取字节信息到内存中。它定义了字节输入流的基本共性功能方法。
     *      public void close() ：关闭此输入流并释放与此流相关联的任何系统资源。
     *      public abstract int read() ： 从输入流读取数据的下一个字节。
     *      public int read(byte[] b) ： 从输入流中读取一些字节数，并将它们存储到字节数组 b中 。
     * java.io.FileInputStream 类是 InputStream 的子类，文件输入流，用于从文件中读取字节。
     *      FileInputStream(File file) ： 通过打开与实际文件的连接来创建一个 FileInputStream ，该文件由文件系统中的 File对象 file命名。
     *      FileInputStream(String name) ： 通过打开与实际文件的连接来创建一个 FileInputStream ，该文件由文件系统中的路径名 name命名。
     *      当你创建一个流对象时，必须传入一个文件路径。该路径下，如果没有该文件,会抛出 FileNotFoundException。
     * @throws IOException
     */
    @Test
    public void FileInputStreamConstructor() throws IOException {
        // 使用File对象创建流对象
        File file = new File("Passerby.txt");
        FileInputStream fileInputStream01 = new FileInputStream(file);
        // 使用文件名称创建对象流
        FileInputStream fileInputStream02 = new FileInputStream("Passerby.txt");
    }

    @Test
    public void FileInputStreamTest() throws IOException {
        FileInputStream fileInputStream;
        fileInputStream  = new FileInputStream("Passerby.txt");
        // 读取数据，返回一个字节
//        int read = fileInputStream.read();
        // 读取到末尾,返回-1

        int state;  //定义变量保存数据
        while ((state = fileInputStream.read()) != -1) {
            System.out.println((char) state);
        }

        fileInputStream  = new FileInputStream("Passerby.txt");
        int len;    // 定义变量，作为有效个数
        byte[] bytes = new byte[2]; // 定义字节数组，作为装字节数据的容器
        while ((len = fileInputStream.read(bytes)) != -1) {
            // 每次读取后,把数组变成字符串打印
            System.out.println(new String(bytes));
//            每次读取后,把数组的有效字节部分，变成字符串打印
            System.out.println(new String(bytes,0,len));    // len 每次读取的有效字节个数
        }

        fileInputStream.close();
    }

    @Test
    public void Copy() throws IOException {
        // 创建流对象
        FileInputStream fileInputStream = new FileInputStream("passerby.jpg");  //指定数据源
        FileOutputStream fileOutputStream = new FileOutputStream("XwCoding.jpg");   //指定目的地

        // 读取数据
        byte[] bytes = new byte[1024];  //定义数组
        int len;    //定义长度
        while ((len = fileInputStream.read(bytes)) != -1) {
            // 写出数据
            fileOutputStream.write(bytes, 0, len);
        }
        // 流的关闭原则：先开后关，后开先关。
        fileOutputStream.close();
        fileInputStream.close();
    }

    /**
     * 字符流，只能操作文本文件，不能操作图片，视频等非文本文件
     *
     * java.io.Writer 抽象类是表示用于写出字符流的所有类的超类，将指定的字符信息写出到目的地。它定义了字节输出流的基本共性功能方法。
     *      public abstract void close() ：关闭此输出流并释放与此流相关联的任何系统资源。
     *      public abstract void flush() ：刷新此输出流并强制任何缓冲的输出字符被写出。
     *      public void write(int c) ：写出一个字符。
     *      public void write(char[] cbuf) ：将 b.length字符从指定的字符数组写出此输出流。
     *      public abstract void write(char[] b, int off, int len) ：从指定的字符数组写出 len字符，从偏移量 off开始输出到此输出流。
     *      public void write(String str) ：写出一个字符串。
     * java.io.FileWriter 类是写出字符到文件的便利类。构造时使用系统默认的字符编码和默认字节缓冲区。
     *      FileWriter(File file) ： 创建一个新的 FileWriter，给定要读取的File对象。
     *      FileWriter(String fileName) ： 创建一个新的 FileWriter，给定要读取的文件的名称。
     *      当你创建一个流对象时，必须传入一个文件路径，类似于FileOutputStream。
     * @throws IOException
     */
    @Test
    public void FileWriterConstructor() throws IOException {
        // 使用File对象创建流对象
        File file = new File("Passerby.txt");
        FileWriter fileWriter01 = new FileWriter(file);
        FileWriter fileWriter02 = new FileWriter(file,true);    // 可以续写数据

        //使用文件名称创建流对象
        FileWriter fileWriter03 = new FileWriter("Passerby.txt");
        FileWriter fileWriter04 = new FileWriter("Passerby.txt",true);  // 可以续写数据
    }

    /**
     * 因为内置缓冲区的原因，如果不关闭输出流，无法写出字符到文件中。但是关闭的流对象，是无法继续写出数据的。
     * 如果我们既想写出数据，又想继续使用流，就需要 flush 方法了。
     *      flush ：刷新缓冲区，流对象可以继续使用。
     *      close ：关闭流，释放系统资源。关闭前会刷新缓冲区。
     * @throws IOException
     */
    @Test
    public void FileWriterTest() throws IOException {
        FileWriter fileWriter = new FileWriter("Passerby.txt");
//        FileWriter fileWriter = new FileWriter("Passerby.txt", true);

        fileWriter.write(97);
        fileWriter.write('b');
        fileWriter.write('C');
        fileWriter.write(30000);    // 写出第4个字符，中文编码表中30000对应一个汉字
        fileWriter.flush();
        fileWriter.write("\r\n");   // 写出一个换行

        char[] chars = "Passerby".toCharArray();
        fileWriter.write(chars);
        fileWriter.write("\r\n");   // 写出一个换行

        // 写出从索引2开始，2个字节
        fileWriter.write(chars,2,2);
        fileWriter.write("\r\n");   // 写出一个换行

        String s = "Passerby";
        fileWriter.write(s);

        fileWriter.close(); // 【注意】关闭资源时,与FileOutputStream不同。如果不关闭,数据只是保存到缓冲区，并未保存到文件。
    }

    /**
     * java.io.Reader 抽象类是表示用于读取字符流的所有类的超类，可以读取字符信息到内存中。它定义了字符输入流的基本共性功能方法。
     *      public void close() ：关闭此流并释放与此流相关联的任何系统资源。
     *      public int read() ： 从输入流读取一个字符。
     *      public int read(char[] cbuf) ： 从输入流中读取一些字符，并将它们存储到字符数组 cbuf中 。
     * java.io.FileReader 类是Reader 的子类 读取字符文件的便利类。构造时使用系统默认的字符编码和默认字节缓冲区。
     *      FileReader(File file) ： 创建一个新的 FileReader ，给定要读取的File对象。
     *      FileReader(String fileName) ： 创建一个新的 FileReader ，给定要读取的文件的名称
     *      当你创建一个流对象时，必须传入一个文件路径。类似于FileInputStream。
     * @throws IOException
     */
    @Test
    public void FileReaderConstructor() throws IOException {
        // 使用File对象创建流对象
        File file = new File("Passerby.txt");
        FileReader fileReader01 = new FileReader(file);

        FileReader fileReader02 = new FileReader("Passerby.txt");
    }

    @Test
    public void FileReaderTest() throws IOException {
        FileReader fileReader;
        fileReader = new FileReader("Passerby.txt");
        // 读取数据，返回一个字符
//        int read = fileReader.read();
        // 读取到末尾,返回-1

        int state;  // 定义变量，保存数据
        while ((state = fileReader.read()) != -1) {
            System.out.println((char) state);
        }

        fileReader = new FileReader("Passerby.txt");
        int len;    // 定义变量  保存有效字符个数
        char[] chars = new char[2]; // 定义字符数组    作为装字符数据的容器
        while ((len = fileReader.read(chars)) != -1) {
            System.out.println(new String(chars));
//            每次读取后,把数组的有效字节部分，变成字符串打印
            System.out.println(new String(chars, 0, len));
        }
        fileReader.close();
    }

    // JDK7之前 使用 try...catch...finally 代码块，处理异常部分
    @Test
    public void HandleException1() {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter("Passerby");
            fileWriter.write("Passerby");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * java.util.Properties 继承于 Hashtable ，来表示一个持久的属性集。它使用键值结构存储数据，每个键及其对应值都是一个字符串。
     * 该类也被许多Java类使用，比如获取系统属性时， System.getProperties 方法就是返回一个 Properties 对象。
     *      public Object setProperty(String key, String value) ： 保存一对属性。
     *      public String getProperty(String key) ：使用此属性列表中指定的键搜索属性值。
     *      public Set<String> stringPropertyNames() ：所有键的名称的集合。
     *
     *   public void load(InputStream inStream) ： 从字节输入流中读取键值对。
     *  参数中使用了字节输入流，通过流对象，可以关联到某文件上，这样就能够加载文本中的数据了
     */
    @Test
    public void Properties() throws IOException {
        // 创建属性集对象
        Properties properties = new Properties();
        // 添加键值对元素
//        properties.setProperty("name", "Passerby");

        properties.load(new FileInputStream("Passerby.txt"));

        System.out.println(properties);

        Set<String> strings = properties.stringPropertyNames();
        for (String key : strings) {
            System.out.println(key+"---"+properties.getProperty(key));
        }
    }
}
