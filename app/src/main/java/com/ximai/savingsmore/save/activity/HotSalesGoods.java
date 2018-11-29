package com.ximai.savingsmore.save.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.fragment.IsBagGoodFragment;
import com.ximai.savingsmore.save.fragment.NoBagGoodFragment;
import com.ximai.savingsmore.save.fragment.ProduceFragment;
import com.ximai.savingsmore.save.fragment.ServiceFragment;

/**
 * Created by caojian on 16/11/28.
 */
//热门促销
public class  HotSalesGoods extends BaseActivity  implements View.OnClickListener{
    private TextView isBag,noBag;
    private View introduce,sales_good;
    private FragmentManager fragmentManager;
    private IsBagGoodFragment isBagGoodFragment;
    private NoBagGoodFragment noBagGoodFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_sales_goods);
        
        initView();

        initData();

        initEvent();
    }

    /**
     * initview
     */
    private void initView() {
        setLeftBackMenuVisibility(HotSalesGoods.this,"");
        setCenterTitle(getIntent().getStringExtra("title"));
        introduce=findViewById(R.id.introduce);
        sales_good=findViewById(R.id.cuxiao_goog);
        isBag= (TextView) findViewById(R.id.is_bag);
        noBag= (TextView) findViewById(R.id.no_bag);
    }

    /**
     * init-data
     */
    private void initData() {
        isBagGoodFragment=new IsBagGoodFragment();
        noBagGoodFragment=new NoBagGoodFragment();

        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment,isBagGoodFragment).commit();
        fragmentManager.beginTransaction().add(R.id.fragment,noBagGoodFragment).commit();
        fragmentManager.beginTransaction().show(isBagGoodFragment).commit();
        fragmentManager.beginTransaction().hide(noBagGoodFragment).commit();
    }

    /**
     * init-event
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
        switch (v.getId()){
            case R.id.is_bag://产品类商品
                introduce.setVisibility(View.VISIBLE);
                sales_good.setVisibility(View.INVISIBLE);
                fragmentManager.beginTransaction().hide(noBagGoodFragment).commit();
                fragmentManager.beginTransaction().show(isBagGoodFragment).commit();
                break;
            case  R.id.no_bag://服务类商品
                introduce.setVisibility(View.INVISIBLE);
                sales_good.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(isBagGoodFragment).commit();
                fragmentManager.beginTransaction().show(noBagGoodFragment).commit();
                break;
        }
    }
}
