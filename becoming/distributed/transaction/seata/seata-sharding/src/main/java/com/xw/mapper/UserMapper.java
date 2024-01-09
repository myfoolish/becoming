package com.xw.mapper;

import com.xw.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
public interface UserMapper {
    @Insert("insert into user(id, name, money) values (#{id}, #{name}, #{money})")
    void insert(User user);

    @Select("select * from user where id = #{id}")
    User findById(Integer id);

    @Update("update user set money = #{money} where id = #{id}")
    void update(User user);
}
