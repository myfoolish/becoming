package com.xw.controller;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author liuxiaowei
 * @description 单机版的限流组件
 * @date 2023/10/26
 */
@RestController
@Slf4j
public class HelloController {
    RateLimiter limiter = RateLimiter.create(2.0);

    /**
     * 非阻塞限流
     *
     * @return
     */
    @GetMapping("/tryAcquire")
    public String tryAcquire(Integer count) {
        if (limiter.tryAcquire(count)) {
            log.info("success, the rate is {}", limiter.getRate());
            return "success";
        } else {
            log.info("fail, the rate is {}", limiter.getRate());
            return "fail";
        }
    }

    /**
     * 限定时间的非阻塞限流
     *
     * @return
     */
    @GetMapping("/tryAcquireWithTimeout")
    public String tryAcquire(Integer count, Integer timeout) {
        if (limiter.tryAcquire(count, timeout, TimeUnit.SECONDS)) {
            log.info("success, the rate is {}", limiter.getRate());
            return "success";
        } else {
            log.info("fail, the rate is {}", limiter.getRate());
            return "fail";
        }
    }

    /**
     * 同步阻塞限流
     *
     * @return
     */
    @GetMapping("/acquire")
    public String acquire(Integer count) {
        limiter.acquire(count);
        log.info("success, the rate is {}", limiter.getRate());
        return "success";
    }
}
