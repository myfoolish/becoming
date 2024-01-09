package com.xw.mapper;

import com.xw.entity.DistributeLock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/6
 */
@Mapper
public interface DistributeLockMapper {
    DistributeLock selectDistributeLock(@Param("businessCode") String businessCode);
}
