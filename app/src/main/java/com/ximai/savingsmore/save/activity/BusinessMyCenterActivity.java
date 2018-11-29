package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.ximai.savingsmore.library.toolbox.UsePicker;
import com.ximai.savingsmore.save.adapter.GridImageAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.BusinessScopes;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.ListBaseMessage;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyDictNode;
import com.ximai.savingsmore.save.modle.MyUserExtInfo;
import com.ximai.savingsmore.save.modle.MyUserInfo;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.UpPhoto;
import com.ximai.savingsmore.save.modle.UploadGoodsBean;
import com.ximai.savingsmore.save.modle.UserParameter;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.ImageTools;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.FullyGridLayoutManager;
import com.ximai.savingsmore.save.view.GlideRoundTransform;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.MyGridView;
import com.ximai.savingsmore.save.view.SelectPopupWindow;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.ximai.savingsmore.save.view.XiMaiPopDialog1;
import com.ximai.savingsmore.save.view.imagepicker.PhotoPreviewActivity;
import com.ximai.savingsmore.save.view.imagepicker.PhotoSelectorActivity;
import com.ximai.savingsmore.save.view.imagepicker.model.PhotoModel;
import com.ximai.savingsmore.save.view.imagepicker.util.CommonUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.Config;
import com.ximai.savingsmore.save.view.imagepicker.util.DbTOPxUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.FileUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by caojian on 16/12/7.
 * 商家的我的中心
 */
public class BusinessMyCenterActivity extends BaseActivity implements View.OnClickListener {
    List<String> list_type = new ArrayList<>();
    List<BaseMessage> businessDateList = new ArrayList<>();
    private ImageView liscense_image, zhegnshu_image;
    private MyGridView my_goods_GV;
    private ImageView head_image;
    private EditText xixiang_adress, website,store_name, phone_number,weChat, lianxiren, position, xiliren_number;
    private TextView apply_time, pizhun_time, name;
    private List<BaseMessage> list = new ArrayList<BaseMessage>();
    private List<BaseMessage> base = new ArrayList<BaseMessage>();
    private List<BaseMessage> good_one_classify = new ArrayList<BaseMessage>();
    private List<BaseMessage> good_two_classify = new ArrayList<BaseMessage>();
    private String dizhi = "";
    private List<Images> images = new ArrayList<>();//用来存储图片 -
    private UserParameter userParameter;
    private MyUserExtInfo myUserExtInfo;
    private MyDictNode myDictNode;
    private BusinessScopes myBusinessScopes;
    private String tuoxiang_path;
    private Button save_message;
    private List<BusinessScopes> list1;
    private List<Images> list_images;
    int a = 0, b = 0;
    private int screen_widthOffset;
    private ArrayList<UploadGoodsBean> img_uri = new ArrayList<>();//方格图片展示的图片数据
    private GridImgAdapter gridImgAdapter;
    private List<PhotoModel> single_photos = new ArrayList<>();

    private SelectPopupWindow menuWindow;
    private String imgPath;
    private static final int REQUEST_CAMERA = 0;//相机请求码
    private static final int REQUESTCODE_PICK = 7;// 相册选图标记
    public static final int REQ_TAKE_PHOTO = 10011;
    private static final int REQUESTCODE_CUTTING = 8;// 图片裁切标记
    private static final int REQUEST_PHOTO = 1;//相册请求码
    public static final int REQ_ZOOM = 102;
    private Uri outputUri;
    private String urlpath;//图片路劲
    private String mName;
    private int choosePhoto = 1;//定义哪里的选择照片   1：头像  2：商家营业执照  3：经营许可证
    private RelativeLayout rl_city;
    private TextView tv_addtess;
    private RelativeLayout rl_startyingye;
    private TextView tv_startyingye;
    private RelativeLayout rl_endyingye;
    private TextView tv_endyingye;
    private RelativeLayout rl_cltime;
    private TextView tv_cltime;
    private RelativeLayout rl_goodstype;
    private TextView tv_goodstype;
    private RelativeLayout rl_rang;
    private TextView tv_rang;
    private TextView tv_businessdate;
    private RelativeLayout rl_businessdata;
    private RelativeLayout rl_fapiao;
    private TextView tv_fapiao;
    private RelativeLayout rl_songhuofw;
    private TextView tv_songhuofw;
    private RelativeLayout rl_songhuobx;
    private TextView tv_songhuobx;
    private boolean onePhoto = false;//用来判断图片是否上传

    private List<BaseMessage> fapiao_list = new ArrayList<>();//商品发票
    private List<BaseMessage> shfw_list = new ArrayList<>();//送货服务
    private List<BaseMessage> shbx_list = new ArrayList<>();//送货保险
    private EditText et_mendianfali;

    private RecyclerView recycler_movie;
    private GridImageAdapter adapter;
    private List<LocalMedia> selectList = new ArrayList<>();//视频方面
    private int maxSelectNum = 1;//视频方面
    private ProgressBar id_progress;
    private UpPhoto upPhoto;
    private ImageView zxing_image;
    private boolean isFirst = false;
    private KyLoadingBuilder builder;
    private EditText et_zhaoping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_my_center_activity);
        /**
         * view
         */
        initView();
        /**
         * data
         */
        initData();
        /**
         * event
         */
        initEvent();
        /**
         * 查询数据
         */
        queryDicNode();
    }

    /**
     * view
     */
    private void initView() {
        setLeftBackMenuVisibility(BusinessMyCenterActivity.this, "");
        setCenterTitle(getIntent().getStringExtra("title"));

        my_goods_GV = (MyGridView) findViewById(R.id.myGridview);
        head_image = (ImageView) findViewById(R.id.head_image);
        name = (TextView) findViewById(R.id.name);
        store_name = (EditText) findViewById(R.id.store_name);
        xixiang_adress = (EditText) findViewById(R.id.xiangxi_adress);
        website = (EditText) findViewById(R.id.website);//商家公众号
        phone_number = (EditText) findViewById(R.id.phone_number);
        weChat = (EditText) findViewById(R.id.wechat);
        liscense_image = (ImageView) findViewById(R.id.liscense_image);
        zhegnshu_image = (ImageView) findViewById(R.id.zhegnshu_image);
        lianxiren = (EditText) findViewById(R.id.linkman_name);
        position = (EditText) findViewById(R.id.linkman_position);
        xiliren_number = (EditText) findViewById(R.id.linkman_number);
        apply_time = (TextView) findViewById(R.id.apply_time);
        pizhun_time = (TextView) findViewById(R.id.pizhun_time);
        save_message = (Button) findViewById(R.id.submit);
        rl_city = (RelativeLayout) findViewById(R.id.rl_city);//促销所在城市
        tv_addtess = (TextView) findViewById(R.id.tv_addtess);
        rl_startyingye = (RelativeLayout) findViewById(R.id.rl_startyingye);//商家开始营业日期
        tv_startyingye = (TextView) findViewById(R.id.tv_startyingye);
        rl_endyingye = (RelativeLayout) findViewById(R.id.rl_endyingye);//商家结束营业日期
        tv_endyingye = (TextView) findViewById(R.id.tv_endyingye);
        rl_cltime = (RelativeLayout) findViewById(R.id.rl_cltime);//商家成立日期
        tv_cltime = (TextView) findViewById(R.id.tv_cltime);
        rl_goodstype = (RelativeLayout) findViewById(R.id.rl_goodstype);//商品类别
        tv_goodstype = (TextView) findViewById(R.id.tv_goodstype);
        rl_rang = (RelativeLayout) findViewById(R.id.rl_rang);//主营商品
        tv_rang = (TextView) findViewById(R.id.tv_rang);
        rl_businessdata = (RelativeLayout) findViewById(R.id.rl_businessdata);
        tv_businessdate = (TextView) findViewById(R.id.tv_businessdate);//商家营业日期

        rl_fapiao = (RelativeLayout) findViewById(R.id.rl_fapiao);//商品发票
        tv_fapiao = (TextView) findViewById(R.id.tv_fapiao);
        rl_songhuofw = (RelativeLayout) findViewById(R.id.rl_songhuofw);//送货服务
        tv_songhuofw = (TextView) findViewById(R.id.tv_songhuofw);
        rl_songhuobx = (RelativeLayout) findViewById(R.id.rl_songhuobx);//送货保险
        tv_songhuobx = (TextView) findViewById(R.id.tv_songhuobx);
        et_mendianfali = (EditText) findViewById(R.id.et_mendianfali);//门店返利

        recycler_movie = (RecyclerView) findViewById(R.id.recycler_movie);//视频选择
        id_progress = (ProgressBar) findViewById(R.id.id_progress);

        zxing_image = (ImageView) findViewById(R.id.zxing_image);//商家二维码
        et_zhaoping = (EditText) findViewById(R.id.et_zhaoping);//招聘兼职转发促销
    }

    /**
     * data
     */
    private void initData() {
        //默认门店促销返利是不可以输入点击时候弹框提醒
        et_mendianfali.setCursorVisible(false);
        et_mendianfali.setFocusable(false);
        et_mendianfali.setFocusableInTouchMode(false);


        list_type.add("产品类商品");
        list_type.add("服务类商品");//商品类别中的数据
        list_images = new ArrayList<>();
        gridImgAdapter = new GridImgAdapter();//商家店铺照片

        /**
         * 将数据存储
         */
        userParameter = new UserParameter();
        myUserExtInfo = new MyUserExtInfo();
        myDictNode = new MyDictNode();
        myBusinessScopes = new BusinessScopes();
        list1 = new ArrayList<>();


        /**
         * 视频适配器
         */
        FullyGridLayoutManager manager = new FullyGridLayoutManager(BusinessMyCenterActivity.this, 5, GridLayoutManager.VERTICAL, false);
        recycler_movie.setLayoutManager(manager);
        adapter = new GridImageAdapter(BusinessMyCenterActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recycler_movie.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (2) {
                        case 2:
                            // 预览视频
                            PictureSelector.create(BusinessMyCenterActivity.this).externalPictureVideo(media.getPath());
                            break;
                    }
                }
            }
        });

        try{
            /**
             * 这个是图片的数据源 - 店铺图片
             */
            if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo && null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.Images) {
                images = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.Images;
                if (images != null){
                    for(int i = 0; i < images.size();i++){//将数据添加到目前方格展示的集合中
                        String imagePath = images.get(i).ImagePath;
                        UploadGoodsBean uploadGoodsBean = new UploadGoodsBean();
                        uploadGoodsBean.setUrl(URLText.img_url + imagePath);
                        img_uri.add(uploadGoodsBean);
                    }
                }
                gridImgAdapter.notifyDataSetChanged();
            }

            /**
             * 头像
             */
            tuoxiang_path = MyUserInfoUtils.getInstance().myUserInfo.PhotoPath;

            RequestOptions options = new RequestOptions()
                    .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH)
                    .transform(new GlideRoundTransform(BusinessMyCenterActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.PhotoPath)
                    .apply(options).into(head_image);
            /**
             * 微信图片 - 换乘商家店铺网址
             */
//            if (null != MyUserInfoUtils.getInstance().myUserInfo.WeChatImagePath && !TextUtils.isEmpty(MyUserInfoUtils.getInstance().myUserInfo.WeChatImagePath)){
//                Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.WeChatImagePath)
//                        .apply(options).into(zxing_image);
//            }

            /**
             * 店铺名称
             */
//            name.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
            /**
             * 商家促销名称
             */
//            store_name.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);

            /**
             * 店铺名称 - 目前设置的是商家注册第四步默认的设置到他的手机号码
             */
            if (11 == MyUserInfoUtils.getInstance().myUserInfo.ShowName.length() && "1".equals(MyUserInfoUtils.getInstance().myUserInfo.ShowName.substring(0,1))){
                xiliren_number.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
                phone_number.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
            }else{
                //不是手机号码
                name.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
                store_name.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
            }

            if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo) {

                /**
                 * 显示视频数据 - 注意这里的视频要添加全部路劲
                 */
                if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.VideoPath){
                    LocalMedia localMedia = new LocalMedia();
                    localMedia.setPath(URLText.img_url+MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.VideoPath);
                    selectList.add(localMedia);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                }

                /**
                 * 商家营业执照
                 */
                Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.BusinessLicensePath)
                        .apply(options).into(liscense_image);
                /**
                 * 如果有营业执照那就可以直接上传
                 */
                onePhoto = true;
                /**
                 * 商家许可证
                 */
                Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.LicenseKeyPath)
                        .apply(options).into(zhegnshu_image);
                /**
                 * 商家二维码
                 */
//                Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.LicenseKeyPath)
//                        .apply(options).into(zhegnshu_image);
                /**
                 * 商品类别
                 */
                if (MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.IsBag) {
                    tv_goodstype.setText("产品类商品");
                } else {
                    tv_goodstype.setText("服务类商品");
                }
                //网站
                if (TextUtils.isEmpty(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.WebSite)){
                    website.setHint("输入店铺网址");
                }else{
                    website.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.WebSite);
                }
                //如果是为空，那么就将注册的手机号码设置默认
                phone_number.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.OfficePhone);//商家负责人电话

                tv_startyingye.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.StartHours);
                tv_endyingye.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.EndHours);
                try{
                    if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.BusinessDate) {//商家营业日期
                        tv_businessdate.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.BusinessDate.Name);//商家营业日期
                    }
                    if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.FirstClass) {//主营商品
                        tv_rang.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.FirstClass.Name);//主营商品
                    }
                    tv_fapiao.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.Invoice.Name);//商品发票
                    tv_songhuofw.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.DeliveryService.Name);//送货服务
                    tv_songhuobx.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.Premium.Name);//送货保险
                    if (null == MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.RebatePercent){
                        et_mendianfali.setText(0.00+"");//门店消费返利
                    }else{
                        String rebatePercent = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.RebatePercent;
                        double v1 = Double.parseDouble(rebatePercent);
                        float v = (float) (v1 * 100);
//                        int a = (int) v;
                        et_mendianfali.setText(v+"");//门店消费返利
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            if (null != MyUserInfoUtils.getInstance().myUserInfo.Country) {
                dizhi = dizhi + MyUserInfoUtils.getInstance().myUserInfo.Country.Name + " ";
            }
            if (null != MyUserInfoUtils.getInstance().myUserInfo.Province) {
                dizhi = dizhi + MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " ";
            }
            if (null != MyUserInfoUtils.getInstance().myUserInfo.City) {
                dizhi = dizhi + MyUserInfoUtils.getInstance().myUserInfo.City.Name + " ";
            }
            if (null != MyUserInfoUtils.getInstance().myUserInfo.Area) {
                dizhi = dizhi + MyUserInfoUtils.getInstance().myUserInfo.Area.Name;
            }
            tv_addtess.setText(dizhi);//设置城市地址
            xixiang_adress.setText(MyUserInfoUtils.getInstance().myUserInfo.Domicile);
            weChat.setText(MyUserInfoUtils.getInstance().myUserInfo.WeChat);
            tv_cltime.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.FoundingDateName);
            apply_time.setText(MyUserInfoUtils.getInstance().myUserInfo.CreateDateName);
            pizhun_time.setText(MyUserInfoUtils.getInstance().myUserInfo.ApprovalDateName);
            lianxiren.setText(MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName);
            if (null == MyUserInfoUtils.getInstance().myUserInfo.Post || "".equals(MyUserInfoUtils.getInstance().myUserInfo.Post)){
                position.setText("门店导购");//职位
            }else{
                position.setText(MyUserInfoUtils.getInstance().myUserInfo.Post);//职位
            }

            //促销联系人号码 - 如果空那么就将注册的手机号码进行设置
            xiliren_number.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.PhoneNumber);//促销人联系手机号码目前没有
            if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.SharedRedPack){
                et_zhaoping.setText(MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.SharedRedPack);
            }
            /**
             * 将数据存储
             */
            userParameter = new UserParameter();
            myUserExtInfo = new MyUserExtInfo();
            myDictNode = new MyDictNode();
            myBusinessScopes = new BusinessScopes();
            list1 = new ArrayList<>();


            if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo) {//这里是为了第二次更改数据的时候还需要这些数据一起上传
                myUserExtInfo.StartHours = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.StartHours;
                myUserExtInfo.EndHours = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.EndHours;
                myUserExtInfo.BusinessLicensePath = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.BusinessLicensePath;
                myUserExtInfo.BusinessLicenseId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.BusinessLicenseId;
                myUserExtInfo.LicenseKeyPath = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.LicenseKeyPath;
                myUserExtInfo.LicenseKeyId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.LicenseKeyId;
                myUserExtInfo.FoundingDate = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.FoundingDate;
                myUserExtInfo.FoundingDateName = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.FoundingDateName;
                myUserExtInfo.FirstClassId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.FirstClassId;
                myUserExtInfo.IsBag = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.IsBag;
                myUserExtInfo.BusinessDateId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.BusinessDateId;//商家营业日期
//                myUserExtInfo.IsRebate = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.IsRebate;//非促销品返利
                myUserExtInfo.RebatePercent = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.RebatePercent;//门店消费返利
                myUserExtInfo.InvoiceId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.InvoiceId;//非促销品返利
                myUserExtInfo.DeliveryServiceId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.DeliveryServiceId;//非促销品返利
                myUserExtInfo.PremiumId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.PremiumId;//非促销品返利
                myUserExtInfo.SharedRedPack = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.SharedRedPack;//招聘兼职转发促销
                if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.VideoId){
                    myUserExtInfo.VideoId = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.VideoId;//视频Id
                }
                if (null != MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.VideoPath){
                    myUserExtInfo.VideoPath = MyUserInfoUtils.getInstance().myUserInfo.UserExtInfo.VideoPath;//视频路径
                }
            }
            //userParameter.UserExtInfo=myUserExtInfo;
            if (null != MyUserInfoUtils.getInstance().myUserInfo.BusinessScopes && MyUserInfoUtils.getInstance().myUserInfo.BusinessScopes.size() > 0) {
                myBusinessScopes.DictNodeId = MyUserInfoUtils.getInstance().myUserInfo.BusinessScopes.get(0).DictNodeId;
            }
            userParameter.ProvinceId = MyUserInfoUtils.getInstance().myUserInfo.ProvinceId;
            userParameter.CityId = MyUserInfoUtils.getInstance().myUserInfo.CityId;
            userParameter.AreaId = MyUserInfoUtils.getInstance().myUserInfo.AreaId;
            if (null != MyUserInfoUtils.getInstance().myUserInfo.WeChatImageId && null != MyUserInfoUtils.getInstance().myUserInfo.WeChatImagePath){//将数据保存
                userParameter.WeChatImageId = MyUserInfoUtils.getInstance().myUserInfo.WeChatImageId;
                userParameter.WeChatImagePath = MyUserInfoUtils.getInstance().myUserInfo.WeChatImagePath;
            }
            // userParameter.BusinessScopes = MyUserInfoUtils.getInstance().myUserInfo.BusinessScopes;

        }catch (Exception e){
            e.printStackTrace();
        }
        /**
         * grdview加载
         */
        Config.ScreenMap = Config.getScreenSize(this, this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screen_widthOffset = (display.getWidth() - (5* DbTOPxUtils.dip2px(this, 2)))/5;
        my_goods_GV.setAdapter(gridImgAdapter);//店铺照片
        img_uri.add(null);
        gridImgAdapter.notifyDataSetChanged();
    }

    /**
     * event
     */
    private void initEvent() {
        rl_city.setOnClickListener(this);
        rl_cltime.setOnClickListener(this);
        rl_goodstype.setOnClickListener(this);
        rl_rang.setOnClickListener(this);
        liscense_image.setOnClickListener(this);
//        zxing_image.setOnClickListener(this);
        zhegnshu_image.setOnClickListener(this);
        rl_startyingye.setOnClickListener(this);
        rl_endyingye.setOnClickListener(this);
        save_message.setOnClickListener(this);
        head_image.setOnClickListener(this);
        et_mendianfali.setOnClickListener(this);//门店消费返利
        rl_businessdata.setOnClickListener(this);//商家营业日期
        rl_fapiao.setOnClickListener(this);//商品发票
        rl_songhuofw.setOnClickListener(this);//送货服务
        rl_songhuobx.setOnClickListener(this);//送货保险
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                if (img_uri.size() == 0 || img_uri.size() == 1){
                    Toast.makeText(this, "请上传店铺照片", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (store_name.getText() == null || TextUtils.isEmpty(store_name.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请填写店铺名称", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_goodstype.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请选择商品类别", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_rang.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请选择主营商品", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_addtess.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请填写店铺所在城市", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (xixiang_adress.getText() == null || TextUtils.isEmpty(xixiang_adress.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请填写店铺详细地址", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_businessdate.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请选择商家营业日期", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_startyingye.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请选择营业开始时间", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_endyingye.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请选择营业结束时间", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_fapiao.getText())){//商品发票
                    Toast.makeText(this, "请选择商品发票", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_songhuofw.getText())){//送货服务
                    Toast.makeText(this, "请选择送货服务", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_songhuobx.getText())){//送货保险
                    Toast.makeText(this, "请选择送货保险", Toast.LENGTH_SHORT).show();
                    break;
                }

                //门店消费返利
                if (et_mendianfali.getText() == null || TextUtils.isEmpty(et_mendianfali.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请输入门店消费返利", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (phone_number.getText() == null || TextUtils.isEmpty(phone_number.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请填商家负责人电话", Toast.LENGTH_SHORT).show();
                    break;
                }

                if ("请选择".equals(tv_cltime.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请选择商家成立日期", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (onePhoto == false){
                    Toast.makeText(this, "请上传营业执照图片", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (true == et_mendianfali.getText().toString().contains("%")){
                    try{
                        String s = et_mendianfali.getText().toString();
                        String substring = s.substring(0, et_mendianfali.getText().toString().length() - 1);
                        if ( 0 <= Double.valueOf(substring).intValue() && Double.valueOf(substring).intValue() <=100){
                            double v1 = Double.parseDouble(substring) / (double)100;
                            myUserExtInfo.RebatePercent = String.valueOf(v1);
                        }else{
                            Toast.makeText(this, "门店消费返利范围0-100", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, "数据输入有误", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    try{
                        String s = et_mendianfali.getText().toString();
                        if ( 0 <= Double.valueOf(s).intValue() && Double.valueOf(s).intValue() <=100){
                            double v1 = Double.parseDouble(s) / (double)100;
                            myUserExtInfo.RebatePercent = String.valueOf(v1);
                        }else{
                            Toast.makeText(this, "门店消费返利范围0-100", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        Toast.makeText(this, "数据输入有误", Toast.LENGTH_SHORT).show();
                    }
                }


                if (lianxiren.getText() == null || TextUtils.isEmpty(lianxiren.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请填写联系人姓名", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (xiliren_number.getText() == null || TextUtils.isEmpty(xiliren_number.getText())) {
                    Toast.makeText(BusinessMyCenterActivity.this, "请输入联系人手机号", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (!TextUtils.isEmpty(position.getText())){//-----------------------
                    userParameter.Post = position.getText().toString();
                }

                if (!TextUtils.isEmpty(weChat.getText())){//-------------------------------------
                    userParameter.WeChat = weChat.getText().toString();//邮箱
                }

                if (!TextUtils.isEmpty(website.getText())){//-------------------------------------
                    myUserExtInfo.WebSite = website.getText().toString();//网站
                }

                if (!TextUtils.isEmpty(et_zhaoping.getText())){//招聘兼职转发促销
                    myUserExtInfo.SharedRedPack = et_zhaoping.getText().toString();//招聘兼职转发促销
                }

                list_images.addAll(images);//更新图片信息
                myUserExtInfo.Images = list_images;//将添加的照片添加到-----------九宫格图片

                userParameter.Domicile = xixiang_adress.getText().toString();//详细地址
                myUserExtInfo.StoreName = store_name.getText().toString();//店铺名称

                myUserExtInfo.OfficePhone = phone_number.getText().toString();//固定电话
                myUserExtInfo.PhoneNumber = xiliren_number.getText().toString();//促销联系人手机
                userParameter.UserDisplayName = lianxiren.getText().toString();//姓名

                if (0 < selectList.size()){
                    if (null != upPhoto){
                        myUserExtInfo.VideoId = upPhoto.Id;//Id
                        myUserExtInfo.VideoPath = upPhoto.FilePath;//path
                    }
                }else{//如果视频删除了也包保存上传
                    myUserExtInfo.VideoId = null;//Id
                    myUserExtInfo.VideoPath = null;//path
                }

//                myUserExtInfo.FoundingDate = tv_cltime.getText().toString();//公司成立时间-=-=-=-=-=-=-
//                myUserExtInfo.BusinessDate.Name = tv_businessdate.getText().toString();//设置营业日期数据-=--=-=--=-=-=

                if (tv_goodstype.getText().equals("产品类商品")) {//商品类别
                    myUserExtInfo.IsBag = true;
                } else {
                    myUserExtInfo.IsBag = false;
                }

                list1.add(myBusinessScopes);
                userParameter.BusinessScopes = list1;
                userParameter.UserExtInfo = myUserExtInfo;//将数据进行合并到
//                if (null == MyUserInfoUtils.getInstance().myUserInfo.Email && null != weChat.getText() && !TextUtils.isEmpty(weChat.getText())) {
//                    bindEmail(weChat.getText().toString(), PreferencesUtils.getString(BaseApplication.getInstance(), "pwd", null));
//                }

                if (a == b) {
                    save_message(userParameter);
                }
                break;
            case R.id.rl_businessdata://商家营业日期
                PopupWindowFromBottomUtil.shouRange(BusinessMyCenterActivity.this, LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.business_my_center_activity, null), businessDateList, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        tv_businessdate.setText(content);
                        myUserExtInfo.BusinessDateId= Id1.Id;//保存上传的ID
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.rl_cltime://商家成立日期
                UsePicker.showYearMonthDay(BusinessMyCenterActivity.this, new UsePicker.CallBack() {
                    @Override
                    public void callBack(String time) {
                        tv_cltime.setText(time);
                        myUserExtInfo.FoundingDate = time;
                        myUserExtInfo.FoundingDateName = time;
                    }
                }, tv_cltime.getText().toString());
                break;
            case R.id.rl_startyingye://商家营业开始时间
                UsePicker.showHuors(BusinessMyCenterActivity.this, new UsePicker.CallBack() {
                    @Override
                    public void callBack(String time) {
                        tv_startyingye.setText(time);
                        myUserExtInfo.StartHours = time;
                    }
                }, tv_startyingye.getText().toString());
                break;
            case R.id.rl_endyingye://商家结束营业日期
                UsePicker.showHuors(BusinessMyCenterActivity.this, new UsePicker.CallBack() {
                    @Override
                    public void callBack(String time) {
                        tv_endyingye.setText(time);
                        myUserExtInfo.EndHours = time;
                    }
                }, tv_endyingye.getText().toString());
                break;
            case R.id.head_image://头像的点击
                choosePhoto = 1;
                showSetIconWindow();
                break;
            case R.id.liscense_image://商家营业执照
                choosePhoto = 2;
                showSetIconWindow();
                break;
            case R.id.zhegnshu_image://经营许可证
                choosePhoto = 3;
                showSetIconWindow();
                break;
//            case R.id.zxing_image://商家二维码
//                choosePhoto = 4;
//                showSetIconWindow();
//                break;
            case R.id.rl_goodstype://商品类别
                PopupWindowFromBottomUtil.shouWindowWithWheel(BusinessMyCenterActivity.this, LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.business_my_center_activity, null), list_type, new PopupWindowFromBottomUtil.Listener() {
                    @Override
                    public void confirm(String content, PopupWindow window) {
                        tv_goodstype.setText(content);
                        if (tv_goodstype.getText().equals("产品类商品")) {//商品类别
                            myUserExtInfo.IsBag = true;
                            tv_rang.setText("请选择");
                        } else {
                            myUserExtInfo.IsBag = false;
                            tv_rang.setText("请选择");
                        }
                        window.dismiss();
                    }
                });
                break;
            case R.id.rl_rang://主营商品
                if (tv_goodstype.getText().equals("产品类商品")) {
                    PopupWindowFromBottomUtil.shouRange(BusinessMyCenterActivity.this, LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.business_my_center_activity, null), good_one_classify, new PopupWindowFromBottomUtil.Listener2() {
                        @Override
                        public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                            tv_rang.setText(content);
                            myUserExtInfo.FirstClassId = Id1.Id;//现在改为firstclass是主营商品
                            popupWindow.dismiss();
                        }
                    });
                } else {
                    PopupWindowFromBottomUtil.shouRange(BusinessMyCenterActivity.this, LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.business_my_center_activity, null), good_two_classify, new PopupWindowFromBottomUtil.Listener2() {
                        @Override
                        public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                            tv_rang.setText(content);
                            myUserExtInfo.FirstClassId = Id1.Id;//进行设置数据
                            popupWindow.dismiss();
                        }
                    });
                }
                break;
            case R.id.rl_city://选择城市
                PopupWindowFromBottomUtil.showAddress(BusinessMyCenterActivity.this, LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.business_my_center_activity, null), list, new PopupWindowFromBottomUtil.Listenre1() {
                    @Override
                    public void callBack(String Id,String Id1, String Id2, String Id3, String content, PopupWindow popupWindow) {
                        if (null != Id) {
                            userParameter.CountryId = Id;
                        }
                        if (null != Id1) {
                            userParameter.ProvinceId = Id1;
                        }
                        if (null != Id2) {
                            userParameter.CityId = Id2;
                        }
                        if (null != Id3) {
                            userParameter.AreaId = Id3;
                        }
                        tv_addtess.setText(content);
                        popupWindow.dismiss();
                    }

                });
                break;
            case R.id.rl_fapiao://商品发票
                PopupWindowFromBottomUtil.shouRange(BusinessMyCenterActivity.this, LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.business_my_center_activity, null), fapiao_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        myUserExtInfo.InvoiceId = Id1.Id;
                        tv_fapiao.setText(Id1.Name);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.rl_songhuofw://送货服务
                PopupWindowFromBottomUtil.shouRange(BusinessMyCenterActivity.this, LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.business_my_center_activity, null), shfw_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        myUserExtInfo.DeliveryServiceId= Id1.Id;
                        tv_songhuofw.setText(Id1.Name);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.rl_songhuobx://送货保险
                PopupWindowFromBottomUtil.shouRange(BusinessMyCenterActivity.this, LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.business_my_center_activity, null), shbx_list, new PopupWindowFromBottomUtil.Listener2() {
                    @Override
                    public void callBack(BaseMessage Id1, String content, PopupWindow popupWindow) {
                        myUserExtInfo.PremiumId = Id1.Id;
                        tv_songhuobx.setText(Id1.Name);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.et_mendianfali://门店消费返利
                if (false == isFirst){
                    menDianDialog();
                }
                break;
                default:
                    break;
        }
    }

    /**
     * 点击头像 - 拍照或者照片选择
     */
    private void showSetIconWindow() {
        menuWindow = new SelectPopupWindow(this, itemsOnClick);
        menuWindow.showAtLocation(head_image, "拍照", "从相册选择");
    }
    /**
     * 弹出窗口实现监听类
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_one://拍照
                    imgPath =  com.ximai.savingsmore.save.utils.FileUtils.generateImgePathInStoragePath();
                    if (ActivityCompat.checkSelfPermission(BusinessMyCenterActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission(); // 权限未被授予
                    }else {
                        openCamera(imgPath);
                    }
                    break;
                case R.id.btn_two://从相册选择
                    if (ContextCompat.checkSelfPermission(BusinessMyCenterActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPhotoPermission(); // 权限未被授予
                    }else {
                        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
                        // 如果朋友们要限制上传到服务器的图片类型时可以直接写如："image/jpeg 、 image/png等的类型"
                        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(pickIntent, REQUESTCODE_PICK);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * 申请相机权限
     */
    private void requestCameraPermission() {
        final List<String> permissionsList2 = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsList2.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList2.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionsList2.size() > 0) {
            ActivityCompat.requestPermissions((Activity) this, permissionsList2.toArray(new String[permissionsList2.size()]), REQUEST_CAMERA);
        }
    }
    /**
     * 相册权限
     */
    private void requestPhotoPermission() {
        final List<String> permissionsList2 = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList2.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionsList2.size() > 0) {
            ActivityCompat.requestPermissions((Activity) this, permissionsList2.toArray(new String[permissionsList2.size()]), REQUEST_PHOTO);
        }
    }
    /**
     * 开启摄像机
     */
    private void openCamera(String imgPath) {
        try{
            File imgFile = new File(imgPath);// 指定调用相机拍照后照片的储存路径
            Uri imgUri = null;
            if (Build.VERSION.SDK_INT >= 24) {
                //如果是7.0或以上
                  imgUri = FileProvider.getUriForFile(this, UIUtils.getPackageName() + ".fileprovider", imgFile);
            } else {
                imgUri = Uri.fromFile(imgFile);
            }
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            startActivityForResult(intent, REQ_TAKE_PHOTO);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 裁剪图片方法实现
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // intent.putExtra("circleCrop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUESTCODE_CUTTING);
        // startActivityForResult(Intent.createChooser(intent, "选择工具裁剪高亮区域"),GETCROPIMAGE);
    }
    /**
     * 保存裁剪之后的图片数据
     * @param picdata
     */
    private void setPicToView(Intent picdata) {
        RequestOptions options = new RequestOptions()
                .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(BusinessMyCenterActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data"); // 取得SDCard图片路径做显示
//            Drawable drawable = new BitmapDrawable(null, photo);
            mName = new SimpleDateFormat("yyyy-MM-dd-hh-MM-dd_hh:mm:ss").format(new Date());
            String path = Environment.getExternalStorageDirectory() + "/ximai";
            urlpath = UIUtils.savePhotoToSDCard(photo,path, mName);
            //  me_icon.setImageDrawable(drawable);     //将照相的图片直接设置到图标上面--
            if (choosePhoto == 1){//头像
                Glide.with(this).load("file://" + urlpath).apply(options).into(head_image);
                upLoadImage(new File(urlpath), "Photo");
            }else if (choosePhoto == 2){//商家营业执照
                Glide.with(this).load("file://" + urlpath).apply(options).into(liscense_image);
                upLoadImage(new File(urlpath), "BusinessLicense");
            }else if (choosePhoto == 3){//商家许可证
                Glide.with(this).load("file://" + urlpath).apply(options).into(zhegnshu_image);
                upLoadImage(new File(urlpath), "LicenseKey");
            }else if (choosePhoto == 4){//商家二维码
//                Glide.with(this).load("file://" + urlpath).apply(options).into(zxing_image);
//                upLoadImage(new File(urlpath), "WeChat");
            }
        }
    }

    /**
     * activtiy回调信息
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File srcFile = null;
        File outPutFile = null;
        Bitmap bm = null;
        File temFile = null;
        switch (requestCode){
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
                    if (paths.size() > 0){
                        for (int i = 0; i < paths.size(); i++) {
                            upLoadImage(new File(paths.get(i)),"Seller");
                        }
                    }
                }
                break;
            case REQUESTCODE_PICK:                  // 直接从相册获取
                try {
                    startPhotoZoom(data.getData());
                } catch (NullPointerException e) {
                    e.printStackTrace();            // 用户点击取消操作
                }
                break;
            case REQ_TAKE_PHOTO:                  // 调用相机拍照
                try{
                    srcFile = new File(imgPath);
                    outPutFile = new File(com.ximai.savingsmore.save.utils.FileUtils.generateImgePathInStoragePath());
                    outputUri = Uri.fromFile(outPutFile);
                    com.ximai.savingsmore.save.utils.FileUtils.startPhotoZoom(this, srcFile, outPutFile, REQ_ZOOM);// 发起裁剪请求
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case REQ_ZOOM://相机拍照后的裁剪 - 回调
                try{
                    //  Bundle extras = data.getExtras();
                    if (data != null) {
                        //  bm = extras.getParcelable("data");
                        if (outputUri != null) {
                            bm = ImageTools.decodeUriAsBitmap(outputUri);
                            //如果是拍照的,删除临时文件
                            temFile = new File(imgPath);
                            if (temFile.exists()) {
                                temFile.delete();
                            }
                            String scaleImgPath = com.ximai.savingsmore.save.utils.FileUtils.saveBitmapByQuality(bm, 80);//复制并压缩到自己的目录并压缩
                            //进行上传，上传成功后显示新图片,这里不演示上传的逻辑，上传只需将scaleImgPath路径下的文件上传即可。
                            //     ivPhoto.setImageBitmap(bm);//显示在iv上
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                                    .priority(Priority.HIGH)
                                    .transform(new GlideRoundTransform(BusinessMyCenterActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
                            if (choosePhoto == 1){
                                Glide.with(this).load("file://" + scaleImgPath).apply(options).into(head_image);
                                upLoadImage(new File(scaleImgPath),"Photo");
                            }else if (choosePhoto == 2){
                                Glide.with(this).load("file://" + scaleImgPath).apply(options).into(liscense_image);
                                upLoadImage(new File(scaleImgPath), "BusinessLicense");
                            }else if (choosePhoto == 3){
                                Glide.with(this).load("file://" + scaleImgPath).apply(options).into(zhegnshu_image);
                                upLoadImage(new File(scaleImgPath), "LicenseKey");
                            }else if (choosePhoto == 4){//商家二维码
//                                Glide.with(this).load("file://" + scaleImgPath).apply(options).into(zxing_image);
//                                upLoadImage(new File(urlpath), "WeChat");
                            }
                        }
                    } else {
                        UIUtils.showToast("未选择图片");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case REQUESTCODE_CUTTING:               // 取得裁剪后的图片
                if (data != null) {
                    setPicToView(data);
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
                        params.put("SecurityStamp", LoginUser.getInstance().userInfo.SecurityStamp);
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
        public void onError(Call call, Exception e, int id) {
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
                    Toast.makeText(BusinessMyCenterActivity.this, "视频上传成功", Toast.LENGTH_SHORT).show();
                    upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                }else{
                    Toast.makeText(BusinessMyCenterActivity.this, "视频上传失败", Toast.LENGTH_SHORT).show();
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
     * 方格店铺照片 - 店铺照片
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
            convertView = LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if(convertView!=null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(BusinessMyCenterActivity.this).inflate(R.layout.activity_addstory_img_item,null);
                convertView.setTag(holder);
            }else{
                holder = ( ViewHolder) convertView.getTag();
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
                        .transform(new GlideRoundTransform(BusinessMyCenterActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(BusinessMyCenterActivity.this).load(R.mipmap.achieve_icon_addphoto_default)
                        .apply(options).into(holder.add_IB);
                holder.add_IB.setOnClickListener(new View.OnClickListener() {//选择照片
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(BusinessMyCenterActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 9 - (img_uri.size() - 1));
                        startActivityForResult(intent, 0);
                    }
                });
            } else {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(BusinessMyCenterActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(BusinessMyCenterActivity.this).load(img_uri.get(position).getUrl())
                        .apply(options).into(holder.add_IB);
                holder.delete_IV.setOnClickListener(new View.OnClickListener() {//删除照片
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
                        CommonUtils.launchActivity(BusinessMyCenterActivity.this, PhotoPreviewActivity.class, bundle);
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
     * 查询基础字典
     */
    private void queryDicNode() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(BusinessMyCenterActivity.this, URLText.QUERYDICNODE, stringEntity, new MyAsyncHttpResponseHandler(BusinessMyCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                ListBaseMessage listBaseMessage = GsonUtils.fromJson(new String(responseBody), ListBaseMessage.class);
                list = listBaseMessage.MainData;
                for (int i = 0; i < list.size(); i++) {
                    if (null == list.get(i).ParentId) {
                        base.add(list.get(i));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).IsBag.equals("true") && null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("ba335639-52c2-4e8d-8d2b-faf8ed097418")) {
                        good_one_classify.add(list.get(i));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).IsBag.equals("false") && null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("ba335639-52c2-4e8d-8d2b-faf8ed097418")) {
                        good_two_classify.add(list.get(i));
                    }
                }

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).IsBag.equals("false") && null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("90b90cbb-98ab-4b19-b245-aefe1dd95a54")) {
                        businessDateList.add(list.get(i));
                    }
                }

                //商品发票
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("21a595ce-99f9-4533-a112-b3f21984d231")) {
                        fapiao_list.add(list.get(i));
                    }
                }

                //送货服务
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("f409bd27-9b47-46b5-b854-e7bd0c1a4f67")) {
                        shfw_list.add(list.get(i));
                    }
                }

                //送货服务
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("007d3adf-ceb6-4d17-ba83-1a41bc7d7ed8")) {
                        shbx_list.add(list.get(i));
                    }
                }
            }
        });
    }

    /**
     * 上传照片
     * @param file      /storage/emulated/0/ximai/2017-12-20-11-12-20_11:14:57.png
     * @param type
     */
    private void upLoadImage(File file, final String type) {
        if (type.equals("Photo") || type.equals("BusinessLicense") || type.equals("LicenseKey") || type.equals("WeChat")){
            showLoading(this,"正在加载");
        }
        a++;
        WebRequestHelper.post(URLText.UPLOAD_IMAGE, RequestParamsPool.upLoad(file, type), new MyAsyncHttpResponseHandler(BusinessMyCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    b++;
                    JSONObject jsonObject = new JSONObject(result);
                    UpPhoto upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                    if (type.equals("Photo")) {//头像
                        userParameter.PhotoId = upPhoto.Id;
                        userParameter.PhotoPath = upPhoto.FilePath;
                    } else if (type.equals("BusinessLicense")) {//商家营业执照
                        onePhoto = true;
                        myUserExtInfo.BusinessLicenseId = upPhoto.Id;
                        myUserExtInfo.BusinessLicensePath = upPhoto.FilePath;
                    } else if (type.equals("LicenseKey")) {//经营许可证
                        myUserExtInfo.LicenseKeyId = upPhoto.Id;
                        myUserExtInfo.LicenseKeyPath = upPhoto.FilePath;
                    }else if (type.equals("WeChat")){//微信号
                        userParameter.WeChatImageId = upPhoto.Id;
                        userParameter.WeChatImagePath = upPhoto.FilePath;
                    } else {//店铺多张图片
                        Images images1 = new Images();
                        images1.ImageId = upPhoto.Id;
                        images1.ImagePath = upPhoto.FilePath;
                        images.add(images1);
                        gridImgAdapter.notifyDataSetChanged();
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("tag","上传照片有误 - upLoadImage");

                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }

    /**
     * 保存信息
     * @param userParameter
     */
    private void save_message(UserParameter userParameter) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(BusinessMyCenterActivity.this, URLText.SAVE_MESSAGE, RequestParamsPool.saveMessage(userParameter), new MyAsyncHttpResponseHandler(BusinessMyCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String IsSuccess = object.getString("IsSuccess");
                    String message = object.optString("Message");
                    if (IsSuccess.equals("true")) {
                        getUsereInfo();
//                        Toast.makeText(BusinessMyCenterActivity.this, "您的开通申请已经收到，正在审核中，我们会以短信或邮件告知结果。", Toast.LENGTH_SHORT).show();
                    }
                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }

    /**
     * 得到用户的信息
     */
    private void getUsereInfo() {
        WebRequestHelper.json_post(BusinessMyCenterActivity.this, URLText.GET_USERINFO, RequestParamsPool.getUserInfo(), new MyAsyncHttpResponseHandler(BusinessMyCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject object = new JSONObject(result);
                    String MianData = object.optString("MainData");
                    MyUserInfoUtils.getInstance().myUserInfo = GsonUtils.fromJson(MianData, MyUserInfo.class);
                    if ("2".equals(MyUserInfoUtils.getInstance().myUserInfo.ApprovalState)){//待审核的状态
                        approvalStateDialog();
                    }else{//不是待审核
                        Toast.makeText(BusinessMyCenterActivity.this, "您的信息修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 待审核的弹框提示
     */
    public void approvalStateDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                finish();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(BusinessMyCenterActivity.this, "温馨提示", "您的开通申请已经收到，正在审核中，我们会以短信或邮件告知结果。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 绑定邮箱
     * @param email
     * @param password
     */
    private void bindEmail(String email, String password) {
        WebRequestHelper.json_post(BusinessMyCenterActivity.this, URLText.bindemail, RequestParamsPool.bindEmail(email, password), new MyAsyncHttpResponseHandler(BusinessMyCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (object.optString("IsSuccess").equals("true")) {
                    Toast.makeText(BusinessMyCenterActivity.this, "去邮箱查收邮件,并给予确认", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        });
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
                PictureSelector.create(BusinessMyCenterActivity.this)
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
     * 门店消费返利率
     */
    public void menDianDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                isFirst = true;
                et_mendianfali.setCursorVisible(true);
                et_mendianfali.setFocusable(true);
                et_mendianfali.setFocusableInTouchMode(true);
                et_mendianfali.requestFocus();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog1(BusinessMyCenterActivity.this, "温馨提示", "建议输入3%以上优惠率，吸引客户入店，带动其它消费", "知道了", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
