package com.xw.util.client.inter;


import com.xw.util.client.exception.RedisException;
import com.xw.util.client.exception.RedisRuntimeException;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/21
 */
public interface IExceptionHandler {
    Object dealException(RedisException var1);

    default Object dealConnectionException(RedisException ex) {
        throw new RedisRuntimeException("发生了连接异常", ex);
    }
}
