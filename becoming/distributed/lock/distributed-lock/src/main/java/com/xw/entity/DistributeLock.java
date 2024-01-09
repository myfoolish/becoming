package com.xw.entity;

import lombok.Data;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/6
 */
@Data
public class DistributeLock {
    private int id;
    // 业务代码
    private String businessCode;
    // 业务名称
    private String businessName;
}
