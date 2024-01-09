package com.xw.mapper;

import com.xw.entity.Items;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
public interface ItemsMapper {

    @Select("select * from items where id = #{itemId}")
    public Items findItemById(Long itemId);
    /**
     * 减少库存
     * @param num 数量
     * @param itemId  商品id
     */
    @Update("update items set item_stock = item_stock - #{num} where id = #{itemId}")
    public void reduceStock(@Param("num") int num, @Param("itemId") Long itemId);
}
