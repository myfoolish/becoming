package com.xw.prototype;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/20
 */
@Component
@RabbitListener(queues = "queueW")
public class MsgReceiverW {

    @RabbitHandler
    public void process(String message) {
        System.out.println("MsgReceiverW: " + message);
    }
}
