package com.xw.spring.ioc.eg2;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/4
 */
@Data
public class Company {
    private List<String> roomList;
    private Set<String> roomSet;
    private Map<String, Computer> computers;
    private Properties info;
}
