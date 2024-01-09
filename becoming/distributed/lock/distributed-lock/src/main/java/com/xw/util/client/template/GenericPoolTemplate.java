package com.xw.util.client.template;

import com.xw.util.client.exception.RedisAccessException;
import com.xw.util.client.exception.RedisClientException;
import com.xw.util.client.exception.RedisConnectionException;
import com.xw.util.client.exception.RedisErrorStreamEntryIDException;
import com.xw.util.client.exception.RedisException;
import com.xw.util.client.exception.RedisLuaException;
import com.xw.util.client.exception.RedisRuntimeException;
import com.xw.util.client.inter.IGenericOperation;
import com.xw.util.client.inter.IRedisPool2;
import com.xw.util.client.model.PoolReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

public abstract class GenericPoolTemplate<R extends JedisCommands> {
    protected static final String EXEC_LUA;
    protected volatile IRedisPool2<R> pool;
    private final PoolReference poolRef;
    private boolean released;
    private AtomicBoolean retring;

    public GenericPoolTemplate(IRedisPool2<R> pool) {
        this.poolRef = PoolReference.INSTANCE;
        this.released = false;
        this.retring = new AtomicBoolean(false);
        this.pool = pool;
        this.poolRef.add(System.identityHashCode(pool));
    }

    public <T> T doRedis(IGenericOperation<T, R> operation) throws RedisException {
        if (this.released) {
            throw new RedisClientException("模板已废弃，不可进行操作\n");
        } else {
            R jedis = null;
            T ret = null;

            try {
                label589: {
                    jedis = this.pool.getResource();
                    Jedis old;
                    if (jedis != null && operation != null) {
                        if (!(jedis instanceof Jedis)) {
                            break label589;
                        }

                        old = (Jedis)jedis;

                        while(true) {
                            if (!this.pool.subscribing(jedis)) {
                                break label589;
                            }

                            try {
                                old.quit();
                            } finally {
                                this.pool.unregister(jedis);
                                old.close();
                            }

                            jedis = this.pool.getResource();
                        }
                    }

                    old = null;
                    return (T) old;
                }

                ret = operation.exec(jedis);
            } catch (JedisConnectionException var20) {
                if ("Unexpected end of stream.".equals(var20.getMessage())) {
                    throw new RedisException("客户端缓冲区异常，请检查redis.conf中“client-output-buffer-limit normal”的配置是否足够\n", var20);
                }

                if (!"Could not get a resource from the pool".equals(var20.getMessage())) {
                    throw new RedisConnectionException("与Redis连接失败\n", var20);
                }

                Throwable innerEx = var20.getCause();
                if (innerEx != null) {
                    if (InterruptedException.class.isAssignableFrom(innerEx.getClass())) {
                        throw new RedisConnectionException("连接池已关闭，请检查业务逻辑，是否在等待连接池分配期间结束了线程。\n也可能是连接池最大线程数不足，请调整该值\n", innerEx);
                    }

                    String msg1 = innerEx.getMessage() == null ? "" : innerEx.getMessage();
                    String msg2 = innerEx.getCause() == null ? "" : (innerEx.getCause().getCause() == null ? innerEx.getCause().getMessage() : (innerEx.getCause().getCause().getMessage() == null ? "" : innerEx.getCause().getCause().getMessage()));
                    if (msg1.contains("Connection refused") || msg2.contains("Connection refused")) {
                        throw new RedisConnectionException(String.format("节点 %s 连接失败，可能是节点失效，等待故障转移\n" + (this.pool.getClass().getName().contains("Sentinel") ? "也可能是哨兵配置的IP不正确\n" : ""), this.pool.getConnectionInfo()), innerEx);
                    }

                    if (msg1.contains("connect timed out") || msg2.contains("connect timed out")) {
                        throw new RedisConnectionException(String.format("创建 %s 连接超时，请检查连通性", this.pool.getConnectionInfo()), innerEx);
                    }
                }

                throw new RedisConnectionException("从连接池中取资源时发生了未知的错误\n", innerEx);
            } catch (JedisDataException var21) {
                if (var21.getMessage().startsWith("NOREPLICAS Not enough")) {
                    throw new RedisRuntimeException("主节点与所有从节点失去了联系，请检查网络连接\n", var21);
                }

                if (var21.getMessage().startsWith("READONLY")) {
                    RedisAccessException ex = new RedisAccessException(var21);
                    ex.setConnectionInfo(this.pool.getConnectionInfo());
                    throw ex;
                }

                if (var21.getMessage().startsWith("BUSY Redis is busy")) {
                    throw new RedisLuaException("服务器端执行Lua脚本超时\n", var21);
                }

                if (var21.getMessage().startsWith("LOADING")) {
                    throw new RedisException("当前Redis正在加载持久化文件\n", var21);
                }

                if (var21.getMessage().startsWith("OOM")) {
                    throw new RedisRuntimeException("Redis已经超过了最大内存限制\n", var21);
                }

                if (var21.getMessage().startsWith("ERR max number of clients")) {
                    throw new RedisException("当前节点已经超过了最大连接数，建议进行故障转移\n", var21);
                }

                if (var21.getMessage().startsWith("WRONGTYPE")) {
                    throw new RedisRuntimeException("操作的数据类型错误\n", var21);
                }

                if (var21.getMessage().startsWith("NOAUTH")) {
                    throw new RedisRuntimeException("密码验证未通过\n", var21);
                }

                if (var21.getMessage().contains("Client sent AUTH, but no password is set")) {
                    throw new RedisRuntimeException(String.format("向未配置密码的Redis节点 %s 发送了密码验证命令，检查配置文件的匹配性", this.pool.getConnectionInfo()), var21);
                }

                if (!var21.getMessage().startsWith("ERR only (P)SUBSCRIBE")) {
                    if (var21.getMessage().contains("The ID specified in XADD is equal or smaller than")) {
                        throw new RedisErrorStreamEntryIDException();
                    }

                    throw new RedisException("发生了数据错误\n", var21);
                }

                if (this.retring.get()) {
                    this.retring.set(false);
                    throw new RedisRuntimeException(String.format("客户端[%h]因使用订阅连接引发异常\n", jedis == null ? "null" : jedis.hashCode()), var21);
                }

                this.retring.set(true);
                this.releaseResource(jedis);
                ret = this.doRedis(operation);
                this.retring.set(false);
            } catch (JedisException var22) {
                if (var22.getMessage().contains("from the pool")) {
                    if (var22.getCause().getMessage().startsWith("Pool exhausted")) {
                        throw new RedisClientException("连接池配置不足\n", var22);
                    }

                    if (var22.getCause().getMessage().startsWith("Timeout waiting for idle object")) {
                        throw new RedisRuntimeException("连接池发生了排队超时，请检查慢查询日志\n", var22);
                    }

                    if (var22.getCause() instanceof NoSuchElementException) {
                        throw new RedisRuntimeException(String.format("迭代器异常，如果使用了多分片部署，请检查各分片主节点配置、密码是否一致。相关连接池信息： %s", this.pool.getConnectionInfo()), var22);
                    }
                }

                throw new RedisRuntimeException("发生了未预测的错误\n", var22);
            } finally {
                this.releaseResource(jedis);
            }

            return ret;
        }
    }

    protected void releaseResource(JedisCommands jedisCommonds) {
        if (jedisCommonds != null) {
            try {
                Method closeMethod = jedisCommonds.getClass().getMethod("close");
                if (closeMethod != null) {
                    closeMethod.invoke(jedisCommonds);
                }
            } catch (NoSuchMethodException var5) {
            } catch (InvocationTargetException var6) {
                Throwable t = var6.getTargetException();
                t.printStackTrace();
                if (JedisException.class.isAssignableFrom(t.getClass())) {
                    throw new RedisRuntimeException("在关闭Redis连接时发生异常，可能是Redis已停止。如果此异常发生在应用关闭时，请忽略");
                }
            } catch (IllegalAccessException var7) {
                var7.printStackTrace();
            }
        }

    }

    public void release() {
        if (!this.released) {
            this.releasePool();
        }

        this.released = true;
    }

    private void releasePool() {
        this.releasePool(this.pool);
    }

    private void releasePool(IRedisPool2<R> pool) {
        int count = this.poolRef.remove(System.identityHashCode(pool));
        if (count <= 0) {
            pool.getPool().destroy();
        }

    }

    public void updatePool(IRedisPool2<R> pool) {
        IRedisPool2<R> oldPool = this.pool;
        this.pool = pool;
        this.releasePool(oldPool);
    }

    public String getName() {
        return this.pool.getConnectionInfo();
    }

    static {
        StringBuilder luaBuf = new StringBuilder();
        luaBuf.append("local func \n").append("local ret=nil \n").append("local funcSha=redis.call('GET',ARGV[1]) \n").append("if funcSha then \n").append("local funcName='f_'..funcSha \n").append("func=_G[funcName] \n").append("if func then \n").append("if #ARGV>=2 then\n").append("for i=2,#ARGV do\n").append("ARGV[i-1]=ARGV[i]\n").append("end\n").append("ARGV[#ARGV]=nil\n").append("end\n").append("ret=func() \n").append("end \n").append("end \n").append("return ret");
        EXEC_LUA = luaBuf.toString();
    }
}
