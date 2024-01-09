package com.xw.springinaction.test.spring;

import org.junit.Test;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/8/28
 */
public class UserTest {

    @Test
    public void user() {
        // 用户实际调用的是业务层，dao层他们不需要接触
        UserService userService = new UserServiceImpl();
        userService.getUser();
    }

    @Test
    public void user1() {
        UserService userService = new UserServiceImpl1();
        ((UserServiceImpl1) userService).setUserDao(new UserDaoImpl());
    }
}
