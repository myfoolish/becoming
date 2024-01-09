package com.xw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/2/17
 */
@SpringBootApplication
//@EnableScheduling
//@ImportResource(locations = "classpath:redisson.xml") // 引入spring配置文件
public class DistributeLockApplication {
    public static void main(String[] args) {
        SpringApplication.run(DistributeLockApplication.class, args);
    }
}
