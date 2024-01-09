package com.xw.lock.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuxiaowei
 * @description 基于zookeeper的curator客户端实现分布式锁
 *  Curator是zookeeper的一个客户端 <a href="https://curator.apache.org/">官网</a>
 *  Apache Curator is a Java/JVM client library for Apache ZooKeeper, a distributed coordination service.
 *  It includes a highlevel API framework and utilities to make using Apache ZooKeeper much easier and more reliable.
 *  It also includes recipes for common use cases and extensions such as service discovery and a Java 8 asynchronous DSL.
 *
 *  引入Curator客户端 <a href="https://mvnrepository.com/artifact/org.apache.curator/curator-recipes"></a>
 *  Curator已经实现了分布式锁的方法
 *  直接调用即可
 *  <a href="https://curator.apache.org/getting-started.html">Getting Started</a>
 * @date 2023/2/17
 */
public class ZookeeperCuratorLock implements AutoCloseable, Watcher {
    private final static Logger logger = LoggerFactory.getLogger(ZookeeperCuratorLock.class);

    @Override
    public void close() throws Exception {

    }

    @Override
    public void process(WatchedEvent watchedEvent) {

    }
}
