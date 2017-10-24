package com.pigbear.hi_andgmusic.ui.listing;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lb.materialdesigndialog.base.DialogBase;
import com.lb.materialdesigndialog.base.DialogWithTitle;
import com.lb.materialdesigndialog.impl.MaterialDialogNormal;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.ACache;
import com.pigbear.hi_andgmusic.data.Song;
import com.pigbear.hi_andgmusic.presenter.LocalMusicPresenter;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;
import com.pigbear.hi_andgmusic.service.OnSongChangeListener;
import com.pigbear.hi_andgmusic.ui.adapter.OnItemClickListener;
import com.pigbear.hi_andgmusic.ui.adapter.PlayListAdapter;
import com.pigbear.hi_andgmusic.ui.music.activity.BottomFragment;
import com.pigbear.hi_andgmusic.ui.music.activity.PlayingActivity;
import com.pigbear.hi_andgmusic.ui.play.MusicPlayList;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayQueueFragment extends DialogFragment implements OnSongChangeListener {

    @Bind(R.id.playlist_addto)
    TextView playlistAddto;
    @Bind(R.id.play_list_number)
    TextView playListNumber;
    @Bind(R.id.playlist_clear_all)
    TextView playlistClearAll;
    @Bind(R.id.play_list)
    RecyclerView recyclerView;
    private Context mContext;
    private LinearLayoutManager layoutManager;
    private PlayListAdapter playListAdapter;

    private MusicPlayManager mPlayerManager;
    private Handler mHandler;
    private MusicPlayList mPlaylist;
    private ArrayList<Song> playlist;
    private DividerItemDecoration itemDecoration;
    private BottomFragment fragment;
    private LocalMusicPresenter mLibraryPresenter;
    private PlayingActivity playingActivity;

    public PlayQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置样式
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDatePickerDialog);
        mContext = getContext();

        Bundle arguments = getArguments();
        if(arguments != null) {
            fragment = (BottomFragment) arguments.getSerializable("fragment");
            playingActivity = (PlayingActivity) arguments.getSerializable("PlayingActivity");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //无标题头
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置从哪里弹出
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);

        View inflate = inflater.inflate(R.layout.fragment_play_queue, container,false);

        ButterKnife.bind(this, inflate);
        MusicPlayManager.getInstance().registerListener(this);

        initRecyclerView(inflate);

        return inflate;
    }

    private void initRecyclerView(View view) {
        playListAdapter = new PlayListAdapter(getActivity());
        recyclerView = (RecyclerView) view.findViewById(R.id.play_list);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(playListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));

        if (MusicPlayManager.getInstance().getMusicPlaylist() != null) {
            playListAdapter.setData(MusicPlayManager.getInstance().getMusicPlaylist().getQueue());
            Log.e("initRecyclerView: ", "initRecyclerView: " + MusicPlayManager.getInstance().getMusicPlaylist().getQueue().size());
        }

        playListAdapter.setSongClickListener(new OnItemClickListener<Song>() {
            @Override
            public void onItemClick(Song song, int position) {
                MusicPlayManager.getInstance().playQueueItem(position);
            }

            @Override
            public void onItemSettingClick(View v, Song song, int position) {
                //showPopupMenu(v, song, position);
                if(playListAdapter.getSongs() != null && playListAdapter.getSongs().size() > 0) {
                    if(song != null) {
                        playListAdapter.onItemDismiss(position);
                    }
                    if(playListAdapter.getSongs().size() == 0) {
                        MusicPlayManager.getInstance().clearPlayer();
                        ACache.get(getActivity(), "bottomFragment").remove("bottomFragment");
                        if(playingActivity != null) {
                            playingActivity.finish();
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.playlist_addto, R.id.playlist_clear_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.playlist_addto://添加到收藏
                Toast.makeText(getActivity(), "收藏", Toast.LENGTH_SHORT).show();
                break;
            case R.id.playlist_clear_all://清空播放列表
                showBasicDialog();
                break;
        }
    }

    private void showBasicDialog(){
        final MaterialDialogNormal materialDialogNormal = new MaterialDialogNormal(getActivity());
        materialDialogNormal.setMessage("确定要清空列表吗?");
        materialDialogNormal.setNegativeButton("取消", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                dialog.dismiss();
            }
        });
        materialDialogNormal.setPositiveButton("确定", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                ACache.get(getActivity(), "bottomFragment").remove("bottomFragment");
                playListAdapter.clearAll();
                MusicPlayManager.getInstance().clearPlayer();
                dismiss();
                materialDialogNormal.dismiss();
                if(playingActivity != null) {
                    playingActivity.finish();
                }
            }
        });
        materialDialogNormal.setTitle("清空列表");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicPlayManager.getInstance().unRegisterListener(this);
        if(fragment != null) {
            this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Object asObject = ACache.get(getActivity(), "bottomFragment").getAsObject("bottomFragment");
                    if(asObject != null && asObject instanceof Song) {
                    }else{
                        fragment.initData();
                    }
                }
            });
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.6);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public void onSongChanged(Song song) {
        if(MusicPlayManager.getInstance().getMusicPlaylist() != null) {
            playListAdapter.setData(MusicPlayManager.getInstance().getMusicPlaylist().getQueue());
        }
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat stateCompat) {

    }

}
