package com.xw.rabbitmq.api;

/**
 * @author liuxiaowei
 * @description 消费者监听消息
 * @date 2022/12/27
 */
public interface MessageListener {

    public void onMessage(Message message);
}
