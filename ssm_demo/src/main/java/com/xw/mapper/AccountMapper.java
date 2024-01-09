package com.xw.mapper;

import com.xw.entity.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/2
 */
public interface AccountMapper {

    @Insert("insert into ssm_demo_account(name, money) values (#{name}, #{money})")
    void saveAccount(Account account);

    @Select("select * from ssm_demo_account")
    List<Account> findAll();
}
