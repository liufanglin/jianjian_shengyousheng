package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.easemob.easeui.EaseConstant;
import com.luck.picture.lib.PictureSelector;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.FileSystem.FileSystem;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.adapter.DataAdapter;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.CartDetail;
import com.ximai.savingsmore.save.modle.ChainStores;
import com.ximai.savingsmore.save.modle.GoodDetial;
import com.ximai.savingsmore.save.modle.GoodSalesType;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.MixedContent;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.ServiceList;
import com.ximai.savingsmore.save.modle.ShareData;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.utils.ShareUtils;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.FullScreenVideoView;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.SelectPopupWindow;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.ximai.savingsmore.save.view.imagepicker.PhotoPreviewActivity;
import com.ximai.savingsmore.save.view.imagepicker.model.PhotoModel;
import com.ximai.savingsmore.save.view.imagepicker.util.CommonUtils;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by caojian on 16/11/22.
 */
// 商品详情页
public class GoodDetailsActivity extends Activity implements View.OnClickListener {
    private String id;
    private ScrollView scrollView;
    private LinearLayout business_message;
    GoodDetial goodDetial;
    private ImageView share, collect, big_imae, message, phone, comment1, comment2, comment3, comment4, comment5;
    private TextView name, price, high_price, dazhe_style, start_time, end_time, comment_number,
            store_name, location, distance, pingpai, danwei, style, reson, bizhong, explain, descript,service, score,busine_name;
    private Button flow_me ,buy,servise;
    private Boolean isFavourite;
    private RelativeLayout comment;
    private RelativeLayout back;
    private LinearLayout comment_score;
    private ImageView send_message;
    private ShareUtils shareUtils = null;
    private TextView sales_count;
    private TextView tv_more;
    private TextView tv_store_count;
    private TextView tv_care,tv_care1;
    private List<GoodSalesType> Serive_list = new ArrayList<GoodSalesType>();
    List<Goods> list = new ArrayList<Goods>();
    private CartDetail cartDetail;
    private SelectPopupWindow menuWindow;

    private KyLoadingBuilder builder;//dialog
    private List<PhotoModel> single_photos = new ArrayList<>();

    private LinearLayout ll_huodongmsg;
    private LinearLayout ll_goodsmsg;
    private FullScreenVideoView videoplayer;
    private ImageView iv_picture;
    List<MixedContent> dataList = new ArrayList<>();//用来存储视频和图片
    private DataAdapter adapter;
    private boolean isPlayingVideo = false;
    private Handler mHandler = new Handler();
    private ImageView iv_play;
    private String path;
    private int bioaji;
    private String isPeopleAndBusiness;

    Runnable scrollRunnable = new Runnable() {
        int i;
        @Override
        public void run() {
            if (0 < dataList.size() ){
                if (!isPlayingVideo) {
                    i++;
                    MixedContent content = dataList.get(i % dataList.size());
                    if (content.getType() == 0) {
                        path = content.getUrl();
                        bioaji = content.getBioaji();//标记索引
                        iv_picture.setVisibility(View.VISIBLE);
                        videoplayer.setVisibility(View.GONE);
                        iv_play.setVisibility(View.GONE);
                        iv_video_yulan.setVisibility(View.GONE);
                        Glide.with(GoodDetailsActivity.this).load(content.getUrl()).into(iv_picture);//默认缺省
                    } else {
                        path = content.getUrl();
                        iv_picture.setVisibility(View.GONE);
                        iv_play.setVisibility(View.VISIBLE);
                        videoplayer.setVisibility(View.VISIBLE);
                        iv_video_yulan.setVisibility(View.VISIBLE);
                        videoplayer.setVideoPath(content.getUrl());

//                        try {      
//                            Bitmap bitmap = retriveVideoFrameFromVideo(content.getUrl());
//                            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
////                            videoplayer.setBackground(drawable);
//                            Glide.with(GoodDetailsActivity.this).load(drawable).into(iv_video_yulan);//默认缺省
//                        } catch (Throwable throwable) {
//                            throwable.printStackTrace();
//                        }

                        isPlayingVideo = false;

//                        videoplayer.start();//直接播放
//                        videoplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                            @Override
//                            public void onCompletion(MediaPlayer mp) {
//                                isPlayingVideo = false;
//                            }
//                        });
//                        videoplayer.requestFocus();
                    }
                }
                mHandler.postDelayed(scrollRunnable, 1000 * 3);
            }
        }
    };
    private ImageView iv_video_yulan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.good_details_activity);
        initView();

        initData();

        initEvent();
    }

    /**
     * init-view
     */
    private void initView() {
        back = (RelativeLayout) findViewById(R.id.back);
        share = (ImageView) findViewById(R.id.share);
        collect = (ImageView) findViewById(R.id.shouchang);
        message = (ImageView) findViewById(R.id.send_message);
        phone = (ImageView) findViewById(R.id.phone);
        name = (TextView) findViewById(R.id.name);
        price = (TextView) findViewById(R.id.price);
        high_price = (TextView) findViewById(R.id.high_price);
        dazhe_style = (TextView) findViewById(R.id.dazhe_style);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        comment_number = (TextView) findViewById(R.id.comment_number);
        store_name = (TextView) findViewById(R.id.store_name);
        location = (TextView) findViewById(R.id.location);
        distance = (TextView) findViewById(R.id.distance);
        pingpai = (TextView) findViewById(R.id.pingpai);
        danwei = (TextView) findViewById(R.id.danwei);
        bizhong = (TextView) findViewById(R.id.bizhong);
        style = (TextView) findViewById(R.id.style);
        reson = (TextView) findViewById(R.id.resonse);
        scrollView = (ScrollView) findViewById(R.id.scrollview);
        explain = (TextView) findViewById(R.id.explain);
        descript = (TextView) findViewById(R.id.describe);
        flow_me = (Button) findViewById(R.id.flow_me);
        service = (TextView) findViewById(R.id.servise);
        score = (TextView) findViewById(R.id.score);
        comment = (RelativeLayout) findViewById(R.id.comment);
        comment_score = (LinearLayout) findViewById(R.id.comment_score);
        send_message = (ImageView) findViewById(R.id.send_message);
        servise = (Button) findViewById(R.id.servise);
        sales_count = (TextView) findViewById(R.id.sales_number);
        buy = (Button) findViewById(R.id.buy);
        business_message = (LinearLayout) findViewById(R.id.business_message);
        busine_name = (TextView) findViewById(R.id.busine_name);
        tv_more = (TextView) findViewById(R.id.tv_more);
        ll_huodongmsg = (LinearLayout) findViewById(R.id.ll_huodongmsg);//促销活动说明
        ll_goodsmsg = (LinearLayout) findViewById(R.id.ll_goodsmsg);//促销商品描述

        videoplayer = (FullScreenVideoView) findViewById(R.id.videoplayer);
        iv_picture = (ImageView) findViewById(R.id.iv_picture);
        iv_play = (ImageView) findViewById(R.id.iv_play);
        iv_video_yulan = (ImageView) findViewById(R.id.iv_video_yulan);//video预览
        tv_store_count= (TextView) findViewById(R.id.tv_store_count);
        tv_care= (TextView) findViewById(R.id.tv_care);
        tv_care1= (TextView) findViewById(R.id.tv_care1);
    }

    /**
     * init-data
     */
    private void initData() {
        adapter = new DataAdapter(this);

        //3是商家 - 2是个人
        isPeopleAndBusiness = PrefUtils.getString(this, "isPeopleAndBusiness", "");
        if ("3".equals(isPeopleAndBusiness)){
            buy.setVisibility(View.GONE);//购买隐藏
            share.setVisibility(View.VISIBLE);//分享隐藏
            collect.setVisibility(View.GONE);//收藏隐藏
        }
        high_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        scrollView.setVerticalScrollBarEnabled(false);
        id = getIntent().getStringExtra("id");
        if (null != getIntent().getStringExtra("isCar") && getIntent().getStringExtra("isCar").equals("true")) {
            buy.setText("加入购物车");
        }
        /**
         * 根据id获取商品详情数据
         */
    }

    /**
     * init-event
     */
    private void initEvent() {
        phone.setOnClickListener(this);
        buy.setOnClickListener(this);
        share.setOnClickListener(this);
        service.setOnClickListener(this);
        send_message.setOnClickListener(this);
        comment_score.setOnClickListener(this);
        comment.setOnClickListener(this);
        business_message.setOnClickListener(this);
        flow_me.setOnClickListener(this);
        collect.setOnClickListener(this);
        back.setOnClickListener(this);

        iv_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.iv_picture){
                    if (0 < single_photos.size()){
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", bioaji);
                        bundle.putBoolean("isSave",false);
                        CommonUtils.launchActivity(GoodDetailsActivity.this, PhotoPreviewActivity.class, bundle);
                    }
                }
            }
        });

        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != path){
                    PictureSelector.create(GoodDetailsActivity.this).externalPictureVideo(path);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getgood_detial(id,BaseApplication.getInstance().Longitude+"",BaseApplication.getInstance().Latitude+"");

        mHandler.removeCallbacks(scrollRunnable);
        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(scrollRunnable, 10);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler.removeCallbacks(scrollRunnable);
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoplayer.pause();
        mHandler.removeCallbacks(scrollRunnable);
        mHandler.removeCallbacksAndMessages(null);
        Log.e("tag","监听到此页面的退出操作");
        finish();
    }

    /**
     * 再按一次退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
           onDestroy();
            return true;
        }
        return super.onKeyDown(keyCode , event);
    }

    /**
     * 根据id获取商品详情数据
     */
    private void getgood_detial(String Id,String Longitude,String Latitude) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.GET_GOOD_DETIAL, RequestParamsPool.getGoodDetial(Id,Longitude,Latitude), new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    Boolean isSuccess = object.optBoolean("IsSuccess");

                    String MainData = object.optString("MainData");
                    goodDetial = GsonUtils.fromJson(MainData, GoodDetial.class);
                    if (null != goodDetial) {
                        if (null != goodDetial.Name) {
                            name.setText(goodDetial.Name);
                        }
                        if (TextUtils.isEmpty(goodDetial.SaleCount)||"0".equals(goodDetial.SaleCount)){
                            sales_count.setVisibility(View.GONE);
                        }else {
                            sales_count.setText("销 " + goodDetial.SaleCount);
                            sales_count.setVisibility(View.VISIBLE);
                        }
                        price.setText(goodDetial.Currency.Name+" "+ UIUtils.formatPrice(Double.parseDouble(goodDetial.Price)));
                        high_price.setText(goodDetial.Currency.Name+" "+ UIUtils.formatPrice(Double.parseDouble(goodDetial.OriginalPrice)));

                        dazhe_style.setText(goodDetial.Preferential);
                        if (null == goodDetial.CommentCount ){
                            comment_number.setText("0" + "评论");//评论
                        }else{
                            comment_number.setText(goodDetial.CommentCount + "评论");//评论
                        }
                        location.setText(goodDetial.Country.Name+" · "+goodDetial.Province.Name+goodDetial.City.Name+goodDetial.Address);//促销地点
                        busine_name.setText(goodDetial.Name);//促销品名
                        pingpai.setText(goodDetial.Brand);//商品品牌
                        danwei.setText(goodDetial.Unit.Name);
                        bizhong.setText(goodDetial.Currency.Name);
                        style.setText(goodDetial.PromotionTypeName);
                        if (null != goodDetial.PromotionCause) {
                            reson.setText(goodDetial.PromotionCause.Name);
                        }
                        if (null == goodDetial.Introduction || "".equals(goodDetial.Introduction)){
                            ll_huodongmsg.setVisibility(View.GONE);
                        }else{
                            ll_huodongmsg.setVisibility(View.VISIBLE);
                            explain.setText(goodDetial.Introduction);//促销活动说明
                        }
                        if (null == goodDetial.Description|| "".equals(goodDetial.Description)){
                            ll_goodsmsg.setVisibility(View.GONE);
                        }else{
                            ll_goodsmsg.setVisibility(View.VISIBLE);
                            descript.setText(goodDetial.Description);//促销商品描述
                        }
                        if (null != goodDetial.StartTimeName && !TextUtils.isEmpty(goodDetial.StartTimeName)){
                            start_time.setText(goodDetial.StartTimeName.substring(0,11));
                        }
                        if (null != goodDetial.EndTimeName && !TextUtils.isEmpty(goodDetial.EndTimeName)){
                            end_time.setText(goodDetial.EndTimeName.substring(0,11));
                        }

//                        store_name.setText(goodDetial.User.ShowName);//商家名称

                        if (null != goodDetial.ChainStores){//---------------------------------------------------------进行分店的实现
                            if (goodDetial.ChainStores.size() > 0){
                                String name = goodDetial.ChainStores.get(0).Name;
                                store_name.setText(name);
                            }else{
                                if (null != goodDetial.User.UserExtInfo.StoreName) {//商家店铺名称
                                    store_name.setText(goodDetial.User.UserExtInfo.StoreName);
                                }
                            }
                        }else{
                            if (null != goodDetial.User.UserExtInfo.StoreName) {//商家店铺名称
                                store_name.setText( goodDetial.User.UserExtInfo.StoreName);//商家名称
                            }
                        }

                        if (goodDetial.User!=null&&goodDetial.User.UserExtInfo!=null) {
                            tv_store_count.setText("到店人次" + goodDetial.User.UserExtInfo.StoreCount);
                        }
                            tv_care.setText("关注"+goodDetial.CareCount);
                            tv_care1.setText("关注"+goodDetial.CareCount);

                        double v = Double.parseDouble(goodDetial.Distance)  / 1000;
                        distance.setText(v + "km");//显示距离
                        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(50, 50);
                        layout.setMargins(5, 0, 5, 0);
                        if (goodDetial.CommentCount.equals("0")) {
                            score.setText(" 0 分");
                            for (int i = 0; i < 5; i++) {
                                ImageView imageView = new ImageView(GoodDetailsActivity.this);
                                imageView.setLayoutParams(layout);
                                imageView.setBackgroundResource(R.mipmap.comment_start_gray);
                                comment_score.addView(imageView);
                            }
                        } else {
                            score.setText(goodDetial.Score + "分");
                            if (goodDetial.Score.length() > 1) {
                                int score1 = Integer.parseInt(goodDetial.Score.substring(0, 1));
                                for (int i = 0; i < score1; i++) {
                                    ImageView imageView = new ImageView(GoodDetailsActivity.this);
                                    imageView.setLayoutParams(layout);
                                    imageView.setBackgroundResource(R.mipmap.comment_star);
                                    comment_score.addView(imageView);
                                }
                                ImageView imageView = new ImageView(GoodDetailsActivity.this);
                                imageView.setLayoutParams(layout);
                                imageView.setBackgroundResource(R.mipmap.start_half);
                                comment_score.addView(imageView);
                                for (int i = 0; i < 5 - score1 - 1; i++) {
                                    ImageView imageView1 = new ImageView(GoodDetailsActivity.this);
                                    imageView1.setLayoutParams(layout);
                                    imageView1.setBackgroundResource(R.mipmap.comment_start_gray);
                                    comment_score.addView(imageView1);
                                }
                            } else {
                                int score1 = Integer.parseInt(goodDetial.Score);
                                for (int i = 0; i < score1; i++) {
                                    ImageView imageView = new ImageView(GoodDetailsActivity.this);
                                    imageView.setLayoutParams(layout);
                                    imageView.setBackgroundResource(R.mipmap.comment_star);
                                    comment_score.addView(imageView);
                                }
                                for (int i = 0; i < 5 - score1; i++) {
                                    ImageView imageView = new ImageView(GoodDetailsActivity.this);
                                    imageView.setLayoutParams(layout);
                                    imageView.setBackgroundResource(R.mipmap.comment_start_gray);
                                    comment_score.addView(imageView);
                                }
                            }
                        }
                        if (goodDetial.IsFavourite) {
                            isFavourite = true;
                            collect.setBackgroundResource(R.mipmap.comment_star);
                        } else {
                            isFavourite = false;
                            collect.setBackgroundResource(R.mipmap.shouchang_white);
                        }

                        dataList.clear();
                        single_photos.clear();
                        if (null != goodDetial.VideoPath ){//视频
                            MixedContent content = new MixedContent(URLText.img_url+goodDetial.VideoPath, 1);
                            dataList.add(content);
                        }
                        if (null != goodDetial.Images){//图片
                            List<Images> images = goodDetial.Images;
                            for (int i = 0; i < images.size(); i++) {
                                PhotoModel photoModel = new PhotoModel();
                                photoModel.setOriginalPath(images.get(i).ImagePath);
                                single_photos.add(photoModel);

                                MixedContent content1 = new MixedContent(URLText.img_url+images.get(i).ImagePath, 0,i);
                                dataList.add(content1);
                            }
                        }
                        adapter.setData(dataList);
                        adapter.notifyDataSetChanged();//适配器刷新
                        mHandler.postDelayed(scrollRunnable, 10);

//                        reloadMainView(goodDetial.Images);
                    }
                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (null != builder){
                        builder.dismiss();
                    }
                }finally {
                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }


    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy://在线下单
                //这里将地址保存 - 在下单购买页的时候下面展示商品需要商品地址一样的才可以展示
                PreferencesUtils.putString(GoodDetailsActivity.this,"buyGoosShow",location.getText().toString());
                getMyCar1("true");
                break;
            case R.id.share://分享
                //这里是对商品的进行一个分享
                PreferencesUtils.putString(GoodDetailsActivity.this,"isGoodsShark","1");
                ShareData data = new ShareData();
                data.setTitleUrl("http://www.savingsmore.com/Product/SharedProductDetail/"+id);
                data.setUrl("http://www.savingsmore.com/Product/SharedProductDetail/"+id);
                data.setTitle(goodDetial.Name+"-"+goodDetial.Preferential+"！");
//                data.setImagePath(FileSystem.getCachesDir(GoodDetailsActivity.this, true).getAbsolutePath() + File.separator + "icon.jpg");
//                if (null == MyUserInfoUtils.getInstance().myUserInfo.ShowName){
//                    data.setText("您的朋友分享了一个促销商品，快去看！促销结束就无效了！");
//                }else{
//                    data.setText("您的朋友"+ MyUserInfoUtils.getInstance().myUserInfo.ShowName +"分享了一个促销商品，快去看！促销结束就无效了！");
//                }
                data.setImageUrl(URLText.img_url+goodDetial.Image);
                if ("2".equals(isPeopleAndBusiness)){
                    String name=MyUserInfoUtils.getInstance().myUserInfo.ShowName;
                    data.setText("您的朋友"+name+"与您分享！点击看！ 商品在促销，限时有折扣！");

                }else {
                    //商家
                    data.setText(store_name.getText().toString()+"开始促销了！货比三家,该商品最优惠！欢迎您来店！");
                }

                shareUtils = new ShareUtils(data, GoodDetailsActivity.this,id);
                shareUtils.show(share);
                break;
            case R.id.back:
                this.finish();
                break;
            case R.id.business_message://商家信息
                Intent intent = new Intent(GoodDetailsActivity.this, BusinessMessageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("id", goodDetial.User.Id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.flow_me://带我去门店
                recodeShop(goodDetial.User.Id);
                if ("0".equals(goodDetial.User.UserExtInfo.RebatePercent) || null == goodDetial.User.UserExtInfo.RebatePercent){
                    goToMenDians();
                }else{
                    goToMenDian();
                }
                break;
            case R.id.shouchang://收藏
                if (null != isFavourite && isFavourite) {
                    cancelCollect(goodDetial.Id);
                    isFavourite = false;
                } else if (null != isFavourite && isFavourite == false) {
                    addCollect(goodDetial.Id);
                    isFavourite = true;
                }
                break;
            case R.id.phone://打电话
                List<ChainStores> chainStores = goodDetial.ChainStores;
                if (null != chainStores && chainStores.size() > 0){
                    String phone = goodDetial.ChainStores.get(0).ContactWay;
                    if (!TextUtils.isEmpty(phone)){
                        call(phone);
                    }
                }else {
                    if (!TextUtils.isEmpty(goodDetial.User.PhoneNumber)){
                        call(goodDetial.User.PhoneNumber);
                    }
                }
                break;
            case R.id.comment:
                Intent comment = new Intent(GoodDetailsActivity.this, GoodsCommentActiviyt.class);
                comment.putExtra("id", goodDetial.Id);
                comment.putExtra("score", goodDetial.Score);
                comment.putExtra("isComment", "false");
                startActivity(comment);
                break;
            case R.id.send_message://去聊天
                //首先判断自己是否有聊天的Id
                if (null == MyUserInfoUtils.getInstance().myUserInfo.IMUserName || null == MyUserInfoUtils.getInstance().myUserInfo.IMPassword) {
                    Toast.makeText(this, "对方暂不支持聊天", Toast.LENGTH_SHORT).show();
                }else {
                    String showName = goodDetial.User.ShowName;//商品详情的商家数据
                    if (showName.equals(MyUserInfoUtils.getInstance().myUserInfo.ShowName)){//不能和自己聊天
                        chatDialog();
                    }else{//不是自己那就看别人是否可以聊天
                        if (null == goodDetial.User.IMUserName){
                            Toast.makeText(this, "对方暂不支持聊天", Toast.LENGTH_SHORT).show();
                        }else{
                            Intent send = new Intent(GoodDetailsActivity.this, ChatActivity.class);
                            send.putExtra(EaseConstant.EXTRA_USER_ID, goodDetial.User.IMUserName);
                            startActivity(send);
                        }
                    }
                }
                break;
            case R.id.servise://客服的点击
                showSetIconWindow();
                break;
                default:
                    break;
        }
    }

    /**
     * 带我去门店的弹框提醒大于0
     */
    public void goToMenDian(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                Intent intent1 = new Intent(GoodDetailsActivity.this, TakeMeActivity.class);
                intent1.putExtra("isgood", "true");
                intent1.putExtra("good", goodDetial.User);
                intent1.putExtra("good1", goodDetial);
                startActivity(intent1);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };


        if (null != goodDetial.User.UserExtInfo.RebatePercent && 0 < Double.parseDouble(goodDetial.User.UserExtInfo.RebatePercent) * 100){
            if (null != goodDetial.ChainStores){//---------------------------------------------------------进行分店的实现
                if (goodDetial.ChainStores.size() > 0){
//                    double v = Double.parseDouble(goodDetial.User.UserExtInfo.RebatePercent) * 100;
//                    int a = (int) v;

                    double v1 = Double.parseDouble(goodDetial.User.UserExtInfo.RebatePercent);
                    float v = (float) (v1 * 100);

                    Dialog dialog = new XiMaiPopDialog(this, "温馨提示", goodDetial.ChainStores.get(0).Name+"门店消费全场再优惠" + v +"%！", "确定", R.style.CustomDialog_1, callBack, 2);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }else{
                    if (null != goodDetial.User.UserExtInfo.StoreName) {//商家店铺名称
                        double v1 = Double.parseDouble(goodDetial.User.UserExtInfo.RebatePercent);
                        float v = (float) (v1 * 100);
                        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", goodDetial.User.UserExtInfo.StoreName+"门店消费全场再优惠" + v +"%！", "确定", R.style.CustomDialog_1, callBack, 2);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                }
            }else{
                if (null != goodDetial.User.UserExtInfo.StoreName) {//商家店铺名称
                    double v1 = Double.parseDouble(goodDetial.User.UserExtInfo.RebatePercent);
                    float v = (float) (v1 * 100);
                    Dialog dialog = new XiMaiPopDialog(this, "温馨提示", goodDetial.User.UserExtInfo.StoreName+"门店消费全场再优惠" + v +"%！", "确定", R.style.CustomDialog_1, callBack, 2);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        }
    }


    /**
     * 带我去门店的弹框提醒 - 小于0
     */
    public void goToMenDians(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                Intent intent1 = new Intent(GoodDetailsActivity.this, TakeMeActivity.class);
                intent1.putExtra("isgood", "true");
                intent1.putExtra("good", goodDetial.User);
                intent1.putExtra("good1", goodDetial);
                startActivity(intent1);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };

        if (null != goodDetial.ChainStores){//---------------------------------------------------------进行分店的实现
            if (goodDetial.ChainStores.size() > 0){
                Dialog dialog = new XiMaiPopDialog(this, "温馨提示", goodDetial.ChainStores.get(0).Name+"更多促销优惠在等您，欢迎来门店询问。", "确定", R.style.CustomDialog_1, callBack, 2);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }else{
                if (null != goodDetial.User.UserExtInfo.StoreName) {//商家店铺名称
                    Dialog dialog = new XiMaiPopDialog(this, "温馨提示", goodDetial.User.UserExtInfo.StoreName+"更多促销优惠在等您，欢迎来门店询问。", "确定", R.style.CustomDialog_1, callBack, 2);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                }
            }
        }else{
            if (null != goodDetial.User.UserExtInfo.StoreName) {//商家店铺名称
                Dialog dialog = new XiMaiPopDialog(this, "温馨提示", goodDetial.User.UserExtInfo.StoreName+"更多促销优惠在等您，欢迎来门店询问。", "确定", R.style.CustomDialog_1, callBack, 2);
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            }
        }
    }

    /**
     * 不能喝自己聊天
     */
    public void chatDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(GoodDetailsActivity.this, "温馨提示", "您不能跟自己对话", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /**
     * 打电话
     * @param phoneNumber
     */
    public void initCallPhone(final String phoneNumber) {
        AndPermission.with(this)
                .requestCode(300)
                .permission(Manifest.permission.CALL_PHONE)
                .rationale(new RationaleListener() {

                    @Override
                    public void showRequestPermissionRationale(int i, final Rationale rationale) {
                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
//                        AndPermission.rationaleDialog(mActivity, rationale).show();
                        AlertDialog.newBuilder(GoodDetailsActivity.this)
                                .setTitle("友好提醒")
                                .setMessage("你已拒绝过定位权限，沒有定位定位权限无法为你推荐附近的商品，你看着办！")
                                .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rationale.resume();
                                    }
                                })
                                .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rationale.cancel();
                                    }
                                }).show();
                    }
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if (requestCode == 300) { // Successfully.
                            intentToCall(phoneNumber);
                        }
                    }
                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(GoodDetailsActivity.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(GoodDetailsActivity.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        if (requestCode == 200) {// Failure.
                            Toast.makeText(GoodDetailsActivity.this, "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .start();
    }
    /**
     * 打电话
     * @param phoneNumber
     */
    private void intentToCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG","进来了");
                return;
            }
            startActivity(intent);
        }catch (Exception e){
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }

    /**
     * 点击客服 - 选择计时交流 - 留言
     */
    private void showSetIconWindow() {
        menuWindow = new SelectPopupWindow(this, itemsOnClick);
        menuWindow.showAtLocation(servise, "及时交流", "留言");
    }

    /**
     * 弹出窗口实现监听类
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_one://及时交流
//                    getServiceList();
                    nowPlayPhone();
                    menuWindow.dismiss();
                    break;
                case R.id.btn_two://留言
                    Intent leave = new Intent(GoodDetailsActivity.this, LeaveMessageActivity.class);
                    leave.putExtra("Id", goodDetial.User.Id);
                    startActivity(leave);
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 状态及时交流
     */
    public void nowPlayPhone(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
//                initCallPhone("02158366991");
                call("02158366991");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "是否确认拨打021-58366991？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 收藏商品
     * @param id
     */
    private void addCollect(String id) {
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.ADD_COLLECT, RequestParamsPool.addColect(id), new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    collect.setBackgroundResource(R.mipmap.comment_star);
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.optString("Message");
                    Toast.makeText(GoodDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 到店人数
    * */

    private void recodeShop(String id) {
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.RECODE_SHOP, RequestParamsPool.addColect(id), new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);

            }
        });
    }


    /**
     * 取消收藏商品
     * @param id
     */
    public void cancelCollect(String id) {
        List<String> list = new ArrayList<String>();
        list.add(id);
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.CANCEL_COLLECT, RequestParamsPool.cancelColect(null, list), new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    collect.setBackgroundResource(R.mipmap.shouchang_white);
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.optString("Message");
                    Toast.makeText(GoodDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                    NotificationCenter.defaultCenter().postNotification(Constants.LOADING_COLLECTION,"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 加入购物车
     * @param ProductId
     * @param Quantity
     * @param CartOperaType
     */
    private void addCat(String ProductId, String Quantity, String CartOperaType) {
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.UPDATE_CAR, RequestParamsPool.update_car(ProductId, Quantity, CartOperaType), new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String success = object.optString("IsSuccess");
                    String message = object.optString("Message");

                    if ("促销活动还未开始，请等候！".equals(message)){
                        Toast.makeText(GoodDetailsActivity.this, "促销未开始，静候！", Toast.LENGTH_SHORT).show();
                    }
                    if (success.equals("true")) {//-----------------------------------------------购买操作
                        Intent intent2 = new Intent(GoodDetailsActivity.this, OrderBuyCeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("good", goodDetial);
                        intent2.putExtras(bundle);
                        startActivity(intent2);
                        BaseApplication.getInstance().GoodDetial = goodDetial;//支付成功之后需要的再来一单
                        Log.d("tag",BaseApplication.getInstance().GoodDetial.toString() );
                        NotificationCenter.defaultCenter().postNotification(Constants.LOADING_ORDERFRAGMENT,"");
//                        finish();
                    } else if (message.equals("购物车中存在其他商铺的商品，请先结算购物车中的商品！")) {
//                        Intent intent2 = new Intent(GoodDetailsActivity.this, WXPayEntryActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("good", goodDetial);
//                        intent2.putExtras(bundle);
//                        startActivity(intent2);
//                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 得到客服列表
     */
    private void getServiceList() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.SERVICE_LIST, stringEntity, new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                ServiceList serviceList = GsonUtils.fromJson(string, ServiceList.class);
                Serive_list = serviceList.MainData;
                if (Serive_list.size() > 0 && null != Serive_list) {
                    Intent send = new Intent(GoodDetailsActivity.this, ChatActivity.class);
                    if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("3")) {
                        LogUtils.instance.d("商家与后台聊天");
                        send.putExtra(EaseConstant.EXTRA_USER_ID, Serive_list.get(0).Id);
                        startActivity(send);
                    } else {
                        LogUtils.instance.d("个人与后台聊天");
                        send.putExtra(EaseConstant.EXTRA_USER_ID, Serive_list.get(1).Id);
                        startActivity(send);
                    }
                }
            }
        });
    }

    private void getMyCar1(String isBag) {
        // list.clear();
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.GET_MYCAR, RequestParamsPool.get_car(isBag), new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    cartDetail = GsonUtils.fromJson(object.optString("MainData"), CartDetail.class);
                    if (null != cartDetail) {
                        if (null != cartDetail.CartProduct && cartDetail.CartProduct.size() > 0) {
                            if (!goodDetial.User.ShowName.equals(cartDetail.CartProduct.get(0).StoreName)) {
                                clearCar();
                            } else {
                                getMyCar2("false");
                            }
                        } else {
                            getMyCar2("false");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getMyCar2(String isBag) {
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.GET_MYCAR, RequestParamsPool.get_car(isBag), new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
                    @Override
                    public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject object = new JSONObject(new String(responseBody));
                            cartDetail = GsonUtils.fromJson(object.optString("MainData"), CartDetail.class);
                            if (null != cartDetail) {
                                if (null != cartDetail.CartProduct && cartDetail.CartProduct.size() > 0) {
                                    if (!goodDetial.User.ShowName.equals(cartDetail.CartProduct.get(0).StoreName)) {
                                        clearCar();
                                    } else {
                                        if (goodDetial.Price.equals("0")) {
                                            Toast.makeText(GoodDetailsActivity.this, "无需下单购买,请与商家联系取货", Toast.LENGTH_SHORT).show();
                                        } else {
                                            addCat(id, "1", "1");
                                        }
                                    }
                                } else {
                                    if (goodDetial.Price.equals("0")) {
                                        Toast.makeText(GoodDetailsActivity.this, "无需下单购买,请与商家联系取货", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addCat(id, "1", "1");
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 清楚购物车
     */
    private void clearCar() {
        WebRequestHelper.json_post(GoodDetailsActivity.this, URLText.CLEAR_CAR, RequestParamsPool.clearCar(), new MyAsyncHttpResponseHandler(GoodDetailsActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                // LogUtils.instance.d("清空购物车=" + result);
                if (null != goodDetial) {
                    if (goodDetial.Price.equals("0")) {
                        Toast.makeText(GoodDetailsActivity.this, "无需下单购买,请于商家联系取货", Toast.LENGTH_SHORT).show();
                    } else {
                        addCat(id, "1", "1");
                    }
                }
                try {
                    JSONObject object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 视频缩略图
     * @param videoPath
     * @return
     * @throws Throwable
     */
    public  Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }


    /**
     * 打开loading
     */
    public void showLoading(Context context, String text){
        if (null == builder){
            builder = new KyLoadingBuilder(context);
        }
        builder.setIcon(R.mipmap.loading);
        builder.setText(text);
        builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        builder.show();
    }
}