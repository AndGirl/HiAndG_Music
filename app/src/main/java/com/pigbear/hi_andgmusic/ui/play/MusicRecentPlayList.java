package com.pigbear.hi_andgmusic.ui.play;

import com.pigbear.hi_andgmusic.MusciApplication;
import com.pigbear.hi_andgmusic.common.ACache;
import com.pigbear.hi_andgmusic.data.Song;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 杨阳洋 on 2017/10/16.
 * usg:最近播放的列表:最多记录100首
 */

public class MusicRecentPlayList {


    private ArrayList<Song> queue;
    private int RECENT_COUNT = 100;
    private String PLAY_QUEUE = "song_queue";
    private static volatile MusicRecentPlayList instance;

    private MusicRecentPlayList(){
        queue = readQueueFromFileCache();
    }

    public static MusicRecentPlayList getInstance(){
        if(instance == null) {
            synchronized (MusicRecentPlayList.class){
                if(instance == null) {
                    instance = new MusicRecentPlayList();
                }
            }
        }
        return instance;
    }

    public ArrayList<Song> getQueue() {
        return queue;
    }

    public void clearRecentPlayList(){
        queue.clear();
        ACache.get(MusciApplication.getInstance(), PLAY_QUEUE).remove(PLAY_QUEUE);
    }

    //歌曲添加
    public void addPlaySong(Song song){
        queue.add(0,song);
        for (int i = queue.size() - 1 ; i > 0 ; i--){
            //醉经播放歌曲有数量限制
            if(i >= RECENT_COUNT) {
                queue.remove(i);
                continue;
            }
            //歌曲不能重复
            if(song.getId() == queue.get(i).getId()) {
                queue.remove(i);
                break;
            }
        }

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {
                adddQueueToFileCache();
            }
        }).subscribeOn(Schedulers.io())
        .subscribe();
    }

    /**
     * 将播放列表缓存到文件中,方便下次读取
     */
    private void adddQueueToFileCache(){
        ACache.get(MusciApplication.getInstance(),PLAY_QUEUE).put(PLAY_QUEUE,queue);
    }

    /**
     * 歌曲的读取
     * @return
     */
    private ArrayList<Song> readQueueFromFileCache(){
        Object object = ACache.get(MusciApplication.getInstance(), PLAY_QUEUE).getAsObject(PLAY_QUEUE);
        if(object != null && object instanceof ArrayList) {
            return (ArrayList<Song>) object;
        }
        return new ArrayList<>();
    }

}
