package com.xw.util.client.util;

import com.xw.util.client.inter.IRedisPool2;
import com.xw.util.client.inter.ISubscribeCallback;
import com.xw.util.client.pool.RedisSentinelPool;
import com.xw.util.client.pool.RedisSentinelPool2;
import com.xw.util.client.pool.RedisShardedPool;
import com.xw.util.client.pool.RedisSinglePool;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    public RedisUtil() {
    }

    public static IRedisPool2<Jedis> getPool(String infoStr, JedisPoolConfig poolConfig) {
        return getPool(infoStr, poolConfig, (ISubscribeCallback)null);
    }

    public static IRedisPool2<Jedis> getPool(String infoStr, JedisPoolConfig poolConfig, ISubscribeCallback failoverCallback) {
        if (infoStr == null) {
            return null;
        } else {
            String[] infos = infoStr.split("/");
            String password = null;
            if (infos.length > 1) {
                password = infos[1];
            }

            String info = infos[0];
            int idxAt = info.indexOf("@");
            Object pool;
            if (idxAt > 0) {
                pool = getSentinelPool(info, idxAt, password, poolConfig, failoverCallback);
            } else {
                pool = getSinglePool(info, password, poolConfig);
            }

            return (IRedisPool2)pool;
        }
    }

    public static RedisShardedPool getShardedPool(String infoStr, JedisPoolConfig poolConfig) {
        if (infoStr == null) {
            return null;
        } else {
            String[] infos = infoStr.split("/");
            String password = null;
            if (infos.length > 1) {
                password = infos[1];
            }

            String info = infos[0];
            Set<HostAndPort> haps = new HashSet();
            String[] var6 = info.split(",");
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String hap = var6[var8];

                try {
                    haps.add(HostAndPort.parseString(hap));
                } catch (IllegalArgumentException var11) {
                }
            }

            return new RedisShardedPool(haps, password, poolConfig);
        }
    }

    public static JedisCluster getCluster(String infoStr, JedisPoolConfig poolConfig) {
        if (infoStr == null) {
            return null;
        } else if (!infoStr.startsWith("cluster://")) {
            throw new IllegalArgumentException("信息格式不正确，如果使用了Cluster集群，请加上cluster://头用以确认");
        } else {
            String[] infos = infoStr.substring(10).split("/");
            String password = null;
            if (infos.length > 1) {
                password = infos[1];
            }

            String info = infos[0];
            Set<HostAndPort> nodes = new HashSet();
            String[] var7 = info.split(",");
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                String node = var7[var9];
                HostAndPort hap = preprosAddr(node);
                if (hap != null) {
                    nodes.add(hap);
                }
            }

            if (nodes.isEmpty()) {
                return null;
            } else {
                return new JedisCluster(nodes, 2000, 2000, 5, password, poolConfig);
            }
        }
    }

    private static IRedisPool2<Jedis> getSentinelPool(String info, int idxAt, String password, JedisPoolConfig poolConfig, ISubscribeCallback failoverCallback, String... channels) {
        String masterName = info.substring(0, idxAt);
        Set<String> sentinels = getHaps(info.substring(idxAt + 1));
        if (sentinels.isEmpty()) {
            return null;
        } else {
            Object pool;
            if (failoverCallback == null) {
                pool = new RedisSentinelPool(sentinels, masterName, password, poolConfig);
            } else {
                pool = new RedisSentinelPool2(masterName, sentinels, poolConfig, 2000, 2000, password, (String)null, failoverCallback, channels);
            }

            return (IRedisPool2)pool;
        }
    }

    public static Set<String> getHaps(String info) {
        Set<String> getHaps = new HashSet();
        String[] var3 = info.split(",");
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String sentinel = var3[var5];
            HostAndPort hap = preprosAddr(sentinel);
            if (hap != null) {
                getHaps.add(hap.toString());
            }
        }

        return getHaps;
    }

    private static RedisSinglePool getSinglePool(String info, String password, JedisPoolConfig poolConfig) {
        RedisSinglePool pool = null;
        HostAndPort hap = preprosAddr(info);
        if (hap == null) {
            return null;
        } else {
            if (password != null) {
                pool = new RedisSinglePool(hap.getHost(), hap.getPort(), password, poolConfig);
            } else {
                pool = new RedisSinglePool(hap.getHost(), hap.getPort(), poolConfig);
            }

            return pool;
        }
    }

    public static HostAndPort preprosAddr(String address) {
        if (address == null) {
            return null;
        } else if (!address.contains(":")) {
            return new HostAndPort(address, 26379);
        } else {
            String[] parts = address.split(":");
            if (parts.length != 2) {
                return null;
            } else {
                int i;
                for(i = 0; i < 1; ++i) {
                    if (parts[i].startsWith("{sys/")) {
                        parts[i] = getSysEnv(parts[i]);
                    }
                }

                try {
                    i = Integer.parseInt(parts[1]);
                    return new HostAndPort(parts[0], i);
                } catch (NumberFormatException var3) {
                    return null;
                }
            }
        }
    }

    private static String getSysEnv(String confStr) {
        int idx = confStr.indexOf("}");
        if (idx < 6) {
            return confStr;
        } else {
            String envKey = confStr.substring(5, idx);
            String value = System.getenv(envKey);
            if (value == null) {
                value = confStr;
            }

            return value;
        }
    }

    public static String[] convertMapToArray(Map<String, String> map, boolean pair) {
        String[] ret = new String[map.size() * 2];
        if (pair) {
            int i = 0;

            Map.Entry entry;
            for(Iterator var4 = map.entrySet().iterator(); var4.hasNext(); ret[i++] = (String)map.get(entry.getValue())) {
                entry = (Map.Entry)var4.next();
                ret[i++] = (String)map.get(entry.getKey());
            }
        } else {
            String[] keys = (String[])map.keySet().toArray(new String[0]);
            String[] values = (String[])map.values().toArray(new String[0]);
            System.arraycopy(keys, 0, ret, 0, keys.length);
            System.arraycopy(values, 0, ret, keys.length, values.length);
        }

        return ret;
    }

    public static Map<String, String> convertArrayToMap(String[] args, boolean pair) {
        if (args.length % 2 == 1) {
            return null;
        } else {
            Map<String, String> ret = new HashMap();
            int l = args.length;
            if (!pair) {
                l /= 2;
            }

            for(int i = 0; i < l; ++i) {
                if (pair) {
                    String var10001 = args[i];
                    ++i;
                    ret.put(var10001, args[i]);
                } else {
                    ret.put(args[i], args[l + i]);
                }
            }

            return ret;
        }
    }
}
