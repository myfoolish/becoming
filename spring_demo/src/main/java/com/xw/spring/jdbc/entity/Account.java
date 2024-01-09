package com.xw.spring.jdbc.entity;

import lombok.Data;

/**
 * @author liuxiaowei
 * @description 账户的实体
 * @date 2021/6/2
 */
@Data
public class Account {
    private Integer id;
    private String name;
    private float money;
}
