package com.ximai.savingsmore.save.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.fragment.SharkDataLeftFrament;
import com.ximai.savingsmore.save.fragment.SharkDataRightFragment;

/**
 * Created by caojian on 17/1/9.
 */
//分享汇总
public class BrowseActivity extends BaseActivity implements View.OnClickListener {
    private TextView isBag, noBag;
    private View introduce, sales_good;
    private FragmentManager fragmentManager;
    private SharkDataLeftFrament sharkDataLeftFrament;
    private SharkDataRightFragment sharkDataRightFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.browse_activity);

        intiView();

        initData();
        
        initEvent();
    }

    /**
     * view
     */
    private void intiView() {
        setCenterTitle("分享汇总");
        setLeftBackMenuVisibility(BrowseActivity.this, "");
        introduce = findViewById(R.id.introduce);
        sales_good = findViewById(R.id.cuxiao_goog);
        isBag = (TextView) findViewById(R.id.is_bag);
        noBag = (TextView) findViewById(R.id.no_bag);
    }

    /**
     * data
     */
    private void initData() {
        sharkDataLeftFrament = new SharkDataLeftFrament();
        sharkDataRightFragment = new SharkDataRightFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hit", "true");
        sharkDataLeftFrament.setArguments(bundle);
        sharkDataRightFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment, sharkDataLeftFrament).commit();
        fragmentManager.beginTransaction().add(R.id.fragment, sharkDataRightFragment).commit();
        fragmentManager.beginTransaction().show(sharkDataLeftFrament).commit();
        fragmentManager.beginTransaction().hide(sharkDataRightFragment).commit();
    }

    /**
     * event
     */
    private void initEvent() {
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
                fragmentManager.beginTransaction().hide(sharkDataRightFragment).commit();
                fragmentManager.beginTransaction().show(sharkDataLeftFrament).commit();
                break;
            case R.id.no_bag:
                introduce.setVisibility(View.INVISIBLE);
                sales_good.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(sharkDataLeftFrament).commit();
                fragmentManager.beginTransaction().show(sharkDataRightFragment).commit();
                break;
        }
    }
}