package com.xw.util.client.pool;

import java.util.concurrent.atomic.AtomicReference;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

class RedisPooledObjectFactory implements PooledObjectFactory<Jedis> {
    private final AtomicReference<HostAndPort> hostAndPort = new AtomicReference();
    private final int connectionTimeout;
    private final int soTimeout;
    private final String password;
    private final String clientName;

    public RedisPooledObjectFactory(String host, int port, int connectionTimeout, int soTimeout, String password, String clientName) {
        this.hostAndPort.set(new HostAndPort(host, port));
        this.connectionTimeout = connectionTimeout;
        this.soTimeout = soTimeout;
        this.password = password;
        this.clientName = clientName;
    }

    public void setHostAndPort(HostAndPort hostAndPort) {
        this.hostAndPort.set(hostAndPort);
    }

    public PooledObject<Jedis> makeObject() throws Exception {
        HostAndPort hostAndPort = (HostAndPort)this.hostAndPort.get();
        Jedis jedis = new Jedis(hostAndPort.getHost(), hostAndPort.getPort(), this.connectionTimeout, this.soTimeout, false);

        try {
            jedis.connect();
            if (null != this.password) {
                jedis.auth(this.password);
            }

            if (this.clientName != null) {
                jedis.clientSetname(this.clientName);
            }
        } catch (JedisException var4) {
            jedis.close();
            throw var4;
        }

        return new DefaultPooledObject(jedis);
    }

    public void destroyObject(PooledObject<Jedis> p) throws Exception {
        BinaryJedis jedis = (BinaryJedis)p.getObject();
        if (jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                } catch (Exception var4) {
                }

                jedis.disconnect();
            } catch (Exception var5) {
            }
        }

    }

    public boolean validateObject(PooledObject<Jedis> p) {
        BinaryJedis jedis = (BinaryJedis)p.getObject();

        try {
            HostAndPort hostAndPort = (HostAndPort)this.hostAndPort.get();
            String connectionHost = jedis.getClient().getHost();
            int connectionPort = jedis.getClient().getPort();
            return hostAndPort.getHost().equals(connectionHost) && hostAndPort.getPort() == connectionPort && jedis.isConnected() && jedis.ping().equals("PONG");
        } catch (Exception var6) {
            return false;
        }
    }

    public void activateObject(PooledObject<Jedis> p) throws Exception {
    }

    public void passivateObject(PooledObject<Jedis> p) throws Exception {
    }
}
