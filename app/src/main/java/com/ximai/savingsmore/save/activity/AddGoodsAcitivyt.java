package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.DateUtils;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.PopupWindowFromBottomUtil;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.library.toolbox.UsePicker;
import com.ximai.savingsmore.save.adapter.GridImageAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.ChainStores;
import com.ximai.savingsmore.save.modle.GoodDetial;
import com.ximai.savingsmore.save.modle.GoodSalesType;
import com.ximai.savingsmore.save.modle.GoodSalesTypeList;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.ListBaseMessage;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyProduct;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.UpPhoto;
import com.ximai.savingsmore.save.modle.UploadGoodsBean;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.view.FullyGridLayoutManager;
import com.ximai.savingsmore.save.view.GlideRoundTransform;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.MyGridView;
import com.ximai.savingsmore.save.view.XiMaiPopDialog1;
import com.ximai.savingsmore.save.view.XiMaiPopDialog2;
import com.ximai.savingsmore.save.view.imagepicker.PhotoPreviewActivity;
import com.ximai.savingsmore.save.view.imagepicker.PhotoSelectorActivity;
import com.ximai.savingsmore.save.view.imagepicker.model.PhotoModel;
import com.ximai.savingsmore.save.view.imagepicker.util.CommonUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.Config;
import com.ximai.savingsmore.save.view.imagepicker.util.DbTOPxUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.FileUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by caojian on 16/12/10.
 * 添加促销
 */
public class AddGoodsAcitivyt extends BaseActivity implements View.OnClickListener {
    private ImageView head_view;
    private TextView my_name;
    private List<BaseMessage> list = new ArrayList<BaseMessage>();
    private List<BaseMessage> good_one_classify = new ArrayList<>();
    private List<BaseMessage> brand_list = new ArrayList<>();
    //活动形式
    private List<GoodSalesType> goodSalesTypes = new ArrayList<>();
    private List<BaseMessage> danwei_list = new ArrayList<>();
    private List<BaseMessage> bizhong_list = new ArrayList<>();
    private List<BaseMessage> fapiao_list = new ArrayList<>();
    private List<BaseMessage> yuanyin_list = new ArrayList<>();
    private List<BaseMessage> shfw_list = new ArrayList<>();//送货服务
    private List<BaseMessage> shbx_list = new ArrayList<>();//送货保险

    private EditText brand;
    private LinearLayout xingshi_item, yuanyin_item, dizhi_item, satrt_item, end_item, danwei_item, bizhong_item, fapiao_item;
    private TextView xingshi, yuanyin, dizhi, start, end, danwei, bizhong, fapiao;
    private String oneId;
    private EditText yuan_price, cuxiao_price;
    private TextView cuxiao_text;
    private TextView tv_unit;
    private String xingshi_id;
    private List<Images> images = new ArrayList<>();
    private MyGridView my_goods_GV;
    private MyProduct myProduct = new MyProduct();
    private EditText explain, descript;
    private Button fabu;
    private EditText product_name;
    private TextView product_bianhao;
    EditText xiangxi_address;
    private String Id;
    private GoodDetial goodDetial;
    private TextView cuxiaoshuoming, shangpingmiaoshu;
    private int shuoming_number = 0, miaoshu_number = 0;
    private AlertDialog classity_dialog, brand_dialog, bug_dialog;
    private TextView custom_type;
    private String start_date = "";
    private Button servise;
    private TextView zidingyi_brand;
    private String type = "";
    private boolean isEnd;
    private List<Images> list_images = new ArrayList<>();//用来存储图片信息
    private List<ChainStores> list_chainStores = new ArrayList<>();//用来存储分店信息

    private GridImgAdapter gridImgAdapter;
    private List<PhotoModel> single_photos = new ArrayList<PhotoModel>();
    private int screen_widthOffset;
    private ArrayList<UploadGoodsBean> img_uri = new ArrayList<>();
    private LinearLayout fuwu_iten;
    private LinearLayout baoxian_iten;
    private TextView tv_shfw;
    private TextView tv_shbx;
    private boolean isUpdata = false;//是否是是修改过来

    private RecyclerView recycler_movie;
    private GridImageAdapter adapter;//视频adapter
    private int maxSelectNum = 1;//视频方面
    private List<LocalMedia> selectList = new ArrayList<>();//视频方面
    private UpPhoto upPhoto;
    private EditText et_fendianname;
    private EditText et_fendian_phone;
    private RelativeLayout rl_updatafendian;
    private RelativeLayout rl_updatafendian_phone;
    private TextView tv_updatafendian;
    private TextView tv_updatafendian_phone;
    private ProgressBar id_progress;
    private KyLoadingBuilder builder;
    /**
     * 用于发布的时候判断是否有更改时间
     */
    private String START_TIME = null;
    /**
     * 用于发布的时候判断是否有更改时间
     */
    private String END_TIME = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_good_activity);
        setCenterTitle("促销发布");
        setLeftBackMenuVisibility(AddGoodsAcitivyt.this, "");

        /**
         * view
         */
        initView();

        /**
         * data
         */
        inirData();

        /**
         * event
         */
        initEvent();

        /**
         * 查询基础字典
         */
        queryDicNode();

        /**
         * 查询促销形式
         */
        queryDicNode2();
    }

    /**
     * view
     */
    private void initView() {
        cuxiaoshuoming = (TextView) findViewById(R.id.cuxiaoshuoming);
        shangpingmiaoshu = (TextView) findViewById(R.id.shangpingmiaoshu);
        my_goods_GV = (MyGridView) findViewById(R.id.myGridview);
        brand = (EditText) findViewById(R.id.brand_name);
        xingshi_item = (LinearLayout) findViewById(R.id.xingshi_item);
        xingshi = (TextView) findViewById(R.id.xingshi);
        yuanyin_item = (LinearLayout) findViewById(R.id.yuanyin_item);
        yuanyin = (TextView) findViewById(R.id.yuanyin);
        dizhi_item = (LinearLayout) findViewById(R.id.dizhi_item);
        dizhi = (TextView) findViewById(R.id.dizhi);
        satrt_item = (LinearLayout) findViewById(R.id.start_time_item);
        start = (TextView) findViewById(R.id.start_time);
        end_item = (LinearLayout) findViewById(R.id.end_time_item);
        end = (TextView) findViewById(R.id.end_time);
        danwei_item = (LinearLayout) findViewById(R.id.danwei_item);
        danwei = (TextView) findViewById(R.id.danwei);
        bizhong_item = (LinearLayout) findViewById(R.id.bizhong_item);
        bizhong = (TextView) findViewById(R.id.bizhong);
        fapiao_item = (LinearLayout) findViewById(R.id.fapiao_item);
        fapiao = (TextView) findViewById(R.id.fapiao);
        yuan_price = (EditText) findViewById(R.id.yuan_price);
        cuxiao_price = (EditText) findViewById(R.id.cuxiao_price);
        cuxiao_text = (TextView) findViewById(R.id.cuxiao_text);
        tv_unit = (TextView) findViewById(R.id.tv_unit);
        fabu = (Button) findViewById(R.id.fabu);
        product_bianhao = (TextView) findViewById(R.id.product_bianhao);
        product_name = (EditText) findViewById(R.id.product_name);
        xiangxi_address = (EditText) findViewById(R.id.xiangxi_address);
        servise = (Button) findViewById(R.id.servise);
        zidingyi_brand = (TextView) findViewById(R.id.zidingyi_brand);
        custom_type = (TextView) findViewById(R.id.custom_type);
        explain = (EditText) findViewById(R.id.explain);
        descript = (EditText) findViewById(R.id.decript);
        head_view = (ImageView) findViewById(R.id.head_image);
        my_name = (TextView) findViewById(R.id.name);
        fuwu_iten = (LinearLayout) findViewById(R.id.fuwu_iten);
        tv_shfw = (TextView) findViewById(R.id.tv_shfw);
        baoxian_iten = (LinearLayout) findViewById(R.id.baoxian_iten);
        tv_shbx = (TextView) findViewById(R.id.tv_shbx);
        //视频选择
        recycler_movie = (RecyclerView) findViewById(R.id.recycler_movie);
        //分店店铺名称
        et_fendianname = (EditText) findViewById(R.id.et_fendianname);
        //保存
        rl_updatafendian = (RelativeLayout) findViewById(R.id.rl_updatafendian);
        tv_updatafendian = (TextView) findViewById(R.id.tv_updatafendian);
        id_progress = (ProgressBar) findViewById(R.id.id_progress);
        //分店店铺电话
        et_fendian_phone = (EditText) findViewById(R.id.et_fendian_phone);
        //保存按钮
        rl_updatafendian_phone = (RelativeLayout) findViewById(R.id.rl_updatafendian_phone);
        //显示状态
        tv_updatafendian_phone = (TextView) findViewById(R.id.tv_updatafendian_phone);
    }

    /**
     * event
     */
    private void initEvent() {
        servise.setOnClickListener(this);
        fabu.setOnClickListener(this);
        xingshi_item.setOnClickListener(this);
        yuanyin_item.setOnClickListener(this);
        //区域选择
         dizhi_item.setOnClickListener(this);
        satrt_item.setOnClickListener(this);
        end_item.setOnClickListener(this);
        danwei_item.setOnClickListener(this);
        bizhong_item.setOnClickListener(this);
        fapiao_item.setOnClickListener(this);
        fuwu_iten.setOnClickListener(this);
        baoxian_iten.setOnClickListener(this);
        rl_updatafendian.setOnClickListener(this);
        et_fendianname.setCursorVisible(false);
        et_fendianname.setFocusable(false);
        et_fendianname.setFocusableInTouchMode(false);
        rl_updatafendian_phone.setOnClickListener(this);
        //默认分店电话
        et_fendian_phone.setCursorVisible(false);
        et_fendian_phone.setFocusable(false);
        et_fendian_phone.setFocusableInTouchMode(false);

        /**
         * 活动形式选择 - 买就送选择
         */
        cuxiao_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("买就送")) {
                    bug_dialog = new AlertDialog.Builder(AddGoodsAcitivyt.this).create();
                    View view = LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.bug_give, null);
                    final TextView queding, quxiao;
                    final EditText number1, number2;
                    queding = (TextView) view.findViewById(R.id.comfirm);
                    quxiao = (TextView) view.findViewById(R.id.cannel);
                    number1 = (EditText) view.findViewById(R.id.number1);
                    number2 = (EditText) view.findViewById(R.id.number2);
                    queding.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != number1.getText() && !TextUtils.isEmpty(number1.getText()) && null != number2.getText() && !TextUtils.isEmpty(number2.getText())) {
                                cuxiao_price.setText("买" + number1.getText().toString() + "送" + number2.getText().toString());
                                bug_dialog.dismiss();
                            }
                        }
                    });
                    quxiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cuxiao_price.setText("");
                            bug_dialog.dismiss();
                        }
                    });
                    bug_dialog.setView(view);
                    bug_dialog.show();
                }
            }
        });
    }

    /**
     * data
     */
    private void inirData() {
        id_progress.setMax(100);
        //图片适配器
        gridImgAdapter = new GridImgAdapter();

        /**
         * 视频适配器
         */
        FullyGridLayoutManager manager = new FullyGridLayoutManager(AddGoodsAcitivyt.this, 5, GridLayoutManager.VERTICAL, false);
        recycler_movie.setLayoutManager(manager);
        adapter = new GridImageAdapter(AddGoodsAcitivyt.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recycler_movie.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    switch (2) {
                        case 2:
                            // 预览视频
                            PictureSelector.create(AddGoodsAcitivyt.this).externalPictureVideo(media.getPath());
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        /**
         * 查看促销品发布的信息 -- 是否有数据
         */
        //点击商品过来
        if (null != getIntent().getStringExtra("id")) {
            isUpdata = true;
            Id = getIntent().getStringExtra("id");
            isEnd = getIntent().getBooleanExtra("isEnd", false);
            getgood_detial(Id, BaseApplication.getInstance().Longitude+"",BaseApplication.getInstance().Latitude+"");
        }else{
            /**
             * grdview加载
             */
            Config.ScreenMap = Config.getScreenSize(AddGoodsAcitivyt.this, AddGoodsAcitivyt.this);
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            screen_widthOffset = (display.getWidth() - (5* DbTOPxUtils.dip2px(AddGoodsAcitivyt.this, 2)))/5;
            my_goods_GV.setAdapter(gridImgAdapter);
            img_uri.add(null);
            gridImgAdapter.notifyDataSetChanged();
        }

        /**
         * 头像
         */
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(AddGoodsAcitivyt.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(AddGoodsAcitivyt.this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.PhotoPath).apply(options)
                .into(head_view);
        /**
         * 公司名称
         */
        if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo) {
            my_name.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.StoreName);
            et_fendianname.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.StoreName);
        }else{
            et_fendianname.setText("请输入促销店铺名称");
        }
        //促销店铺电话 - 默认显示商家负责人电话
        if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.OfficePhone){
            et_fendian_phone.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.OfficePhone);
        }else{
            et_fendian_phone.setText("请输入促销店铺电话");
        }

//        /**
//         * 地址
//         */
//        if (null != MyUserInfoUtils.getInstance().myUserInfo.Area) {
//            dizhi.setText(MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.City.Name + "" + MyUserInfoUtils.getInstance().myUserInfo.Area.Name);
//        } else {
//            dizhi.setText(MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.City.Name);
//        }

        /**
         * 地址
         */
        if (null != MyUserInfoUtils.getInstance().myUserInfo.Country.Name && null != MyUserInfoUtils.getInstance().myUserInfo.Province.Name && null != MyUserInfoUtils.getInstance().myUserInfo.City.Name) {
            dizhi.setText(MyUserInfoUtils.getInstance().myUserInfo.Country.Name + " " +MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.City.Name);
        }
        //详细地址 - 这里获取的是商家我的中心保存的地址
        if (null !=MyUserInfoUtils.getInstance().myUserInfo.Domicile){
            xiangxi_address.setText(MyUserInfoUtils.getInstance().myUserInfo.Domicile);
        }

        /**
         * 详细地址 - 这里是定位的地址 - 放在分点保存的里面进行设置
         */
//        if (!Texten !Utils.isEmpty(BaseApplication.getInstance().userAddress)){
//            String userAddress = BaseApplication.getInstance().userAddress;
//            xiangxi_address.setText(userAddress.substring(userAddress.indexOf("区")+1,userAddress.length()));
//        }

        if (null != MyUserInfoUtils.getInstance().myUserInfo.BusinessScopes && MyUserInfoUtils.getInstance().myUserInfo.BusinessScopes.size() > 0) {
            myProduct.FirstClassId = MyUserInfoUtils.getInstance().myUserInfo.BusinessScopes.get(0).DictNode.Id;
            oneId = MyUserInfoUtils.getInstance().myUserInfo.BusinessScopes.get(0).DictNode.Id;
        }

        myProduct.CountryId = MyUserInfoUtils.getInstance().myUserInfo.CountryId;
        myProduct.ProvinceId = MyUserInfoUtils.getInstance().myUserInfo.ProvinceId;
        myProduct.CityId = MyUserInfoUtils.getInstance().myUserInfo.CityId;
        myProduct.AreaId = MyUserInfoUtils.getInstance().myUserInfo.AreaId;
        myProduct.FirstClassId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.FirstClassId;//主营商品
    }

    /**
     * 查询基础字典
     */
    private void queryDicNode() {
        good_one_classify.clear();
        brand_list.clear();
        danwei_list.clear();
        bizhong_list.clear();
        fapiao_list.clear();
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(AddGoodsAcitivyt.this, URLText.QUERYDICNODE, stringEntity, new MyAsyncHttpResponseHandler(AddGoodsAcitivyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                ListBaseMessage listBaseMessage = GsonUtils.fromJson(new String(responseBody), ListBaseMessage.class);
                list = listBaseMessage.MainData;
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("ba335639-52c2-4e8d-8d2b-faf8ed097418")) {
                        good_one_classify.add(list.get(i));
                    }
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("a390a2ff-40a2-487d-a719-c9ae5980fbae")) {
                        brand_list.add(list.get(i));
                    }
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("3a8eb937-691f-47be-84d5-bf0b531009d5")) {
                        danwei_list.add(list.get(i));
                    }
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("f8467615-d17b-4f30-877f-2bb1a4a0f8c0")) {
                        bizhong_list.add(list.get(i));
                    }
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("21a595ce-99f9-4533-a112-b3f21984d231")) {
                        fapiao_list.add(list.get(i));
                    }
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("558f9fab-3e39-40cd-a5ca-7d4c76e4d5a4")) {
                        yuanyin_list.add(list.get(i));
                    }
                    //送货服务
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("f409bd27-9b47-46b5-b854-e7bd0c1a4f67")) {
                        shfw_list.add(list.get(i));
                    }
                    //送货保险
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("007d3adf-ceb6-4d17-ba83-1a41bc7d7ed8")) {
                        shbx_list.add(list.get(i));
                    }
                }
                BaseMessage brand_zidiyi = new BaseMessage();
                brand_zidiyi.Name = "自定义";
                brand_list.add(brand_zidiyi);
            }
        });
    }

    /**
     * 查询促销形式
     */
    private void queryDicNode2() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(AddGoodsAcitivyt.this, URLText.QUERYDICNODE2, stringEntity, new MyAsyncHttpResponseHandler(AddGoodsAcitivyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                GoodSalesTypeList goodSalesTypeList = GsonUtils.fromJson(new String(responseBody), GoodSalesTypeList.class);
                List<GoodSalesType> showData = goodSalesTypeList.ShowData;
                for (int i = 0; i < showData.size(); i++) {
                    if (!TextUtils.equals("不限",showData.get(i).Value)){
                        goodSalesTypes.add(showData.get(i));
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
            case R.id.servise://咨询热线
//                initCallPhone("02158366991");
                call("02158366991");
                break;
            case R.id.one_classity:
//                PopupWindowFromBottomUtil.shouRange(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), good_one_classify, new PopupWindowFromBottomUtil.Listener2() {
//                    @Override
//                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
//                        yijifenlei.setTextColor(getResources().getColor(R.color.text_black));
//                        yijifenlei.setText(Id1.Name);
//                        myProduct.FirstClassId = Id1.Id;
//                        oneId = Id1.Id;
//                        popupWindow.dismiss();
//                    }
//                });
                break;
//            case R.id.two_classity:
//                if (null != oneId && !TextUtils.isEmpty(oneId)) {
//                    good_two_classify.clear();
//                    for (int i = 0; i < list.size(); i++) {
//                        if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals(oneId)) {
//                            good_two_classify.add(list.get(i));
//                        }
//                    }
//                    BaseMessage zidiyi = new BaseMessage();
//                    zidiyi.Name = "自定义";
//                    good_two_classify.add(zidiyi);
//                    PopupWindowFromBottomUtil.shouRange(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), good_two_classify, new PopupWindowFromBottomUtil.Listener2() {
//                        @Override
//                        public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
//                            if (Id1.Name.equals("自定义")) {
//                                if (oneId != null && !TextUtils.isEmpty(oneId)) {
//                                    classity_dialog = new AlertDialog.Builder(AddGoodsAcitivyt.this).create();
//                                    View view = LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.custom_classfy, null);
//                                    final TextView queding, quxiao;
//                                    final EditText content1;
//                                    queding = (TextView) view.findViewById(R.id.comfirm);
//                                    quxiao = (TextView) view.findViewById(R.id.cannel);
//                                    content1 = (EditText) view.findViewById(R.id.content);
//                                    queding.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if (null != content1.getText() && !TextUtils.isEmpty(content1.getText())) {
//                                                apply_calssity(content1.getText().toString(), oneId);
//                                            }
//                                        }
//                                    });
//                                    quxiao.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            classity_dialog.dismiss();
//                                        }
//                                    });
//                                    classity_dialog.setView(view);
//                                    classity_dialog.show();
//                                } else {
//                                    Toast.makeText(AddGoodsAcitivyt.this, "请先选择一级分类", Toast.LENGTH_SHORT).show();
//                                }
//
//                            } else {
////                                erjifenlei.setTextColor(getResources().getColor(R.color.text_black));
////                                erjifenlei.setText(Id1.Name);
//                                myProduct.SecondClassId = Id1.Id;
//                            }
//                            popupWindow.dismiss();
//                        }
//                    });
//                } else {
//                    Toast.makeText(AddGoodsAcitivyt.this, "请先选择一级分类", Toast.LENGTH_SHORT).show();
//                }
//                break;
            case R.id.yuanyin_item://活动原因
                PopupWindowFromBottomUtil.shouRange(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), yuanyin_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        yuanyin.setTextColor(getResources().getColor(R.color.text_black));
                        yuanyin.setText(Id1.Name);
                        myProduct.PromotionCauseId= Id1.Id;
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.xingshi_item://活动形式
                PopupWindowFromBottomUtil.shouSalesType(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), goodSalesTypes, new PopupWindowFromBottomUtil.Listenrt3() {
                    @Override
                    public void callback(GoodSalesType goodSalesType, PopupWindow popupWindow) {
                        type = "";
                        myProduct.PromotionType = goodSalesType.Id;
                        yuan_price.setText("");
                        cuxiao_price.setText("");
                        yuan_price.setEnabled(true);
                        cuxiao_price.setEnabled(true);

                        cuxiao_price.setCursorVisible(true);
                        cuxiao_price.setFocusable(true);
                        cuxiao_price.setFocusableInTouchMode(true);

                        cuxiao_text.setText("促销价格");
                        tv_unit.setText("(元)");
                        xingshi.setTextColor(getResources().getColor(R.color.text_black));
                        xingshi.setText(goodSalesType.Value);
                        xingshi_id = goodSalesType.Id;

                        if (goodSalesType.Id.equals("5")) {
                            yuan_price.setText("10");
                            cuxiao_price.setText("10");
                            yuan_price.setEnabled(false);
                            cuxiao_price.setEnabled(false);
                        } else if (goodSalesType.Id.equals("6")) {
                            yuan_price.setText("1");
                            cuxiao_price.setText("1");
                            yuan_price.setEnabled(false);
                            cuxiao_price.setEnabled(false);
                        } else if (goodSalesType.Id.equals("11")) {
                            yuan_price.setText("0");
                            cuxiao_price.setText("0");
                            yuan_price.setEnabled(false);
                            cuxiao_price.setEnabled(false);
                        } else if (goodSalesType.Id.equals("13")) {
                            yuan_price.setText("0");
                            cuxiao_price.setText("0");
                            yuan_price.setEnabled(false);
                            cuxiao_price.setEnabled(false);
                        } else if (goodSalesType.Id.equals("2")) {//买就送
                            type = "买就送";
                             cuxiao_text.setText("买N送N");
                            tv_unit.setText("(件)");
                            cuxiao_price.setCursorVisible(false);
                            cuxiao_price.setFocusable(false);
                            cuxiao_price.setFocusableInTouchMode(false);
                        }else if(goodSalesType.Id.equals("3")){//买A赠B
                            cuxiao_price.setCursorVisible(false);
                            cuxiao_price.setFocusable(false);
                            cuxiao_price.setFocusableInTouchMode(false);
                            yuan_price.addTextChangedListener(new TextWatcher() {//设置文本改变监听 - 和促销价格一样
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if ("买A赠B".equals(xingshi.getText()) || "抽奖赠送".equals(xingshi.getText()) || "免费送货".equals(xingshi.getText())){
                                        cuxiao_price.setText(yuan_price.getText());
                                    }
                                }
                            });
                        }else if (goodSalesType.Id.equals("7")){//抽奖赠送
                            cuxiao_price.setCursorVisible(false);
                            cuxiao_price.setFocusable(false);
                            cuxiao_price.setFocusableInTouchMode(false);
                            yuan_price.addTextChangedListener(new TextWatcher() {//设置文本改变监听 - 和促销价格一样
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if ("买A赠B".equals(xingshi.getText()) || "抽奖赠送".equals(xingshi.getText()) || "免费送货".equals(xingshi.getText())){
                                        cuxiao_price.setText(yuan_price.getText());
                                    }
                                }
                            });
                        }else if (goodSalesType.Id.equals("12")){//免费送货
                            cuxiao_price.setCursorVisible(false);
                            cuxiao_price.setFocusable(false);
                            cuxiao_price.setFocusableInTouchMode(false);
                            yuan_price.addTextChangedListener(new TextWatcher() {//设置文本改变监听 - 和促销价格一样
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if ("买A赠B".equals(xingshi.getText()) || "抽奖赠送".equals(xingshi.getText()) || "免费送货".equals(xingshi.getText())){
                                        cuxiao_price.setText(yuan_price.getText());
                                    }
                                }
                            });
                        }
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.dizhi_item://地址
                PopupWindowFromBottomUtil.showAddress(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), list, new PopupWindowFromBottomUtil.Listenre1() {
                    @Override
                    public void callBack(String Id,String Id1, String Id2, String Id3, String content, PopupWindow popupWindow) {
                        myProduct.CountryId = Id;
                        myProduct.ProvinceId = Id1;
                        myProduct.CityId = Id2;
                        myProduct.AreaId = Id3;
                        dizhi.setTextColor(getResources().getColor(R.color.text_black));
                        dizhi.setText(content);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.start_time_item://开始日期
                if ("请选择".equals(start.getText().toString())){
                    UsePicker.showAll(AddGoodsAcitivyt.this, new UsePicker.CallBack() {
                        @Override
                        public void callBack(String time) {
                            SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date1 = null;
                            try {
                                date1 = dd.parse(time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Long time1 = date1.getTime();
                            Date dt = new Date();
                            Long now = dt.getTime();
                            if (now > time1) {
                                Toast.makeText(AddGoodsAcitivyt.this, "促销时间不能早于当前时间", Toast.LENGTH_SHORT).show();
                            } else {
                                start_date = time;
                                myProduct.StartTime = time;
                                start.setTextColor(getResources().getColor(R.color.text_black));
                                start.setText(time);
                            }
                        }
                    }, start.getText().toString());
                }else{
                    long currentTime  = System.currentTimeMillis();
//                    currentTime +=5*60*1000;
                    Date date=new Date(currentTime);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    String format = sdf.format(date);

                    UsePicker.showAll(AddGoodsAcitivyt.this, new UsePicker.CallBack() {
                        @Override
                        public void callBack(String time) {
                            SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date date1 = null;
                            try {
                                date1 = dd.parse(time);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Long time1 = date1.getTime();
                            Date dt = new Date();
                            Long now = dt.getTime();
                            if (now > time1) {
                                Toast.makeText(AddGoodsAcitivyt.this, "促销时间不能早于当前时间", Toast.LENGTH_SHORT).show();
                            } else {
                                start_date = time;
                                myProduct.StartTime = time;
                                start.setTextColor(getResources().getColor(R.color.text_black));
                                start.setText(time);
                            }
                        }
                    }, format);
                }
                break;
            case R.id.end_time_item://结束时间
                UsePicker.showAll(AddGoodsAcitivyt.this, new UsePicker.CallBack() {
                    @Override
                    public void callBack(String time) {
                        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date date1 = dd.parse(start_date);
                            Date date2 = dd.parse(time);
                            Long time1 = date1.getTime();
                            Long time2 = date2.getTime();
                            if ((time2 > time1)) {
                                myProduct.EndTime = time;
                                end.setTextColor(getResources().getColor(R.color.text_black));
                                end.setText(time);
                            } else {
                                Toast.makeText(AddGoodsAcitivyt.this, "结束时间要大于开始时间", Toast.LENGTH_SHORT).show();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },end.getText().toString());
                break;
            case R.id.danwei_item://计量单位
                PopupWindowFromBottomUtil.shouRange(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), danwei_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        myProduct.UnitId = Id1.Id;
                        danwei.setTextColor(getResources().getColor(R.color.text_black));
                        danwei.setText(Id1.Name);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.fuwu_iten://送货服务
                PopupWindowFromBottomUtil.shouRange(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), shfw_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        myProduct.DeliveryServiceId= Id1.Id;
                        tv_shfw.setTextColor(getResources().getColor(R.color.text_black));
                        tv_shfw.setText(Id1.Name);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.baoxian_iten://送货保险
                PopupWindowFromBottomUtil.shouRange(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), shbx_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        myProduct.PremiumId = Id1.Id;
                        tv_shbx.setTextColor(getResources().getColor(R.color.text_black));
                        tv_shbx.setText(Id1.Name);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.bizhong_item://购物币种
                PopupWindowFromBottomUtil.shouRange(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), bizhong_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        myProduct.CurrencyId = Id1.Id;
                        bizhong.setTextColor(getResources().getColor(R.color.text_black));
                        bizhong.setText(Id1.Name);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.fapiao_item://发票
                PopupWindowFromBottomUtil.shouRange(AddGoodsAcitivyt.this, LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.business_my_center_activity, null), fapiao_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        myProduct.InvoiceId = Id1.Id;
                        fapiao.setTextColor(getResources().getColor(R.color.text_black));
                        fapiao.setText(Id1.Name);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.fabu://发布
                try{
                    if (TextUtils.isEmpty(et_fendianname.getText())){
                        Toast.makeText(this, "请输入店铺名称", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(et_fendian_phone.getText())){
                        Toast.makeText(this, "请输入店铺电话", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (img_uri.size() == 0 || img_uri.size() == 1){
                        Toast.makeText(this, "请上传促销品照片", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(product_name.getText())){
                        Toast.makeText(this, "请输入商品名称", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(brand.getText())){
                        Toast.makeText(this, "请输入商品品牌", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if ("请选择".equals(xingshi.getText()) || TextUtils.isEmpty(xingshi.getText())){
                        Toast.makeText(this, "请选择活动形式", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if ("请选择".equals(yuanyin.getText()) || TextUtils.isEmpty(yuanyin.getText())){
                        Toast.makeText(this, "请选择活动原因", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if ("请选择".equals(danwei.getText()) || TextUtils.isEmpty(danwei.getText())){
                        Toast.makeText(this, "请选择计量单位", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(yuan_price.getText())){
                        Toast.makeText(this, "请输入商品原价", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if (yuan_price.getText().toString().contains(".")){
                        String substring = yuan_price.getText().toString().substring(yuan_price.getText().toString().indexOf("."), yuan_price.getText().toString().length());
                        if (substring.length() > 3){
                            Toast.makeText(this, "原价小数点请保留两位", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }

                    if (TextUtils.isEmpty(cuxiao_price.getText())){
                        Toast.makeText(this, "请输入促销价格", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if (cuxiao_price.getText().toString().contains(".")){
                        String substring = cuxiao_price.getText().toString().substring(cuxiao_price.getText().toString().indexOf("."), cuxiao_price.getText().toString().length());
                        if (substring.length() > 3){
                            Toast.makeText(this, "促销价小数点请保留两位", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }
                    if ("请选择".equals(bizhong.getText()) || TextUtils.isEmpty(bizhong.getText())){
                        Toast.makeText(this, "请选择购物币种", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if ("请选择".equals(dizhi.getText()) || TextUtils.isEmpty(dizhi.getText())){
                        Toast.makeText(this, "请选所在区域", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(xiangxi_address.getText())){
                        Toast.makeText(this, "请输入详细地址", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if ("请选择".equals(start.getText()) || TextUtils.isEmpty(start.getText())){
                        Toast.makeText(this, "请选择开始日期", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if ("请选择".equals(end.getText()) || TextUtils.isEmpty(end.getText())){
                        Toast.makeText(this, "请选择结束日期", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(explain.getText())){
                        Toast.makeText(this, "请输入促销活动说明", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(descript.getText())){
                        Toast.makeText(this, "请输入促销商品描述", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    //比较原价和促销价的大小
                    String s = xingshi.getText().toString();//活动形式
                    if ("买就送".equals(s) || "买A赠B".equals(s) || "一律10元".equals(s) || "1元商品".equals(s) || "免费赠送".equals(s) || "抽奖赠送".equals(s) || "免费送货".equals(s)){
                        //不做任何限制
                    }else{
                        if ("不限".equals(s) || "价格折扣".equals(s) || "会员优惠".equals(s) || "众筹购买".equals(s) || "团购分享".equals(s) || "限时甩卖".equals(s)){
                            if ("促销价格".equals(cuxiao_text.getText().toString())){//如果是促销价格才可以
                                String s1 = yuan_price.getText().toString();//商品原价
                                String s2 = cuxiao_price.getText().toString();//促销价格
                                if (Double.parseDouble(s2) > Double.parseDouble(s1)){
                                    Toast.makeText(this, "请重新输入促销价格", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                    }
                    //转移到注册界面
    //                if ("请选择".equals(fapiao.getText()) || TextUtils.isEmpty(fapiao.getText())){
    //                    Toast.makeText(this, "请选择商品发票", Toast.LENGTH_SHORT).show();
    //                    break;
    //                }
    //                if ("请选择".equals(tv_shfw.getText()) || TextUtils.isEmpty(tv_shfw.getText())){
    //                    Toast.makeText(this, "请选择送货服务", Toast.LENGTH_SHORT).show();
    //                    break;
    //                }
    //                if ("请选择".equals(tv_shbx.getText()) || TextUtils.isEmpty(tv_shbx.getText())){
    //                    Toast.makeText(this, "请选择送货保险", Toast.LENGTH_SHORT).show();
    //                    break;
    //                }
                    //将原图片进行清除
                    list_images.clear();
                    list_images.addAll(images);
                    myProduct.Images = list_images;//方格图片
                    myProduct.Name = product_name.getText().toString();//商品名称
                    myProduct.Brand = brand.getText().toString();//商品品牌
                    myProduct.Number = product_bianhao.getText().toString();//商品编号
    //                myProduct.PromotionTypeName = xingshi.getText().toString();//商品形式------------
    //                myProduct.PromotionCause.Name= yuanyin.getText().toString();//活动原因
                    myProduct.Address =  xiangxi_address.getText().toString();
    //                myProduct.Address = xiangxi_address.getText().toString();
    //                myProduct.StartTime = start.getText().toString();//开始日期
    //                myProduct.EndTimeName = end.getText().toString();//结束日期
                    myProduct.OriginalPrice = yuan_price.getText().toString();//原价
    //                if (true == updateDianPuName){
    //                    myProduct.ChainStores = list_chainStores;//自己手动输入保存
    //                }else{
    //                    list_chainStores.clear();
    //                    //在这李进行分店保存
    //                    ChainStores chainStores = new ChainStores();
    //                    chainStores.Name = et_fendianname.getText().toString();
    //                    chainStores.LinkMan =MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName;
    //                    chainStores.ContactWay = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.PhoneNumber;
    //                    chainStores.Address = xiangxi_address.getText().toString();
    //                    list_chainStores.add(chainStores);
    //                }
                    //在这李进行分店保存
                    list_chainStores.clear();
                    ChainStores chainStores = new ChainStores();
                    chainStores.Name = et_fendianname.getText().toString();
                    chainStores.ContactWay = et_fendian_phone.getText().toString();
                    chainStores.LinkMan =MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName;
//                    chainStores.ContactWay = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.PhoneNumber;
                    chainStores.Address = xiangxi_address.getText().toString();
                    list_chainStores.add(chainStores);
                    myProduct.ChainStores = list_chainStores;//默认保存

                    if ("促销价格".equals(cuxiao_text.getText().toString())){
                        myProduct.Price = cuxiao_price.getText().toString();//促销现价-----------------
                    }else if ("买N送N".equals(cuxiao_text.getText().toString())){
                        myProduct.Price = yuan_price.getText().toString();//买N送N的就是原价
                        int mai = cuxiao_price.getText().toString().indexOf("买");
                        int song = cuxiao_price.getText().toString().indexOf("送");

                        String substring = cuxiao_price.getText().toString().substring(mai+1, song);//买到就之间的数据
                        String substring1 = cuxiao_price.getText().toString().substring(song+1, cuxiao_price.length());//就之后的数据

                        myProduct.PurchaseQuantity = substring;
                        myProduct.FreeQuantity = substring1;
                    }
    //                myProduct.Price = cuxiao_price.getText().toString();//促销现价-----------------
    //                myProduct.Unit.Name = danwei.getText().toString();//计量单位
    //                myProduct.Currency.Name = bizhong.getText().toString();//购物币种
    //                myProduct.Invoice.Name = fapiao.getText().toString();//商品发票
                    myProduct.Introduction = explain.getText().toString();//促销活动说明
                    myProduct.Description = descript.getText().toString();//促销商品描述
    //                myProduct.DeliveryService.Name = tv_shfw.getText().toString();//送货服务
    //                myProduct.Premium.Name = tv_shbx.getText().toString();//送货保险
    //                save_brand(brand.getText().toString());//商品品牌--------------之前的保存品牌
                    if (null != upPhoto){
                        //Id
                        myProduct.VideoId = upPhoto.Id;
                        //path
                        myProduct.VideoPath = upPhoto.FilePath;
                    }
                    /**
                     * 如果时间没有改变 - 开始时间
                     */
                    if (null != START_TIME && START_TIME.equals(start.getText().toString())){
                        startTimeDialog("请修改开始日期");
                        break;
                    }
                    /**
                     * 结束时间要大于开始时间
                     */
//                    SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd");
//                    try {
//                        Date date1 = dd.parse(start.getText().toString());
//                        Date date2 = dd.parse(end.getText().toString());
//                        Long time1 = date1.getTime();
//                        Long time2 = date2.getTime();
//                        if ((time2 < time1)) {
//                            startTimeDialog("请修改结束日期");
//                            break;
//                        }
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
                    /**
                     * 如果时间没有改变 - 结束时间
                     */
                    if (null != END_TIME && END_TIME.equals(end.getText().toString())){
                        startTimeDialog("请修改结束日期");
                        break;
                    }

                    //新添加
                    if (false == isUpdata){
                        saveMyProduct(myProduct);
                    //修改过来
                    }else if(true == isUpdata){
                        String storeName = PreferencesUtils.getString(AddGoodsAcitivyt.this, "StoreName", "");
                        String address = PreferencesUtils.getString(AddGoodsAcitivyt.this, "address", "");
                        String xiangxiaddress = PreferencesUtils.getString(AddGoodsAcitivyt.this, "xiangxiaddress", "");
                        if (product_name.getText().toString().equals(storeName) && dizhi.getText().toString().equals(address) && xiangxi_address.getText().toString().equals(xiangxiaddress)){
                            myProduct.Id = null;
                            saveMyProduct(myProduct);
                        }else{
                            myProduct.Id = null;
                            saveMyProduct(myProduct);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "发布失败", Toast.LENGTH_SHORT).show();
                }
                break;
            //保存 - 更改分店地址
            case R.id.rl_updatafendian:
                if ("更换店铺名称".equals(tv_updatafendian.getText().toString())){
                    et_fendianname.setText("");
                    tv_updatafendian.setText("保存");
                    et_fendianname.setCursorVisible(true);
                    et_fendianname.setFocusable(true);
                    et_fendianname.setFocusableInTouchMode(true);
                    et_fendianname.requestFocus();
                }else if ("保存".equals(tv_updatafendian.getText().toString())){
                    tv_updatafendian.setText("更换店铺名称");
                    if (TextUtils.isEmpty(et_fendianname.getText())){
                        Toast.makeText(this, "请输入促销店铺名称", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                        et_fendianname.setCursorVisible(false);
                        et_fendianname.setFocusable(false);
                        et_fendianname.setFocusableInTouchMode(false);
                    }

                    if (!TextUtils.isEmpty(BaseApplication.getInstance().userAddress)) {//保存分店地址实现定位详细地址
                        String userAddress = BaseApplication.getInstance().userAddress;
                        xiangxi_address.setText(userAddress.substring(userAddress.indexOf("区") + 1, userAddress.length()));
                    }
                }



//                if (updateDianPuName == false){
//                    et_fendianname.setText("");
//                    tv_updatafendian.setText("保存");
//                    et_fendianname.setCursorVisible(true);
//                    et_fendianname.setFocusable(true);
//                    et_fendianname.setFocusableInTouchMode(true);
//                    et_fendianname.requestFocus();
//                    updateDianPuName = true;
//                }else{
//                    if (TextUtils.isEmpty(et_fendianname.getText())){
//                        Toast.makeText(this, "请输入促销店铺名称", Toast.LENGTH_SHORT).show();
//                    }else{
//                        if (TextUtils.isEmpty(xiangxi_address.getText())){
//                            Toast.makeText(this, "请输入街道、门牌号", Toast.LENGTH_SHORT).show();
//                            break;
//                        }
//
//                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
//                        rl_updatafendian.setEnabled(false);
//                        et_fendianname.setCursorVisible(false);
//                        et_fendianname.setFocusable(false);
//                        et_fendianname.setFocusableInTouchMode(false);
//
//                        //在这李进行分店保存
//                        list_chainStores.clear();
//                        ChainStores chainStores = new ChainStores();
//                        chainStores.Name = et_fendianname.getText().toString();
//                        chainStores.LinkMan =MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName;
//                        chainStores.ContactWay = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.PhoneNumber;
//                        chainStores.Address = xiangxi_address.getText().toString();
//                        list_chainStores.add(chainStores);
//                    }
//                }
                break;
            case R.id.rl_updatafendian_phone://促销店铺电话
                if ("更换店铺电话".equals(tv_updatafendian_phone.getText().toString())){
                    et_fendian_phone.setText("");
                    tv_updatafendian_phone.setText("保存");
                    et_fendian_phone.setCursorVisible(true);
                    et_fendian_phone.setFocusable(true);
                    et_fendian_phone.setFocusableInTouchMode(true);
                    et_fendian_phone.requestFocus();
                }else if ("保存".equals(tv_updatafendian_phone.getText().toString())){
                    if (TextUtils.isEmpty(et_fendian_phone.getText())){
                        Toast.makeText(this, "请输入促销店铺电话", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                        tv_updatafendian_phone.setText("更换店铺电话");
                        et_fendian_phone.setCursorVisible(false);
                        et_fendian_phone.setFocusable(false);
                        et_fendian_phone.setFocusableInTouchMode(false);
                    }
                }
                break;
                default:
                    break;
        }
    }

    /**
     * 如果没有更改开始日期
     */
    public void startTimeDialog(String content){
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
        Dialog dialog = new XiMaiPopDialog1(AddGoodsAcitivyt.this, "温馨提示", content, "知道了", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 发布
     * @param myProduct
     */
    private void saveMyProduct(MyProduct myProduct) {
        showLoading(this,"正在发布，请稍后");
        WebRequestHelper.json_post(AddGoodsAcitivyt.this, URLText.SAVEMYPRODUCT, RequestParamsPool.getMyProduct(myProduct), new MyAsyncHttpResponseHandler(AddGoodsAcitivyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                String isSucess = null;
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                    String message = object.optString("Message");
                    isSucess = object.optString("IsSuccess");

                    if (null != builder){
                        builder.dismiss();
                    }
                    if (isSucess.equals("true")) {
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 发布成功刷新收藏汇总的结束的商品
         */
        NotificationCenter.defaultCenter().postNotification(Constants.LOADING_COLLECTION_OVERGOODS,"");

        /**
         * 发布成功的时候对发布促销的商品进行刷新
         */
        NotificationCenter.defaultCenter().postNotification(Constants.LOADING_FABUCUXIAO,"");
    }

    private void apply_calssity(String name, String ParentId) {
        WebRequestHelper.json_post(AddGoodsAcitivyt.this, URLText.APPLY_CLASSITY, RequestParamsPool.apply_classity(name, ParentId), new MyAsyncHttpResponseHandler(AddGoodsAcitivyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.optString("Message");
                    String is = object.optString("IsSuccess");
                    if (is.equals("true")) {
                        queryDicNode();
                        classity_dialog.dismiss();
                    }
                    Toast.makeText(AddGoodsAcitivyt.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 保存品牌信息 - 自定义的时候使用--废弃
     * @param name
     */
    private void save_brand(String name) {
        WebRequestHelper.json_post(AddGoodsAcitivyt.this, URLText.SAVEBRAND, RequestParamsPool.save_brand(name), new MyAsyncHttpResponseHandler(AddGoodsAcitivyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.optString("Message");
                    String is = object.optString("IsSuccess");
                    if (is.equals("true")) {
                        queryDicNode();
                        if (null != brand_dialog) {
                            brand_dialog.dismiss();
                        }
                    }
                    Toast.makeText(AddGoodsAcitivyt.this, message+"2", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 打电话
     *
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
                        com.yanzhenjie.alertdialog.AlertDialog.newBuilder(AddGoodsAcitivyt.this)
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
                        if (AndPermission.hasAlwaysDeniedPermission(AddGoodsAcitivyt.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(AddGoodsAcitivyt.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        if (requestCode == 200) {// Failure.
                            Toast.makeText(AddGoodsAcitivyt.this, "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
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
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }

    /**
     * 查看已发布的促销品信息
     * @param Id
     */
    private void getgood_detial(final String Id,String Longitude,String Latitude) {
        WebRequestHelper.json_post(AddGoodsAcitivyt.this, URLText.GET_GOOD_DETIAL, RequestParamsPool.getGoodDetial(Id,Longitude,Latitude), new MyAsyncHttpResponseHandler(AddGoodsAcitivyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    Boolean isSuccess = object.optBoolean("IsSuccess");
                    String MainData = object.optString("MainData");
                    goodDetial = GsonUtils.fromJson(MainData, GoodDetial.class);
                    //商品Id
                    myProduct.Id = Id;
                    if (null != goodDetial) {
                        if (null != goodDetial.Images){
                            //保存原有的照片
                            images = goodDetial.Images;
                            for (int i = 0; i <goodDetial.Images.size() ; i++) {
                                String imagePath = goodDetial.Images.get(i).ImagePath;
                                UploadGoodsBean uploadGoodsBean = new UploadGoodsBean();
                                uploadGoodsBean.setUrl(URLText.img_url + imagePath);
                                img_uri.add(uploadGoodsBean);
                            }
                            gridImgAdapter.notifyDataSetChanged();
                        }
                        //设置店铺名称 - 有就显示有的没有就显示默认
                        if (null != goodDetial.ChainStores){
                            if (goodDetial.ChainStores.size() > 0){
                                String name = goodDetial.ChainStores.get(0).Name;
                                et_fendianname.setText(name);
                            }
                            //分店号码
                            if (goodDetial.ChainStores.size() > 0){
                                String ContactWay = goodDetial.ChainStores.get(0).ContactWay;
                                if (null != ContactWay){
                                    et_fendian_phone.setText(ContactWay);
                                }
                            }
                        }
                        product_name.setText(goodDetial.Name);//名称
                        yuanyin.setText(goodDetial.PromotionCause.Name);//促销原因
                        product_bianhao.setText(goodDetial.Number);//商品编号
                        brand.setText(goodDetial.Brand);//品牌
                        xingshi.setText(goodDetial.PromotionTypeName);//设置商品形式 - 显示
                        myProduct.PromotionType = goodDetial.PromotionType;//上传是上传这个 -
                        if (null != goodDetial.Country.Name && null != goodDetial.Province.Name && null != goodDetial.City.Name) {
                            dizhi.setText(goodDetial.Country.Name+ " " +goodDetial.Province.Name + " " + goodDetial.City.Name);
                        }
                        xiangxi_address.setText(goodDetial.Address);
//                        else {
//                            dizhi.setText(MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.City.Name);
//                        }
//                        myProduct.ProvinceId = MyUserInfoUtils.getInstance().myUserInfo.ProvinceId;
//                        myProduct.CityId = MyUserInfoUtils.getInstance().myUserInfo.CityId;
//                        myProduct.AreaId = MyUserInfoUtils.getInstance().myUserInfo.AreaId;
                        if (isEnd) {
                            start.setTextColor(getResources().getColor(R.color.stepcolor));
                            end.setTextColor(getResources().getColor(R.color.stepcolor));
                        }
                        myProduct.CountryId = goodDetial.CountryId;
                        myProduct.ProvinceId = goodDetial.ProvinceId;
                        myProduct.CityId = goodDetial.CityId;
                        myProduct.AreaId = goodDetial.AreaId;
                        //获取到产品发布的开始时间
                        start.setText(goodDetial.StartTimeName);
                        //保存当前时间 - 用于发布的时候判断是否有更改时间
                        START_TIME = goodDetial.StartTimeName;
                        //保存当前时间 - 用于发布的时候判断是否有更改时间
                        END_TIME = goodDetial.EndTimeName;
                        end.setText(goodDetial.EndTimeName);
                        myProduct.StartTime = goodDetial.StartTime;//开始时间
                        myProduct.EndTime = goodDetial.EndTime;//结束时间
                        yuan_price.setText(goodDetial.OriginalPrice);//原价
                        if (("买就送").equals(goodDetial.PromotionTypeName)){
                            cuxiao_text.setText("买N送N");
                            tv_unit.setText("(件)");
                            cuxiao_price.setText("买"+ goodDetial.PurchaseQuantity +"送"+ goodDetial.FreeQuantity);
                            cuxiao_price.setEnabled(true);
                            type = "买就送";
                            cuxiao_price.setCursorVisible(false);
                            cuxiao_price.setFocusable(false);
                            cuxiao_price.setFocusableInTouchMode(false);
                        }else{
                            cuxiao_text.setText("促销价格");
                            tv_unit.setText("(元)");
                            cuxiao_price.setText(goodDetial.Price);
                        }
                        myProduct.PromotionCauseId = goodDetial.PromotionCause.Id;//活动原因
                        xingshi_id = goodDetial.PromotionType;//活动形式
                        myProduct.FirstClassId = goodDetial.FirstClassId;//主营商品
                        myProduct.PremiumId = goodDetial.PremiumId;//送货保险
                        if (null != goodDetial.Premium){
                            tv_shbx.setText(goodDetial.Premium.Name);//送货保险
                        }
                        if (null != goodDetial.DeliveryService){
                            tv_shfw.setText(goodDetial.DeliveryService.Name);//送货服务
                        }
                        myProduct.DeliveryServiceId = goodDetial.DeliveryServiceId;//送货服务
                        myProduct.PromotionTypeName = goodDetial.PromotionTypeName;//促销形式
                        myProduct.UnitId = goodDetial.UnitId;//计量单位
                        if (null != goodDetial.Unit) {
                            danwei.setText(goodDetial.Unit.Name);
                        }
                        myProduct.CurrencyId = goodDetial.CurrencyId;//币种
                        if (null != goodDetial.Currency) {
                            bizhong.setText(goodDetial.Currency.Name);
                        }
                        myProduct.InvoiceId = goodDetial.InvoiceId;//商品发票
                        if (null != goodDetial.Invoice) {
                            fapiao.setText(goodDetial.Invoice.Name);
                        }
                        explain.setText(goodDetial.Introduction);//促销说明
                        descript.setText(goodDetial.Description);//商品描述
                    }


                    /**
                     * grdview加载
                     */
                    Config.ScreenMap = Config.getScreenSize(AddGoodsAcitivyt.this, AddGoodsAcitivyt.this);
                    WindowManager windowManager = getWindowManager();
                    Display display = windowManager.getDefaultDisplay();
                    screen_widthOffset = (display.getWidth() - (5* DbTOPxUtils.dip2px(AddGoodsAcitivyt.this, 2)))/5;
                    my_goods_GV.setAdapter(gridImgAdapter);
                    img_uri.add(null);
                    gridImgAdapter.notifyDataSetChanged();

                    /**
                     * 视频加载
                     */
                    if (null != goodDetial.VideoPath){
                        LocalMedia localMedia = new LocalMedia();
                        localMedia.setPath(URLText.img_url+goodDetial.VideoPath);
                        selectList.add(localMedia);
                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }

                    PreferencesUtils.putString(AddGoodsAcitivyt.this,"StoreName",product_name.getText().toString());
                    PreferencesUtils.putString(AddGoodsAcitivyt.this,"address",dizhi.getText().toString());
                    PreferencesUtils.putString(AddGoodsAcitivyt.this,"xiangxiaddress",xiangxi_address.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 上传图片
     * @param file
     * @param type
     */
    private void upLoadImage(File file, final String type) {
        WebRequestHelper.post(URLText.UPLOAD_IMAGE, RequestParamsPool.upLoad(file, type), new MyAsyncHttpResponseHandler(AddGoodsAcitivyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    UpPhoto upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                    Images images1 = new Images();
                    if (null != upPhoto) {
                        images1.ImageId = upPhoto.Id;
                        images1.ImagePath = upPhoto.FilePath;
                        images.add(images1);
                        gridImgAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 上传视频
     * @param file
     * @param type
     */
    private void upLoadVideo(File file, final String type) {
        WebRequestHelper.post(URLText.UPLOAD_IMAGE, RequestParamsPool.upLoad(file, type), new MyAsyncHttpResponseHandler(AddGoodsAcitivyt.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean isSuccess = jsonObject.optBoolean("IsSuccess");
                    if (true == isSuccess){
                        Toast.makeText(AddGoodsAcitivyt.this, "视频上传成功", Toast.LENGTH_SHORT).show();
                        upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                    }else{
                        Toast.makeText(AddGoodsAcitivyt.this, "视频上传失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:     //店铺方格图片进行回调
                if (data != null) {
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");
                    if (img_uri.size() > 0) {
                        img_uri.remove(img_uri.size() - 1);
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        img_uri.add(new UploadGoodsBean(paths.get(i), false));
                        //上传参数
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.setOriginalPath(paths.get(i));
                        photoModel.setChecked(true);
                        single_photos.add(photoModel);
                    }
                    if (img_uri.size() < 9) {
                        img_uri.add(null);
                    }
                    gridImgAdapter.notifyDataSetChanged();
                    /**
                     * 上传照片 -获取到本地的图片路径 - 转换成file - 进行上传
                     */
                    if (paths.size() > 0) {
                        for (int i = 0; i < paths.size(); i++) {
                            upLoadImage(new File(paths.get(i)), "BusinessLicense");
                        }
                    }
                }
                break;
            case PictureConfig.CHOOSE_REQUEST://选择视频的回调设置到适配器
                // 图片选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);
                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

//                I/视频-----》: /storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20180303_134105.mp4
//                I/视频-----》: 00:15
//                I/原图地址::: /storage/emulated/0/PictureSelector/CameraImage/PictureSelector_20180303_134105.mp4

                for (LocalMedia media : selectList) {
                    Log.i("视频-----》", media.getPath());
                    long duration = media.getDuration();
                    String s = DateUtils.timeParse(duration);
                    Log.i("视频-----》", s);
                }
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();

                if (selectList.size() > 0){//判断是否选择视频
                    String path = selectList.get(0).getPath();//视频地址
                    long duration = selectList.get(0).getDuration();//视频时间long类型
                    String s = DateUtils.timeParse(duration);//视频时间00:15

                    if (duration >= 0 && duration <= 15040){
//                    upLoadVideo(new File(path),"Video");
                        Map params = new HashMap<>();
//                    params.put("file",new File(path));
                        params.put("FileType","Video");
                        params.put("SecurityStamp",LoginUser.getInstance().userInfo.SecurityStamp);
                        params.put("Password","15047754A79842C784E56355FC691E6A");
                        Map<String, String> headers = new HashMap<>();
                        headers.put("Authorization", BaseApplication.getInstance().Token);
                        OkHttpUtils.post()//
                                .addFile("file", new File(path).getName(), new File(path))//
                                .url(URLText.UPLOAD_IMAGE)
                                .params(params)//
                                .headers(headers)//
                                .build()//
                                .execute(new MyStringCallback());
                    }else{
                        Toast.makeText(this, "视频时间已超过15秒", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * okhttp
     */
    public class MyStringCallback extends StringCallback {
        @Override
        public void onBefore(Request request, int id)
        {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id)
        {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
            id_progress.setVisibility(View.GONE);
//            mTv.setText("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e("yag", "onResponse：complete");
//            mTv.setText("onResponse:" + response);
            id_progress.setVisibility(View.GONE);
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean isSuccess = jsonObject.optBoolean("IsSuccess");
                if (true == isSuccess){
                    Toast.makeText(AddGoodsAcitivyt.this, "视频上传成功", Toast.LENGTH_SHORT).show();
                    upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                }else{
                    Toast.makeText(AddGoodsAcitivyt.this, "视频上传失败", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void inProgress(float progress, long total, int id) {
            Log.e("yag", "inProgress:" + progress);
            id_progress.setVisibility(View.VISIBLE);
            id_progress.setProgress((int) (100 * progress));
        }
    }

    /**
     * 方格店铺照片
     */
    class GridImgAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return img_uri.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if(convertView!=null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(AddGoodsAcitivyt.this).inflate(R.layout.activity_addstory_img_item,null);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            holder.delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);
            if (img_uri.get(position) == null) {
                holder.delete_IV.setVisibility(View.GONE);

                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(AddGoodsAcitivyt.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(AddGoodsAcitivyt.this).load(R.mipmap.achieve_icon_addphoto_default).apply(options).into(holder.add_IB);

                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(AddGoodsAcitivyt.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 15 - (img_uri.size() - 1));
                        startActivityForResult(intent, 0);
                    }
                });
            } else {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(AddGoodsAcitivyt.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(AddGoodsAcitivyt.this).load(img_uri.get(position).getUrl())
                        .apply(options).into(holder.add_IB);

                holder.delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;
                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = img_uri.remove(position).getUrl();
                        for (int i = 0; i < img_uri.size(); i++) {
                            if (img_uri.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            img_uri.add(null);
                        }
                        FileUtils.DeleteFolder(img_url);//删除在emulate/0文件夹生成的图片
                        images.remove(position);//这里添加对应的店铺展示图片集合
                        gridImgAdapter.notifyDataSetChanged();
                    }
                });
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        single_photos.clear();
                        for (int i = 0; i < images.size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(images.get(i).ImagePath);
                            single_photos.add(photoModel);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putBoolean("isSave",false);
                        CommonUtils.launchActivity(AddGoodsAcitivyt.this, PhotoPreviewActivity.class, bundle);
                    }
                });
            }
            return convertView;
        }
        class ViewHolder {
            ImageView add_IB;
            ImageView delete_IV;
        }
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
     * 视频展示的适配器
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
//            boolean mode = cb_mode.isChecked();
            if (true) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(AddGoodsAcitivyt.this)
                        .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
//                        .selectionMode(cb_choose_mode.isChecked() ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
//                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
                        .previewImage(true)// 是否可预览图片
//                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
                        .previewVideo(true)// 是否可预览视频
//                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                        .enablePreviewAudio(true) // 是否可播放音频
//                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
//                        .enableCrop(cb_crop.isChecked())// 是否裁剪
                        .enableCrop(true)// 是否裁剪
//                        .compress(cb_compress.isChecked())// 是否压缩
                        .compress(true)// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
//                        .videoSecond()//显示多少秒以内的视频or音频也可适用
                        .recordVideoSecond(14)//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            } else {
                // 单独拍照
//                PictureSelector.create(AddGoodsAcitivyt.this)
//                        .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
//                        .theme(themeId)// 主题样式设置 具体参考 values/styles
//                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
//                        .minSelectNum(1)// 最小选择数量
//                        .selectionMode(cb_choose_mode.isChecked() ?
//                                PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
//                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
//                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
//                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
//                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
//                        .enableCrop(cb_crop.isChecked())// 是否裁剪
//                        .compress(cb_compress.isChecked())// 是否压缩
//                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
//                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
//                        .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
//                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                        .openClickSound(cb_voice.isChecked())// 是否开启点击声音
//                        .selectionMedia(selectList)// 是否传入已选图片
//                        .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
//                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
//                        //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
//                        .minimumCompressSize(100)// 小于100kb的图片不压缩
//                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
//                        //.rotateEnabled() // 裁剪是否可旋转图片
//                        //.scaleEnabled()// 裁剪是否可放大缩小图片
//                        //.videoQuality()// 视频录制质量 0 or 1
                        //.videoSecond()////显示多少秒以内的视频or音频也可适用
//                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        }
    };

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
        builder.setBackTouchable(true);
        builder.show();
    }
}