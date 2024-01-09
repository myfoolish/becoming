package com.xw.test;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/8/13 17:31
 */
public class TCPTest {
    @Test
    public void ServerTCP() throws IOException {
        System.out.println("服务端启动 , 等待连接 .... ");
        // 1、创建ServerSocket 对象，绑定端口，开始等待连接
        ServerSocket serverSocket = new ServerSocket(6666);
        // 2、接收连接 accept 方法，返回 socket 对象
        Socket server = serverSocket.accept();
        // 3、通过 socket 获取输入流
        InputStream inputStream = server.getInputStream();
        // 4.一次性读取数据
        // 4.1 创建字节数组
        byte[] bytes = new byte[1024];
        // 4.2 据读取到字节数组中.
        int len = inputStream.read(bytes);
        // 4.3 解析数组,打印字符串信息
        String s = new String(bytes, 0, len);
        System.out.println(s);
        inputStream.close();
        serverSocket.close();
    }

    @Test
    public void ClientTCP() throws IOException {
        System.out.println("客户端 发送数据");
        // 1.创建 Socket ( ip , port ) , 确定连接到哪里.
        Socket client = new Socket("localhost", 6666);
        // 2.获取流对象 . 输出流
        OutputStream outputStream = client.getOutputStream();
        // 3.写出数据.
        outputStream.write("你好么? tcp ,我来了".getBytes());
        // 4. 关闭资源 .
        outputStream.close();
        client.close();
    }

    public static void main(String[] args) {
        String str = "fsfxzb vbbvnvnvbmnbmxfhbxfnvbcv";
        int size = str.length() / 8;
        if (str.length() % 8 != 0) {
            size += 1;
        }
        List<String> list =  getList(str, 8, size);
        for (String s : list) {
            s = s + '\n';
            System.out.println(s);
        }
    }
    public static List<String> getList(String str, int length,
                                       int size) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            String childStr = substring(str, i * length,
                    (i + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    public static String substring(String str, int x, int y) {
        if (x > str.length())
            return null;
        if (y > str.length()) {
            return str.substring(x, str.length());
        } else {
            return str.substring(x, y);
        }
    }
}
