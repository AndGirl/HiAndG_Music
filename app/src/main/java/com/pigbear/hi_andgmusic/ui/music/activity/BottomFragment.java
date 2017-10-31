package com.pigbear.hi_andgmusic.ui.music.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding2.view.RxView;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.ACache;
import com.pigbear.hi_andgmusic.common.ImageUtils;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;
import com.pigbear.hi_andgmusic.service.OnSongChangeListener;
import com.pigbear.hi_andgmusic.ui.listing.PlayQueueFragment;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 * 底部状态栏
 */
public class BottomFragment extends BaseFragment implements OnSongChangeListener, Serializable {

    @Bind(R.id.playbar_img)
    SimpleDraweeView playbarImg;
    @Bind(R.id.playbar_info)
    TextView playbarInfo;
    @Bind(R.id.playbar_singer)
    TextView playbarSinger;
    @Bind(R.id.play_list)
    TintImageView playList;
    @Bind(R.id.control)
    TintImageView control;
    @Bind(R.id.play_next)
    TintImageView playNext;
    @Bind(R.id.linear)
    LinearLayout linear;
    @Bind(R.id.song_progress_normal)
    SeekBar songProgressNormal;
    @Bind(R.id.nav_play)
    LinearLayout navPlay;
    private View rootView;

    private Handler handler = new Handler();

    private Song song;
    private boolean isPaused;
    private BottomFragment bottomFragment;

    public static BottomFragment getInstance() {
        return new BottomFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.bottom_nav, container, false);

        bottomFragment = this;
        ButterKnife.bind(this, rootView);

        //音乐信息更新的监听
        MusicPlayManager.getInstance().registerListener(this);

        RxView.clicks(rootView).throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (MusicPlayManager.getInstance().getPlayingSong() != null) {
                            Intent intent = new Intent(getActivity(), PlayingActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getActivity().startActivity(intent);
                        } else {
                            showToast(R.string.music_playing_none);
                        }
                    }
                });

        //播放列表
        RxView.clicks(playList).throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PlayQueueFragment queueFragment = new PlayQueueFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("fragment", bottomFragment);
                                queueFragment.setArguments(bundle);
                                queueFragment.show(getFragmentManager(), "playqueuefragment");
                            }
                        }, 60);
                    }
                });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.play_next, R.id.control})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_next:
                if (MusicPlayManager.getInstance().getPlayingSong() != null) {
                    MusicPlayManager.getInstance().playNext();
                } else {
                    showToast(R.string.music_playing_none);
                }
                break;
            case R.id.control:
                if (MusicPlayManager.getInstance().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    MusicPlayManager.getInstance().playPause();
                    control.setImageResource(R.drawable.playbar_btn_play);
                } else if (MusicPlayManager.getInstance().getState() == PlaybackStateCompat.STATE_PAUSED) {
                    MusicPlayManager.getInstance().play();
                    control.setImageResource(R.drawable.playbar_btn_pause);
                }
                break;
        }
    }

    @Override
    public void onSongChanged(Song song) {
        this.song = song;
        updateData();
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat stateCompat) {

    }

    /***
     * 更新数据:封面,标题,图标
     */
    public void updateData() {
        //歌曲的封面
        String coverUrl = song.getCoverUrl();
        ImageUtils.GlideWith(getActivity(), coverUrl, R.drawable.ah1, playbarImg);

        //设置标题
        if (!TextUtils.isEmpty(song.getTitle())) {
            String title = song.getTitle();
            Spanned fromHtml = Html.fromHtml(title);
            playbarInfo.setText(fromHtml);
        }
        //设置歌手
        if (!TextUtils.isEmpty(song.getArtistName())) {
            String title = song.getArtistName();
            Spanned fromHtml = Html.fromHtml(title);
            playbarSinger.setText(fromHtml);
        }


        if (MusicPlayManager.getInstance().getPlayingSong() != null && MusicPlayManager.getInstance().getMediaPlayer().isPlaying()) {
            control.setImageResource(R.drawable.playbar_btn_pause);
        } else {
            control.setImageResource(R.drawable.playbar_btn_play);
        }

        ACache.get(getActivity(), "bottomFragment").put("bottomFragment", song);

    }

    public void initData() {
        ImageUtils.GlideWith(getActivity(), "", R.drawable.ah1, playbarImg);
        playbarInfo.setText("歌曲名");
        playbarSinger.setText("歌手");
        control.setImageResource(R.drawable.playbar_btn_play);
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "resume哈哈哈");
        isPaused = false;
        //updateData();

    }

    @Override
    public void onPause() {
        isPaused = true;
        Log.e("TAG", "给点活路呀");
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicPlayManager.getInstance().unRegisterListener(this);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
