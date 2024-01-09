package com.xw.mapper;

import com.xw.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/5/20
 */
@Mapper
public interface UserMapper {

    void add(User user);

    void delete(Long id);

    void update(User user);

    List<User> findAll();

    List<User> findById(Long id);

    User findByName(String name);
}
