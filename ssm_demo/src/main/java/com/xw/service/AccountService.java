package com.xw.service;

import com.xw.entity.Account;

import java.util.List;

/**
 * @author liuxiaowei
 * @description 业务接口
 * @date 2021/6/2
 */
public interface AccountService {

    void saveAccount(Account account);

    List<Account> findAll();
}
