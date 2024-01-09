package com.xw.service.impl;

import com.xw.entity.Accounts;
import com.xw.mapper.AccountsMapper;
import com.xw.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@Service
public class AccountsServiceImpl implements AccountsService {
    @Autowired
    private AccountsMapper accountsMapper;

    @Override
    public Accounts findAccountById(Long accountId) {
        return accountsMapper.findAccountById(accountId);
    }

    @Override
    public void reduceMoney(double money, Long accountId) {
        Accounts account = accountsMapper.findAccountById(accountId);
        if (account.getAccountMoney() < money) {
            throw new RuntimeException("余额不足");
        }
        accountsMapper.reduceMoney(money, accountId);
    }
}
