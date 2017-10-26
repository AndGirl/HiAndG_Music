package com.pigbear.hi_andgmusic.net;

import com.pigbear.hi_andgmusic.common.SettingManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 杨阳洋 on 2017/10/26.
 * Retrofit初始化
 */

public class RetrofitManager {

    private static RetrofitManager instance;
    private Retrofit retroft,fmRetrofit;
    private String accessToken;
    private String accessTokenSecret;
    private String baseUrl = Constant.BASE_URL;

    public static RetrofitManager getInstance(){
        if(instance == null) {
            synchronized (RetrofitManager.class){
                if(instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    private RetrofitManager(){}

    private void build(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retroft = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .build();
    }

    private void buildFM() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(Constant.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        fmRetrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constant.BASE_FM_URL)
                .build();
    }

    public Retrofit getRetroft(){
        if(retroft == null) {
            build();
        }
        return retroft;
    }

    public Retrofit getFMRetrofit() {
        if (fmRetrofit == null)
            buildFM();
        return fmRetrofit;
    }

    public String getAccessToken() {
        if (accessToken == null)
            accessToken = SettingManager.getInstance().getSetting(SettingManager.ACCESS_TOKEN);
        return accessToken;
    }

    public String getAccessTokenSecret() {
        if (accessTokenSecret == null) {
            accessTokenSecret = SettingManager.getInstance().getSetting(SettingManager.ACCESS_TOKEN_SECRET);
        }
        return accessTokenSecret;
    }

    /**
     * 清除登录的token
     */
    public void clear() {
        retroft = null;
        accessToken = null;
        accessTokenSecret = null;
        fmRetrofit = null;
    }

}
