package com.pigbear.hi_andgmusic.ui.local;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.ui.music.activity.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * 歌手列表
 */
public class ArtistFragment extends BaseFragment {


    public ArtistFragment() {
        // Required empty public constructor
    }

    public static ArtistFragment newInstance(){
        ArtistFragment artistFragment = new ArtistFragment();
        return artistFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

}
