package com.xw.entity;

import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/7/15 17:44
 */
@Data
@ToString
//@EqualsAndHashCode
public class Person {
    private String name;
    private int age;

    @Override
    public boolean equals(Object o) {
        // 如果对象地址一样，则认为相同
        if (this == o) return true;
        // 如果参数为空，或者类型信息不一样，则认为不同
        if (o == null || getClass() != o.getClass()) return false;
        // 转换为当前类型
        Person person = (Person) o;
        // 要求基本类型相等，并且将引用类型交给java.util.Objects类的equals静态方法取用结果
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}
