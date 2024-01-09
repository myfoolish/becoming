package com.xw.service.impl;

import com.xw.entity.Items;
import com.xw.entity.Orders;
import com.xw.enums.OrderStatus;
import com.xw.feign.FeignService;
import com.xw.service.BusinessService;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/26
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    // 创建订单
    private static final String CREATE_ORDER = "http://seata-cloud-orders/orders/createOrder";
    // 查询商品信息
    private static final String ITEM_INFO = "http://seata-cloud-items/items/findItemById/%d";
    // 减库存
    private static final String REDUCE_STOCK = "http://seata-cloud-items/items/reduceStock?num=%d&itemId=%d";
    // 扣款
    private static final String REDUCE_MONEY = "http://seata-cloud-accounts/accounts/reduceMoney?money=%f&accountId=%d";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 通过ribbon调用
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
        Items item = restTemplate.getForObject(String.format(ITEM_INFO, itemId), Items.class);
        if (item != null) {
            Orders order = new Orders()
                    .setOrderStatus(OrderStatus.CREATE.name())
                    .setAccountId(accountId)
                    .setItemId(itemId)
                    .setItemNum(num)
                    .setOrderAmount(item.getItemPrice() * num);

//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<Orders> httpEntity = new HttpEntity<>(order, httpHeaders);
            restTemplate.postForObject(CREATE_ORDER, order, String.class);
            // 减库存
            restTemplate.put(String.format(REDUCE_STOCK, num, itemId), null);
            // 扣款
            restTemplate.put(String.format(REDUCE_MONEY, item.getItemPrice() * num, accountId), null);
        }
    }

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
    public void placeOrderByFeign(Long accountId, Long itemId, int num) {
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
