package com.pigbear.hi_andgmusic.ui.guide;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.ui.widget.CustomView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends Fragment {

    private CustomView cv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        cv = (CustomView) view.findViewById(R.id.cv);
        int index = getArguments().getInt("index");
        if (index == 1) {
            cv.playVideo(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_1));
        } else if (index == 2) {
            cv.playVideo(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_2));
        } else if (index == 3) {
            cv.playVideo(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_3));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cv != null) {
            cv.stopPlayback();
            cv.suspend();
        }
    }
}
