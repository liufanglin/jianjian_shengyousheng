package com.ximai.savingsmore.save.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.save.adapter.BannerHeaderAdapter;
import com.ximai.savingsmore.save.adapter.ContactAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.UserEntity;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import me.yokeyword.indexablerv.IndexableAdapter;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * 区号选择
 */
public class ZoneNumberActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout mBack;
    private ContactAdapter mContactAdapter;
    private IndexableLayout indexableLayout;
    private BannerHeaderAdapter mBannerHeaderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_zone_number);

        initView();

        initAdapter();

        initEvent();
    }

    /**
     * init - view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);

        mBack = (RelativeLayout) findViewById(R.id.back);
        indexableLayout = (IndexableLayout) findViewById(R.id.indexableLayout);
        indexableLayout.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * init - event
     */
    private void initEvent() {
        mBack.setOnClickListener(this);

        /**
         * 所有城市内容点击事件
         */
        mContactAdapter.setOnItemContentClickListener(new IndexableAdapter.OnItemContentClickListener<UserEntity>() {
            @Override
            public void onItemClick(View v, int originalPosition, int currentPosition, UserEntity entity) {
                if (originalPosition >= 0) {
                    Intent intent = new Intent();
                    intent.putExtra("cityCode",entity.getMobile());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        /**
         * 头部iten点击事件
         */
        mBannerHeaderAdapter.setViewClickListener(new BannerHeaderAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, List<UserEntity> list) {
                Intent intent = new Intent();
                intent.putExtra("cityCode",list.get(postion).getMobile());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    public void initAdapter(){
        mContactAdapter = new ContactAdapter(this);
        indexableLayout.setAdapter(mContactAdapter);
        indexableLayout.setOverlayStyle_Center();
        mContactAdapter.setDatas(initDatas());
        indexableLayout.setCompareMode(IndexableLayout.MODE_FAST);

        List<String> bannerList = new ArrayList<>();
        bannerList.add("");
        mBannerHeaderAdapter = new BannerHeaderAdapter("↑", null, bannerList,this);
        indexableLayout.addHeaderAdapter(mBannerHeaderAdapter);
    }

    private List<UserEntity> initDatas() {
        List<UserEntity> list = new ArrayList<>();
        // 初始化数据
        List<String> contactStrings = Arrays.asList(getResources().getStringArray(R.array.provinces));
        List<String> mobileStrings = Arrays.asList(getResources().getStringArray(R.array.code));
        for (int i = 0; i < contactStrings.size(); i++) {
            UserEntity contactEntity = new UserEntity(contactStrings.get(i), mobileStrings.get(i));
            list.add(contactEntity);
        }
        return list;
    }
}