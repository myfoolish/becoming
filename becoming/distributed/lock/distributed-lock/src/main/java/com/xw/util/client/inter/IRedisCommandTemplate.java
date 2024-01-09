package com.xw.util.client.inter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;

public interface IRedisCommandTemplate extends IStreamDataCommands {
    String set(String var1, String var2);

    String set(String var1, String var2, String var3, String var4, long var5);

    String set(String var1, String var2, String var3);

    String get(String var1);

    Boolean exists(String var1);

    Long persist(String var1);

    String type(String var1);

    Long expire(String var1, int var2);

    Long pexpire(String var1, long var2);

    Long expireAt(String var1, long var2);

    Long pexpireAt(String var1, long var2);

    Long ttl(String var1);

    Long pttl(String var1);

    Boolean setbit(String var1, long var2, boolean var4);

    Boolean setbit(String var1, long var2, String var4);

    Boolean getbit(String var1, long var2);

    Long setrange(String var1, long var2, String var4);

    String getrange(String var1, long var2, long var4);

    String getSet(String var1, String var2);

    Long setnx(String var1, String var2);

    String setex(String var1, int var2, String var3);

    String psetex(String var1, long var2, String var4);

    Long decrBy(String var1, long var2);

    Long decr(String var1);

    Long incrBy(String var1, long var2);

    Double incrByFloat(String var1, double var2);

    Long incr(String var1);

    Long append(String var1, String var2);

    String substr(String var1, int var2, int var3);

    Long hset(String var1, String var2, String var3);

    String hget(String var1, String var2);

    Long hsetnx(String var1, String var2, String var3);

    String hmset(String var1, Map<String, String> var2);

    List<String> hmget(String var1, String... var2);

    Long hincrBy(String var1, String var2, long var3);

    Double hincrByFloat(String var1, String var2, double var3);

    Boolean hexists(String var1, String var2);

    Long hdel(String var1, String... var2);

    Long hlen(String var1);

    Set<String> hkeys(String var1);

    List<String> hvals(String var1);

    Map<String, String> hgetAll(String var1);

    Long rpush(String var1, String... var2);

    Long lpush(String var1, String... var2);

    Long llen(String var1);

    List<String> lrange(String var1, long var2, long var4);

    String ltrim(String var1, long var2, long var4);

    String lindex(String var1, long var2);

    String lset(String var1, long var2, String var4);

    Long lrem(String var1, long var2, String var4);

    String lpop(String var1);

    String rpop(String var1);

    Long sadd(String var1, String... var2);

    Set<String> smembers(String var1);

    Long srem(String var1, String... var2);

    String spop(String var1);

    Set<String> spop(String var1, long var2);

    Long scard(String var1);

    Boolean sismember(String var1, String var2);

    String srandmember(String var1);

    List<String> srandmember(String var1, int var2);

    Long strlen(String var1);

    Long zadd(String var1, double var2, String var4);

    Long zadd(String var1, double var2, String var4, ZAddParams var5);

    Long zadd(String var1, Map<String, Double> var2);

    Long zadd(String var1, Map<String, Double> var2, ZAddParams var3);

    Set<String> zrange(String var1, long var2, long var4);

    Long zrem(String var1, String... var2);

    Double zincrby(String var1, double var2, String var4);

    Double zincrby(String var1, double var2, String var4, ZIncrByParams var5);

    Long zrank(String var1, String var2);

    Long zrevrank(String var1, String var2);

    Set<String> zrevrange(String var1, long var2, long var4);

    Set<Tuple> zrangeWithScores(String var1, long var2, long var4);

    Set<Tuple> zrevrangeWithScores(String var1, long var2, long var4);

    Long zcard(String var1);

    Double zscore(String var1, String var2);

    List<String> sort(String var1);

    List<String> sort(String var1, SortingParams var2);

    Long zcount(String var1, double var2, double var4);

    Long zcount(String var1, String var2, String var3);

    Set<String> zrangeByScore(String var1, double var2, double var4);

    Set<String> zrangeByScore(String var1, String var2, String var3);

    Set<String> zrevrangeByScore(String var1, double var2, double var4);

    Set<String> zrangeByScore(String var1, double var2, double var4, int var6, int var7);

    Set<String> zrevrangeByScore(String var1, String var2, String var3);

    Set<String> zrangeByScore(String var1, String var2, String var3, int var4, int var5);

    Set<String> zrevrangeByScore(String var1, double var2, double var4, int var6, int var7);

    Set<Tuple> zrangeByScoreWithScores(String var1, double var2, double var4);

    Set<Tuple> zrevrangeByScoreWithScores(String var1, double var2, double var4);

    Set<Tuple> zrangeByScoreWithScores(String var1, double var2, double var4, int var6, int var7);

    Set<String> zrevrangeByScore(String var1, String var2, String var3, int var4, int var5);

    Set<Tuple> zrangeByScoreWithScores(String var1, String var2, String var3);

    Set<Tuple> zrevrangeByScoreWithScores(String var1, String var2, String var3);

    Set<Tuple> zrangeByScoreWithScores(String var1, String var2, String var3, int var4, int var5);

    Set<Tuple> zrevrangeByScoreWithScores(String var1, double var2, double var4, int var6, int var7);

    Set<Tuple> zrevrangeByScoreWithScores(String var1, String var2, String var3, int var4, int var5);

    Long zremrangeByRank(String var1, long var2, long var4);

    Long zremrangeByScore(String var1, double var2, double var4);

    Long zremrangeByScore(String var1, String var2, String var3);

    Long zlexcount(String var1, String var2, String var3);

    Set<String> zrangeByLex(String var1, String var2, String var3);

    Set<String> zrangeByLex(String var1, String var2, String var3, int var4, int var5);

    Set<String> zrevrangeByLex(String var1, String var2, String var3);

    Set<String> zrevrangeByLex(String var1, String var2, String var3, int var4, int var5);

    Long zremrangeByLex(String var1, String var2, String var3);

    Long linsert(String var1, ListPosition var2, String var3, String var4);

    Long lpushx(String var1, String... var2);

    Long rpushx(String var1, String... var2);

    /** @deprecated */
    @Deprecated
    List<String> blpop(String var1);

    List<String> blpop(int var1, String var2);

    /** @deprecated */
    @Deprecated
    List<String> brpop(String var1);

    List<String> brpop(int var1, String var2);

    Long del(String var1);

    String echo(String var1);

    Long move(String var1, int var2);

    Long bitcount(String var1);

    Long bitcount(String var1, long var2, long var4);

    Long bitpos(String var1, boolean var2);

    Long bitpos(String var1, boolean var2, BitPosParams var3);

    /** @deprecated */
    @Deprecated
    ScanResult<Map.Entry<String, String>> hscan(String var1, int var2);

    /** @deprecated */
    @Deprecated
    ScanResult<String> sscan(String var1, int var2);

    /** @deprecated */
    @Deprecated
    ScanResult<Tuple> zscan(String var1, int var2);

    ScanResult<Map.Entry<String, String>> hscan(String var1, String var2);

    ScanResult<Map.Entry<String, String>> hscan(String var1, String var2, ScanParams var3);

    ScanResult<String> sscan(String var1, String var2);

    ScanResult<String> sscan(String var1, String var2, ScanParams var3);

    ScanResult<Tuple> zscan(String var1, String var2);

    ScanResult<Tuple> zscan(String var1, String var2, ScanParams var3);

    Long pfadd(String var1, String... var2);

    long pfcount(String var1);

    Long geoadd(String var1, double var2, double var4, String var6);

    Long geoadd(String var1, Map<String, GeoCoordinate> var2);

    Double geodist(String var1, String var2, String var3);

    Double geodist(String var1, String var2, String var3, GeoUnit var4);

    List<String> geohash(String var1, String... var2);

    List<GeoCoordinate> geopos(String var1, String... var2);

    List<GeoRadiusResponse> georadius(String var1, double var2, double var4, double var6, GeoUnit var8);

    List<GeoRadiusResponse> georadius(String var1, double var2, double var4, double var6, GeoUnit var8, GeoRadiusParam var9);

    List<GeoRadiusResponse> georadiusByMember(String var1, String var2, double var3, GeoUnit var5);

    List<GeoRadiusResponse> georadiusByMember(String var1, String var2, double var3, GeoUnit var5, GeoRadiusParam var6);

    List<Long> bitfield(String var1, String... var2);

    long unlink(String... var1);

    void done();
}
