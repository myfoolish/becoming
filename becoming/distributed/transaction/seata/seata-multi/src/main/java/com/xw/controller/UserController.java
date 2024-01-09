package com.xw.controller;

import com.xw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/22
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("becomingXA")
    public String becomingXA() {
        userService.becomingXA();
        return "XA success";
    }

    @GetMapping("becomingAT")
    public String becomingAT() {
        userService.becomingAT();
        return "AT success";
    }
}
