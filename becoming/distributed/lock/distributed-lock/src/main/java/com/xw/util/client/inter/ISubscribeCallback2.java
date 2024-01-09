package com.xw.util.client.inter;

import com.xw.util.client.template.RedisPoolTemplate;

public interface ISubscribeCallback2 extends ISubscribeCallback {
    void setTemplate(RedisPoolTemplate var1);
}
