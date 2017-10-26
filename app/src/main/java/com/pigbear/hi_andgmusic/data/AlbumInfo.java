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

package com.pigbear.hi_andgmusic.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class AlbumInfo implements Parcelable {

    public static final String KEY_ALBUM_NAME = "album_name";
    public static final String KEY_ALBUM_ID = "album_id";
    public static final String KEY_NUMBER_OF_SONGS = "number_of_songs";
    public static final String KEY_ALBUM_ART = "album_art";
    public static final String KEY_ALBUM_ARTIST = "album_artist";
    public static final String KEY_ALBUM_SORT = "album_sort";

    //专辑名称
    public String album_name;
    //专辑在数据库中的id
    public int album_id = -1;
    //专辑的歌曲数目
    public int number_of_songs = 0;
    //专辑封面图片路径
    public String album_art;
    public String album_artist;
    public String album_sort;
    public static final Creator<AlbumInfo> CREATOR = new Creator<AlbumInfo>() {

        //读数据恢复数据
        @Override
        public AlbumInfo createFromParcel(Parcel source) {
            AlbumInfo info = new AlbumInfo();
            Bundle bundle = source.readBundle();
            info.album_name = bundle.getString(KEY_ALBUM_NAME);
            info.album_art = bundle.getString(KEY_ALBUM_ART);
            info.number_of_songs = bundle.getInt(KEY_NUMBER_OF_SONGS);
            info.album_id = bundle.getInt(KEY_ALBUM_ID);
            info.album_artist = bundle.getString(KEY_ALBUM_ARTIST);
            info.album_sort = bundle.getString(KEY_ALBUM_SORT);
            return info;
        }

        @Override
        public AlbumInfo[] newArray(int size) {
            return new AlbumInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    //写数据保存数据
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_ALBUM_NAME, album_name);
        bundle.putString(KEY_ALBUM_ART, album_art);
        bundle.putInt(KEY_NUMBER_OF_SONGS, number_of_songs);
        bundle.putInt(KEY_ALBUM_ID, album_id);
        bundle.putString(KEY_ALBUM_ARTIST, album_artist);
        bundle.putString(KEY_ALBUM_SORT, album_sort);
        dest.writeBundle(bundle);
    }

}
