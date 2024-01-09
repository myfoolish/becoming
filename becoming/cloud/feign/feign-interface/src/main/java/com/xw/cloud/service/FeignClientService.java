package com.xw.cloud.service;

import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liuxiaowei
 * @description
 * @date 2024/1/8
 */
@FeignClient("feign-client")
public interface FeignClientService {

    @GetMapping("/hello")
    public String hello();

    @PostMapping("hello")
    public Friend helloPost(@RequestBody Friend friend);

    @Data
    public static class Friend {
        private String name;
        private String port;
    }

    @GetMapping("/retry")
    public String retry(@RequestParam("timeout") int timeout);
}
