package com.xw.interceptor;

import io.seata.core.context.RootContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/26
 * 每次使用restTemplate请求时都会先走此拦截器，然后存入全局事务id
 */
public class XAPutXidInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        if (!StringUtils.isEmpty(RootContext.getXID())) {
            httpRequest.getHeaders().add(RootContext.KEY_XID, RootContext.getXID());
        }
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }
}
