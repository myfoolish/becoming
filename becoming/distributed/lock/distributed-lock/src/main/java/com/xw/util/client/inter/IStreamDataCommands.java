package com.xw.util.client.inter;

import java.util.List;
import java.util.Map;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;

public interface IStreamDataCommands {
    StreamEntryID xadd(String var1, StreamEntryID var2, Map<String, String> var3);

    StreamEntryID xadd(String var1, StreamEntryID var2, String... var3);

    StreamEntryID xaddWithMaxlen(String var1, boolean var2, long var3, StreamEntryID var5, Map<String, String> var6);

    StreamEntryID xaddWithMaxlen(String var1, boolean var2, long var3, StreamEntryID var5, String... var6);

    StreamEntryID xaddDefault(String var1, String... var2);

    StreamEntryID xaddDefault(String var1, Map<String, String> var2);

    StreamEntryID xaddWithMaxlen(String var1, long var2, StreamEntryID var4, Map<String, String> var5);

    StreamEntryID xaddWithMaxlen(String var1, long var2, StreamEntryID var4, String... var5);

    long xlen(String var1);

    List<StreamEntry> xrange(String var1, StreamEntryID var2, StreamEntryID var3, int var4);

    List<StreamEntry> xrevrange(String var1, StreamEntryID var2, StreamEntryID var3, int var4);

    long xdel(String var1, StreamEntryID var2);

    long xtrimWithMaxlen(String var1, long var2);

    long xtrimWithMaxlen(String var1, boolean var2, long var3);
}
