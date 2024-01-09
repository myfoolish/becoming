package com.xw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@SpringBootApplication
@MapperScan("com.xw.mapper")
// @EnableTransactionManagement + @Transactional 只能解决单连接/数据源的事务【在使用sharding jdbc分库分表的时候，此方法可以解决】
public class SeataShardingApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SeataShardingApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
