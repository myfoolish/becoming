package com.xw.service.impl;

import com.xw.entity.Items;
import com.xw.entity.Orders;
import com.xw.enums.OrderStatus;
import com.xw.feign.FeignService;
import com.xw.service.BusinessService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/26
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    @Autowired
    private FeignService.OrderFeignService orderFeignService;
    @Autowired
    private FeignService.ItemFeignService itemFeignService;
    @Autowired
    private FeignService.AccountFeignService accountFeignService;

    /**
     * 通过feign调用
     * @param accountId
     * @param itemId
     * @param num
     */
    @Override
    @GlobalTransactional
    public void placeOrder(Long accountId, Long itemId, int num) {
        System.out.println(RootContext.inGlobalTransaction());
        System.out.println(RootContext.getXID());
        // 查询商品信息
        Items item = itemFeignService.findItemById(itemId);
        if (item != null) {
            Orders order = new Orders()
                    .setId(System.nanoTime())
                    .setAccountId(accountId)
                    .setItemId(itemId)
                    .setItemNum(num)
                    .setOrderStatus(OrderStatus.CREATE.name())
                    .setOrderAmount(item.getItemPrice() * num);
            orderFeignService.createOrder(order);
            itemFeignService.reduceStock(num, itemId);
            accountFeignService.reduceMoney(item.getItemPrice() * num, accountId);
            order.setOrderStatus(OrderStatus.SUCCESS.name());
            orderFeignService.updateOrder(order);
        }
    }
}
