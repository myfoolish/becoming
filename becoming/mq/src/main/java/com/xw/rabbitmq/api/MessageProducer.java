package com.xw.rabbitmq.api;

import com.xw.rabbitmq.exception.MessageRunTimeException;

import java.util.List;

/**
 * @author liuxiaowei
 * @description 生产者生产消息
 * @date 2022/12/27
 */
public interface MessageProducer {

    /**
     * 消息的发送
     * @param message
     * @throws MessageRunTimeException
     */
    public void send(Message message) throws MessageRunTimeException;

    /**
     * 消息的批量发送
     * @param messages
     * @throws MessageRunTimeException
     */
    public void send(List<Message> messages) throws MessageRunTimeException;

    /**
     * 消息的发送 附带SendCallBack回掉执行响应的业务逻辑处理
     * @param messages
     * @param sendCallBack
     * @throws MessageRunTimeException
     */
    public void send(List<Message> messages, SendCallBack sendCallBack) throws MessageRunTimeException;
}
