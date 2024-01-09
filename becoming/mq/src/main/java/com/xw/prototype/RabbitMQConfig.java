package com.xw.prototype;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxiaowei
 * @description rabbitMQ 配置类
 * @date 2023/12/19
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queueX() {
        return new Queue("queueX");
    }

    @Bean
    public Queue queueW() {
        return new Queue("queueW");
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    Binding bindingExchangeAndQueueX(Queue queueX, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueX).to(topicExchange).with("topic.x.w");
    }

    @Bean
    Binding bindingExchangeAndQueueW(Queue queueW, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueW).to(topicExchange).with("topic.#");
    }
}
