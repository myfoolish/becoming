package com.xw.util;

import com.xw.util.client.exception.RedisException;
import com.xw.util.client.inter.IRedisOperation;
import com.xw.util.client.template.RedisPoolTemplate;
import com.xw.util.client.template.RedisShardedPoolTemplate;
import com.xw.util.client.util.RedisUtil;
import redis.clients.jedis.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RedisClientUtil {
    private RedisPoolTemplate redisPoolTemplate;
    private RedisShardedPoolTemplate RedisShardedPoolTemplate;

    public RedisClientUtil(RedisPoolTemplate redisPoolTemplate, RedisShardedPoolTemplate RedisShardedPoolTemplate) {
        this.redisPoolTemplate = redisPoolTemplate;
        this.RedisShardedPoolTemplate = RedisShardedPoolTemplate;
    }

    public String loadLuaSha(RedisPoolTemplate poolTemplate, String path, String luaScriptName) throws Exception {
        if (!StringUtil.isNullOrBlank(path) && !StringUtil.isNullOrBlank(luaScriptName)) {
            String sha = this.getValueFromStream(poolTemplate, luaScriptName, luaScriptName);
            return StringUtil.isNullOrBlank(sha) ? (String) poolTemplate.doRedis(new IRedisOperation<String>() {
                public String exec(Jedis jedis) {
                    String sha = null;
                    InputStream luaStream = null;
                    String luaScript = null;

                    try {
                        if (StringUtil.isNullOrBlank(path) || StringUtil.isNullOrBlank(luaScriptName)) {
                            throw new Exception("脚本目录和名称未传入！");
                        }

                        luaStream = RedisUtil.class.getResourceAsStream(path + luaScriptName);
                        luaScript = (String) (new BufferedReader(new InputStreamReader(luaStream))).lines().collect(Collectors.joining("\n"));
                    } catch (Exception var14) {
                        var14.printStackTrace();
                    } finally {
                        if (luaStream != null) {
                            try {
                                luaStream.close();
                            } catch (IOException var13) {
                                var13.printStackTrace();
                            }
                        }

                    }

                    if (StringUtil.isNotNull(luaScript)) {
                        poolTemplate.xaddWithMaxlen(luaScriptName, true, 100L, StreamEntryID.NEW_ENTRY, new String[]{luaScriptName, sha = jedis.scriptLoad(luaScript)});
                    }

                    return sha;
                }
            }) : sha;
        } else {
            throw new Exception("脚本目录和名称未传入！");
        }
    }

    public String getValueFromStream(RedisPoolTemplate poolTemplate, String streamName, String mapKey) throws Exception {
        if (!StringUtil.isNullOrBlank(streamName) && !StringUtil.isNullOrBlank(mapKey)) {
            List<StreamEntry> streamList = poolTemplate.xrevrange(streamName, (StreamEntryID) null, (StreamEntryID) null, 1);
            if (streamList != null && streamList.size() > 0) {
                StreamEntry streamEntry = (StreamEntry) streamList.get(0);
                Map<String, String> shaMap = streamEntry.getFields();
                return shaMap != null && !shaMap.isEmpty() ? (String) shaMap.get(mapKey) : null;
            } else {
                return null;
            }
        } else {
            throw new Exception("getValueFromStream方法缺失参数！");
        }
    }

    public ArrayList<Object> doRedis4Evalsha(RedisPoolTemplate poolTemplate, String shavalue, List<String> keys, List<String> args) throws Exception {
        if (StringUtil.isNullOrBlank(shavalue)) {
            throw new Exception("shavalue缺失，无法执行！");
        } else {
            ArrayList<Object> result = (ArrayList) poolTemplate.doRedis(new IRedisOperation<ArrayList<Object>>() {
                public ArrayList<Object> exec(Jedis jedis) {
                    ArrayList result = null;

                    try {
                        result = (ArrayList) jedis.evalsha(shavalue, keys, args);
                        return result;
                    } catch (Exception var4) {
                        var4.printStackTrace();
                        throw var4;
                    }
                }
            });
            return result;
        }
    }

    public String getLuaScript(String path, String luaScriptName) throws Exception {
        String luaScript = null;
        InputStream luaStream = null;

        try {
            if (StringUtil.isNullOrBlank(path) || StringUtil.isNullOrBlank(luaScriptName)) {
                throw new Exception("脚本目录和名称未传入！");
            }

            luaStream = RedisUtil.class.getResourceAsStream("/lua/" + luaScriptName);
            luaScript = (String) (new BufferedReader(new InputStreamReader(luaStream))).lines().collect(Collectors.joining("\n"));
        } catch (Exception var9) {
            var9.printStackTrace();
            throw new Exception("获取" + luaScriptName + "错误！");
        } finally {
            if (luaStream != null) {
                luaStream.close();
            }

        }

        return luaScript;
    }

    public ArrayList<Object> doRedis4Eval(RedisPoolTemplate poolTemplate, String luaScript, List<String> keys, List<String> args) throws Exception {
        ArrayList<Object> result = (ArrayList) poolTemplate.doRedis(new IRedisOperation<ArrayList<Object>>() {
            public ArrayList<Object> exec(Jedis jedis) {
                ArrayList result = null;

                try {
                    result = (ArrayList) jedis.eval(luaScript, keys, args);
                    return result;
                } catch (Exception var4) {
                    var4.printStackTrace();
                    throw var4;
                }
            }
        });
        return result;
    }

    public RedisPoolTemplate getRedisPoolTemplate() {
        return this.redisPoolTemplate;
    }

    public void setRedisPoolTemplate(RedisPoolTemplate redisPoolTemplate) {
        this.redisPoolTemplate = redisPoolTemplate;
    }

    public RedisShardedPoolTemplate getRedisShardedPoolTemplate() {
        return this.RedisShardedPoolTemplate;
    }

    public void setRedisShardedPoolTemplate(RedisShardedPoolTemplate RedisShardedPoolTemplate) {
        this.RedisShardedPoolTemplate = RedisShardedPoolTemplate;
    }

    private String loadLua(RedisPoolTemplate poolTemplate, final String shaKey, final String script) throws RedisException {
        String sha = null;
        List<StreamEntry> streamList = poolTemplate.xrevrange(shaKey, (StreamEntryID) null, (StreamEntryID) null, 1);
        if (streamList != null && streamList.size() > 0) {
            StreamEntry streamEntry = (StreamEntry) streamList.get(0);
            Map<String, String> shaMap = streamEntry.getFields();
            if (shaMap != null) {
                sha = (String) shaMap.get(shaKey);
            }
        }

        return StringUtil.isNullOrBlank(sha) ? (String) poolTemplate.doRedis(new IRedisOperation<String>() {
            public String exec(Jedis jedis) {
                String sha;
                if (jedis.exists(shaKey) && jedis.scriptExists(sha = jedis.get(shaKey))) {
                    return sha;
                } else {
                    poolTemplate.xaddWithMaxlen(shaKey, true, 100L, StreamEntryID.NEW_ENTRY, new String[]{shaKey, sha = jedis.scriptLoad(script)});
                    return sha;
                }
            }
        }) : sha;
    }

    private String loadLua4Normal(RedisPoolTemplate poolTemplate, String path, String luaScriptName) throws Exception {
        String shavalue = null;
        String luaScript = null;
        InputStream luaStream = null;

        try {
            if (StringUtil.isNullOrBlank(path) || StringUtil.isNullOrBlank(luaScriptName)) {
                throw new Exception("脚本目录和名称未传入！");
            }

            luaStream = RedisUtil.class.getResourceAsStream(path + luaScriptName);
            luaScript = (String) (new BufferedReader(new InputStreamReader(luaStream))).lines().collect(Collectors.joining("\n"));
            shavalue = this.loadLua(poolTemplate, luaScriptName, luaScript);
        } catch (Exception var11) {
            var11.printStackTrace();
            throw new Exception("获取" + luaScriptName + "错误！");
        } finally {
            if (luaStream != null) {
                luaStream.close();
            }

        }

        return shavalue;
    }

    public static List<String> getKeysForSingle(RedisPoolTemplate template, String keyPattern) {
        final String fKeyPattern = keyPattern;

        Object response;
        try {
            response = (List) template.doRedis(new IRedisOperation<List<String>>() {
                public List<String> exec(Jedis jedis) {
                    List<String> result = new ArrayList();
                    String cursor = "0";
                    ScanParams params = new ScanParams();
                    params.match(fKeyPattern).count(5);

                    do {
                        ScanResult<String> scanResult = jedis.scan(cursor, params);
                        cursor = scanResult.getCursor();
                        result.addAll(scanResult.getResult());
                    } while (!"0".equals(cursor));

                    return result;
                }
            });
        } catch (RedisException var5) {
            var5.printStackTrace();
            response = new ArrayList();
        }

        return (List) response;
    }

    public static List<String> getKeysForMultiple(RedisShardedPoolTemplate template, String keyPattern) {
        List<String> response = new ArrayList();
        template.getAllShards().parallelStream().forEach((shard) -> {
            try {
                shard.doRedis(new IRedisOperation<Void>() {
                    public Void exec(Jedis jedis) {
                        String cursor = "0";
                        ScanParams params = new ScanParams();
                        params.match(keyPattern).count(5);

                        do {
                            ScanResult scanResult = jedis.scan(cursor, params);
                            cursor = scanResult.getCursor();
                            response.addAll(scanResult.getResult());
                        } while (!"0".equals(cursor));

                        return null;
                    }
                });
            } catch (RedisException var4) {
                var4.printStackTrace();
                response.clear();
            }

        });
        return response;
    }
}
