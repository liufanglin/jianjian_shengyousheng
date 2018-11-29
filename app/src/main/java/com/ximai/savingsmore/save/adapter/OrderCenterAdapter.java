package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.Order;
import com.ximai.savingsmore.save.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxing on 2017/12/13 0013.
 */

public class  OrderCenterAdapter extends RecyclerView.Adapter<OrderCenterAdapter.ViewHolder>{
    public Context context;
    private List<Order> orderList = new ArrayList<>();//数据源
    private OnItenClickListener listener;
    private OnItenDeleteClickListener listenerDelete;

    public OrderCenterAdapter(Context context) {
        this.context = context;
    }

    /**
     * 设置数据
     * @param order
     */
    public void setDdata(List<Order> order) {
        this.orderList = order;
    }

    /**
     * 数据长度
     * @return
     */
    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_centerce_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (orderList != null){
            holder.bianhao.setText(orderList.get(position).Number);
            holder.price.setText(orderList.get(position).Currency+ UIUtils.formatPrice(Double.parseDouble(orderList.get(position).Price)));
            holder.time.setText(orderList.get(position).CreateTimeName);

            if ("1".equals(orderList.get(position).OrderState)){//未支付
                holder.sate.setText(orderList.get(position).OrderStateName);
                holder.iv_state.setBackgroundResource(R.mipmap.order_weizhifu);
            }else if ("2".equals(orderList.get(position).OrderState)){//2已支付
                holder.sate.setText(orderList.get(position).OrderStateName);
                holder.iv_state.setBackgroundResource(R.mipmap.order_yizhifu);
            }else if ("3".equals(orderList.get(position).OrderState)){//3商家已发货
                holder.sate.setText(orderList.get(position).OrderStateName);
                holder.iv_state.setBackgroundResource(R.mipmap.order_sjyfh);
            }else if ("5".equals(orderList.get(position).OrderState)){//---这个状态是已关闭
                holder.sate.setText(orderList.get(position).OrderStateName);//不需要关闭的状态
                holder.iv_state.setBackgroundResource(R.mipmap.order_quxiao);
            }else if ("6".equals(orderList.get(position).OrderState)){//退款审核中------
                holder.sate.setText(orderList.get(position).OrderStateName);
                holder.iv_state.setBackgroundResource(R.mipmap.order_tuikuansh);
                if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {//2是个人－３是商家
                    holder.sate.setTextColor(Color.parseColor("#CE2020"));
                }
            } else if ("4".equals(orderList.get(position).OrderState)){//4已完成----
                holder.sate.setText(orderList.get(position).OrderStateName);
                holder.iv_state.setBackgroundResource(R.mipmap.order_yishouhuo);
            }else if ("8".equals(orderList.get(position).OrderState)){//商家取消----------
                holder.sate.setText(orderList.get(position).OrderStateName);
                holder.iv_state.setBackgroundResource(R.mipmap.order_gerentuikuan);
            }else if ("7".equals(orderList.get(position).OrderState)){//已退款----------
                holder.sate.setText(orderList.get(position).OrderStateName);
                holder.iv_state.setBackgroundResource(R.mipmap.order_yituikuan);
            }else if ("9".equals(orderList.get(position).OrderState)){//个人已支付取消订单后-----------
                holder.sate.setText(orderList.get(position).OrderStateName);
                holder.iv_state.setBackgroundResource(R.mipmap.order_yizhifuquxiao);
            }
            /**
             * 查看详情
             */
            holder.tv_orderdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItenClick(position,orderList.get(position).Id);
                }
            });
//            /**
//             * 订单删除按钮
//             */
//            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    listenerDelete.onItenDeleteClick(position,orderList.get(position).Id);
//                }
//            });

        }
    }

    /**
     * holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_orderdetail;
        public TextView bianhao;
        public TextView price;
        public TextView time;
        public TextView sate;
        private ImageView iv_state;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_orderdetail = (TextView) itemView.findViewById(R.id.tv_orderdetail);
            iv_state = (ImageView) itemView.findViewById(R.id.iv_state);
            bianhao = (TextView) itemView.findViewById(R.id.bianhao);
            price = (TextView) itemView.findViewById(R.id.price);
            time = (TextView) itemView.findViewById(R.id.time);
            sate = (TextView) itemView.findViewById(R.id.state);
        }
    }

    /**
     * 详情的点击事件
     */
    public interface OnItenClickListener{
        void onItenClick(int position, String id);
    }
    public void setOnItenClickListener(OrderCenterAdapter.OnItenClickListener listener){
        this.listener = listener;
    }

    /**
     * 详情的点击事件
     */
    public interface OnItenDeleteClickListener{
        void onItenDeleteClick(int position, String id);
    }
    public void setOnItenDeleteClickListener(OrderCenterAdapter.OnItenDeleteClickListener listener){
        this.listenerDelete = listener;
    }
}
