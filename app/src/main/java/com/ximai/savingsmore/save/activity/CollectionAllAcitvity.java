package com.ximai.savingsmore.save.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.fragment.CollectionDataLeftFrament;
import com.ximai.savingsmore.save.fragment.CollectionDataRightFragment;

/**
 * Created by caojian on 17/1/9.fff
 */
//收藏汇总
public class CollectionAllAcitvity extends BaseActivity implements View.OnClickListener {
    private TextView isBag, noBag;
    private View introduce, sales_good;
    private FragmentManager fragmentManager;
    private CollectionDataLeftFrament collectionDataLeftFrament;
    private CollectionDataRightFragment collectionDataRightFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collecttion_activity);
        setLeftBackMenuVisibility(CollectionAllAcitvity.this, "");
        setCenterTitle("收藏汇总");
        
        initView();
        
        initData();

        initEvent();
    }

    /**
     * view
     */
    private void initView() {
        introduce = findViewById(R.id.introduce);
        sales_good = findViewById(R.id.cuxiao_goog);
        isBag = (TextView) findViewById(R.id.is_bag);
        noBag = (TextView) findViewById(R.id.no_bag);
    }

    /**
     * data
     */
    private void initData() {
        collectionDataLeftFrament = new CollectionDataLeftFrament();
        collectionDataRightFragment = new CollectionDataRightFragment();
        Bundle bundle = new Bundle();
        bundle.putString("collect", "true");
        collectionDataLeftFrament.setArguments(bundle);
        collectionDataRightFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment, collectionDataLeftFrament).commit();
        fragmentManager.beginTransaction().add(R.id.fragment, collectionDataRightFragment).commit();
        fragmentManager.beginTransaction().show(collectionDataLeftFrament).commit();
        fragmentManager.beginTransaction().hide(collectionDataRightFragment).commit();
    }

    /**
     * event
     */
    public void initEvent(){
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
            case R.id.is_bag://促销中
                introduce.setVisibility(View.VISIBLE);
                sales_good.setVisibility(View.INVISIBLE);
                fragmentManager.beginTransaction().hide(collectionDataRightFragment).commit();
                fragmentManager.beginTransaction().show(collectionDataLeftFrament).commit();
                break;
            case R.id.no_bag://促销结束
                introduce.setVisibility(View.INVISIBLE);
                sales_good.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(collectionDataLeftFrament).commit();
                fragmentManager.beginTransaction().show(collectionDataRightFragment).commit();
                break;
        }
    }
}