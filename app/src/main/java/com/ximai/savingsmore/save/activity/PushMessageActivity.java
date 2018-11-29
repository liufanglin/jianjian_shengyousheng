package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
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
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.PushMessageBean;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
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
 * Created by caojian on 16/12/23.
 */
public class PushMessageActivity extends BaseActivity implements SwipeItemClickListener {
    private SwipeMenuRecyclerView recyclerView;
    private MyAdapter myAdapter;
    private OnItemClickEventListener listener = null;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout ll_defaultdata;
    private List<PushMessageBean.MainData> mainDataList = new ArrayList<>();
    private DefaultItemDecoration mItemDecoration;
    private int adapterPosition;
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_message_activity);

        initView();

        initData();
    }

    /**
     * initView
     */
    private void initView() {
        setCenterTitle("促销推送");
        setLeftBackMenuVisibility(PushMessageActivity.this, "");

        recyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycle_view);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);
    }

    /**
     * initdata
     */
    private void initData() {
        mLayoutManager = new LinearLayoutManager(PushMessageActivity.this);//设置布局参数
        mItemDecoration = new DefaultItemDecoration(ContextCompat.getColor(PushMessageActivity.this, R.color.white));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(mItemDecoration);
        recyclerView.setSwipeItemClickListener(this);//设置Iten的点击事件
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);//创建侧边栏
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);//侧边栏点击事件

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        /**
         * 获取推送数据
         */
        getPushGood();
    }


    /**
     * 获取数据    -- 目前暂无数据   URLText.GET_GOODS, RequestParamsPool.getHotGoods(false)这个接口测试数据
     */
    private void getPushGood() {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(PushMessageActivity.this, URLText.PUSH_GOODS, RequestParamsPool.getPushGood(), new MyAsyncHttpResponseHandler(PushMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                try {
                    PushMessageBean pushMessageBean = GsonUtils.fromJson(new String(responseBody), PushMessageBean.class);
                    mainDataList = pushMessageBean.MainData;
                    if (mainDataList.size() == 0){
                        ll_defaultdata.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }else{
                        ll_defaultdata.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        myAdapter.notifyDataSetChanged();
                    }
                    if (null != builder){
                        builder.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }

    /**
     * 删除推送消息
     * @param Id
     */
    private void removePushGood(String Id) {
        WebRequestHelper.json_post(PushMessageActivity.this, URLText.REMOVE_PUSH_GOODS, RequestParamsPool.removePushGoods(Id), new MyAsyncHttpResponseHandler(PushMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    String isSuccess = object.optString("IsSuccess");
                    if (isSuccess.equals("true")) {
                        Toast.makeText(PushMessageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 适配器
     */
    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(PushMessageActivity.this).inflate(R.layout.salegood_item, null);
            return new MyViewHodel(layout);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHodel viewHodel = (MyViewHodel) holder;
//            MyImageLoader.displayDefaultImage(URLText.img_url + list.get(position).Image, viewHodel.imageView);
            Glide.with(PushMessageActivity.this).load(URLText.img_url + mainDataList.get(position).Image).into(viewHodel.imageView);
            if (null != mainDataList.get(position).Name) {
                viewHodel.name.setText(mainDataList.get(position).Name);
            }
            if (null != mainDataList.get(position).SaleCount) {
                viewHodel.sales_number.setText("销 " + mainDataList.get(position).SaleCount);
            }
//            if (null != mainDataList.get(position).StoreName) {
//                viewHodel.shop_name.setText(mainDataList.get(position).StoreName);
//            }

            if (null != mainDataList.get(position).ChainStores){//---------------------------------------------------------进行分店的实现
                if (mainDataList.get(position).ChainStores.size() > 0){
                    String name = mainDataList.get(position).ChainStores.get(0).Name;
                    viewHodel.shop_name.setText(name);
                }else{
                    if (null != mainDataList.get(position).StoreName) {//商家店铺名称
                        viewHodel.shop_name.setText(mainDataList.get(position).StoreName);
                    }
                }
            }else{
                if (null != mainDataList.get(position).StoreName) {//商家店铺名称
                    viewHodel.shop_name.setText(mainDataList.get(position).StoreName);
                }
            }

            if (null != mainDataList.get(position).FavouriteCount) {
                viewHodel.shou_chang.setText(mainDataList.get(position).FavouriteCount);
            }

            if (null == mainDataList.get(position).CommentCount ){
                viewHodel.liulan.setText("0");//评论
            }else{
                viewHodel.liulan.setText(mainDataList.get(position).CommentCount);//评论
            }

            viewHodel.share.setText(mainDataList.get(position).SharedCount);

            if (null == mainDataList.get(position).HitCount ){
                viewHodel.tv_lookthroughs.setText("0");//浏览
            }else{
                viewHodel.tv_lookthroughs.setText(mainDataList.get(position).HitCount);//浏览
            }

            if (null != mainDataList.get(position).Address && null != mainDataList.get(position).Province && null != mainDataList.get(position).City) {
                viewHodel.location.setText(mainDataList.get(position).Country+" · "+mainDataList.get(position).Province + mainDataList.get(position).City + mainDataList.get(position).Address);
            }
            if (null != mainDataList.get(position).StartTimeName && !TextUtils.isEmpty(mainDataList.get(position).StartTimeName)) {
                viewHodel.start_time.setText(mainDataList.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != mainDataList.get(position).EndTimeName && !TextUtils.isEmpty(mainDataList.get(position).EndTimeName)) {
                viewHodel.end_time.setText(mainDataList.get(position).EndTimeName.split(" ")[0]);
            }
            viewHodel.price.setText(mainDataList.get(position).Currency+" "+ mainDataList.get(position).Price);
//            viewHodel.dazhe_style.setText(mainDataList.get(position).Preferential);
            if (mainDataList.get(position).Preferential.length() > 5){
                viewHodel.dazhe_style.setText(mainDataList.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.dazhe_style.setText(mainDataList.get(position).Preferential);
            }
            viewHodel.high_price.setText("原价¥" + mainDataList.get(position).OriginalPrice);
        }
        @Override
        public int getItemCount() {
            return mainDataList.size();
        }
    }
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
        }
    }

    /**
     * iten点击事件
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (LoginUser.getInstance().isLogin()) {
            Intent intent = new Intent(PushMessageActivity.this, GoodDetailsActivity.class);
            intent.putExtra("id", mainDataList.get(position).ProductId);
            startActivity(intent);
        } else {
            Intent intent = new Intent(PushMessageActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(PushMessageActivity.this, "温馨提示,您还没有登录", Toast.LENGTH_LONG).show();
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(PushMessageActivity.this)
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

    /**
     * 是否确认删除订单
     */
    public void isYesDeletedialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                removePushGood(mainDataList.get(adapterPosition).Id);
                mainDataList.remove(adapterPosition);
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(PushMessageActivity.this, "温馨提示", "是否确认删除？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
        builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        builder.show();
    }
}