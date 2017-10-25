package com.pigbear.hi_andgmusic.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintImageView;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.data.MusicInfo;
import com.pigbear.hi_andgmusic.data.OverFlowItem;

import java.util.List;

/**
 * Created by 杨阳洋 on 2017/10/25.
 * usg:
 */

public class MusicFlowAdapter extends RecyclerView.Adapter<MusicFlowAdapter.ListItemViewHolder> implements View.OnClickListener {

    private List<OverFlowItem> mList;
    private MusicInfo musicInfo;
    private Activity mContext;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, String data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public MusicFlowAdapter(Activity context, List<OverFlowItem> list, MusicInfo info) {
        mList = list;
        musicInfo = info;
        mContext = context;
    }

    public MusicFlowAdapter(Activity context, List<OverFlowItem> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_flow_layout, parent, false);
        ListItemViewHolder vh = new ListItemViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        OverFlowItem minfo = mList.get(position);
        holder.icon.setImageResource(minfo.getAvatar());
        holder.icon.setImageTintList(R.color.theme_color_primary);
        holder.title.setText(minfo.getTitle());
        //设置tag
        holder.itemView.setTag(position + "");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (String) v.getTag());
        }
    }

    public class ListItemViewHolder extends RecyclerView.ViewHolder {
        TintImageView icon;
        TextView title;

        ListItemViewHolder(View view) {
            super(view);
            this.icon = (TintImageView) view.findViewById(R.id.pop_list_view);
            this.title = (TextView) view.findViewById(R.id.pop_list_item);

        }


    }

}
