package com.xw.test;

import com.xw.entity.Account;
import com.xw.mapper.AccountMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.util.List;

/**
 * @author liuxiaowei
 * @description 测试 Mybatis 的环境搭建
 * @date 2021/6/3
 */
public class Test02Mybatis {
    public static void main(String[] args) throws Exception {
        // 读取配置文件
        InputStream inputStream = Resources.getResourceAsStream("sqlMapConfig.xml");
        // 创建构建者对象
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        // 创建 SqlSession 工厂
        SqlSessionFactory factory = builder.build(inputStream);
        // 获取 SqlSession
        SqlSession sqlSession = factory.openSession();
        // 创建 mapper 的代理类
        AccountMapper accountMapper = sqlSession.getMapper(AccountMapper.class);
        List<Account> accounts = accountMapper.findAll();
        for (Account account : accounts) {
            System.out.println(account);
        }
        sqlSession.close();
        inputStream.close();
    }
}
