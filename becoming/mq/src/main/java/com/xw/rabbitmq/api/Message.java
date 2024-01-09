package com.xw.rabbitmq.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/12/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 8707350092876854831L;

    // 消息的唯一ID
    private String messageId;
    // 消息的类型: 默认为confirm消息类型
    private String messageType = MessageType.CONFIRM;
    // 消息的主题
    private String topic;
    // 消息的路由规则
    private String routingKey = "";
    // 消息的附加属性
    private Map<String, Object> attributes = new HashMap<>();
    // 延迟消息的参数配置
    private int delayMills;

    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
    }
}
