package com.ximai.savingsmore.save.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.fragment.LookThroughLeftFrament;
import com.ximai.savingsmore.save.fragment.LookThroughRightFragment;

/**
 * Created by luck on 2018/3/20 0020.
 * 浏览汇总
 */

public class LookThroughActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back;
    private TextView isBag, noBag;
    private View introduce, sales_good;
    private LookThroughLeftFrament lookThroughLeftFrament;
    private LookThroughRightFragment lookThroughRightFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookthrough_activity);

        intiView();

        initData();

        initEvent();
    }

    /**
     * view
     */
    private void intiView() {
        /**
         * 将标题隐藏
         */
        toolbar.setVisibility(View.GONE);
        back = (RelativeLayout) findViewById(R.id.back);

        introduce = findViewById(R.id.introduce);
        sales_good = findViewById(R.id.cuxiao_goog);
        isBag = (TextView) findViewById(R.id.is_bag);
        noBag = (TextView) findViewById(R.id.no_bag);

    }

    /**
     * data
     */
    private void initData() {
        lookThroughLeftFrament = new LookThroughLeftFrament();
        lookThroughRightFragment = new LookThroughRightFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hit", "true");
        lookThroughLeftFrament.setArguments(bundle);
        lookThroughRightFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment, lookThroughLeftFrament).commit();
        fragmentManager.beginTransaction().add(R.id.fragment, lookThroughRightFragment).commit();
        fragmentManager.beginTransaction().show(lookThroughLeftFrament).commit();
        fragmentManager.beginTransaction().hide(lookThroughRightFragment).commit();
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        isBag.setOnClickListener(this);
        noBag.setOnClickListener(this);
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
                fragmentManager.beginTransaction().hide(lookThroughLeftFrament).commit();
                fragmentManager.beginTransaction().show(lookThroughRightFragment).commit();
                break;
            case R.id.no_bag:
                introduce.setVisibility(View.INVISIBLE);
                sales_good.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(lookThroughLeftFrament).commit();
                fragmentManager.beginTransaction().show(lookThroughRightFragment).commit();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}