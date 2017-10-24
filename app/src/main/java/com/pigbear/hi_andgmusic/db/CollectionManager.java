package com.pigbear.hi_andgmusic.db;

import android.util.Log;

import com.pigbear.hi_andgmusic.data.CollectionBean;
import com.pigbear.hi_andgmusic.data.CollectionShipBean;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.db.dao.CollectionDao;
import com.pigbear.hi_andgmusic.db.dao.CollectionShipDao;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 杨阳洋 on 2017/10/24.
 * 收藏夹管理类
 */

public class CollectionManager {

    private static volatile CollectionManager instance;
    private CollectionDao collectionDao;
    private CollectionShipDao collectionShipDao;
    private List<CollectionBean> collectionList;

    public static CollectionManager getInstance(){
        if(instance == null) {
            synchronized (CollectionManager.class){
                if(instance == null) {
                    instance = new CollectionManager();
                }
            }
        }
        return instance;
    }

    public CollectionManager(){
        collectionDao = new CollectionDao();
        collectionShipDao = new CollectionShipDao();
        getAllCollectionsFromDao();
    }

    /**
     * 预先异步从数据库获取数据
     */
    private void getAllCollectionsFromDao() {
        Observable.create(new ObservableOnSubscribe<List<CollectionBean>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<CollectionBean>> e) throws Exception {
                e.onNext(collectionDao.queryAll());
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CollectionBean>>() {
                    @Override
                    public void accept(List<CollectionBean> collectionBeen) throws Exception {
                        collectionList = collectionBeen;
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "getAllCollectionsFromDao + " + throwable);
                    }
                });
    }

    public List<CollectionBean> getCollectionList() {
        List<CollectionBean> collectionBeen = new ArrayList<>();
        if (getAllCollections() != null)
            collectionBeen.addAll(getAllCollections());
        return collectionBeen;
    }

    /**
     * 获取所有收藏夹，若内存中没有数据则直接在UI线程上访问数据库获得数据<br />
     * 原则上是要异步获取数据，这里简化了步骤。
     *
     * @return
     */
    private List<CollectionBean> getAllCollections() {
        if (collectionList != null) {
            collectionList = collectionDao.queryAll();
        }
        if (collectionList == null) {
            collectionList = new ArrayList<>();
        }
        return collectionList;
    }

    /**
     * 设置收藏夹
     *
     * @param bean
     */
    public void setCollection(CollectionBean bean) {
        int index = containCollection(bean);
        if (index < 0) {
            long id = collectionDao.insertCollection(bean);
            bean.setId((int) id);
            getAllCollections().add(bean);
        } else {
            collectionDao.updateCollection(bean);
            getAllCollections().set(index, bean);
        }
    }

    public CollectionBean getCollectionById(int id) {
        for (CollectionBean bean : getAllCollections()) {
            if (bean.getId() == id) {
                return bean;
            }
        }
        return null;
    }

    /**
     * 删除收藏夹
     * @param bean
     */
    public void deleteCollection(CollectionBean bean){
        Observable.just(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<CollectionBean, Integer>() {
                    @Override
                    public Integer apply(@NonNull CollectionBean collectionBean) throws Exception {
                        int index = containCollection(collectionBean);
                        collectionDao.deleteCollection(collectionBean);
                        List<CollectionShipBean> shipBeen = getCollectionShipList(collectionBean.getId());
                        for (CollectionShipBean bean : shipBeen){
                            Song song = SongManager.getInstance().querySong(bean.getSid());
                            if(song != null && song.getDownload() == Song.DOWNLOAD_NONE){
                                SongManager.getInstance().deleteSong(song);
                            }
                            deleteCollectionShip(bean);
                        }
                        return index;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (integer > 0) {
                            getAllCollections().remove((int) integer);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "deleteCollection + " + throwable);
                    }
                });
    }

    /**
     * 当数据库已经存在该收藏夹时，进行更新<br />
     * 当数据库不存在该收藏夹时，进行插入操作
     *
     * @param bean 收藏夹实例
     */
    public void setCollectionAsync(final CollectionBean bean){
        Observable.just(bean)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<CollectionBean>() {
                    @Override
                    public void accept(CollectionBean collectionBean) throws Exception {
                        setCollection(collectionBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("TAG", "setCollectionAsync + " + throwable);
                    }
                });
    }

    /**
     * 是否包含当前收藏夹，是返回位置，否返回-1
     *
     * @param collectionBean
     * @return 0 or -1
     */
    private int containCollection(CollectionBean collectionBean) {
        for (CollectionBean bean : getAllCollections()) {
            if (bean.getId() == collectionBean.getId()) {
                int index = getAllCollections().indexOf(bean);
                return index;
            }
        }
        return -1;
    }

     /*###################################  收藏夹关系操作  ######################################*/

    /**
     * 获取某个收藏夹的所有关系
     *
     * @param cid
     * @return
     */
    public List<CollectionShipBean> getCollectionShipList(int cid) {
        List<CollectionShipBean> cs = new ArrayList<>();
        List<CollectionShipBean> collectionShipBeen = collectionShipDao.queryByCid(cid);
        if (collectionShipBeen != null) {
            cs.addAll(collectionShipBeen);
        }
        return cs;
    }

    public List<CollectionShipBean> getCollectionShip(int cid, int sid) {
        List<CollectionShipBean> cs = new ArrayList<>();
        List<CollectionShipBean> collectionShipBeen = collectionShipDao.queryByCid(cid);
        if (collectionShipBeen != null) {
            cs.addAll(collectionShipBeen);
        }
        return cs;
    }

    /**
     * 收藏歌曲
     *
     * @param bean 收藏夹
     * @param song 歌曲
     * @return
     */
    public long insertCollectionShip(CollectionBean bean, Song song) {
        if(SongManager.getInstance().querySong(song.getId())==null){
            SongManager.getInstance().insertOrUpdateSong(song);
        }
        CollectionShipBean collectionShipBean = new CollectionShipBean(-1, bean.getId(), (int) song.getId());
        long index = collectionShipDao.insertCollectionShip(collectionShipBean);
        if(index > 0){
            bean.setCount(bean.getCount() + 1);
            setCollection(bean);
        }


        return index;
    }

    public void insertCollectionShipAsync(final CollectionBean item, final Song song) {
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                long index = CollectionManager.getInstance().insertCollectionShip(item, song);
                e.onNext(index > 0);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {

                    }
                });
    }

    /**
     * 删除收藏夹的歌曲
     *
     * @param bean
     * @return
     */
    public int deleteCollectionShip(CollectionShipBean bean) {
        CollectionBean collectionBean = getCollectionById(bean.getId());
        if (collectionBean != null && collectionBean.getCount() > 0) {
            collectionBean.setCount(collectionBean.getCount() - 1);
            setCollection(collectionBean);
        }
        return collectionShipDao.deleteCollection(bean.getId());
    }

    public int deleteCollectionShip(int cid, int sid) {
        CollectionBean collectionBean = getCollectionById(cid);
        if (collectionBean != null && collectionBean.getCount() > 0) {
            collectionBean.setCount(collectionBean.getCount() - 1);
            setCollection(collectionBean);
        }
        return collectionShipDao.deleteCollection(cid, sid);
    }

}
