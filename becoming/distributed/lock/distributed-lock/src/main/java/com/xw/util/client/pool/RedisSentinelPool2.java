package com.xw.util.client.pool;

import com.xw.util.client.inter.IRedisPool2;
import com.xw.util.client.inter.ISubscribeCallback;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.params.ClientKillParams;
import redis.clients.jedis.params.ClientKillParams.Type;
import redis.clients.jedis.util.Pool;

public class RedisSentinelPool2 extends JedisPoolAbstract implements IRedisPool2<Jedis> {
    protected Logger log;
    protected Set<MasterListener> masterListeners;
    private volatile HostAndPort currentHostMaster;
    private volatile RedisPooledObjectFactory factory;
    protected GenericObjectPoolConfig poolConfig;
    protected int connectionTimeout;
    protected int soTimeout;
    protected String password;
    protected String clientName;
    private final String masterName;
    private final Object initPoolLock;
    private final Set<Integer> subscribeClients;

    public void register(Jedis j) {
        this.subscribeClients.add(System.identityHashCode(j));
    }

    public void unregister(Jedis j) {
        this.subscribeClients.remove(System.identityHashCode(j));
    }

    public boolean subscribing(Jedis j) {
        return this.subscribeClients.contains(System.identityHashCode(j));
    }

    public RedisSentinelPool2(Set<String> sentinels, String masterName, JedisPoolConfig config) {
        this(sentinels, masterName, (String)null, config);
    }

    public RedisSentinelPool2(Set<String> sentinels, String masterName, String password, JedisPoolConfig config) {
        this(masterName, sentinels, config, 2000, 2000, password, ManagementFactory.getRuntimeMXBean().getName());
    }

    public RedisSentinelPool2(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, String clientName) {
        this(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, password, clientName, (ISubscribeCallback)null);
    }

    public RedisSentinelPool2(String masterName, Set<String> sentinels, GenericObjectPoolConfig poolConfig, int connectionTimeout, int soTimeout, String password, String clientName, ISubscribeCallback sentinelCallback, String... channels) {
        this.log = Logger.getLogger(this.getClass().getName());
        this.masterListeners = new HashSet();
        this.currentHostMaster = null;
        this.factory = null;
        this.connectionTimeout = 2000;
        this.soTimeout = 2000;
        this.initPoolLock = new Object();
        this.subscribeClients = new HashSet();
        this.masterName = masterName;
        this.poolConfig = poolConfig;
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.password = password;
        this.clientName = (String)Optional.ofNullable(clientName).orElseGet(() -> {
            return ManagementFactory.getRuntimeMXBean().getName();
        });
        HostAndPort master = this.initSentinels(sentinels, masterName, sentinelCallback, channels);
        this.initPool(master);
    }

    public boolean isPool() {
        return true;
    }

    public Pool<Jedis> getPool() {
        return this;
    }

    public String getConnectionInfo() {
        return this.getCurrentHostMaster().toString();
    }

    public String getRedisUrl() {
        return this.masterName;
    }

    public HostAndPort getCurrentHostMaster() {
        return this.currentHostMaster;
    }

    private void initPool(HostAndPort master) {
        synchronized(this.initPoolLock) {
            if (!master.equals(this.currentHostMaster)) {
                try {
                    if (this.currentHostMaster != null) {
                        Jedis j = new Jedis(this.currentHostMaster);
                        if (this.password != null) {
                            j.auth(this.password);
                        }

                        j.clientKill((new ClientKillParams()).type(Type.PUBSUB));
                        j.close();
                    }
                } catch (Exception var5) {
                }

                this.currentHostMaster = master;
                if (this.factory == null) {
                    this.factory = new RedisPooledObjectFactory(master.getHost(), master.getPort(), this.connectionTimeout, this.soTimeout, this.password, this.clientName);
                    this.initPool(this.poolConfig, this.factory);
                } else {
                    this.factory.setHostAndPort(this.currentHostMaster);
                    this.internalPool.clear();
                }

                this.log.info("Created JedisPool to master at " + master);
            }

        }
    }

    private HostAndPort initSentinels(Set<String> sentinels, String masterName) {
        return this.initSentinels(sentinels, masterName, (ISubscribeCallback)null);
    }

    private HostAndPort initSentinels(Set<String> sentinels, String masterName, ISubscribeCallback sentinelCallback, String... channels) {
        HostAndPort master = null;
        boolean sentinelAvailable = false;
        this.log.info("Trying to find master from available Sentinels...");
        Iterator var7 = sentinels.iterator();

        String sentinel;
        HostAndPort hap;
        while(var7.hasNext()) {
            sentinel = (String)var7.next();
            hap = HostAndPort.parseString(sentinel);
            this.log.fine("Connecting to Sentinel " + hap);
            Jedis jedis = null;

            try {
                jedis = new Jedis(hap.getHost(), hap.getPort());
                List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);
                sentinelAvailable = true;
                if (masterAddr != null && masterAddr.size() == 2) {
                    master = this.toHostAndPort(masterAddr);
                    this.log.fine("Found Redis master at " + master);
                    break;
                }

                this.log.warning("Can not get master addr, master name: " + masterName + ". Sentinel: " + hap + ".");
            } catch (JedisException var15) {
                this.log.warning("Cannot get master address from sentinel running @ " + hap + ". Reason: " + var15 + ". Trying next one.");
            } finally {
                if (jedis != null) {
                    jedis.close();
                }

            }
        }

        if (master == null) {
            if (sentinelAvailable) {
                throw new JedisException("Can connect to sentinel, but " + masterName + " seems to be not monitored...");
            } else {
                throw new JedisConnectionException("All sentinels down, cannot determine where is " + masterName + " master is running...");
            }
        } else {
            this.log.info("Redis master running at " + master + ", starting Sentinel listeners...");
            var7 = sentinels.iterator();

            while(var7.hasNext()) {
                sentinel = (String)var7.next();
                hap = HostAndPort.parseString(sentinel);
                MasterListener masterListener = new MasterListener(masterName, hap.getHost(), hap.getPort(), -1L, sentinelCallback, channels);
                masterListener.setDaemon(true);
                this.masterListeners.add(masterListener);
                masterListener.start();
            }

            return master;
        }
    }

    public Jedis getResource() {
        while(true) {
            Jedis jedis = (Jedis)super.getResource();
            jedis.setDataSource(this);
            HostAndPort master = this.currentHostMaster;
            HostAndPort connection = new HostAndPort(jedis.getClient().getHost(), jedis.getClient().getPort());
            if (master.equals(connection)) {
                return jedis;
            }

            this.returnBrokenResourceObject(jedis);
        }
    }

    public void destroy() {
        Iterator var1 = this.masterListeners.iterator();

        while(var1.hasNext()) {
            MasterListener m = (MasterListener)var1.next();
            m.shutdown();
        }

        super.destroy();
    }

    private HostAndPort toHostAndPort(List<String> getMasterAddrByNameResult) {
        String host = (String)getMasterAddrByNameResult.get(0);
        int port = Integer.parseInt((String)getMasterAddrByNameResult.get(1));
        return new HostAndPort(host, port);
    }

    protected class MasterListener extends Thread {
        protected String masterName;
        protected String host;
        protected int port;
        protected long subscribeRetryWaitTimeMillis;
        protected volatile Jedis j;
        protected AtomicBoolean running;
        protected ISubscribeCallback sentinelCallback;
        protected String[] channels;

        protected MasterListener() {
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.running = new AtomicBoolean(false);
            this.channels = null;
        }

        public MasterListener(String masterName, String host, int port) {
            this(masterName, host, port, -1L);
        }

        public MasterListener(String masterName, String host, int port, long subscribeRetryWaitTimeMillis) {
            this(masterName, host, port, subscribeRetryWaitTimeMillis, (ISubscribeCallback)null);
        }

        public MasterListener(String masterName, String host, int port, long subscribeRetryWaitTimeMillis, final ISubscribeCallback sentinelCallback, String... channels) {
            super(String.format("MasterListener-%s-[%s:%d]", masterName, host, port));
            this.subscribeRetryWaitTimeMillis = 5000L;
            this.running = new AtomicBoolean(false);
            this.channels = null;
            this.masterName = masterName;
            this.host = host;
            this.port = port;
            if (subscribeRetryWaitTimeMillis > 0L) {
                this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
            }

            final String fMasterName = this.masterName;
            final String fHost = this.host;
            final int fPort = this.port;
            this.sentinelCallback = new ISubscribeCallback() {
                public void onMessage(String channel, String message) {
                    RedisSentinelPool2.this.log.fine("Sentinel " + fHost + ":" + fPort + " published: " + message + ".");
                    if ("+switch-master".equals(channel)) {
                        String[] switchMasterMsg = message.split(" ");
                        if (switchMasterMsg.length > 3) {
                            if (fMasterName.equals(switchMasterMsg[0])) {
                                RedisSentinelPool2.this.initPool(RedisSentinelPool2.this.toHostAndPort(Arrays.asList(switchMasterMsg[3], switchMasterMsg[4])));
                            } else {
                                RedisSentinelPool2.this.log.fine("Ignoring message on +switch-master for master name " + switchMasterMsg[0] + ", our master name is " + fMasterName);
                            }
                        } else {
                            RedisSentinelPool2.this.log.severe("Invalid message received on Sentinel " + fHost + ":" + fPort + " on channel +switch-master: " + message);
                        }
                    }

                    if (sentinelCallback != null) {
                        sentinelCallback.onMessage(channel, message);
                    }

                }
            };
            if (channels != null && channels.length != 0 && channels[0] != null && channels[0].length() != 0) {
                String[] var12 = channels;
                int var13 = channels.length;

                for(int var14 = 0; var14 < var13; ++var14) {
                    String channel = var12[var14];
                    if ("+switch-master".equals(channel)) {
                        this.channels = channels;
                        break;
                    }
                }

                if (this.channels == null) {
                    this.channels = new String[channels.length + 1];
                    this.channels[0] = "+switch-master";
                    System.arraycopy(channels, 0, this.channels, 1, channels.length);
                }
            } else {
                this.channels = new String[]{"+switch-master"};
            }

        }

        public void run() {
            this.running.set(true);

            while(this.running.get()) {
                this.j = new Jedis(this.host, this.port);

                try {
                    if (!this.running.get()) {
                        break;
                    }

                    this.j.subscribe(new JedisPubSub() {
                        public void onMessage(String channel, String message) {
                            MasterListener.this.sentinelCallback.onMessage(channel, message);
                        }
                    }, this.channels);
                } catch (JedisConnectionException var8) {
                    if (this.running.get()) {
                        RedisSentinelPool2.this.log.log(Level.SEVERE, "Lost connection to Sentinel at " + this.host + ":" + this.port + ". Sleeping 5000ms and retrying.", var8);
                        this.sentinelCallback.onMessage("+lost-connection", this.host + ":" + this.port);

                        try {
                            Thread.sleep(this.subscribeRetryWaitTimeMillis);
                        } catch (InterruptedException var7) {
                            RedisSentinelPool2.this.log.log(Level.SEVERE, "Sleep interrupted: ", var7);
                        }
                    } else {
                        RedisSentinelPool2.this.log.fine("Unsubscribing from Sentinel at " + this.host + ":" + this.port);
                    }
                } finally {
                    this.j.close();
                }
            }

        }

        public void shutdown() {
            try {
                RedisSentinelPool2.this.log.fine("Shutting down listener on " + this.host + ":" + this.port);
                this.running.set(false);
                if (this.j != null) {
                    this.j.disconnect();
                }
            } catch (Exception var5) {
                RedisSentinelPool2.this.log.log(Level.SEVERE, "Caught exception while shutting down: ", var5);
            } finally {
                this.sentinelCallback = null;
                this.channels = null;
            }

        }
    }
}
