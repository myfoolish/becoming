package com.xw.rabbitmq.api;

/**
 * @author liuxiaowei
 * @description 消息的
 * @date 2022/12/21
 */
public class MessageType {
    /**
     * 迅速消息：不需要保障消息的可靠性投递，也不需要做消息的confirm确认
     */
    public static final String RAPID = "0";
    /**
     * 确认消息：不需要保障消息的可靠性投递，但是要做消息的confirm确认
     */
    public static final String CONFIRM = "1";
    /**
     * 可靠性消息：需要保证消息的100%可靠性投递，不允许有任何消息的丢失
     * PS: 保障数据库和所发的消息是原子性的（最终一致性的）
     */
    public static final String RELIABLE = "2";
}
