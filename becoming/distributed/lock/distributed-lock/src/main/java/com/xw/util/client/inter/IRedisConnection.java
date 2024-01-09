package com.xw.util.client.inter;

import redis.clients.jedis.commands.JedisCommands;

public interface IRedisConnection<R extends JedisCommands> {
    boolean isPool();

    R getResource();
}
