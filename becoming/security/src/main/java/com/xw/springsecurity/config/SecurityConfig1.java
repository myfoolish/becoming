package com.xw.springsecurity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author liuxiaowei
 * @description 从数据库中查询用户相关信息
 * @date 2020/10/21 10:20
 */
//@Configuration
//@EnableWebSecurity
// 拦截注解了@PreAuthrize注解的配置，prePostEnabled=true 的时候，@PreAuthorize 可以使用
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig1 extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 定义哪些URL需要被保护、哪些不需要被保护
                .authorizeRequests()
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
        auth.inMemoryAuthentication()
                // 密码前面的{noop}表示的是指定的PasswordEncoder
                // 普通用户，拥有 USER 权限，可以访问部分资源
                .withUser("user").password("{noop}user").roles("USER")
                .and()
                // 管理员，拥有 ADMIN 和 USER 权限，可以访问所有资源
                .withUser("admin").password("{noop}admin").roles("ADMIN", "USER");;
    }
}
