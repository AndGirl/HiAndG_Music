package com.pigbear.hi_andgmusic.ui.collections;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.HandlerUtil;
import com.pigbear.hi_andgmusic.data.CollectionBean;
import com.pigbear.hi_andgmusic.data.OverFlowItem;
import com.pigbear.hi_andgmusic.db.CollectionManager;
import com.pigbear.hi_andgmusic.ui.adapter.CollectionAdapter;
import com.pigbear.hi_andgmusic.ui.adapter.MusicFlowAdapter;
import com.pigbear.hi_andgmusic.ui.adapter.PlayListAdapter;
import com.pigbear.hi_andgmusic.ui.music.activity.PlayingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopupFragment extends DialogFragment {

    public static final int TYPE_DOWNLOAD = 0;  //说明是带有Header的
    public static final int TYPE_SHARE = 1;  //说明是带有Footer的
    public static final int TYPE_EDITOR = 2;  //说明是不带有header和footer的
    public static final int TYPE_DELETE = 3;  //说明是不带有header和footer的

    @Bind(R.id.pop_list_title)
    TextView popListTitle;
    @Bind(R.id.pop_list)
    RecyclerView popList;
    @Bind(R.id.pop_layout)
    LinearLayout popLayout;
    @Bind(R.id.more_frament)
    FrameLayout moreFrament;
    private CollectionAdapter collectionAdapter;
    private CollectionBean bean;
    private Context mContext;
    private Handler mHandler;
    private PlayListAdapter playListAdapter;
    private LinearLayoutManager layoutManager;
    private PlayingActivity playingActivity;
    private List<OverFlowItem> mlistInfo = new ArrayList<>();  //声明一个list，动态存储要显示的信息
    private MusicFlowAdapter musicFlowAdapter;
    private int position;//点击的位置

    public static PopupFragment newInstance(CollectionBean item, CollectionAdapter collectionAdapter) {
        PopupFragment fragment = new PopupFragment();
        fragment.bean = item;
        fragment.collectionAdapter = collectionAdapter;
        return fragment;
    }

    public PopupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDatePickerDialog);
        mContext = getActivity();
        mHandler = HandlerUtil.getInstance(mContext);

        Bundle arguments = getArguments();
        if(arguments != null) {
            position = arguments.getInt("position", -2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //设置无标题栏
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置从底部弹出
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);

        View inflate = inflater.inflate(R.layout.fragment_popup, container, false);
        ButterKnife.bind(this, inflate);

        setMusicInfo();
        initRecyclerView();

        musicFlowAdapter.setOnItemClickListener(new MusicFlowAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                int index = Integer.valueOf(data);
                switch (index) {
                    case TYPE_DOWNLOAD:
                        break;
                    case TYPE_SHARE:
                        break;
                    case TYPE_EDITOR://编辑
                        Intent intent = new Intent(getActivity(), CollectionCreateActivity.class);
                        intent.putExtra("cid", bean.getId());
                        intent.putExtra("position",position);
                        startActivity(intent);
                        break;
                    case TYPE_DELETE://删除歌单
                        CollectionManager.getInstance().deleteCollection(bean);
                        collectionAdapter.deleteCollection(bean);
                        break;
                }
                dismiss();
            }
        });
        return inflate;
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(mContext);
        popList.setLayoutManager(layoutManager);
        popList.setHasFixedSize(true);
        musicFlowAdapter = new MusicFlowAdapter(getActivity(), mlistInfo);
        popList.setAdapter(musicFlowAdapter);
        popList.setItemAnimator(new DefaultItemAnimator());
        popList.addItemDecoration(new DividerItemDecoration(mContext,DividerItemDecoration.VERTICAL));
    }

    //设置音乐overflow条目
    private void setMusicInfo() {
        //设置mlistInfo，listview要显示的内容
//        setInfo("下一首播放", R.drawable.lay_icn_next);
//        setInfo("收藏到歌单", R.drawable.lay_icn_fav);
//        setInfo("分享", R.drawable.lay_icn_share);
//        setInfo("删除", R.drawable.lay_icn_delete);
//        setInfo("设为铃声", R.drawable.lay_icn_ring);
//        setInfo("查看歌曲信息", R.drawable.lay_icn_document);

        setInfo("下载",R.drawable.ic_red_download);
        setInfo("分享",R.drawable.ic_red_share);
        setInfo("编辑歌单信息",R.drawable.ic_red_editor);
        setInfo("删除",R.drawable.ic_red_delete);

    }

    //为info设置数据，并放入mlistInfo
    public void setInfo(String title, int id) {
        // mlistInfo.clear();
        OverFlowItem information = new OverFlowItem();
        information.setTitle(title);
        information.setAvatar(id);
        mlistInfo.add(information); //将新的info对象加入到信息列表中
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.35);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
