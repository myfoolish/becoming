package com.xw.springinaction.soundsystem;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/1/13
 */
public class Discography implements CompactDisc{

    private String artist;
    private List<CompactDisc> cds;

    public Discography(String artist, List<CompactDisc> cds) {
        this.artist = artist;
        this.cds = cds;
    }

    @Override
    public void play() {

    }
}
