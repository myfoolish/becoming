package com.xw.consumer;

import com.xw.ProviderService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/28
 */
@Component
public class Task implements CommandLineRunner {

    @DubboReference
    private ProviderService providerService;

    @Override
    public void run(String... args) throws Exception {
        String result = providerService.sayHello("world");
        System.out.println("Receive result ======> " + result);

        new Thread(()-> {
            while (true) {
                try {
                    Thread.sleep(1000);
                    System.out.println(new Date() + " Receive result ======> " + providerService.sayHello("world"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }
}
