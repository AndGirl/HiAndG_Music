package com.pigbear.hi_andgmusic.presenter;

import android.content.Context;

import com.pigbear.hi_andgmusic.ui.local.ILocalView;

/**
 * Created by 杨阳洋 on 2017/10/30.
 * usg:
 */

public class LocalAlbumPresenter implements ILocalAlbumPresenter {

    private Context mContext;
    private ILocalView.LocalAlbum mILocalAlbum;

    public LocalAlbumPresenter(Context mContext, ILocalView.LocalAlbum mILocalAlbum) {
        this.mContext = mContext;
        this.mILocalAlbum = mILocalAlbum;
    }

    @Override
    public void requestLocalAlbum() {
//        Observable.create(new ObservableOnSubscribe<List<Album>>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<List<Album>> e) throws Exception {
//                List<Album> allAlbums = LocalMusicLibrary.getAllAlbums(mContext);
//                e.onNext(allAlbums);
//                e.onComplete();
//            }
//        }).subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Consumer<List<Album>>() {
//            @Override
//            public void accept(List<Album> alba) throws Exception {
//                if (mILocalAlbum != null) {
//                    mILocalAlbum.getLocalAlbumSuccess(alba);
//                }
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                mILocalAlbum.getLocalAlbumFail(throwable);
//            }
//        });
    }
}
