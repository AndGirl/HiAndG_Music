/*
 * Copyright (C) 2015 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package com.pigbear.hi_andgmusic.net;

import android.content.Context;
import android.util.Log;

import com.pigbear.hi_andgmusic.data.AlbumInfo;
import com.pigbear.hi_andgmusic.data.AlbumQuery;
import com.pigbear.hi_andgmusic.data.ArtistInfo;
import com.pigbear.hi_andgmusic.data.ArtistQuery;
import com.pigbear.hi_andgmusic.net.callbacks.AlbuminfoListener;
import com.pigbear.hi_andgmusic.net.callbacks.ArtistInfoListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LastFmClient {

    public static final String BASE_API_URL = "http://ws.audioscrobbler.com/2.0/";
    private static final Object sLock = new Object();
    private static LastFmClient sInstance;
    private LastFmRestService mRestService;

    public static LastFmClient getsInstance(Context context){
        synchronized (sLock){
            if(sInstance == null){
                sInstance = new LastFmClient();
                sInstance.mRestService = RestServiceFactory.create(context,BASE_API_URL,LastFmRestService.class);
            }
            return sInstance;
        }
    }

    /****
     * 获取歌手信息
     * @param artistQuery
     * @param listener
     */
    public void getArtistInfo(ArtistQuery artistQuery, final ArtistInfoListener listener){
        mRestService.getArtistInfo(artistQuery.mArtist).enqueue(new Callback<ArtistInfo>() {
            @Override
            public void onResponse(Call<ArtistInfo> call, Response<ArtistInfo> response) {
                Log.e("TAG", "response + " + response.body());
                listener.artistInfoSucess(response.body().mArtist);
            }

            @Override
            public void onFailure(Call<ArtistInfo> call, Throwable t) {
                listener.artistInfoFailed();
            }
        });

    }

    /**
     * 获取专辑信息
     * @param albumQuery
     * @param listener
     */
    public void getAlbumInfo(AlbumQuery albumQuery,final AlbuminfoListener listener){

        mRestService.getAlbumInfo(albumQuery.mArtist, albumQuery.mALbum).enqueue(new Callback<AlbumInfo>() {
            @Override
            public void onResponse(Call<AlbumInfo> call, Response<AlbumInfo> response) {
                listener.albumInfoSucess(response.body().mAlbum);
            }

            @Override
            public void onFailure(Call<AlbumInfo> call, Throwable t) {
                listener.albumInfoFailed();
            }
        });

    }




}
