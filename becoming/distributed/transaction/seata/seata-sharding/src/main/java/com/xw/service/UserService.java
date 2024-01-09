package com.xw.service;

import com.xw.entity.User;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/22
 */
public interface UserService {

    public void become(User user);

    public void becomingXA(Integer fromId, Integer toId, double money);

    public void becomingAT(Integer fromId, Integer toId, double money);
}
