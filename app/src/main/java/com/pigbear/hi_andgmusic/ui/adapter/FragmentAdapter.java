package com.pigbear.hi_andgmusic.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.pigbear.hi_andgmusic.service.MusicPlayManager;
import com.pigbear.hi_andgmusic.ui.play.RoundFragment;

/**
 * Created by 杨阳洋 on 2017/10/30.
 * usg:
 */

public class FragmentAdapter extends FragmentStatePagerAdapter{

    private int mChildCount = 0;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return RoundFragment.newInstance(MusicPlayManager.getInstance().getPlayingSong().getCoverUrl());
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public void notifyDataSetChanged() {
        mChildCount = getCount();
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        if (mChildCount > 0) {
            mChildCount--;
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

}
