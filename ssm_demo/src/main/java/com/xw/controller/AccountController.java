package com.xw.controller;

import com.xw.entity.Account;
import com.xw.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/8
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    // 将 service 接口注入到 controller 层，实现具体业务
    //注意:这时候，如果 spring 的配置文件名称不是 applicationContext.xml，service 层接口就用不了。
    @Autowired
    private AccountService accountService;

    /**
     * 保存
     * @param account
     * @return
     */
    @RequestMapping("/save")
    public String save(Account account) {
        System.out.println("保存账户");
        accountService.saveAccount(account);
        return "success";
    }

    @RequestMapping("/findAll")
    public ModelAndView findAll() {
        List<Account> accountList = accountService.findAll();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("success");
        modelAndView.addObject("accountList", accountList);
        return modelAndView;
    }
}
