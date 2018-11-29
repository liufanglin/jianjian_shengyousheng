package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.PopupWindowFromBottomUtil;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.ListBaseMessage;
import com.ximai.savingsmore.save.modle.MyUserExtInfo;
import com.ximai.savingsmore.save.modle.MyUserInfo;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.UpPhoto;
import com.ximai.savingsmore.save.modle.UserParameter;
import com.ximai.savingsmore.save.utils.FileUtils;
import com.ximai.savingsmore.save.utils.ImageTools;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.FavourableCardDialog;
import com.ximai.savingsmore.save.view.GlideRoundTransform;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.SelectPopupWindow;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caojian on 16/11/30.
 */
public class PersonalMyMessageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView head_image;
    private EditText nickname, xiangxi_address, phone, zhiye ,et_wechat;
    private TextView sex, diqu;
    private Images images1;
    private List<String> sex_list = new ArrayList<>();
    private UserParameter userParameter = new UserParameter();
    private List<BaseMessage> list = new ArrayList<BaseMessage>();
    private Button save;
    private MyUserExtInfo myUserExtInfo;
    private SelectPopupWindow menuWindow;

    private static final int REQUEST_CAMERA = 0;//相机请求码
    private static final int REQUESTCODE_PICK = 7;// 相册选图标记
    public static final int REQ_TAKE_PHOTO = 10011;
    private static final int REQUESTCODE_CUTTING = 8;// 图片裁切标记
    private static final int REQUEST_PHOTO = 1;//相册请求码
    public static final int REQ_ZOOM = 102;
    private String urlpath;//图片路劲
    private Uri outputUri;
    private String mName;
    private String imgPath;
    private RelativeLayout rl_sex;
    private RelativeLayout rl_cuxiaocard;
    private FavourableCardDialog dialog;
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_mymessage_activity);

        initView();

        initData();

        initEvent();
    }

    /**
     * view
     */
    private void initView() {
        setCenterTitle("我的中心");
        setLeftBackMenuVisibility(PersonalMyMessageActivity.this, "");
        head_image = (ImageView) findViewById(R.id.head_image);
        nickname = (EditText) findViewById(R.id.nickname);
        xiangxi_address = (EditText) findViewById(R.id.address);
        phone = (EditText) findViewById(R.id.phone);
        sex = (TextView) findViewById(R.id.sex);
        diqu = (TextView) findViewById(R.id.location);
        rl_sex = (RelativeLayout) findViewById(R.id.rl_sex);
        zhiye = (EditText) findViewById(R.id.job);
        et_wechat = (EditText) findViewById(R.id.et_wechat);
        save = (Button) findViewById(R.id.save);
        rl_cuxiaocard = (RelativeLayout) findViewById(R.id.rl_cuxiaocard);//促销优惠卡
//        youxiang = (EditText) findViewById(R.id.email);//邮箱，现在不需要了
    }

    /**
     * data
     */
    private void initData() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                .priority(Priority.HIGH)
                .transform(new GlideRoundTransform(PersonalMyMessageActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

        if (null != MyUserInfoUtils.getInstance().myUserInfo.Email && !TextUtils.isEmpty(MyUserInfoUtils.getInstance().myUserInfo.Email)) {
            //youxiang.setText(MyUserInfoUtils.getInstance().myUserInfo.Email);
//            youxiang.setText("去邮箱查收邮件,并给予确认");
//            youxiang.setText(MyUserInfoUtils.getInstance().myUserInfo.Email);
        }
        String email = MyUserInfoUtils.getInstance().myUserInfo.Email;
        Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.PhotoPath)
                .apply(options).into(head_image);
        userParameter.PhotoPath = MyUserInfoUtils.getInstance().myUserInfo.PhotoPath;
        if (null != MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName && !TextUtils.isEmpty(MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName)) {
            nickname.setText(MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName);
            /**
             * 保存 - 收获地址使用
             */
            PrefUtils.setString(this,"username",MyUserInfoUtils.getInstance().myUserInfo.UserDisplayName);
        }
        if (null != MyUserInfoUtils.getInstance().myUserInfo.Domicile && !TextUtils.isEmpty(MyUserInfoUtils.getInstance().myUserInfo.Domicile)) {
            xiangxi_address.setText(MyUserInfoUtils.getInstance().myUserInfo.Domicile);
            /**
             * 保存地址为了在收货的时候
             */
            PrefUtils.setString(this,"xiangXiAddress",MyUserInfoUtils.getInstance().myUserInfo.Domicile);
        }
        if (null != MyUserInfoUtils.getInstance().myUserInfo.RecipientsTelephone && !TextUtils.isEmpty(MyUserInfoUtils.getInstance().myUserInfo.RecipientsTelephone)) {
            phone.setText(MyUserInfoUtils.getInstance().myUserInfo.RecipientsTelephone);
            /**
             * 收货人电话
             */
            PrefUtils.setString(this,"userphone",MyUserInfoUtils.getInstance().myUserInfo.RecipientsTelephone);
        }
        if (MyUserInfoUtils.getInstance().myUserInfo.Sex.equals("false")) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }
        userParameter.Sex = MyUserInfoUtils.getInstance().myUserInfo.Sex;
        if (null != MyUserInfoUtils.getInstance().myUserInfo.Area) {
            /**
             * 保存城市为了在收货的时候使用
             */
            diqu.setText(MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.City.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.Area.Name);
            PrefUtils.setString(this,"cityAddress",MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.City.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.Area.Name);
        } else if (null != MyUserInfoUtils.getInstance().myUserInfo.Country && null != MyUserInfoUtils.getInstance().myUserInfo.Province && null != MyUserInfoUtils.getInstance().myUserInfo.City) {
            diqu.setText(MyUserInfoUtils.getInstance().myUserInfo.Country.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.City.Name);
            PrefUtils.setString(this,"cityAddress",MyUserInfoUtils.getInstance().myUserInfo.Country.Name + " " +MyUserInfoUtils.getInstance().myUserInfo.Province.Name + " " + MyUserInfoUtils.getInstance().myUserInfo.City.Name);
        }
        userParameter.ProvinceId = MyUserInfoUtils.getInstance().myUserInfo.ProvinceId;
        userParameter.CityId = MyUserInfoUtils.getInstance().myUserInfo.CityId;
        userParameter.AreaId = MyUserInfoUtils.getInstance().myUserInfo.AreaId;
        if (null != MyUserInfoUtils.getInstance().myUserInfo.Post && !TextUtils.isEmpty(MyUserInfoUtils.getInstance().myUserInfo.Post)) {
            zhiye.setText(MyUserInfoUtils.getInstance().myUserInfo.Post);
        }
        /**
         * 微信号
         */
        if (null != MyUserInfoUtils.getInstance().myUserInfo.WeChat){
            et_wechat.setText(MyUserInfoUtils.getInstance().myUserInfo.WeChat);
        }

        sex_list.add("男");
        sex_list.add("女");
        myUserExtInfo = new MyUserExtInfo();
        queryDicNode();
    }

    /**
     * event
     */
    private void initEvent() {
        save.setOnClickListener(this);
        head_image.setOnClickListener(this);
        diqu.setOnClickListener(this);
        rl_sex.setOnClickListener(this);
        rl_cuxiaocard.setOnClickListener(this);
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
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_one://拍照
                    imgPath =  FileUtils.generateImgePathInStoragePath();
                    if (ActivityCompat.checkSelfPermission(PersonalMyMessageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission(); // 权限未被授予
                    }else {
                        openCamera(imgPath);
                    }
                    break;
                case R.id.btn_two://从相册选择
                    if (ContextCompat.checkSelfPermission(PersonalMyMessageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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
            // 指定调用相机拍照后照片的储存路径
            File imgFile = new File(imgPath);
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
     * 进行回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode!=RESULT_OK){
            return;
        }
        File srcFile = null;
        File outPutFile = null;
        Bitmap bm = null;
        File temFile = null;
        switch (requestCode){
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
                    outPutFile = new File(FileUtils.generateImgePathInStoragePath());
                    outputUri = Uri.fromFile(outPutFile);
                    FileUtils.startPhotoZoom(this, srcFile, outPutFile, REQ_ZOOM);// 发起裁剪请求
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case REQ_ZOOM://裁剪后回调
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
                            String scaleImgPath = FileUtils.saveBitmapByQuality(bm, 80);//复制并压缩到自己的目录并压缩
                            //进行上传，上传成功后显示新图片,这里不演示上传的逻辑，上传只需将scaleImgPath路径下的文件上传即可。
                            //     ivPhoto.setImageBitmap(bm);//显示在iv上
                            RequestOptions options = new RequestOptions()
                                    .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                                    .priority(Priority.HIGH)
                                    .transform(new GlideRoundTransform(PersonalMyMessageActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(this).load("file://" + scaleImgPath)
                                    .apply(options).into(head_image);

                            upLoadImage(new File(scaleImgPath), "Photo");
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
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            // 取得SDCard图片路径做显示
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(null, photo);
            mName = new SimpleDateFormat("yyyy-MM-dd-hh-MM-dd_hh:mm:ss").format(new Date());
            String path = Environment.getExternalStorageDirectory() + "/parkbox";
            urlpath = UIUtils.savePhotoToSDCard(photo,path, mName);
            //  me_icon.setImageDrawable(drawable);     //将照相的图片直接设置到图标上面--
            upLoadImage(new File(urlpath), "Photo");                                  //上传
        }
    }

    /**
     * 拍照或者从相册选择
     * @param file
     * @param type
     */
    private void upLoadImage(File file, final String type) {
        WebRequestHelper.post(URLText.UPLOAD_IMAGE, RequestParamsPool.upLoad(file, type), new MyAsyncHttpResponseHandler(PersonalMyMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    UpPhoto upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                    images1 = new Images();
                    images1.ImageId = upPhoto.Id;
                    images1.ImagePath = upPhoto.FilePath;
                    images1.SortNo = upPhoto.SortNo;
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                            .priority(Priority.HIGH)
                            .transform(new GlideRoundTransform(PersonalMyMessageActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

                    Glide.with(PersonalMyMessageActivity.this).load(URLText.img_url + upPhoto.FilePath)
                            .apply(options).into(head_image);
                    userParameter.PhotoId = upPhoto.Id;
                    userParameter.PhotoPath = upPhoto.FilePath;
                } catch (JSONException e) {
                    e.printStackTrace();
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
            case R.id.location:
                PopupWindowFromBottomUtil.showAddress(PersonalMyMessageActivity.this, LayoutInflater.from(PersonalMyMessageActivity.this).inflate(R.layout.business_my_center_activity, null), list, new PopupWindowFromBottomUtil.Listenre1() {
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
                        diqu.setText(content);
                        popupWindow.dismiss();
                    }

                });
                break;
            case R.id.rl_sex://性别
                PopupWindowFromBottomUtil.shouWindowWithWheel(PersonalMyMessageActivity.this, LayoutInflater.from(PersonalMyMessageActivity.this).inflate(R.layout.business_my_center_activity, null), sex_list, new PopupWindowFromBottomUtil.Listener() {
                    @Override
                    public void confirm(String content, PopupWindow window) {
                        sex.setText(content);
                        if (content.equals("男")) {
                            userParameter.Sex = "false";
                        } else {
                            userParameter.Sex = "true";
                        }
                        window.dismiss();
                    }
                });
                break;
            case R.id.save:
                /**
                 * 地区
                 */
                if ("请您选择".equals(diqu.getText().toString())){
                    Toast.makeText(this, "请选择收货人所在城市", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    PrefUtils.setString(this,"cityAddress",xiangxi_address.getText().toString());
                }

                /**
                 * 保存地址信息 - 详细
                 */
                if (TextUtils.isEmpty(xiangxi_address.getText().toString())){
                    Toast.makeText(this, "请填写收货详细地址", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    PrefUtils.setString(this,"xiangXiAddress",xiangxi_address.getText().toString());
                }

                /**
                 * 收货人电话
                 */
                if (TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(this, "请填写收货人联系电话", Toast.LENGTH_SHORT).show();
                    return;
                }

                /**
                 * 收货人电话
                 */
                PrefUtils.setString(this,"userphone",phone.getText().toString());

                /**
                 * 姓名 - 收获地址使用
                 */
                PrefUtils.setString(this,"username",nickname.getText().toString());

                userParameter.UserDisplayName = nickname.getText().toString();
                userParameter.NickName = nickname.getText().toString();
                userParameter.Domicile = xiangxi_address.getText().toString();
                userParameter.Post = zhiye.getText().toString();
                userParameter.WeChat = et_wechat.getText().toString();
                userParameter.RecipientsTelephone = phone.getText().toString();
                userParameter.UserExtInfo = myUserExtInfo;

                //邮箱
//                if (null == MyUserInfoUtils.getInstance().myUserInfo.Email && null != youxiang.getText() && !TextUtils.isEmpty(youxiang.getText())) {
//                    bindEmail(youxiang.getText().toString(), PreferencesUtils.getString(BaseApplication.getInstance(), "pwd", null));
//                }
                save_message(userParameter);
                break;
            case R.id.head_image://头像的点击
                showSetIconWindow();
                break;
            case R.id.rl_cuxiaocard://促销优惠卡
                dialog = new FavourableCardDialog(this);

                if (TextUtils.isEmpty(phone.getText())){
                    dialog.setPhone("暂无");
                }else{
                    dialog.setPhone(phone.getText().toString());
                }

                if (TextUtils.isEmpty(nickname.getText())){
                    dialog.setName("暂无");
                }else{
                    dialog.setName(nickname.getText().toString());
                }

                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = (int)(display.getWidth()); //设置宽度
                dialog.getWindow().setBackgroundDrawable(null);//将原有的dialog背景消除
                dialog.getWindow().setAttributes(lp);
                break;
                default:
                    break;
        }
    }

    /**
     * 保存个人信息
     * @param userParameter
     */
    private void save_message(UserParameter userParameter) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(PersonalMyMessageActivity.this, URLText.SAVE_MESSAGE, RequestParamsPool.savePersonMessage(userParameter), new MyAsyncHttpResponseHandler(PersonalMyMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String IsSuccess = object.getString("IsSuccess");
                    String message = object.optString("Message");
                    if (IsSuccess.equals("true")) {
                        getUsereInfo();

                        /**
                         * 下单页面地址需要 - 将个人信息和地址进行一个保存的操作
                         */
                        PrefUtils.setString(PersonalMyMessageActivity.this, "cityAddress",diqu.getText().toString()+xiangxi_address.getText().toString());
                        PrefUtils.setString(PersonalMyMessageActivity.this, "userphone",phone.getText().toString());
                        PrefUtils.setString(PersonalMyMessageActivity.this, "username", nickname.getText().toString());
                    }
                    Toast.makeText(PersonalMyMessageActivity.this, message, Toast.LENGTH_SHORT).show();
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

    //得到用户的信息
    private void getUsereInfo() {
        WebRequestHelper.json_post(PersonalMyMessageActivity.this, URLText.GET_USERINFO, RequestParamsPool.getUserInfo(), new MyAsyncHttpResponseHandler(PersonalMyMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String MianData = object.optString("MainData");
                MyUserInfoUtils.getInstance().myUserInfo = GsonUtils.fromJson(MianData, MyUserInfo.class);
                finish();
            }
        });

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
        WebRequestHelper.json_post(PersonalMyMessageActivity.this, URLText.QUERYDICNODE, stringEntity, new MyAsyncHttpResponseHandler(PersonalMyMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                ListBaseMessage listBaseMessage = GsonUtils.fromJson(new String(responseBody), ListBaseMessage.class);
                list = listBaseMessage.MainData;
            }
        });
    }

    /**
     * 绑定邮箱
     * @param email
     * @param password
     */
//    private void bindEmail(String email, String password) {
//        WebRequestHelper.json_post(PersonalMyMessageActivity.this, URLText.bindemail, RequestParamsPool.bindEmail(email, password), new MyAsyncHttpResponseHandler(PersonalMyMessageActivity.this) {
//            @Override
//            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
//                String result = new String(responseBody);
//                JSONObject object = null;
//                try {
//                    object = new JSONObject(result);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                if (object.optString("IsSuccess").equals("true")) {
//                    youxiang.setText("去邮箱查收邮件,并给予确认");
//                    //Toast.makeText(PersonalMyMessageActivity.this, "去邮箱查收邮件,并给予确认", Toast.LENGTH_SHORT).show();
//                }
//                Toast.makeText(PersonalMyMessageActivity.this, object.optString("Message"), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
//                super.onFailure(arg0, arg1, arg2, arg3);
//            }
//        });
//    }

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