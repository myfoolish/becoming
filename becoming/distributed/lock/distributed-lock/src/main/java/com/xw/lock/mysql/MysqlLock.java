package com.xw.lock.mysql;

/**
 * @author liuxiaowei
 * @description 基于数据库实现分布式锁🔒
 *  多个进程、多个线程访问共同组件
 *  通过select ... for update访问同一条数据
 *
 *  优点：简单方便，易于理解和操作
 *  缺点：并发量大时，对数据库压力较大
 * @date 2023/7/6
 */
public class MysqlLock {
}
