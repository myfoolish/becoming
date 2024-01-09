package com.xw.spring.ioc.eg2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Computer {
//    private String brand;   // 品牌
    private String type;    // 台式机、笔记本、服务器
//    private String sn;      // 产品序列号
    private Float price;    // 价格
}
