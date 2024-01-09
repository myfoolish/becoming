package com.xw.spring.jdbc.service;

import com.xw.spring.jdbc.entity.Account;

import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/13
 */
public interface AccountService {
    void saveAccount(Account account);

    List<Map<String, Object>> find(Account account);

    Account findById(Integer id);
}
