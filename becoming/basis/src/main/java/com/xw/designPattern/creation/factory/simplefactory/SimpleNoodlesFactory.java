package com.xw.designPattern.creation.factory.simplefactory;

import com.xw.designPattern.creation.factory.noodles.BanNoodles;
import com.xw.designPattern.creation.factory.noodles.HuiNoodles;
import com.xw.designPattern.creation.factory.noodles.Noodles;
import com.xw.designPattern.creation.factory.noodles.RegNoodles;

/**
 * @author liuxiaowei
 * @description 简单工厂模式【一家“简单面馆”（简单工厂类）】
 * @date 2023/6/16
 */
public class SimpleNoodlesFactory {

    public static final int TYPE_HUI_NOODLES = 1; // 河南烩面
    public static final int TYPE_BAN_NOODLES = 2; // 安徽板面
    public static final int TYPE_REG_NOODLES = 3; // 武汉热干面

    /**
     * 如果生产对象的方法是static的，这种简单工厂也叫做静态工厂
     * 如果生产对象的方法不是static的，这种简单工厂也叫做实例工厂
     * @param type
     * @return
     */
    public static Noodles createNoodles(int type) {
//    public Noodles createNoodles(int type) {
        switch (type) {
            case 1:
                return new HuiNoodles();
            case 2:
                return new BanNoodles();
            case 3:
                return new RegNoodles();
            default:
                return new HuiNoodles();
        }
    }

    public static void main(String[] args) {
        /**
         *  一开始，穷，想吃面必须得自己做
         *  想吃烩面得自己做，new HuiNoodles()
         *  想吃板面得自己做，new BanNoodles()
         *  想吃热干面得自己做，new RegNoodles()
         */

        // 做烩面
        Noodles huiNoodles = new HuiNoodles();
        // 做板面
        Noodles banNoodles = new BanNoodles();
        // 做热干面
        Noodles regNoodles = new RegNoodles();
// ==================分界线=============================
        /**
         * new来new去，改来改去，好心烦...
         * 忽然，有一天走了狗屎运成了暴发户
         * 幸福生活从此来临，吃面从此变得简单
         * 给面馆说一声想吃啥，面馆做好了给自己就好了
         * 自己不必关心面条怎么做（怎么new，如何new）让面馆操心去吧（面馆帮我们new）！
         */

        Noodles iNoodles = SimpleNoodlesFactory.createNoodles(1);  // 和具体的对象脱离关系
        iNoodles.desc();
    }
}
