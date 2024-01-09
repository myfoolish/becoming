package com.xw.service.impl;

import com.xw.mapper.AccountMapper;
import com.xw.entity.Account;
import com.xw.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/2
 */
@Service("accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public void saveAccount(Account account) {
        System.out.println("保存账户");
        accountMapper.saveAccount(account);
    }

    @Override
    public List<Account> findAll() {
        System.out.println("查询所有");
        return accountMapper.findAll();
    }
}
