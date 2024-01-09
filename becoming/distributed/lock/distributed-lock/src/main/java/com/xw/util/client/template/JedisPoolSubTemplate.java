package com.xw.util.client.template;

import com.xw.util.client.inter.IRedisPool2;
import com.xw.util.client.inter.ISpecificPool;
import com.xw.util.client.inter.ISubscribeCallback;
import com.xw.util.client.inter.ISubscribeCallback2;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Protocol.Command;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

public abstract class JedisPoolSubTemplate extends GenericPoolTemplate<Jedis> {
    protected Logger logger = Logger.getLogger(this.getClass().getName());
    private static final String DEFAULT_CLIENT_NAME = ManagementFactory.getRuntimeMXBean().getName();
    public static final String KEYSPACE_PREFIX = "__keyspace@0__:";
    private static final ThreadPoolExecutor callbackPool;
    private ISpecificPool specificPool = null;
    private static final AtomicBoolean globalPoolSetted;
    private final Map<String, SubListener> listeners = new HashMap();
    private static final Map<String, Map<String, SubListener>> staticListeners;
    private boolean useStaticListeners = false;

    public JedisPoolSubTemplate useStaticListeners() {
        Iterator var1 = this.listeners.values().iterator();

        while(var1.hasNext()) {
            SubListener listener = (SubListener)var1.next();
            listener.shutdown();
        }

        this.listeners.clear();
        this.useStaticListeners = true;
        if (!staticListeners.containsKey(this.pool.getRedisUrl())) {
            staticListeners.put(this.pool.getRedisUrl(), new HashMap());
        }

        return this;
    }

    private Map<String, SubListener> getListeners() {
        if (this.useStaticListeners) {
            if (!staticListeners.containsKey(this.pool.getRedisUrl())) {
                staticListeners.put(this.pool.getRedisUrl(), new HashMap());
            }

            return (Map)staticListeners.get(this.pool.getRedisUrl());
        } else {
            return this.listeners;
        }
    }

    protected JedisPoolSubTemplate(IRedisPool2<Jedis> pool) {
        super(pool);
    }

    protected void setSpecificPool(ISpecificPool specificPool) {
        this.specificPool = specificPool;
    }

    public void subscribe(ISubscribeCallback callback, String channel) {
        SubListener listener = (SubListener)this.getListeners().get(channel);
        if (listener == null) {
            this.getListeners().put(channel, listener = new SubListener(channel, callback));
            this.submit(listener);
        } else {
            listener.addCallback(callback);
        }

    }

    public void psubscribe(ISubscribeCallback callback, String channel) {
        SubListener listener = (SubListener)this.getListeners().get(channel);
        if (listener == null) {
            this.getListeners().put(channel, listener = (new SubListener(channel, callback)).enablePattern());
            this.submit(listener);
        } else {
            listener.addCallback(callback);
        }

    }

    public void unsubscribe(String channel) {
        if (this.getListeners().containsKey(channel)) {
            ((SubListener)this.getListeners().get(channel)).shutdown();
        }

    }

    public void resizeThreadPool(int corePoolSize, int maxPoolSize) {
        if (!globalPoolSetted.get()) {
            callbackPool.setCorePoolSize(corePoolSize);
            callbackPool.setMaximumPoolSize(maxPoolSize);
        }
    }

    public static void resizeGlobalThreadPool(int corePoolSize, int maxPoolSize) {
        globalPoolSetted.set(true);
        callbackPool.setCorePoolSize(corePoolSize);
        callbackPool.setMaximumPoolSize(maxPoolSize);
    }

    public void release() {
        Iterator var1 = this.getListeners().values().iterator();

        while(var1.hasNext()) {
            SubListener listener = (SubListener)var1.next();
            listener.shutdown();
        }

        this.getListeners().clear();
        callbackPool.shutdownNow();
        if (this.specificPool != null) {
            this.specificPool.shutdownNow();
        }

        staticListeners.remove(this.pool.getRedisUrl());
        super.release();
    }

    public int getPoolActiveCount() {
        return callbackPool.getActiveCount();
    }

    protected void submit(Runnable runnable) {
        if (this.specificPool != null) {
            this.specificPool.execute(runnable);
        } else {
            callbackPool.execute(runnable);
        }

    }

    public void updatePool(IRedisPool2<Jedis> pool) {
        if (this.useStaticListeners) {
            Map<String, SubListener> listeners = (Map)staticListeners.remove(this.pool.getRedisUrl());
            if (listeners == null) {
                listeners = new HashMap();
            }

            staticListeners.put(pool.getRedisUrl(), listeners);
            this.specificPool.updatePool(this.pool.getRedisUrl(), pool.getRedisUrl());
        } else {
            Map<String, SubListener> listeners = this.listeners;
        }

        super.updatePool(pool);
    }

    static {
        callbackPool = new ThreadPoolExecutor(2, 5, 300L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(20), new ThreadPoolExecutor.CallerRunsPolicy());
        globalPoolSetted = new AtomicBoolean(false);
        staticListeners = new HashMap();
    }

    class SubListener implements Runnable {
        private AtomicBoolean listening = new AtomicBoolean(true);
        private String channel;
        private boolean isEnablePattern = false;
        private final List<ISubscribeCallback> callbacks = new LinkedList();
        private final RedisPoolTemplate template;
        private Jedis jedis = null;

        SubListener(String channel, ISubscribeCallback callback) {
            this.channel = channel;
            this.callbacks.add(callback);
            if (callback instanceof ISubscribeCallback2) {
                ((ISubscribeCallback2)callback).setTemplate(this.template = new RedisPoolTemplate(JedisPoolSubTemplate.this.pool));
            } else {
                this.template = null;
            }

        }

        void addCallback(ISubscribeCallback callback) {
            this.callbacks.add(callback);
        }

        SubListener enablePattern() {
            this.isEnablePattern = true;
            return this;
        }

        public void shutdown() {
            this.listening.set(false);
            if (this.jedis != null) {
                this.jedis.disconnect();
                this.jedis.ping();
                this.jedis = null;
            }

            if (this.template != null) {
                this.template.release();
            }

            this.callbacks.clear();
        }

        private void doCallback(final String channel, final String message) {
            final String channelStr;
            if (!channel.startsWith("__keyspace@0__:") && !channel.startsWith("__keyevent@0__:")) {
                channelStr = channel;
            } else {
                channelStr = channel.substring(15);
            }

            JedisPoolSubTemplate.this.submit(new Runnable() {
                public void run() {
                    Thread.currentThread().setName(String.format("Redis-Callback-%s with %d listener(s)", channel, SubListener.this.callbacks.size()));

                    ISubscribeCallback callback;
                    for(Iterator var1 = SubListener.this.callbacks.iterator(); var1.hasNext(); callback.onMessage(channelStr, message)) {
                        callback = (ISubscribeCallback)var1.next();
                        if (SubListener.this.template != null && callback instanceof ISubscribeCallback2) {
                            ((ISubscribeCallback2)callback).setTemplate(SubListener.this.template);
                        }
                    }

                }
            });
        }

        public void run() {
            while(true) {
                if (this.listening.get()) {
                    try {
                        this.jedis = (Jedis)JedisPoolSubTemplate.this.pool.getSubscribeResource();
                        if (this.template != null) {
                            this.template.updatePool(JedisPoolSubTemplate.this.pool);
                        }

                        String clientName = String.format("Redis-%sSubscribe-[connection %s[@%h] pool@%h],[channel %s in template@%h]", this.isEnablePattern ? "P" : "", JedisPoolSubTemplate.this.pool.getConnectionInfo(), this.jedis.hashCode(), JedisPoolSubTemplate.this.pool.hashCode(), this.channel, this.hashCode());
                        Thread.currentThread().setName(clientName);
                        this.jedis.clientSetname(String.format("%s[%h]-channel[%s]-template[%h]", JedisPoolSubTemplate.DEFAULT_CLIENT_NAME, this.jedis.hashCode(), this.channel, this.hashCode()));
                        JedisPoolSubTemplate.this.logger.info("开始订阅" + this.channel);
                        if (this.isEnablePattern) {
                            this.jedis.psubscribe(new JedisPubSub() {
                                public void onPMessage(String pattern, String channel, String message) {
                                    SubListener.this.doCallback(channel, message);
                                }
                            }, new String[]{this.channel});
                            continue;
                        }

                        this.jedis.subscribe(new JedisPubSub() {
                            public void onMessage(String channel, String message) {
                                SubListener.this.doCallback(channel, message);
                            }
                        }, new String[]{this.channel});
                        continue;
                    } catch (JedisConnectionException var178) {
                        JedisPoolSubTemplate.this.logger.warning("连接异常，5秒后重试");
                        if (!this.listening.get()) {
                            continue;
                        }

                        try {
                            Thread.sleep(5000L);
                            continue;
                        } catch (InterruptedException var177) {
                            if (this.listening.get()) {
                                continue;
                            }
                        }
                    } catch (JedisException var179) {
                        if (var179.getMessage().contains("Could not get a resource")) {
                            JedisPoolSubTemplate.this.logger.warning("连接池资源已耗尽");
                            continue;
                        }

                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        var179.printStackTrace(pw);
                        JedisPoolSubTemplate.this.logger.warning("与Redis交互中发生了异常： " + sw.toString());
                        pw.close();
                        continue;
                    } catch (ClassCastException var180) {
                        JedisPoolSubTemplate.this.logger.warning("由于资源未正确释放引起了类型错误（未预期的结束符）");
                        continue;
                    } finally {
                        label1331: {
                            JedisPoolSubTemplate.this.logger.info(String.format("释放订阅连接 @%h", this.jedis.hashCode()));
                            JedisPoolSubTemplate.this.pool.unregister(this.jedis);

                            try {
                                this.jedis.sendCommand(Command.UNSUBSCRIBE);
                            } catch (JedisConnectionException var175) {
                                if (this.listening.get()) {
                                    try {
                                        Thread.sleep(5000L);
                                    } catch (InterruptedException var174) {
                                        if (!this.listening.get()) {
                                            break label1331;
                                        }
                                    }
                                }
                            } finally {
                                JedisPoolSubTemplate.this.releaseResource(this.jedis);
                            }

                        }
                    }
                }

                JedisPoolSubTemplate.this.logger.info("订阅线程" + Thread.currentThread().getName() + "结束");
                return;
            }
        }
    }
}
