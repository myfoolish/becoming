package com.xw.publish;

import com.xw.annoations.ThreadNotSafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * @author liuxiaowei
 * @description 不安全发布
 * @date 2023/6/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ThreadNotSafe
public class UnsafePublish {

    private String[] states = {"a", "b", "c"};

    public String[] getStates() {
        return states;
    }

    @Test
    public void name() {
        UnsafePublish unsafePublish = new UnsafePublish();
        System.out.println(Arrays.toString(unsafePublish.getStates()));
        unsafePublish.getStates()[0] = "abc";
        System.out.println(Arrays.toString(unsafePublish.getStates()));
    }
}
