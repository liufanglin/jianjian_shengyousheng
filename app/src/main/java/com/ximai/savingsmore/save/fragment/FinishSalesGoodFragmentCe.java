package com.ximai.savingsmore.save.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/9.
 * 促销结束 - 支持侧滑
 */
public class FinishSalesGoodFragmentCe extends Fragment implements SwipeItemClickListener {
    private SwipeMenuRecyclerView recyclerView;
    private List<Goods> list = new ArrayList<>();
    private GoodsList goodsList = new GoodsList();
    private MyAdapter myAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean isComment, isHit, isCollect;
    private List<Comment> commentList = new ArrayList<>();
    private List<Goods> all_list = new ArrayList<>();
    private View view;
    private LinearLayout ll_defaultdata;
    private DefaultItemDecoration mItemDecoration;
    private String isSucess;
    private int adapterPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.no_bag_goods_ce, null);

        initView();

        initData();

        return view;
    }

    /**
     * init-view
     */
    private void initView() {
        recyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.recycle_view);
        ll_defaultdata = (LinearLayout) view.findViewById(R.id.ll_defaultdata);
    }

    /**
     * init-data
     */
    private void initData() {
        mLayoutManager = new LinearLayoutManager(getActivity());//设置布局参数
        mItemDecoration = new DefaultItemDecoration(ContextCompat.getColor(getActivity(), R.color.white));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(mItemDecoration);
        recyclerView.setSwipeItemClickListener(this);//设置Iten的点击事件
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);//创建侧边栏
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);//侧边栏点击事件

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
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
    }

    @Override
    public void onItemClick(View itemView, int position) {
        if (LoginUser.getInstance().isLogin()) {
            if (isComment) {
                if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("1")){
                    Onedialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("2")){
                    Twodialog();
                } else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("3")){
                    Intent intent = new Intent(getActivity(), GoodsCommentActiviyt.class);
                    intent.putExtra("id", list.get(position).Id);
                    intent.putExtra("score", list.get(position).Score);
                    intent.putExtra("isComment", "true");
                    startActivity(intent);
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("4")){
                    /**
                     * 打电话
                     */
                    Fourdialog();
                }
            } else {
                if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("1")){
                    Onedialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("2")){
                    Twodialog();
                } else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("3")){
                    Intent intent = new Intent(getActivity(), AddGoodsAcitivyt.class);
                    intent.putExtra("isEnd", true);
                    intent.putExtra("id", list.get(position).Id);
                    startActivity(intent);
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("4")){
                    /**
                     * 打电话
                     */
                    Fourdialog();
                }
            }
        }
    }

    /**
     * 适配器
     */
    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.salegood_item, null);
            return new MyViewHodel(layout);
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
            if (null != list.get(position).Address && null != list.get(position).Province && null != list.get(position).City) {//促销地点
                viewHodel.location.setText(list.get(position).Country+" · "+list.get(position).Province+list.get(position).City+list.get(position).Address);
            }
            if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)) {
                // viewHodel.start_time.setTextColor(getResources().getColor(R.color.stepcolor));
                viewHodel.start_time.setText(list.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)) {
                // viewHodel.end_time.setTextColor(getResources().getColor(R.color.stepcolor));
                viewHodel.end_time.setText(list.get(position).EndTimeName.split(" ")[0]);
            }
            viewHodel.price.setText(list.get(position).Currency+" "+list.get(position).Price);
//            viewHodel.dazhe_style.setText(list.get(position).Preferential);
            if (list.get(position).Preferential.length() > 5){
                viewHodel.dazhe_style.setText(list.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.dazhe_style.setText(list.get(position).Preferential);
            }
            viewHodel.high_price.setText(list.get(position).Currency+" "+list.get(position).OriginalPrice);
            viewHodel.tv_care.setText("关注"+list.get(position).CareCount);
            viewHodel.tv_store_count.setText("到店人次"+list.get(position).StoreCount);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }
    static class MyViewHodel extends RecyclerView.ViewHolder {
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
        public MyViewHodel(View itemView) {
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
        }
    }

    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            }
        }
    };
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            adapterPosition = menuBridge.getAdapterPosition();// RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                isYesDeletedialog();
            }
        }
    };

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
        WebRequestHelper.json_post(getActivity(), URLText.REMOVE_MYGOODS, RequestParamsPool.removeGoods(Id), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject object = null;
                try {
                    String result = new String(responseBody);
                    object = new JSONObject(result);
                    isSucess = object.optString("IsSuccess");
                    if (isSucess.equals("true")) {
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    /**
     * 未申请
     */
    public void Onedialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "请完成注册商家信息，再发布促销。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 未申请
     */
    public void Twodialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "您提交的入驻申请正在审核，请耐心等待。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 未通过审核 - 进行一个弹框操作
     */
    public void Fourdialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call("02158366991");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "您的入驻申请未通过审核，请联系我们 021-58366991。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 是否确认删除订单
     */
    public void isYesDeletedialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                removeGood(list.get(adapterPosition).Id);
                list.remove(adapterPosition);
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "是否确认删除？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}