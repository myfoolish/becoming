package com.xw.lock.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author liuxiaowei
 * @description 基于zookeeper的瞬时节点实现分布式锁
 *  利用zookeeper的瞬时有序性节点的特性
 *  多线程并发创建瞬时节点时，得到有序的序列
 *  序号最小的线程获得锁
 *  其他线程则监听自己序号的前一个序号
 *  前一个线程执行完成删除自己序号的节点
 *  下一个序号的线程得到通知，继续执行
 *  以此类推
 *  创建节点时已经确定了线程的执行顺序
 *
 *  Watcher：观察器
 * @date 2023/2/17
 */
public class ZookeeperLock implements AutoCloseable, Watcher {
    private final static Logger logger = LoggerFactory.getLogger(ZookeeperLock.class);
    private ZooKeeper zooKeeper;
    private String zookeeperNode;

    public ZookeeperLock() throws IOException {
        this.zooKeeper = new ZooKeeper("192.168.31.197:2181", 10000, this);
    }

    /**
     *
     * @param businessCode  不同业务 锁🔒 区分
     * @return
     */
    public boolean getLock(String businessCode) {
        // 添加观察器zooKeeper.getData()、zooKeeper.getChildren()、zooKeeper.exists()
        try {
            // 创建业务根结点
            Stat stat = zooKeeper.exists("/" + businessCode, false);
            if (stat == null) {
                zooKeeper.create("/" + businessCode, businessCode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            // 创建瞬时有序节点 eg: /order/order_000001
            zookeeperNode = zooKeeper.create("/" + businessCode + "/" + businessCode + "_", businessCode.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            // 获取业务节点下所有的子节点
            List<String> childrenNodes = zooKeeper.getChildren("/" + businessCode, false);
            Collections.sort(childrenNodes);    // 排序
            // 获取序号最小（第一个）子节点
            String firstNode = childrenNodes.get(0);
            // 如果创建的节点是第一个子节点，则获得锁🔒
            if (zookeeperNode.endsWith(firstNode)) {
                return true;
            }
            // 如果创建的节点不是第一个子节点，则监听前一个节点
            String lastNode = firstNode;
            for (String childrenNode : childrenNodes) {
                if (zookeeperNode.endsWith(childrenNode)) {
                    zooKeeper.exists("/" + businessCode + "/" + lastNode, true);
                    break;
                } else {
                    lastNode = childrenNode;
                }
            }
            synchronized (this) {
                wait();
            }
            return true;
        } catch (Exception e) {
//            throw new RuntimeException(e);
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void close() throws Exception {
        zooKeeper.delete(zookeeperNode, -1);
        zooKeeper.close();
//        System.out.println("已经释放了锁🔒");
        logger.info("已经释放了锁🔒");
    }

    @Override
    public void process(WatchedEvent watchedEvent) {
        if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
            synchronized (this) {
                notify();
            }
        }
    }
}
