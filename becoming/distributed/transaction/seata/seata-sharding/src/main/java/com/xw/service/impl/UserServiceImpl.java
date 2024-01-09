package com.xw.service.impl;

import com.xw.entity.User;
import com.xw.mapper.UserMapper;
import com.xw.service.UserService;
import io.seata.core.context.RootContext;
import org.apache.shardingsphere.transaction.annotation.ShardingTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/22
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void become(User user) {
        userMapper.insert(user);
    }

    /**
     * Seata目前不支持分库分表的XA（强一致性）模式
     * 使用注解@ShardingTransactionType(TransactionType.XA)配合@Transactional使用XA模式 解决分布式事务问题
     */
    @Transactional
    @ShardingTransactionType(TransactionType.XA)
    @Override
    public void becomingXA(Integer fromId, Integer toId, double money) {
        transfer(fromId, toId, money);
    }

    /**
     * Seata目前不支持分库分表的AT（弱一致性）模式
     * 使用注解@ShardingTransactionType(TransactionType.BASE)配合@Transactional使用AT模式 解决分布式事务问题
     */
    @Transactional
    @ShardingTransactionType(TransactionType.BASE)
    @Override
    public void becomingAT(Integer fromId, Integer toId, double money) {
        transfer(fromId, toId, money);
    }

    private void transfer(Integer fromId, Integer toId, double money) {
        User from = userMapper.findById(fromId);
        User to = userMapper.findById(toId);
        from.setMoney(from.getMoney() - money);
        to.setMoney(to.getMoney() + money);
        userMapper.update(from);
        System.out.println(RootContext.inGlobalTransaction());
        System.out.println(RootContext.getXID());
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int i = 1 / 0;
        userMapper.update(to);
    }
}
