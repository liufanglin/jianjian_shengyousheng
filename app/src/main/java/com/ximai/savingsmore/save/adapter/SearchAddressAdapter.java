package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.modle.SearchLocationModle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/15 0015.
 */
public class SearchAddressAdapter extends RecyclerView.Adapter<SearchAddressAdapter.ViewHolder> {

    private Context context;
    private List<SearchLocationModle.MainDataBean> list = new ArrayList<>();
    public OnItenClickListener listener;
    public SearchAddressAdapter(Context context){
        this.context = context;
    }

    /**
     * 设置数据
     * @param list
     */
    public void setData(List<SearchLocationModle.MainDataBean> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_address, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list != null){
            holder.tv_address.setText(list.get(position).getAddress());
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

    static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_address;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);
        }
    }

    /**
     * 提供一个接口实现点击事件
     */
    public interface OnItenClickListener{
        void onItenClick(int position, List<SearchLocationModle.MainDataBean> list);
    }
    public void setOnItenClickListener(SearchAddressAdapter.OnItenClickListener listener){
        this.listener = listener;
    }
}