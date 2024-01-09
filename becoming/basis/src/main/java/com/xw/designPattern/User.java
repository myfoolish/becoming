package com.xw.designPattern;

import lombok.Data;
import lombok.ToString;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
@Data
@ToString
public class User implements Cloneable {
    private String username;
    private int age;

    public User() {
        System.out.println("User对象被创建");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        User user = new User();
        user.setUsername(this.username);
        user.setAge(this.age);
        return user;
    }
}
