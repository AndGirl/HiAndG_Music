package com.pigbear.hi_andgmusic.ui.album;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.ui.music.activity.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewSongFragment extends BaseFragment {


    public NewSongFragment() {
        // Required empty public constructor
    }

    public static NewSongFragment getInstance(){
        return new NewSongFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_song, container, false);
    }

}
