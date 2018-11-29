package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.modle.RebateApplyCenterBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxing on 2018/1/8 0008.
 * 返利申请中心数据适配器
 */

public class RebateApplyCenterAdapter extends RecyclerView.Adapter<RebateApplyCenterAdapter.ViewHolder> {

    private Context context;
    private OnItemClickListener onItemClickListener;//点击事件
    private OnLongClickListener onLongClickClickListener;//长按点击事件
    private List<RebateApplyCenterBean.MainData> list = new ArrayList<>();
    private OnItenClickDeleteListener onItenClickDeleteListener;//删除的点击事件

    public RebateApplyCenterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rebate_applycenter, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (null != list){
            String state = list.get(position).State;//1申请中 - 2通过 - 3驳回

            if ("1".equals(state)){
                holder.iv_fanli.setBackgroundResource(R.mipmap.fanli_shenhezhong);
            }else if ("2".equals(state)){
                holder.iv_fanli.setBackgroundResource(R.mipmap.fanli_yifanli);
            }else if ("3".equals(state)){
                holder.iv_fanli.setBackgroundResource(R.mipmap.fanli_weitongguo);
            }

            holder.tv_bianhao.setText(list.get(position).Number);
            holder.tv_date.setText(list.get(position).CreateTimeName);

            /**
             * 点击查看详情
             */
            holder.tv_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onViewClcik(position,list);
                }
            });

            /**
             * 长按删除
             */
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    if (null != onLongClickClickListener){
//                        onLongClickClickListener.onLongClick(position,list);
//                    }
//                    return true;
//                }
//            });

            /**
             * 点击删除的按钮
             */
            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItenClickDeleteListener.onViewClickDelete(position,list);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 设置数据
     * @param list
     */
    public void setData(List<RebateApplyCenterBean.MainData> list) {
        this.list = list;
    }

    /**
     * holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_bianhao;
        private final TextView tv_date;
        private final TextView tv_detail;
        private final ImageView iv_fanli;
        private final ImageView iv_delete;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_bianhao = (TextView) itemView.findViewById(R.id.tv_bianhao);//编号
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);//日期
            tv_detail = (TextView) itemView.findViewById(R.id.tv_detail);//查看详情
            iv_fanli = (ImageView) itemView.findViewById(R.id.iv_fanli);//是否返利
            iv_delete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }

    /**
     * 查看详情的点击事件
     */
    public interface  OnItemClickListener{
        void  onViewClcik(int postion , List<RebateApplyCenterBean.MainData> list);
    }
    public void setViewClickListener(RebateApplyCenterAdapter.OnItemClickListener listener){
        this.onItemClickListener=listener;
    }

    /**
     * 长按点击事件
     */
    public interface OnLongClickListener{
        void onLongClick(int postion , List<RebateApplyCenterBean.MainData> list);
    }
    public void setLongClickListener(RebateApplyCenterAdapter.OnLongClickListener listener){
        this.onLongClickClickListener=listener;
    }

    /**
     * 点击删除的接口
     */
    public interface OnItenClickDeleteListener{
        void onViewClickDelete(int postion , List<RebateApplyCenterBean.MainData> list);
    }
    public void setViewClickDeleteListener(RebateApplyCenterAdapter.OnItenClickDeleteListener listener){
            this.onItenClickDeleteListener = listener;
    }
}