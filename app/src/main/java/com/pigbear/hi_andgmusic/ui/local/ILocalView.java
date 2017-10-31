package com.pigbear.hi_andgmusic.ui.local;

import com.pigbear.hi_andgmusic.data.AlbumInfo;
import com.pigbear.hi_andgmusic.data.Song;

import java.util.List;

/**
 * Created by 杨阳洋 on 2017/10/12.
 * usg:
 */

public interface ILocalView {

    //单曲
    interface  LocalMusic{
        void getLocalMusicSuccess(List<Song> songs);
        void getLocalMusicFail(Throwable throwable);
    }

    //专辑
    interface  LocalAlbum{
        void getLocalAlbumSuccess(List<AlbumInfo> albums);
        void getLocalAlbumFail(Throwable throwable);
    }
    //歌手
    interface  LocalArtist{

    }

}
