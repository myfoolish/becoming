package com.xw.service;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/26
 */
public interface BusinessService {

    /**
     * 下订单  创建订单---减库存---扣钱
     * @param accountId
     * @param itemId
     * @param num
     */
    public void placeOrder(Long accountId, Long itemId, int num);
}
