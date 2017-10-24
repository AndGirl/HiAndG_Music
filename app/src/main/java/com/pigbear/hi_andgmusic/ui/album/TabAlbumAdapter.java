package com.pigbear.hi_andgmusic.ui.album;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by 杨阳洋 on 2017/10/9.
 * usg:
 */

public class TabAlbumAdapter extends FragmentPagerAdapter {

    private String[] titles = new String[]{"歌单", "MV", "排行榜"};
    private NewSongFragment mNewSongFragment;
    private MVFragment songMenuFragment;
    private RankingFragment rankingFragment;

    public TabAlbumAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (mNewSongFragment == null) {
                    mNewSongFragment = mNewSongFragment.getInstance();
                }
                return mNewSongFragment;
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
