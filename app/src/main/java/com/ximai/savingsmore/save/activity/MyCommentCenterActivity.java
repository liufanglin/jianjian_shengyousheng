package com.ximai.savingsmore.save.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.fragment.MyCommentLeftFrament;
import com.ximai.savingsmore.save.fragment.MyCommentRightFragment;

/**
 * Created by caojian on 16/12/14.
 */
public class MyCommentCenterActivity extends BaseActivity implements View.OnClickListener {
    private TextView isBag, noBag;
    private View introduce, sales_good;
    private FragmentManager fragmentManager;
    private MyCommentLeftFrament myCommentLeftFrament;
    private MyCommentRightFragment myCommentRightFragment;
    private RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentcenter_activity);
        
        initView();
        
        initData();

        initEvent();
    }

    /**
     * view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);
        introduce = findViewById(R.id.introduce);
        sales_good = findViewById(R.id.cuxiao_goog);
        isBag = (TextView) findViewById(R.id.is_bag);
        noBag = (TextView) findViewById(R.id.no_bag);
        back = (RelativeLayout) findViewById(R.id.back);
    }

    /**
     * data
     */
    private void initData() {
        myCommentLeftFrament = new MyCommentLeftFrament();
        myCommentRightFragment = new MyCommentRightFragment();
        Bundle bundle = new Bundle();
        bundle.putString("isComment", "true");
        myCommentLeftFrament.setArguments(bundle);
        myCommentRightFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment, myCommentLeftFrament).commit();
        fragmentManager.beginTransaction().add(R.id.fragment, myCommentRightFragment).commit();
        fragmentManager.beginTransaction().show(myCommentLeftFrament).commit();
        fragmentManager.beginTransaction().hide(myCommentRightFragment).commit();
    }

    /**
     * event
     */
    private void initEvent() {
        isBag.setOnClickListener(this);
        noBag.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.is_bag:
                introduce.setVisibility(View.VISIBLE);
                sales_good.setVisibility(View.INVISIBLE);
                fragmentManager.beginTransaction().hide(myCommentRightFragment).commit();
                fragmentManager.beginTransaction().show(myCommentLeftFrament).commit();
                break;
            case R.id.no_bag:
                introduce.setVisibility(View.INVISIBLE);
                sales_good.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(myCommentLeftFrament).commit();
                fragmentManager.beginTransaction().show(myCommentRightFragment).commit();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
