package com.xw.rabbitmq.exception;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/12/27
 */
public class MessageException extends Exception{
    private static final long serialVersionUID = -7058316704820054490L;

    public MessageException() {
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    public MessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
