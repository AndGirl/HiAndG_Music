package com.pigbear.hi_andgmusic.ui.music.activity;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by 杨阳洋 on 2017/10/9.
 * usg:
 */

public class BaseFragment extends Fragment {

    /**
     * snackbar的显示
     *
     * @param toast
     */
    public void showSnackBar(String toast) {
        Snackbar.make(getActivity().getWindow().getDecorView(), toast, Snackbar.LENGTH_SHORT).show();
    }

    public void showToast(int toastRes) {
        Toast.makeText(getActivity(), getString(toastRes), Toast.LENGTH_SHORT).show();
    }

//    public boolean gotoSongPlayerActivity(){
//        if(MusicPlayerManager.get().getPlayingSong() == null){
//            showToast(R.string.music_playing_none);
//            return false;
//        }
//        PlayingActivity.open(getActivity());
//        return true;
//    }

}
