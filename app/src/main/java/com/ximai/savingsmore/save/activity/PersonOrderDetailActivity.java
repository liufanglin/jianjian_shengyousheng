package com.ximai.savingsmore.save.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.save.adapter.PersonOrderDetailAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.AlipaySignResult;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.OrderDetial;
import com.ximai.savingsmore.save.modle.OrderStateResult;
import com.ximai.savingsmore.save.modle.PersonOrderDetialBean;
import com.ximai.savingsmore.save.modle.WeChatSign;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.PayDialog;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/13 0013.
 */

public class PersonOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private List<Goods> orderList = new ArrayList<>();
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
    private Button btn_cancel;
    private Button btn_againorder;
    private Button btn_yesgoods;
    private ImageView iv_liuyan;
    private PersonOrderDetailAdapter personOrderDetailAdapter;
    private LinearLayout ll_btn;
    private TextView tv_cancelstatue;
    private Button btn_tuikuan;
    private Button btn_gopay;//去结算
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private PayDialog payDialog;
    private AlipaySignResult alipaySignResult;
    private static final int SDK_PAY_FLAG = 1;
    private WeChatSign weChatSign;


    /**
     * 支付宝回调
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    // @SuppressWarnings("unchecked")
                    // PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    // String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    // String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (msg.obj.toString().contains("resultStatus={9000}")) {
                        finish();
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(PersonOrderDetailActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PersonOrderDetailActivity.this, PaySuccessActivity.class);
                        intent.putExtra("Id", OrderId);
                        startActivity(intent);
                    } else {
                        finish();
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(PersonOrderDetailActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };
    private String PayAppName;
    private KyLoadingBuilder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_orderdetial);
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
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_againorder = (Button) findViewById(R.id.btn_againorder);
        btn_yesgoods = (Button) findViewById(R.id.btn_yesgoods);
        ll_btn = (LinearLayout) findViewById(R.id.ll_btn);
        tv_cancelstatue = (TextView) findViewById(R.id.tv_cancelstatue);
        btn_tuikuan = (Button) findViewById(R.id.btn_tuikuan);//申请退款
        btn_gopay = (Button) findViewById(R.id.btn_gopay);//去结算

        tv1 = (TextView) findViewById(R.id.tv1);//已经退款
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        initRecycleView(recycle_view);
    }

    /**
     * init - data
     */
    private void initData() {
        OrderId = getIntent().getStringExtra("Id");
        setLeftBackMenuVisibility(PersonOrderDetailActivity.this, "");
        personOrderDetailAdapter = new PersonOrderDetailAdapter(this);
        /**
         * 获取订单详情数据
         */
        getOrderDetial(OrderId);
        recycle_view.setAdapter(personOrderDetailAdapter);
        recycle_view.setNestedScrollingEnabled(false);

    }

    /**
     * init - event
     */
    private void initEvent() {
        rl_businessly.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_yesgoods.setOnClickListener(this);
        btn_againorder.setOnClickListener(this);
        btn_tuikuan.setOnClickListener(this);
        btn_gopay.setOnClickListener(this);
        tv_goodsstatue.setOnClickListener(this);
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
        WebRequestHelper.json_post(PersonOrderDetailActivity.this, URLText.ORDER_DETIAL, RequestParamsPool.orderDetial(Id), new MyAsyncHttpResponseHandler(PersonOrderDetailActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    orderDetial = GsonUtils.fromJson(object.optString("MainData"), PersonOrderDetialBean.class);
                    /**
                     * 支付类型 - 这里主要判断一下线下支付的订单 = 7的状态不同
                     */
                    PayAppName = orderDetial.PayAppName;
                    if (null != orderDetial) {
                        tv_name.setText(orderDetial.Recipients);
                        tv_phone.setText(orderDetial.PhoneNumber);
                        tv_adddress.setText(orderDetial.Address);
                        tv_ordernumber.setText(orderDetial.Number);//订单号
                        tv_goodyssmoney.setText(orderDetial.Seller.UserExtInfo.DeliveryService.Name);//运费--------------
                        tv_goodsbxmoney.setText(orderDetial.Seller.UserExtInfo.Premium.Name);//保险---------------
                        tv_fapiao.setText(orderDetial.InvoiceTitle);//发票----------------
//                        tv_goodsstatue.setText(orderDetial.OrderStateName);//订单状态-----------

                        if (tv_liuyandata.getVisibility() == View.GONE) {//留言数据
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

                        orderList = orderDetial.OrderProducts;
                        if (orderList.size() > 0){
                            tv_paymoney.setText(orderList.get(0).Currency + UIUtils.formatPrice(Double.parseDouble(orderDetial.Price)));
                        }
//                        personOrderDetailAdapter.setStoreCount(orderDetial.Seller.UserExtInfo.StoreCount);
                        personOrderDetailAdapter.setData(orderList);//将商品数据传递
                        personOrderDetailAdapter.setOrderDetailData(orderDetial);
                        personOrderDetailAdapter.notifyDataSetChanged();
                        if (orderDetial.OrderState == 1){//未支付
                            btn_cancel.setVisibility(View.VISIBLE);
                            btn_gopay.setVisibility(View.VISIBLE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_dengdaijiedan);
                            tv_orderstatue.setText("未支付");
                            tv_cancelstatue.setText("已发货");
                            tv_goodsstatue.setText("未支付");
                            tv1.setText("待支付：");
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.order_successgray);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 2){//已支付      -----个人已经付款
                            btn_cancel.setVisibility(View.VISIBLE);
                            btn_againorder.setVisibility(View.VISIBLE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_dengdaijiedan);
                            tv_orderstatue.setText("等待接单");
                            tv_goodsstatue.setText("已支付");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.order_successgray);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 3){//已发货--------------
                            btn_yesgoods.setVisibility(View.VISIBLE);//确认收货
                            btn_cancel.setVisibility(View.VISIBLE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_dengdaishouhuo);
                            tv_orderstatue.setText("等待收货");
                            tv_goodsstatue.setText("已发货");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 4){//已完成--------
                            btn_againorder.setVisibility(View.VISIBLE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_dingdanwancheng);
                            tv_orderstatue.setText("订单完成");
                            tv_goodsstatue.setText("已收货");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.ordercenter_icon4);
                        }else if(orderDetial.OrderState == 5){//个未付款取消
                            btn_againorder.setVisibility(View.VISIBLE);
                            btn_cancel.setVisibility(View.GONE);
                            btn_gopay.setVisibility(View.GONE);
                            btn_tuikuan.setVisibility(View.GONE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_lianxituikuan);
                            tv_orderstatue.setText("订单取消");
                            tv_cancelstatue.setText("订单取消");
                            tv_goodsstatue.setText("订单取消");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 6){//退款审核中
                            btn_tuikuan.setVisibility(View.GONE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_lianxituikuan);
                            tv_orderstatue.setText("联系客服退款");
                            tv_cancelstatue.setText("未收货");
                            tv_goodsstatue.setText("退款审核");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);

                            /**
                             * 如果是个人的订单退款审核中，那么就是弹出弹框
                             */
                            tv_goodsstatue.setTextColor(Color.parseColor("#CE2020"));
                        }else if (orderDetial.OrderState == 7){//已退款
                            btn_cancel.setVisibility(View.GONE);
                            btn_yesgoods.setVisibility(View.GONE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_dingdanwancheng);
                            tv_orderstatue.setText("退款成功");
                            tv2.setText("订单取消");
                            tv3.setText("退款成功");
                            tv_cancelstatue.setText("退款审核");
                            if ("线下支付".equals(PayAppName)){
                                tv_goodsstatue.setText("商家安排退款");//订单状态
                            }else{
                                tv_goodsstatue.setText("已退款");//订单状态
                            }
                            tv1.setText("已退款：");
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.ordercenter_icon4);
                        }else if(orderDetial.OrderState == 8){//商家订单取消---------------
                            btn_tuikuan.setVisibility(View.VISIBLE);
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_lianxituikuan);
                            tv_orderstatue.setText("订单取消");
                            tv_cancelstatue.setText("订单取消");
                            tv_goodsstatue.setText("商家取消订单");//订单状态
                            iv_orderstatue1.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue2.setBackgroundResource(R.mipmap.ordercenter_icon4);
                            iv_orderstatue3.setBackgroundResource(R.mipmap.order_successgray);
                        }else if (orderDetial.OrderState == 9){//----------个人已支付的状态取消订单
                            btn_tuikuan.setVisibility(View.VISIBLE);
                            btn_cancel.setVisibility(View.GONE);
                            btn_againorder.setVisibility(View.GONE);
                            btn_yesgoods.setVisibility(View.GONE);
                            tv_orderstatue.setText("订单取消");
                            tv_cancelstatue.setText("订单取消");
                            tv_goodsstatue.setText("订单取消");
                            iv_orderstatue0.setBackgroundResource(R.mipmap.order_lianxituikuan);
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
     * 没有红包的对话框
     * @param phoneNumber
     */
    public void orderStatusSeven(final String phoneNumber){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                if (!TextUtils.isEmpty(phoneNumber)){
                    call(phoneNumber);
                }
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "联系商家 "+phoneNumber, "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 调用拨号界面
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /**
     * 事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //留言
            case R.id.rl_businessly:
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
            //取消订单
            case R.id.btn_cancel:
                //待支付的取消订单
                if (orderDetial.OrderState == 1 || orderDetial.OrderState == 2 || orderDetial.OrderState == 3){
                    cancel_myorder(OrderId);
                }
                break;
            //确认收货
            case R.id.btn_yesgoods:
                receipteMyOrder(OrderId);
                break;
            //再来一单
            case R.id.btn_againorder:
                Intent intent = new Intent(PersonOrderDetailActivity.this, GoodDetailsActivity.class);
                intent.putExtra("id", MyUserInfoUtils.getInstance().myUserInfo.ProductId);
                startActivity(intent);
                break;
            //申请退款
            case R.id.btn_tuikuan:
                if (orderDetial.OrderState == 9 || orderDetial.OrderState == 8){
                    //申请退款
                    quit_moneny(OrderId);
                }
                break;
            //去结算
            case R.id.btn_gopay:
                payDialog = new PayDialog(PersonOrderDetailActivity.this, UIUtils.formatPrice(Double.parseDouble(orderDetial.Price)));
                Window window = payDialog.getWindow();
                WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                //设置x坐标
                params.x = 0;
                //设置y坐标
                params.y = 0;
                window.setAttributes(params);
                //设置点击Dialog外部任意区域关闭Dialog
                payDialog.setCanceledOnTouchOutside(true);
                payDialog.setOnClickHandler(new PayDialog.OnClickHandler() {
                    @Override
                    public void onClick(View view) {
                    switch (view.getId()) {
                        case R.id.paymode_wx:
                            weChatPaySign(OrderId);
                            break;
                        case R.id.paymode_zfb:
                            alipaySign(OrderId);
                            break;
                        case R.id.paymode_md:
                            menDian();
                            break;
                            default:
                                break;
                    }
                    }
                });
                payDialog.show();
                break;
            case R.id.tv_goodsstatue:
                if (orderDetial.OrderState == 6){
                    if (null != orderDetial.Seller.UserExtInfo.PhoneNumber){
                        orderStatusSeven(orderDetial.Seller.UserExtInfo.OfficePhone);
                    }
                }
                break;
                default:
                    break;
        }
    }

    /**
     * mendian
     */
    private void menDian() {
        Intent intent = new Intent(PersonOrderDetailActivity.this, PayLineActivity.class);
        intent.putExtra("orderDetial", (Serializable) orderDetial);
        startActivity(intent);
        finish();
    }

    /**
     * 支付宝支付签名
     * @param Id
     */
    private void alipaySign(String Id) {
        WebRequestHelper.json_post(PersonOrderDetailActivity.this, URLText.ALIPAY_SIGN, RequestParamsPool.alipaySign(Id), new MyAsyncHttpResponseHandler(PersonOrderDetailActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                alipaySignResult = GsonUtils.fromJson(object.optString("MainData"), AlipaySignResult.class);
                if (null != alipaySignResult) {
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(PersonOrderDetailActivity.this);
                            String result = alipay.pay(aliPayParameter(alipaySignResult));
                            Log.i("msp", result.toString());
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }
        });
    }

    /**
     * 构造支付宝支付参数
     * @param result
     * @return
     */
    private String aliPayParameter(AlipaySignResult result) {
        String aliPayString = "app_id=" + result.app_id + "&";
        aliPayString = aliPayString + "method=" + result.method + "&";
        aliPayString = aliPayString + "format=" + result.format + "&";
        aliPayString = aliPayString + "charset=" + result.charset + "&";
        aliPayString = aliPayString + "sign_type=" + result.sign_type + "&";
        aliPayString = aliPayString + "timestamp=" + result.timestamp + "&";
        aliPayString = aliPayString + "version=" + result.version + "&";
        aliPayString = aliPayString + "notify_url=" + result.notify_url + "&";
        aliPayString = aliPayString + "biz_content=" + result.biz_content + "&";
        aliPayString = aliPayString + "sign=" + result.sign;
        return aliPayString;
    }

    /**
     * 微信支付
     * @param Id
     */
    private void weChatPaySign(final String Id) {
        WebRequestHelper.json_post(PersonOrderDetailActivity.this, URLText.WECHAT_SIGN, RequestParamsPool.weChatSign(Id), new MyAsyncHttpResponseHandler(PersonOrderDetailActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    weChatSign = GsonUtils.fromJson(jsonObject.optString("MainData"), WeChatSign.class);
                    final IWXAPI msgApi = WXAPIFactory.createWXAPI(PersonOrderDetailActivity.this, null);
                    msgApi.registerApp(weChatSign.appid);
                    LogUtils.instance.d("appid=" + weChatSign.appid);
//                    WXPayEntryActivity wxPayEntryActivity = new WXPayEntryActivity();
//                    wxPayEntryActivity.setOrderId(Id);
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    BaseApplication.getInstance().OrderId = Id;
                    req.appId = weChatSign.appid;
                    req.partnerId = weChatSign.partnerid;
                    req.prepayId = weChatSign.prepayid;
                    req.nonceStr = weChatSign.noncestr;
                    req.timeStamp = weChatSign.timestamp;
                    req.packageValue = "Sign=WXPay";
                    req.sign = weChatSign.sign;
                    msgApi.sendReq(req);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 个人确认收货
     * @param ID
     */
    private void receipteMyOrder(String ID) {
        WebRequestHelper.json_post(PersonOrderDetailActivity.this, URLText.RECEIPEMY_ORDER, RequestParamsPool.queren_order(ID), new MyAsyncHttpResponseHandler(PersonOrderDetailActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
                Toast.makeText(PersonOrderDetailActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                finish();
                LogUtils.instance.d("确认收款=" + result);
            }
        });
    }

    /**
     * 申请退款 - 个人或者是取消订单 -  订单状态必须是商家取消订单或已支付
     * @param Id
     */
    private void quit_moneny(String Id) {
        WebRequestHelper.json_post(PersonOrderDetailActivity.this, URLText.QUIT_MONEY, RequestParamsPool.quit_moneny(Id), new MyAsyncHttpResponseHandler(PersonOrderDetailActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
//                Toast.makeText(PersonOrderDetailActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                Toast.makeText(PersonOrderDetailActivity.this,"退款申请审核中", Toast.LENGTH_SHORT).show();
                LogUtils.instance.d("退款=" + result);
//                finish();
            }
        });
    }

    /**
     * 订单取消 - 1,2,3，状态进行
     * @param Id
     */
    private void cancel_myorder(String Id) {
        WebRequestHelper.json_post(PersonOrderDetailActivity.this, URLText.CANCEL_MY_MONEY, RequestParamsPool.quit_moneny(Id), new MyAsyncHttpResponseHandler(PersonOrderDetailActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    getOrderDetial(OrderId);
                }
                Toast.makeText(PersonOrderDetailActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
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