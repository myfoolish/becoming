package com.xw.rabbitmq.exception;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/12/27
 */
public class MessageRunTimeException extends  RuntimeException{
    private static final long serialVersionUID = -1548598243015693356L;

    public MessageRunTimeException() {
    }

    public MessageRunTimeException(String message) {
        super(message);
    }

    public MessageRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageRunTimeException(Throwable cause) {
        super(cause);
    }

    public MessageRunTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
