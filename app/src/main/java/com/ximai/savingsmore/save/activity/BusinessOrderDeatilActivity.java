package com.ximai.savingsmore.save.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.save.adapter.BusinessOrderDetailAdapter;
import com.ximai.savingsmore.save.adapter.PersonOrderDetailAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.OrderStateResult;
import com.ximai.savingsmore.save.modle.PersonOrderDetialBean;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/14 0014.
 * 商家订单详情
 */

public class BusinessOrderDeatilActivity extends BaseActivity implements View.OnClickListener {

    private List<Goods> orderList = new ArrayList<Goods>();
    private PersonOrderDetialBean orderDetial;
    private OrderStateResult orderStateResult;
    private String OrderId;
    private ImageView iv_orderstatue0;
    private TextView tv_orderstatue;
    private ImageView iv_orderstatue1;
    private ImageView iv_orderstatue2;
    private ImageView iv_orderstatue3;
    private TextView tv_name;
    private TextView tv_phone;
    private TextView tv_adddress;
    private TextView tv_ordernumber;
    private RecyclerView recycle_view;
    private TextView tv_goodyssmoney;
    private TextView tv_goodsbxmoney;
    private TextView tv_fapiao;
    private TextView tv_goodsstatue;
    private TextView tv_paymoney;
    private RelativeLayout rl_businessly;
    private TextView tv_liuyandata;
    private Button btn_sendgoods;
    private ImageView iv_liuyan;
    private BusinessOrderDetailAdapter businessOrderDetailAdapter;
    private LinearLayout ll_btn;
    private TextView tv_cancelstatue;
    private Button btn_cancel;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private String PayAppName;
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_orderdetial);
        setCenterTitle("订单详情");

        initView();

        initData();

        initEvent();
    }

    /**
     * init - view
     */
    private void initView() {
        iv_orderstatue0 = (ImageView) findViewById(R.id.iv_orderstatue0);
        tv_orderstatue = (TextView) findViewById(R.id.tv_orderstatue);
        iv_orderstatue1 = (ImageView) findViewById(R.id.iv_orderstatue1);
        iv_orderstatue2 = (ImageView) findViewById(R.id.iv_orderstatue2);
        iv_orderstatue3 = (ImageView) findViewById(R.id.iv_orderstatue3);
        iv_liuyan = (ImageView) findViewById(R.id.iv_liuyan);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_adddress = (TextView) findViewById(R.id.tv_adddress);
        tv_ordernumber = (TextView) findViewById(R.id.tv_ordernumber);
        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);
        tv_goodyssmoney = (TextView) findViewById(R.id.tv_goodyssmoney);
        tv_goodsbxmoney = (TextView) findViewById(R.id.tv_goodsbxmoney);
        tv_fapiao = (TextView) findViewById(R.id.tv_fapiao);
        tv_goodsstatue = (TextView) findViewById(R.id.tv_goodsstatue);
        tv_paymoney = (TextView) findViewById(R.id.tv_paymoney);
        rl_businessly = (RelativeLayout) findViewById(R.id.rl_businessly);
        tv_liuyandata = (TextView) findViewById(R.id.tv_liuyandata);
        btn_sendgoods = (Button) findViewById(R.id.btn_sendgoods);
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        tv_cancelstatue = (TextView) findViewById(R.id.tv_cancelstatue);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);//商家取消按钮
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        initRecycleView(recycle_view);
    }

    /**
     * init - data
     */
    private void initData() {
        OrderId = getIntent().getStringExtra("Id");
        setLeftBackMenuVisibility(BusinessOrderDeatilActivity.this, "");
        businessOrderDetailAdapter = new BusinessOrderDetailAdapter(this);
        /**
         * 获取订单详情数据
         */
        getOrderDetial(OrderId);
        recycle_view.setAdapter(businessOrderDetailAdapter);
        recycle_view.setNestedScrollingEnabled(false);
    }

    /**
     * init - event
     */
    private void initEvent() {
        rl_businessly.setOnClickListener(this);
        btn_sendgoods.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
    }

    /**
     * 配置recycleview
     *
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
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 获取订单详情数据
     */
    private void getOrderDetial(String Id) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(BusinessOrderDeatilActivity.this,URLText.BUSINESS_ORDER_DETIAL, RequestParamsPool.orderDetial(Id), new MyAsyncHttpResponseHandler(BusinessOrderDeatilActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    orderDetial = GsonUtils.fromJson(object.optString("MainData"), PersonOrderDetialBean.class);
                    /**
                     * 对于线下支付的订单进行退款
                     */
                    PayAppName = orderDetial.PayAppName;
                    if (null != orderDetial) {
                        tv_name.setText(orderDetial.Recipients);
                        tv_phone.setText(orderDetial.PhoneNumber);
                        tv_adddress.setText(orderDetial.Address);
                        tv_ordernumber.setText(orderDetial.Number);//订单号
                        tv_goodyssmoney.setText(orderDetial.Seller.UserExtInfo.DeliveryService.Name);//运费
                        tv_goodsbxmoney.setText(orderDetial.Seller.UserExtInfo.Premium.Name);//保险
                        tv_fapiao.setText(orderDetial.InvoiceTitle);//发票
//                        tv_goodsstatue.setText(orderDetial.OrderStateName);//订单状态
                        orderList = orderDetial.OrderProducts;
                        if (orderList.size() > 0){
                            tv_paymoney.setText(orderList.get(0).Currency + UIUtils.formatPrice(Double.parseDouble(orderDetial.Price)));
                        }
//                        businessOrderDetailAdapter.setStoreCoun(orderDetial.Seller.UserExtInfo.StoreCount);
                        businessOrderDetailAdapter.setData(orderList);
                        businessOrderDetailAdapter.setOrderDeatil(orderDetial);
                        businessOrderDetailAdapter.notifyDataSetChanged();

                        //根据订单状态来判断不同时间下的订单      处理订单 - 已发货 - 交易完成      当商家取消的时候状态为不发货/退货   中间显示订单取消
                        if (orderDetial.OrderState == 1){//待支付
//                            btn_cancel.setVisibility(View.VISIBLE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_chulidingdan);
                            tv_orderstatue.setText("处理订单");
                            tv2.setText("订货");
                            tv_cancelstatue.setText("未收货");
                            tv_goodsstatue.setText("未支付");//订单状态
                            tv1.setText("未支付:");
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.order_successgray);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 2){//处理订单
                            btn_cancel.setVisibility(View.VISIBLE);
                            btn_sendgoods.setVisibility(View.VISIBLE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_chulidingdan);
                            tv_orderstatue.setText("处理订单");
                            tv2.setText("订货");
                            tv_cancelstatue.setText("未收货");
                            tv_goodsstatue.setText("已支付");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.order_successgray);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 3){//已发货
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_daishohuo);
                            tv_orderstatue.setText("待收货");
                            tv2.setText("订单");
                            tv_cancelstatue.setText("已发货");
                            tv_goodsstatue.setText("已发货");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 4){//已完成
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_dingdanwancheng);
                            tv_orderstatue.setText("交易完成");
                            tv2.setText("订单");
                            tv_cancelstatue.setText("已发货");
                            tv_goodsstatue.setText("已完成");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.ordercenter_icon4);
                        }else if (orderDetial.OrderState == 5){//5是已关闭 - 7是已退款
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_bufahuo);
                            tv_orderstatue.setText("处理订单");
                            tv2.setText("订货付款");
                            tv_cancelstatue.setText("订单取消");
                            tv_goodsstatue.setText("订单取消");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 6){//退款审核中
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_chulidingdan);
                            tv_orderstatue.setText("处理订单");
                            tv2.setText("订单取消");
                            tv_cancelstatue.setText("退款审核");
                            tv3.setText("退款成功");
                            tv_goodsstatue.setText("退款审核");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 7){//已退款
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_dingdanwancheng);
                            tv_orderstatue.setText("退款成功");
                            tv2.setText("订单取消");
                            tv_cancelstatue.setText("退款审核");
                            tv3.setText("退款成功");
                            if ("线下支付".equals(PayAppName)){
                                tv_goodsstatue.setText("商家安排退款");//订单状态
                            }else{
                                tv_goodsstatue.setText("退款成功");//订单状态
                            }
                            tv1.setText("已退款:");
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.ordercenter_icon4);
                        }else if (orderDetial.OrderState == 8){//商家取消订单
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_daishohuo);
                            tv_orderstatue.setText("处理订单");
                            tv2.setText("订单");
                            tv_cancelstatue.setText("订单取消");
                            tv_goodsstatue.setText("订单取消");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if(orderDetial.OrderState == 9){//个人已支付的取消
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_daishohuo);
                            tv_orderstatue.setText("处理订单");
                            tv2.setText("订单");
                            tv_cancelstatue.setText("订单取消");
                            tv_goodsstatue.setText("订单取消");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }
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
     * 事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_businessly://留言
                if (tv_liuyandata.getVisibility() == View.GONE) {
                    tv_liuyandata.setVisibility(View.VISIBLE);
                    iv_liuyan.setBackgroundResource(R.mipmap.search_dowm3);
                    if (orderDetial.Remark == null || "".equals(orderDetial.Remark)) {
                        tv_liuyandata.setText("暂无留言");
                    } else {
                        tv_liuyandata.setText(orderDetial.Remark);
                    }
                } else {
                    iv_liuyan.setBackgroundResource(R.mipmap.search_up3);
                    tv_liuyandata.setVisibility(View.GONE);
                }
                break;
            case R.id.btn_sendgoods://接单发货
                deliverGoods(OrderId);
                break;
            case R.id.btn_cancel://商家取消订单
                cancelOrder(OrderId);
                break;
            case R.id.tv_phone:
                call(tv_phone.getText().toString());
                break;
        }
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 接单发货
     * @param Id
     */
    private void deliverGoods(String Id) {
        WebRequestHelper.json_post(BusinessOrderDeatilActivity.this, URLText.FAHUO, RequestParamsPool.fa_huo(Id), new MyAsyncHttpResponseHandler(BusinessOrderDeatilActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
                Toast.makeText(BusinessOrderDeatilActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                LogUtils.instance.d("发货=" + result);
                finish();
            }
        });
    }

    /**
     * 商家的取消订单
     * @param Id
     */
    private void cancelOrder(String Id) {
        WebRequestHelper.json_post(BusinessOrderDeatilActivity.this, URLText.CANAEL, RequestParamsPool.cancelOrder(Id), new MyAsyncHttpResponseHandler(BusinessOrderDeatilActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
                Toast.makeText(BusinessOrderDeatilActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                LogUtils.instance.d("取消订单=" + result);
                finish();
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
        builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        builder.show();
    }
}
