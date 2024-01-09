package com.xw.feign;

import com.xw.entity.Accounts;
import com.xw.entity.Items;
import com.xw.entity.Orders;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/26
 */
public interface FeignService {

//    @FeignClient(value = "seata-cloud-orders", fallback = FeignFallbackService.OrderFeignFallbackService.class)
    @FeignClient(value = "seata-cloud-orders")
    @RequestMapping("orders")
    public interface OrderFeignService {
        @PostMapping("createOrder")
        public String createOrder(@RequestBody Orders orders);

        @PutMapping("updateOrder")
        public void updateOrder(@RequestBody Orders orders);
    }

//    @FeignClient(value = "seata-cloud-items", fallback = FeignFallbackService.itemFeignFallbackService.class)
    @FeignClient(value = "seata-cloud-items")
    @RequestMapping("items")
    public interface ItemFeignService {
        @GetMapping("findItemById/{itemId}")
        public Items findItemById(@PathVariable("itemId") Long itemId);

        @PutMapping("reduceStock")
        public String reduceStock(@RequestParam("num") int num,
                                  @RequestParam("itemId") Long itemId);
    }

//    @FeignClient(value = "seata-cloud-accounts",fallback = FeignFallbackService.AccountFeignFallbackService.class)
    @FeignClient(value = "seata-cloud-accounts")
    @RequestMapping("accounts")
    public interface AccountFeignService{
        @GetMapping("findAccountById/{accountId}")
        public Accounts findAccountById(@PathVariable("accountId") Long accountId);

        @PutMapping("reduceMoney")
        public String reduceMoney(@RequestParam("money") double money,
                                  @RequestParam("accountId") Long accountId);
    }
}
