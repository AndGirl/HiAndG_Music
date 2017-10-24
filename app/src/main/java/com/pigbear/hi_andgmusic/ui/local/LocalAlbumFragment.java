package com.pigbear.hi_andgmusic.ui.local;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;

/**
 * A simple {@link Fragment} subclass.
 * 唱片集列表
 */
public class LocalAlbumFragment extends Fragment {


    public LocalAlbumFragment() {
        // Required empty public constructor
    }

    public static LocalAlbumFragment newInstance(){
        LocalAlbumFragment localAlbumFragment = new LocalAlbumFragment();
        return localAlbumFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_local_album, container, false);
    }

}
