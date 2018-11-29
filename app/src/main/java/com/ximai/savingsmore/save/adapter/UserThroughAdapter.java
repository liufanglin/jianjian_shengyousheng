package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.UserThroughBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxk on 2017/12/18 0018.
 */

public class UserThroughAdapter extends RecyclerView.Adapter<UserThroughAdapter.ViewHolder>{


    private Context context;
    private List<UserThroughBean.MainData> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    /**
     * 设置数据
     */
    public void setData(List<UserThroughBean.MainData> mainData) {
        this.list = mainData;
    }

    public UserThroughAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_through, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list != null){
            holder.tv_id.setText(position + 1 +"");

            if ( null == list.get(position).Name){
                holder.tv_vipname.setText("");
            }else{
                holder.tv_vipname.setText(list.get(position).Name);
            }

            if ( null == list.get(position).Sex){
                holder.tv_sex.setText("");
            }else{
                holder.tv_sex.setText(list.get(position).Sex);
            }

            if ( null == list.get(position).Post){
                holder.tv_post.setText("");
            }else{
                holder.tv_post.setText(list.get(position).Post);
            }

            if ( null == list.get(position).Address){
                holder.tv_address.setText("");
            }else{
                holder.tv_address.setText(list.get(position).Address);
            }

            if ( null == list.get(position).JoinTimeName){
                holder.tv_time.setText("");
            }else{
                holder.tv_time.setText(list.get(position).JoinTimeName);
            }
        }

//        /**
//         * 设置布局监听
//         */
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickListener.onViewClcik(position,list.get(position).Id);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_id;
        public TextView tv_vipname;
        public TextView tv_sex;
        public TextView tv_post;
        public TextView tv_address;
        public TextView tv_time;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_vipname = (TextView) itemView.findViewById(R.id.tv_vipname);//会员名
            tv_sex = (TextView) itemView.findViewById(R.id.tv_sex);//性别
            tv_post = (TextView) itemView.findViewById(R.id.tv_post);//职位
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);//地址
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);//时间
        }
    }

    /**
     * iten点击事件
     */
    public interface  OnItemClickListener{
        void  onViewClcik(int postion, String id);
    }
    public void setViewClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
