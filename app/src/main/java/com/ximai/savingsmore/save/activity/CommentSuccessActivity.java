package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;

/**
 * Created by caojian on 16/12/13.
 */
public class CommentSuccessActivity extends BaseActivity {
    private TextView jifen;
    private RelativeLayout title＿right;
    private TextView tv_seejifen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_success);

        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);

        title＿right = (RelativeLayout) findViewById(R.id.title＿right);
        tv_seejifen = (TextView) findViewById(R.id.tv_seejifen);

        title＿right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_seejifen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(CommentSuccessActivity.this, PointManagerActivity.class);
                startActivity(intent4);
                finish();
            }
        });

        jifen = (TextView) findViewById(R.id.jifen);
        if (null != getIntent() && null != getIntent().getStringExtra("jifen") && getIntent().getStringExtra("jifen").equals("2")) {
            jifen.setText("奖励积分10分");
        } else {
            jifen.setText("奖励积分10分");
        }
    }
}
