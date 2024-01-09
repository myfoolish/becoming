package com.xw.service;

import com.xw.mapper.at.mapper1.UserAT1Mapper;
import com.xw.mapper.at.mapper2.UserAT2Mapper;
import com.xw.mapper.xa.mapper1.UserXA1Mapper;
import com.xw.mapper.xa.mapper2.UserXA2Mapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/22
 */
@Service
public class UserService {

    @Autowired
    private UserXA1Mapper userXA1Mapper;
    @Autowired
    private UserXA2Mapper userXA2Mapper;

//    @Transactional

    /**
     * XA模式
     */
    @GlobalTransactional
    public void becomingXA() {
        userXA1Mapper.updateUser1(8000);
        int i = 1 / 0;
        userXA2Mapper.updateUser2(100000);
    }

    @Autowired
    private UserAT1Mapper userAT1Mapper;
    @Autowired
    private UserAT2Mapper userAT2Mapper;

    /**
     * AT模式
     */
    @GlobalTransactional
    public void becomingAT() {
        userAT1Mapper.updateUser1(8000);
        int i = 1 / 0;
        userAT2Mapper.updateUser2(100000);
    }
}
