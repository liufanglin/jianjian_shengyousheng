package com.ximai.savingsmore.save.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximai.savingsmore.R;

/**
 * Created by luxing on 2018/2/27 0027.
 * 促销优惠卡
 */

public class FavourableCardDialog extends Dialog{

    private String phone;
    private String name;
    private TextView tv_phone,tv_name;
    private ImageView iv_close;

    public FavourableCardDialog(Context context) {
        super(context);
    }


    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_favourablecard);
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
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_close = (ImageView) findViewById(R.id.iv_close);
    }

    private void initEvent() {
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initData() {
        if ("暂无".equals(phone)){
            tv_phone.setText("暂无");
        }else{
            if (phone.length() == 11){
                String s1 = phone.substring(0, 3);
                String s2 = phone.substring(7, 11);
                tv_phone.setText(s1 + "  ****  " + s2);
            }else{
                tv_phone.setText("暂无");
            }
        }

        if ("暂无".equals(name)){
            tv_name.setText("暂无");
        }else{
            tv_name.setText(name);
        }
    }



}
