package com.xw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
@SpringBootApplication
@EnableEurekaClient
public class SeckillDemoTimeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoTimeServerApplication.class, args);
    }
}
