package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/28.
 */
public class OrderCenterActivity extends BaseActivity {
    private List<Order> order = new ArrayList<>();
    private RecyclerView recycle_view;
    private OrderList orderList;
    private String title;
    private OrderCenterAdapter orderCenterAdapter;
    private LinearLayout ll_defaultdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_center_activity);

        initView();

        initData();
    }

    /**
     * init - view
     */
    private void initView() {
        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);
        initRecycleView(recycle_view);
    }

    /**
     * init - data
     */
    private void initData() {
        //title = getIntent().getStringExtra("title");
        if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {//2是个人－３是商家
            setCenterTitle("订单收货");
            title = "订单收货";
        } else {
            setCenterTitle("订单中心");
            title = "订单中心";
        }
        setLeftBackMenuVisibility(OrderCenterActivity.this, "");

        orderCenterAdapter = new OrderCenterAdapter(this);
        /**
         * 查看详情
         */
        orderCenterAdapter.setOnItenClickListener(new OrderCenterAdapter.OnItenClickListener() {
            @Override
            public void onItenClick(int position, String id) {
                if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {//2是个人3是商家
                    //个人订单详情
                    Intent intent = new Intent(OrderCenterActivity.this, PersonOrderDetailActivity.class);
                    intent.putExtra("Id", id);//订单Id
                    startActivity(intent);
                } else {
                    //商家订单详情
                    //Intent intent = new Intent(OrderCenterActivity.this, OrderDetialActivity.class);//原先的订单详情
                    Intent intent = new Intent(OrderCenterActivity.this, BusinessOrderDeatilActivity.class);
                    intent.putExtra("Id", id);//订单Id
                    startActivity(intent);
                }
            }
        });

        /**
         * 删除订单
         */
        orderCenterAdapter.setOnItenDeleteClickListener(new OrderCenterAdapter.OnItenDeleteClickListener() {
            @Override
            public void onItenDeleteClick(int position, String id) {
                if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {//2是个人3是商家
                    //个人删除订单
                    deleteOrder(id);
                } else {
                    //商家删除订单详情
                    deleteOrder(id);
                }
            }
        });
        /**
         * 获取数据
         */
        getMyOrder();

        recycle_view.setAdapter(orderCenterAdapter);

        /**
         * 如果是个人订单状态那么退款时
         */
    }

    /**
     * 配置recycleview
     * @param recyclerView
     */
    private void initRecycleView(RecyclerView recyclerView) {
        FullyLinearLayoutManager myLayoutManager = new FullyLinearLayoutManager(this) {
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
     * lifestylef
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
        String url = null;
        if (title.equals("订单中心")) {//商家的获取商品详情
            url = URLText.BUSINESS_GET_ORDER;
        } else {//个人的获取订单详情
            url = URLText.GET_ORDER;
        }
        WebRequestHelper.json_post(OrderCenterActivity.this, url, RequestParamsPool.getOrder(), new MyAsyncHttpResponseHandler(OrderCenterActivity.this) {
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
                } catch (JSONException e) {
                    e.printStackTrace();
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
        Dialog dialog = new XiMaiPopDialog(OrderCenterActivity.this, "温馨提示", "该状态订单不可以删除。", "确认", R.style.CustomDialog_1, callBack, 2);
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
        Dialog dialog = new XiMaiPopDialog(OrderCenterActivity.this, "温馨提示", "是否确认删除订单？", "确认", R.style.CustomDialog_1, callBack, 2);
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
        WebRequestHelper.json_post(OrderCenterActivity.this, url, RequestParamsPool.deleteOrder(id), new MyAsyncHttpResponseHandler(OrderCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    Boolean isScuess = object.optBoolean("IsSuccess");
                    if (true == isScuess){//可以删除
                        Toast.makeText(OrderCenterActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
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
}