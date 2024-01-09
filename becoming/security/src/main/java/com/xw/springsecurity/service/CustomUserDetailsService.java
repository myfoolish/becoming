package com.xw.springsecurity.service;

import com.mysql.cj.util.StringUtils;
import com.xw.springsecurity.dao.UserRepository;
import com.xw.springsecurity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/10/21 17:28
 */
//@Service
@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final GrantedAuthority DEFAULT_ROLE = new SimpleGrantedAuthority("USER");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userFromDatabase = userRepository.findByUsername(username);
        if (userFromDatabase == null) {
            // log.warn("User: {} not found", login);
            throw new UsernameNotFoundException("User " + username + " was not found in db.");
        }
        // 设置角色
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        String rolesFromDatabase = userFromDatabase.getRoles();
        if (StringUtils.isNullOrEmpty(rolesFromDatabase)) {
            grantedAuthorities.add(DEFAULT_ROLE);
        } else {
            String[] roles = rolesFromDatabase.split(",");
            for (String role : roles) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                grantedAuthorities.add(grantedAuthority);
            }
        }
        return new org.springframework.security.core.userdetails.User(username,userFromDatabase.getPassword(),grantedAuthorities);
    }
}
