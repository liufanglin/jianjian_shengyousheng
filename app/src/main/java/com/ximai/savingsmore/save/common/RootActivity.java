package com.ximai.savingsmore.save.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ximai.savingsmore.library.core.CoreJob;


public class RootActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //记录activity打开记录
        CoreJob.addToActivityStack(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //移除activity打开记录
        CoreJob.removeFormActivityStack(this);
    }




}
