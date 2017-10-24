package com.pigbear.hi_andgmusic.ui.music.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.bilibili.magicasakura.widgets.TintToolbar;
import com.lb.materialdesigndialog.base.DialogBase;
import com.lb.materialdesigndialog.base.DialogWithTitle;
import com.lb.materialdesigndialog.impl.MaterialDialogNormal;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;
import com.pigbear.hi_andgmusic.service.OnSongChangeListener;
import com.pigbear.hi_andgmusic.ui.adapter.OnItemClickListener;
import com.pigbear.hi_andgmusic.ui.adapter.RecentPlayAdapter;
import com.pigbear.hi_andgmusic.ui.play.MusicPlayList;
import com.pigbear.hi_andgmusic.ui.play.MusicRecentPlayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;



public class RecentPlayListActivity extends AppCompatActivity implements OnSongChangeListener {

    @Bind(R.id.btnRight)
    Button btnRight;
    @Bind(R.id.toolbar)
    TintToolbar toolbar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private ActionBar actionBar;
    private RecentPlayAdapter recentPlayAdapter;
    private MusicPlayList musicPlayList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_play_list);
        ButterKnife.bind(this);

        setToolBar();
        MusicPlayManager.getInstance().registerListener(this);
        initRecyclerView();
    }

    private void setToolBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.actionbar_back);
        actionBar.setTitle("最近播放");
    }

    private void initRecyclerView() {
        btnRight.setText("清空");
        recentPlayAdapter = new RecentPlayAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recentPlayAdapter);
        recentPlayAdapter.setData(MusicRecentPlayList.getInstance().getQueue());
        if(MusicRecentPlayList.getInstance().getQueue() != null && MusicRecentPlayList.getInstance().getQueue().size() > 0) {
            musicPlayList = new MusicPlayList(MusicRecentPlayList.getInstance().getQueue());
        }
        recentPlayAdapter.setSongClickListener(new OnItemClickListener<Song>() {
            @Override
            public void onItemClick(Song song, int position) {
                MusicPlayManager.getInstance().playQueue(musicPlayList,position);
                startActivity(new Intent(RecentPlayListActivity.this,PlayingActivity.class));
            }

            @Override
            public void onItemSettingClick(View v, Song song, int position) {
                showPopupMenu(v, song, position);
            }
        });

    }

    private void showPopupMenu(View v, final Song song, final int position) {
        PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        MusicPlayManager.getInstance().playQueue(musicPlayList, position);
                        startActivity(new Intent(RecentPlayListActivity.this,PlayingActivity.class));
                        break;
                    case R.id.popup_song_addto_playlist:
                        MusicPlayList mp = MusicPlayManager.getInstance().getMusicPlaylist();
                        if (mp == null) {
                            mp = new MusicPlayList();
                            MusicPlayManager.getInstance();
                        }
                        mp.addSong(song);
                        break;
                    case R.id.popup_song_fav:
                        showCollectionDialog(song);
                        break;
                    case R.id.popup_song_download:
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_recently_playlist_setting);
        menu.show();
        
    }

    /**
     * 显示选择收藏夹列表的弹窗
     *
     * @param song
     */
    public void showCollectionDialog(final Song song) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        MusicPlayManager.getInstance().unRegisterListener(this);
    }

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RecentPlayListActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.btnRight)
    public void onViewClicked() {
        MaterialDialogNormal materialDialogNormal = new MaterialDialogNormal(this);
        materialDialogNormal.setTitle("");
        materialDialogNormal.setMessage("清空全部所有最近播放记录");
        materialDialogNormal.setNegativeButton("取消", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                dialog.dismiss();
            }
        });
        materialDialogNormal.setPositiveButton("清空", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                MusicRecentPlayList.getInstance().clearRecentPlayList();
                recentPlayAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onSongChanged(Song song) {
//        recentPlayAdapter.setData(MusicRecentPlayList.getInstance().getQueue());
//        musicPlayList = new MusicPlayList(MusicRecentPlayList.getInstance().getQueue());
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat stateCompat) {

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

}
