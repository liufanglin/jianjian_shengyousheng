package com.ximai.savingsmore.save.activity;

import android.os.Bundle;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;

/**
 * Created by caojian on 16/12/23.
 */
//个人协议
public class PersonalAgreement extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_agreement);
        setLeftBackMenuVisibility(PersonalAgreement.this, "");
        setCenterTitle("《省又省个人用户服务协议》");
    }
}
