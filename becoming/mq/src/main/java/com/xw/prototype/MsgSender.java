package com.xw.prototype;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/19
 */
@Component
public class MsgSender {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void sendX() {
        String message = "this is X and the RoutingKey is Topic.x.w";
        System.out.println("发送了：" + message);
        amqpTemplate.convertAndSend("topicExchange", "topic.x.w", message);
    }

    public void sendW() {
        String message = "this is W and the RoutingKey is Topic.o.r";
        System.out.println("发送了：" + message);
        amqpTemplate.convertAndSend("topicExchange", "topic.o.r", message);
    }

//    public static void main(String[] args) {
//        MsgSender msgSender = new MsgSender();
//        msgSender.sendX();
//        msgSender.sendW();
//    }
}
