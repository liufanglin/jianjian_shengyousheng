package com.ximai.savingsmore.save.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.OrderProducts;
import com.ximai.savingsmore.save.modle.PersonOrderDetialBean;
import com.ximai.savingsmore.save.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/11 0011.
 */

public class PersonOrderDetailAdapter extends RecyclerView.Adapter<PersonOrderDetailAdapter.ViewHolder>{

    private Context context;
    private List<Goods> list = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private PersonOrderDetialBean orderDetial;

    public PersonOrderDetailAdapter(Context context) {
        this.context = context;
    }

    /**
     * //订单的数据
     */
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
        View view = LayoutInflater.from(context).inflate(R.layout.person_orderdetail, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * 设置数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (list != null){

            if ("非促销品".equals(list.get(position).Name)){
                holder.rl_coll_com_shark.setVisibility(View.INVISIBLE);
                holder.ll_cuxiaodate.setVisibility(View.INVISIBLE);
                holder.tv_agoprice.setVisibility(View.INVISIBLE);
                holder.tv_volume.setVisibility(View.INVISIBLE);

                Glide.with(context).load(URLText.img_url + list.get(position).Image).into(holder.image);
                holder.tv_business.setText(list.get(position).StoreName);
                holder.tv_address.setText(list.get(position).Address);//商家地址
                holder.tv_price.setText(list.get(position).Currency+" " +UIUtils.formatPrice(Double.parseDouble(list.get(position).Price)));
                holder.tv_goodsnumber.setText(list.get(position).Number);
                holder.name.setText("见图片");
                holder.tv_favourable.setText("我要促销");
            }else{
                holder.rl_coll_com_shark.setVisibility(View.VISIBLE);
                holder.ll_cuxiaodate.setVisibility(View.VISIBLE);
                holder.tv_agoprice.setVisibility(View.VISIBLE);
                holder.tv_volume.setVisibility(View.VISIBLE);

                Glide.with(context).load(URLText.img_url + list.get(position).Image).into(holder.image);
//                holder.tv_favourable.setText(list.get(position).Preferential);
                if (list.get(position).Preferential.length() > 5){
                    holder.tv_favourable.setText(list.get(position).Preferential.substring(0,5)+"...");
                }else{
                    holder.tv_favourable.setText(list.get(position).Preferential);
                }
                holder.tv_price.setText(list.get(position).Currency+" "+UIUtils.formatPrice(Double.parseDouble(list.get(position).Price)));
                holder.tv_agoprice.setText(list.get(position).Currency+" "+UIUtils.formatPrice(Double.parseDouble(list.get(position).OriginalPrice)));
                holder.name.setText(list.get(position).Name);
                if (null == list.get(position).FavouriteCount && TextUtils.isEmpty(list.get(position).FavouriteCount)){
                    holder.tv_assesss.setText("0");//收藏
                }else{
                    holder.tv_assesss.setText(list.get(position).FavouriteCount);//收藏
                }
                if (null == list.get(position).CommentCount && TextUtils.isEmpty(list.get(position).CommentCount)){
                    holder.tv_comments.setText("0");//评论
                }else{
                    holder.tv_comments.setText(list.get(position).CommentCount);//评论
                }
                if (null == list.get(position).SharedCount && TextUtils.isEmpty(list.get(position).SharedCount)){
                    holder.tv_sharks.setText("0");//分享
                }else{
                    holder.tv_sharks.setText(list.get(position).SharedCount);
                }
                if (null == list.get(position).HitCount && TextUtils.isEmpty(list.get(position).HitCount)){
                    holder.tv_lookthroughs.setText("0");//浏览
                }else{
                    holder.tv_lookthroughs.setText(list.get(position).HitCount);
                }
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
//                holder.tv_business.setText(list.get(position).StoreName);
//                if (null != list.get(position).Province && null != list.get(position).City && null != list.get(position).Address){
//                    holder.tv_address.setText(list.get(position).Province+list.get(position).City+list.get(position).Address);
//                }
                holder.tv_address.setText(list.get(position).Address);
                if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)){
                    holder.statr_time.setText(list.get(position).StartTimeName.substring(0,10));
                }
                if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)){
                    holder.end_time.setText(list.get(position).EndTimeName.substring(0,10));
                }
                holder.tv_volume.setText("销 "+list.get(position).SalesCount);
                holder.tv_goodsnumber.setText(list.get(position).Number);
                holder.tv_goodsdata.setText(list.get(position).Quantity+"");//购买数量
                MyUserInfoUtils.getInstance().myUserInfo.ProductId = list.get(position).ProductId;//再来一单的ProductId
            }
        }
    }

    public void setOrderDetailData(PersonOrderDetialBean orderDetial) {
        this.orderDetial = orderDetial;
    }

    /**
     * holder
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_cuxiaodate;
        private RelativeLayout rl_coll_com_shark;
        private TextView tv_assesss;
        private TextView tv_comments;
        private TextView tv_sharks;
        private TextView tv_business;
        private TextView tv_address;
        private TextView statr_time;
        private TextView end_time;
        private TextView tv_volume;
        private ImageView image;

        private TextView name;
        private TextView tv_price;
        private TextView tv_agoprice;
        private TextView tv_favourable;
        private TextView tv_goodsnumber;
        private TextView tv_goodsdata;
        private TextView tv_lookthroughs;

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
            tv_goodsnumber = (TextView) itemView.findViewById(R.id.tv_goodsnumber);//促销品名
            tv_goodsdata = (TextView) itemView.findViewById(R.id.tv_goodsdata);//购买数量
            tv_lookthroughs = (TextView) itemView.findViewById(R.id.tv_lookthroughs);//浏览数量
            rl_coll_com_shark = (RelativeLayout) itemView.findViewById(R.id.rl_coll_com_shark);
            ll_cuxiaodate = (LinearLayout) itemView.findViewById(R.id.ll_cuxiaodate);
            tv_agoprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    /**
     * iten点击事件
     */
    public interface  OnItemClickListener{
        void  onViewClcik(int postion, String id);
    }
    public void setViewClickListener(PersonOrderDetailAdapter.OnItemClickListener listener){
        this.onItemClickListener=listener;
    }
}
