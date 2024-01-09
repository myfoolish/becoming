package com.xw.mapper;

import com.xw.spring.entity.User1;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/8/31
 */
@Mapper
public interface User1Mapper {

    int insert(User1 record);

    User1 selectByPrimaryKey(Integer id);

    //其他方法省略...
}
