package com.pigbear.hi_andgmusic.ui.guide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.ui.music.activity.MainActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 引导页面：视屏引导
 */
public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.iv1)
    ImageView iv1;
    @Bind(R.id.iv2)
    ImageView iv2;
    @Bind(R.id.iv3)
    ImageView iv3;
    @Bind(R.id.bt_start)
    Button btStart;
    @Bind(R.id.activity_guide)
    RelativeLayout activityGuide;
    @Bind(R.id.vp)
    ViewPager vp;
    private ArrayList<Fragment> fragments;
    private Disposable subscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initListener() {
        subscribe = RxView.clicks(btStart)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        startActivity(new Intent(GuideActivity.this,MainActivity.class));
                        finish();
                    }
                });
    }

    private void initView() {
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        vp.addOnPageChangeListener(this);
        vp.setCurrentItem(0);//默认选中第一页
    }

    /**
     * 初始化数据，添加3个Fragment
     */
    private void initData() {
        fragments = new ArrayList<>();
        GuideFragment guideFragment1 = new GuideFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("index", 1);
        guideFragment1.setArguments(bundle1);
        fragments.add(guideFragment1);

        GuideFragment guideFragment2 = new GuideFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("index", 2);
        guideFragment2.setArguments(bundle2);
        fragments.add(guideFragment2);

        GuideFragment guideFragment3 = new GuideFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("index", 3);
        guideFragment3.setArguments(bundle3);
        fragments.add(guideFragment3);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
//            container.removeView(fragments.get(position).getView());
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * 切换下标点
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0 :
                btStart.setVisibility(View.GONE);
                iv1.setImageResource(R.mipmap.dot_focus);
                iv2.setImageResource(R.mipmap.dot_normal);
                iv3.setImageResource(R.mipmap.dot_normal);
                break;
            case 1:
                btStart.setVisibility(View.GONE);
                iv1.setImageResource(R.mipmap.dot_normal);
                iv2.setImageResource(R.mipmap.dot_focus);
                iv3.setImageResource(R.mipmap.dot_normal);
                break;
            case 2:
                btStart.setVisibility(View.VISIBLE);
                iv1.setImageResource(R.mipmap.dot_normal);
                iv2.setImageResource(R.mipmap.dot_normal);
                iv3.setImageResource(R.mipmap.dot_focus);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscribe != null) {
            subscribe.dispose();
        }
        fragments.clear();
    }
}
