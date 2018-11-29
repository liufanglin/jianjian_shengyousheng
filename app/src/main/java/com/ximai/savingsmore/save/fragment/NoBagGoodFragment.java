package com.ximai.savingsmore.save.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.cache.MyImageLoader;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.activity.GoodDetailsActivity;
import com.ximai.savingsmore.save.activity.LoginActivity;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/11/28.
 *服务类
 */
public class NoBagGoodFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Goods> list = new ArrayList<Goods>();
    private OnItemClickEventListener listener = null;
    private GoodsList goodsList = new GoodsList();
    private MyAdapter myAdapter;
    private LinearLayoutManager mLayoutManager;
    private View view;
    private LinearLayout ll_defaultdata;
    private KyLoadingBuilder builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.no_bag_goods, null);

        initView();

        initData();

        return view;
    }

    /**
     * initview
     */
    private void initView() {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        ll_defaultdata = (LinearLayout) view.findViewById(R.id.ll_defaultdata);
        initRecycleView(recyclerView);
    }

    /**
     * init-data
     */
    private void initData() {
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        /**
         *获取所有商品
         */
        getAllGoods();
    }

    /**
     * 配置recycleview
     * @param recyclerView
     */
    private void initRecycleView(RecyclerView recyclerView) {
        FullyLinearLayoutManager myLayoutManager = new FullyLinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        myLayoutManager.setOrientation(FullyLinearLayoutManager.VERTICAL);
        configRecycleView(recyclerView, myLayoutManager);
    }

    /**
     * 配置recycleview
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 适配器
     */
    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.salegood_item, null);
            return new MyViewHodel(layout, new OnItemClickEventListener() {
                @Override
                public void onItemClick(int position) {
                    if (LoginUser.getInstance().isLogin()) {
                        Intent intent = new Intent(getActivity(), GoodDetailsActivity.class);
                        intent.putExtra("id", list.get(position).Id);
                        startActivity(intent);
                    }
                }
                @Override
                public void onItemLongClick(int position) {
                }
            });
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHodel viewHodel = (MyViewHodel) holder;
//            MyImageLoader.displayDefaultImage(URLText.img_url + list.get(position).Image, viewHodel.imageView);
            Glide.with(getContext()).load(URLText.img_url + list.get(position).Image).into(viewHodel.imageView);
            if (null != list.get(position).Name) {
                viewHodel.name.setText(list.get(position).Name);
            }
            if (null != list.get(position).SaleCount) {
                viewHodel.sales_number.setText("销 " + list.get(position).SaleCount);
            }
//            if (null != list.get(position).Preferential) {
//                viewHodel.zhekou.setText(list.get(position).PromotionTypeName);
//            }



//            if (null != list.get(position).StoreName) {
//                viewHodel.shop_name.setText(list.get(position).StoreName);
//            }
            if (null != list.get(position).ChainStores){//---------------------------------------------------------进行分店的实现
                if (list.get(position).ChainStores.size() > 0){
                    String name = list.get(position).ChainStores.get(0).Name;
                    viewHodel.shop_name.setText(name);
                }else{
                    if (null != list.get(position).StoreName) {//商家店铺名称
                        viewHodel.shop_name.setText(list.get(position).StoreName);
                    }
                }
            }else{
                if (null != list.get(position).StoreName) {//商家店铺名称
                    viewHodel.shop_name.setText(list.get(position).StoreName);
                }
            }

            if (null != list.get(position).FavouriteCount) {
                viewHodel.shou_chang.setText(list.get(position).FavouriteCount);
            }
            if (null == list.get(position).CommentCount ){
                viewHodel.liulan.setText("0");//评论
            }else{
                viewHodel.liulan.setText(list.get(position).CommentCount);//评论
            }
            viewHodel.share.setText(list.get(position).SharedCount);
            if (null == list.get(position).HitCount ){
                viewHodel.tv_lookthroughs.setText("0");//浏览
            }else{
                viewHodel.tv_lookthroughs.setText(list.get(position).HitCount);//浏览
            }
            if (null != list.get(position).Address && null != list.get(position).Province && null != list.get(position).City) {//促销地址
                viewHodel.location.setText(list.get(position).Country+" · "+list.get(position).Province+list.get(position).City+list.get(position).Address);
            }
            if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)) {
                viewHodel.start_time.setText(list.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)) {
                viewHodel.end_time.setText(list.get(position).EndTimeName.split(" ")[0]);
            }
            viewHodel.price.setText(list.get(position).Currency+" "+ list.get(position).Price);

            if (list.get(position).Preferential.length() > 5){//优惠方式
                viewHodel.dazhe_style.setText(list.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.dazhe_style.setText(list.get(position).Preferential);
            }
            viewHodel.high_price.setText(list.get(position).Currency+" "+ list.get(position).OriginalPrice);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    /**
     * 提供接口
     */
    public interface OnItemClickEventListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
    public void setOnItemClickEventListener(OnItemClickEventListener listener) {
        this.listener = listener;
    }
    class MyViewHodel extends RecyclerView.ViewHolder {
        public TextView sales_number;
        public ImageView imageView;
        public TextView shop_name;
        public TextView name;
        public TextView shou_chang;
        public TextView liulan;
        public TextView share;
        public TextView location;
        public TextView start_time;
        public TextView end_time;
        public TextView price;
        public TextView dazhe_style;
        public TextView high_price;
        public TextView tv_lookthroughs;

        public MyViewHodel(View itemView, final OnItemClickEventListener lis) {
            super(itemView);
            sales_number = (TextView) itemView.findViewById(R.id.tv_volume);
            name = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            shou_chang = (TextView) itemView.findViewById(R.id.tv_assesss);
            liulan = (TextView) itemView.findViewById(R.id.tv_comments);
            share = (TextView) itemView.findViewById(R.id.tv_sharks);
            shop_name = (TextView) itemView.findViewById(R.id.tv_business);
            location = (TextView) itemView.findViewById(R.id.tv_address);
            start_time = (TextView) itemView.findViewById(R.id.statr_time);
            end_time = (TextView) itemView.findViewById(R.id.end_time);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            dazhe_style = (TextView) itemView.findViewById(R.id.tv_favourable);
            high_price = (TextView) itemView.findViewById(R.id.tv_agoprice);
            tv_lookthroughs = (TextView) itemView.findViewById(R.id.tv_lookthroughs);
            high_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            if (null != lis) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lis.onItemClick(getAdapterPosition());
                    }
                });
            }
        }
    }

    /**
     *获取所有商品
     */
    private void getAllGoods() {
//        isRefreshing = true;
//        swipeRefreshLayout.setRefreshing(isRefreshing);
        showLoading(getActivity(),"正在加载");
        WebRequestHelper.json_post(getActivity(), URLText.GET_GOODS, RequestParamsPool.getHotGoods(false), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                    if (goodsList.IsSuccess) {
                        if (goodsList.MainData != null && goodsList.MainData.size() != 0 ) {
                            list.addAll(goodsList.MainData);
                            myAdapter.notifyDataSetChanged();
                        }else{
                            ll_defaultdata.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();

                    if (null != builder){
                        builder.dismiss();
                    }
                }finally {
                    if (null != builder){
                        builder.dismiss();
                    }
                }
//                isRefreshing = false;
//                swipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    /**
     * 打开loading
     */
    public void showLoading(Context context, String text){
        if (null == builder){
            builder = new KyLoadingBuilder(context);
        }
        builder.setIcon(R.mipmap.loading);
        builder.setText(text);
        builder.setOutsideTouchable(true);
        //builder.setBackTouchable(true);
        builder.show();
    }
}
