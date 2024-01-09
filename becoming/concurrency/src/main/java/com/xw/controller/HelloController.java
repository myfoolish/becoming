package com.xw.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/3/17
 */
@RestController
@RequestMapping("/")
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello 并发！";
    }
}
