package com.xw.interceptor;

import io.seata.core.context.RootContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/26
 * 获取全局事务id
 */
@Component
public class XAGetXidInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String xid = request.getHeader(RootContext.KEY_XID);
        if (!StringUtils.isEmpty(xid)) {
            RootContext.bind(xid);
        }
//        return HandlerInterceptor.super.preHandle(request, response, handler);
        return true;
    }
}
