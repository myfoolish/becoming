package com.xw.util.client.exception;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/21
 */
public class RedisException extends Exception {
    protected Throwable cause;

    public RedisException(String message) {
        super(message);
    }

    public RedisException(String message, Throwable cause) {
        super(message, cause);
        this.cause = cause;
    }
}
