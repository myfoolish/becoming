package com.xw.controller;

import com.xw.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/7/14
 */
@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @RequestMapping(value = "/getStockStorage/{sku_id}")
    public Map<String, Object> getStockStorage(@PathVariable("sku_id") String sku_id){
        return storageService.getStockStorage(sku_id);
    }
}
