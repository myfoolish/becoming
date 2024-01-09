package com.xw.springinaction.test;

import com.xw.springinaction.PersistenceConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/1/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PersistenceConfig.class})
@ActiveProfiles("dev")  // 使用profile进行测试
public class PersistenceTest {
}
