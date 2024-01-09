package com.xw.util.client.template;

import com.xw.util.client.exception.RedisException;
import com.xw.util.client.pool.RedisSinglePool;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.util.Hashing;
import redis.clients.jedis.util.Sharded;

public abstract class RedisShardedComponent<T extends GenericPoolTemplate<Jedis>> extends Sharded<T, ShardedComponentInfo<T>> {
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private boolean released = false;
    private JedisPoolConfig poolConfig = null;
    private Set<RedisShardedComponent<T>.ShardedMasterListener> listeners = new HashSet();

    protected RedisShardedComponent(Set<String> sentinels, Set<String> masterNames, List<ShardedComponentInfo<T>> shards, Hashing algo, JedisPoolConfig poolConfig) {
        super(shards, algo);
        this.initListeners(sentinels, masterNames);
        this.poolConfig = poolConfig;
    }

    private void initListeners(Set<String> sentinels, Set<String> masterNames) {
        Iterator var3 = sentinels.iterator();

        while(var3.hasNext()) {
            String sentinel = (String)var3.next();
            HostAndPort hap = HostAndPort.parseString(sentinel);
            RedisShardedComponent<T>.ShardedMasterListener listener = new ShardedMasterListener(masterNames, hap.getHost(), hap.getPort());
            this.listeners.add(listener);
            Thread listenThread = new Thread(listener);
            listenThread.setDaemon(true);
            listenThread.start();
        }

    }

    public void release() {
        if (!this.released) {
            Iterator var1 = this.getAllShardInfo().iterator();

            while(var1.hasNext()) {
                ShardedComponentInfo<T> shardedTemplateInfo = (ShardedComponentInfo)var1.next();
                shardedTemplateInfo.release();
            }

            var1 = this.getAllShards().iterator();

            while(var1.hasNext()) {
//                T template = (GenericPoolTemplate)var1.next();
                T template = (T) var1.next();
                template.release();
            }

            var1 = this.listeners.iterator();

            while(var1.hasNext()) {
                RedisShardedComponent<T>.ShardedMasterListener listener = (ShardedMasterListener)var1.next();
                listener.shutdown();
            }

            this.listeners.clear();
            this.released = true;
        }
    }

    private synchronized void fallOver(String[] message) {
        String masterName = message[0];
        String oldHost = message[1];
        int oldPort = Integer.parseInt(message[2]);
        String newHost = message[3];
        int newPort = Integer.parseInt(message[4]);
        String oldMaster = (new HostAndPort(oldHost, oldPort)).toString();
        String newMaster = (new HostAndPort(newHost, newPort)).toString();
        this.logger.fine(String.format("%s 进行故障转移 %s->%s", masterName, oldMaster, newMaster));
        String password = null;
        Iterator var11 = this.getAllShardInfo().iterator();

        while(var11.hasNext()) {
            ShardedComponentInfo<T> shardInfo = (ShardedComponentInfo)var11.next();
            if (shardInfo.toString().equals(masterName)) {
                shardInfo.failOver(newHost, newPort);
                password = shardInfo.getPwd();
                break;
            }
        }

        var11 = this.getAllShards().iterator();

        while(var11.hasNext()) {
//            T template = (GenericPoolTemplate)var11.next();
            T template = (T) var11.next();
            if (template.getName().equals(oldMaster)) {
                try {
                    this.dealOldMaster(template);
                } catch (Exception var15) {
                }

                RedisSinglePool pool = new RedisSinglePool(newHost, newPort, password, this.poolConfig);
                template.updatePool(pool);

                try {
                    this.dealNewMaster(template);
                } catch (RedisException var14) {
                    this.logger.log(Level.WARNING, String.format("[M]%s 新主节点 %s 执行命令发生异常", masterName, newMaster), var14);
                }

                this.logger.info(String.format("[M]%s 重置模板连接池，主节点迁移： %s->%s", masterName, oldMaster, newMaster));
                break;
            }
        }

        this.logger.fine("故障转移结束");
    }

    protected void dealOldMaster(T template) throws RedisException {
    }

    protected void dealNewMaster(T template) throws RedisException {
    }

    private class ShardedMasterListener implements Runnable {
        private Set<String> masterNames;
        private AtomicBoolean running;
        private String sentinelHost;
        private int sentinelPort;
        private long subscribeRetryWaitTimeMillis;
        private volatile Jedis jedis;

        ShardedMasterListener(Set<String> masterNames, String sentinelHost, int sentinelPort) {
            this.running = new AtomicBoolean(false);
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.masterNames = masterNames;
            this.sentinelHost = sentinelHost;
            this.sentinelPort = sentinelPort;
        }

        public ShardedMasterListener(Set<String> masterNames, String sentinelHost, int sentinelPort, long subscribeRetryWaitTimeMillis) {
            this(masterNames, sentinelHost, sentinelPort);
            this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
        }

        public void shutdown() {
            this.running.set(false);

            try {
                if (this.jedis != null) {
                    try {
                        this.jedis.quit();
                    } finally {
                        this.jedis.disconnect();
                    }
                }
            } catch (Exception var5) {
                RedisShardedComponent.this.logger.warning("[X]断开连接时发生了异常：" + var5.getMessage());
            }

            this.masterNames.clear();
        }

        public void run() {
            this.running.set(true);

            while(this.running.get()) {
                this.jedis = new Jedis(this.sentinelHost, this.sentinelPort);

                try {
                    this.jedis.subscribe(new JedisPubSub() {
                        public void onMessage(String channel, String message) {
                            String[] switchMasterMsg = message.split(" ");
                            RedisShardedComponent.this.logger.info(String.format("[X]%s:%d 监测到故障转移事件", ShardedMasterListener.this.sentinelHost, ShardedMasterListener.this.sentinelPort));
                            if (ShardedMasterListener.this.masterNames.contains(switchMasterMsg[0])) {
                                RedisShardedComponent.this.fallOver(switchMasterMsg);
                            } else {
                                RedisShardedComponent.this.logger.info(String.format("[X]%s 与本群组无关，忽略", switchMasterMsg[0]));
                            }

                        }
                    }, new String[]{"+switch-master"});
                } catch (JedisConnectionException var8) {
                    if (this.running.get()) {
                        try {
                            Thread.sleep(this.subscribeRetryWaitTimeMillis);
                        } catch (InterruptedException var7) {
                            if (!this.running.get()) {
                                break;
                            }
                        }
                    }
                } finally {
                    this.jedis.close();
                }
            }

            this.masterNames.clear();
        }
    }
}
