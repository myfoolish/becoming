package com.xw.mapper;

import com.xw.entity.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/9
 */
@Mapper
public interface OrderMapper {
    void insertSelective(Order order);
}
