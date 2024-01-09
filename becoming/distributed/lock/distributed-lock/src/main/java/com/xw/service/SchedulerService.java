package com.xw.service;

import com.xw.lock.redis.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author liuxiaowei
 * @description 利用分布式锁 解决定时任务集群部署，任务重复执行的问题
 *              《执行此批量需要在启动类增加/打开注解 @EnableScheduling》
 * @date 2023/7/21
 */
@Service
public class SchedulerService {

    @Autowired
    private RedisTemplate redisTemplate;

//    @Scheduled(cron = "0/30 * * * * ? ")    // 每30秒执行一次
    @Scheduled(cron = "30 * * * * ? ")  // 只在xxxx年xx月xx日 xx:xx:30执行
    public void batch() {
        try (RedisLock redisLock = new RedisLock(redisTemplate, "batch", 20)) {
            if (redisLock.getLock()) {
                System.out.println("利用分布式锁解决定时任务集群部署，任务重复执行的问题...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
