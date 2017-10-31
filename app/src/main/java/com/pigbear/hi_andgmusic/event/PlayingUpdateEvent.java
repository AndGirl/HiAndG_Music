package com.pigbear.hi_andgmusic.event;

/**
 * 圆盘转动
 */
public class PlayingUpdateEvent {
    private boolean isPlaying;

    public PlayingUpdateEvent(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
}