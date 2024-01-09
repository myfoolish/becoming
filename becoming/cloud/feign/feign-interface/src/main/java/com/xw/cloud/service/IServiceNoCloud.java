package com.xw.cloud.service;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author liuxiaowei
 * @description
 * @date 2024/1/8
 */
public interface IServiceNoCloud {

    public String hello();

    public Friend helloPost(@RequestBody Friend friend);

    @Data
    public static class Friend {
        private String name;
        private String port;
    }
}
