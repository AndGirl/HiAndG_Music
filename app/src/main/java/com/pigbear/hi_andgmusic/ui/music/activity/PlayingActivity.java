package com.pigbear.hi_andgmusic.ui.music.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.ImageUtils;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;
import com.pigbear.hi_andgmusic.service.OnSongChangeListener;
import com.pigbear.hi_andgmusic.ui.listing.PlayQueueFragment;
import com.pigbear.hi_andgmusic.ui.play.MusicRecentPlayList;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;


public class PlayingActivity extends AppCompatActivity implements OnSongChangeListener,Serializable{

    @Bind(R.id.albumArt)
    ImageView albumArt;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coverImage)
    ImageView coverImage;
    @Bind(R.id.view_line)
    View viewLine;
    @Bind(R.id.needle)
    ImageView needle;
    @Bind(R.id.headerView)
    FrameLayout headerView;
    @Bind(R.id.volume_seek)
    SeekBar volumeSeek;
    @Bind(R.id.volume_layout)
    LinearLayout volumeLayout;
    @Bind(R.id.tragetlrc)
    TextView tragetlrc;
    @Bind(R.id.lrcviewContainer)
    RelativeLayout lrcviewContainer;
    @Bind(R.id.playing_fav)
    ImageView playingFav;
    @Bind(R.id.playing_down)
    ImageView playingDown;
    @Bind(R.id.playing_cmt)
    ImageView playingCmt;
    @Bind(R.id.playing_more)
    ImageView playingMore;
    @Bind(R.id.music_tool)
    LinearLayout musicTool;
    @Bind(R.id.music_duration_played)
    TextView musicDurationPlayed;
    @Bind(R.id.play_seek)
    SeekBar playSeek;
    @Bind(R.id.music_duration)
    TextView musicDuration;
    @Bind(R.id.playing_mode)
    ImageView playingMode;
    @Bind(R.id.playing_pre)
    ImageView playingPre;
    @Bind(R.id.playing_play)
    ImageView playingPlay;
    @Bind(R.id.playing_next)
    ImageView playingNext;
    @Bind(R.id.playing_playlist)
    ImageView playingPlaylist;
    private Song song;
    private boolean isLocalMusic;

    private Handler handler = new Handler();
    private PlayingActivity playingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);
        ButterKnife.bind(this);

        isLocalMusic = getIntent().getBooleanExtra("isLocalMusic", false);

        MusicPlayManager.getInstance().registerListener(this);

        playingActivity = this;

        initToolBar();
        initData();
        updateProgress();
        updateData();

        RxView.clicks(playingPlaylist).throttleFirst(2000,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                PlayQueueFragment queueFragment = new PlayQueueFragment();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("PlayingActivity",playingActivity);
                                queueFragment.setArguments(bundle);
                                queueFragment.show(getSupportFragmentManager(), "playqueuefragment");
                            }
                        }, 60);
                    }
                });

    }

    private void initToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initData() {
        song = MusicPlayManager.getInstance().getPlayingSong();
        if(song == null) {
            finish();
        }

        playSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    MusicPlayManager.getInstance().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.e("TAG", "开始拖拽");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.e("TAG", "拖拽完成");
                if(MusicPlayManager.getInstance().getMediaPlayer().isPlaying()) {//正在播放歌曲
                    MusicPlayManager.getInstance().getmService().setState(PlaybackStateCompat.STATE_PLAYING);
                }else{
                    MusicPlayManager.getInstance().play();
                    playingPlay.setImageResource(R.drawable.play_rdi_btn_pause);
                }
            }
        });

    }

    /**
     * 更新进度条，进度显示，歌曲长度
     */
    private void updateProgress() {
        Observable.interval(0,1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        playSeek.setMax(MusicPlayManager.getInstance().getCurrentMaxDuration());
                        playSeek.setProgress(MusicPlayManager.getInstance().getCurrentPosition());
                        musicDuration.setText(formatChange(MusicPlayManager.getInstance().getCurrentMaxDuration()));
                        musicDurationPlayed.setText(formatChange(MusicPlayManager.getInstance().getCurrentPosition()));
                    }
                });


    }

    /**
     * 更新数据：封面，标题，图标
     */
    private void updateData() {
        //歌曲的封面
        String coverUrl = song.getCoverUrl();
        ImageUtils.GlideWith(this,coverUrl,R.drawable.ah1,coverImage);

        //设置标题
        if(!TextUtils.isEmpty(song.getAlbumName())) {
            String title = song.getAlbumName();
            Spanned fromHtml = Html.fromHtml(title);
            getSupportActionBar().setTitle(fromHtml);
        }
        toolbar.setTitle(song.getTitle());

        if(MusicPlayManager.getInstance().getPlayingSong() != null) {
            playingPlay.setImageResource(R.drawable.play_rdi_btn_pause);
            //添加歌曲
            if(isLocalMusic) {//最近播放列表进入
                MusicRecentPlayList.getInstance().addPlaySong(song);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MusicPlayManager.getInstance().unRegisterListener(this);
    }

    /**
     * 歌曲长度格式装换
     *
     * @param millSeconds
     * @return
     */
    public String formatChange(int millSeconds) {
        int seconds = millSeconds / 1000;
        int second = seconds % 60;
        int munite = (seconds - second) / 60;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(munite) + ":" + decimalFormat.format(second);
    }

    @OnClick({R.id.playing_mode, R.id.playing_pre, R.id.playing_play, R.id.playing_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playing_mode://播放模式
                break;
            case R.id.playing_pre://上一首
                MusicPlayManager.getInstance().playPre();
                break;
            case R.id.playing_play://播放
                if(MusicPlayManager.getInstance().getState() == PlaybackStateCompat.STATE_PLAYING){
                    MusicPlayManager.getInstance().playPause();
                    playingPlay.setImageResource(R.drawable.play_rdi_btn_play);
                }else if(MusicPlayManager.getInstance().getState() == PlaybackStateCompat.STATE_PAUSED){
                    MusicPlayManager.getInstance().play();
                    playingPlay.setImageResource(R.drawable.play_rdi_btn_pause);
                }
                break;
            case R.id.playing_next://下一首
                MusicPlayManager.getInstance().playNext();
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
