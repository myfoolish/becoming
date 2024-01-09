package com.xw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/7/13
 */
@SpringBootApplication
@EnableEurekaClient
public class SeckillDemoStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoStorageApplication.class, args);
    }
}
