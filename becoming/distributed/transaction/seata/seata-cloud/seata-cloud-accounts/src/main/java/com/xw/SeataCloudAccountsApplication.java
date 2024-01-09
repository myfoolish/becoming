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
public class SeataCloudAccountsApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SeataCloudAccountsApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
