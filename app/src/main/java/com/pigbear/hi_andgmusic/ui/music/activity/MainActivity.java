package com.pigbear.hi_andgmusic.ui.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bilibili.magicasakura.widgets.TintToolbar;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.ACache;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;
import com.pigbear.hi_andgmusic.ui.album.AlbumFragment;
import com.pigbear.hi_andgmusic.ui.local.LocalFragment;
import com.pigbear.hi_andgmusic.ui.radio.RadioFragment;
import com.pigbear.hi_andgmusic.ui.widget.CustomerViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.bar_net)
    ImageView barNet;
    @Bind(R.id.bar_music)
    ImageView barMusic;
    @Bind(R.id.bar_friends)
    ImageView barFriends;
    @Bind(R.id.bar_search)
    ImageView barSearch;
    @Bind(R.id.toolbar)
    TintToolbar toolbar;
    @Bind(R.id.main_viewpager)
    CustomerViewPager mainViewpager;
    @Bind(R.id.a)
    RelativeLayout a;
    @Bind(R.id.id_lv_left_menu)
    ListView idLvLeftMenu;
    @Bind(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    private ActionBar actionBar;

    private ArrayList<ImageView> tabs = new ArrayList<>();
    private long time = 0;
    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawableResource(R.color.background_material_light_1);


        ButterKnife.bind(this);
        setToolBar();
        setCustomeViewPager();
        setDrawerLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MusicPlayManager.getInstance().getPlayingSong() != null) {
            if(getFragment() != null) {
                Object asObject = ACache.get(this, "bottomFragment").getAsObject("bottomFragment");
                if(asObject != null && asObject instanceof Song) {
                    song = (Song) asObject;
                    getFragment().setSong(song);
                    getFragment().updateData();
                }else{//加载默认图片
                    getFragment().initData();
                }
            }
        }
    }

    /**
     * 设置ToolBar
     */
    private void setToolBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setTitle("");
    }

    /**
     * 设置自定义ViewPager
     */
    private void setCustomeViewPager() {
        //添加tab标签
        tabs.add(barNet);
        tabs.add(barMusic);
        tabs.add(barFriends);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        AlbumFragment albumFragment = new AlbumFragment();
        LocalFragment localFragment = new LocalFragment();
        RadioFragment radioFragment = new RadioFragment();
        myPagerAdapter.addFragment(albumFragment);
        myPagerAdapter.addFragment(localFragment);
        myPagerAdapter.addFragment(radioFragment);

        mainViewpager.setAdapter(myPagerAdapter);
        mainViewpager.setCurrentItem(0);
        tabs.get(0).setSelected(true);
        mainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //切换tab标签
                switchTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 切换toolbar的标签
     */
    private void switchTabs(int pos) {
        for (int i = 0; i < tabs.size(); i++) {
            if (pos == i) {
                tabs.get(i).setSelected(true);
                mainViewpager.setCurrentItem(i);
            } else {
                tabs.get(i).setSelected(false);
            }
        }
    }

    @OnClick({R.id.bar_net, R.id.bar_music, R.id.bar_friends, R.id.bar_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bar_net:
                switchTabs(0);
                break;
            case R.id.bar_music:
                switchTabs(1);
                break;
            case R.id.bar_friends:
                switchTabs(2);
                break;
            case R.id.bar_search://跳转搜索页面
                break;
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> mFragments = new ArrayList();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    /**
     * 辨认出用户点击的是哪一个Action按钮
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                drawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化侧滑菜单
     */
    private void setDrawerLayout() {
        //关联侧滑菜单布局
        View drawerView = LayoutInflater.from(this).inflate(R.layout.nav_header_main, idLvLeftMenu,false);
        idLvLeftMenu.addHeaderView(drawerView);
        idLvLeftMenu.setAdapter(new MyListAdapter());
        idLvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
//                    case  :
//
//                        break;
                }
            }
        });
    }

    class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }

    /**
     * 双击返回桌面
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if(System.currentTimeMillis() - time > 1000) {
                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            }else{
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                //System.exit(0);//强制退出程序，清空jvm
            }
            return true;
        }else{
            return super.onKeyDown(keyCode, event);
        }
    }
}
