package com.xw.custom;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/7/28 9:45
 */
public class CustomException extends Exception {

    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }
}
