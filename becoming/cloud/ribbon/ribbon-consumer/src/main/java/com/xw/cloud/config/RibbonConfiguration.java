package com.xw.cloud.config;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxiaowei
 * @description ribbon 负载均衡策略-七种策略  「com.netflix.loadbalancer.」
 *      RandomRule  随机挑选一个节点访问
 *      RoundRobinRule  轮询
 *      RetryRule
 *      WeightedResponseTimeRule
 *      BestAvailableRule
 *      AvailabilityFilteringRule
 *      ZoneAvoidanceRule
 * @date 2023/11/22
 */
@Configuration
@RibbonClient(value = "eureka-client",configuration = com.netflix.loadbalancer.RoundRobinRule.class)
public class RibbonConfiguration {

    /**
     * 1、全局负载均衡策略
     * ribbon 默认负载均衡策略是轮询「RoundRobinRule」若想改变负载均衡策略
     * 1️⃣：在声明 @Configuration 的类里面声明一个 @Bean 返回对应的负载均衡策略
     * 2️⃣：在启动类里面面声明一个 @Bean 返回对应的负载均衡策略
     * .@SpringBootApplication-> @SpringBootConfiguration-> @Configuration
     *
     * 2、针对某个服务负载均衡策略【1️⃣的优先级比2️⃣高】
     * 1️⃣：在声明 @Configuration 的类再声明 @RibbonClient
     * .@RibbonClient(value = "eureka-client",configuration = com.netflix.loadbalancer.RandomRule.class)
     *
     * 2️⃣：在application.properties中配置 固定格式：服务名称.ribbon.NFLoadBalancerRuleClassName
     * .eureka-client.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule
     * @return
     */
//    @Bean
//    public IRule defaultLBStrategy() {
//        return new RoundRobinRule();
//    }

}
