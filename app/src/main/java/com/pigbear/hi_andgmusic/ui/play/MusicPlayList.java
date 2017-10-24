package com.pigbear.hi_andgmusic.ui.play;

import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 杨阳洋 on 2017/10/13.
 * usg:播放列表
 * 对歌曲存放，查找
 */

public class MusicPlayList {

    private Song curSong;
    private long albumID;
    private String title;
    private List<Song> queue;

    public MusicPlayList(List<Song> listSong) {
        setQueue(listSong);
    }

    public MusicPlayList() {
        queue = new ArrayList<Song>();
    }

    /**
     * 设置队列
     * @param queue
     */
    public void setQueue(List<Song> queue) {
        this.queue = queue;
        setCurrentPlay(0);
    }

    /**
     * 设置当前播放歌曲
     * @param position
     * @return
     */
    public long setCurrentPlay(int position){
        if(queue.size() > position && position >= 0) {
            curSong = queue.get(position);
            return curSong.getId();
        }
        return -1;
    }

    /**
     * 获取当前播放歌曲
     */
    public Song getCurrentPlay(){
        if(curSong == null) {
            setCurrentPlay(0);
        }
        return curSong;
    }

    public List<Song> getQueue(){
        return queue;
    }

    /**
     * 添加歌曲队列
     * @param songs
     * @param head
     */
    public void addQueue(List<Song> songs,boolean head){
        if(!head) {
            queue.addAll(songs);
        }else{
            queue.addAll(0,songs);
        }
    }

    /**
     * 添加歌曲
     * @param song
     */
    public void addSong(Song song){
        queue.add(song);
    }

    /***
     * 添加歌曲,带位置
     * @param song
     * @param position
     */
    public void addSong(Song song,int position){
        queue.add(position,song);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }

    /**
     * 获取队列中下一首歌
     * @return
     */
    public Song getNextSong() {
        int currentPos = queue.indexOf(curSong);
        //播放模式
        int playMode = MusicPlayManager.getInstance().getPlayMode();
        if(playMode == MusicPlayManager.CYCLETYPE || playMode == MusicPlayManager.SINGLETYPE) {
            if(++currentPos == queue.size()) {
                currentPos = 0;
            }
        }else{
            currentPos = new Random().nextInt(queue.size());
        }
        curSong = queue.get(currentPos);
        return curSong;
    }

    /**
     * 获取队列中上一首歌曲
     */
    public Song getPreSong(){
        int currentPos = queue.indexOf(curSong);
        //播放模式
        int playMode = MusicPlayManager.getInstance().getPlayMode();
        if(playMode == MusicPlayManager.CYCLETYPE || playMode == MusicPlayManager.SINGLETYPE) {
            if(--currentPos == -1) {
                currentPos = queue.size() - 1;
            }
        }else{
            currentPos = new Random().nextInt(queue.size());
        }
        curSong = queue.get(currentPos);
        return curSong;
    }

    /**
     * 清楚列表当前歌曲
     */
    public void clear(){
        queue.clear();
        curSong = null;
    }

}
