package com.xw.util.client.pool;

import com.xw.util.client.inter.IRedisPool2;
import java.lang.management.ManagementFactory;
import java.util.Set;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

public class RedisSentinelPool extends AbstractRedisPool<Jedis> implements IRedisPool2<Jedis> {
    private final JedisSentinelPool pool;
    private final String masterName;

    public RedisSentinelPool(Set<String> sentinels, String masterName, JedisPoolConfig config) {
        this(sentinels, masterName, (String)null, config);
    }

    public RedisSentinelPool(Set<String> sentinels, String masterName, String password, JedisPoolConfig config) {
        this.masterName = masterName;
        this.pool = new JedisSentinelPool(masterName, sentinels, config, 2000, 2000, (String)null, password, 0, ManagementFactory.getRuntimeMXBean().getName());
    }

    public JedisSentinelPool getPool() {
        return this.pool;
    }

    public String getConnectionInfo() {
        return this.pool.getCurrentHostMaster().toString();
    }

    public String getRedisUrl() {
        return this.masterName;
    }

    public boolean isPool() {
        return true;
    }

    /** @deprecated */
    @Deprecated
    public Jedis getResource() {
        return this.getPool().getResource();
    }
}
