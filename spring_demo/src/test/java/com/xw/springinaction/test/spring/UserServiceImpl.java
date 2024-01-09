package com.xw.springinaction.test.spring;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/31
 */
public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();
    @Override
    public void getUser() {
        userDao.getUser();
    }
}
