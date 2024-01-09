package com.xw.controller;

import com.xw.entity.User;
import com.xw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping("become")
    public String become(@RequestBody User user) {
        userService.become(user);
        return "success";
    }

    @GetMapping("becomingXA")
    public String becomingXA(Integer fromId, Integer toId, double money) {
        userService.becomingXA(fromId, toId, money);
        return "success";
    }

    @GetMapping("becomingAT")
    public String becomingAT(Integer fromId, Integer toId, double money) {
        userService.becomingAT(fromId, toId, money);
        return "success";
    }
}
