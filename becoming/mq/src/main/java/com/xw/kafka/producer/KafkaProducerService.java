//package com.xw.kafka.producer;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.core.KafkaTemplate;
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
//public class KafkaProducerService {
//
//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    public void sendMessage(String topic, Object object) {
//        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, object);
//        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
//            @Override
//            public void onSuccess(SendResult<String, Object> result) {
//                log.info("发送消息成功：" + result.toString());
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                log.info("发送消息失败：" + throwable.getMessage());
//            }
//        });
//    }
//}
