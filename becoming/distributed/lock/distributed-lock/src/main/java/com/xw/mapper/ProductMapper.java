package com.xw.mapper;

import com.xw.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/6/9
 */
@Mapper
public interface ProductMapper {
    Product selectByPrimaryKey(int productId);

    void updateByPrimaryKeySelective(Product product);

    void updateProductCount(@Param("id") int productId, @Param("purchaseProductNum") int purchaseProductNum, @Param("updateTime") Date updateTime, @Param("updateUser") String updateUser);
}
