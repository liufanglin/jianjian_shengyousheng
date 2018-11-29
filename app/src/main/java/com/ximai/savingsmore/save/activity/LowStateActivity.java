package com.ximai.savingsmore.save.activity;

import android.os.Bundle;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;

/**
 * Created by caojian on 16/12/15.
 */
public class LowStateActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.law_state);
        setCenterTitle("法律声明");
        setLeftBackMenuVisibility(LowStateActivity.this, "");
    }
}
