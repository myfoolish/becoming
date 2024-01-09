package com.xw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
@SpringBootApplication
@EnableEurekaServer
public class SeckillDemoServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoServerApplication.class, args);
    }
}
