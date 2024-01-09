package com.xw.springsecurity.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/10/20 17:00
 */
@RestController
@RequestMapping("/")
public class HelloController {

    // 拥有 USER 或者 ADMIN 角色
    // @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping("/hello")
    public String hello() {
        return "Hello SpringSecurity!";
    }

    // @PreAuthorize("hasAnyRole('ADMIN')")
    // @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/admin")
    public String adminInfo() {
        return "AdminInfo!";
    }

    // 拥有 USER 或者 ADMIN 角色
    // @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    // 同时拥有 USER 和 ADMIN 角色
    // @PreAuthorize("hasRole('USER') and hasRole('ADMIN')")
    @RequestMapping("/user")
    public String userInfo() {
        return "UserInfo!";
    }

    // 获取当前登录用户的信息
    @RequestMapping("/getCurrentUser")
    public String getCurrentUserInfo() {
        String currentUser = "";
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            currentUser = ((UserDetails) principal).getUsername();
        } else {
            currentUser = principal.toString();
        }
        return "CurrentUser is: " + currentUser;
    }


    // 对应 SecurityConfig1 的EnableGlobalMethodSecurity 测试注解 PreAuthorize
    @RequestMapping("/info01")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String info01() {
        return "Info01";
    }

    @RequestMapping("/info02")
    @PreAuthorize("hasRole('USER') and hasRole('ADMIN')")
    public String info02() {
        return "Info02";
    }
}
