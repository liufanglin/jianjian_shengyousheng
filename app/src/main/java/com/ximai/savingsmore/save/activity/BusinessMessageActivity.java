package com.ximai.savingsmore.save.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.tools.Constant;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.fragment.BusinessIntroduceFragment;
import com.ximai.savingsmore.save.fragment.SalesGoodsFragment;
import com.ximai.savingsmore.save.modle.Business;
import com.ximai.savingsmore.save.modle.BusinessList;
import com.ximai.savingsmore.save.modle.BusinessMessage;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.view.GlideRoundTransform;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/11/26
 */
//商家信息 - 个人查看商家信息
public class BusinessMessageActivity extends BaseActivity implements View.OnClickListener {
    private TextView business_introduce, business_sales;
    private View introduce, sales_good;
    private FragmentManager fragmentManager;
    private BusinessIntroduceFragment introduceFragment;
    private SalesGoodsFragment salesGoodsFragment;
    private FragmentTransaction fragmentTransaction;
    //private User user;
    private ImageView head_image;
    private TextView store_name;
    private TextView address;
    //private GoodDetial good;
    private ImageView focus;
    private Boolean isFocus = false;
    private BusinessMessage businessMessage;
    private String id;
    private List<Business> listBusiness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_message_activity);

        /**
         * 注册一个监听 - 用来收到订单
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.BUSINESS_MSG);

        /**
         * init
         */
        initView();

        /**
         *data
         */
        initData();

        /**
         *event
         */
        initEvent();
    }

    /**
     *view
     */
    private void initView() {
        setLeftBackMenuVisibility(BusinessMessageActivity.this, "");
        setCenterTitle("商家信息");
        //good = (GoodDetial) intent.getSerializableExtra("good");
        // user = good.User;
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        introduce = findViewById(R.id.introduce);
        sales_good = findViewById(R.id.cuxiao_goog);
        business_introduce = (TextView) findViewById(R.id.business_introduce);
        business_sales = (TextView) findViewById(R.id.business_sales);
        head_image = (ImageView) findViewById(R.id.head_image);
        store_name = (TextView) findViewById(R.id.store_name);
        address = (TextView) findViewById(R.id.address);
        focus = (ImageView) findViewById(R.id.foues);
    }

    /**
     *data
     */
    private void initData() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        /**
         * 获取店铺收藏
         */
        getCollectBusiness();
//        String collection = PrefUtils.getString(this, "collection", "");//这是从
//        if ("1".equals(collection)){
//            focus.setBackgroundResource(R.mipmap.comment_star);
//            isFocus = false;
//        }else{
//            focus.setBackgroundResource(R.mipmap.comment_start_gray);
//            isFocus = true;
//        }

//        String isPeopleAndBusiness = PrefUtils.getString(this, "isPeopleAndBusiness", "");//如果是商家啊就将收藏隐藏
//        if ("3".equals(isPeopleAndBusiness)){
//            focus.setVisibility(View.GONE);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getBusinessMessage(id);
    }

    /**
     *event
     */
    private void initEvent() {
        focus.setOnClickListener(this);
        business_introduce.setOnClickListener(this);
        business_sales.setOnClickListener(this);
    }

    /**
     * 事件处理
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.business_introduce://商家介绍
                introduce.setVisibility(View.VISIBLE);
                sales_good.setVisibility(View.INVISIBLE);
                fragmentManager.beginTransaction().hide(salesGoodsFragment).commit();
                fragmentManager.beginTransaction().show(introduceFragment).commit();
                break;
            case R.id.business_sales://促销商品
                introduce.setVisibility(View.INVISIBLE);
                sales_good.setVisibility(View.VISIBLE);
                fragmentManager.beginTransaction().hide(introduceFragment).commit();
                fragmentManager.beginTransaction().show(salesGoodsFragment).commit();
                break;
            case R.id.foues:
                if (isFocus == true){
                    if (null != businessMessage) {
                        cancelBusiness(businessMessage.Id);
                        isFocus = false;
                    }
                }else{
                    if (null != businessMessage) {
                        collectBusiness(businessMessage.Id);
                        isFocus = true;
                    }
                }
                break;
        }
    }

    /**
     * 进行收藏
     */
    private void collectBusiness(String id) {
        WebRequestHelper.json_post(BusinessMessageActivity.this, URLText.ADD_COLLECT_BUSINESS, RequestParamsPool.focusBusiness(id), new MyAsyncHttpResponseHandler(BusinessMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    focus.setBackgroundResource(R.mipmap.comment_star);
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.optString("Message");
                    Toast.makeText(BusinessMessageActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 取消收藏
     */
    private void cancelBusiness(String id) {
        List<String> list = new ArrayList<>();
        list.add(id);
        WebRequestHelper.json_post(BusinessMessageActivity.this, URLText.CANCEL_COLLECT_BUSINESS, RequestParamsPool.cancelBusiness("", list), new MyAsyncHttpResponseHandler(BusinessMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    focus.setBackgroundResource(R.mipmap.comment_start_gray);
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.optString("Message");
                    Toast.makeText(BusinessMessageActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取商家信息
     */
    private void getBusinessMessage(String id) {
        WebRequestHelper.json_post(BusinessMessageActivity.this, URLText.USER_DETIAL, RequestParamsPool.getBusinessMessage(id), new MyAsyncHttpResponseHandler(BusinessMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject object = new JSONObject(result);

                    String MainData = new String(object.optString("MainData"));
                    businessMessage = GsonUtils.fromJson(MainData, BusinessMessage.class);
                    Constant.storeCount=businessMessage.UserExtInfo.StoreCount;
    //                MyImageLoader.displayDefaultImage(URLText.img_url + businessMessage.PhotoPath, head_image);
                    /*
                     * 加载圆角图片
                     */
                    if (!TextUtils.isEmpty(businessMessage.PhotoPath)){
                        RequestOptions options = new RequestOptions()
                                .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                                .priority(Priority.HIGH)
                                .transform(new GlideRoundTransform(BusinessMessageActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

                        Glide.with(BusinessMessageActivity.this).load(URLText.img_url + businessMessage.PhotoPath).apply(options).into(head_image);
                    }

                    store_name.setText(businessMessage.ShowName);
                    if (null != businessMessage.City && null != businessMessage.Province) {
                        address.setText(businessMessage.Province.Name + " " + businessMessage.City.Name);
                    } else {
                        address.setText("上海" + " " + "浦东新区");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("good", businessMessage);

                introduceFragment = new BusinessIntroduceFragment();
                salesGoodsFragment = new SalesGoodsFragment();
                introduceFragment.setArguments(bundle1);
                salesGoodsFragment.setArguments(bundle1);

                fragmentManager.beginTransaction().add(R.id.fragment, introduceFragment).commit();
                fragmentManager.beginTransaction().add(R.id.fragment, salesGoodsFragment).commit();
                fragmentManager.beginTransaction().show(introduceFragment).commit();
            }
        });
    }

    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName,Object cid) {
            try {
                if (Constants.BUSINESS_MSG.equals(eventName)) {
                    introduce.setVisibility(View.INVISIBLE);
                    sales_good.setVisibility(View.VISIBLE);
                    fragmentManager.beginTransaction().hide(introduceFragment).commit();
                    fragmentManager.beginTransaction().show(salesGoodsFragment).commit();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    /**
     * onDestroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.BUSINESS_MSG);
    }

    /**
     * 得到收藏的店铺
     */
    private void getCollectBusiness() {
        WebRequestHelper.json_post(BusinessMessageActivity.this, URLText.COLLECT_BUSINESS, RequestParamsPool.collectBusiness(), new MyAsyncHttpResponseHandler(BusinessMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    BusinessList businessList = GsonUtils.fromJson(new String(responseBody), BusinessList.class);
                    listBusiness = businessList.MainData;
                    for (int i = 0; i <listBusiness.size(); i++) {
                        if (listBusiness.get(i).UserId.equals(id)){
                            focus.setBackgroundResource(R.mipmap.comment_star);
                            isFocus = true;
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}