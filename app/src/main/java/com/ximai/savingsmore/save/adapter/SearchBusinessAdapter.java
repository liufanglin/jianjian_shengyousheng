package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.modle.Business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/15 0015.
 */

public class SearchBusinessAdapter extends RecyclerView.Adapter<SearchBusinessAdapter.ViewHolder> {


    private Context context;
    private List<Business> list = new ArrayList<>();
    public OnItenClickListener listener;

    public SearchBusinessAdapter(Context context){
        this.context = context;
    }

    /**
     * 设置数据
     * @param list
     */
    public void setData(List<Business> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if (list != null){
            holder.tv_company.setText(list.get(position).StoreName);
            holder.tv_address.setText(list.get(position).Province + list.get(position).City);

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });

            holder.rl_goBussiness.setOnClickListener(new View.OnClickListener() {
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

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_address;
        public TextView tv_company;
        private RelativeLayout rl_goBussiness;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_company = (TextView) itemView.findViewById(R.id.tv_company);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
            rl_goBussiness = (RelativeLayout) itemView.findViewById(R.id.rl_goBussiness);
        }
    }

    /**
     * 提供一个接口实现点击事件
     */
    public interface OnItenClickListener{
        void onItenClick(int position, List<Business> list);
    }
    public void setOnItenClickListener(SearchBusinessAdapter.OnItenClickListener listener){
        this.listener = listener;
    }
}
