package com.xw.test;

import com.xw.spring.entity.User1;
import com.xw.spring.entity.User2;
import com.xw.spring.service.User1Service;
import com.xw.spring.service.User2Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/8/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Spring_Transaction_Propagation_REQUIRES_NEW_Test {

    @Autowired
    private User1Service user1Service;

    @Autowired
    private User2Service user2Service;

    /**
     * 外围方法（未）开启事务
     * 执行成功，正常插入
     */
    @Test
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addRequires_NEW() {
        User1 user1 = new User1();
        user1.setName("user1.1");
        user1Service.addRequiresNew(user1);

        User2 user2 = new User2();
        user2.setName("user2.1");
        user2Service.addRequiresNew(user2);
    }

    /**
     * 外围方法未开启事务
     * 执行失败，user1、user2 插入方法都在自己的事务中独立运行，外围方法异常不影响内部插入方法独立的事务。
     */
    @Test
    public void noTransaction_addRequires_NEW_abnormal_01() {
        User1 user1 = new User1();
        user1.setName("user1.1");
        user1Service.addRequiresNew(user1);

        User2 user2 = new User2();
        user2.setName("user2.1");
        user2Service.addRequiresNew(user2);

        throw new RuntimeException();
    }

    /**
     * 外围方法未开启事务
     * 执行失败、user1、user2 插入方法都在自己的事务中独立运行，所以 user2 方法抛出异常只会回滚 user2，user1 方法不受影响。
     */
    @Test
    public void noTransaction_addRequires_NEW_abnormal_02() {
        User1 user1 = new User1();
        user1.setName("user1.1");
        user1Service.addRequired(user1);

        User2 user2 = new User2();
        user2.setName("user2.1");
        user2Service.addRequiresNewException(user2);
    }
//    结论：在外围方法未开启事务的情况下 Propagation.REQUIRES_NEW 修饰的内部方法会新开启自己的事务，且开启的事务相互独立，互不干扰。

    /**
     * 外围方法开启事务
     * 执行失败，user0 插入方法和外围方法一个事务，插入 user1 方法、插入 user2 方法分别在独立的新建事务中，
 *          外围方法抛出异常只回滚和外围方法同一事务的方法，故插 入user0 的方法回滚。
     */
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_addRequires_NEW_abnormal_01() {

        User1 user0 = new User1();
        user0.setName("user1.0");
        user1Service.addRequired(user0);

        User1 user1 = new User1();
        user1.setName("user1.1");
        user1Service.addRequiresNew(user1);

        User2 user2 = new User2();
        user2.setName("user2.1");
        user2Service.addRequiresNew(user2);

        throw new RuntimeException();
    }

    /**
     * 外围方法开启事务
     * 执行失败，user0 插入方法和外围方法一个事务，插入 user1 方法、插入 user2 方法分别在独立的新建事务中，
 *          插入 user2 方法抛出异常，首先插入 user2 方法的事务被回滚，异常继续抛出被外围方法感知，外围方法事务亦被回滚，故插入 user0 方法也被回滚。
     */
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_addRequires_NEW_abnormal_02() {

        User1 user0 = new User1();
        user0.setName("user1.0");
        user1Service.addRequired(user0);

        User1 user1 = new User1();
        user1.setName("user1.1");
        user1Service.addRequiresNew(user1);

        User2 user2 = new User2();
        user2.setName("user2.1");
        user2Service.addRequiresNewException(user2);
    }

    /**
     * 外围方法开启事务
     * 执行成功，user0 插入方法和外围方法一个事务，插入 user1 方法、插入 user2 方法分别在独立的新建事务中，
     *      插入 user2 方法抛出异常，首先插入 user2 方法的事务被回滚，异常被catch不会被外围方法感知，外围方法事务不回滚，故插入 user0 方法插入成功。
     */
    @Test
    @Transactional(propagation = Propagation.REQUIRED)
    public void transaction_addRequires_NEW_abnormal_03() {

        User1 user0 = new User1();
        user0.setName("user1.0");
        user1Service.addRequired(user0);

        User1 user1 = new User1();
        user1.setName("user1.1");
        user1Service.addRequiresNew(user1);

        User2 user2 = new User2();
        user2.setName("user2.1");
        try {
            user2Service.addRequiresNewException(user2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("方法回滚");
        }
    }
//    结论：在外围方法开启事务的情况下 Propagation.REQUIRED 修饰的内部方法会加入到外围方法的事务中，所有 Propagation.REQUIRED 修饰的内部方法和外围方法均属于同一事务，只要一个方法回滚，整个事务均回滚。
}
