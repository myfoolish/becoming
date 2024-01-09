package com.xw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.xw.mapper"})
public class SeataShardingCloudAccountsApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SeataShardingCloudAccountsApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
