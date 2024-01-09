package com.xw.handler;

import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/27
 */
//@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    public String handler(RuntimeException e) throws TransactionException {
        // 1、手动回滚事务；2、后台服务不需要异常处理
//        if (RootContext.inGlobalTransaction()) {
//            GlobalTransactionContext.reload(RootContext.getXID()).rollback();
//        }
        return "This is GlobalExceptionHandler";
    }
}
