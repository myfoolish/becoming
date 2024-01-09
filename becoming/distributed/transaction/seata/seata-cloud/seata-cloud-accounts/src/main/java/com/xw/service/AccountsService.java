package com.xw.service;

import com.xw.entity.Accounts;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
public interface AccountsService {
    public Accounts findAccountById(Long accountId);

    public void reduceMoney(double money, Long accountId);
}
