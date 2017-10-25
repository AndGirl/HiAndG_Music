package com.pigbear.hi_andgmusic.ui.collections;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.service.OnSongChangeListener;
import com.pigbear.hi_andgmusic.ui.widget.RefreshRecyclerView;

/**
 * Created by 杨阳洋 on 2017/10/25.
 * usg:音乐专辑详情页面
 */

public abstract class MusicDetailActivity extends AppCompatActivity implements RefreshRecyclerView.RefreshListener, View.OnClickListener, OnSongChangeListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_detail);
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().setDuration(500);
        }
        
    }
}
