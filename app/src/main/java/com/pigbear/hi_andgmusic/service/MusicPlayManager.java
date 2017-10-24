package com.pigbear.hi_andgmusic.service;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;

import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.ui.play.MusicPlayList;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 杨阳洋 on 2017/10/13.
 * usg:音乐播放功能管理类
 */

public class MusicPlayManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener {

    private Context mContext;
    private MusicService mService;

    private MusicPlayList mMusicPlaylist;//播放列表

    private MediaPlayer mediaPlayer;
    private long currentMediaId = -1;
    private int currentProgress;
    public static final int MAX_DURATION_FOR_REPEAT = 3000;
    private int currentMaxDuration = MAX_DURATION_FOR_REPEAT;
    private ArrayList<OnSongChangeListener> changeListeners = new ArrayList<>();

    private static volatile MusicPlayManager singleton;

    private MusicPlayManager() {

    }

    public static MusicPlayManager getInstance(){
        if(singleton == null) {
            synchronized (MusicPlayManager.class){
                if(singleton == null) {
                    singleton = new MusicPlayManager();
                }
            }
        }
        return singleton;
    }

    /**
     * 是否需要重启服务
     * @param context
     */
    public static void startServiceIfNecessary(Context context) {
        if(getInstance().mService == null) {
            MusicServiceHelper.getInstance(context).initService();
            getInstance().mService = MusicServiceHelper.getInstance(context).getMusicService();
        }
    }

    /***
     * 设置Context和Service
     * @return
     */
    public static MusicPlayManager from(MusicService musicService){
        return MusicPlayManager.getInstance().setContext(musicService).setService(musicService);
    }

    public MusicPlayManager setContext(Context context){
        this.mContext = context;
        return this;
    }

    public MusicPlayManager setService(MusicService service){
        this.mService = service;
        return this;
    }

    /**跟音乐相关的方法**/
    public void playQueue(MusicPlayList musicPlayList,int position){
        this.mMusicPlaylist = musicPlayList;
        mMusicPlaylist.setCurrentPlay(position);
        play(mMusicPlaylist.getCurrentPlay());
    }

    /**
     * 根据下标播放已有列表歌曲
     * @param position
     */
    public void playQueueItem(int position){
        mMusicPlaylist.setCurrentPlay(position);
        play(mMusicPlaylist.getCurrentPlay());
    }

    /**
     * 直接播放
     */
    public void play(){
        if(mMusicPlaylist == null || mMusicPlaylist.getCurrentPlay() == null) {
            return;
        }
        play(mMusicPlaylist.getCurrentPlay());
    }

    private void play(Song song){
        if(song == null) {
            return;
        }
        boolean musicHasChanged = !(song.getId() == currentMediaId);
        if(musicHasChanged) {//当前是两首歌
            currentProgress = 0;
            currentMediaId = song.getId();
        }
        if(mService.getState() == PlaybackStateCompat.STATE_PAUSED && !musicHasChanged && mediaPlayer != null) {
            configMediaPlayerState();
        }else{
            try {
                createMediaPlayerIfNeed();
                mService.setState(PlaybackStateCompat.STATE_PLAYING);
                mediaPlayer.setDataSource(mContext,song.getUri());
                mediaPlayer.prepareAsync();
                //歌曲切换的监听
                for (OnSongChangeListener l : changeListeners){
                    l.onSongChanged(song);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createMediaPlayerIfNeed() {
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);

            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnSeekCompleteListener(this);
            mediaPlayer.setOnErrorListener(this);
        }else{
            mediaPlayer.reset();
        }
    }

    private void configMediaPlayerState() {
        if(mediaPlayer != null && !mediaPlayer.isPlaying()) {
            if(currentProgress == mediaPlayer.getCurrentPosition()) {
                mediaPlayer.start();
                mService.setState(PlaybackStateCompat.STATE_PLAYING);
            }else{
                mediaPlayer.seekTo(currentProgress);
                mediaPlayer.start();
                mService.setState(PlaybackStateCompat.STATE_PLAYING);
            }
        }
    }

    public final static int CYCLETYPE = 0; //循环
    public final static int SINGLETYPE = 1;//单曲
    public final static int RANDOMTYPE = 0;//随机
    private int currentPlayType = CYCLETYPE;

    public void switchPlayMode(){
        if (currentPlayType == CYCLETYPE) {
            setPlayMode(CYCLETYPE);
        } else if (currentPlayType == SINGLETYPE) {
            setPlayMode(SINGLETYPE);
        } else if (currentPlayType == RANDOMTYPE) {
            setPlayMode(RANDOMTYPE);
        }
    }

    /**
     * 设置播放模式
     * @param type
     */
    private void setPlayMode(int type){
        if (type < 0 || type > 2) {
            throw new IllegalArgumentException("incorrect type");
        }
        createMediaPlayerIfNeed();
        currentPlayType = type;
        if(type == SINGLETYPE) {
            mediaPlayer.setLooping(true);
        }else{
            mediaPlayer.setLooping(false);
        }
    }

    public int getPlayMode() {
        return currentPlayType;
    }

    /**
     * 播放下一首
     */
    public void playNext(){
        currentProgress = 0;

        play(mMusicPlaylist.getNextSong());
    }

    /**
     * 播放上一首歌
     */
    public void playPre(){
        currentProgress = 0;
        play(mMusicPlaylist.getPreSong());
    }

    /**
     * 暂停
     */
    public void playPause(){
        if(mService.getState() == PlaybackStateCompat.STATE_PLAYING) {
            if(mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                currentProgress = mediaPlayer.getCurrentPosition();
                mService.setState(PlaybackStateCompat.STATE_PAUSED);
            }
        }
    }

    /**
     * 恢复播放
     */
    public void resume(){
        if (mService.getState() == PlaybackStateCompat.STATE_PAUSED
                && mediaPlayer != null) {

            mediaPlayer.start();
            mService.setState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    /**
     * 清空播放列表
     */
    public void clearPlayer(){
        if(getMusicPlayList() != null) {
            getMusicPlayList().clear();
        }
        mService.setState(PlaybackStateCompat.STATE_STOPPED);
        if(mediaPlayer != null) {
            mediaPlayer.reset();
        }
    }

    /**
     * 停止
     */
    public void stop(){
        mService.setState(PlaybackStateCompat.STATE_STOPPED);
        currentProgress = getCurrentProgressInSong();
        mService.stopService();
    }

    /**
     * 进行拖拽
     */
    public void seekTo(int progress){
        if(mediaPlayer == null) {
            currentProgress = progress;
        }else{
            if(getCurrentProgressInSong() > progress){
                mService.setState(PlaybackStateCompat.STATE_REWINDING);
            }else{
                mService.setState(PlaybackStateCompat.STATE_FAST_FORWARDING);
            }
            currentProgress = progress;
            mediaPlayer.seekTo(currentProgress);
        }
    }

    private MusicPlayList getMusicPlayList(){
        return  mMusicPlaylist;
    }

    /**
     * 获取当前歌曲播放进度
     * @return
     */
    private int getCurrentProgressInSong(){
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : currentProgress;
    }

    /****
     * 获取当前播放歌曲
     *
     * @return
     */
    public Song getPlayingSong() {
        if (mMusicPlaylist != null)
            return mMusicPlaylist.getCurrentPlay();
        else return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //在这个方法确定播放歌曲
        currentMaxDuration = mediaPlayer.getDuration();
        configMediaPlayerState();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(!mediaPlayer.isLooping()) {
            currentProgress = 0;
            if(mMusicPlaylist.getQueue().size() == 0) {
                return;
            }
            playNext();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    public int getCurrentMaxDuration() {
        return currentMaxDuration;
    }

    /**
     * 当前播放进度
     * @return
     */
    public int getCurrentPosition() {
        if(mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /***
     * 获取当前状态
     * @return
     */
    public int getState() {
        if (mService != null)
            return mService.getState();
        return PlaybackStateCompat.STATE_STOPPED;
    }

    public void registerListener(OnSongChangeListener listener){
        changeListeners.add(listener);
    }

    public void unRegisterListener(OnSongChangeListener listener){
        changeListeners.remove(listener);
    }

    /**
     * 得到MediaPlayer对象
     * @return
     */
    public MediaPlayer getMediaPlayer() {
        if(mediaPlayer != null) {
            return mediaPlayer;
        }else
            return null;
    }

    /***
     * 获取播放列表
     *
     * @return
     */
    public MusicPlayList getMusicPlaylist() {
        return mMusicPlaylist;
    }

    /**
     * 获取服务
     * @return
     */
    public MusicService getmService() {
        return mService;
    }

}
