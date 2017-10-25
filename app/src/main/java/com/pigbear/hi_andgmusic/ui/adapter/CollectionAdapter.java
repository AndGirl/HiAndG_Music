package com.pigbear.hi_andgmusic.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pigbear.hi_andgmusic.R;
import com.pigbear.hi_andgmusic.data.CollectionBean;
import com.pigbear.hi_andgmusic.db.CollectionManager;

import java.util.List;

/**
 * Created by 杨阳洋 on 2017/10/25.
 * usg:收藏夹适配器
 */

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder>{

    private List<CollectionBean> collectionList;
    private Context context;
    private OnItemClickListener<CollectionBean> itemClickListener;
    private boolean inPopupMenu;

    public CollectionAdapter(Context context){
        this.context = context;
        inPopupMenu = false;
        update();
    }

    public CollectionAdapter(Context context, boolean inPopupMenu) {
        this.context = context;
        this.inPopupMenu = inPopupMenu;
        update();
    }

    public void update() {
        collectionList = CollectionManager.getInstance().getCollectionList();
        for (CollectionBean bean : collectionList){
            if(bean.getId() == -1) {
                collectionList.remove(bean);
            }
        }
//        collectionList.add(createDefault());
        notifyDataSetChanged();
    }

    /**
     * 创建默认的收藏夹
     * @return
     */
    private CollectionBean createDefault(){
        CollectionBean bean = new CollectionBean();
        bean.setId(-1);
        return bean;
    }

    public void deleteCollection(CollectionBean bean){
        if(collectionList.contains(bean)) {
            collectionList.remove(bean);
        }
        notifyDataSetChanged();
    }

    @Override
    public CollectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_collection_listitem, parent, false);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CollectionViewHolder holder, int position) {
        final CollectionBean collectionBean = collectionList.get(position);
        if(collectionBean.getId() == -1) {
            holder.setting.setVisibility(View.GONE);
            holder.count.setVisibility(View.GONE);
            holder.title.setText(R.string.collection_create);
            holder.cover.setImageResource(R.drawable.ah1);
            holder.collectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onItemClick(null,-1);
                    }
                }
            });
        }else{
            holder.setting.setVisibility(View.VISIBLE);
            holder.count.setVisibility(View.VISIBLE);
            holder.title.setText(collectionBean.getTitle());
            String count = String.format(context.getString(R.string.song), collectionBean.getCount());
            holder.count.setText(count);
            Glide.with(context)
                    .load(collectionBean.getCoverUrl())
                    .placeholder(R.drawable.ah1)
                    .into(holder.cover);
            holder.collectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemClickListener != null) {
                        itemClickListener.onItemClick(collectionBean,holder.getAdapterPosition());
                    }
                    if(inPopupMenu) {
                        return;
                    }
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        return;
                    }
                }
            });
            if (inPopupMenu) {
                holder.setting.setVisibility(View.GONE);
            }
            holder.setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemSettingClick(holder.setting, collectionBean, holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public OnItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class CollectionViewHolder extends RecyclerView.ViewHolder {

        private View collectLayout;
        private ImageView cover;
        private TextView title, count;
        private AppCompatImageView setting;

        public CollectionViewHolder(View itemView) {
            super(itemView);
            collectLayout = itemView.findViewById(R.id.collection_item);
            cover = (ImageView) itemView.findViewById(R.id.collection_cover);
            count = (TextView) itemView.findViewById(R.id.collection_count);
            title = (TextView) itemView.findViewById(R.id.collection_title);
            setting = (AppCompatImageView) itemView.findViewById(R.id.collection_setting);
        }
    }

}
