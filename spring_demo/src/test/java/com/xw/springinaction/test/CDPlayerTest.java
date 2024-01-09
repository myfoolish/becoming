package com.xw.springinaction.test;

import com.xw.springinaction.soundsystem.CompactDisc;
import com.xw.springinaction.soundsystem.MediaPlayer;
import com.xw.springinaction.soundsystem.config.CDPlayerConfig;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author liuxiaowei
 * @description
 * @date 2022/1/11
 */
@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = {CDPlayerConfig.class})
@ContextConfiguration(locations = "classpath:soundsystem.xml")
public class CDPlayerTest {

    @Rule
    public final SystemOutRule log = new SystemOutRule().enableLog();

    @Autowired
    private CompactDisc cd;

    @Autowired
    private MediaPlayer player;

    @Test
    public void cdShouldNotBeNull() {
        assertNotNull(cd);
    }

    @Test
    public void play() {
        player.play();
        assertEquals("Playing Sgt. Pepper's Lonely Hearts Club Band " + "by The Beatles\n",log.getLog());
    }
}
