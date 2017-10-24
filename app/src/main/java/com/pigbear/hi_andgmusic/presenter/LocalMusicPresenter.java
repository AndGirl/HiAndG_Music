package com.pigbear.hi_andgmusic.presenter;

import android.content.Context;

import com.pigbear.hi_andgmusic.common.LocalMusicLibrary;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.ui.local.ILocalView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 杨阳洋 on 2017/10/10.
 * usg:本地音乐的Presenter
 */

public class LocalMusicPresenter implements ILocalMusicPresenter{

    private Context mContext;
    private ILocalView.LocalMusic mILocalView;

    public LocalMusicPresenter(Context mContext,ILocalView.LocalMusic mILocalView) {
        this.mContext = mContext;
        this.mILocalView = mILocalView;
    }

    @Override
    public void requestMusic() {
        Observable.create(new ObservableOnSubscribe<List<Song>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Song>> e) throws Exception {
                //从底层获取本地音乐
                List<Song> songs = LocalMusicLibrary.getAllSongs(mContext);
                e.onNext(songs);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Song>>() {
                @Override
                public void accept(List<Song> songs) throws Exception {
                    if (mILocalView != null) {
                        mILocalView.getLocalMusicSuccess(songs);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    mILocalView.getLocalMusicFail(throwable);
                }
            });
    }
}
