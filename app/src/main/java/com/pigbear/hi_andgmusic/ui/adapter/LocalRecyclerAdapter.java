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
import com.jakewharton.rxbinding2.view.RxView;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.common.LocalMusicLibrary;
import com.pigbear.hi_andgmusic.data.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * @description: 本地歌曲适配器
 */

public class LocalRecyclerAdapter extends RecyclerView.Adapter<LocalRecyclerAdapter.LocalMusicViewHolder> {

    private Context context;
    private List<Song> songs;
    private OnItemClickListener<Song> itemClickListener;

    public LocalRecyclerAdapter(Context context) {
        this.context = context;
        songs = new ArrayList<>();
    }

    //给适配器设置数据,并更新
    public void setData(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public List<Song> getSongs() {
        return songs;
    }

    @Override
    public LocalMusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_localmusic_listitem, parent, false);
        return new LocalMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LocalMusicViewHolder holder, final int position) {
        final Song song = songs.get(position);
        holder.title.setText(Html.fromHtml(song.getTitle()));
        if (TextUtils.isEmpty(song.getArtistName())) {//艺术家的名字为空
            holder.detail.setText("unknown");
        } else {
            holder.detail.setText(song.getArtistName());
        }

        //解析图片
        Glide.with(context)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.cover)
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class LocalMusicViewHolder extends RecyclerView.ViewHolder {

        public View musicLayout;
        public TextView title, detail;
        public ImageView cover;
        public AppCompatImageView setting;

        public LocalMusicViewHolder(View itemView) {
            super(itemView);
            musicLayout = itemView.findViewById(R.id.local_song_item);
            title = (TextView) itemView.findViewById(R.id.local_song_title);
            detail = (TextView) itemView.findViewById(R.id.local_song_detail);
            cover = (ImageView) itemView.findViewById(R.id.local_song_cover);
            setting = (AppCompatImageView) itemView.findViewById(R.id.local_song_setting);
            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Song song = songs.get(getAdapterPosition());
                    if (itemClickListener != null && song.isStatus()) {
                        itemClickListener.onItemSettingClick(setting, song, getAdapterPosition());
                    }
                }
            });

            RxView.clicks(musicLayout)
                    .throttleFirst(1000, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Object>() {
                        @Override
                        public void accept(Object o) throws Exception {
                            if(songs.size() == 0) {
                                songs = LocalMusicLibrary.getAllSongs(context);
                                //setData(songs);
                            }

                            Song song = songs.get(getAdapterPosition());

                            if (itemClickListener != null && song.isStatus()) {
                                itemClickListener.onItemClick(song, getAdapterPosition());
                            }
                        }
                    });
        }
    }
}
