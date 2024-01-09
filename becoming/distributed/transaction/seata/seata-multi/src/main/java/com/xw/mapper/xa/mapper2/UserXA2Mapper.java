package com.xw.mapper.xa.mapper2;

import org.apache.ibatis.annotations.Update;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/22
 */
public interface UserXA2Mapper {
    @Update("UPDATE user2 SET money = money + #{money} WHERE id = 1")
    void updateUser2(double money);
}
