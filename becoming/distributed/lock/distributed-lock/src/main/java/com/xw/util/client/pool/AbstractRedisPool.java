package com.xw.util.client.pool;

import com.xw.util.client.inter.IRedisPool2;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import redis.clients.jedis.commands.JedisCommands;

public abstract class AbstractRedisPool<T extends JedisCommands> implements IRedisPool2<T> {
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);
    private final Set<Integer> subscribeClients = new HashSet();

    public AbstractRedisPool() {
    }

    public void register(T j) {
        this.lock.writeLock().lock();

        try {
            this.subscribeClients.add(System.identityHashCode(j));
        } finally {
            this.lock.writeLock().unlock();
        }

    }

    public void unregister(T j) {
        this.lock.writeLock().lock();

        try {
            this.subscribeClients.remove(System.identityHashCode(j));
        } finally {
            this.lock.writeLock().unlock();
        }

    }

    public boolean subscribing(T j) {
        this.lock.readLock().lock();

        boolean ret;
        try {
            ret = this.subscribeClients.contains(System.identityHashCode(j));
        } catch (Throwable var7) {
            ret = false;
        } finally {
            this.lock.readLock().unlock();
        }

        return ret;
    }
}
