package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.GoodDetial;
import com.ximai.savingsmore.save.modle.OrderIdList;
import com.ximai.savingsmore.save.modle.OrderStateResult;
import com.ximai.savingsmore.save.modle.PayResult;
import com.ximai.savingsmore.save.modle.PersonOrderDetialBean;
import com.ximai.savingsmore.save.modle.SubmitOrderResult;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.ximai.savingsmore.wxapi.WXPayEntryActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by caojian on 17/1/5.
 */
public class PaySuccessActivity extends BaseActivity implements View.OnClickListener {
    private TextView shoukuanfang, order_number, pay_number, pay_type, pay_date, pay_jifen,tv_address;
    private PayResult payResult;
    private Button btn_againorder;
    private Button btn_seepoint;
    private Button btn_seelogistics;
    private OrderStateResult orderStateResult;
    private String id;
    private Button btn_cancels;
    private SubmitOrderResult submitOrderResult;//提交订单状态数据
    private OrderIdList goodsList;
    private PersonOrderDetialBean orderDetial;
    private String cuxiaoOrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_success_activity);

        initView();

        initData();
        
        initEvent();
    }

    /**
     * init - view
     */
    private void initView() {
        setCenterTitle("支付成功");
        setLeftBackMenuVisibility(PaySuccessActivity.this, "");

        shoukuanfang = (TextView) findViewById(R.id.shoukuanfang);
        order_number = (TextView) findViewById(R.id.order_number);
        pay_number = (TextView) findViewById(R.id.pay_price);
        pay_type = (TextView) findViewById(R.id.pay_type);
        pay_date = (TextView) findViewById(R.id.pay_date);
        pay_jifen = (TextView) findViewById(R.id.pay_jifen);
        tv_address = (TextView) findViewById(R.id.tv_address);

        btn_againorder = (Button) findViewById(R.id.btn_againorder);
        btn_seepoint = (Button) findViewById(R.id.btn_seepoint);
        btn_seelogistics = (Button) findViewById(R.id.btn_seelogistics);
        btn_cancels = (Button) findViewById(R.id.btn_cancels);
    }

    /**
     * init - data
     */
    private void initData() {
//        submitOrderResult = (SubmitOrderResult) getIntent().getSerializableExtra("SubmitOrderResult");//用来跳转在来一单
        /**
         * 获取订单支付数据
         */
        id = getIntent().getStringExtra("Id");//这是订单id
        /**
         * 获取支付结果
         */
        getPayResult(id);
        /**
         * 获取订单详情
         */
        getOrderDetial(id);

        //如果是这个的送货服务那么进行弹框弹出f
        String deliveryService = PreferencesUtils.getString(PaySuccessActivity.this, "DeliveryService", "");
        if ("“省又省”送货服务".equals(deliveryService)){
            transfer();
        }else if ("发货前与商家确认".equals(deliveryService)){
            transferTwo();
        }
    }

    /**
     * init - event
     */
    private void initEvent() {
        btn_cancels.setOnClickListener(this);
        btn_againorder.setOnClickListener(this);
        btn_seepoint.setOnClickListener(this);
        btn_seelogistics.setOnClickListener(this);
    }

    /**
     * 事件处理 - 需要对接后台
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_cancels://取消订单
                cancel_myorder(id);
                break;
            case R.id.btn_againorder://在来一单
//                Intent intent2 = new Intent(PaySuccessActivity.this, OrderBuyActivity.class);
//                Bundle bundle = new Bundle();
//                GoodDetial goodDetial = BaseApplication.getInstance().GoodDetial;
//                bundle.putSerializable("good", goodDetial );
//                intent2.putExtras(bundle);
//                startActivity(intent2);
//                finish();
//                for (int i = 0; i < submitOrderResult.MainData.size(); i++) {
//                    if ("在线支付".equals(submitOrderResult.MainData.get(i).PayType)){
//                        Intent intent1 = new Intent(PaySuccessActivity.this, GoodDetailsActivity.class);
//                        intent1.putExtra("id", submitOrderResult.MainData.get(i).OrderProducts.get(i).ProductId);
//                        startActivity(intent1);
//                        finish();
//                        break;
//                    }else{
//                        goodsDetail();
//                    }
//                }

                for (int i = 0; i < orderDetial.OrderProducts.size(); i++) {
                    if (null != orderDetial.OrderProducts.get(i).ProductId){
                        cuxiaoOrderId = orderDetial.OrderProducts.get(i).ProductId;
                    }
                }

                if (null == cuxiaoOrderId){
                    goodsDetail();
                }else{
                    Intent intent1 = new Intent(PaySuccessActivity.this, GoodDetailsActivity.class);
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
            case R.id.btn_seelogistics://查看物流
                /**
                 * 查看物流将下单页面关闭
                 */
                NotificationCenter.defaultCenter().postNotification(Constants.SEE_WULIU_FINISHORDERBUY,"");
                Intent intent = new Intent(this, PersonOrderDetailActivity.class);
                intent.putExtra("Id", id);//订单Id
                startActivity(intent);
                finish();
                break;
                default:
                    break;
        }
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
        Dialog dialog = new XiMaiPopDialog(PaySuccessActivity.this, "温馨提示", "此商品为非促销商品。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     *
     * @param Id
     */
    private void quit_moneny(String Id) {
        WebRequestHelper.json_post(PaySuccessActivity.this, URLText.QUIT_MONEY, RequestParamsPool.quit_moneny(Id), new MyAsyncHttpResponseHandler(PaySuccessActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
//                    Toast.makeText(PaySuccessActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                    Toast.makeText(PaySuccessActivity.this,"退款申请审核中", Toast.LENGTH_SHORT).show();
                    LogUtils.instance.d("退款=" + result);
                    finish();
                }
            }
        });
    }

    /**
     * 订单取消 - 1,2,3，状态进行 - 支付成功的取消订单
     * @param Id
     */
    private void cancel_myorder(String Id) {
        WebRequestHelper.json_post(PaySuccessActivity.this, URLText.CANCEL_MY_MONEY, RequestParamsPool.quit_moneny(Id), new MyAsyncHttpResponseHandler(PaySuccessActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                orderStateResult = GsonUtils.fromJson(result, OrderStateResult.class);
                if (orderStateResult.IsSuccess.equals("true")) {
                    Toast.makeText(PaySuccessActivity.this, orderStateResult.Message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PaySuccessActivity.this, PersonOrderDetailActivity.class);
                    intent.putExtra("Id", id);//订单Id
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     * 获取支付成功返回数据
     * {
     "IsSuccess": true,
     "Message": "订单状态为已支付",
     "Id": null,
     "MainData": {
     "StoreName": "了东",
     "Number": "2017121417422525498175",
     "Price": 0.1,
     "PayApp": 1,
     "PayTime": "2017-12-14T17:42:40.567",
     "GivePoint": 0,
     "PayAppName": "支付宝支付",
     "PayTimeName": "2017-12-14 17:42:40"
     },
     "ShowData": null,
     "OtherData": null,
     "TotalRecordCount": 0,
     "TotalPageCount": 0,
     "AllTotalRecordCount": 0
     }
     *
     *
     * @param Id
     */
    private void getPayResult(String Id) {
        WebRequestHelper.json_post(PaySuccessActivity.this, URLText.PAY_RESULT, RequestParamsPool.payResult(Id), new MyAsyncHttpResponseHandler(PaySuccessActivity.this) {
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
                        pay_number.setText(payResult.Price);
                        pay_type.setText(payResult.PayAppName);
                        pay_date.setText(payResult.PayTimeName);
                        pay_jifen.setText("在确认收货后获得");
                        tv_address.setText(payResult.Address);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                try {
//                    JSONObject object = new JSONObject(result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    /**
     * 获取订单详情数据
     */
    private void getOrderDetial(String Id) {
        WebRequestHelper.json_post(PaySuccessActivity.this, URLText.ORDER_DETIAL, RequestParamsPool.orderDetial(Id), new MyAsyncHttpResponseHandler(PaySuccessActivity.this) {
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

    /**
     * 如果运送费用那块的
     */
    public void transfer(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call("02138687133");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "省又省送货服务电话021-38687133", "拨打", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 如果运送费用那块的
     */
    public void transferTwo(){
        String orderStoreName = PreferencesUtils.getString(PaySuccessActivity.this, "OrderStoreName", "");
        final String orderPhoneNumber = PreferencesUtils.getString(PaySuccessActivity.this, "OrderPhoneNumber", "");
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call(orderPhoneNumber);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", orderStoreName+"送货服务电话", "拨打", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
}