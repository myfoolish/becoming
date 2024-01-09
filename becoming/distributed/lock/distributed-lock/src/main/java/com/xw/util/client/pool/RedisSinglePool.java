package com.xw.util.client.pool;

import com.xw.util.client.inter.IRedisPool2;
import java.lang.management.ManagementFactory;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public final class RedisSinglePool extends AbstractRedisPool<Jedis> implements IRedisPool2<Jedis> {
    private final JedisPool pool;
    private final String connectionInfo;

    public RedisSinglePool(String host, int port, JedisPoolConfig config) {
        this(host, port, (String)null, config);
    }

    public RedisSinglePool(String host, int port, String password, JedisPoolConfig config) {
        this.connectionInfo = (new HostAndPort(host, port)).toString();
        this.pool = new JedisPool(config, host, port, 2000, password, 0, ManagementFactory.getRuntimeMXBean().getName());
    }

    /** @deprecated */
    @Deprecated
    public JedisPool getPool() {
        return this.pool;
    }

    public String getConnectionInfo() {
        return this.connectionInfo;
    }

    public String getRedisUrl() {
        return this.connectionInfo;
    }

    public boolean isPool() {
        return true;
    }

    public Jedis getResource() {
        return this.pool.getResource();
    }
}
