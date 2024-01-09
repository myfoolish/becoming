package com.xw.util.client.inter;

import java.util.List;
import java.util.Map;
import redis.clients.jedis.StreamConsumersInfo;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.StreamGroupInfo;
import redis.clients.jedis.StreamInfo;
import redis.clients.jedis.StreamPendingEntry;

public interface IStreamCommands extends IStreamDataCommands {
    Map<String, List<StreamEntry>> xread(String... var1);

    Map<String, List<StreamEntry>> xread(Map<String, StreamEntryID> var1);

    Map<String, List<StreamEntry>> xread(int var1, String... var2);

    Map<String, List<StreamEntry>> xread(int var1, Map<String, StreamEntryID> var2);

    Map.Entry<String, StreamEntry> xreadBlock(long var1, String... var3);

    Map.Entry<String, StreamEntry> xreadBlock(String... var1);

    String xgroupcreate(String var1, String var2, StreamEntryID var3);

    String xgroupcreate(String var1, String var2, StreamEntryID var3, boolean var4);

    String xgroupsetid(String var1, String var2, StreamEntryID var3);

    long xgroupdestroy(String var1, String var2);

    long xgroupdelconsumer(String var1, String var2, String var3);

    StreamInfo xinfostream(String var1);

    List<StreamGroupInfo> xinfogroups(String var1);

    List<StreamConsumersInfo> xinfoconsumers(String var1, String var2);

    List<StreamPendingEntry> xpending(String var1, String var2, StreamEntryID var3, StreamEntryID var4, int var5, String var6);

    List<StreamPendingEntry> xpending(String var1, String var2, StreamEntryID var3, StreamEntryID var4, int var5);

    Map<String, List<StreamEntry>> xreadgroup(String var1, String var2, String... var3);

    Map<String, List<StreamEntry>> xreadgroup(String var1, String var2, Map<String, StreamEntryID> var3);

    Map<String, List<StreamEntry>> xreadgroup(String var1, String var2, int var3, String... var4);

    Map<String, List<StreamEntry>> xreadgroup(String var1, String var2, int var3, Map<String, StreamEntryID> var4);

    Map.Entry<String, StreamEntry> xreadgroupBlock(String var1, String var2, long var3, String... var5);

    Map.Entry<String, StreamEntry> xreadgroupBlock(String var1, String var2, String... var3);

    long xack(String var1, String var2, StreamEntryID... var3);

    List<StreamEntry> xclaim(String var1, String var2, String var3, long var4, long var6, int var8, StreamEntryID... var9);

    List<StreamEntry> xclaimForce(String var1, String var2, String var3, StreamEntryID... var4);

    List<StreamEntryID> xclaimJustid(String var1, String var2, String var3, long var4, long var6, int var8, StreamEntryID... var9);

    List<StreamEntryID> xclaimForceAndJustid(String var1, String var2, String var3, StreamEntryID... var4);
}
