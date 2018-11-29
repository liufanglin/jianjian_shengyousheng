package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.modle.MixedContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wangqiang on 2017/8/16.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {


    private List<MixedContent> dataList =  new ArrayList<>();
    private Context context;

    public void setData(List<MixedContent> dataList) {
        this.dataList = dataList;
    }

    public DataAdapter(List<MixedContent> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    public DataAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mixed, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        int newPos = position % dataList.size();
        MixedContent item = dataList.get(newPos);
        if (item.getType() == 0) {
            Glide.with(context).load(item.getResId()).thumbnail(0.1f).into(holder.ivThumbnail);

        } else {
            try {
                Bitmap bitmap = retriveVideoFrameFromVideo(item.getUrl());
                Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                //显示视频缩略图
                holder.ivThumbnail.setImageDrawable(drawable);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

        //itemView 的点击事件
        if (mItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = holder.getAdapterPosition();
                    mItemClickListener.onItemClick(adapterPosition, v);
                }
            });
        }
    }

    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }




    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }




    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ivThumbnail = (ImageView) itemView.findViewById(R.id.iv_thumbnail);
        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
