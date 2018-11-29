package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.activity.ZoneNumberActivity;
import com.ximai.savingsmore.save.modle.UserEntity;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.indexablerv.IndexableHeaderAdapter;

public class BannerHeaderAdapter extends IndexableHeaderAdapter {
    private static final int TYPE = 1;
    private final Context context;

    /**
     * 热门城市数据
     */
    private String[] city = {"中国大陆","日本","美国","韩国","澳大利亚","新加坡","加拿大","法国","马来西亚","俄罗斯","德国","泰国"};

    private String[] cityCode = {"86","81","1","82","61","65","1","33","60","7","49","66"};
    /**
     * 热门城市的集合
     */
    private ArrayList<UserEntity> list;

    public BannerHeaderAdapter(String index, String indexTitle, List datas, Context context) {
        super(index, indexTitle, datas);
        this.context = context;
    }

    @Override
    public int getItemViewType() {
        return TYPE;
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_city_header, parent, false);
        VH holder = new VH(view);
        return holder;
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, Object entity) {
        // 数据源为null时, 该方法不用实现
        final VH vh = (VH) holder;
        list=new ArrayList<>();

        for(int i = 0; i<city.length; i++){
            UserEntity contactEntity = new UserEntity(city[i], cityCode[i]);
            list.add(contactEntity);
        }

        HotCityAdapter hotCityAdapter = new HotCityAdapter(context, list);
        vh.hot_recycleview.setAdapter(hotCityAdapter);

        /**
         * 热门城市
         */
        hotCityAdapter.setViewClickListener(new HotCityAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, List<UserEntity> list) {
                onItemClickListener.onViewClcik(postion,list);
            }
        });
    }

    private class VH extends RecyclerView.ViewHolder {
        private final RecyclerView hot_recycleview;
        public VH(View itemView) {
            super(itemView);
            hot_recycleview = (RecyclerView)itemView.findViewById(R.id.hot_recycleview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            hot_recycleview.setLayoutManager(linearLayoutManager);
        }
    }

    /**
     * iten点击事件
     */
    private OnItemClickListener onItemClickListener;
    public interface  OnItemClickListener{
        void  onViewClcik(int postion, List<UserEntity> list);
    }
    public void setViewClickListener(OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
