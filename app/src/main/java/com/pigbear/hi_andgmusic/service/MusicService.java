package com.pigbear.hi_andgmusic.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.ui.play.MusicRecentPlayList;

/**
 * 音乐播放的服务
 * 1.开启服务--开启服务的帮组类
 * 2.MediaSession框架，专门解决媒体播放时界面和服务通讯问题
 * 3.监听--歌曲切换
 * 4.音乐播放管理
 */
public class MusicService extends Service implements OnSongChangeListener{
    private MediaSessionCompat mediaSession;
    private MusicPlayManager playerManager;
    private PlaybackStateCompat mState;

    public MusicService() {
    }

    public final Binder mBinder = new MyBinder();

    @Override
    public void onSongChanged(Song song) {
        //添加歌曲
        MusicRecentPlayList.getInstance().addPlaySong(song);
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat stateCompat) {

    }

    public class MyBinder extends Binder{
        public MusicService getMusicService(){
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //服务的入口方法
        MediaButtonReceiver.handleIntent(mediaSession,intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 初始化
     */
    public void setUp(){
        playerManager = MusicPlayManager.from(this);
        setUpMediaSession();
    }

    /**
     * 使用MediaButtonReceiver来兼容api21之前的版本
     */
    private void setUpMediaSession(){
        ComponentName mbr = new ComponentName(getPackageName(), MediaButtonReceiver.class.getName());
        mediaSession = new MediaSessionCompat(this,"fd",mbr,null);
        //设置处理media button的flag
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        //设置回调
        mediaSession.setCallback(new MediaSessionCallback());
        setState(PlaybackStateCompat.STATE_NONE);
    }

    /**
     * 设置播放状态
     * @param state
     */
    public void setState(int state){
        mState = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY|
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PAUSE|
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT|
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS|
                        PlaybackStateCompat.ACTION_STOP|
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID|
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH|
                        PlaybackStateCompat.ACTION_SEEK_TO|
                        PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM)
                .setState(state,PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,1.0f, SystemClock.elapsedRealtime())
                .build();

        mediaSession.setPlaybackState(mState);
        mediaSession.setActive(state != PlaybackStateCompat.STATE_NONE &&
                state != PlaybackStateCompat.STATE_STOPPED);
    }

    /***
     * 获取播放状态
     * @return
     */
    public int getState(){
        return mState.getState();
    }

    public class MediaSessionCallback extends MediaSessionCompat.Callback{
        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaSession.release();
    }

    /***
     * 停止服务
     */
    public void stopService() {
        stopSelf();
    }


}
