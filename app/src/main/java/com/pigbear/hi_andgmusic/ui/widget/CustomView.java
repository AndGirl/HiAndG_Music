package com.pigbear.hi_andgmusic.ui.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.VideoView;

/**
 * Created by 杨阳洋 on 2017/9/28.
 * usg:自定义播放页面
 */

public class CustomView extends VideoView {
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec),View.MeasureSpec.getSize(heightMeasureSpec));
    }

    /**
     * 播放视频
     * @param uri
     */
    public void playVideo(Uri uri){
        if(uri == null) {
            throw  new IllegalArgumentException("uri can not be null");
        }
        /**设置播放路径*/
        setVideoURI(uri);
        /**开始播放*/
        start();
        setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                /**设置循环播放*/
                mediaPlayer.setLooping(true);
            }
        });

        setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                return true;
            }
        });
    }

}
