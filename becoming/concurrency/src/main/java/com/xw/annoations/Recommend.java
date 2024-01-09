package com.xw.annoations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liuxiaowei
 * @description 用来标识【推荐】的类
 * @date 2023/3/16
 */
@Target(ElementType.TYPE)   // 此注解作用的目标，期望对一个推荐类加上此注解做标识
@Retention(RetentionPolicy.SOURCE)  // 注解存在的范围，
                        // SOURCE：代表注解在编译时会被忽略掉，因此注解仅做标识，故使用此属性
                        // CLASS：代表注解会在class字节码中存在，在运行时通过反射不可以拿到
                        // RUNTIME：代表注解会在class字节码中存在，在运行时通过反射可以拿到
public @interface Recommend {

    String value() default "";
}
