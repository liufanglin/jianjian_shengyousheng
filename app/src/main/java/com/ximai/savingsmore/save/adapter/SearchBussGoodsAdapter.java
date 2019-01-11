package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.view.imagepicker.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/11 0011.
 */

public class SearchBussGoodsAdapter extends RecyclerView.Adapter<SearchBussGoodsAdapter.ViewHolder>{

    private Context context;
    private List<Goods> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public SearchBussGoodsAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Goods> list) {
        this.list = list;
    }

    /**
     * layout
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.salegood_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 设置数据f
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list != null){
//            MyImageLoader.displayDefaultImage(URLText.img_url + list.get(position).Image, holder.image);
            Glide.with(context).load(URLText.img_url + list.get(position).Image).into(holder.image);
//            holder.tv_favourable.setText(list.get(position).Preferential);
            if (list.get(position).Preferential.length() > 5){
                holder.tv_favourable.setText(list.get(position).Preferential.substring(0,5)+"...");
            }else{
                holder.tv_favourable.setText(list.get(position).Preferential);
            }
            holder.tv_price.setText(list.get(position).Currency+" "+list.get(position).Price);
            holder.tv_agoprice.setText(list.get(position).Currency+" "+list.get(position).OriginalPrice);
            holder.name.setText(list.get(position).Name);
            holder.tv_assesss.setText(list.get(position).FavouriteCount);

            if (null == list.get(position).CommentCount ){
                holder.tv_comments.setText("0");//评论
            }else{
                holder.tv_comments.setText(list.get(position).CommentCount);//评论
            }

            holder.tv_sharks.setText(list.get(position).SharedCount);

            if (null == list.get(position).HitCount ){
                holder.tv_lookthroughs.setText("0");//浏览
            }else{
                holder.tv_lookthroughs.setText(list.get(position).HitCount);//浏览
            }

//            holder.tv_business.setText(list.get(position).StoreName);
            if (null != list.get(position).ChainStores){//---------------------------------------------------------进行分店的实现
                if (list.get(position).ChainStores.size() > 0){
                    String name = list.get(position).ChainStores.get(0).Name;
                    holder.tv_business.setText(name);
                }else{
                    if (null != list.get(position).StoreName) {//商家店铺名称
                        holder.tv_business.setText(list.get(position).StoreName);
                    }
                }
            }else{
                if (null != list.get(position).StoreName) {//商家店铺名称
                    holder.tv_business.setText(list.get(position).StoreName);
                }
            }

            if (null != list.get(position).Province && null != list.get(position).City && null != list.get(position).Address){
                holder.tv_address.setText(list.get(position).Country+" · "+list.get(position).Province+list.get(position).City+list.get(position).Address);
            }

//            holder.statr_time.setText(list.get(position).StartTimeName.substring(0,10));
//            holder.end_time.setText(list.get(position).EndTimeName.substring(0,10));

            if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)){
                holder.statr_time.setText(list.get(position).StartTimeName.substring(0,10));
            }

            if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)){
                holder.end_time.setText(list.get(position).EndTimeName.substring(0,10));
                try {
                    long time= Utils.dateToStamp(list.get(position).EndTimeName)-System.currentTimeMillis();
                    if (time<3*24*60*60*1000&&time>0){
                        Glide.with(context).load(R.mipmap.end_time_git).into(holder.iv_endtime);
                        holder.iv_endtime.setVisibility(View.VISIBLE);
                    }else {
                        holder.iv_endtime.setVisibility(View.GONE);

                    }
                }catch (Exception e){

                }

            }
            if (null == list.get(position).SaleCount){//销量
                holder.tv_volume.setVisibility(View.GONE);
                holder.tv_volume.setText("销 "+0);
            }else{
                if ("0".equals(list.get(position).SaleCount)){
                    holder.tv_volume.setVisibility(View.GONE);
                }else {
                    holder.tv_volume.setText("销 "+list.get(position).SaleCount);
                    holder.tv_volume.setVisibility(View.GONE);
                }
            }
            holder.tv_care.setText("关注"+list.get(position).CareCount);
            holder.tv_store_count.setText("到店人次"+list.get(position).StoreCount);

        }
        /**
         * 设置布局监听
         */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onViewClcik(position,list.get(position).Id);
            }
        });
    }

    /**
     * holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_assesss;
        private TextView tv_comments;
        private TextView tv_sharks;
        private TextView tv_business;
        private TextView tv_address;
        private TextView statr_time;
        private TextView end_time;
        private TextView tv_volume;
        private ImageView image,iv_endtime;

        private TextView name;
        private TextView tv_price;
        private TextView tv_agoprice;
        private TextView tv_favourable;
        private TextView tv_lookthroughs;
        public TextView tv_store_count;
        public TextView tv_care;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            tv_assesss = (TextView) itemView.findViewById(R.id.tv_assesss);//收藏
            tv_comments = (TextView) itemView.findViewById(R.id.tv_comments);//评论
            tv_sharks = (TextView) itemView.findViewById(R.id.tv_sharks);//分享
            tv_business = (TextView) itemView.findViewById(R.id.tv_business);//公司
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);//地址和
            statr_time = (TextView) itemView.findViewById(R.id.statr_time);//开水时间
            end_time = (TextView) itemView.findViewById(R.id.end_time);//结束时间
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);//价格
            tv_agoprice = (TextView) itemView.findViewById(R.id.tv_agoprice);//以前价格
            tv_favourable = (TextView) itemView.findViewById(R.id.tv_favourable);//优惠
            tv_volume = (TextView) itemView.findViewById(R.id.tv_volume);//销量
            tv_lookthroughs = (TextView) itemView.findViewById(R.id.tv_lookthroughs);//浏览
            tv_agoprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_store_count= (TextView) itemView.findViewById(R.id.tv_store_count);
            tv_care= (TextView) itemView.findViewById(R.id.tv_care);
            iv_endtime= (ImageView) itemView.findViewById(R.id.iv_endtime);

        }
    }

    /**
     * iten点击事件
     */
    public interface  OnItemClickListener{
        void  onViewClcik(int postion, String id);
    }
    public void setViewClickListener(SearchBussGoodsAdapter.OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
