package com.pigbear.hi_andgmusic.ui.music.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.pigbear.hi_andgmusic.MusciApplication;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;

/**
 * Created by 杨阳洋 on 2017/9/28.
 * usg:Activity基类
 */

public class BaseActivity extends AppCompatActivity{

    /**
     * @param outState 取消保存状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    /**
     * @param savedInstanceState 取消保存状态
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //默认屏幕不能横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //服务初始化
        MusicPlayManager.startServiceIfNecessary(MusciApplication.getInstance());
        showQuickControl(true);
    }

    /**
     * 显示Snackbar
     */
    public void showSnackBar(View view,String text){
        Snackbar.make(view,text,Snackbar.LENGTH_SHORT).show();
    }


    /**
     * 显示Snackbar
     */
    public void showSnackBar(View view,int resId){
        Snackbar.make(view,resId,Snackbar.LENGTH_SHORT).show();
    }

    public void startToActivity(Class activity){
        Intent intent = new Intent();
        intent.setClass(this,activity);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unbind from the service
        unbindService();
    }

    public void unbindService() {
//        MusicPlayManager.getInstance().getmService().stopService();
    }

    private BottomFragment fragment; //底部播放控制栏
    /**
     * @param show 显示或关闭底部播放控制栏
     */
    protected void showQuickControl(boolean show) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (show) {
            if (fragment == null) {
                fragment = BottomFragment.getInstance();
                ft.add(R.id.bottom_container, fragment,"bottomFragment").commitAllowingStateLoss();
                setFragment(fragment);
            } else {
                ft.show(fragment).commitAllowingStateLoss();
            }
        } else {
            if (fragment != null)
                ft.hide(fragment).commitAllowingStateLoss();
        }
    }

    public void setFragment(BottomFragment fragment) {
        this.fragment = fragment;
    }

    public BottomFragment getFragment() {
        return fragment;
    }

}
