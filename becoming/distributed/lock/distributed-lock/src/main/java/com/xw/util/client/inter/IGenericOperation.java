package com.xw.util.client.inter;

import redis.clients.jedis.commands.JedisCommands;

public interface IGenericOperation<T, R extends JedisCommands> {
    T exec(R var1);
}
