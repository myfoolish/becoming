package com.xw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
@SpringBootApplication
@EnableEurekaClient
public class SeckillDemoStockApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoStockApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
