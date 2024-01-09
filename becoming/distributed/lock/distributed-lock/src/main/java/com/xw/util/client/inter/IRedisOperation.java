package com.xw.util.client.inter;

import redis.clients.jedis.Jedis;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/21
 */
public interface IRedisOperation <T> extends IGenericOperation<T, Jedis> {
    T exec(Jedis var1);
}
