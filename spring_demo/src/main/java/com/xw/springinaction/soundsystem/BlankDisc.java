package com.xw.springinaction.soundsystem;

import java.util.List;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/1/13
 */
public class BlankDisc implements CompactDisc{

    // 唱片名称
    private String title;
    // 艺术家
    private String artist;
    // 磁道列表
    public List<String> tracks;

    // 将字面量注入到构造器中
    public BlankDisc(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    // 将集合注入到构造器中
    public BlankDisc(String title, String artist, List<String> tracks) {
        this.title = title;
        this.artist = artist;
        this.tracks = tracks;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }

    @Override
    public void play() {
        System.out.println("Playing " + title + " by " + artist);
        for (String track : tracks) {
            System.out.println("-Tracks" + track);
        }
    }
}
