package com.xw;

import com.xw.interceptor.XAPutXidInterceptor;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/26
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableFeignClients
public class SeataShardingCloudBusinessApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SeataShardingCloudBusinessApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 增加拦截器，传递全局事务id
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        interceptors.add(new XAPutXidInterceptor());
        restTemplate.setInterceptors(interceptors);

        return restTemplate;
    }
}
