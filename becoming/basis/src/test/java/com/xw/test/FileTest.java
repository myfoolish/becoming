package com.xw.test;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/8/5 9:29
 */
public class FileTest {
    /**
     * public File(String pathname) ：通过将给定的路径名字符串转换为抽象路径名来创建新的 File实例。
     * public File(String parent, String child) ：从父路径名字符串和子路径名字符串创建新的 File实例。
     * public File(File parent, String child) ：从父抽象路径名和子路径名字符串创建新的 File实例。
     */
    @Test
    public void file01() {
        // 文件路径名
        String pathname = "D:\\file\\XwCoding.txt";
        File file01 = new File(pathname);
        System.out.println(file01);

        // 通过父路径和子路径字符串
        String parent = "D:\\file";
        String child = "Passerby.txt";
        File file02 = new File(parent, child);
        System.out.println(file02);

        // 通过父级File对象和子路径字符串
        File file03 = new File("D:\\file");
        File file04 = new File(file03,"MyFoolish.txt");
        System.out.println(file04);
    }

    /**
     * public String getAbsolutePath() ：返回此File的绝对路径名字符串。
     * public String getPath() ：将此File转换为路径名字符串。
     * public String getName() ：返回由此File表示的文件或目录的名称。
     * public long length() ：返回由此File表示的文件的长度。
     */
    @Test
    public void file02() {
        File file00 = new File("D:\\file");
//        File file00 = new File("D:/file");
        File file = new File(file00,"MyFoolish.txt");
        System.out.println("文件绝对路径：" + file.getAbsolutePath());
        System.out.println("文件构造路径：" + file.getPath());
        System.out.println("文件名称：" + file.getName());
        System.out.println("文件长度：" + file.length() + "字节");
    }

    /**
     * 绝对路径：从盘符开始的路径，这是一个完整的路径。
     * 相对路径：相对于项目目录的路径，这是一个便捷的路径，开发中经常使用
     */
    @Test
    public void file03() {
        File file = null;
        file = new File("D:/file/MyFoolish.txt");
        System.out.println("绝对路径：" + file.getAbsolutePath());
        // 项目下的路径
        file = new File("MyFoolish.txt");
        System.out.println("相对路径：" + file.getAbsolutePath());
    }

    /**
     * public boolean exists() ：此File表示的文件或目录是否实际存在。
     * public boolean isDirectory() ：此File表示的是否为目录。
     * public boolean isFile() ：此File表示的是否为文件。
     */
    @Test
    public void file04() {
        File file00 = new File("D:\\file");
//        File file00 = new File("D:/file");
        File file = new File(file00,"MyFoolish.txt");

        //判断是否存在
        System.out.println("D:\\file\\MyFoolish.txt是否存在：" + file.exists());
        System.out.println(file.isDirectory());
        System.out.println(file.isFile());
        System.out.println("D:\\file\\MyFoolish.txt是否可读："+file.canRead());
        System.out.println("D:\\file\\MyFoolish.txt是否隐藏："+file.isHidden());
    }

    /**
     * public boolean createNewFile() ：当且仅当具有该名称的文件尚不存在时，创建一个新的空文件。
     * public boolean delete() ：删除由此File表示的文件或目录。
     * public boolean mkdir() ：创建由此File表示的目录。
     * public boolean mkdirs() ：创建由此File表示的目录，包括任何必需但不存在的父目录。
     */
    @Test
    public void file05() throws IOException {
        // 文件的创建
        File file01 = new File("MyFoolish.txt");
        System.out.println("MyFoolish.txt是否存在：" + file01.exists());
        System.out.println("MyFoolish.txt是否被创建：" + file01.createNewFile());

        // 目录的创建
        File file02 = new File("MyFoolish");
        System.out.println("MyFoolish目录是否存在：" + file02.exists());
        System.out.println("MyFoolish目录是否被创建：" + file02.mkdir());

        // 创建多级目录
        File file03 = new File("file\\MyFoolish");
        System.out.println("MyFoolish目录是否存在：" + file03.exists());
//        System.out.println("MyFoolish目录是否被创建：" + file03.mkdirs());

        // 文件或目录的删除
        file01.delete();
        file02.delete();
        System.out.println(file03.delete());    //API中说明：delete方法，如果此File表示目录，则目录必须为空才能删除
    }

    /**
     * public String[] list() ：返回一个String数组，表示该File目录中的所有子文件或目录。
     * public File[] listFiles() ：返回一个File数组，表示该File目录中的所有子文件或目录。
     */
    @Test
    public void file06() {
        File dir = new File("D:\\file");

        // 获取当前目录下的文件以及文件夹的名称
        String[] list = dir.list();
        for (String s : list) {
            System.out.println(s);
        }

        // 获取当前目录下的文件以及文件夹对象，只要拿到了文件对象，那么就可以获取更多信息
        File[] files = dir.listFiles();
        for (File file : files) {
            System.out.println(file);
        }
    }
}
