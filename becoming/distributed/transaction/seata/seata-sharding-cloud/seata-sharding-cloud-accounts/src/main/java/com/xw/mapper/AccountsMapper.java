package com.xw.mapper;

import com.xw.entity.Accounts;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
public interface AccountsMapper {

    @Select("select * from accounts where id = #{accountId}")
    public Accounts findAccountById(Long accountId);

    /**
     * 扣款
     * @param money 钱
     * @param accountId 帐户id
     */
    @Update("update accounts set account_money = account_money - #{money} where id = #{accountId}")
    public void reduceMoney(@Param("money") double money, @Param("accountId") Long accountId);
}
