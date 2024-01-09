package com.xw.mapper;

import com.xw.entity.Orders;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
public interface OrdersMapper {
    @Insert("insert into orders values (#{id}, #{accountId}, #{itemId}, #{itemNum}, #{orderStatus}, #{orderAmount})")
    public void createOrder(Orders orders);

    @Update("update orders set order_status = #{orderStatus} where id = #{id}")
    void updateOrder(Orders orders);
}
