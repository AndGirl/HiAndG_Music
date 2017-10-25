package com.pigbear.hi_andgmusic.common;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {
    private final Subject<Object> mBus = PublishSubject.create();
    private ConcurrentHashMap<Object, List<Disposable>> mSubjects = new ConcurrentHashMap<>();

    private RxBus() {
    }

    public static class RxBusHolder {
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
    public  <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
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
        List<Disposable> disposables = mSubjects.get(subscribe);
        if (disposables == null) {
            disposables = new ArrayList<>();
            mSubjects.put(subscribe, disposables);

        } else {
            Observable<T> tObservable = toObservable(event);
            Disposable disposable = tObservable.subscribe(action, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                }
            });
            disposables.add(disposable);
        }
    }


    /**
     * 注销事件
     *
     * @param subscribe 订阅者
     */
    public void unRegister(Object subscribe) {
        List<Disposable> disposables = mSubjects.get(subscribe);
        if (disposables != null) {
            if (!disposables.isEmpty()) {
                for (Disposable disposable : disposables) {
                    disposable.dispose();
                }
            } else {
                mSubjects.remove(subscribe);
            }
        }
    }
}