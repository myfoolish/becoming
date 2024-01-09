package com.xw.util.client.exception;

public class RedisErrorStreamEntryIDException extends RedisRuntimeException {
    public RedisErrorStreamEntryIDException() {
        super("Error EntryID");
    }

    private RedisErrorStreamEntryIDException(String message) {
        super(message);
    }
}
