package com.xw.mapper;

import com.xw.spring.entity.User2;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/8/31
 */
@Mapper
public interface User2Mapper {

    int insert(User2 record);

    User2 selectByPrimaryKey(Integer id);

    //其他方法省略...
}
