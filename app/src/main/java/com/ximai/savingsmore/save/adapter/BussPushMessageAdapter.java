package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.modle.BUssPushMessageBean;
import com.ximai.savingsmore.save.modle.UserPressBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xin on 2018/10/16 0018.
 */

public class BussPushMessageAdapter extends RecyclerView.Adapter<BussPushMessageAdapter.ViewHolder>{

    private Context context;
    private List<BUssPushMessageBean.MainData> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setData(List<BUssPushMessageBean.MainData> mainData) {
        this.list = mainData;
    }

    public BussPushMessageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.buss_push_iten, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list != null){
            if ( null == list.get(position).Title){
                holder.tv_title.setText("");
            }else{
                holder.tv_title.setText(list.get(position).Title);
            }

            if ( null == list.get(position).Content){
                holder.tv_count.setText("");
            }else{
                holder.tv_count.setText(list.get(position).Content);
            }

            if ( null == list.get(position).CreateTimeName){
                holder.tv_time.setText("");
            }else{
                holder.tv_time.setText(list.get(position).CreateTimeName);
            }
        }

//        /**
//         * 设置布局监听
//         */
//        holder.rl_vipname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onItemClickListener.onViewClcik(position,list);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_title;
        public TextView tv_count;
        public TextView tv_time;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }

    /**
     * iten点击事件
     */
    public interface  OnItemClickListener{
        void  onViewClcik(int postion, List<BUssPushMessageBean.MainData> list);
    }
    public void setViewClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
