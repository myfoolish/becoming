package com.xw.service;

import com.xw.mapper.DistributeLockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/7/6
 */
@Service
public class DistributeLockService {

    @Autowired
    private DistributeLockMapper distributeLockMapper;
    
}
