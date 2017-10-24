package com.pigbear.hi_andgmusic.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pigbear.hi_andgmusic.ui.local.ArtistFragment;
import com.pigbear.hi_andgmusic.ui.local.LocalAlbumFragment;
import com.pigbear.hi_andgmusic.ui.local.LocalMusicFragment;

/**
 * Created by 杨阳洋 on 2017/10/9.
 * usg:
 */

public class LocalMusicAdapter extends FragmentPagerAdapter {
    private String[] titles = new String[]{"单曲", "歌手", "专辑"};
    private LocalMusicFragment mLocalMusicFragment;
    private ArtistFragment songMenuFragment;
    private LocalAlbumFragment rankingFragment;

    public LocalMusicAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (mLocalMusicFragment == null) {
                    mLocalMusicFragment = mLocalMusicFragment.newInstance();
                }
                return mLocalMusicFragment;
            case 1:
                if (songMenuFragment == null) {
                    songMenuFragment = songMenuFragment.newInstance();
                }
                return songMenuFragment;
            case 2:

                if (rankingFragment == null) {
                    rankingFragment = rankingFragment.newInstance();
                }
                return rankingFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
