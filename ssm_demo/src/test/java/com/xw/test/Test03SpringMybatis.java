package com.xw.test;

import com.xw.entity.Account;
import com.xw.service.AccountService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author liuxiaowei
 * @description Spring 整合 Junit 测试
 * @date 2021/6/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class Test03SpringMybatis {

    @Autowired
    private AccountService accountService;

    @Test
    public void testFindAll() {
        List<Account> accountList = accountService.findAll();
        for (Account account : accountList) {
            System.out.println(account);
        }
    }

    @Test
    public void testSave() {
        Account account = new Account();
        account.setName("晓威");
        account.setMoney(200000);
        accountService.saveAccount(account);
    }
}
