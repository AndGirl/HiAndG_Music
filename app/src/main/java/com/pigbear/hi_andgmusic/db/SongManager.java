package com.pigbear.hi_andgmusic.db;

import android.text.TextUtils;
import android.util.Log;

import com.pigbear.hi_andgmusic.common.FileUtils;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.db.dao.SongDao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * @desciption: 歌曲管理器，单例
 */
public class SongManager {
    private static SongManager instance;
    private Map<Long, Song> songLibrary;
    private SongDao songDao;

    public static SongManager getInstance() {
        if (instance == null) {
            instance = new SongManager();
        }
        return instance;
    }

    public SongManager() {
        songDao = new SongDao();
        songLibrary = new LinkedHashMap<>();
        updateSongLibrary();
    }

    /**
     * 异步更新歌曲信息
     */
    private void updateSongLibrary() {
        Observable.create(new ObservableOnSubscribe<List<Song>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Song>> e) throws Exception {
                e.onNext(songDao.queryAll());
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .concatMap(new Function<List<Song>, ObservableSource<Song>>() {
                    @Override
                    public ObservableSource<Song> apply(@NonNull List<Song> songs) throws Exception {
                        return Observable.fromIterable(songs);
                    }
                })
                .map(new Function<Song, Song>() {
                    @Override
                    public Song apply(@NonNull Song song) throws Exception {
                        if (song.getDownload() == Song.DOWNLOAD_COMPLETE && !TextUtils.isEmpty(song.getPath())) {
                            if (!FileUtils.existFile(song.getPath())) {
                                song.setDownload(Song.DOWNLOAD_NONE);
                                insertOrUpdateSong(song);
                            }
                        }
                        return song;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Song>() {
                    @Override
                    public void accept(Song song) throws Exception {
                        songLibrary.put(song.getId(), song);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "SongManager + " + throwable);
                    }
                });
    }

    public Song querySong(long sid) {
        if (songLibrary.containsKey(sid)) {
            return songLibrary.get(sid);
        }
        return null;
    }

    public void updateSongFromLibrary(Song song) {
        if (songLibrary.containsKey(song.getId())) {
            Song cacheSong = songLibrary.get(song.getId());
            song.setDownload(cacheSong.getDownload());
            song.setPath(cacheSong.getPath());
        }
    }

    /**
     * 添加或者更新歌曲
     * @param song
     */
    public void insertOrUpdateSong(final Song song) {
        Observable.create(new ObservableOnSubscribe<Song>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Song> e) throws Exception {
                updateSongFromLibrary(song);
                songDao.insertOrUpdateSong(song);
                e.onNext(song);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Song>() {
                    @Override
                    public void accept(Song song) throws Exception {
                        songLibrary.put(song.getId(), song);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "insertOrUpdateSong + " + throwable);
                    }
                });
    }

    /**
     * 删除数据库的歌曲信息，包括下载中的temp缓存和已经下载完的歌曲
     *
     * @param song 歌曲信息
     */
    public void deleteSong(final Song song) {
          Observable.create(new ObservableOnSubscribe<Song>() {
              @Override
              public void subscribe(@NonNull ObservableEmitter<Song> e) throws Exception {
                  if(songLibrary.containsKey(song.getId())) {
                      songDao.deleteSong(song);
                  }
                  e.onNext(song);
                  e.onComplete();
              }
          }).subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Consumer<Song>() {
                      @Override
                      public void accept(Song song) throws Exception {
                          songLibrary.remove(song.getId());
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          Log.e("TAG", "deleteSong + " + throwable);
                      }
                  });
    }



}
