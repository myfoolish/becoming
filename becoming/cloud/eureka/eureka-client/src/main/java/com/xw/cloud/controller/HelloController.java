package com.xw.cloud.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/11/9
 */
@RestController
@RequestMapping("/")
@Slf4j
public class HelloController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/hello")
    public String hello() {
        return "hello " + port;
    }

    @PostMapping("hello")
    public Friend helloPost(@RequestBody Friend friend) {
        log.info("hello: " + friend.getName());
        friend.setPort(port);
        return friend;
    }

    @Data
    public static class Friend {
        private String name;
        private String port;
    }
}
