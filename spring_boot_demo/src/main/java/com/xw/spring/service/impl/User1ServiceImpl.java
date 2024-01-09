package com.xw.spring.service.impl;

import com.xw.spring.entity.User1;
import com.xw.mapper.User1Mapper;
import com.xw.spring.service.User1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/8/31
 */
@Service
public class User1ServiceImpl implements User1Service {

    @Autowired
    private User1Mapper user1Mapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User1 record) {
        user1Mapper.insert(record);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addRequiresNew(User1 record) {
        user1Mapper.insert(record);
    }

    @Override
    public User1 selectByPrimaryKey(Integer id) {
        return user1Mapper.selectByPrimaryKey(id);
    }
}
