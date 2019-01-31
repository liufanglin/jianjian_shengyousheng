package com.ximai.savingsmore.save.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.OrderProducts;
import com.ximai.savingsmore.save.modle.PersonOrderDetialBean;
import com.ximai.savingsmore.save.modle.SubmitOrderResult;
import com.ximai.savingsmore.save.modle.submitOrderResults;
import com.ximai.savingsmore.save.utils.APPUtil;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.PayDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

import static com.ximai.savingsmore.R.id.number;

/**
 * Created by luxing on 2018/1/10 0010.
 * 线下支付
 */

public class PayLineActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout back;
    private TextView tv_gathering;
    private TextView tv_ordernumber;
    private TextView tv_payprice;
    private TextView tv_paystyle;
    private TextView tv_paydate;
    private TextView tv_paypoint;
    private Button btn_yespay;
    private submitOrderResults submitOrderResult;
    private PersonOrderDetialBean personOrderDetialBean;
    private boolean isWeiZhiFu = true;
    private String id;
    private TextView tv_getjifen;
    private TextView tv_address;
    private KyLoadingBuilder builder;
    private PayDialog payDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_line);
        initView();
        initEvent();
        initData();
    }

    /**
     * view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);
        back = (RelativeLayout) findViewById(R.id.back);
        tv_gathering = (TextView) findViewById(R.id.tv_gathering);
        tv_ordernumber = (TextView) findViewById(R.id.tv_ordernumber);
        tv_payprice = (TextView) findViewById(R.id.tv_payprice);
        tv_paystyle = (TextView) findViewById(R.id.tv_paystyle);
        tv_paydate = (TextView) findViewById(R.id.tv_paydate);
        tv_paypoint = (TextView) findViewById(R.id.tv_paypoint);
        btn_yespay = (Button) findViewById(R.id.btn_yespay);
        tv_getjifen = (TextView) findViewById(R.id.tv_getjifen);
        tv_address = (TextView) findViewById(R.id.tv_address);
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        btn_yespay.setOnClickListener(this);
    }

    /**
     * data
     */
    private void initData() {
        submitOrderResult = (submitOrderResults) getIntent().getSerializableExtra("submitOrderResult");
        if (null != submitOrderResult){
            try{
                isWeiZhiFu = false;
                tv_gathering.setText(submitOrderResult.MainData.get(0).OrderProducts.get(0).StoreName);//收款方
                tv_ordernumber.setText(submitOrderResult.MainData.get(0).Number);//订单号
                tv_payprice.setText(submitOrderResult.MainData.get(0).OrderProducts.get(0).Currency+submitOrderResult.MainData.get(0).Price);//促销价格
//            tv_paystyle.setText(submitOrderResult.MainData.get(0).PayType);//支付工具
                tv_paystyle.setText("线下支付");//支付工具
                List<OrderProducts> orderProducts = submitOrderResult.MainData.get(0).OrderProducts;
                tv_paydate.setText(orderProducts.get(0).CreateTimeName);//订单创建时间
                double v = submitOrderResult.MainData.get(0).Price * 5;
                String s = String.valueOf(v);
                tv_paypoint.setText(s.substring(0,s.indexOf(".")) +"分");//这是使用抵扣积分
//                tv_getjifen.setText(s.substring(0,s.indexOf(".")));//温馨提示下面的文字说明
                tv_getjifen.setText(s.substring(0,s.indexOf(".")));//温馨提示下面的文字说明
                tv_address.setText(orderProducts.get(0).Address);//收款方地址
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            try{
                //这是未支付的状态过来
                isWeiZhiFu = true;
                personOrderDetialBean = (PersonOrderDetialBean) getIntent().getSerializableExtra("orderDetial");
                tv_ordernumber.setText(personOrderDetialBean.Number);//订单号
                tv_payprice.setText(personOrderDetialBean.Currency+ UIUtils.formatPrice(Double.parseDouble(personOrderDetialBean.Price)));//促销价格
                tv_paystyle.setText(personOrderDetialBean.PayType);//支付工具
                List<Goods> orderProducts = personOrderDetialBean.OrderProducts;
                //未支付的Id
                tv_gathering.setText(orderProducts.get(0).StoreName);//收款方
                tv_paydate.setText(orderProducts.get(0).CreateTimeName);//订单创建时间
                tv_paypoint.setText(personOrderDetialBean.DeductionPoint+"分");//这是使用抵扣积分
                tv_address.setText(orderProducts.get(0).Address);//收款方地址
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * event
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back://返回
                finish();
                break;
            case R.id.btn_yespay://线下支付
                String price;
                if (submitOrderResult!=null&&submitOrderResult.MainData!=null){
                    price=submitOrderResult.MainData.get(0).OrderProducts.get(0).Currency+submitOrderResult.MainData.get(0).Price;
                }else {
                    price="0.00";
                }

                payDialog = new PayDialog(PayLineActivity.this, price);
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
                                if (APPUtil.isGoWechat(PayLineActivity.this)){
                                    if (isWeiZhiFu == true){
                                        line_pay(personOrderDetialBean.Id,"wechat");//订单Id
                                    }else{
                                        if (null != submitOrderResult.MainData.get(0).Id){
                                            line_pay(submitOrderResult.MainData.get(0).Id,"wechat");
                                        }
                                    }
                                }

                                break;
                            case R.id.paymode_zfb:
                                if (APPUtil.isGoPay(PayLineActivity.this)){
                                    if (isWeiZhiFu == true){
                                        line_pay(personOrderDetialBean.Id,"ailiPay");//订单Id
                                    }else{
                                        if (null != submitOrderResult.MainData.get(0).Id){
                                            line_pay(submitOrderResult.MainData.get(0).Id,"ailiPay");
                                        }
                                    }
                                }

                                break;
                            case R.id.paymode_md:
                                if (isWeiZhiFu == true){
                                    line_pay(personOrderDetialBean.Id,"");//订单Id
                                }else{
                                    if (null != submitOrderResult.MainData.get(0).Id){
                                        line_pay(submitOrderResult.MainData.get(0).Id,"");
                                    }
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
                payDialog.show();
                payDialog.setLinePay();

                break;
                default:
                    break;
        }
    }

    /**
     * 线下支付
     * @param Id
     */
    private void line_pay(String Id, final String type) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(PayLineActivity.this, URLText.LINE_PAY, RequestParamsPool.line_pay(Id), new MyAsyncHttpResponseHandler(PayLineActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (true == jsonObject.optBoolean("IsSuccess")){
//                        Toast.makeText(PayLineActivity.this, "确认已付款", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PayLineActivity.this, PaySuccessActivity.class);
                        if (isWeiZhiFu == true){
                            intent.putExtra("Id",personOrderDetialBean.Id);
                        }else{
                            intent.putExtra("Id",submitOrderResult.MainData.get(0).Id);
                        }
                        /**
                         * 线下支付成功
                         */
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("SubmitOrderResult",submitOrderResult);
//                        intent.putExtras(bundle);
                        startActivity(intent);


                        if (!TextUtils.isEmpty(type)){
                            if ("wechat".equals(type)){
                                Intent intent1 = new Intent();
                                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                                intent1.setAction(Intent.ACTION_MAIN);
                                intent1.addCategory(Intent.CATEGORY_LAUNCHER);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent1.setComponent(cmp);
                                startActivity(intent1);
                            }else if ("ailiPay".equals(type)){
                                PackageManager packageManager
                                        = getApplicationContext().getPackageManager();
                                Intent intent1 = packageManager.
                                        getLaunchIntentForPackage("com.eg.android.AlipayGphone");
                                startActivity(intent1);
                            }
                        }

                        finish();

                        /**
                         * 如果是确认已付款那么将购买页面进行关闭
                         */
                        NotificationCenter.defaultCenter().postNotification(Constants.SEE_WULIU_FINISHORDERBUY,"");
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    if (null != builder){
                        builder.dismiss();
                    }
                }
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
        builder.setBackTouchable(true);
        builder.show();
    }
}
