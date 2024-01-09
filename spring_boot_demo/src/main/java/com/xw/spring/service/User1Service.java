package com.xw.spring.service;

import com.xw.spring.entity.User1;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/8/31
 */
public interface User1Service {

    void addRequired(User1 record);

    void addRequiresNew(User1 record);

    User1 selectByPrimaryKey(Integer id);

    //其他方法省略...
}
