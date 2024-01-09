package com.xw.controller;

import com.xw.entity.Items;
import com.xw.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/25
 */
@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemsService itemsService;

    @GetMapping("findItemById/{itemId}")
    public Items findItemById(@PathVariable("itemId") Long itemId) {
        return itemsService.findItemById(itemId);
    }

    @PutMapping("reduceStock")
    public String reduceStock(int num, Long itemId) {
        itemsService.reduceStock(num, itemId);
        return "库存扣减成功";
    }
}
