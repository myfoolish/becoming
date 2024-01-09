package com.xw.rabbitmq.api;

/**
 * @author liuxiaowei
 * @description 回掉函数处理
 * @date 2022/12/27
 */
public interface SendCallBack {
    public void onSuccess();

    public void onFailure();
}
