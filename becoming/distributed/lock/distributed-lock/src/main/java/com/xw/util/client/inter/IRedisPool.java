package com.xw.util.client.inter;

import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.util.Pool;

public interface IRedisPool<R extends JedisCommands, T extends Pool<R>> extends IRedisConnection<R> {
    T getPool();

    String getConnectionInfo();

    String getRedisUrl();
}
