package com.xw.spring.aop.service;

import com.xw.spring.aop.dao.UserDao;
import org.springframework.stereotype.Service;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/5
 */
@Service
public class UserService {
    private UserDao userDao;

    public void insert() {
        // 为了测试异常通知加的代码
//        if (1 == 1) {
//            throw new RuntimeException();
//        }
        try {
            Thread.sleep(3300);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        userDao.insert();
    }

    public String generateRandomPassword(String type, Integer length) {
        System.out.println("按" + type + "方式生成" + length + "位随机密码");
        return "xwor";
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
