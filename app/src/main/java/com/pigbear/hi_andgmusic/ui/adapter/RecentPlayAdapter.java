package com.pigbear.hi_andgmusic.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.data.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨阳洋 on 2017/10/16.
 * usg:最近播放的适配器
 */

public class RecentPlayAdapter extends RecyclerView.Adapter<RecentPlayAdapter.RecentViewHolder>{
    private Context context;
    private List<Song> songs;
    private OnItemClickListener songClickListener;

    public OnItemClickListener getSongClickListener(){
        return songClickListener;
    }

    public void setSongClickListener(OnItemClickListener songClickListener){
        this.songClickListener = songClickListener;
    }

    public RecentPlayAdapter(Context context) {
        this.context = context;
        songs = new ArrayList<>();
    }

    public void setData(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_recently_listitem,parent,false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecentViewHolder holder, int position) {
        final Song song = songs.get(position);
        holder.title.setText(Html.fromHtml(song.getTitle()));
        if (TextUtils.isEmpty(song.getArtistName())) {
            holder.detail.setText(R.string.music_unknown);
        } else {
            holder.detail.setText(song.getArtistName());
        }
        Glide.with(context)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.cover)
                .into(holder.cover);

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songClickListener != null && song.isStatus()) {
                    songClickListener.onItemSettingClick(holder.setting, song, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs == null ? 0 : songs.size();
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder{

        public View songItem;
        public TextView title, detail;
        public ImageView cover;
        public AppCompatImageView setting;

        public RecentViewHolder(View itemView) {
            super(itemView);
            songItem = itemView.findViewById(R.id.song_item);
            title = (TextView) itemView.findViewById(R.id.song_title);
            detail = (TextView) itemView.findViewById(R.id.song_detail);
            cover = (ImageView) itemView.findViewById(R.id.song_cover);
            setting = (AppCompatImageView) itemView.findViewById(R.id.song_setting);
            songItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Song song = songs.get(position);
                    if (songClickListener != null) {
                        songClickListener.onItemClick(song, position);
                    }
                }
            });
        }
    }

}
