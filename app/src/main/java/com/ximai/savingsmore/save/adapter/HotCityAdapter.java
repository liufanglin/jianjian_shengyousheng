package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.modle.UserEntity;

import java.util.List;

public class HotCityAdapter extends RecyclerView.Adapter<HotCityAdapter.HotViewHolder> {

    private final Context context;
    private final List<UserEntity> list;

    /**
     * iten点击事件
     */
    private OnItemClickListener onItemClickListener;
    public interface  OnItemClickListener{
        void  onViewClcik(int postion, List<UserEntity> list);
    }
    public void setViewClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    public HotCityAdapter(Context context, List<UserEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public HotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false);
        return new HotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HotViewHolder holder, final int position) {
        holder.tv_name.setText(list.get(position).getNick());
        holder.tv_mobile.setText("+"+list.get(position).getMobile());

        /**
         * recycleview - itemclick
         */
        holder.itemView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onViewClcik(position,list);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class HotViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_mobile;
        HotViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_mobile = (TextView) itemView.findViewById(R.id.tv_mobile);
        }
    }
}
