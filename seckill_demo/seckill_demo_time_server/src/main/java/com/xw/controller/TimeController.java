package com.xw.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liuxiaowei
 * @description
 * @date 2021/6/11
 */
@RestController
@RequestMapping("/time_server")
public class TimeController {

    @RequestMapping("/getTime")
    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
}
