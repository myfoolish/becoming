package com.xw.spring.ioc.autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author liuxiaowei
 * @description
 *      -@PostConstruct 描述方法 相当于xml中init-method配置的注解版本
 *      -@PreDestroy 描述方法 相当于xml中destroy-method配置的注解版本
 *      -@Scope 相当于xml中设置对象的scope属性的注解版本
 *      -@Value 为属性注入静态数据
 * @date 2023/12/4
 */
//@Service
@Scope(scopeName = "prototype") // 设置单例/多例 相当于xml中设置对象的scope属性的注解版本
public class UserService {

    @Value("${ioc.metaData}")
    private String metaData;

    @PostConstruct  // 相当于xml中init-method配置的注解版本
    public void init() {
        System.out.println("初始化init" + metaData);
    }

    @PreDestroy // 相当于xml中destroy-method配置的注解版本
    public void destroy() {
        System.out.println("初始化init");
    }

    /**
     * @Autowired   按照类型装配（不推荐）
     *
     * @Resource
     *  1、若设置name属性，eg: @Resource(name = "user1Dao")，则按name在ioc容器将bean注入
     *  2、若未设置name属性
     *      以属性名作为bean name在ioc容器中匹配bean，若有匹配则注入
     *      若按属性名未匹配，则按照类型进行匹配，此时同@Autowired（若存在冲突，可加@Primaey解决冲突）
     * @Primaey 按照类型装配时出现多个相同类型对象时，使用此注解对象优先被注入
     * 使用建议：在使用@Resource对象时推荐设置name属性或保证属性名与bean名称一致
     */
//    @Autowired
    @Resource(name = "user1Dao")
    private UserDao user1Dao;
}

interface UserDao {

}

@Repository
class User1Dao implements UserDao {
    public User1Dao() {
        System.out.println("这里是user1Dao");
    }
}

@Repository
class User2Dao implements UserDao {
    public User2Dao() {
        System.out.println("这里是user2Dao");
    }
}