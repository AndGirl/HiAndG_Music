package com.pigbear.hi_andgmusic.ui.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.bilibili.magicasakura.widgets.TintToolbar;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.ACache;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.ui.adapter.LocalMusicAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocalMusicActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    TintToolbar toolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    private LocalMusicAdapter localMusicAdapter;
    private ActionBar actionBar;
    private Song song;

    @Override
    protected void onResume() {
        super.onResume();
        if(getFragment() != null) {
            Object asObject = ACache.get(this, "bottomFragment").getAsObject("bottomFragment");
            if(asObject != null && asObject instanceof Song) {
                song = (Song) asObject;
                getFragment().setSong(song);
                getFragment().updateData();
            }else{
                getFragment().initData();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_music);
        ButterKnife.bind(this);

        setToolBar();

        localMusicAdapter = new LocalMusicAdapter(getSupportFragmentManager());
        viewpager.setAdapter(localMusicAdapter);
        viewpager.setCurrentItem(0);
        viewpager.setOffscreenPageLimit(localMusicAdapter.getCount());
        tabLayout.setupWithViewPager(viewpager);
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back);
        actionBar.setTitle("本地音乐");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
