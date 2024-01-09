package com.xw.springsecurity.config;

import com.xw.springsecurity.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author liuxiaowei
 * @description 从数据库中查询用户相关信息
 * @date 2020/10/21 10:20
 */
@Configuration
@EnableWebSecurity
// 拦截注解了@PreAuthrize注解的配置，prePostEnabled=true 的时候，@PreAuthorize 可以使用
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 1 与下面1相对应
    @Bean
    public UserDetailsService customUserDetailsService() {
        return new CustomUserDetailsService();
    }
    // 2 与下面2相对应
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 定义哪些URL需要被保护、哪些不需要被保护
                .authorizeRequests()
                .antMatchers("/admin").hasAuthority("ADMIN")
                .antMatchers("/**").hasAuthority("USER")
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
                // 1 与上面1相对应
//                .userDetailsService(customUserDetailsService())
                // 2 与上面2相对应
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String user = bCryptPasswordEncoder.encode("user");
        String admin = bCryptPasswordEncoder.encode("admin");
        System.out.println(user);
        System.out.println(admin);
    }
}
