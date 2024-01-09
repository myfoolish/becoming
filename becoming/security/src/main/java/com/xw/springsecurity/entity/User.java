package com.xw.springsecurity.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/10/21 17:20
 */
@Data
@Entity
@Table(name = "spring_security")
public class User implements Serializable {
    private static final long serialVersionUID = 4221507003552430239L;
    @Id
    @Column
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String roles;
}
