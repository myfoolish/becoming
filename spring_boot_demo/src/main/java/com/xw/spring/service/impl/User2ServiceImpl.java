package com.xw.spring.service.impl;

import com.xw.spring.entity.User2;
import com.xw.mapper.User2Mapper;
import com.xw.spring.service.User2Service;
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
public class User2ServiceImpl implements User2Service {

    @Autowired
    private User2Mapper user2Mapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequired(User2 record) {
        user2Mapper.insert(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void addRequiredException(User2 record) {
        user2Mapper.insert(record);
        throw new RuntimeException();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void addRequiresNew(User2 record) {
        user2Mapper.insert(record);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequiresNewException(User2 record) {
        user2Mapper.insert(record);
        throw new RuntimeException();
    }

    @Override
    public User2 selectByPrimaryKey(Integer id) {
        return user2Mapper.selectByPrimaryKey(id);
    }
}
