package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.modle.ProduceBean;
import com.ximai.savingsmore.save.view.ChoiceItemLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/15 0015.
 */

public class ServiceFragmentAdapter extends RecyclerView.Adapter<ServiceFragmentAdapter.ViewHolder>{

    private Context context;
    private List<ProduceBean> list = new ArrayList<>();
    private OnItenClickListener listener;

    public ServiceFragmentAdapter(Context context){
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.produceservice_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        ChoiceItemLayout layout = (ChoiceItemLayout) holder.itemView;//下面三行设置是佛是选中状态
        ProduceBean produceBean = list.get(position);
        layout.setChecked(produceBean.isChecked());

        if (list != null){
            holder.iv_photo.setImageResource(list.get(position).getImage());
            holder.tv_title.setText(list.get(position).getFont());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItenClick(position,list);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * init - data
     * @param list
     */
    public void setData(List<ProduceBean> list) {
        this.list = list;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_photo;
        public TextView tv_title;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_photo = (ImageView) itemView.findViewById(R.id.iv_photo);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    /**
     * 提供一个接口实现点击事件
     */
    public interface OnItenClickListener{
        void onItenClick(int position, List<ProduceBean> list);
    }
    public void setOnItenClickListener(ServiceFragmentAdapter.OnItenClickListener listener){
        this.listener = listener;
    }
}
