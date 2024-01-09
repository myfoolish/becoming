package com.xw.designPattern.creation.builder;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/10/18
 */
@Data
@ToString
@Builder
public class Phone {
    private String cpu;
    private String memory;
    private String disk;
    private String camera;
}
