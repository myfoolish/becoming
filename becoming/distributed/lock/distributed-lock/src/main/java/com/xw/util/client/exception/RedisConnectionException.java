package com.xw.util.client.exception;

public class RedisConnectionException extends RedisException {
    public RedisConnectionException(String message) {
        super(message);
    }

    public RedisConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
