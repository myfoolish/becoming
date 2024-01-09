package com.xw.util.client.model;

import com.xw.util.client.inter.IRedisPool2;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import redis.clients.jedis.JedisPoolConfig;

public abstract class AbstractPoolBuilder<T extends IRedisPool2<?>> {
    static final Map<String, AbstractPoolBuilder<?>> poolBuilders = new ConcurrentHashMap();
    private final String redisUrl;
    private final int cap;
    private final LinkedList<T> pools = new LinkedList();

    public AbstractPoolBuilder(String redisUrl) {
        poolBuilders.putIfAbsent(redisUrl, this);
        this.redisUrl = redisUrl;
        this.cap = 10;
    }

    public AbstractPoolBuilder(String redisUrl, int size) {
        Class var3 = AbstractPoolBuilder.class;
        synchronized(AbstractPoolBuilder.class) {
            poolBuilders.putIfAbsent(redisUrl, this);
        }

        this.redisUrl = redisUrl;
        this.cap = size;
    }

    public T getPool(JedisPoolConfig config) {
        synchronized(this.pools) {
            IRedisPool2 ret;
            if (this.pools.size() == this.cap) {
                ret = (IRedisPool2)this.pools.poll();
//                this.pools.offer(ret);
                this.pools.offer((T) ret);
            } else {
                ret = this.createPool(this.redisUrl, config);
            }
//            return ret;
            return (T) ret;
        }
    }

    public void destory(T pool) {
        synchronized(this.pools) {
            this.pools.remove(pool);
            pool.getPool().destroy();
            if (this.pools.isEmpty()) {
                poolBuilders.remove(this.redisUrl);
            }

        }
    }

    protected abstract T createPool(String var1, JedisPoolConfig var2);
}
