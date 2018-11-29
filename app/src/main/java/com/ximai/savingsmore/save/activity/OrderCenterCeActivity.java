package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.adapter.OrderCenterAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.Order;
import com.ximai.savingsmore.save.modle.OrderList;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/28.
 */
public class OrderCenterCeActivity extends BaseActivity implements SwipeItemClickListener {
    private List<Order> order = new ArrayList<>();
    private SwipeMenuRecyclerView recycle_view;
    private OrderList orderList;
    private String title;
    private OrderCenterAdapter orderCenterAdapter;
    private LinearLayout ll_defaultdata;
    private DefaultItemDecoration mItemDecoration;
    private LinearLayoutManager mLayoutManager;
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_centerce_activity);

        initView();

        initData();
    }

    /**
     * init - view
     */
    private void initView() {
        recycle_view = (SwipeMenuRecyclerView) findViewById(R.id.recycle_view);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);
    }

    /**
     * init - data
     */
    private void initData() {
        if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {//2是个人－３是商家
            setCenterTitle("订单收货");
            title = "订单收货";
        } else {
            setCenterTitle("订单中心");
            title = "订单中心";
        }
        setLeftBackMenuVisibility(OrderCenterCeActivity.this, "");

        mLayoutManager = new LinearLayoutManager(OrderCenterCeActivity.this);//设置布局参数
        mItemDecoration = new DefaultItemDecoration(ContextCompat.getColor(OrderCenterCeActivity.this, R.color.white));
        recycle_view.setLayoutManager(mLayoutManager);
        recycle_view.addItemDecoration(mItemDecoration);
        recycle_view.setSwipeItemClickListener(this);//设置Iten的点击事件
        recycle_view.setSwipeMenuCreator(swipeMenuCreator);//创建侧边栏
        recycle_view.setSwipeMenuItemClickListener(mMenuItemClickListener);//侧边栏点击事件

        orderCenterAdapter = new OrderCenterAdapter(this);

        /**
         * 获取数据
         */
//        getMyOrder();

        recycle_view.setAdapter(orderCenterAdapter);

        /**
         * 查看详情
         */
        orderCenterAdapter.setOnItenClickListener(new OrderCenterAdapter.OnItenClickListener() {
            @Override
            public void onItenClick(int position, String id) {
                if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {//2是个人3是商家
                    //个人订单详情
                    Intent intent = new Intent(OrderCenterCeActivity.this, PersonOrderDetailActivity.class);
                    intent.putExtra("Id", id);//订单Id
                    startActivity(intent);
                } else {
                    //商家订单详情
                    Intent intent = new Intent(OrderCenterCeActivity.this, BusinessOrderDeatilActivity.class);
                    intent.putExtra("Id", id);//订单Id
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * lifestyle
     */
    @Override
    protected void onResume() {
        super.onResume();
        getMyOrder();
    }

    /**
     * 获取我的订单
     */
    private void getMyOrder() {
        showLoading(this,"正在加载");
        String url = null;
        if (title.equals("订单中心")) {//商家的获取商品详情
            url = URLText.BUSINESS_GET_ORDER;
        } else {//个人的获取订单详情
            url = URLText.GET_ORDER;
        }
        WebRequestHelper.json_post(OrderCenterCeActivity.this, url, RequestParamsPool.getOrder(), new MyAsyncHttpResponseHandler(OrderCenterCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    order.clear();
                    JSONObject object = new JSONObject(new String(responseBody));
                    orderList = GsonUtils.fromJson(object.toString(), OrderList.class);
                    order = orderList.MainData;
                    if (null == order || 0 == order.size()) {
                        ll_defaultdata.setVisibility(View.VISIBLE);//暂无数据
                        recycle_view.setVisibility(View.GONE);
                    }else{
                        orderCenterAdapter.setDdata(order);
                        orderCenterAdapter.notifyDataSetChanged();
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }

    /**
     * 不可以删除订单状态
     */
    public void noDeleteOrder(){
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
        Dialog dialog = new XiMaiPopDialog(OrderCenterCeActivity.this, "温馨提示", "该状态订单不可以删除。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 可以删除订单状态
     */
    public void yesDeleteOrder(final String id){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                deleteOrder(id);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(OrderCenterCeActivity.this, "温馨提示", "是否确认删除订单？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 删除我的订单
     */
    private void deleteOrder(String id) {
        String url = null;
        if (title.equals("订单中心")) {//商家的获取商品详情
            url = URLText.BUSINESS_DETELEORDER;
        } else {//个人的获取订单详情
            url = URLText.PERSONAL_DETELEORDER;
        }
        WebRequestHelper.json_post(OrderCenterCeActivity.this, url, RequestParamsPool.deleteOrder(id), new MyAsyncHttpResponseHandler(OrderCenterCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    Boolean isScuess = object.optBoolean("IsSuccess");
                    if (true == isScuess){//可以删除
                        Toast.makeText(OrderCenterCeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        /**
                         * 获取订单收货的列表
                         */
                        getMyOrder();
                    }else{//不可以删除
                        noDeleteOrder();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(OrderCenterCeActivity.this)
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
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {//2是个人3是商家
                    //个人删除订单
                    deleteOrder(order.get(adapterPosition).Id);
                } else {
                    //商家删除订单详情
                    deleteOrder(order.get(adapterPosition).Id);
                }
            }
        }
    };

    /**
     * 查看详情在iten上面的按钮
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {

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