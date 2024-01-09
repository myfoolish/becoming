package com.xw.util.client.template;

import com.xw.util.client.exception.DefaultExceptionHandler;
import com.xw.util.client.exception.RedisConnectionException;
import com.xw.util.client.exception.RedisException;
import com.xw.util.client.exception.RedisRuntimeException;
import com.xw.util.client.inter.IExceptionHandler;
import com.xw.util.client.inter.IGenericOperation;
import com.xw.util.client.inter.IRedisOperation;
import com.xw.util.client.inter.IRedisPool2;
import com.xw.util.client.inter.IStreamCommands;
import com.xw.util.client.util.RedisUtil;
import java.net.SocketTimeoutException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Builder;
import redis.clients.jedis.BuilderFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamConsumersInfo;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.StreamGroupInfo;
import redis.clients.jedis.StreamInfo;
import redis.clients.jedis.StreamPendingEntry;
import redis.clients.jedis.Protocol.Command;
import redis.clients.jedis.Protocol.Keyword;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.util.SafeEncoder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public abstract class RedisStreamTemplate extends JedisPoolSubTemplate implements IStreamCommands {
    protected volatile IExceptionHandler exceptionHandler;
    static final Builder<List<StreamEntryID>> STREAM_ENTRY_ID_LIST = new Builder<List<StreamEntryID>>() {
        public List<StreamEntryID> build(Object data) {
            if (null == data) {
                return null;
            } else {
                List<Object> objectList = (List)data;
                List<StreamEntryID> responses = new ArrayList(objectList.size());
                if (objectList.isEmpty()) {
                    return responses;
                } else {
                    Iterator var4 = objectList.iterator();

                    while(var4.hasNext()) {
                        Object res = var4.next();
                        responses.add(BuilderFactory.STREAM_ENTRY_ID.build(res));
                    }

                    return responses;
                }
            }
        }
    };

    protected <T> T doRedisWithExceptionHandler(IRedisOperation<T> operation) {
        Object ret;
        try {
            ret = super.doRedis(operation);
        } catch (RedisRuntimeException var4) {
            ret = this.exceptionHandler.dealException(new RedisException(var4.getMessage(), var4));
        } catch (RedisException var5) {
            if (var5 instanceof RedisConnectionException) {
                ret = this.exceptionHandler.dealConnectionException(var5);
            } else {
                ret = this.exceptionHandler.dealException(var5);
            }
        }
        return (T) ret;
    }

    public void setExceptionHandler(IExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    protected RedisStreamTemplate(IRedisPool2<Jedis> pool) {
        super(pool);
        this.exceptionHandler = DefaultExceptionHandler.INSTANCE;
    }

    public <T> T doRedis(IRedisOperation<T> operation) throws RedisException {
        return super.doRedis(operation);
    }

    public <T> T doRedis(IGenericOperation<T, Jedis> operation) throws RedisException {
        throw new NotImplementedException();
    }

    public StreamEntryID xadd(final String key, final StreamEntryID entryId, final Map<String, String> pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisOperation<StreamEntryID>() {
            public StreamEntryID exec(Jedis jedis) {
                return jedis.xadd(key, entryId, pairs);
            }
        });
    }

    public StreamEntryID xadd(final String key, final StreamEntryID entryId, final String... pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisOperation<StreamEntryID>() {
            public StreamEntryID exec(Jedis jedis) {
                return jedis.xadd(key, entryId, RedisUtil.convertArrayToMap(pairs, true));
            }
        });
    }

    public StreamEntryID xaddWithMaxlen(final String key, final boolean approx, final long maxLen, final StreamEntryID entryId, final Map<String, String> pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisOperation<StreamEntryID>() {
            public StreamEntryID exec(Jedis jedis) {
                return jedis.xadd(key, entryId, pairs, maxLen, approx);
            }
        });
    }

    public StreamEntryID xaddWithMaxlen(final String key, final boolean approx, final long maxLen, final StreamEntryID entryId, final String... pairs) {
        return (StreamEntryID)this.doRedisWithExceptionHandler(new IRedisOperation<StreamEntryID>() {
            public StreamEntryID exec(Jedis jedis) {
                return jedis.xadd(key, entryId, RedisUtil.convertArrayToMap(pairs, true), maxLen, approx);
            }
        });
    }

    public StreamEntryID xaddDefault(String key, String... pairs) {
        return this.xadd(key, StreamEntryID.NEW_ENTRY, pairs);
    }

    public StreamEntryID xaddDefault(String key, Map<String, String> pairs) {
        return this.xadd(key, StreamEntryID.NEW_ENTRY, pairs);
    }

    public StreamEntryID xaddWithMaxlen(String key, long maxLen, StreamEntryID entryId, Map<String, String> pairs) {
        return this.xaddWithMaxlen(key, false, maxLen, entryId, pairs);
    }

    public StreamEntryID xaddWithMaxlen(String key, long maxLen, StreamEntryID entryId, String... pairs) {
        return this.xaddWithMaxlen(key, false, maxLen, entryId, RedisUtil.convertArrayToMap(pairs, true));
    }

    public long xlen(final String key) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xlen(key);
            }
        });
    }

    public List<StreamEntry> xrange(final String key, final StreamEntryID start, final StreamEntryID end, final int count) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamEntry>>() {
            public List<StreamEntry> exec(Jedis jedis) {
                return jedis.xrange(key, start, end, count);
            }
        });
    }

    public List<StreamEntry> xrevrange(final String key, final StreamEntryID start, final StreamEntryID end, final int count) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamEntry>>() {
            public List<StreamEntry> exec(Jedis jedis) {
                return jedis.xrevrange(key, start, end, count);
            }
        });
    }

    public Map<String, List<StreamEntry>> xread(String... params) {
        return this.xread(0, (String[])params);
    }

    public Map<String, List<StreamEntry>> xread(Map<String, StreamEntryID> pairs) {
        return this.xread(0, (Map)pairs);
    }

    public Map<String, List<StreamEntry>> xread(int count, String... params) {
        if (params != null && params.length >= 2 && params.length % 2 != 1) {
            int l = params.length / 2;
            Map<String, StreamEntryID> map = new HashMap(l);

            for(int i = 0; i < l; ++i) {
                map.put(params[i], params[i + l].equals("$") ? StreamEntryID.LAST_ENTRY : (params[i + l].contains("-") ? new StreamEntryID(params[i + l]) : new StreamEntryID(Long.parseLong(params[i + l]), 0L)));
            }

            return this.xread(count, (Map)map);
        } else {
            return new HashMap();
        }
    }

    public Map<String, List<StreamEntry>> xread(final int count, Map<String, StreamEntryID> pairs) {
        if (pairs != null && !pairs.isEmpty()) {
            final Map.Entry<String, StreamEntryID>[] param = (Map.Entry[])pairs.entrySet().toArray(new Map.Entry[0]);
            List<Map.Entry<String, List<StreamEntry>>> reply = (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<Map.Entry<String, List<StreamEntry>>>>() {
                public List<Map.Entry<String, List<StreamEntry>>> exec(Jedis jedis) {
                    return jedis.xread(count, -1L, param);
                }
            });
            Map<String, List<StreamEntry>> ret = new HashMap();
            if (reply == null) {
                return ret;
            } else {
                Iterator var6 = reply.iterator();

                while(var6.hasNext()) {
                    Map.Entry<String, List<StreamEntry>> entry = (Map.Entry)var6.next();
                    ret.put(entry.getKey(), entry.getValue());
                }

                return ret;
            }
        } else {
            return new HashMap();
        }
    }

    public Map.Entry<String, StreamEntry> xreadBlock(final long block, String... keys) {
        if (keys != null && keys.length != 0) {
            final Map.Entry<String, StreamEntryID>[] param = new Map.Entry[keys.length];
            int i = 0;
            String[] var6 = keys;
            int var7 = keys.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String key = var6[var8];
                param[i++] = new AbstractMap.SimpleEntry(key, StreamEntryID.LAST_ENTRY);
            }

            List<Map.Entry<String, List<StreamEntry>>> reply = (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<Map.Entry<String, List<StreamEntry>>>>() {
                public List<Map.Entry<String, List<StreamEntry>>> exec(Jedis jedis) {
                    return jedis.xread(1, block, param);
                }
            });
            return reply != null && !reply.isEmpty() ? new AbstractMap.SimpleImmutableEntry(((Map.Entry)reply.get(0)).getKey(), ((List)((Map.Entry)reply.get(0)).getValue()).get(0)) : null;
        } else {
            return null;
        }
    }

    public Map.Entry<String, StreamEntry> xreadBlock(String... keys) {
        return this.xreadBlock(0L, keys);
    }

    public long xdel(final String key, final StreamEntryID entryId) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xdel(key, new StreamEntryID[]{entryId});
            }
        });
    }

    public long xtrimWithMaxlen(final String key, final long maxlen) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xtrim(key, maxlen, false);
            }
        });
    }

    public long xtrimWithMaxlen(final String key, final boolean approx, final long maxlen) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xtrim(key, maxlen, approx);
            }
        });
    }

    public String xgroupcreate(final String key, final String group, final StreamEntryID entryId) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.xgroupCreate(key, group, entryId, false);
            }
        });
    }

    public String xgroupcreate(final String key, final String group, final StreamEntryID entryId, final boolean mkstream) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.xgroupCreate(key, group, entryId, mkstream);
            }
        });
    }

    public String xgroupsetid(final String key, final String group, final StreamEntryID entryId) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                return jedis.xgroupSetID(key, group, entryId);
            }
        });
    }

    public long xgroupdestroy(final String key, final String group) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xgroupDestroy(key, group);
            }
        });
    }

    public long xgroupdelconsumer(final String key, final String group, final String consumer) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xgroupDelConsumer(key, group, consumer);
            }
        });
    }

    public StreamInfo xinfostream(final String key) {
        return (StreamInfo)this.doRedisWithExceptionHandler(new IRedisOperation<StreamInfo>() {
            public StreamInfo exec(Jedis jedis) {
                return jedis.xinfoStream(key);
            }
        });
    }

    public List<StreamGroupInfo> xinfogroups(final String key) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamGroupInfo>>() {
            public List<StreamGroupInfo> exec(Jedis jedis) {
                return jedis.xinfoGroup(key);
            }
        });
    }

    public List<StreamConsumersInfo> xinfoconsumers(final String key, final String group) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamConsumersInfo>>() {
            public List<StreamConsumersInfo> exec(Jedis jedis) {
                return jedis.xinfoConsumers(key, group);
            }
        });
    }

    public List<StreamPendingEntry> xpending(final String key, final String groupname, final StreamEntryID start, final StreamEntryID end, final int count, final String consumername) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamPendingEntry>>() {
            public List<StreamPendingEntry> exec(Jedis jedis) {
                return jedis.xpending(key, groupname, start, end, count, consumername);
            }
        });
    }

    public List<StreamPendingEntry> xpending(final String key, final String groupname, final StreamEntryID start, final StreamEntryID end, final int count) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamPendingEntry>>() {
            public List<StreamPendingEntry> exec(Jedis jedis) {
                return jedis.xpending(key, groupname, start, end, count, (String)null);
            }
        });
    }

    public Map<String, List<StreamEntry>> xreadgroup(String group, String consumer, String... params) {
        return this.xreadgroup(group, consumer, 0, (String[])params);
    }

    public Map<String, List<StreamEntry>> xreadgroup(String group, String consumer, Map<String, StreamEntryID> pairs) {
        return this.xreadgroup(group, consumer, 0, (Map)pairs);
    }

    public Map<String, List<StreamEntry>> xreadgroup(String group, String consumer, int count, String... params) {
        if (params != null && params.length >= 2 && params.length % 2 != 1) {
            int l = params.length / 2;
            Map<String, StreamEntryID> map = new HashMap(l);

            for(int i = 0; i < l; ++i) {
                map.put(params[i], params[i + l].equals(">") ? StreamEntryID.UNRECEIVED_ENTRY : (params[i + l].contains("-") ? new StreamEntryID(params[i + l]) : new StreamEntryID(Long.parseLong(params[i + l]), 0L)));
            }

            return this.xreadgroup(group, consumer, count, (Map)map);
        } else {
            return new HashMap();
        }
    }

    public Map<String, List<StreamEntry>> xreadgroup(final String group, final String consumer, final int count, Map<String, StreamEntryID> pairs) {
        final Map.Entry<String, StreamEntryID>[] params = (Map.Entry[])pairs.entrySet().toArray(new Map.Entry[0]);
        List<Map.Entry<String, List<StreamEntry>>> reply = (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<Map.Entry<String, List<StreamEntry>>>>() {
            public List<Map.Entry<String, List<StreamEntry>>> exec(Jedis jedis) {
                return jedis.xreadGroup(group, consumer, count, -1L, false, params);
            }
        });
        Map<String, List<StreamEntry>> ret = new HashMap();
        if (reply != null) {
            Iterator var8 = reply.iterator();

            while(var8.hasNext()) {
                Map.Entry<String, List<StreamEntry>> entry = (Map.Entry)var8.next();
                ret.put(entry.getKey(), entry.getValue());
            }
        }

        return ret;
    }

    public Map.Entry<String, StreamEntry> xreadgroupBlock(final String group, final String consumer, final long block, String... keys) {
        if (keys != null && keys.length != 0) {
            final Map.Entry<String, StreamEntryID>[] param = new Map.Entry[keys.length];
            int i = 0;
            String[] var8 = keys;
            int var9 = keys.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                String key = var8[var10];
                param[i++] = new AbstractMap.SimpleEntry(key, StreamEntryID.UNRECEIVED_ENTRY);
            }

            try {
                List<Map.Entry<String, List<StreamEntry>>> reply = (List)this.doRedis(new IRedisOperation<List<Map.Entry<String, List<StreamEntry>>>>() {
                    public List<Map.Entry<String, List<StreamEntry>>> exec(Jedis jedis) {
                        return jedis.xreadGroup(group, consumer, 0, block, false, param);
                    }
                });
                return reply != null && !reply.isEmpty() ? new AbstractMap.SimpleEntry(((Map.Entry)reply.get(0)).getKey(), ((List)((Map.Entry)reply.get(0)).getValue()).get(0)) : null;
            } catch (RedisException var12) {
                Throwable t = var12.getCause();
                if (!(t instanceof SocketTimeoutException)) {
                    this.exceptionHandler.dealException(var12);
                }

                return null;
            }
        } else {
            return null;
        }
    }

    public Map.Entry<String, StreamEntry> xreadgroupBlock(String group, String consumer, String... keys) {
        return this.xreadgroupBlock(group, consumer, 0L, keys);
    }

    public long xack(final String key, final String group, final StreamEntryID... entryIds) {
        return (Long)this.doRedisWithExceptionHandler(new IRedisOperation<Long>() {
            public Long exec(Jedis jedis) {
                return jedis.xack(key, group, entryIds);
            }
        });
    }

    public List<StreamEntry> xclaim(final String key, final String group, final String consumer, final long minIdleTime, final long idleTime, final int retryCount, final StreamEntryID... entryIds) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamEntry>>() {
            public List<StreamEntry> exec(Jedis jedis) {
                return jedis.xclaim(key, group, consumer, minIdleTime, idleTime, retryCount, false, entryIds);
            }
        });
    }

    public List<StreamEntry> xclaimForce(final String key, final String group, final String consumer, final StreamEntryID... entryIds) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamEntry>>() {
            public List<StreamEntry> exec(Jedis jedis) {
                return jedis.xclaim(key, group, consumer, 0L, 0L, 1, true, entryIds);
            }
        });
    }

    public List<StreamEntryID> xclaimJustid(final String key, final String group, final String consumer, final long minIdleTime, final long idleTime, final int retryCount, final StreamEntryID... entryIds) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamEntryID>>() {
            public List<StreamEntryID> exec(Jedis jedis) {
                return (List)RedisStreamTemplate.STREAM_ENTRY_ID_LIST.build(RedisStreamTemplate.this.xclaimJustid(jedis, key, group, consumer, minIdleTime, idleTime, retryCount, false, entryIds));
            }
        });
    }

    public List<StreamEntryID> xclaimForceAndJustid(final String key, final String group, final String consumer, final StreamEntryID... entryIds) {
        return (List)this.doRedisWithExceptionHandler(new IRedisOperation<List<StreamEntryID>>() {
            public List<StreamEntryID> exec(Jedis jedis) {
                return (List)RedisStreamTemplate.STREAM_ENTRY_ID_LIST.build(RedisStreamTemplate.this.xclaimJustid(jedis, key, group, consumer, 0L, 0L, 1, true, entryIds));
            }
        });
    }

    public String setnxex(final String key, final String value, final int time) {
        return (String)this.doRedisWithExceptionHandler(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                SetParams setParams = new SetParams();
                setParams.nx();
                setParams.ex(time);
                return jedis.set(key, value, setParams);
            }
        });
    }

    protected Object xclaimJustid(Jedis jedis, String key, String group, String consumer, long minIdleTime, long newIdleTime, int retries, boolean force, StreamEntryID... ids) {
        ArrayList<byte[]> arguments = new ArrayList(10 + ids.length);
        arguments.add(SafeEncoder.encode(key));
        arguments.add(SafeEncoder.encode(group));
        arguments.add(SafeEncoder.encode(consumer));
        arguments.add(SafeEncoder.encode(String.valueOf(minIdleTime)));
        StreamEntryID[] var13 = ids;
        int var14 = ids.length;

        for(int var15 = 0; var15 < var14; ++var15) {
            StreamEntryID id = var13[var15];
            arguments.add(SafeEncoder.encode(id.toString()));
        }

        if (newIdleTime > 0L) {
            arguments.add(Keyword.IDLE.raw);
            arguments.add(SafeEncoder.encode(String.valueOf(newIdleTime)));
        }

        if (retries > 0) {
            arguments.add(Keyword.RETRYCOUNT.raw);
            arguments.add(SafeEncoder.encode(String.valueOf(retries)));
        }

        if (force) {
            arguments.add(Keyword.FORCE.raw);
        }

        arguments.add(SafeEncoder.encode("JUSTID"));
        return jedis.sendCommand(Command.XCLAIM, (byte[][])arguments.toArray(new byte[arguments.size()][]));
    }
}