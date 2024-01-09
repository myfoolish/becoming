package com.xw.cloud.controller;

import com.xw.cloud.service.FeignClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuxiaowei
 * @description
 * @date 2024/1/8
 */
@RestController
@Slf4j
public class HelloController implements FeignClientService {

    @Value("${server.port}")
    private String port;

    @Override
    public String hello() {
        return "hello " + port;
    }

    @Override
    public Friend helloPost(Friend friend) {
        log.info("hello: " + friend.getName());
        friend.setPort(port);
        return friend;
    }

    @Override
    public String retry(int timeout) {
        while (timeout-- > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        log.info("retry " + port);
        return port;
    }
}
