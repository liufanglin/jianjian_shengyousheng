package com.ximai.savingsmore.save.activity;

import android.os.Bundle;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;

/**
 * Created by caojian on 16/12/15.
 */
public class ComplainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complain_activity);
        setCenterTitle("投诉反馈");
        setLeftBackMenuVisibility(ComplainActivity.this,"");
    }
}
