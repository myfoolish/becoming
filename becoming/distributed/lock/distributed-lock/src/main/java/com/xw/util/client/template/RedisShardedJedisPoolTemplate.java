package com.xw.util.client.template;

import com.xw.util.client.exception.DefaultExceptionHandler;
import com.xw.util.client.exception.RedisException;
import com.xw.util.client.inter.IExceptionHandler;
import com.xw.util.client.inter.IGenericOperation;
import com.xw.util.client.inter.IRedisCommandTemplate;
import com.xw.util.client.inter.IRedisShardedOperation;
import com.xw.util.client.pool.RedisShardedPool;
import com.xw.util.client.util.RedisUtil;
import java.util.HashMap;
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
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RedisShardedJedisPoolTemplate extends GenericPoolTemplate<ShardedJedis> implements IRedisCommandTemplate {
    private IExceptionHandler exceptionHandler;

    public void setExceptionHandler(IExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public RedisShardedJedisPoolTemplate(RedisShardedPool pool) {
        super(pool);
        this.exceptionHandler = DefaultExceptionHandler.INSTANCE;
    }

    public <T> T doRedis(IRedisShardedOperation<T> operation) throws RedisException {
        return super.doRedis(operation);
    }

    public <T> T doRedis(IGenericOperation<T, ShardedJedis> operation) throws RedisException {
        throw new NotImplementedException();
    }

    private <T> T doRedisWithExceptionHandler(IRedisShardedOperation<T> operation) {
        T ret = null;

        try {
            ret = super.doRedis(operation);
        } catch (RedisException var4) {
            this.exceptionHandler.dealException(var4);
        }

        return ret;
    }

    public Map<String, String> commonHgetAll(final String key) {
        final Map<String, String> ret = new HashMap();
        this.doRedisWithExceptionHandler(new IRedisShardedOperation<Integer>() {
            public Integer exec(ShardedJedis shardedJedis) {
                Iterator var2 = shardedJedis.getAllShards().iterator();

                while(var2.hasNext()) {
                    Jedis jedis = (Jedis)var2.next();
                    Map<String, String> shardRet = jedis.hgetAll(key);
                    if (shardRet != null) {
                        ret.putAll(shardRet);
                    }
                }

                return ret.size();
            }
        });
        return ret;
    }

    public Set<String> commonSmembers(final String key) {
        final Set<String> ret = new HashSet();
        this.doRedisWithExceptionHandler(new IRedisShardedOperation<Integer>() {
            public Integer exec(ShardedJedis shardedJedis) {
                Iterator var2 = shardedJedis.getAllShards().iterator();

                while(var2.hasNext()) {
                    Jedis jedis = (Jedis)var2.next();
                    Set<String> shardRet = jedis.smembers(key);
                    if (shardRet != null) {
                        ret.addAll(shardRet);
                    }
                }

                return ret.size();
            }
        });
        return ret;
    }

    public String set(final String key, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.set(key, value);
            }
        });
    }

    public String set(final String key, final String value, final String nxxx, String expx, final long time) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                SetParams setParams = new SetParams();
                if ("nx".equalsIgnoreCase(nxxx)) {
                    setParams.nx();
                }

                if ("xx".equalsIgnoreCase(nxxx)) {
                    setParams.xx();
                }

                setParams.ex((int)time);
                return shardedJedis.set(key, value, setParams);
            }
        });
    }

    public String set(final String key, final String value, final String nxxx) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                SetParams setParams = new SetParams();
                if ("nx".equalsIgnoreCase(nxxx)) {
                    setParams.nx();
                }

                if ("xx".equalsIgnoreCase(nxxx)) {
                    setParams.xx();
                }

                return shardedJedis.set(key, value, setParams);
            }
        });
    }

    public String get(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.get(key);
            }
        });
    }

    public Boolean exists(final String key) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Boolean>() {
            public Boolean exec(ShardedJedis shardedJedis) {
                return shardedJedis.exists(key);
            }
        });
    }

    public Long persist(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.persist(key);
            }
        });
    }

    public String type(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.type(key);
            }
        });
    }

    public Long expire(final String key, final int seconds) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.expire(key, seconds);
            }
        });
    }

    public Long pexpire(final String key, final long milliseconds) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.pexpire(key, milliseconds);
            }
        });
    }

    public Long expireAt(final String key, final long unixTime) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.expireAt(key, unixTime);
            }
        });
    }

    public Long pexpireAt(final String key, final long millisecondsTimestamp) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.pexpireAt(key, millisecondsTimestamp);
            }
        });
    }

    public Long ttl(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.ttl(key);
            }
        });
    }

    public Long pttl(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.pttl(key);
            }
        });
    }

    public Boolean setbit(final String key, final long offset, final boolean value) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Boolean>() {
            public Boolean exec(ShardedJedis shardedJedis) {
                return shardedJedis.setbit(key, offset, value);
            }
        });
    }

    public Boolean setbit(final String key, final long offset, final String value) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Boolean>() {
            public Boolean exec(ShardedJedis shardedJedis) {
                return shardedJedis.setbit(key, offset, value);
            }
        });
    }

    public Boolean getbit(final String key, final long offset) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Boolean>() {
            public Boolean exec(ShardedJedis shardedJedis) {
                return shardedJedis.getbit(key, offset);
            }
        });
    }

    public Long setrange(final String key, final long offset, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.setrange(key, offset, value);
            }
        });
    }

    public String getrange(final String key, final long startOffset, final long endOffset) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.getrange(key, startOffset, endOffset);
            }
        });
    }

    public String getSet(final String key, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.getSet(key, value);
            }
        });
    }

    public Long setnx(final String key, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.setnx(key, value);
            }
        });
    }

    public String setex(final String key, final int seconds, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.setex(key, seconds, value);
            }
        });
    }

    public String psetex(final String key, final long milliseconds, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.psetex(key, milliseconds, value);
            }
        });
    }

    public Long decrBy(final String key, final long integer) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.decrBy(key, integer);
            }
        });
    }

    public Long decr(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.decr(key);
            }
        });
    }

    public Long incrBy(final String key, final long integer) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.incrBy(key, integer);
            }
        });
    }

    public Double incrByFloat(final String key, final double value) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Double>() {
            public Double exec(ShardedJedis shardedJedis) {
                return shardedJedis.incrByFloat(key, value);
            }
        });
    }

    public Long incr(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.incr(key);
            }
        });
    }

    public Long append(final String key, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.append(key, value);
            }
        });
    }

    public String substr(final String key, final int start, final int end) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.substr(key, start, end);
            }
        });
    }

    public Long hset(final String key, final String field, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.hset(key, field, value);
            }
        });
    }

    public String hget(final String key, final String field) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.hget(key, field);
            }
        });
    }

    public Long hsetnx(final String key, final String field, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.hsetnx(key, field, value);
            }
        });
    }

    public String hmset(final String key, final Map<String, String> hash) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.hmset(key, hash);
            }
        });
    }

    public List<String> hmget(final String key, final String... fields) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.hmget(key, fields);
            }
        });
    }

    public Long hincrBy(final String key, final String field, final long value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.hincrBy(key, field, value);
            }
        });
    }

    public Double hincrByFloat(final String key, final String field, final double value) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Double>() {
            public Double exec(ShardedJedis shardedJedis) {
                return shardedJedis.hincrByFloat(key, field, value);
            }
        });
    }

    public Boolean hexists(final String key, final String field) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Boolean>() {
            public Boolean exec(ShardedJedis shardedJedis) {
                return shardedJedis.hexists(key, field);
            }
        });
    }

    public Long hdel(final String key, final String... field) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.hdel(key, field);
            }
        });
    }

    public Long hlen(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.hlen(key);
            }
        });
    }

    public Set<String> hkeys(final String key) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.hkeys(key);
            }
        });
    }

    public List<String> hvals(final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.hvals(key);
            }
        });
    }

    public Map<String, String> hgetAll(final String key) {
        return (Map)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Map<String, String>>() {
            public Map<String, String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.hgetAll(key);
            }
        });
    }

    public Long rpush(final String key, final String... string) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.rpush(key, string);
            }
        });
    }

    public Long lpush(final String key, final String... string) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.lpush(key, string);
            }
        });
    }

    public Long llen(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.llen(key);
            }
        });
    }

    public List<String> lrange(final String key, final long start, final long end) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.lrange(key, start, end);
            }
        });
    }

    public String ltrim(final String key, final long start, final long end) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.ltrim(key, start, end);
            }
        });
    }

    public String lindex(final String key, final long index) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.lindex(key, index);
            }
        });
    }

    public String lset(final String key, final long index, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.lset(key, index, value);
            }
        });
    }

    public Long lrem(final String key, final long count, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.lrem(key, count, value);
            }
        });
    }

    public String lpop(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.lpop(key);
            }
        });
    }

    public String rpop(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.rpop(key);
            }
        });
    }

    public Long sadd(final String key, final String... member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.sadd(key, member);
            }
        });
    }

    public Set<String> smembers(final String key) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.smembers(key);
            }
        });
    }

    public Long srem(final String key, final String... member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.srem(key, member);
            }
        });
    }

    public String spop(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.spop(key);
            }
        });
    }

    public Set<String> spop(final String key, final long count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.spop(key, count);
            }
        });
    }

    public Long scard(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.scard(key);
            }
        });
    }

    public Boolean sismember(final String key, final String member) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Boolean>() {
            public Boolean exec(ShardedJedis shardedJedis) {
                return shardedJedis.sismember(key, member);
            }
        });
    }

    public String srandmember(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.srandmember(key);
            }
        });
    }

    public List<String> srandmember(final String key, final int count) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.srandmember(key, count);
            }
        });
    }

    public Long strlen(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.strlen(key);
            }
        });
    }

    public Long zadd(final String key, final double score, final String member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zadd(key, score, member);
            }
        });
    }

    public Long zadd(final String key, final double score, final String member, final ZAddParams params) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zadd(key, score, member, params);
            }
        });
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zadd(key, scoreMembers);
            }
        });
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers, final ZAddParams params) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zadd(key, scoreMembers, params);
            }
        });
    }

    public Set<String> zrange(final String key, final long start, final long end) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrange(key, start, end);
            }
        });
    }

    public Long zrem(final String key, final String... member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrem(key, member);
            }
        });
    }

    public Double zincrby(final String key, final double score, final String member) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Double>() {
            public Double exec(ShardedJedis shardedJedis) {
                return shardedJedis.zincrby(key, score, member);
            }
        });
    }

    public Double zincrby(final String key, final double score, final String member, final ZIncrByParams params) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Double>() {
            public Double exec(ShardedJedis shardedJedis) {
                return shardedJedis.zincrby(key, score, member, params);
            }
        });
    }

    public Long zrank(final String key, final String member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrank(key, member);
            }
        });
    }

    public Long zrevrank(final String key, final String member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrank(key, member);
            }
        });
    }

    public Set<String> zrevrange(final String key, final long start, final long end) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrange(key, start, end);
            }
        });
    }

    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeWithScores(key, start, end);
            }
        });
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeWithScores(key, start, end);
            }
        });
    }

    public Long zcard(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zcard(key);
            }
        });
    }

    public Double zscore(final String key, final String member) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Double>() {
            public Double exec(ShardedJedis shardedJedis) {
                return shardedJedis.zscore(key, member);
            }
        });
    }

    public List<String> sort(final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.sort(key);
            }
        });
    }

    public List<String> sort(final String key, final SortingParams sortingParameters) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.sort(key, sortingParameters);
            }
        });
    }

    public Long zcount(final String key, final double min, final double max) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zcount(key, min, max);
            }
        });
    }

    public Long zcount(final String key, final String min, final String max) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zcount(key, min, max);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByScore(key, min, max);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByScore(key, min, max);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByScore(key, max, min);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByScore(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByScore(key, max, min);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByScore(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByScore(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByScoreWithScores(key, max, min);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByScore(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByScoreWithScores(key, max, min);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<Tuple>>() {
            public Set<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        });
    }

    public Long zremrangeByRank(final String key, final long start, final long end) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zremrangeByRank(key, start, end);
            }
        });
    }

    public Long zremrangeByScore(final String key, final double start, final double end) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zremrangeByScore(key, start, end);
            }
        });
    }

    public Long zremrangeByScore(final String key, final String start, final String end) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zremrangeByScore(key, start, end);
            }
        });
    }

    public Long zlexcount(final String key, final String min, final String max) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zlexcount(key, min, max);
            }
        });
    }

    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByLex(key, min, max);
            }
        });
    }

    public Set<String> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrangeByLex(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByLex(final String key, final String max, final String min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByLex(key, max, min);
            }
        });
    }

    public Set<String> zrevrangeByLex(final String key, final String max, final String min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Set<String>>() {
            public Set<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zrevrangeByLex(key, max, min, offset, count);
            }
        });
    }

    public Long zremrangeByLex(final String key, final String min, final String max) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.zremrangeByLex(key, min, max);
            }
        });
    }

    public Long linsert(final String key, final ListPosition where, final String pivot, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.linsert(key, where, pivot, value);
            }
        });
    }

    public Long lpushx(final String key, final String... string) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.lpushx(key, string);
            }
        });
    }

    public Long rpushx(final String key, final String... string) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.rpushx(key, string);
            }
        });
    }

    public List<String> blpop(final String arg) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.blpop(arg);
            }
        });
    }

    public List<String> blpop(final int timeout, final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.blpop(timeout, key);
            }
        });
    }

    public List<String> brpop(final String arg) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.brpop(arg);
            }
        });
    }

    public List<String> brpop(final int timeout, final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.brpop(timeout, key);
            }
        });
    }

    public Long del(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.del(key);
            }
        });
    }

    public String echo(final String string) {
        return (String)this.doRedisWithExceptionHandler(new IRedisShardedOperation<String>() {
            public String exec(ShardedJedis shardedJedis) {
                return shardedJedis.echo(string);
            }
        });
    }

    public Long move(final String key, final int dbIndex) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.move(key, dbIndex);
            }
        });
    }

    public Long bitcount(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.bitcount(key);
            }
        });
    }

    public Long bitcount(final String key, final long start, final long end) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.bitcount(key, start, end);
            }
        });
    }

    public Long bitpos(final String key, final boolean value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.bitpos(key, value);
            }
        });
    }

    public Long bitpos(final String key, final boolean value, final BitPosParams params) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.bitpos(key, value, params);
            }
        });
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final int cursor) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<Map.Entry<String, String>>>() {
            public ScanResult<Map.Entry<String, String>> exec(ShardedJedis shardedJedis) {
                return shardedJedis.hscan(key, String.valueOf(cursor));
            }
        });
    }

    public ScanResult<String> sscan(final String key, final int cursor) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<String>>() {
            public ScanResult<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.sscan(key, String.valueOf(cursor));
            }
        });
    }

    public ScanResult<Tuple> zscan(final String key, final int cursor) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<Tuple>>() {
            public ScanResult<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zscan(key, String.valueOf(cursor));
            }
        });
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<Map.Entry<String, String>>>() {
            public ScanResult<Map.Entry<String, String>> exec(ShardedJedis shardedJedis) {
                return shardedJedis.hscan(key, cursor);
            }
        });
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor, final ScanParams params) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<Map.Entry<String, String>>>() {
            public ScanResult<Map.Entry<String, String>> exec(ShardedJedis shardedJedis) {
                return shardedJedis.hscan(key, cursor, params);
            }
        });
    }

    public ScanResult<String> sscan(final String key, final String cursor) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<String>>() {
            public ScanResult<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.sscan(key, cursor);
            }
        });
    }

    public ScanResult<String> sscan(final String key, final String cursor, final ScanParams params) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<String>>() {
            public ScanResult<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.sscan(key, cursor, params);
            }
        });
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<Tuple>>() {
            public ScanResult<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zscan(key, cursor);
            }
        });
    }

    public ScanResult<Tuple> zscan(final String key, final String cursor, final ScanParams params) {
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisShardedOperation<ScanResult<Tuple>>() {
            public ScanResult<Tuple> exec(ShardedJedis shardedJedis) {
                return shardedJedis.zscan(key, cursor, params);
            }
        });
    }

    public Long pfadd(final String key, final String... elements) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.pfadd(key, elements);
            }
        });
    }

    public long pfcount(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.pfcount(key);
            }
        });
    }

    public Long geoadd(final String key, final double longitude, final double latitude, final String member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.geoadd(key, longitude, latitude, member);
            }
        });
    }

    public Long geoadd(final String key, final Map<String, GeoCoordinate> memberCoordinateMap) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.geoadd(key, memberCoordinateMap);
            }
        });
    }

    public Double geodist(final String key, final String member1, final String member2) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Double>() {
            public Double exec(ShardedJedis shardedJedis) {
                return shardedJedis.geodist(key, member1, member2);
            }
        });
    }

    public Double geodist(final String key, final String member1, final String member2, final GeoUnit unit) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Double>() {
            public Double exec(ShardedJedis shardedJedis) {
                return shardedJedis.geodist(key, member1, member2, unit);
            }
        });
    }

    public List<String> geohash(final String key, final String... members) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<String>>() {
            public List<String> exec(ShardedJedis shardedJedis) {
                return shardedJedis.geohash(key, members);
            }
        });
    }

    public List<GeoCoordinate> geopos(final String key, final String... members) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<GeoCoordinate>>() {
            public List<GeoCoordinate> exec(ShardedJedis shardedJedis) {
                return shardedJedis.geopos(key, members);
            }
        });
    }

    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(ShardedJedis shardedJedis) {
                return shardedJedis.georadius(key, longitude, latitude, radius, unit);
            }
        });
    }

    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(ShardedJedis shardedJedis) {
                return shardedJedis.georadius(key, longitude, latitude, radius, unit, param);
            }
        });
    }

    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(ShardedJedis shardedJedis) {
                return shardedJedis.georadiusByMember(key, member, radius, unit);
            }
        });
    }

    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(ShardedJedis shardedJedis) {
                return shardedJedis.georadiusByMember(key, member, radius, unit, param);
            }
        });
    }

    public List<Long> bitfield(final String key, final String... arguments) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<Long>>() {
            public List<Long> exec(ShardedJedis shardedJedis) {
                return shardedJedis.bitfield(key, arguments);
            }
        });
    }

    public long unlink(String... keys) {
        long s = 0L;
        String[] var4 = keys;
        int var5 = keys.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            final String key = var4[var6];
            s += (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
                public Long exec(ShardedJedis shardedJedis) {
                    return shardedJedis.unlink(key);
                }
            });
        }

        return s;
    }

    public void done() {
    }

    public StreamEntryID xadd(final String key, final StreamEntryID entryId, final Map<String, String> pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisShardedOperation<StreamEntryID>() {
            public StreamEntryID exec(ShardedJedis shardedJedis) {
                return shardedJedis.xadd(key, entryId, pairs);
            }
        });
    }

    public StreamEntryID xadd(final String key, final StreamEntryID entryId, final String... pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisShardedOperation<StreamEntryID>() {
            public StreamEntryID exec(ShardedJedis shardedJedis) {
                return shardedJedis.xadd(key, entryId, RedisUtil.convertArrayToMap(pairs, true));
            }
        });
    }

    public StreamEntryID xaddWithMaxlen(final String key, final boolean approx, final long maxLen, final StreamEntryID entryId, final Map<String, String> pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisShardedOperation<StreamEntryID>() {
            public StreamEntryID exec(ShardedJedis shardedJedis) {
                return shardedJedis.xadd(key, entryId, pairs, maxLen, approx);
            }
        });
    }

    public StreamEntryID xaddWithMaxlen(final String key, final boolean approx, final long maxLen, final StreamEntryID entryId, final String... pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisShardedOperation<StreamEntryID>() {
            public StreamEntryID exec(ShardedJedis shardedJedis) {
                return shardedJedis.xadd(key, entryId, RedisUtil.convertArrayToMap(pairs, true), maxLen, approx);
            }
        });
    }

    public StreamEntryID xaddDefault(final String key, final String... pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisShardedOperation<StreamEntryID>() {
            public StreamEntryID exec(ShardedJedis shardedJedis) {
                return shardedJedis.xadd(key, StreamEntryID.NEW_ENTRY, RedisUtil.convertArrayToMap(pairs, true));
            }
        });
    }

    public StreamEntryID xaddDefault(final String key, final Map<String, String> pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisShardedOperation<StreamEntryID>() {
            public StreamEntryID exec(ShardedJedis shardedJedis) {
                return shardedJedis.xadd(key, StreamEntryID.NEW_ENTRY, pairs);
            }
        });
    }

    public StreamEntryID xaddWithMaxlen(final String key, final long maxLen, final StreamEntryID entryId, final Map<String, String> pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisShardedOperation<StreamEntryID>() {
            public StreamEntryID exec(ShardedJedis shardedJedis) {
                return shardedJedis.xadd(key, entryId, pairs, maxLen, false);
            }
        });
    }

    public StreamEntryID xaddWithMaxlen(final String key, final long maxLen, final StreamEntryID entryId, final String... pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisShardedOperation<StreamEntryID>() {
            public StreamEntryID exec(ShardedJedis shardedJedis) {
                return shardedJedis.xadd(key, entryId, RedisUtil.convertArrayToMap(pairs, true), maxLen, false);
            }
        });
    }

    public long xlen(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.xlen(key);
            }
        });
    }

    public List<StreamEntry> xrange(final String key, final StreamEntryID start, final StreamEntryID end, final int count) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<StreamEntry>>() {
            public List<StreamEntry> exec(ShardedJedis shardedJedis) {
                return shardedJedis.xrange(key, start, end, count);
            }
        });
    }

    public List<StreamEntry> xrevrange(final String key, final StreamEntryID start, final StreamEntryID end, final int count) {
        return (List)this.doRedisWithExceptionHandler(new IRedisShardedOperation<List<StreamEntry>>() {
            public List<StreamEntry> exec(ShardedJedis shardedJedis) {
                return shardedJedis.xrevrange(key, start, end, count);
            }
        });
    }

    public long xdel(final String key, final StreamEntryID entryId) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.xdel(key, new StreamEntryID[]{entryId});
            }
        });
    }

    public long xtrimWithMaxlen(final String key, final long maxlen) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.xtrim(key, maxlen, false);
            }
        });
    }

    public long xtrimWithMaxlen(final String key, final boolean approx, final long maxlen) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisShardedOperation<Long>() {
            public Long exec(ShardedJedis shardedJedis) {
                return shardedJedis.xtrim(key, maxlen, approx);
            }
        });
    }
}
