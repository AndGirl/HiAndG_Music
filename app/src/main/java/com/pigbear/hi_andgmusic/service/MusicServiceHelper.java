package com.pigbear.hi_andgmusic.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by 杨阳洋 on 2017/10/13.
 * usg:服务帮组类
 */

public class MusicServiceHelper {

    private Context mContext;

    MusicService musicService;

    private static volatile MusicServiceHelper singleton;

    private MusicServiceHelper() {

    }

    public static MusicServiceHelper getInstance(Context context){
        if(singleton == null) {
            synchronized (MusicServiceHelper.class){
                if(singleton == null) {
                    singleton = new MusicServiceHelper();
                }
            }
        }
        singleton.mContext = context;
        return singleton;
    }

    /**
     * 服务初始化
     */
    public void initService(){
        if(musicService == null) {
            Intent intent = new Intent(mContext, MusicService.class);
            ServiceConnection conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MusicService.MyBinder binder = (MusicService.MyBinder) service;
                    musicService = binder.getMusicService();
                    musicService.setUp();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            mContext.startService(intent);
            mContext.bindService(intent,conn,Context.BIND_AUTO_CREATE);
        }
    }

    public MusicService getMusicService(){
        return musicService;
    }

}
