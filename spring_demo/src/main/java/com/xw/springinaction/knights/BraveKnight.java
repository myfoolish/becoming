package com.xw.springinaction.knights;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/1/10
 */
public class BraveKnight implements Knight{

    private Quest quest;

    public BraveKnight(Quest quest) {   // Quest 被注入进来
        this.quest = quest;
    }

    @Override
    public void embark() {
        quest.embark();
    }
}
