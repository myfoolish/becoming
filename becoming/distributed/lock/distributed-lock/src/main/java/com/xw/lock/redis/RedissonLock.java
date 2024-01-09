package com.xw.lock.redis;

/**
 * @author liuxiaowei
 * @description 基于Redisson实现分布式锁
 *  Redisson是redis的一个客户端 <a href="https://redisson.org/">官网</a>
 *  实现的redis的基本功能之外，还实现了java concurrency并发包下的内容
 *  引入Redisson客户端 <a href="https://mvnrepository.com/artifact/org.redisson/redisson"></a>
 *  进行Redisson与redis配置
 *  直接调用即可
 *      1、通过Java API方式引入Redisson
 *      <a href="https://mvnrepository.com/artifact/org.redisson/redisson">...</a>
 *      <a href="https://github.com/redisson/redisson#quick-start">quick-start</a>
 *      2、Spring项目引入Redisson（通过配置spring.xml）
 *          本项目通过在启动类中添加注解@ImportResource(locations = "classpath:redisson.xml")引入spring.xml配置文件
 *          然后在项目组注入 @Autowired private RedissonClient redisson; 即可
 *      3、SpringBoot项目引入Redisson（引入redisson的starter）
 *      <a href="https://mvnrepository.com/artifact/org.redisson/redisson-spring-boot-starter">...</a>
 *      <a href="https://github.com/redisson/redisson/tree/master/redisson-spring-boot-starter">...</a>
 *          直接在项目组注入 @Autowired private RedissonClient redisson; 即可
 * @date 2023/7/27
 */
public class RedissonLock {
}
