package com.ximai.savingsmore.save.activity;

import android.os.Bundle;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;

/**
 * Created by caojian on 16/12/23.
 */
public class BUsinessAgreement extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_argeement);
        setLeftBackMenuVisibility(BUsinessAgreement.this, "");
        setCenterTitle("“省又省”APP发布促销");
    }
}
