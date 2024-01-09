package com.xw.designPattern.creation.prototype;

import com.xw.designPattern.User;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
public class MyPrototype {

    private final Map<String, User> userCache = new HashMap<>();

    public User getUser(String username) throws CloneNotSupportedException {
        User user = null;
        if (userCache.containsKey(username)) {
            user = getUserFromDb(username);
        } else {
            user = userCache.get(username);
            user = (User) user.clone();
        }
        return user;
    }

    private User getUserFromDb(String username) throws CloneNotSupportedException {
        System.out.println("从数据库中查询" + username);
        User user = new User();
        user.setUsername(username);
        user.setAge(25);
        user = (User) user.clone();
        userCache.put(username, user);
        return user;
    }
}
