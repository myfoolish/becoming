package com.xw.cloud.controller;

import com.xw.cloud.service.EurekaClientService;
import com.xw.cloud.service.FeignClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/11/30
 */
@RestController
@RequestMapping("/")
public class FeignConsumerController {

    @Autowired
    private EurekaClientService eurekaClientService;

    @Autowired
    private FeignClientService feignClientService;

    @GetMapping("/hello1")
    public String hello1() {
        return eurekaClientService.hello();
    }

    @GetMapping("/hello2")
    public String hello2() {
        return feignClientService.hello();
    }

    @GetMapping("/retry")
    public String retry(Integer timeout) {
        return feignClientService.retry(timeout);
    }
}
