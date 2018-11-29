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
import com.ximai.savingsmore.save.modle.BeanPressBean;
import com.ximai.savingsmore.save.modle.UserPressBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxk on 2017/12/18 0018.
 */

public class BussPressAdapter extends RecyclerView.Adapter<BussPressAdapter.ViewHolder>{


    private Context context;
    private List<BeanPressBean.MainData> list = new ArrayList<>();
    private OnItemClickListener1 onItemClickListener1;
    private OnItemClickListener2 onItemClickListener2;
    private OnItemClickListener3 onItemClickListener3;
    private OnItemClickListener4 onItemClickListener4;
    private OnItemClickListener5 onItemClickListener5;
    private OnItemClickListener6 onItemClickListener6;
    private View view;

    /**
     * 设置数据
     */
    public void setData(List<BeanPressBean.MainData> mainData) {
        this.list = mainData;
    }

    public BussPressAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.buss_press, parent, false);
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

            if (false == list.get(position).IsSelect1){
                holder.iv_vipnamechoose.setVisibility(View.GONE);
            }else{
                holder.iv_vipnamechoose.setVisibility(View.VISIBLE);
            }

            if (false == list.get(position).IsSelect2){
                holder.iv_linipaychoose.setVisibility(View.GONE);
            }else{
                holder.iv_linipaychoose.setVisibility(View.VISIBLE);
            }

            if (false == list.get(position).IsSelect3){
                holder.iv_mendianpaychoose.setVisibility(View.GONE);
            }else{
                holder.iv_mendianpaychoose.setVisibility(View.VISIBLE);
            }

            if (false == list.get(position).IsSelect4){
                holder.iv_collectchoose.setVisibility(View.GONE);
            }else{
                holder.iv_collectchoose.setVisibility(View.VISIBLE);
            }

            if (false == list.get(position).IsSelect5){
                holder.iv_sharkchoose.setVisibility(View.GONE);
            }else{
                holder.iv_sharkchoose.setVisibility(View.VISIBLE);
            }

            if (false == list.get(position).IsSelect6){
                holder.iv_throughchoose.setVisibility(View.GONE);
            }else{
                holder.iv_throughchoose.setVisibility(View.VISIBLE);
            }

        }

        /**
         * 商品名称
         */
        holder.rl_vipname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener1.onViewClcik1(position,list);
            }
        });

        /**
         * 在线支付
         */
        holder.rl_linepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener2.onViewClcik2(position,list);
            }
        });
        /**
         * 门店支付
         */
        holder.rl_mendianpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener3.onViewClcik3(position,list);
            }
        });

        /**
         * 收藏
         */
        holder.rl_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener4.onViewClcik4(position,list);
            }
        });

        /**
         * 分享
         */
        holder.rl_shark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener5.onViewClcik5(position,list);
            }
        });

        /**
         * 浏览
         */
        holder.rl_through.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener6.onViewClcik6(position,list);
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
        private ImageView iv_vipnamechoose;
        private ImageView iv_linipaychoose;
        private ImageView iv_mendianpaychoose;
        private ImageView iv_collectchoose;
        private ImageView iv_sharkchoose;
        private ImageView iv_throughchoose;
        private RelativeLayout rl_vipname;
        private RelativeLayout rl_linepay;
        private RelativeLayout rl_mendianpay;
        private RelativeLayout rl_collect;
        private RelativeLayout rl_shark;
        private RelativeLayout rl_through;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_id = (TextView) itemView.findViewById(R.id.tv_id);
            tv_vipname = (TextView) itemView.findViewById(R.id.tv_vipname);//会员名
            tv_linipay = (TextView) itemView.findViewById(R.id.tv_linipay);//在线支付
            tv_mendianpay = (TextView) itemView.findViewById(R.id.tv_mendianpay);//门店支付
            tv_collect = (TextView) itemView.findViewById(R.id.tv_collect);//收藏
            tv_shark = (TextView) itemView.findViewById(R.id.tv_shark);//分享
            tv_through = (TextView) itemView.findViewById(R.id.tv_through);//浏览
            rl_vipname = (RelativeLayout) itemView.findViewById(R.id.rl_vipname);
            rl_linepay = (RelativeLayout) itemView.findViewById(R.id.rl_linepay);
            rl_mendianpay = (RelativeLayout) itemView.findViewById(R.id.rl_mendianpay);
            rl_collect = (RelativeLayout) itemView.findViewById(R.id.rl_collect);
            rl_shark = (RelativeLayout) itemView.findViewById(R.id.rl_shark);
            rl_through = (RelativeLayout) itemView.findViewById(R.id.rl_through);
            iv_vipnamechoose = (ImageView) itemView.findViewById(R.id.iv_vipnamechoose);
            iv_linipaychoose = (ImageView) itemView.findViewById(R.id.iv_linipaychoose);
            iv_mendianpaychoose = (ImageView) itemView.findViewById(R.id.iv_mendianpaychoose);
            iv_collectchoose = (ImageView) itemView.findViewById(R.id.iv_collectchoose);
            iv_sharkchoose = (ImageView) itemView.findViewById(R.id.iv_sharkchoose);
            iv_throughchoose = (ImageView) itemView.findViewById(R.id.iv_throughchoose);
        }
    }

    /**
     * iten点击事件1
     */
    public interface  OnItemClickListener1{
        void  onViewClcik1(int postion, List<BeanPressBean.MainData> list);
    }
    public void setViewClickListener1(OnItemClickListener1 listener){
        this.onItemClickListener1=listener;
    }

    /**
     * iten点击事件2
     */
    public interface  OnItemClickListener2{
        void  onViewClcik2(int postion, List<BeanPressBean.MainData> list);
    }
    public void setViewClickListener2(OnItemClickListener2 listener){
        this.onItemClickListener2=listener;
    }

    /**
     * iten点击事件3
     */
    public interface  OnItemClickListener3{
        void  onViewClcik3(int postion, List<BeanPressBean.MainData> list);
    }
    public void setViewClickListener3(OnItemClickListener3 listener){
        this.onItemClickListener3=listener;
    }

    /**
     * iten点击事件2
     */
    public interface  OnItemClickListener4{
        void  onViewClcik4(int postion, List<BeanPressBean.MainData> list);
    }
    public void setViewClickListener4(OnItemClickListener4 listener){
        this.onItemClickListener4=listener;
    }
    /**
     * iten点击事件2
     */
    public interface  OnItemClickListener5{
        void  onViewClcik5(int postion, List<BeanPressBean.MainData> list);
    }
    public void setViewClickListener5(OnItemClickListener5 listener){
        this.onItemClickListener5=listener;
    }
    /**
     * iten点击事件2
     */
    public interface  OnItemClickListener6{
        void  onViewClcik6(int postion, List<BeanPressBean.MainData> list);
    }
    public void setViewClickListener6(OnItemClickListener6 listener){
        this.onItemClickListener6=listener;
    }
}
