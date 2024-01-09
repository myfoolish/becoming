package com.xw.util.client.inter;

import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.util.Pool;

public interface IRedisPool2<T extends JedisCommands> extends IRedisPool<T, Pool<T>> {
    void register(T var1);

    void unregister(T var1);

    boolean subscribing(T var1);

    default T getSubscribeResource() {
        T resource = this.getResource();
        this.register(resource);
        return resource;
    }
}
