package com.xw.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author liuxiaowei
 * @description 在代码中配置用户相关信息，demo使用
 * @date 2020/10/21 10:20
 *
 * SecurityContext 安全上下文，用户通过 Spring Security 的校验之后，验证信息存储在 SecurityContext 中，查看接口
 * （org.springframework.security.core.context.SecurityContext）可以看到只定义了两个方法，其主要作用就是获取 Authentication 对象。
 *
 * 在典型的 web 应用程序中，用户登录一次，然后由其会话 ID 标识，服务器缓存持续时间会话的主体信息。
 * SecurityContextHolder 用来存储 SecurityContext 实例。默认情况下，SecurityContextHolder 将 SecurityContext 存储为 HTTP 请求之间的 HttpSession 属性。
 * 它会为每个请求恢复上下文 SecurityContextHolder，并且最重要的是，在请求完成时清除 SecurityContextHolder。
 * （org.springframework.security.core.context.SecurityContextHolder）是一个类，它的功能方法都是静态的（static）。
 */

//@Configuration
//@EnableWebSecurity
// 拦截注解了@PreAuthrize注解的配置
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig0 extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 定义哪些URL需要被保护、哪些不需要被保护
                .authorizeRequests()
                // USER 角色设置必须在 ADMIN 前面
                .antMatchers("/**").hasRole("USER")
                .anyRequest()
                .authenticated()
                .and()
                // 定义当需要用户登录时候，转到的登录页面
                .formLogin()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                // 密码前面的{noop}表示的是指定的PasswordEncoder
                // 普通用户，拥有 USER 权限，可以访问部分资源
                .withUser("user").password("{noop}user").roles("USER")
                .and()
                // 管理员，拥有 ADMIN 和 USER 权限，可以访问所有资源
                .withUser("admin").password("{noop}admin").roles("ADMIN", "USER");
    }
}
