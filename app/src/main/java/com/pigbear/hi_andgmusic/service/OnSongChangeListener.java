package com.pigbear.hi_andgmusic.service;

import android.support.v4.media.session.PlaybackStateCompat;

import com.pigbear.hi_andgmusic.data.Song;

/**
 * Created by 杨阳洋 on 2017/10/13.
 * usg:歌曲改编的接口监听
 */

public interface OnSongChangeListener {

    //歌曲改编的回调
    void onSongChanged(Song song);
    //歌曲后台改编的回调
    void onPlayBackStateChanged(PlaybackStateCompat stateCompat);

}
