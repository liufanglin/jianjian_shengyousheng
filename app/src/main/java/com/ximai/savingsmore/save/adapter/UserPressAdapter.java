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
import com.ximai.savingsmore.save.modle.UserPressBean;
import com.ximai.savingsmore.save.modle.UserThroughBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxk on 2017/12/18 0018.
 */

public class UserPressAdapter extends RecyclerView.Adapter<UserPressAdapter.ViewHolder>{

    private Context context;
    private List<UserPressBean.MainData> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    /**
     * 设置数据
     */
    public void setData(List<UserPressBean.MainData> mainData) {
        this.list = mainData;
    }

    public UserPressAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_press, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (list != null){
            holder.tv_id.setText(position + 1 +"");

            if ( null == list.get(position).Name){
                holder.tv_vipname.setText("");
            }else{
                holder.tv_vipname.setText(list.get(position).Name);
            }

            if ( null == list.get(position).OnlinePay){
                holder.tv_linipay.setText("");
            }else{
                holder.tv_linipay.setText(list.get(position).OnlinePay);
            }

            if ( null == list.get(position).LinePay){
                holder.tv_mendianpay.setText("");
            }else{
                holder.tv_mendianpay.setText(list.get(position).LinePay);
            }

            if ( null == list.get(position).Favourite){
                holder.tv_collect.setText("");
            }else{
                holder.tv_collect.setText(list.get(position).Favourite);
            }

            if ( null == list.get(position).Shared){
                holder.tv_shark.setText("");
            }else{
                holder.tv_shark.setText(list.get(position).Shared);
            }

            if ( null == list.get(position).Hit){
                holder.tv_through.setText("");
            }else{
                holder.tv_through.setText(list.get(position).Hit);
            }

            if (false == list.get(position).IsSelect){
                holder.iv_choose.setVisibility(View.GONE);
            }else{
                holder.iv_choose.setVisibility(View.VISIBLE);
            }
        }

        /**
         * 设置布局监听
         */
        holder.rl_vipname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onViewClcik(position,list,holder.iv_choose);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_id;
        public TextView tv_vipname;
        public TextView tv_linipay;
        public TextView tv_mendianpay;
        public TextView tv_collect;
        public TextView tv_shark;
        public TextView tv_through;
        private ImageView iv_choose;
        private RelativeLayout rl_vipname;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_vipname = (TextView) itemView.findViewById(R.id.tv_vipname);//会员名
            tv_linipay = (TextView) itemView.findViewById(R.id.tv_linipay);//在线支付
            tv_mendianpay = (TextView) itemView.findViewById(R.id.tv_mendianpay);//门店支付
            tv_collect = (TextView) itemView.findViewById(R.id.tv_collect);//收藏
            tv_shark = (TextView) itemView.findViewById(R.id.tv_shark);//分享
            tv_through = (TextView) itemView.findViewById(R.id.tv_through);//浏览
            iv_choose = (ImageView) itemView.findViewById(R.id.iv_choose);
            rl_vipname = (RelativeLayout) itemView.findViewById(R.id.rl_vipname);
        }
    }

    /**
     * iten点击事件
     */
    public interface  OnItemClickListener{
        void  onViewClcik(int postion, List<UserPressBean.MainData> list, ImageView iv_choose);
    }
    public void setViewClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
