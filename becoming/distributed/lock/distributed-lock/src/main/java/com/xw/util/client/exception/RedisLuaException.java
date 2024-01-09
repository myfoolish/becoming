package com.xw.util.client.exception;

public class RedisLuaException extends RuntimeException {
    public RedisLuaException(String message) {
        super(message);
    }

    public RedisLuaException(String message, Throwable cause) {
        super(message, cause);
    }
}
