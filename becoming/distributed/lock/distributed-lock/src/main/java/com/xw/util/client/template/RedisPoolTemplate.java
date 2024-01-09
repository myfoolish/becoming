package com.xw.util.client.template;

import com.xw.util.client.exception.RedisException;
import com.xw.util.client.inter.IGenericOperation;
import com.xw.util.client.inter.IRedisOperation;
import com.xw.util.client.inter.IRedisPool2;
import com.xw.util.client.inter.IRedisTemplate;
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
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public final class RedisPoolTemplate extends RedisStreamTemplate implements IRedisTemplate {
    public RedisPoolTemplate(IRedisPool2<Jedis> pool) {
        super(pool);
    }

    public <T> T doRedis(IGenericOperation<T, Jedis> operation) throws RedisException {
        throw new NotImplementedException();
    }

    public String set(final String key, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.set(key, value);
            }
        });
    }

    public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                SetParams setParams = new SetParams();
                if ("nx".equalsIgnoreCase(nxxx)) {
                    setParams.nx();
                }

                if ("xx".equalsIgnoreCase(nxxx)) {
                    setParams.xx();
                }

                if ("ex".equalsIgnoreCase(expx)) {
                    setParams.ex((int)time);
                }

                if ("px".equalsIgnoreCase(expx)) {
                    setParams.px(time);
                }

                return jedis.set(key, value, setParams);
            }
        });
    }

    public String set(final String key, final String value, final String nxxx) {
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
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.get(key);
            }
        });
    }

    public Boolean exists(final String key) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.exists(key);
            }
        });
    }

    public Long persist(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.persist(key);
            }
        });
    }

    public String type(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.type(key);
            }
        });
    }

    public Long expire(final String key, final int seconds) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.expire(key, seconds);
            }
        });
    }

    public Long pexpire(final String key, final long milliseconds) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pexpire(key, milliseconds);
            }
        });
    }

    public Long expireAt(final String key, final long unixTime) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.expireAt(key, unixTime);
            }
        });
    }

    public Long pexpireAt(final String key, final long millisecondsTimestamp) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pexpireAt(key, millisecondsTimestamp);
            }
        });
    }

    public Long ttl(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.ttl(key);
            }
        });
    }

    public Long pttl(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pttl(key);
            }
        });
    }

    public Boolean setbit(final String key, final long offset, final boolean value) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.setbit(key, offset, value);
            }
        });
    }

    public Boolean setbit(final String key, final long offset, final String value) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.setbit(key, offset, value);
            }
        });
    }

    public Boolean getbit(final String key, final long offset) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.getbit(key, offset);
            }
        });
    }

    public Long setrange(final String key, final long offset, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.setrange(key, offset, value);
            }
        });
    }

    public String getrange(final String key, final long startOffset, final long endOffset) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.getrange(key, startOffset, endOffset);
            }
        });
    }

    public String getSet(final String key, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.getSet(key, value);
            }
        });
    }

    public Long setnx(final String key, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.setnx(key, value);
            }
        });
    }

    public String setex(final String key, final int seconds, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.setex(key, seconds, value);
            }
        });
    }

    public String psetex(final String key, final long milliseconds, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.psetex(key, milliseconds, value);
            }
        });
    }

    public Long decrBy(final String key, final long integer) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.decrBy(key, integer);
            }
        });
    }

    public Long decr(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.decr(key);
            }
        });
    }

    public Long incrBy(final String key, final long integer) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.incrBy(key, integer);
            }
        });
    }

    public Double incrByFloat(final String key, final double value) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.incrByFloat(key, value);
            }
        });
    }

    public Long incr(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.incr(key);
            }
        });
    }

    public Long append(final String key, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.append(key, value);
            }
        });
    }

    public String substr(final String key, final int start, final int end) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.substr(key, start, end);
            }
        });
    }

    public Long hset(final String key, final String field, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hset(key, field, value);
            }
        });
    }

    public String hget(final String key, final String field) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.hget(key, field);
            }
        });
    }

    public Long hsetnx(final String key, final String field, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hsetnx(key, field, value);
            }
        });
    }

    public String hmset(final String key, final Map<String, String> hash) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.hmset(key, hash);
            }
        });
    }

    public List<String> hmget(final String key, final String... fields) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.hmget(key, fields);
            }
        });
    }

    public Long hincrBy(final String key, final String field, final long value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hincrBy(key, field, value);
            }
        });
    }

    public Double hincrByFloat(final String key, final String field, final double value) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.hincrByFloat(key, field, value);
            }
        });
    }

    public Boolean hexists(final String key, final String field) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.hexists(key, field);
            }
        });
    }

    public Long hdel(final String key, final String... field) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hdel(key, field);
            }
        });
    }

    public Long hlen(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.hlen(key);
            }
        });
    }

    public Set<String> hkeys(final String key) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.hkeys(key);
            }
        });
    }

    public List<String> hvals(final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.hvals(key);
            }
        });
    }

    public Map<String, String> hgetAll(final String key) {
        return (Map)this.doRedisWithExceptionHandler(new IRedisOperation<Map<String, String>>() {
            public Map<String, String> exec(Jedis jedis) {
                return jedis.hgetAll(key);
            }
        });
    }

    public Long rpush(final String key, final String... string) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.rpush(key, string);
            }
        });
    }

    public Long lpush(final String key, final String... string) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.lpush(key, string);
            }
        });
    }

    public Long llen(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.llen(key);
            }
        });
    }

    public List<String> lrange(final String key, final long start, final long end) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.lrange(key, start, end);
            }
        });
    }

    public String ltrim(final String key, final long start, final long end) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.ltrim(key, start, end);
            }
        });
    }

    public String lindex(final String key, final long index) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.lindex(key, index);
            }
        });
    }

    public String lset(final String key, final long index, final String value) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.lset(key, index, value);
            }
        });
    }

    public Long lrem(final String key, final long count, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.lrem(key, count, value);
            }
        });
    }

    public String lpop(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.lpop(key);
            }
        });
    }

    public String rpop(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.rpop(key);
            }
        });
    }

    public Long sadd(final String key, final String... member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.sadd(key, member);
            }
        });
    }

    public Set<String> smembers(final String key) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.smembers(key);
            }
        });
    }

    public Long srem(final String key, final String... member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.srem(key, member);
            }
        });
    }

    public String spop(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.spop(key);
            }
        });
    }

    public Set<String> spop(final String key, final long count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.spop(key, count);
            }
        });
    }

    public Long scard(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.scard(key);
            }
        });
    }

    public Boolean sismember(final String key, final String member) {
        return (Boolean)this.doRedisWithExceptionHandler(new IRedisOperation<Boolean>() {
            public Boolean exec(Jedis jedis) {
                return jedis.sismember(key, member);
            }
        });
    }

    public String srandmember(final String key) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.srandmember(key);
            }
        });
    }

    public List<String> srandmember(final String key, final int count) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.srandmember(key, count);
            }
        });
    }

    public Long strlen(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.strlen(key);
            }
        });
    }

    public Long zadd(final String key, final double score, final String member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zadd(key, score, member);
            }
        });
    }

    public Long zadd(final String key, final double score, final String member, final ZAddParams params) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zadd(key, score, member, params);
            }
        });
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zadd(key, scoreMembers);
            }
        });
    }

    public Long zadd(final String key, final Map<String, Double> scoreMembers, final ZAddParams params) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zadd(key, scoreMembers, params);
            }
        });
    }

    public Set<String> zrange(final String key, final long start, final long end) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrange(key, start, end);
            }
        });
    }

    public Long zrem(final String key, final String... member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zrem(key, member);
            }
        });
    }

    public Double zincrby(final String key, final double score, final String member) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.zincrby(key, score, member);
            }
        });
    }

    public Double zincrby(final String key, final double score, final String member, final ZIncrByParams params) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.zincrby(key, score, member, params);
            }
        });
    }

    public Long zrank(final String key, final String member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zrank(key, member);
            }
        });
    }

    public Long zrevrank(final String key, final String member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zrevrank(key, member);
            }
        });
    }

    public Set<String> zrevrange(final String key, final long start, final long end) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrange(key, start, end);
            }
        });
    }

    public Set<Tuple> zrangeWithScores(final String key, final long start, final long end) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeWithScores(key, start, end);
            }
        });
    }

    public Set<Tuple> zrevrangeWithScores(final String key, final long start, final long end) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeWithScores(key, start, end);
            }
        });
    }

    public Long zcard(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zcard(key);
            }
        });
    }

    public Double zscore(final String key, final String member) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.zscore(key, member);
            }
        });
    }

    public List<String> sort(final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.sort(key);
            }
        });
    }

    public List<String> sort(final String key, final SortingParams sortingParameters) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.sort(key, sortingParameters);
            }
        });
    }

    public Long zcount(final String key, final double min, final double max) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zcount(key, min, max);
            }
        });
    }

    public Long zcount(final String key, final String min, final String max) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zcount(key, min, max);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min);
            }
        });
    }

    public Set<String> zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByScore(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeByScoreWithScores(key, max, min);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeByScoreWithScores(key, min, max);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeByScoreWithScores(key, max, min);
            }
        });
    }

    public Set<Tuple> zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        });
    }

    public Set<Tuple> zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<Tuple>>() {
            public Set<Tuple> exec(Jedis jedis) {
                return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        });
    }

    public Long zremrangeByRank(final String key, final long start, final long end) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zremrangeByRank(key, start, end);
            }
        });
    }

    public Long zremrangeByScore(final String key, final double start, final double end) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zremrangeByScore(key, start, end);
            }
        });
    }

    public Long zremrangeByScore(final String key, final String start, final String end) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zremrangeByScore(key, start, end);
            }
        });
    }

    public Long zlexcount(final String key, final String min, final String max) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zlexcount(key, min, max);
            }
        });
    }

    public Set<String> zrangeByLex(final String key, final String min, final String max) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByLex(key, min, max);
            }
        });
    }

    public Set<String> zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrangeByLex(key, min, max, offset, count);
            }
        });
    }

    public Set<String> zrevrangeByLex(final String key, final String max, final String min) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByLex(key, max, min);
            }
        });
    }

    public Set<String> zrevrangeByLex(final String key, final String max, final String min, final int offset, final int count) {
        return (Set)this.doRedisWithExceptionHandler(new IRedisOperation<Set<String>>() {
            public Set<String> exec(Jedis jedis) {
                return jedis.zrevrangeByLex(key, max, min, offset, count);
            }
        });
    }

    public Long zremrangeByLex(final String key, final String min, final String max) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.zremrangeByLex(key, min, max);
            }
        });
    }

    public Long linsert(final String key, final ListPosition where, final String pivot, final String value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.linsert(key, where, pivot, value);
            }
        });
    }

    public Long lpushx(final String key, final String... string) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.lpushx(key, string);
            }
        });
    }

    public Long rpushx(final String key, final String... string) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.rpushx(key, string);
            }
        });
    }

    public List<String> blpop(final String arg) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.blpop(new String[]{arg});
            }
        });
    }

    public List<String> blpop(final int timeout, final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.blpop(timeout, key);
            }
        });
    }

    public List<String> brpop(final String arg) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.brpop(new String[]{arg});
            }
        });
    }

    public List<String> brpop(final int timeout, final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.brpop(timeout, key);
            }
        });
    }

    public Long del(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.del(key);
            }
        });
    }

    public String echo(final String string) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.echo(string);
            }
        });
    }

    public Long move(final String key, final int dbIndex) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.move(key, dbIndex);
            }
        });
    }

    public Long bitcount(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.bitcount(key);
            }
        });
    }

    public Long bitcount(final String key, final long start, final long end) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.bitcount(key, start, end);
            }
        });
    }

    public Long bitpos(final String key, final boolean value) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.bitpos(key, value);
            }
        });
    }

    public Long bitpos(final String key, final boolean value, final BitPosParams params) {
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
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisOperation<ScanResult<Map.Entry<String, String>>>() {
            public ScanResult<Map.Entry<String, String>> exec(Jedis jedis) {
                return jedis.hscan(key, cursor);
            }
        });
    }

    public ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor, final ScanParams params) {
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
        return (ScanResult)this.doRedisWithExceptionHandler(new IRedisOperation<ScanResult<Tuple>>() {
            public ScanResult<Tuple> exec(Jedis jedis) {
                return jedis.zscan(key, cursor, params);
            }
        });
    }

    public Long pfadd(final String key, final String... elements) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pfadd(key, elements);
            }
        });
    }

    public long pfcount(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.pfcount(key);
            }
        });
    }

    public Long geoadd(final String key, final double longitude, final double latitude, final String member) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.geoadd(key, longitude, latitude, member);
            }
        });
    }

    public Long geoadd(final String key, final Map<String, GeoCoordinate> memberCoordinateMap) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.geoadd(key, memberCoordinateMap);
            }
        });
    }

    public Double geodist(final String key, final String member1, final String member2) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.geodist(key, member1, member2);
            }
        });
    }

    public Double geodist(final String key, final String member1, final String member2, final GeoUnit unit) {
        return (Double)this.doRedisWithExceptionHandler(new IRedisOperation<Double>() {
            public Double exec(Jedis jedis) {
                return jedis.geodist(key, member1, member2, unit);
            }
        });
    }

    public List<String> geohash(final String key, final String... members) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<String>>() {
            public List<String> exec(Jedis jedis) {
                return jedis.geohash(key, members);
            }
        });
    }

    public List<GeoCoordinate> geopos(final String key, final String... members) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoCoordinate>>() {
            public List<GeoCoordinate> exec(Jedis jedis) {
                return jedis.geopos(key, members);
            }
        });
    }

    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(Jedis jedis) {
                return jedis.georadius(key, longitude, latitude, radius, unit);
            }
        });
    }

    public List<GeoRadiusResponse> georadius(final String key, final double longitude, final double latitude, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(Jedis jedis) {
                return jedis.georadius(key, longitude, latitude, radius, unit, param);
            }
        });
    }

    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(Jedis jedis) {
                return jedis.georadiusByMember(key, member, radius, unit);
            }
        });
    }

    public List<GeoRadiusResponse> georadiusByMember(final String key, final String member, final double radius, final GeoUnit unit, final GeoRadiusParam param) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<GeoRadiusResponse>>() {
            public List<GeoRadiusResponse> exec(Jedis jedis) {
                return jedis.georadiusByMember(key, member, radius, unit, param);
            }
        });
    }

    public List<Long> bitfield(final String key, final String... arguments) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<Long>>() {
            public List<Long> exec(Jedis jedis) {
                return jedis.bitfield(key, arguments);
            }
        });
    }

    public long publish(final String channel, final String message) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.publish(channel, message);
            }
        });
    }

    public long unlink(final String... keys) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.unlink(keys);
            }
        });
    }

    public void done() {
    }
}