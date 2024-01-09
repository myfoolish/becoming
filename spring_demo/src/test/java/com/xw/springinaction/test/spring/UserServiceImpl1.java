package com.xw.springinaction.test.spring;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/31
 */
public class UserServiceImpl1 implements UserService {

    //在Service层的实现类(UserServiceImpl)增加一个Set()方法
    //利用set动态实现值的注入！
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    @Override
    public void getUser() {
        userDao.getUser();
    }
}
