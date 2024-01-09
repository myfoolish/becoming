package com.xw.cloud.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/11/14
 */
@RestController
@RequestMapping("/")
@Slf4j
public class HelloController {

    @Autowired
    private LoadBalancerClient client;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        ServiceInstance instance = client.choose("eureka-client");
        if (instance == null) {
            return "No available instances";
        }

        String target = String.format("http://%s:%s/hello", instance.getHost(), instance.getPort());
        log.info("the url is {}", target);
        return restTemplate.getForObject(target, String.class);
    }

    @PostMapping("hello")
    public Friend helloPost() {
        Friend friend = new Friend();

        ServiceInstance instance = client.choose("eureka-client");
        if (instance == null) {
            return friend;
        }

        String target = String.format("http://%s:%s/hello", instance.getHost(), instance.getPort());
        log.info("the url is {}", target);

        friend.setName("Eureka Consumer");
        return restTemplate.postForObject(target,friend,Friend.class);
    }

    @Data
    public static class Friend {
        private String name;
        private String port;
    }
}
