package com.pigbear.hi_andgmusic.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintImageView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.data.ArtistInfo;
import com.pigbear.hi_andgmusic.data.ArtistQuery;
import com.pigbear.hi_andgmusic.data.LastfmArtist;
import com.pigbear.hi_andgmusic.net.LastFmClient;
import com.pigbear.hi_andgmusic.net.callbacks.ArtistInfoListener;
import com.pigbear.hi_andgmusic.service.MusicPlayManager;

import java.util.List;

/**
 * Created by 杨阳洋 on 2017/10/30.
 * usg:
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ArtistViewHolder> {
    private List<ArtistInfo> mList;
    private Context mContext;

    public ArtistAdapter(List<ArtistInfo> list, Context context) {
        this.mList = list;
        this.mContext = context;
    }

    //更新adpter的数据
    public void updateDataSet(List<ArtistInfo> list) {
        this.mList = list;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_common_item, parent, false);
        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ArtistViewHolder holder, int position) {
        ArtistInfo model = mList.get(position);
        holder.mainTitle.setText(model.artist_name);
        holder.title.setText(model.number_of_tracks + "首");
        if(MusicPlayManager.getInstance().isPlaying()) {
            //根据播放中的歌曲的歌手名判断当前歌手专辑条目是否有播放的歌曲
            if(MusicPlayManager.getInstance().getPlayingSong().getArtistId() == model.artist_id) {
                holder.moreOverflow.setImageResource(R.drawable.song_play_icon);
                holder.moreOverflow.setImageTintList(R.color.theme_color_primary);
            }else{
                holder.moreOverflow.setImageResource(R.drawable.ic_more_vert);
            }
        }

        //加载歌手图片
        LastFmClient.getsInstance(mContext).getArtistInfo(new ArtistQuery(model.artist_name.toString()), new ArtistInfoListener() {
            @Override
            public void artistInfoSucess(LastfmArtist artist) {
                if(artist != null && artist.mArtwork != null) {
                    holder.draweeView.setImageURI(Uri.parse(artist.mArtwork.get(2).mUrl));
                }
            }

            @Override
            public void artistInfoFailed() {
                Log.e("TAG", "失败了");
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        SimpleDraweeView draweeView;
        TextView mainTitle, title;
        TintImageView moreOverflow;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            this.mainTitle = (TextView) itemView.findViewById(R.id.viewpager_list_toptext);
            this.title = (TextView) itemView.findViewById(R.id.viewpager_list_bottom_text);
            this.draweeView = (SimpleDraweeView) itemView.findViewById(R.id.viewpager_list_img);
            this.moreOverflow = (TintImageView) itemView.findViewById(R.id.viewpager_list_button);

            //为每个条目设置监听
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            
        }
    }

}
