//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.xw.util.client.template;

import com.xw.util.client.exception.RedisRuntimeException;
import com.xw.util.client.pool.RedisSinglePool;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.jedis.util.ShardInfo;

public abstract class ShardedComponentInfo<T extends GenericPoolTemplate<Jedis>> extends ShardInfo<T> {
    private Set<String> sentinels;
    private String masterName;
    private JedisPoolConfig poolConfig = null;
    private volatile String currentMasterHost = null;
    private int currentMasterPort = -1;
    private String password = null;

    public ShardedComponentInfo(Set<String> sentinels, String masterName) {
        super(1);
        this.sentinels = sentinels;
        this.masterName = masterName;
    }

    public ShardedComponentInfo(Set<String> sentinels, String masterName, String password) {
        super(1);
        this.sentinels = sentinels;
        this.masterName = masterName;
        this.password = password;
    }

    public ShardedComponentInfo(Set<String> sentinels, String masterName, String password, JedisPoolConfig poolConfig) {
        super(1);
        this.sentinels = sentinels;
        this.masterName = masterName;
        this.password = password;
        this.poolConfig = poolConfig;
    }

    public void failOver(String masterHost, int masterPort) {
        this.currentMasterHost = masterHost;
        this.currentMasterPort = masterPort;
    }

    public ShardedComponentInfo<T> poolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
        return this;
    }

    public abstract T createResource();

    protected RedisSinglePool getPool() {
        if (this.poolConfig == null) {
            this.poolConfig = new JedisPoolConfig();
        }

        if (this.currentMasterHost != null && this.currentMasterPort > 0) {
            new HostAndPort(this.currentMasterHost, this.currentMasterPort);
        } else {
            HostAndPort master = this.getMasterInfo();
            this.currentMasterHost = master.getHost();
            this.currentMasterPort = master.getPort();
        }

        RedisSinglePool pool;
        if (this.password == null) {
            pool = new RedisSinglePool(this.currentMasterHost, this.currentMasterPort, this.poolConfig);
        } else {
            pool = new RedisSinglePool(this.currentMasterHost, this.currentMasterPort, this.password, this.poolConfig);
        }

        return pool;
    }

    private HostAndPort getMasterInfo() {
        boolean sentinelAvailable = false;
        String host = null;
        int port = -1;
        Iterator var4 = this.sentinels.iterator();

        while(var4.hasNext()) {
            String sentinel = (String)var4.next();
            HostAndPort sentinelAddr = HostAndPort.parseString(sentinel);
            Jedis jedis = null;

            try {
                jedis = new Jedis(sentinelAddr.getHost(), sentinelAddr.getPort());
                List<String> masterAddr = jedis.sentinelGetMasterAddrByName(this.masterName);
                sentinelAvailable = true;
                if (masterAddr != null && masterAddr.size() == 2) {
                    host = (String)masterAddr.get(0);
                    port = Integer.parseInt((String)masterAddr.get(1));
                    break;
                }
            } catch (JedisException var12) {
                throw new RedisRuntimeException("从哨兵取主节点地址出错", var12);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }

            }
        }

        if (host != null && port != -1) {
            return new HostAndPort(host, port);
        } else if (sentinelAvailable) {
            throw new RedisRuntimeException("哨兵连接成功，但" + this.masterName + "不在其监控下");
        } else {
            throw new RedisRuntimeException("所有哨兵已下线");
        }
    }

    public void release() {
        if (this.sentinels != null) {
            this.sentinels.clear();
        }

        this.poolConfig = null;
    }

    public String toString() {
        return this.masterName;
    }

    public int hashCode() {
        return this.masterName.hashCode();
    }

    public String getName() {
        return String.format("master: %s ->M: %s:%d", this.masterName, this.currentMasterHost, this.currentMasterPort);
    }

    public String getPwd() {
        return this.password;
    }

    public Set<String> getSentinels() {
        return this.sentinels;
    }
}
