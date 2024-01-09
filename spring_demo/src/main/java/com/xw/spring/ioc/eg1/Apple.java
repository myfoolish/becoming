package com.xw.spring.ioc.eg1;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liuxiaowei
 * @description
 * @date 2023/12/2
 */
@Data
public class Apple {
    private String title;
    private String color;
    private String origin;

    public Apple() {
        System.out.println("默认基于无参构造创建对象");
    }

    public Apple(String title, String color, String origin) {
//        System.out.println("基于有参构造创建对象");
        this.title = title;
        this.color = color;
        this.origin = origin;
    }
}
