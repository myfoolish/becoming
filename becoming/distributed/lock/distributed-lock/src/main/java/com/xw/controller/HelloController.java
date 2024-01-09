package com.xw.controller;

import com.xw.entity.DistributeLock;
import com.xw.lock.redis.RedisLock;
import com.xw.lock.zookeeper.ZookeeperLock;
import com.xw.mapper.DistributeLockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/12
 */
@RestController
@RequestMapping
@Slf4j
public class HelloController {

    private Lock lock = new ReentrantLock();

    @RequestMapping("/hello")
    public String hello() {
        return "hello controller!";
    }

    @RequestMapping("/singleLock")
    public String singleLock() {
        log.info("进入了方法");
        lock.lock();
        log.info("进入了锁");
        try {
            Thread.sleep(60000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lock.unlock();
        return "这里是单体锁";
    }

    @Autowired
    private DistributeLockMapper distributeLockMapper;

    @RequestMapping("/mysqlDistributeLock")
    @Transactional(rollbackFor = Exception.class)
    public String mysqlDistributeLock() throws Exception {
        log.info("进入了方法");
        DistributeLock distributeLock = distributeLockMapper.selectDistributeLock("distribute_lock");
        if (distributeLock == null) {
            // spring 默认回滚的是runtimeException异常
            // 现在抛出的是Exception，所以需要在Transactional中指定
            throw new Exception("分布式锁找不到");
        }
        log.info("进入了锁");
        try {
            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "这里是Mysql分布式锁";
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 实现AutoCloseable接口之前
     * @return
     */
    @RequestMapping("/redisDistributeLock")
    @Transactional(rollbackFor = Exception.class)
    public String redisDistributeLock() {
        log.info("进入了方法");
        String value = UUID.randomUUID().toString();
        String key = "redisDistributeLock";
        RedisLock redisLock = new RedisLock(redisTemplate, key, 30);
        // 获取分布式锁
        if (redisLock.getLock()) {
            log.info("进入了锁");
            try {
                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "这里是Redis分布式锁";
    }

    /**
     * 实现AutoCloseable接口之后建议这样写
     * @return
     */
    @RequestMapping("/redisDistributeLock2")
    @Transactional(rollbackFor = Exception.class)
    public String redisDistributeLock2() {
        log.info("进入了方法");
        String value = UUID.randomUUID().toString();
        String key = "redisDistributeLock";
        try (RedisLock redisLock = new RedisLock(redisTemplate, key, 30)) {
            // 获取分布式锁
            if (redisLock.getLock()) {
                log.info("进入了锁");
                Thread.sleep(20000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "这里是Redis分布式锁";
    }

    @Autowired
    private RedissonClient redisson;

    /**
     * 通过Java API方式引入Redisson
     * @return
     */
    @RequestMapping("/redissonDistributeLock")
    @Transactional(rollbackFor = Exception.class)
    public String redissonDistributeLock() {
        // 通过Java API方式引入Redisson start
        // 1. Create config object
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setPassword("xwcoding");
        // 2. Create Redisson instance
        // Sync and Async API
//        RedissonClient redisson = Redisson.create(config);
        // 3. Get Redis based implementation of java.util.concurrent.ConcurrentMap
//        RMap<String, Object> map = redisson.getMap("myMap");
        // 4. Get Redis based implementation of java.util.concurrent.locks.Lock
//        RLock rLock = redisson.getLock("order");
        // 通过Java API方式引入Redisson end

        // 通过通过SpringBoot项目引入Redisson
        RLock rLock = redisson.getLock("order");
        log.info("进入了方法");
        try {
            rLock.lock(30, TimeUnit.SECONDS);
            log.info("拿到锁🔒");
            Thread.sleep(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            log.info("释放锁🔒");
            rLock.unlock();
        }
        return "这里是Redisson分布式锁";
    }

    /**
     * 实现AutoCloseable接口之后建议这样写
     * @return
     */
    @RequestMapping("/zookeeperDistributeLock")
    @Transactional(rollbackFor = Exception.class)
    public String zookeeperDistributeLock() {
        log.info("进入了方法");
        try (ZookeeperLock zookeeperLock = new ZookeeperLock()) {
            // 获取分布式锁
            if (zookeeperLock.getLock("order")) {
                log.info("进入了锁");
                Thread.sleep(20000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "这里是Zookeeper分布式锁";
    }

    @Bean(initMethod = "start",destroyMethod = "close")
    public CuratorFramework getCuratorFramework() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.31.197:2181", retryPolicy);
        return client;
    }

    @Autowired
    private CuratorFramework client;

    @RequestMapping("/zookeeperCuratorDistributeLock")
    @Transactional(rollbackFor = Exception.class)
    public String zookeeperCuratorDistributeLock() {
        log.info("进入了方法");
        InterProcessMutex lock = new InterProcessMutex(client, "/order");
        try {
            // 获取分布式锁
            if (lock.acquire(20, TimeUnit.SECONDS)) {
                // do some work inside of the critical section here
                log.info("进入了锁");
                Thread.sleep(20000);
//                System.out.println("拿到锁🔒");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                lock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "这里是Zookeeper-Curator分布式锁";
    }
}
