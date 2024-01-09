package com.xw.util.client.template;

import com.xw.util.client.inter.ISpecificPool;
import com.xw.util.client.inter.ISubTemplate;
import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class GlobalSubTemplateThreadPool implements ISpecificPool {
    private static final HashMap<String, GlobalSubTemplateThreadPool> pools = new HashMap();
    private final String redisUrl;
    private final ThreadPoolExecutor staticPool;
    private final ThreadPoolExecutor threadPool;
    private final Set<Class<? extends ISubTemplate>> subTemplateClasses;
    private final Map<String, Integer> baseThreadCounts;
    private final AtomicInteger staticThreads;
    private final AtomicInteger activeThreads;

    private GlobalSubTemplateThreadPool() {
        this.staticPool = new ThreadPoolExecutor(1, 1, 300L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        this.threadPool = new ThreadPoolExecutor(3, 10, 300L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(5), new ThreadPoolExecutor.CallerRunsPolicy());
        this.subTemplateClasses = new HashSet();
        this.baseThreadCounts = new HashMap();
        this.staticThreads = new AtomicInteger(0);
        this.activeThreads = new AtomicInteger(1);
        this.redisUrl = null;
    }

    private GlobalSubTemplateThreadPool(String redisUrl) {
        this.staticPool = new ThreadPoolExecutor(1, 1, 300L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue());
        this.threadPool = new ThreadPoolExecutor(3, 10, 300L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(5), new ThreadPoolExecutor.CallerRunsPolicy());
        this.subTemplateClasses = new HashSet();
        this.baseThreadCounts = new HashMap();
        this.staticThreads = new AtomicInteger(0);
        this.activeThreads = new AtomicInteger(1);
        this.redisUrl = redisUrl;
    }

    public static synchronized GlobalSubTemplateThreadPool getGlobalSubTemplateThreadPool(String redisUrl) {
        if (!pools.containsKey(redisUrl)) {
            pools.put(redisUrl, new GlobalSubTemplateThreadPool(redisUrl));
        }

        return (GlobalSubTemplateThreadPool)pools.get(redisUrl);
    }

    public GlobalSubTemplateThreadPool nThread(int n) {
        this.activeThreads.set(n);
        if (n > this.threadPool.getActiveCount()) {
            this.threadPool.setCorePoolSize(n);
            this.threadPool.setMaximumPoolSize(2 * n);
        }

        return this;
    }

    private void refreshStaticThreadPool() {
        this.staticPool.setMaximumPoolSize(this.staticThreads.get());
        this.staticPool.setCorePoolSize(this.staticThreads.get());
    }

    public void shutdown() {
        this.staticPool.shutdown();
        this.threadPool.shutdown();
        this.close();
    }

    public void shutdownNow() {
        this.staticPool.shutdownNow();
        this.threadPool.shutdownNow();
        this.close();
    }

    public synchronized void close() {
        if (this.redisUrl != null && pools.containsKey(this.redisUrl)) {
            pools.remove(this.redisUrl);
        }

        this.subTemplateClasses.clear();
        this.baseThreadCounts.clear();
    }

    public void execute(Runnable command) {
        if (command instanceof JedisPoolSubTemplate.SubListener) {
            this.staticPool.execute(command);
        } else {
            this.threadPool.execute(command);
        }

    }

    public synchronized void updatePool(String oldUrl, String newUrl) {
        if (oldUrl.equals(this.redisUrl)) {
            pools.remove(oldUrl);
            pools.put(newUrl, this);
        }

    }

    public synchronized GlobalSubTemplateThreadPool register(@NotNull ISubTemplate obj) {
        Class<? extends ISubTemplate> regClazz = obj.getClass();
        int parentThreadCount = 0;
        int baseThreadCount = obj.getBaseThreadCount();
        Iterator var5 = this.subTemplateClasses.iterator();

        while(var5.hasNext()) {
            Class<?> clazz = (Class)var5.next();
            if (regClazz.equals(clazz)) {
                return this;
            }

            if (regClazz.isAssignableFrom(clazz)) {
                this.registerClazz(regClazz, baseThreadCount);
                return this;
            }

            if (clazz.isAssignableFrom(regClazz)) {
                parentThreadCount = Math.max(parentThreadCount, (Integer)this.baseThreadCounts.get(clazz.getCanonicalName()));
            }
        }

        this.registerClazz(regClazz, baseThreadCount);
        this.staticThreads.addAndGet(baseThreadCount - parentThreadCount);
        this.refreshStaticThreadPool();
        return this;
    }

    private void registerClazz(Class<? extends ISubTemplate> regClazz, int baseThreadCount) {
        this.subTemplateClasses.add(regClazz);
        this.baseThreadCounts.put(regClazz.getCanonicalName(), baseThreadCount);
    }
}
