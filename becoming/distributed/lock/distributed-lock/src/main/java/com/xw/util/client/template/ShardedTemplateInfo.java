package com.xw.util.client.template;

import com.xw.util.client.pool.RedisSinglePool;
import java.util.Set;

import redis.clients.jedis.JedisPoolConfig;

public class ShardedTemplateInfo extends ShardedComponentInfo<RedisPoolTemplate> {
    public ShardedTemplateInfo(Set<String> sentinels, String masterName) {
        super(sentinels, masterName);
    }

    public ShardedTemplateInfo(Set<String> sentinels, String masterName, String password) {
        super(sentinels, masterName, password);
    }

    public ShardedTemplateInfo(Set<String> sentinels, String masterName, String password, JedisPoolConfig poolConfig) {
        super(sentinels, masterName, password, poolConfig);
    }

    public RedisPoolTemplate createResource() {
        RedisSinglePool pool = this.getPool();
        return new RedisPoolTemplate(pool);
    }
}
