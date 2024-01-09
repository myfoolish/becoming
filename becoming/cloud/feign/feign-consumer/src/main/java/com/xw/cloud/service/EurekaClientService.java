package com.xw.cloud.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liuxiaowei
 * @description 利用动态代理
 * @date 2023/11/30
 */
@FeignClient("eureka-client")   // 生成代理类
public interface EurekaClientService {

    @GetMapping("/hello")
    String hello();
}
