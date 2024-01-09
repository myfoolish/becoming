package com.xw.proto;

import com.xw.mapper.xa.mapper1.UserXA1Mapper;
import com.xw.mapper.xa.mapper2.UserXA2Mapper;
import io.seata.core.exception.TransactionException;
import io.seata.rm.RMClient;
import io.seata.rm.datasource.xa.DataSourceProxyXA;
import io.seata.tm.TMClient;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/22
 */
public class XAMybatis {
    private static final String driverClassName = "com.mysql.cj.jdbc.Driver";
    private static final String url1 = "jdbc:mysql://localhost:3306/becoming?useSSL=false";
    private static final String url2 = "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private static final String username = "root";
    private static final String password = "root";

    public static SqlSessionFactory proto1() {
        DataSource dataSource = new PooledDataSource(driverClassName, url1, username, password);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserXA1Mapper.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }
    public static SqlSessionFactory proto2() {
        DataSource dataSource = new PooledDataSource(driverClassName, url2, username, password);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserXA2Mapper.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    /**
     * registry.conf和file.conf时在不整合springboot时使用，此时的seata依赖为seata-all
     */

    static {
        TMClient.init("user", "default_tx_group");
        RMClient.init("suer", "default_tx_group");
    }

    public static SqlSessionFactory xa1() {
        DataSource dataSource = new PooledDataSource(driverClassName, url1, username, password);
        DataSourceProxyXA dataSourceProxyXA = new DataSourceProxyXA(dataSource);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSourceProxyXA);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserXA1Mapper.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    public static SqlSessionFactory xa2() {
        DataSource dataSource = new PooledDataSource(driverClassName, url2, username, password);
        DataSourceProxyXA dataSourceProxyXA = new DataSourceProxyXA(dataSource);
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment("development", transactionFactory, dataSourceProxyXA);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(UserXA2Mapper.class);
        return new SqlSessionFactoryBuilder().build(configuration);
    }

    public static void main(String[] args) throws Exception {
//        proto();
        xa();
    }

    /**
     * 使用seata xa模式解决分布式事务
     * @throws TransactionException
     */
    private static void xa() throws TransactionException {
        SqlSessionFactory sqlSessionFactory1 = XAMybatis.xa1();
        SqlSessionFactory sqlSessionFactory2 = XAMybatis.xa2();
        SqlSession sqlSession1 = sqlSessionFactory1.openSession(true);;
        SqlSession sqlSession2 = sqlSessionFactory2.openSession(true);;
        // 使用seata xa模式解决分布式事务
        GlobalTransaction transaction = GlobalTransactionContext.getCurrentOrCreate();
        transaction.begin();
        try {
            sqlSession1.getMapper(UserXA1Mapper.class).updateUser1(8000);
//            int i = 1 / 0;
            sqlSession2.getMapper(UserXA2Mapper.class).updateUser2(100000);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw new RuntimeException(e);
        }finally {
            sqlSession1.close();
            sqlSession2.close();
        }
    }

    /**
     * 原生mybatis出现分布式事务问题
     */
    private static void proto() {
        SqlSessionFactory sqlSessionFactory1 = XAMybatis.proto1();
        SqlSessionFactory sqlSessionFactory2 = XAMybatis.proto2();
        SqlSession sqlSession1 = sqlSessionFactory1.openSession(true);
        SqlSession sqlSession2 = sqlSessionFactory2.openSession(true);
        sqlSession1.getMapper(UserXA1Mapper.class).updateUser1(8000);
//        int i = 1 / 0;
        sqlSession2.getMapper(UserXA2Mapper.class).updateUser2(100000);
        sqlSession1.close();
        sqlSession2.close();
    }
}
