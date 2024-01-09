package com.xw.service;

import com.xw.entity.Order;
import com.xw.entity.OrderItem;
import com.xw.entity.Product;
import com.xw.mapper.OrderItemMapper;
import com.xw.mapper.OrderMapper;
import com.xw.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/14
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    // 购买商品id
    private final int purchaseProductId = 1;
    // 购买商品数量
    private final int purchaseProductNum = 1;

    private final Lock lock = new ReentrantLock();

    // 手动控制事务
    @Autowired
    private PlatformTransactionManager platformTransactionManager;  // 平台的事务管理器
    @Autowired
    private TransactionDefinition transactionDefinition;

//    @Transactional(rollbackFor = Exception.class)
    public Integer createOrder() throws Exception {

        lock.lock();
        Product product = null;
        try {
            // 手动开启事务
            TransactionStatus transactionStatus1 = platformTransactionManager.getTransaction(transactionDefinition);
            product = productMapper.selectByPrimaryKey(purchaseProductId);
            if (product == null) {
                platformTransactionManager.rollback(transactionStatus1);
                throw new Exception("购买商品：" + purchaseProductId + "不存在");
            }
            // 商品当前库存
            int currentCount = product.getCount();
            // 校验库存
            if (purchaseProductNum > currentCount) {
                platformTransactionManager.rollback(transactionStatus1);
                throw new Exception("商品：" + purchaseProductId + "仅剩" + currentCount + "件，无法购买");
            }

            /**
             * 在程序中计算扣减库存出现超卖问题，故删掉下沉到数据库中计算
             // 计算剩余库存
             int leftCount = currentCount - purchaseProductNum;
             // 更新库存
             product.setCount(leftCount);
             product.setUpdateTime(new Date());
             product.setUpdateUser("xw");
             productMapper.updateByPrimaryKeySelective(product);
             */

            productMapper.updateProductCount(product.getId(), purchaseProductNum, new Date(), "xw");
            // 检索商品库存，如果商品库存为负数，则抛出异常（不推荐）
            platformTransactionManager.commit(transactionStatus1);
        }  finally {
            lock.unlock();
        }

        // 手动开启事务
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        Order order = new Order();
        order.setOrderAmount(product.getPrice().multiply(new BigDecimal(purchaseProductNum)));
        order.setOrderStatus(1);    // 待处理
        order.setReceiverName("xw");
        order.setReceiverMobile("17800000000");
        order.setCreateTime(new Date());
        order.setCreateUser("xw");
        order.setUpdateTime(new Date());
        order.setUpdateUser("xw");
        orderMapper.insertSelective(order);

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(order.getId());
        orderItem.setProduceId(product.getId());
        orderItem.setPurchasePrice(product.getPrice());
        orderItem.setPurchaseCount(purchaseProductNum);
        orderItem.setCreateTime(new Date());
        orderItem.setCreateUser("xw");
        orderItem.setUpdateTime(new Date());
        orderItem.setUpdateUser("xw");
        orderItemMapper.insertSelective(orderItem);

        platformTransactionManager.commit(transactionStatus);   // 提交

        lock.unlock();

        return order.getId();
    }
}
