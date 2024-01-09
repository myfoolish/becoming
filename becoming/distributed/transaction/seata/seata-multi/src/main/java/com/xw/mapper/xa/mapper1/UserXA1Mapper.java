package com.xw.mapper.xa.mapper1;

import org.apache.ibatis.annotations.Update;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/22
 */
public interface UserXA1Mapper {
    @Update("UPDATE user1 SET money = money + #{money} WHERE id = 1")
    void updateUser1(double money);
}
