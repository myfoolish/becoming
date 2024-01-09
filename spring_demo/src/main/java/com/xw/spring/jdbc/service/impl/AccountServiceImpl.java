package com.xw.spring.jdbc.service.impl;

import com.xw.spring.jdbc.entity.Account;
import com.xw.spring.jdbc.service.AccountService;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/2
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void saveAccount(Account account) {
        String sql = "insert into ssm_demo_account(name,money) values (?,?)";
        int num = jdbcTemplate.update(sql, new Object[]{account.getName(), account.getMoney()});
        System.out.println("保存了" + num + "个账户");
    }

    @Override
    public List<Map<String, Object>> find(Account account) {
        String sql;
        List<Map<String, Object>> list;
        if (account.getId() != null) {
            System.out.println("根据条件查询");
            sql = "select id, name as '姓名', money as '工资' from ssm_demo_account where id = ?";
            list = jdbcTemplate.queryForList(sql, account.getId());
        } else {
            System.out.println("查询所有");
            sql = "select * from ssm_demo_account";
            list = jdbcTemplate.queryForList(sql);
        }
        return list;
    }

    @Override
    public Account findById(Integer id) {
        System.out.println("根据id查询");
        String sql = "select * from ssm_demo_account where id = ?";
        return jdbcTemplate.queryForObject(sql, new Integer[]{id}, new BeanPropertyRowMapper<>(Account.class));
    }
}
