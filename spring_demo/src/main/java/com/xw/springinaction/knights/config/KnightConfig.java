package com.xw.springinaction.knights.config;

import com.xw.springinaction.knights.BraveKnight;
import com.xw.springinaction.knights.Knight;
import com.xw.springinaction.knights.Quest;
import com.xw.springinaction.knights.SlayDragonQuest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/1/10
 */
@Configuration
public class KnightConfig {

//    @Bean
    public Knight knight() {
        return new BraveKnight(quest());
    }

//    @Bean
    public Quest quest() {
        return new SlayDragonQuest(System.out);
    }
}
