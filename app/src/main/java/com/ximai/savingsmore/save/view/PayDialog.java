package com.ximai.savingsmore.save.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.utils.UIUtils;


/**
 * Created by luxing on 2017/8/23 0023.
 */

public class PayDialog extends AlertDialog{

    public interface OnClickHandler {
        void onClick(View view);
    }

    OnClickHandler onClickHandler;

    private String price;                           //支付价格信息
    private Context context;                        //上下文
    private ImageView paymode_cancel;
    private TextView paymode_money;
    private RelativeLayout paymode_wx;
    private RelativeLayout paymode_zfb;
    private RelativeLayout paymode_md;
    private TextView tv_line_pay;

    public void setOnClickHandler(OnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
    }

    public PayDialog(Context context, String price) {
        super(context);
        this.context = context;
        this.price = price;
    }

    public void setLinePay(){
        tv_line_pay.setText("现金支付");

    }
    /**
     * init-
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_paymode);
        init();
    }

    /**
     * init
     */
    private void init() {
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        paymode_money = (TextView) findViewById(R.id.paymode_money);
        paymode_cancel = (ImageView) findViewById(R.id.paymode_cancel);
        paymode_wx = (RelativeLayout) findViewById(R.id.paymode_wx);
        paymode_zfb = (RelativeLayout) findViewById(R.id.paymode_zfb);
        paymode_md = (RelativeLayout) findViewById(R.id.paymode_md);
        tv_line_pay= (TextView) findViewById(R.id.tv_line_pay);
    }

    private void initData() {
        paymode_money.setText("¥"+price);
    }
    /**
     * 支付方式
     */
    private void initEvent() {
        paymode_cancel.setOnClickListener(new View.OnClickListener() {                 //cancel
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        paymode_wx.setOnClickListener(new View.OnClickListener() {                      //WX
            @Override
            public void onClick(View view) {
//                MemberShipCardActivity.getInstance().WXPay();
                onClickHandler.onClick(view);
                dismiss();
            }
        });
        paymode_zfb.setOnClickListener(new View.OnClickListener() {                     //alipay
            @Override
            public void onClick(View view) {
//                MemberShipCardActivity.getInstance().AliPay();
                onClickHandler.onClick(view);
                dismiss();
            }
        });
        paymode_md.setOnClickListener(new View.OnClickListener() {                     //medina
            @Override
            public void onClick(View view) {
//                MemberShipCardActivity.getInstance().AliPay();
                onClickHandler.onClick(view);
                dismiss();
            }
        });
    }
}
