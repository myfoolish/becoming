package com.xw.springsecurity.dao;

import com.xw.springsecurity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/10/21 17:30
 */
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
