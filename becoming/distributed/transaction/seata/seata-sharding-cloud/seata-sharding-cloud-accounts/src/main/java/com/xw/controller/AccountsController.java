package com.xw.controller;

import com.xw.entity.Accounts;
import com.xw.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@RestController
@RequestMapping("accounts")
public class AccountsController {

    @Autowired
    private AccountsService accountsService;

    @GetMapping("findAccountById/{accountId}")
    public Accounts findAccountById(@PathVariable("accountId") Long accountId) {
        return accountsService.findAccountById(accountId);
    }

    @PutMapping("reduceMoney")
    public String reduceMoney(double money, Long accountId) {
        accountsService.reduceMoney(money, accountId);
        return "扣款成功";
    }
}
