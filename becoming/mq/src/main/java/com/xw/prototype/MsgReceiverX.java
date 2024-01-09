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
@RabbitListener(queues = "queueX")
public class MsgReceiverX {

    @RabbitHandler
    public void process(String message) {
        System.out.println("MsgReceiverX: " + message);
    }
}
