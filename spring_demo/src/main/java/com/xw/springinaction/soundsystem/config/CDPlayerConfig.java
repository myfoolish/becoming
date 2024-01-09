package com.xw.springinaction.soundsystem.config;

import com.xw.springinaction.soundsystem.CDPlayer;
import com.xw.springinaction.soundsystem.CompactDisc;
import com.xw.springinaction.soundsystem.SgtPeppers;
import org.springframework.context.annotation.*;

/**
 * @author liuxiaowei
 * @description @ComponentScan 启用组件扫描。
 *              设置组件扫描的基础包：（Spring 将会扫描这个包以及这个包下的所有子包，查找带有 @Component 注解的类）
 *              1、如果没有其他配置的话，即单独使用 @ComponentScan，按照默认规则，它会以配置类所在的包作为基础包（base package）来扫描组件。
 *              2、扫描不同基础包。在@ComponentScan的value属性中指明包的名称
 *              如：@ComponentScan(value = "com.xw.springinaction.soundsystem") 等价于 @ComponentScan("com.xw.springinaction.soundsystem")
 *              3、扫描多个基础包。在@ComponentScan的basePackages属性中指明包的名称
 *              如：@ComponentScan(basePackages = "com.xw.springinaction.soundsystem")
 * 或者设置多个基础包：@ComponentScan(basePackages = {"com.xw.springinaction.soundsystem", "com.xw.springinaction.knights"})
 * 上述三种配置所设置的基础包是以String类型表示的，但这种方法是类型不安全（not type-safe）的。除了将包设置为简单的String类型之外，@ComponentScan还提供了另外一种方法，那就是将其指定为包中所包含的类或接口
 *              4、扫描多个基础包。在@ComponentScan的 basePackageClasses 属性中指明包所包含的类或接口
 *              如：@ComponentScan(basePackageClasses = {SgtPeppers.class,SgtPeppers.class})
 *
 *              1、在JavaConfig中引用JavaConfig   @Import(CDPlayerConfig.class)
 *              2、在JavaConfig中引用XML配置   @ImportResource("classpath:soundsystem.xml")
 * @date 2022/1/11
 */
@Configuration
//@ComponentScan("com.xw.springinaction.soundsystem")
public class CDPlayerConfig {

    @Bean
    public CompactDisc sgtPeppers() {
        return new SgtPeppers();
    }

    @Bean
    public CDPlayer cdPlayer() {
        return new CDPlayer(sgtPeppers());
    }
}
