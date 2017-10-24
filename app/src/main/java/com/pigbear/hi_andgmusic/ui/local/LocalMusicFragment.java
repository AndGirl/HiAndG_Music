package com.pigbear.hi_andgmusic.ui.local;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.presenter.LocalMusicPresenter;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;
import com.pigbear.hi_andgmusic.ui.adapter.LocalRecyclerAdapter;
import com.pigbear.hi_andgmusic.ui.adapter.OnItemClickListener;
import com.pigbear.hi_andgmusic.ui.music.activity.BaseFragment;
import com.pigbear.hi_andgmusic.ui.music.activity.PlayingActivity;
import com.pigbear.hi_andgmusic.ui.play.MusicPlayList;

import java.util.List;

/**
 * 1.音乐的实体类
 * 2.RecyclerView的适配器  //获取的过程，数据的处理过程，逻辑部分
 * 3.歌曲的获取
 * 4.歌曲如何播放
 * 5.歌曲如何存取
 */
public class LocalMusicFragment extends BaseFragment implements ILocalView.LocalMusic {
    private RecyclerView mRecyclerView;
    private LocalRecyclerAdapter mAdapter;
    private LocalMusicPresenter mLibraryPresenter;
    private MusicPlayList musicPlayList;

    public LocalMusicFragment() {
        // Required empty public constructor
    }

    public static LocalMusicFragment newInstance() {
        LocalMusicFragment fragment = new LocalMusicFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_music, container, false);

        initRecyclerView(view);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLibraryPresenter = new LocalMusicPresenter(getActivity(), this);
        musicPlayList = new MusicPlayList();
    }

    private void initRecyclerView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        mAdapter = new LocalRecyclerAdapter(getActivity());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object item, int position) {
                if (musicPlayList.getQueue().size() == 0) {//清空数据后
                    musicPlayList.setQueue(mAdapter.getSongs());
                }

                MusicPlayManager.getInstance().playQueue(musicPlayList, position);
                if (MusicPlayManager.getInstance().getPlayingSong() == null) {
                    Toast.makeText(getActivity(), getString(R.string.music_playing_none), Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(getActivity(), PlayingActivity.class).putExtra("isLocalMusic", true));
                }

            }

            @Override
            public void onItemSettingClick(View v, Object item, int position) {

            }
        });

        mLibraryPresenter.requestMusic();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("TAG", "LocalMusicFragment暂停");
        mLibraryPresenter.requestMusic();
    }

    @Override
    public void getLocalMusicSuccess(List<Song> songs) {
        //获取到本地音乐
        musicPlayList.setQueue(songs);
        musicPlayList.setTitle("本地歌曲");
        mAdapter.setData(songs);
    }

    @Override
    public void getLocalMusicFail(Throwable throwable) {
        //获取失败
        Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
    }
}
