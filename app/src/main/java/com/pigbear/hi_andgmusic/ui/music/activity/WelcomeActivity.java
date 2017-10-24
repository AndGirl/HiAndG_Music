package com.pigbear.hi_andgmusic.ui.music.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.ui.guide.GuideActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        logo = (ImageView) findViewById(R.id.logo);

        startAnimation();
    }

    /**
     * 启动页的动画
     */
    private void startAnimation() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet.Builder builder = animatorSet.play(alpha).with(scaleX).with(scaleY);
        animatorSet.setDuration(1500);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animator) {
                //当App由用户的时候，可以再次判断是否登录

                //需要跳转页面
                startActivity(new Intent(WelcomeActivity.this,GuideActivity.class));
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logo.clearAnimation();
    }
}
