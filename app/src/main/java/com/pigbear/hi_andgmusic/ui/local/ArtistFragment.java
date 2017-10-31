package com.pigbear.hi_andgmusic.ui.local;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.MusicUtils;
import com.pigbear.hi_andgmusic.data.ArtistInfo;
import com.pigbear.hi_andgmusic.ui.adapter.ArtistAdapter;
import com.pigbear.hi_andgmusic.ui.music.activity.BaseFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * 歌手列表
 */
public class ArtistFragment extends BaseFragment {

    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    private ArtistAdapter adapter;

    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public static ArtistFragment newInstance() {
        ArtistFragment artistFragment = new ArtistFragment();
        return artistFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_artist, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        reloadAdapter();

        return view;
    }

    private void initRecyclerView() {
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ArtistAdapter(null, getActivity());
        recyclerview.setAdapter(adapter);
        recyclerview.setHasFixedSize(true);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
    }

    /**
     * 更新adapter界面
     */
    private void reloadAdapter() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                List<ArtistInfo> artList = MusicUtils.queryArtist(getActivity());
                if(artList != null) {
                    adapter.updateDataSet(artList);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
