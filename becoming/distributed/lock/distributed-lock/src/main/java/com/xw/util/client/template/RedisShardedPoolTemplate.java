package com.xw.util.client.template;

import com.xw.util.client.exception.DefaultExceptionHandler;
import com.xw.util.client.exception.RedisException;
import com.xw.util.client.exception.RedisRuntimeException;
import com.xw.util.client.inter.IExceptionHandler;
import com.xw.util.client.inter.IRedisCommandTemplate;
import com.xw.util.client.inter.IRedisOperation;
import com.xw.util.client.inter.ISubscribeCallback;
import com.xw.util.client.inter.ISubscribeCallback2;
import com.xw.util.client.util.RedisUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;
import redis.clients.jedis.util.Hashing;

public class RedisShardedPoolTemplate extends RedisShardedComponent<RedisPoolTemplate> implements IRedisCommandTemplate {
    private static final ThreadLocal<String> keyTags = new ThreadLocal();
    private IExceptionHandler exceptionHandler;

    public static RedisShardedPoolTemplate createTemplate(Set<String> sentinels, Set<String> masterNames, Hashing algo, JedisPoolConfig poolConfig) {
        List<ShardedComponentInfo<RedisPoolTemplate>> shards = new ArrayList();
        Iterator var6 = masterNames.iterator();

        while(var6.hasNext()) {
            String masterName = (String)var6.next();
            ShardedTemplateInfo shardInfo = new ShardedTemplateInfo(sentinels, masterName, (String)null, poolConfig);
            shards.add(shardInfo);
        }

        return new RedisShardedPoolTemplate(sentinels, masterNames, shards, algo, poolConfig);
    }

    public static RedisShardedPoolTemplate createTemplate(Set<String> sentinels, Set<String> masterNames, String password, Hashing algo, JedisPoolConfig poolConfig) {
        List<ShardedComponentInfo<RedisPoolTemplate>> shards = new ArrayList();
        Iterator var7 = masterNames.iterator();

        while(var7.hasNext()) {
            String masterName = (String)var7.next();
            ShardedTemplateInfo shardInfo = new ShardedTemplateInfo(sentinels, masterName, password, poolConfig);
            shards.add(shardInfo);
        }

        return new RedisShardedPoolTemplate(sentinels, masterNames, shards, algo, poolConfig);
    }

    public static RedisShardedPoolTemplate createTemplate(String infoStr, Hashing algo, JedisPoolConfig poolConfig) {
        new ArrayList();
        if (infoStr == null) {
            return null;
        } else if (!infoStr.contains("@")) {
            throw new RedisRuntimeException("多分片模板不支持无哨兵模式");
        } else {
            String[] infos = infoStr.split("/");
            String password = null;
            if (infos.length > 1) {
                password = infos[1];
            }

            String info = infos[0];
            int idxAt = info.indexOf("@");
            Set<String> masterNames = new HashSet();
            masterNames.addAll(Arrays.asList(info.substring(0, idxAt).split(",")));
            Set<String> sentinels = RedisUtil.getHaps(info.substring(idxAt + 1));
            return password == null ? createTemplate(sentinels, masterNames, algo, poolConfig) : createTemplate(sentinels, masterNames, password, algo, poolConfig);
        }
    }

    private RedisShardedPoolTemplate(Set<String> sentinels, Set<String> masterNames, List<ShardedComponentInfo<RedisPoolTemplate>> shards, Hashing algo, JedisPoolConfig poolConfig) {
        super(sentinels, masterNames, shards, algo, poolConfig);
        this.exceptionHandler = DefaultExceptionHandler.INSTANCE;
    }

    public <T> T doRedis(String keyTag, IRedisOperation<T> operation) throws RedisException {
        if (keyTag == null) {
            return null;
        } else {
            RedisPoolTemplate template = (RedisPoolTemplate)this.getShard(keyTag);
            return template.doRedis(operation);
        }
    }

    private void checkKeyTagExists(String key) {
        if (keyTags.get() == null) {
            keyTags.set(key);
        }

    }

    private <T> T doRedisWithExceptionHandler(IRedisOperation<T> operation) {
        T ret = null;
        String keyTag = (String)keyTags.get();

        try {
            ret = this.doRedis(keyTag, operation);
        } catch (RedisRuntimeException var5) {
            this.exceptionHandler.dealException(new RedisException(var5.getMessage(), var5));
        } catch (RedisException var6) {
            this.exceptionHandler.dealException(var6);
        }

        return ret;
    }

    public void subscribe(ISubscribeCallback callback, String channel) {
        Iterator var3 = this.getAllShards().iterator();

        while(var3.hasNext()) {
            RedisPoolTemplate template = (RedisPoolTemplate)var3.next();
            template.subscribe(callback, channel);
        }

    }

    public void psubscribe(ISubscribeCallback callback, String channel) {
        Iterator var3 = this.getAllShards().iterator();

        while(var3.hasNext()) {
            RedisPoolTemplate template = (RedisPoolTemplate)var3.next();
            template.psubscribe(callback, channel);
        }

    }

    public void psubscribe(Class<? extends ISubscribeCallback2> callbackClazz, String channel, Object... args) {
        Class[] classes = new Class[args.length];

        for(int i = 0; i < args.length; ++i) {
            classes[i] = args[i].getClass();
        }

        try {
            Constructor<? extends ISubscribeCallback2> constructor = callbackClazz.getConstructor(classes);
            Iterator var6 = this.getAllShards().iterator();

            while(var6.hasNext()) {
                RedisPoolTemplate template = (RedisPoolTemplate)var6.next();
                template.psubscribe((ISubscribeCallback)constructor.newInstance(args), channel);
            }
        } catch (NoSuchMethodException var8) {
        } catch (IllegalAccessException var9) {
        } catch (InstantiationException var10) {
        } catch (InvocationTargetException var11) {
        }

    }

    public void resizeThreadPool(int corePoolSize, int maxPoolSize) {
        Iterator var3 = this.getAllShards().iterator();

        while(var3.hasNext()) {
            RedisPoolTemplate shard = (RedisPoolTemplate)var3.next();
            shard.resizeThreadPool(corePoolSize, maxPoolSize);
        }

    }

    public void setKeyTag(String keyTag) {
        keyTags.set(keyTag);
    }

    public void done() {
        keyTags.remove();
    }

    public void setExceptionHandler(IExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public String set(final String key, final String value) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.set(key, value);
            }
        });
    }

    public String set(final String key, final String value, final String nxxx, String expx, final long time) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                SetParams setParams = new SetParams();
                if ("nx".equalsIgnoreCase(nxxx)) {
                    setParams.nx();
                }

                if ("xx".equalsIgnoreCase(nxxx)) {
                    setParams.xx();
                }

                setParams.ex((int)time);
                return jedis.set(key, value, setParams);
            }
        });
    }

    public String set(final String key, final String value, final String nxxx) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                SetParams setParams = new SetParams();
                if ("nx".equalsIgnoreCase(nxxx)) {
                    setParams.nx();
                }

                if ("xx".equalsIgnoreCase(nxxx)) {
                    setParams.xx();
                }

                return jedis.set(key, value, setParams);
            }
        });
    }

    public String get(final String key) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public Boolean exists(final String key) {
        this.checkKeyTagExists(key);
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    public Long persist(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.persist(key);
            }
        });
    }

    public String type(final String key) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.type(key);
            }
        });
    }

    public Long expire(final String key, final int seconds) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.expire(key, seconds);
            }
        });
    }

    public Long pexpire(final String key, final long milliseconds) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pexpire(key, milliseconds);
            }
        });
    }

    public Long expireAt(final String key, final long unixTime) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.expireAt(key, unixTime);
            }
        });
    }

    public Long pexpireAt(final String key, final long millisecondsTimestamp) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pexpireAt(key, millisecondsTimestamp);
            }
        });
    }

    public Long ttl(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.ttl(key);
            }
        });
    }

    public Long pttl(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pttl(key);
            }
        });
    }

    public Boolean setbit(final String key, final long offset, final boolean value) {
        this.checkKeyTagExists(key);
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.setbit(key, offset, value);
            }
        });
    }

    public Boolean setbit(final String key, final long offset, final String value) {
        this.checkKeyTagExists(key);
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.setbit(key, offset, value);
            }
        });
    }

    public Boolean getbit(final String key, final long offset) {
        this.checkKeyTagExists(key);
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.getbit(key, offset);
            }
        });
    }

    public Long setrange(final String key, final long offset, final String value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.setrange(key, offset, value);
            }
        });
    }

    public String getrange(final String key, final long startOffset, final long endOffset) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.getrange(key, startOffset, endOffset);
            }
        });
    }

    public String getSet(final String key, final String value) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.getSet(key, value);
            }
        });
    }

    public Long setnx(final String key, final String value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.setnx(key, value);
            }
        });
    }

    public String setex(final String key, final int seconds, final String value) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.setex(key, seconds, value);
            }
        });
    }

    public String psetex(final String key, final long milliseconds, final String value) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.psetex(key, milliseconds, value);
            }
        });
    }

    public Long decrBy(final String key, final long integer) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.decrBy(key, integer);
            }
        });
    }

    public Long decr(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    public Long incrBy(final String key, final long integer) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.incrBy(key, integer);
            }
        });
    }

    public Double incrByFloat(final String key, final double value) {
        this.checkKeyTagExists(key);
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.incrByFloat(key, value);
            }
        });
    }

    public Long incr(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public Long append(final String key, final String value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.append(key, value);
            }
        });
    }

    public String substr(final String key, final int start, final int end) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.substr(key, start, end);
            }
        });
    }

    public Long hset(final String key, final String field, final String value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hset(key, field, value);
            }
        });
    }

    public String hget(final String key, final String field) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.hget(key, field);
            }
        });
    }

    public Long hsetnx(final String key, final String field, final String value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hsetnx(key, field, value);
            }
        });
    }

    public String hmset(final String key, final Map<String, String> hash) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.hmset(key, hash);
            }
        });
    }

    public List<String> hmget(final String key, final String... fields) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.hmget(key, fields);
            }
        });
    }

    public Long hincrBy(final String key, final String field, final long value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hincrBy(key, field, value);
            }
        });
    }

    public Double hincrByFloat(final String key, final String field, final double value) {
        this.checkKeyTagExists(key);
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.hincrByFloat(key, field, value);
            }
        });
    }

    public Boolean hexists(final String key, final String field) {
        this.checkKeyTagExists(key);
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.hexists(key, field);
            }
        });
    }

    public Long hdel(final String key, final String... field) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hdel(key, field);
            }
        });
    }

    public Long hlen(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hlen(key);
            }
        });
    }

    public Set<String> hkeys(final String key) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.hkeys(key);
            }
        });
    }

    public List<String> hvals(final String key) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.hvals(key);
            }
        });
    }

    public Map<String, String> hgetAll(final String key) {
        this.checkKeyTagExists(key);
        return (Map)this.doRedisWithExceptionHandler(new IRedisOperation<Map<String, String>>() {
            public Map<String, String> exec(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    public Long rpush(final String key, final String... string) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.rpush(key, string);
            }
        });
    }

    public Long lpush(final String key, final String... string) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.lpush(key, string);
            }
        });
    }

    public Long llen(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.llen(key);
            }
        });
    }

    public List<String> lrange(final String key, final long start, final long end) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.lrange(key, start, end);
            }
        });
    }

    public String ltrim(final String key, final long start, final long end) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.ltrim(key, start, end);
            }
        });
    }

    public String lindex(final String key, final long index) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.lindex(key, index);
            }
        });
    }

    public String lset(final String key, final long index, final String value) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.lset(key, index, value);
            }
        });
    }

    public Long lrem(final String key, final long count, final String value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.lrem(key, count, value);
            }
        });
    }

    public String lpop(final String key) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.lpop(key);
            }
        });
    }

    public String rpop(final String key) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.rpop(key);
            }
        });
    }

    public Long sadd(final String key, final String... member) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.sadd(key, member);
            }
        });
    }

    public Set<String> smembers(final String key) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.smembers(key);
            }
        });
    }

    public Long srem(final String key, final String... member) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.srem(key, member);
            }
        });
    }

    public String spop(final String key) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.spop(key);
            }
        });
    }

    public Set<String> spop(final String key, final long count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.spop(key, count);
            }
        });
    }

    public Long scard(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.scard(key);
            }
        });
    }

    public Boolean sismember(final String key, final String member) {
        this.checkKeyTagExists(key);
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.sismember(key, member);
            }
        });
    }

    public String srandmember(final String key) {
        this.checkKeyTagExists(key);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.srandmember(key);
            }
        });
    }

    public List<String> srandmember(final String key, final int count) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.srandmember(key, count);
            }
        });
    }

    public Long strlen(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.strlen(key);
            }
        });
    }

    public Long zadd(final String key, final double score, final String member) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zadd(key, score, member);
            }
        });
    }

    public Long zadd(final String key, final double score, final String member, final ZAddParams params) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zadd(key, score, member, params);
            }
        });
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zadd(key, scoreMembers);
            }
        });
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers, final ZAddParams params) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zadd(key, scoreMembers, params);
            }
        });
    }

    public Set<String> zrange(final String key, final long start, final long end) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrange(key, start, end);
            }
        });
    }

    public Long zrem(final String key, final String... member) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zrem(key, member);
            }
        });
    }

    public Double zincrby(final String key, final double score, final String member) {
        this.checkKeyTagExists(key);
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.zincrby(key, score, member);
            }
        });
    }

    public Double zincrby(final String key, final double score, final String member, final ZIncrByParams params) {
        this.checkKeyTagExists(key);
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.zincrby(key, score, member, params);
            }
        });
    }

    public Long zrank(final String key, final String member) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zrank(key, member);
            }
        });
    }

    public Long zrevrank(final String key, final String member) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zrevrank(key, member);
            }
        });
    }

    public Set<String> zrevrange(final String key, final long start, final long end) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrange(key, start, end);
            }
        });
    }

    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeWithScores(key, start, end);
            }
        });
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeWithScores(key, start, end);
            }
        });
    }

    public Long zcard(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zcard(key);
            }
        });
    }

    public Double zscore(final String key, final String member) {
        this.checkKeyTagExists(key);
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.zscore(key, member);
            }
        });
    }

    public List<String> sort(final String key) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.sort(key);
            }
        });
    }

    public List<String> sort(final String key, final SortingParams sortingParameters) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.sort(key, sortingParameters);
            }
        });
    }

    public Long zcount(final String key, final double min, final double max) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zcount(key, min, max);
            }
        });
    }

    public Long zcount(final String key, final String min, final String max) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zcount(key, min, max);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeByScoreWithScores(key, max, min);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeByScoreWithScores(key, max, min);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        });
    }

    public Long zremrangeByRank(final String key, final long start, final long end) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zremrangeByRank(key, start, end);
            }
        });
    }

    public Long zremrangeByScore(final String key, final double start, final double end) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zremrangeByScore(key, start, end);
            }
        });
    }

    public Long zremrangeByScore(final String key, final String start, final String end) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zremrangeByScore(key, start, end);
            }
        });
    }

    public Long zlexcount(final String key, final String min, final String max) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zlexcount(key, min, max);
            }
        });
    }

    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByLex(key, min, max);
            }
        });
    }

    public Set<String> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByLex(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByLex(final String key, final String max, final String min) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByLex(key, max, min);
            }
        });
    }

    public Set<String> zrevrangeByLex(final String key, final String max, final String min, final int offset, final int count) {
        this.checkKeyTagExists(key);
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByLex(key, max, min, offset, count);
            }
        });
    }

    public Long zremrangeByLex(final String key, final String min, final String max) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zremrangeByLex(key, min, max);
            }
        });
    }

    public Long linsert(final String key, final ListPosition where, final String pivot, final String value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.linsert(key, where, pivot, value);
            }
        });
    }

    public Long lpushx(final String key, final String... string) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.lpushx(key, string);
            }
        });
    }

    public Long rpushx(final String key, final String... string) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.rpushx(key, string);
            }
        });
    }

    public List<String> blpop(final String arg) {
        this.checkKeyTagExists(arg);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.blpop(new String[]{arg});
            }
        });
    }

    public List<String> blpop(final int timeout, final String key) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.blpop(timeout, key);
            }
        });
    }

    public List<String> brpop(final String arg) {
        this.checkKeyTagExists(arg);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.brpop(new String[]{arg});
            }
        });
    }

    public List<String> brpop(final int timeout, final String key) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.brpop(timeout, key);
            }
        });
    }

    public Long del(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.del(key);
            }
        });
    }

    public String echo(final String string) {
        this.checkKeyTagExists(string);
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.echo(string);
            }
        });
    }

    public Long move(final String key, final int dbIndex) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.move(key, dbIndex);
            }
        });
    }

    public Long bitcount(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.bitcount(key);
            }
        });
    }

    public Long bitcount(final String key, final long start, final long end) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.bitcount(key, start, end);
            }
        });
    }

    public Long bitpos(final String key, final boolean value) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.bitpos(key, value);
            }
        });
    }

    public Long bitpos(final String key, final boolean value, final BitPosParams params) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.bitpos(key, value, params);
            }
        });
    }

    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor) {
        return this.hscan(key, String.valueOf(cursor));
    }

    public ScanResult<String> sscan(String key, int cursor) {
        return this.sscan(key, String.valueOf(cursor));
    }

    public ScanResult<Tuple> zscan(String key, int cursor) {
        return this.zscan(key, String.valueOf(cursor));
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        this.checkKeyTagExists(key);
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisOperation<ScanResult<Map.Entry<String, String>>>() {
            public ScanResult<Map.Entry<String, String>> exec(Jedis jedis) {
                return jedis.hscan(key, cursor);
            }
        });
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor, final ScanParams params) {
        this.checkKeyTagExists(key);
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisOperation<ScanResult<Map.Entry<String, String>>>() {
            public ScanResult<Map.Entry<String, String>> exec(Jedis jedis) {
                return jedis.hscan(key, cursor, params);
            }
        });
    }

    public ScanResult<String> sscan(String key, String cursor) {
        return this.sscan(key, cursor, new ScanParams());
    }

    public ScanResult<String> sscan(final String key, final String cursor, final ScanParams params) {
        this.checkKeyTagExists(key);
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisOperation<ScanResult<String>>() {
            public ScanResult<String> exec(Jedis jedis) {
                return jedis.sscan(key, cursor, params);
            }
        });
    }

    public ScanResult<Tuple> zscan(String key, String cursor) {
        return this.zscan(key, cursor, new ScanParams());
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor, final ScanParams params) {
        this.checkKeyTagExists(key);
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisOperation<ScanResult<Tuple>>() {
            public ScanResult<Tuple> exec(Jedis jedis) {
                return jedis.zscan(key, cursor, params);
            }
        });
    }

    public Long pfadd(final String key, final String... elements) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pfadd(key, elements);
            }
        });
    }

    public long pfcount(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pfcount(key);
            }
        });
    }

    public Long geoadd(final String key, final double longitude, final double latitude, final String member) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.geoadd(key, longitude, latitude, member);
            }
        });
    }

    public Long geoadd(final String key, final Map<String, GeoCoordinate> memberCoordinateMap) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.geoadd(key, memberCoordinateMap);
            }
        });
    }

    public Double geodist(final String key, final String member1, final String member2) {
        this.checkKeyTagExists(key);
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.geodist(key, member1, member2);
            }
        });
    }

    public Double geodist(final String key, final String member1, final String member2, final GeoUnit unit) {
        this.checkKeyTagExists(key);
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.geodist(key, member1, member2, unit);
            }
        });
    }

    public List<String> geohash(final String key, final String... members) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.geohash(key, members);
            }
        });
    }

    public List<GeoCoordinate> geopos(final String key, final String... members) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoCoordinate>>() {
            public List<GeoCoordinate> exec(Jedis jedis) {
                return jedis.geopos(key, members);
            }
        });
    }

    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(Jedis jedis) {
                return jedis.georadius(key, longitude, latitude, radius, unit);
            }
        });
    }

    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(Jedis jedis) {
                return jedis.georadius(key, longitude, latitude, radius, unit, param);
            }
        });
    }

    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(Jedis jedis) {
                return jedis.georadiusByMember(key, member, radius, unit);
            }
        });
    }

    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(Jedis jedis) {
                return jedis.georadiusByMember(key, member, radius, unit, param);
            }
        });
    }

    public List<Long> bitfield(final String key, final String... arguments) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<Long>>() {
            public List<Long> exec(Jedis jedis) {
                return jedis.bitfield(key, arguments);
            }
        });
    }

    public long unlink(String... keys) {
        long s = 0L;

        RedisPoolTemplate shard;
        for(Iterator var4 = this.getAllShards().iterator(); var4.hasNext(); s += shard.unlink(keys)) {
            shard = (RedisPoolTemplate)var4.next();
        }

        return s;
    }

    public StreamEntryID xadd(final String key, final StreamEntryID entryId, final Map<String, String> pairs) {
        this.checkKeyTagExists(key);
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisOperation<StreamEntryID>() {
            public StreamEntryID exec(Jedis jedis) {
                return jedis.xadd(key, entryId, pairs);
            }
        });
    }

    public StreamEntryID xadd(final String key, final StreamEntryID entryId, final String... pairs) {
        this.checkKeyTagExists(key);
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisOperation<StreamEntryID>() {
            public StreamEntryID exec(Jedis jedis) {
                return jedis.xadd(key, entryId, RedisUtil.convertArrayToMap(pairs, true));
            }
        });
    }

    public StreamEntryID xaddWithMaxlen(final String key, final boolean approx, final long maxLen, final StreamEntryID entryId, final Map<String, String> pairs) {
        this.checkKeyTagExists(key);
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisOperation<StreamEntryID>() {
            public StreamEntryID exec(Jedis jedis) {
                return jedis.xadd(key, entryId, pairs, maxLen, approx);
            }
        });
    }

    public StreamEntryID xaddWithMaxlen(final String key, final boolean approx, final long maxLen, final StreamEntryID entryId, final String... pairs) {
        this.checkKeyTagExists(key);
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisOperation<StreamEntryID>() {
            public StreamEntryID exec(Jedis jedis) {
                return jedis.xadd(key, entryId, RedisUtil.convertArrayToMap(pairs, true), maxLen, approx);
            }
        });
    }

    public StreamEntryID xaddDefault(String key, String... pairs) {
        this.checkKeyTagExists(key);
        return this.xadd(key, StreamEntryID.NEW_ENTRY, pairs);
    }

    public StreamEntryID xaddDefault(String key, Map<String, String> pairs) {
        this.checkKeyTagExists(key);
        return this.xadd(key, StreamEntryID.NEW_ENTRY, pairs);
    }

    public StreamEntryID xaddWithMaxlen(String key, long maxLen, StreamEntryID entryId, Map<String, String> pairs) {
        this.checkKeyTagExists(key);
        return this.xaddWithMaxlen(key, false, maxLen, entryId, pairs);
    }

    public StreamEntryID xaddWithMaxlen(String key, long maxLen, StreamEntryID entryId, String... pairs) {
        this.checkKeyTagExists(key);
        return this.xaddWithMaxlen(key, false, maxLen, entryId, RedisUtil.convertArrayToMap(pairs, true));
    }

    public long xlen(final String key) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xlen(key);
            }
        });
    }

    public List<StreamEntry> xrange(final String key, final StreamEntryID start, final StreamEntryID end, final int count) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamEntry>>() {
            public List<StreamEntry> exec(Jedis jedis) {
                return jedis.xrange(key, start, end, count);
            }
        });
    }

    public List<StreamEntry> xrevrange(final String key, final StreamEntryID start, final StreamEntryID end, final int count) {
        this.checkKeyTagExists(key);
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamEntry>>() {
            public List<StreamEntry> exec(Jedis jedis) {
                return jedis.xrevrange(key, start, end, count);
            }
        });
    }

    public long xdel(final String key, final StreamEntryID entryId) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xdel(key, new StreamEntryID[]{entryId});
            }
        });
    }

    public long xtrimWithMaxlen(final String key, final long maxlen) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xtrim(key, maxlen, false);
            }
        });
    }

    public long xtrimWithMaxlen(final String key, final boolean approx, final long maxlen) {
        this.checkKeyTagExists(key);
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xtrim(key, maxlen, approx);
            }
        });
    }
}
