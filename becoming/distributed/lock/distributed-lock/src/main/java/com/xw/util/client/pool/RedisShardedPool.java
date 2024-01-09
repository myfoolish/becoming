package com.xw.util.client.pool;

import com.xw.util.client.inter.IRedisPool2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.util.Pool;

public final class RedisShardedPool extends AbstractRedisPool<ShardedJedis> implements IRedisPool2<ShardedJedis> {
    private ShardedJedisPool pool;
    private String connectionInfo;

    public RedisShardedPool(Set<HostAndPort> haps, JedisPoolConfig poolConfig) {
        this(haps, (String)null, poolConfig);
    }

    public RedisShardedPool(Set<HostAndPort> haps, String password, JedisPoolConfig poolConfig) {
        List<JedisShardInfo> shardInfos = new ArrayList();
        StringBuilder buf = new StringBuilder();
        Iterator var7 = haps.iterator();

        while(var7.hasNext()) {
            HostAndPort hap = (HostAndPort)var7.next();
            buf.append(hap.toString()).append(",");
            JedisShardInfo info = new JedisShardInfo(hap.getHost(), hap.getPort());
            info.setPassword(password);
            shardInfos.add(info);
        }

        if (!shardInfos.isEmpty()) {
            buf.setLength(buf.length() - 1);
            this.connectionInfo = buf.toString();
            this.pool = new ShardedJedisPool(poolConfig, shardInfos);
        }
    }

    public boolean isPool() {
        return true;
    }

    public ShardedJedis getResource() {
        return this.pool.getResource();
    }

    /** @deprecated */
    @Deprecated
    public Pool<ShardedJedis> getPool() {
        return this.pool;
    }

    public String getConnectionInfo() {
        return this.connectionInfo;
    }

    public String getRedisUrl() {
        return this.connectionInfo;
    }
}
