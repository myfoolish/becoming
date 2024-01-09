package com.xw.provider;

import com.xw.ProviderService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/28
 */
@DubboService
public class ProviderServiceImpl implements ProviderService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
