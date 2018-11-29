package com.ximai.savingsmore.wxapi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.save.activity.GoodDetailsActivity;
import com.ximai.savingsmore.save.activity.PersonOrderDetailActivity;
import com.ximai.savingsmore.save.activity.PointManagerActivity;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.OrderStateResult;
import com.ximai.savingsmore.save.modle.PayResult;
import com.ximai.savingsmore.save.modle.PersonOrderDetialBean;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by caojian on 17/1/11.
 */


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler, View.OnClickListener {

    private IWXAPI api;
    private String Id;

    private TextView shoukuanfang, order_number, pay_number, tv_address,pay_type, pay_date, pay_jifen;
    private PayResult payResult;
    private LinearLayout pay;
    private Button btn_cancels;
    private Button btn_againorder;
    private Button btn_seepoint;
    private Button btn_seelogistics;
    private OrderStateResult orderStateResult;
    private PersonOrderDetialBean orderDetial;
    private String cuxiaoOrderId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx9d2fb51599ac7698");
        api.handleIntent(getIntent(), this);
        setContentView(R.layout.pay_success_activity);
        setCenterTitle("支付成功");
        setLeftBackMenuVisibility(WXPayEntryActivity.this, "");
        shoukuanfang = (TextView) findViewById(R.id.shoukuanfang);
        order_number = (TextView) findViewById(R.id.order_number);
        pay_number = (TextView) findViewById(R.id.pay_price);
        pay_type = (TextView) findViewById(R.id.pay_type);
        tv_address = (TextView) findViewById(R.id.tv_address);
        pay_date = (TextView) findViewById(R.id.pay_date);
        pay_jifen = (TextView) findViewById(R.id.pay_jifen);
        pay = (LinearLayout) findViewById(R.id.pay);

        btn_cancels = (Button) findViewById(R.id.btn_cancels);
        btn_againorder = (Button) findViewById(R.id.btn_againorder);
        btn_seepoint = (Button) findViewById(R.id.btn_seepoint);
        btn_seelogistics = (Button) findViewById(R.id.btn_seelogistics);
        
        initEvent();

        getOrderDetial(BaseApplication.getInstance().OrderId);
    }

    /**
     * 事件处理
     */
    private void initEvent() {
        btn_cancels.setOnClickListener(this);
        btn_againorder.setOnClickListener(this);
        btn_seepoint.setOnClickListener(this);
        btn_seelogistics.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancels://订单取消
                cancel_myorder(BaseApplication.getInstance().OrderId);
                break;
            case R.id.btn_againorder://在来一单
//                Intent intent2 = new Intent(WXPayEntryActivity.this, OrderBuyActivity.class);
//                Bundle bundle = new Bundle();
//                GoodDetial goodDetial = BaseApplication.getInstance().GoodDetial;
//                bundle.putSerializable("good", goodDetial );
//                intent2.putExtras(bundle);
//                startActivity(intent2);
//                finish();
                for (int i = 0; i < orderDetial.OrderProducts.size(); i++) {
                    if (null != orderDetial.OrderProducts.get(i).ProductId){
                        cuxiaoOrderId = orderDetial.OrderProducts.get(i).ProductId;
                    }
                }

                if (null == cuxiaoOrderId){
                    goodsDetail();
                }else{
                    Intent intent1 = new Intent(WXPayEntryActivity.this, GoodDetailsActivity.class);
                    intent1.putExtra("id", cuxiaoOrderId);
                    startActivity(intent1);
                    finish();
                }
                break;
            case R.id.btn_seepoint://查看积分
                Intent intent4 = new Intent(this, PointManagerActivity.class);
                startActivity(intent4);
                finish();
                break;
            case R.id.btn_seelogistics://查看物流路
//                Intent intent5 = new Intent(this, OrderCenterActivity.class);
//                startActivity(intent5);
//                finish();

                Intent intent = new Intent(this, PersonOrderDetailActivity.class);
                intent.putExtra("Id", BaseApplication.getInstance().OrderId);//订单Id
                startActivity(intent);
                finish();
                break;
        }
    }

//    /**
//     * 申请退款 - 个人或者是取消订单 -  订单状态必须是商家取消订单或已支付
//     * @param Id
//     */
//    private void quit_moneny(String Id) {
//        WebRequestHelper.json_post(WXPayEntryActivity.this, URLText.QUIT_MONEY, RequestParamsPool.quit_moneny(Id), new MyAsyncHttpResponseHandler(WXPayEntryActivity.this) {
//            @Override
//            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
//                String result = new String(responseBody);
//                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
//                if (orderStateResult.IsSuccess.equals("true")) {
////                    Toast.makeText(WXPayEntryActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(WXPayEntryActivity.this,"退款申请审核中", Toast.LENGTH_SHORT).show();
//                    LogUtils.instance.d("退款=" + result);
//                    finish();
//                }
//            }
//        });
//    }

    /**
     * 订单取消 - 1,2,3，状态进行 - 支付成功的取消订单
     * @param Id
     */
    private void cancel_myorder(String Id) {
        WebRequestHelper.json_post(WXPayEntryActivity.this, URLText.CANCEL_MY_MONEY, RequestParamsPool.quit_moneny(Id), new MyAsyncHttpResponseHandler(WXPayEntryActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    Toast.makeText(WXPayEntryActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(WXPayEntryActivity.this, PersonOrderDetailActivity.class);
                    intent.putExtra("Id", BaseApplication.getInstance().OrderId);//订单Id
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void setOrderId(String Id) {
        this.Id = Id;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                getPayResult(BaseApplication.getInstance().OrderId);
            } else if (resp.errCode == -2) {
                // pay.setVisibility(View.GONE);
                setCenterTitle("支付失败");
                Toast.makeText(this, "没有交易", Toast.LENGTH_SHORT).show();
                Toast.makeText(WXPayEntryActivity.this, "没有交易", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                // pay.setVisibility(View.GONE);
                setCenterTitle("支付失败");
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void getPayResult(String Id) {
        LogUtils.instance.d("Id+" + Id);
        WebRequestHelper.json_post(WXPayEntryActivity.this, URLText.PAY_RESULT, RequestParamsPool.payResult(Id), new MyAsyncHttpResponseHandler(WXPayEntryActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                LogUtils.instance.d("支付结果=" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    payResult = GsonUtils.fromJson(object.optString("MainData"), PayResult.class);
                    if (null != payResult) {
                        shoukuanfang.setText(payResult.StoreName);
                        order_number.setText(payResult.Number);
                        pay_number.setText("¥" + payResult.Price);
                        pay_type.setText(payResult.PayAppName);
                        pay_date.setText(payResult.PayTimeName);
                        tv_address.setText(payResult.Address);
                        pay_jifen.setText("在确认收货后获得");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 非促销品不可以跳转商品详情
     */
    public void goodsDetail(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                finish();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(WXPayEntryActivity.this, "温馨提示", "此商品为非促销商品。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 获取订单详情数据
     */
    private void getOrderDetial(String Id) {
        WebRequestHelper.json_post(WXPayEntryActivity.this, URLText.ORDER_DETIAL, RequestParamsPool.orderDetial(Id), new MyAsyncHttpResponseHandler(WXPayEntryActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    orderDetial = GsonUtils.fromJson(object.optString("MainData"), PersonOrderDetialBean.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}