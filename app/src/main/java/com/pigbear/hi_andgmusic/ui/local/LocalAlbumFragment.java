package com.pigbear.hi_andgmusic.ui.local;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.MusicUtils;
import com.pigbear.hi_andgmusic.data.AlbumInfo;
import com.pigbear.hi_andgmusic.presenter.LocalAlbumPresenter;
import com.pigbear.hi_andgmusic.ui.adapter.LocalAlbumAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * 唱片集列表
 */
public class LocalAlbumFragment extends Fragment {


    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private LocalAlbumAdapter localAlbumAdapter;
    private LocalAlbumPresenter albumPresenter;

    public LocalAlbumFragment() {
        // Required empty public constructor
    }

    public static LocalAlbumFragment newInstance() {
        LocalAlbumFragment localAlbumFragment = new LocalAlbumFragment();
        return localAlbumFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        albumPresenter = new LocalAlbumPresenter(getActivity(),this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_album, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        reloadAdapter();

        return view;
    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        localAlbumAdapter = new LocalAlbumAdapter(getActivity());
        recyclerView.setAdapter(localAlbumAdapter);

        //albumPresenter.requestLocalAlbum();

    }

    /**
     * 更新adapter界面
     */
    private void reloadAdapter() {
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                List<AlbumInfo> artList = MusicUtils.queryAlbums(getActivity());
                if(artList != null) {
                    localAlbumAdapter.setData(artList);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                localAlbumAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @Override
//    public void getLocalAlbumSuccess(List<AlbumInfo> albums) {
//        localAlbumAdapter.setData(albums);
//    }
//
//    @Override
//    public void getLocalAlbumFail(Throwable throwable) {
//        Log.e("TAG", "获取专辑出错");
//    }
}
