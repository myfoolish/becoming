package com.xw.test;

import com.xw.utils.JDBCUtils;
import org.junit.Test;

import java.sql.*;

/**
 * @author liuxiaowei
 * @description 注意：使用JDBC规范，采用都是 java.sql包下的内容
 * @date 2019/8/20 15:52
 */
public class JDBCTest {
    @Test
    public void JDBCTest01() throws ClassNotFoundException, SQLException {
        // 1、注册驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2、获得连接
        /**
         * URL:SUN公司与数据库厂商之间的一种协议。
         * 协议子协议 IP :端口号数据库 mysql数据库: jdbc:mysql://localhost:3306/day04 或者 jdbc:mysql:///day04（默认本机连接）
         *                          oracle数据库: jdbc:oracle:thin:@localhost:1521:sid
         */
//        Connection connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/dql", "root", "root");
        Connection connection = DriverManager.getConnection("jdbc:mysql:///dql", "root", "root");
        // 3、获取执行sql语句的对象
        Statement statement = connection.createStatement();
        // 4、执行sql语句
        ResultSet resultSet = statement.executeQuery("select * from user");
        // 5、处理结果集
        while (resultSet.next()) {
            // 获得一行数据
            int id = resultSet.getInt("id");
            String name = resultSet.getString("name");
            System.out.println(id + "、"+name);
        }
        // 6、释放资源
        resultSet.close();
        statement.close();
        connection.close();
    }

    @Test
    public void JDBCTest02() {
        // 增、删、改
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // 获取连接
            connection = JDBCUtils.getConnection();
            // 获取执行sql对象
            statement = connection.createStatement();

//            int i = statement.executeUpdate("insert into user values (4,'坏脾气先生L','24')");   // 插入
            int i = statement.executeUpdate("update user set age=26 where name='MyFoolish'");   // 更新
//            int i = statement.executeUpdate("delete from user where name='坏脾气先生L'");   // 删除
            System.out.println(i);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, statement, resultSet);
        }
    }

    @Test
    public void JDBCTest03() {
        //查
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            // 获取连接
            connection = JDBCUtils.getConnection();
            // 获取执行sql对象
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from user where id = 2");// 根据id查询
            if(resultSet.next()){
                String cid = resultSet.getString("id");
                String cname = resultSet.getString("name");
                System.out.println("根据id查询：" + cid + "、" + cname);
            } else {
                System.out.println("没有数据");
            }

            resultSet = statement.executeQuery("select * from user");// 查询所有
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.println("查询所有：" + id + "、" + name);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, statement, resultSet);
        }
    }

    @Test
    public void JDBCTest04() {
        // 增、删、改
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取连接
            connection = JDBCUtils.getConnection();
            // 获取执行sql对象
            String sql = "update user set age=? where name= ? ";
            // 获得预处理对象
            preparedStatement = connection.prepareStatement(sql);
            // 设置实际参数
            preparedStatement.setInt(1, 26);
            preparedStatement.setString(2, "XwCoding");
//            int i = statement.executeUpdate("insert into user values (4,'坏脾气先生L','24')");   // 插入
            int i = preparedStatement.executeUpdate();   // 更新
//            int i = statement.executeUpdate("delete from user where name='坏脾气先生L'");   // 删除
            System.out.println(i);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.closeResource(connection, preparedStatement, resultSet);
        }
    }
}
