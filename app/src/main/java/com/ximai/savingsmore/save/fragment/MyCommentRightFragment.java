package com.ximai.savingsmore.save.fragment;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.activity.AddGoodsAcitivyt;
import com.ximai.savingsmore.save.activity.GoodsCommentActiviyt;
import com.ximai.savingsmore.save.modle.Comment;
import com.ximai.savingsmore.save.modle.CommentList;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by caojian on 16/12/9.
 * 评语中心促销结束
 */
public class MyCommentRightFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Goods> list = new ArrayList<Goods>();
    private OnItemClickEventListener listener = null;
    private GoodsList goodsList = new GoodsList();
    private MyAdapter myAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean isComment, isHit, isCollect;
    private AlertDialog classity_dialog;
    private List<Comment> commentList = new ArrayList<Comment>();
    private List<Goods> all_list = new ArrayList<Goods>();
    private View view;
    private LinearLayout ll_defaultdata;
    private int i;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.no_bag_goods, null);

        initView();

        initData();

        return view;
    }

    /**
     * init-view
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getArguments().getString("isComment")) {
            if (getArguments().getString("isComment").equals("true")) {
                isComment = true;
            }
        }
        if (null != getArguments().getString("hit")) {
            if (getArguments().getString("hit").equals("true")) {
                isHit = true;
            }
        }
        if (null != getArguments().getString("collect")) {
            if (getArguments().getString("collect").equals("true")) {
                isCollect = true;
            }
        }
        //getAllGoods();
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
                        if (isComment) {
                            Intent intent = new Intent(getActivity(), GoodsCommentActiviyt.class);
                            intent.putExtra("id", list.get(position).Id);
                            intent.putExtra("score", list.get(position).Score);
                            intent.putExtra("isComment", "true");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getActivity(), AddGoodsAcitivyt.class);
                            intent.putExtra("isEnd", true);
                            intent.putExtra("id", list.get(position).Id);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onItemLongClick(final int position) {
                    classity_dialog = new AlertDialog.Builder(getActivity()).create();
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.delect_good_dialog, null);
                    final TextView queding, quxiao;
                    final EditText content;
                    queding = (TextView) view.findViewById(R.id.commit);
                    quxiao = (TextView) view.findViewById(R.id.cancel);
                    queding.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeGood(list.get(position).Id);
                            list.remove(position);
                            myAdapter.notifyDataSetChanged();
                            classity_dialog.dismiss();
                        }
                    });
                    quxiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            classity_dialog.dismiss();
                        }
                    });
                    classity_dialog.setView(view);
                    classity_dialog.show();
                }
            });
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHodel viewHodel = (MyViewHodel) holder;
            Glide.with(getContext()).load(URLText.img_url + list.get(position).Image).into(viewHodel.imageView);
            if (null != list.get(position).Name) {
                viewHodel.name.setText(list.get(position).Name);
            }
            if (null != list.get(position).SaleCount) {
                if ("0".equals(list.get(position).SaleCount)){
                    viewHodel.sales_number.setVisibility(View.GONE);
                }else {
                    viewHodel.sales_number.setText("销 " + list.get(position).SaleCount);
                    viewHodel.sales_number.setVisibility(View.VISIBLE);
                }
            }
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
            if (null != list.get(position).Address && null != list.get(position).Province && null != list.get(position).City) {
                viewHodel.location.setText(list.get(position).Country+" · "+list.get(position).Province + list.get(position).City + list.get(position).Address);
            }

            if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)) {
                // viewHodel.start_time.setTextColor(getResources().getColor(R.color.stepcolor));
                viewHodel.start_time.setText(list.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)) {
                // viewHodel.end_time.setTextColor(getResources().getColor(R.color.stepcolor));
                viewHodel.end_time.setText(list.get(position).EndTimeName.split(" ")[0]);
            }
            viewHodel.price.setText(list.get(position).Currency+" "+ list.get(position).Price);
//            viewHodel.dazhe_style.setText(list.get(position).Preferential);
            if (list.get(position).Preferential.length() > 5){
                viewHodel.dazhe_style.setText(list.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.dazhe_style.setText(list.get(position).Preferential);
            }
            viewHodel.high_price.setText(list.get(position).Currency+" "+ list.get(position).OriginalPrice);
            viewHodel.tv_care.setText("关注"+list.get(position).CareCount);
            viewHodel.tv_store_count.setText("到店人次"+list.get(position).StoreCount);
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
        public TextView tv_store_count;
        public TextView tv_care;

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
            tv_store_count= (TextView) itemView.findViewById(R.id.tv_store_count);
            tv_care= (TextView) itemView.findViewById(R.id.tv_care);
            if (null != lis) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lis.onItemClick(getAdapterPosition());
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        lis.onItemLongClick(getAdapterPosition());
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllGoods();
    }

    /**
     * 得到所有的商品
     */
    private void getAllGoods() {
        WebRequestHelper.json_post(getActivity(), URLText.GET_SALES_GOODS, RequestParamsPool.getMySalesGoods(true, isComment, isHit, isCollect), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String resule = new String(responseBody);
                goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                if (goodsList.IsSuccess) {
                    if (null != goodsList.MainData && goodsList.MainData.size() != 0) {
                        all_list = goodsList.MainData;
                    }

                    if (isComment && all_list.size() > 0) {
                        list.clear();
                        for (int i = 0; i < all_list.size(); i++) {
                            getComment(all_list.get(i).Id, all_list.get(i));
                        }
                    } else {
                        list = all_list;
                        if (null != list && list.size() != 0){
                            myAdapter.notifyDataSetChanged();
                        }else{
                            ll_defaultdata.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                    }

                    /**
                     *对评语进行排序
                     */
                    Collections.sort(all_list, new Comparator<Goods>() {
                        @Override
                        public int compare(Goods goods, Goods t1) {
                            if (null != t1.CommentCount && null != goods.CommentCount){
                                i = Integer.parseInt(t1.CommentCount) - Integer.parseInt(goods.CommentCount);
                                myAdapter.notifyDataSetChanged();
                            }
                            return i;
                        }
                    });
                }
            }
        });
    }

    /**
     * 移除商品
     *
     * @param Id
     */
    private void removeGood(String Id) {
        WebRequestHelper.json_post(getActivity(), URLText.REMOVE_MYGOODS, RequestParamsPool.removeGoods(Id), new MyAsyncHttpResponseHandler(getActivity()
        ) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = new JSONObject();
            }
        });
    }

    private void getComment(String id, final Goods goods) {
        WebRequestHelper.json_post(getActivity(), URLText.GOODS_COMMENT, RequestParamsPool.getGoodsComment(id), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                CommentList commentList1 = GsonUtils.fromJson(result, CommentList.class);
                commentList = commentList1.MainData;
                if (commentList.size() > 0) {
                    list.add(goods);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}