package com.xw.util.client.exception;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/21
 */
public class RedisRuntimeException extends RuntimeException{
    public RedisRuntimeException(String message) {
        super(message);
    }

    public RedisRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
