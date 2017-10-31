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

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LastfmAlbum {

    private static final String NAME = "name";
    private static final String IMAGE = "image";
    private static final String SIMILAR = "similar";
    private static final String TAGS = "tags";
    private static final String BIO = "bio";

    @SerializedName(NAME)
    public String mName;

    @SerializedName(IMAGE)
    public List<Artwork> mArtwork;

    @SerializedName(SIMILAR)
    public SimilarAlbum mSimilarArtist;

    @SerializedName(TAGS)
    public AlbumTag mArtistTags;

    @SerializedName(BIO)
    public ArtistBio mArtistBio;


    public class SimilarAlbum {

        public static final String ALBUM = "album";

        @SerializedName(ALBUM)
        public List<LastfmAlbum> mSimilarAlbum;
    }

    public class AlbumTag {

        public static final String TAG = "tag";

        @SerializedName(TAG)
        public List<AlbumTag> mTags;
    }

}
