package com.xw.lock.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author liuxiaowei
 * @description 基于Redis的Setnx实现分布式锁
 *  获取锁的redis命令：
 *  set resource_name random_value NX PX 30000
 *      resource_name：资源名称，可根据不同的业务区分不同的锁
 *      random_value：随机值，每个线程的随机值都不同，用于释放锁时的校验，可用uuid
 *      NX：key存在时设置不成功，key不存在时设置成功（利用NX的原子性，多线程并发时，只有一个线程可以设置成功）
 *      PX：自动失效时间，出现异常情况，锁可以过期失效
 *  释放锁的redis命令：
 *      释放锁采用redis的delete命令（释放锁的LUA脚本）
 *      释放锁时校验之前设置的随机数，相同才能释放
 *      LUA脚本:
 *      if redis.call("get", KEYS[1]) == ARGV[1]
 *      then
 *          return redis.call("del", KEYS[1])
 *      else
 *          return 0
 *      end
 *  AutoCloseable: 从jdk1.7之后，jdk官方提供了自动关闭的功能，只需要去实现AutoCloseable接口
 * @date 2023/7/14
 */
//@Component
public class RedisLock implements AutoCloseable{
    private final static Logger logger = LoggerFactory.getLogger(RedisLock.class);
    private RedisTemplate redisTemplate;
    private String key;
    private String value;
    // 过期时间，单位：秒
    private int expireTime;

    private static final String UNLOCK_LUA;

    String script = "if redis.call(\"get\", KEYS[1]) == ARGV[1]\n" +
            "then\n" +
            "    return redis.call(\"del\", KEYS[1])\n" +
            "else\n" +
            "    return 0\n" +
            "end";

    static {
        StringBuffer sb = new StringBuffer();
        sb.append("if redis.call(\"get\", KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\", KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    public RedisLock(RedisTemplate redisTemplate, String key, int expireTime) {
        this.redisTemplate = redisTemplate;
        this.key = key;
        this.value = UUID.randomUUID().toString();
        this.expireTime = expireTime;
    }

    /**
     * 获取redis分布式锁
     * @return
     */
    public boolean getLock() {
        RedisCallback redisCallback = redisConnection -> {
            // 设置NX
            RedisStringCommands.SetOption setOption = RedisStringCommands.SetOption.ifAbsent();
            // 设置过期时间
            Expiration expiration = Expiration.seconds(expireTime);
            // 序列化value
            byte[] redisValue = redisTemplate.getValueSerializer().serialize(value);
            // 序列化key
            byte[] redisKey = redisTemplate.getKeySerializer().serialize(key);
            // 执行setNX操作
            Boolean result = redisConnection.set(redisKey, redisValue, expiration, setOption);
            return result;
        };
        // 获取分布式锁
        Boolean lock = (Boolean) redisTemplate.execute(redisCallback);
        logger.info("获取锁的结果: " + lock);
        return lock;
    }

    /**
     * 释放redis分布式锁
     * @return
     */
    public boolean unLock() {
        RedisScript<Boolean> redisScript = RedisScript.of(UNLOCK_LUA, Boolean.class);
        List<String> keys = Arrays.asList(key);
        Boolean result = (Boolean) redisTemplate.execute(redisScript, keys, value);
        logger.info("释放锁的结果: " + result);
        return result;
    }

    @Override
    public void close() throws Exception {
        unLock();
    }
}
