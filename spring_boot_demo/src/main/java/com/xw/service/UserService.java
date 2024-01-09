package com.xw.service;

import com.xw.entity.User;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/5/20
 */
public interface UserService extends BaseService<User>{

    User findByName(String name);
}
