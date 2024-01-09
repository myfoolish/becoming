package com.xw.service.impl;

import com.xw.entity.Items;
import com.xw.mapper.ItemsMapper;
import com.xw.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@Service
public class ItemsServiceImpl implements ItemsService {
    @Autowired
    private ItemsMapper itemsMapper;


    @Override
    public Items findItemById(Long itemId) {
        return itemsMapper.findItemById(itemId);
    }

    @Override
    public void reduceStock(int num, Long itemId) {
        Items items = itemsMapper.findItemById(itemId);
        if (items.getItemStock() < num) {
            throw new RuntimeException("库存不足");
        }
        itemsMapper.reduceStock(num, itemId);
    }
}
