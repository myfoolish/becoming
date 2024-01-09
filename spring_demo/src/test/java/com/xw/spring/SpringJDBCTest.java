package com.xw.spring;

import com.xw.spring.jdbc.entity.Account;
import com.xw.spring.jdbc.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * 编程式事务是指通过代码手动提交回滚事务的事务控制方法
 * Spring JDBC通过TransactionManager事务管理器提供的commit/rollback方法进行事务提交与回滚来实现事务管理
 *
 * 声明式事务是指在不修改源码的的情况下通过配置形式自动实现事务控制，其本质就是AOP环绕通知
 * 当目标方法执行成功时，自动提交事务
 * 当目标方法抛出运行时异常时，自动回滚事务
 * @date 2023/12/13
 */
@RunWith(SpringRunner.class)
@ContextConfiguration("classpath:jdbc.xml")
public class SpringJDBCTest {
    @Resource
    private AccountService accountService;

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Test
    public void insert() {
        // 定义了事务默认的标准配置
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        // 开始一个事务，返回事务状态，事务状态说明当前事务的执行阶段
        TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
        try {
            for (int i = 0; i < 3; i++) {
//                if (i == 2) {
//                    throw new RuntimeException();
//                }
                Account account = new Account();
                account.setName("姓名" + i);
                account.setMoney(20000f + i);
                // 每次保存操作都是一个事物
                accountService.saveAccount(account);
            }
            transactionManager.commit(transactionStatus);
        } catch (TransactionException e) {
            transactionManager.rollback(transactionStatus);
            throw new RuntimeException(e);
        }
    }

    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void insert1() {
        try {
            for (int i = 0; i < 3; i++) {
                if (i == 2) {
                    throw new RuntimeException();
                }
                Account account = new Account();
                account.setName("姓名" + i);
                account.setMoney(20000f + i);
                // 每次保存操作都是一个事物
                accountService.saveAccount(account);
            }
        } catch (TransactionException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void find() {
        Account account = new Account();
        account.setId(2);
        List<Map<String, Object>> accountList = accountService.find(account);
        System.out.println(accountList);
    }

    @Test
    public void findById() {
        Account account = accountService.findById(1);
        System.out.println(account);
    }
}
