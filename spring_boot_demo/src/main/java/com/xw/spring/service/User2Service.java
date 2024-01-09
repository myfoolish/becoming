package com.xw.spring.service;

import com.xw.spring.entity.User2;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/8/31
 */
public interface User2Service {

    void addRequired(User2 record);

    void addRequiredException(User2 record);

    void addRequiresNew(User2 record);

    void addRequiresNewException(User2 record);

    User2 selectByPrimaryKey(Integer id);

    //其他方法省略...
}
