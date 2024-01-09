package com.xw.service;

import com.xw.entity.Items;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
public interface ItemsService {

    public Items findItemById(Long itemId);
    public void reduceStock(int num, Long itemId);
}
