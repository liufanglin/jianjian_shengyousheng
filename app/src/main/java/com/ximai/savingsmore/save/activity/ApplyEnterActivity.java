package com.ximai.savingsmore.save.activity;

import android.os.Bundle;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;

/**
 * Created by caojian on 16/11/18.
 */
//商家申请入驻
public class ApplyEnterActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_enter_activity);
        setLeftBackMenuVisibility(ApplyEnterActivity.this,"");
        setCenterTitle("注册");
    }
}
