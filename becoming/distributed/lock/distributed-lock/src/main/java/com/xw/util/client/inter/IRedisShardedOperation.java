package com.xw.util.client.inter;

import redis.clients.jedis.ShardedJedis;

public interface IRedisShardedOperation<T> extends IGenericOperation<T, ShardedJedis> {
    T exec(ShardedJedis var1);
}
