package com.xw.mapper;

import com.xw.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/9
 */
@Mapper
public interface OrderItemMapper {
    void insertSelective(OrderItem orderItem);
}
