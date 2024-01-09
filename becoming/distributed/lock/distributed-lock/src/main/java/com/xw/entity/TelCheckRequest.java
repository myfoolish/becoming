package com.xw.entity;

import lombok.Data;

@Data
public class TelCheckRequest {
    /**
     * 姓名
     */
    private String name;
    /**
     * 身份证
     */
    private String certNo;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 身份证类型
     */
    private String certType;
    /**
     * 加密方式
     */
    private String encryptType;
    /**
     * 产品编码
     */
    private String productCode;
}