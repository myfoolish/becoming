package com.xw.springinaction.knights;

import java.io.PrintStream;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/1/10
 */
public class SlayDragonQuest implements Quest{

    private PrintStream stream;

    public SlayDragonQuest(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void embark() {
        stream.println("Embarking on quest to slay the dragon");
    }
}
