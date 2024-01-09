package com.xw.utils;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @author liuxiaowei
 * @description JDBC中最常用的有一个核心类和三个核心接口：
 *              DriverManager类：用于实现数据库连接，具体为实现注册驱动和创建数据库连接对象
 *              Connection接口：用于表示与数据库的连接对象，从DriverManager对象获取
 *              Statement接口：用于表示SQL语句的对象，从Connection对象获取
 *              ResultSet接口：用于表示查询操作的结果集，当对Statement对象执行查询操作时，会返回该接口的对象
 * @date 2022/1/10
 */
public class JDBCUtils {
    private static String driverName;
    private static String url;
    private static String username;
    private static String password;

    // 静态代码块，类加载的时候运行
    // 通过静态代码块初始化数据库连接配置数据，并注册数据库驱动
    static {
        try {
            Properties properties = new Properties();
            // 通过读取 properties 文件给属性赋值，即每次使用该工具类都会读取最新配置进行连接
            properties.load(new FileInputStream(new File("jdbc.properties")));
//            properties.load(Files.newInputStream(Paths.get("jdbc.properties")));
            properties.getProperty("driverName");
            properties.getProperty("url");
            properties.getProperty("username");
            properties.getProperty("password");

            Class.forName(driverName);
        } catch (Exception e) {
            throw new RuntimeException("获取数据库连接异常，请检查配置数据" + e);
        }
    }

    /**
     * 原生JDBC获取数据库连接对象
     * @return
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            throw new RuntimeException("获取数据库连接异常，请检查配置数据" + e);
        }
        return connection;
    }

    // 创建一个C3P0的连接池对象（使用c3p0‐config.xml中default‐config标签中对应的参数）
    public static DataSource c3p0DataSource = new ComboPooledDataSource();

    /**
     * 从C3P0连接池中获取数据库连接对象
     * @return
     */
    public static Connection getC3P0Connection() {
        Connection connection = null;
        try {
            connection = c3p0DataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("获取数据库连接异常，请检查配置数据" + e);
        }
        return connection;
    }

    // 1、声明静态数据源成员变量
    private static final DataSource druidDataSource;

    // 2、创建连接池对象
    static {
        // 加载配置文件中的数据
        InputStream inputStream = JDBCUtils.class.getResourceAsStream("jdbc.properties");
        Properties properties = new Properties();
        try {
            properties.load(inputStream);
            // 创建连接池，使用配置文件中的参数
            druidDataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException("获取数据库连接异常，请检查配置数据" + e);
        }
    }

    // 3. 定义公有的得到数据源的方法
    public static DataSource getDataSource() {
        return druidDataSource;
    }

    /**
     * 4、从Druid连接池中获取数据库连接对象
     * @return
     */
    public static Connection getDruidConnection() {
        Connection connection = null;
        try {
            connection = druidDataSource.getConnection();
        } catch (Exception e) {
            throw new RuntimeException("获取数据库连接异常，请检查配置数据" + e);
        }
        return connection;
    }

    // 6.重载关闭方法
    public static void closeResource(Connection connection, Statement statement) {
        closeResource(connection, statement, null);
    }

    /**
     * 释放资源（关闭 JDBC 相关资源）
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void closeResource(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException("释放数据库连接异常，请检查配置数据" + e);
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException("释放数据库连接异常，请检查配置数据" + e);
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("释放数据库连接异常，请检查配置数据" + e);
            }
        }
    }
}