package com.pigbear.hi_andgmusic.ui.local;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.ui.collections.CollectionCreateActivity;
import com.pigbear.hi_andgmusic.ui.music.activity.LocalMusicActivity;
import com.pigbear.hi_andgmusic.ui.music.activity.RecentPlayListActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocalFragment extends Fragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    public LocalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.local_layout, R.id.recently_layout, R.id.download_layout, R.id.artist_layout, R.id.add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.local_layout://本地音乐
                startActivity(new Intent(getActivity(), LocalMusicActivity.class));
                break;
            case R.id.recently_layout://最近播放
                startActivity(new Intent(getActivity(), RecentPlayListActivity.class));
                break;
            case R.id.download_layout:
                break;
            case R.id.artist_layout:
                break;
            case R.id.add:
                startActivity(new Intent(getActivity(), CollectionCreateActivity.class));
                break;
        }
    }
}
