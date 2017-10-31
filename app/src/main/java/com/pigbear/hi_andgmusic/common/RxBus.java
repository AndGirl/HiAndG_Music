package com.pigbear.hi_andgmusic.common;


import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private final Subject<Object> mBus = PublishSubject.create().toSerialized();
    private ConcurrentHashMap<String, CompositeDisposable> mSubjects = new ConcurrentHashMap<>();

    private RxBus() {
    }

    private static class RxBusHolder {
        private static final RxBus INSTANCE = new RxBus();
    }

    public static RxBus getDefault() {
        return RxBusHolder.INSTANCE;
    }

    /**
     * 发送一个事件
     */
    public void post(Object o) {
        mBus.onNext(o);
    }

    /**
     * 根据传递的 eventType 类型返回特定类型的(eventType)的 被观察者
     */
    private <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }


    /**
     * 订阅
     *
     * @param eventType 事件类型
     * @param action
     * @param error
     * @param <T>
     * @return
     */
    public <T> Disposable doSubscribe(Class<T> eventType, Consumer<T> action, Consumer<Throwable> error) {
        return toObservable(eventType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action, error);
    }

    /**
     * 保存订阅后的disposable
     */
    public void addDisposable(Object object, Disposable disposable) {
        String key = object.getClass().getName();
        if (mSubjects.get(key) != null) {
            mSubjects.get(key).add(disposable);
        } else {
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            mSubjects.put(key, disposables);
        }
    }

    /**
     * 处理注册事件
     *
     * @param event     事件
     * @param action    处理方式
     * @param subscribe 订阅者
     * @param <T>       事件类型泛型
     */
    public <T> void register(Object subscribe, Class<T> event, Consumer<T> action) {
        Disposable disposable = doSubscribe(event, action, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
            }
        });
        addDisposable(subscribe, disposable);
    }


    /**
     * 注销监听事件
     *
     * @param subscribe 订阅者
     */
    public void unRegister(Object subscribe) {
        if (mSubjects != null) {
            String key = subscribe.getClass().getName();
            if (mSubjects.containsKey(key) && mSubjects.get(key) != null) {
                mSubjects.get(key).dispose();
            }
            mSubjects.remove(key);
        }
    }
}