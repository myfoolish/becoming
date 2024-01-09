//package com.xw.kafka.consumer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.Consumer;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.stereotype.Component;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//
///**
// * @author liuxiaowei
// * @description
// * @date 2023/1/9
// */
//@Slf4j
//@Component
//public class KafkaConsumerService {
//
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    @KafkaListener(groupId = "kafkaConsumer", topics = "kafkaTopic")
//    public void onMessage(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment, Consumer<?, ?> consumer) {
//        log.info("消费端接收消息：" + record.value());
//        // 手工签收机制
//        acknowledgment.acknowledge();
//
//    }
//}
