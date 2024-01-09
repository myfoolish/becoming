package com.xw.util.client.exception;

public class RedisClientException extends RedisException {
    public RedisClientException(String message) {
        super(message);
    }

    public RedisClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
