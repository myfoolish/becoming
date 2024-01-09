package com.xw.springinaction.soundsystem;

import org.springframework.stereotype.Component;

/**
 * @author liuxiaowei
 * @description
 *              为组件扫描的bean命名：
 *              1、Spring应用上下文中所有的bean都会根据类名为其给定一个ID。具体来讲，也就是将类名的第一个字母变为小写。
 *              如果想为这个bean设置不同的ID，你所要做的就是将期望的ID作为值传递给@Component注解。如：@Component("lonelyHeartsClub")
 *              2、还有另外一种方式，这种方式不使用@Component注解，
 *              而是使用Java依赖注入规范（Java Dependency Injection）中所提供的@Named注解来为bean设置ID。如：@Named("lonelyHeartsClub")
 *              Spring支持将@Named作为@Component注解的替代方案。两者之间有一些细微的差异，但是在大多数场景中，它们是可以互相替换的。
 * @date 2022/1/11
 */
@Component
public class SgtPeppers implements CompactDisc {

    private String title = "Sgt. Pepper's Lonely Hearts Club Band";
    private String artist = "The Beatles";

    @Override
    public void play() {
        System.out.println("Playing " + title + " by " + artist);
    }
}
