//package com.xw.feign;
//
//import com.xw.entity.Accounts;
//import com.xw.entity.Items;
//import com.xw.entity.Orders;
//import io.seata.core.context.RootContext;
//import io.seata.core.exception.TransactionException;
//import io.seata.tm.api.GlobalTransactionContext;
//import org.springframework.stereotype.Component;
//
///**
// * @author liuxiaowei
// * @description
// * @date 2023/12/27
// */
//public class FeignFallbackService {
//
//    /**
//     * orders服务降级
//     */
////    @Component
////    public static class OrderFeignFallbackService implements FeignService.OrderFeignService {
////
////        @Override
////        public String createOrder(Orders orders) {
////            return "创建订单降级！";
////        }
////    }
//
//    /**
//     * items服务降级
//     */
////    @Component
////    public static class itemFeignFallbackService implements FeignService.ItemFeignService {
////        @Override
////        public Items findItemById(Long itemId) {
////            return null;
////        }
////
////        @Override
////        public String reduceStock(int num, Long itemId) {
////            return "扣减库存降级处理！";
////        }
////    }
//
//    /**
//     * account服务降级
//     */
//    @Component
//    public static class AccountFeignFallbackService implements FeignService.AccountFeignService {
//
//        @Override
//        public Accounts findAccountById(Long accountId) {
//            return null;
//        }
//
//        @Override
//        public String reduceMoney(double money, Long accountId) {
//            if (RootContext.inGlobalTransaction()) {
//                try {
//                    GlobalTransactionContext.reload(RootContext.getXID()).rollback();
//                } catch (TransactionException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            return "扣款降级处理！";
//        }
//    }
//}
