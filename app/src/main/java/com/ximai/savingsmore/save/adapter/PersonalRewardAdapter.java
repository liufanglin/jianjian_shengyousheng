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
import com.ximai.savingsmore.save.modle.PersonalRewardBean;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.imagepicker.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2018/1/29 0029.
 * 个人奖赏中心适配器
 */

public class PersonalRewardAdapter extends RecyclerView.Adapter<PersonalRewardAdapter.ViewHolder>{

    private Context context;
    private List<PersonalRewardBean.MainData> rewardList = new ArrayList<>();
    private OnItemClickListener listener;

    public PersonalRewardAdapter(Context context) {
        this.context = context;
    }
    /**
     * 设置数据
     * @param rewardList
     */
    public void setDdata(List<PersonalRewardBean.MainData> rewardList) {
        this.rewardList = rewardList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.personal_reward_iten, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (null != rewardList){
            String state = rewardList.get(position).State;//红包状态   1：未打开 2 :已打开3：申请中 4：提现成功 5;驳回
            if ("1".equals(state)){//未打开
                holder.iv_rewardstate.setBackgroundResource(R.mipmap.iv_reward1);
                holder.tv_rewardstyle.setText("奖赏："+rewardList.get(position).RedPacketTypeName);
                holder.tv_rewarddate.setText("奖励日期："+ rewardList.get(position).CreateTimeName);
                holder.tv_rewardsbtn.setText("打开红包");
                holder.tv_rewardsbtn.setTextColor(Color.parseColor("#ffffff"));
                holder.tv_rewardsbtn.setBackgroundResource(R.drawable.bg_black);
            }else if ("2".equals(state)){//已打开
                holder.iv_rewardstate.setBackgroundResource(R.mipmap.iv_reward2);
                holder.tv_rewardstyle.setText("奖赏："+rewardList.get(position).RedPacketTypeName);
                holder.tv_rewarddate.setText("领取日期："+ rewardList.get(position).OpenTimeName);
                holder.tv_rewardsbtn.setText("获得¥"+UIUtils.formatPrice(Double.parseDouble(rewardList.get(position).Price)));
                holder.tv_rewardsbtn.setTextColor(Color.parseColor("#808080"));
                holder.tv_rewardsbtn.setBackgroundResource(R.drawable.button_gray);
            }else if ("5".equals(state)){//驳回
                holder.iv_rewardstate.setBackgroundResource(R.mipmap.iv_reward2);
                holder.tv_rewardstyle.setText("奖赏："+rewardList.get(position).RedPacketTypeName);
                holder.tv_rewarddate.setText("奖励日期："+ rewardList.get(position).CreateTimeName);
                holder.tv_rewardsbtn.setText("未通过");
                holder.tv_rewardsbtn.setTextColor(Color.parseColor("#808080"));
                holder.tv_rewardsbtn.setBackgroundResource(R.drawable.button_gray);
            }else if ("4".equals(state)){//提现成功
                holder.iv_rewardstate.setBackgroundResource(R.mipmap.iv_reward2);
                holder.tv_rewardstyle.setText("奖赏："+rewardList.get(position).RedPacketTypeName);
                holder.tv_rewarddate.setText("领取日期："+ rewardList.get(position).OpenTimeName);
                holder.tv_rewardsbtn.setText("获得¥"+UIUtils.formatPrice(Double.parseDouble(rewardList.get(position).Price)));
                holder.tv_rewardsbtn.setTextColor(Color.parseColor("#808080"));
                holder.tv_rewardsbtn.setBackgroundResource(R.drawable.button_gray);
            }else if ("3".equals(state)) {
                holder.iv_rewardstate.setBackgroundResource(R.mipmap.iv_reward2);
                holder.tv_rewardstyle.setText("奖赏：" + rewardList.get(position).RedPacketTypeName);
                holder.tv_rewarddate.setText("领取日期：" + rewardList.get(position).OpenTimeName);
                holder.tv_rewardsbtn.setText("获得¥" + UIUtils.formatPrice(Double.parseDouble(rewardList.get(position).Price)));
                holder.tv_rewardsbtn.setTextColor(Color.parseColor("#808080"));
                holder.tv_rewardsbtn.setBackgroundResource(R.drawable.button_gray);
            }
            /**
             * 打开红包
             */
            holder.tv_rewardsbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(position,rewardList);
                }
            });

            /**
             * 如果是红包的打开那么让显示
             */
            if (rewardList.get(position).isOpen){
                holder.iv_rewardstate.setBackgroundResource(R.mipmap.iv_reward2);
                holder.tv_rewardstyle.setText("奖赏："+rewardList.get(position).RedPacketTypeName);
                holder.tv_rewarddate.setText("领取日期："+ Utils.timeStamp2Date(String.valueOf(System.currentTimeMillis()),"yyyy-MM-dd HH:mm"));
                holder.tv_rewardsbtn.setText("获得¥"+"1.88");
                holder.tv_rewardsbtn.setTextColor(Color.parseColor("#808080"));
                holder.tv_rewardsbtn.setBackgroundResource(R.drawable.button_gray);
            }
        }
    }

    @Override
    public int getItemCount() {
        return rewardList.size();
    }

    /**
     * viewholder
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_rewardstate;//图片
        private TextView tv_rewardstyle;//文字
        private TextView tv_rewarddate;//时间
        private TextView tv_rewardsbtn;//打开红包

        public ViewHolder(View itemView) {
            super(itemView);
            iv_rewardstate = (ImageView) itemView.findViewById(R.id.iv_rewardstate);
            tv_rewardstyle = (TextView) itemView.findViewById(R.id.tv_rewardstyle);
            tv_rewarddate = (TextView) itemView.findViewById(R.id.tv_rewarddate);
            tv_rewardsbtn = (TextView) itemView.findViewById(R.id.tv_rewardsbtn);
        }
    }

    /**
     * 打开红包
     */
    public interface OnItemClickListener{
       void onClick(int position, List<PersonalRewardBean.MainData> rewardList);
    }
    public void setOnClickListener(PersonalRewardAdapter.OnItemClickListener listener){
        this.listener = listener;
    }
}