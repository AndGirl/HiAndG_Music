package com.pigbear.hi_andgmusic.ui.local;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.data.CollectionBean;
import com.pigbear.hi_andgmusic.ui.adapter.CollectionAdapter;
import com.pigbear.hi_andgmusic.ui.adapter.OnItemClickListener;
import com.pigbear.hi_andgmusic.ui.collections.CollectionCreateActivity;
import com.pigbear.hi_andgmusic.ui.collections.CollectionPlayActivity;
import com.pigbear.hi_andgmusic.ui.collections.PopupFragment;
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
    private CollectionAdapter collectionAdapter;


    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "Fragment + onResume()");
        collectionAdapter.update();
    }

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

        initRecyclerView();

        return view;
    }

    /**
     * 初始化recyclerView及其adapter
     */
    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        collectionAdapter = new CollectionAdapter(getActivity());
        recyclerView.setAdapter(collectionAdapter);
        collectionAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object item, int position) {
                if(item instanceof CollectionBean) {
                    Intent intent = new Intent(getActivity(), CollectionPlayActivity.class);
                    intent.putExtra("collection",(CollectionBean)item);
                    startActivity(intent);
                }
            }

            @Override
            public void onItemSettingClick(View v, Object item, int position) {
                if(item instanceof CollectionBean) {
                    PopupFragment popupFragment = PopupFragment.newInstance((CollectionBean) item, collectionAdapter);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);
                    popupFragment.setArguments(bundle);
                    popupFragment.show(getFragmentManager(),"pupupFragment");
                }
            }
        });
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
                Intent intent = new Intent(getActivity(), CollectionCreateActivity.class);
                intent.putExtra("cid", -1);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
