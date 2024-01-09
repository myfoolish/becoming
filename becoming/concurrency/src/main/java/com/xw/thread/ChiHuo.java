package com.xw.thread;

/**
 * @author liuxiaowei
 * @description
 * @date 2020/8/4 11:07
 */
public class ChiHuo {
    public static final BaoZi baoZi = new BaoZi();

    public static void main(String[] args) {
        // 吃货线程
        new Thread(() -> {
            while (true) {
                synchronized (baoZi) {
                    if (!baoZi.flag) {
                        try {
                            baoZi.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("吃货正在吃" + baoZi.pier + baoZi.xianer + "包子");
                    baoZi.flag = false;
                    baoZi.notify();
                }
            }
        }, "吃货").start();

        new Thread(() -> {
            int count = 0;
            while (true) {
                synchronized (baoZi) {
                    if (baoZi.flag) {
                        try {
                            baoZi.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    // 没有包子 造包子
                    System.out.println("包子铺开始做包子");
                    if (count % 2 == 0) {
                        // 冰皮 五仁
                        baoZi.pier = "冰皮";
                        baoZi.xianer = "五仁";
                    } else {
                        // 薄皮 牛肉大葱
                        baoZi.pier = "薄皮";
                        baoZi.xianer = "牛肉大葱";
                    }
                    count++;
                    baoZi.flag = true;
                    System.out.println("包子造好了：" + baoZi.pier + baoZi.xianer);
                    System.out.println("吃货来吃吧");
                    //唤醒等待线程 （吃货）
                    baoZi.notify();
                }
            }
        }, "包子铺").start();
    }

    /**
     * @author liuxiaowei
     * @description 包子
     * @date 2020/8/4 10:49
     */
    public static class BaoZi {
        String pier;
        String xianer;
        boolean flag = false;   //包子状态
    }
}
