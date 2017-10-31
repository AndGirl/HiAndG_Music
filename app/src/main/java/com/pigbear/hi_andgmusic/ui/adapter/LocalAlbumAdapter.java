package com.pigbear.hi_andgmusic.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.data.AlbumInfo;
import com.pigbear.hi_andgmusic.data.AlbumQuery;
import com.pigbear.hi_andgmusic.data.LastfmAlbum;
import com.pigbear.hi_andgmusic.net.LastFmClient;
import com.pigbear.hi_andgmusic.net.callbacks.AlbuminfoListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 杨阳洋 on 2017/10/30.
 * usg:专辑的adapter
 */

public class LocalAlbumAdapter extends RecyclerView.Adapter<LocalAlbumAdapter.LocalAlbumViewHolder>{

    private List<AlbumInfo> albumList;
    private Context context;

    public LocalAlbumAdapter(Context context) {
        this.context = context;
        albumList = new ArrayList<>();
    }

    public void setData(List<AlbumInfo> albumList) {
        this.albumList = albumList;
    }

    @Override
    public LocalAlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(context).inflate(R.layout.recycler_localalbum_listitem, parent, false);
        return new LocalAlbumViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final LocalAlbumViewHolder holder, int position) {
        AlbumInfo album = albumList.get(position);
        holder.title.setText(album.album_name);
        holder.artist.setText(album.album_artist);

        //获取专辑图片
        LastFmClient.getsInstance(context).getAlbumInfo(new AlbumQuery(album.album_name.toString(), album.album_artist.toString()), new AlbuminfoListener() {
            @Override
            public void albumInfoSucess(LastfmAlbum album) {
                if(album != null) {
                    Glide.with(context)
                            .load(album.mArtwork.get(2).mUrl)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    holder.cover.setImageBitmap(resource);
                                    new Palette.Builder(resource)
                                            .generate(new Palette.PaletteAsyncListener(){
                                                @Override
                                                public void onGenerated(Palette palette) {
                                                    //获取到充满活力的这种色调
                                                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                                                    if(vibrant != null) {
                                                        //背景颜色
                                                        int color = vibrant.getRgb();
                                                        holder.footerView.setBackgroundColor(color);
                                                        //标题颜色
                                                        int titleTextColor = vibrant.getTitleTextColor();
                                                        holder.title.setTextColor(titleTextColor);
                                                        holder.artist.setTextColor(titleTextColor);
                                                    }else{
                                                        //获取柔和的色调
                                                        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                                                        if(mutedSwatch != null) {
                                                            //背景颜色
                                                            int color = mutedSwatch.getRgb();
                                                            holder.footerView.setBackgroundColor(color);
                                                            //标题颜色
                                                            int titleTextColor = mutedSwatch.getTitleTextColor();
                                                            holder.title.setTextColor(titleTextColor);
                                                            holder.artist.setTextColor(titleTextColor);
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });
                }else{//网络获取数据失败
                    Glide.with(context)
                            .load(R.drawable.mybest)
                            .asBitmap()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    holder.cover.setImageBitmap(resource);
                                    new Palette.Builder(resource)
                                            .generate(new Palette.PaletteAsyncListener(){
                                                @Override
                                                public void onGenerated(Palette palette) {
                                                    //获取到充满活力的这种色调
                                                    Palette.Swatch vibrant = palette.getVibrantSwatch();
                                                    if(vibrant != null) {
                                                        //背景颜色
                                                        int color = vibrant.getRgb();
                                                        holder.footerView.setBackgroundColor(color);
                                                        //标题颜色
                                                        int titleTextColor = vibrant.getTitleTextColor();
                                                        holder.title.setTextColor(titleTextColor);
                                                        holder.artist.setTextColor(titleTextColor);
                                                    }else{
                                                        //获取柔和的色调
                                                        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                                                        if(mutedSwatch != null) {
                                                            //背景颜色
                                                            int color = mutedSwatch.getRgb();
                                                            holder.footerView.setBackgroundColor(color);
                                                            //标题颜色
                                                            int titleTextColor = mutedSwatch.getTitleTextColor();
                                                            holder.title.setTextColor(titleTextColor);
                                                            holder.artist.setTextColor(titleTextColor);
                                                        }
                                                    }
                                                }
                                            });
                                }
                            });
                }

            }

            @Override
            public void albumInfoFailed() {

            }
        });

    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class LocalAlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private View content;
        private ImageView cover;
        private TextView title, artist;
        private LinearLayout footerView;

        public LocalAlbumViewHolder(View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.card_view);
            footerView = (LinearLayout) itemView.findViewById(R.id.album_footer);
            cover = (ImageView) itemView.findViewById(R.id.cover);
            title = (TextView) itemView.findViewById(R.id.album_title);
            artist = (TextView) itemView.findViewById(R.id.album_artist);
            content.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
