package com.pigbear.hi_andgmusic.ui.album;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.ui.music.activity.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * 唱片集页面
 */
public class AlbumFragment extends BaseFragment {


    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    private TabAlbumAdapter adapter;

    public AlbumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_album, container, false);
        ButterKnife.bind(this, inflate);

        adapter = new TabAlbumAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(adapter.getCount());
        tabLayout.setupWithViewPager(viewpager);
        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
