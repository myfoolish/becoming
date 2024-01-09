package com.xw.kafka.producer;

import com.xw.prototype.MsgSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/2/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaProducerTest {
//    @Autowired
//    private KafkaProducerService kafkaProducerService;

//    @Test
//    public void send() throws InterruptedException {
//        String topic = "kafkaTopic";
//        for (int i = 0; i < 5; i++) {
//            kafkaProducerService.sendMessage(topic, "hello kafka" + i);
//        }
//        Thread.sleep(Integer.MAX_VALUE);
//    }

    @Autowired
    private MsgSender msgSender;
    @Test
    public void name() {
        msgSender.sendX();
        msgSender.sendW();
    }
}
