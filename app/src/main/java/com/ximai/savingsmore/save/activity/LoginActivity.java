package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.mob.tools.utils.UIHandler;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyUserInfo;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.RegisterNumber;
import com.ximai.savingsmore.save.modle.UserInfo;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static android.R.attr.action;
import static com.loopj.android.http.AsyncHttpClient.log;

/**
 * Created by caojian on 16/11/16.
 */
//个人登录
public class LoginActivity extends BaseActivity implements View.OnClickListener, PlatformActionListener, Handler.Callback {
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 0;
    private EditText name;
    private EditText password;
    private Button button;
    private ImageView btnWechat, btnQQ, btnXinlang;
    private TextView register, forgetPassword;
    //用户的类型  2个人 3商家
    private int type = 2;
    private LinearLayout ll_business, ll_personal, ll_choose_user;
    private TextView business_number, good_number, person_number, today_number;
    private String ExternalSigninType;
    private RelativeLayout back;
    private RelativeLayout title＿right;
    private TextView tv_title;
    private TextView tv_msg_one;
    private TextView tv_msg_two;
    private TextView cen_title;
    private static final int MSG_ACTION_CCALLBACK = 0;
    private boolean isRegister;
    private LinearLayout ll_updata;
    private ImageView iv_geren;
    private KyLoadingBuilder builder;
    private LinearLayout ll_login_icon;
    private LinearLayout ll_login_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_login_activity);

        initView();

        initData();

        initEvent();
    }

    /**
     * view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        button = (Button) findViewById(R.id.login);
        register = (TextView) findViewById(R.id.register);
        forgetPassword = (TextView) findViewById(R.id.forget_password);
        btnQQ = (ImageView) findViewById(R.id.account_loginuser__qq);
        btnWechat = (ImageView) findViewById(R.id.account_login_user_wechat);
        btnXinlang = (ImageView) findViewById(R.id.account_login_user_xinlang);
        ll_business = (LinearLayout) findViewById(R.id.ll_business);
        ll_personal = (LinearLayout) findViewById(R.id.ll_personal);
        ll_choose_user = (LinearLayout) findViewById(R.id.ll_choose_user);
        business_number = (TextView) findViewById(R.id.business_number);
        good_number = (TextView) findViewById(R.id.pingzhong_number);
        person_number = (TextView) findViewById(R.id.kuhu_number);
        today_number = (TextView) findViewById(R.id.all_number);
        cen_title = (TextView) findViewById(R.id.cen_title);
        ll_updata = (LinearLayout) findViewById(R.id.ll_updata);
        back = (RelativeLayout) findViewById(R.id.back);//返回键
        title＿right = (RelativeLayout) findViewById(R.id.title＿right);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_msg_one = (TextView) findViewById(R.id.tv_msg_one);
        tv_msg_two = (TextView) findViewById(R.id.tv_msg_two);
        iv_geren = (ImageView) findViewById(R.id.iv_geren);//个人下方显示的
        ll_login_icon = (LinearLayout) findViewById(R.id.ll_login_icon);
        ll_login_msg = (LinearLayout) findViewById(R.id.ll_login_msg);
    }

    /**
     * data
     */
    private void initData() {
        /**
         * 登陆的时候请求定位权限
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }

        if (null != getIntent()) {
            name.setText(getIntent().getStringExtra("phone"));
        }

        /**
         * 判断是商家登录还是个人登录
         */
        String isPeopleAndBusiness = PrefUtils.getString(this, "isPeopleAndBusiness", null);
        if ("2".equals(isPeopleAndBusiness)) {//个人
            cen_title.setText("个人登录");
//            tv_title.setText("商家登录");
            tv_msg_one.setText("商家");
            tv_msg_two.setText("促销");
            register.setText("个人免费注册");
            ll_updata.setVisibility(View.GONE);
            ll_business.setVisibility(View.GONE);
            ll_personal.setVisibility(View.GONE);//数据少而修改
            iv_geren.setVisibility(View.VISIBLE);
            ll_login_icon.setVisibility(View.VISIBLE);
            ll_login_msg.setVisibility(View.VISIBLE);
            PrefUtils.setString(this, "isPeopleAndBusiness", "2");
            type = 2;
        } else if ("3".equals(isPeopleAndBusiness)) {//商家
            cen_title.setText("商家登录");
//            tv_title.setText("个人登录");
            tv_msg_one.setText("个人");
            tv_msg_two.setText("登录");
            register.setText("商家免费注册");
            ll_updata.setVisibility(View.VISIBLE);
            ll_personal.setVisibility(View.VISIBLE);
            ll_business.setVisibility(View.GONE);
            iv_geren.setVisibility(View.GONE);
            PrefUtils.setString(this, "isPeopleAndBusiness", "3");
            ll_login_icon.setVisibility(View.INVISIBLE);
            ll_login_msg.setVisibility(View.INVISIBLE);
            type = 3;
        } else {
//            tv_title.setText("商家登录");
            tv_msg_one.setText("商家");
            tv_msg_two.setText("促销");
            cen_title.setText("个人登录");
            register.setText("个人免费注册");
            iv_geren.setVisibility(View.VISIBLE);
            ll_updata.setVisibility(View.GONE);
            ll_personal.setVisibility(View.GONE);
            ll_business.setVisibility(View.GONE);
            ll_login_icon.setVisibility(View.VISIBLE);
            ll_login_msg.setVisibility(View.VISIBLE);
            /**
             * 默认是个人登录
             */
            PrefUtils.setString(this, "isPeopleAndBusiness", "2");
        }

        getRegisterNUmber();
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        title＿right.setOnClickListener(this);
        btnQQ.setOnClickListener(this);
        btnWechat.setOnClickListener(this);
        btnXinlang.setOnClickListener(this);
        register.setOnClickListener(this);
        ll_choose_user.setOnClickListener(this);
        styleRightTextMenuLayout.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    /**
     * 用户选择允许或拒绝后，会回调onRequestPermissionsResult方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (WRITE_COARSE_LOCATION_REQUEST_CODE == requestCode) {
            log.d("map", "地图权限问题");
        }
    }

    /**
     * 事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                if (!TextUtils.isEmpty(name.getText().toString()) && null != name.getText() && !TextUtils.isEmpty(password.getText().toString()) && null != password.getText()) {
                    //判断用户是否注册
//                    isSignup(name.getText().toString());
                    login(name.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "请您输入用户名和密码", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.register://注册
                register(type);
                break;
            case R.id.forget_password://忘记密码
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class).putExtra("title", "找回密码"));
                break;
            case R.id.title＿right://标题right
                if (type == 3) {
                    cen_title.setText("个人登录");
                    tv_title.setText("商家登录");
                    register.setText("个人免费注册");
                    ll_updata.setVisibility(View.GONE);
                    ll_business.setVisibility(View.GONE);
                    ll_personal.setVisibility(View.GONE);//数据少而修改
                    iv_geren.setVisibility(View.VISIBLE);
                    ll_login_icon.setVisibility(View.VISIBLE);
                    ll_login_msg.setVisibility(View.VISIBLE);
                    type = 2;
                    PrefUtils.setString(this, "isPeopleAndBusiness", "2");
                } else {
                    cen_title.setText("商家登录");
                    tv_title.setText("个人登录");
                    register.setText("商家免费注册");
                    ll_updata.setVisibility(View.VISIBLE);
                    ll_personal.setVisibility(View.VISIBLE);
                    ll_business.setVisibility(View.GONE);
                    iv_geren.setVisibility(View.GONE);
                    ll_login_icon.setVisibility(View.INVISIBLE);
                    ll_login_msg.setVisibility(View.INVISIBLE);
                    type = 3;
                    PrefUtils.setString(this, "isPeopleAndBusiness", "3");
                }
                break;
            case R.id.ll_choose_user:
                if (type == 3) {
                    cen_title.setText("个人登录");
                    tv_msg_one.setText("商家");
                    tv_msg_two.setText("促销");
                    register.setText("个人免费注册");
                    ll_updata.setVisibility(View.GONE);
                    ll_business.setVisibility(View.GONE);
                    ll_login_icon.setVisibility(View.VISIBLE);
                    ll_login_msg.setVisibility(View.VISIBLE);
                    ll_personal.setVisibility(View.GONE);//数据少而修改
                    iv_geren.setVisibility(View.VISIBLE);
                    type = 2;
                    PrefUtils.setString(this, "isPeopleAndBusiness", "2");
                } else {
                    cen_title.setText("商家登录");
                    tv_msg_one.setText("个人");
                    tv_msg_two.setText("登录");
                    register.setText("商家免费注册");
                    ll_login_icon.setVisibility(View.INVISIBLE);
                    ll_login_msg.setVisibility(View.INVISIBLE);
                    ll_updata.setVisibility(View.VISIBLE);
                    ll_personal.setVisibility(View.VISIBLE);
                    ll_business.setVisibility(View.GONE);
                    iv_geren.setVisibility(View.GONE);
                    type = 3;
                    PrefUtils.setString(this, "isPeopleAndBusiness", "3");
                }
                break;
            case R.id.back://返回键
//                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;
            case R.id.account_loginuser__qq://qq登录
                ExternalSigninType = "1";
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(LoginActivity.this);
                qq.SSOSetting(false);
                authorize(qq, 1);
                break;
            case R.id.account_login_user_wechat://微信登录
                ExternalSigninType = "2";
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.SSOSetting(false);
                authorize(wechat, 2);
                break;
            case R.id.account_login_user_xinlang://微博 -- RedirectUri 有问题 http://blog.csdn.net/li438730745/article/details/50680487
                ExternalSigninType = "3";
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                sina.setPlatformActionListener(this);
                sina.SSOSetting(false);
                authorize(sina, 3);
                break;
            default:
                break;
        }
    }

    /**
     * 授权
     */
    private void authorize(Platform plat, int type) {
        if (plat.isValid()) { //如果授权就删除授权资料
            plat.removeAccount();
        }
        plat.showUser(null);//授权并获取用户信息
    }

    /**
     * 自己写的第三方监听
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, LoginActivity.this);   //发送消息
    }

    /**
     * 自己写的第三方监听
     */
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = throwable;
        UIHandler.sendMessage(msg, LoginActivity.this);
    }

    /**
     * 自己写的第三方监听
     */
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, LoginActivity.this);
    }

    /**
     * 登陆发送的handle消息在这里处理
     */
    @Override
    public boolean handleMessage(Message message) {
        switch (message.arg1) {
            case 1: { // 成功
                Toast.makeText(LoginActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();
                //获取用户资料
                Platform platform = (Platform) message.obj;
                String userId = platform.getDb().getUserId();//获取用户账号
                String userName = platform.getDb().getUserName();//获取用户名字
                String userIcon = platform.getDb().getUserIcon();//获取用户头像
                String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
//                Toast.makeText(LoginActivity.this, "用户信息为--用户名：" + userName + "  性别：" + userGender, Toast.LENGTH_SHORT).show();

                PreferencesUtils.putString(BaseApplication.getInstance(), "userId", userId);
                PreferencesUtils.putString(BaseApplication.getInstance(), "userName", userName);
                PreferencesUtils.putString(BaseApplication.getInstance(), "ExternalSigninType", ExternalSigninType);
                PreferencesUtils.putString(BaseApplication.getInstance(), "PushId", JPushInterface.getRegistrationID(LoginActivity.this));
                PreferencesUtils.putInt(BaseApplication.getInstance(), "type", type);

                /**
                 * 保存三方登录的Id
                 */
                if (null != userId) {
                    PreferencesUtils.putString(BaseApplication.getInstance(), "OpenId", userId);
                }
                /**
                 * 将数据绑定上传
                 */
                ThirdLogin(userId, userName, ExternalSigninType, JPushInterface.getRegistrationID(LoginActivity.this), type + "");

//                String account = PreferencesUtils.getString(LoginActivity.this, "account", "");
//                String pwd = PreferencesUtils.getString(LoginActivity.this, "pwd", "");
//                PreferencesUtils.putInt(BaseApplication.getInstance(), "type", type);
//
//                if (null == account || "".equals(account)){
//                    Intent intent = new Intent(LoginActivity.this, OneStepRegisterActivity.class);
//                    intent.putExtra("register_type", type + "");
//                    startActivity(intent);
//                }else{
//                    login(account,pwd);
//                }
            }
            break;
            case 2: { // 失败
                Toast.makeText(LoginActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
            }
            break;
            case 3: { // 取消
                Toast.makeText(LoginActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
            }
            break;
        }
        return false;
    }

    /**
     * 第三方登录信息返回上传到网络
     */
    private void ThirdLogin(String OpenId, String NickName, String ExternalSigninType, String PushId, String UserType) {
        WebRequestHelper.post(URLText.THIRD_LOGIN, RequestParamsPool.thirldLogin(OpenId, NickName, ExternalSigninType, PushId, UserType), new MyAsyncHttpResponseHandler(LoginActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String message = object.optString("Message");
                    Boolean isLogin = object.optBoolean("IsSuccess");
                    if (isLogin) {
                        String MainData = object.optString("MainData");
                        UserInfo userInfo = GsonUtils.fromJson(MainData, UserInfo.class);
                        /**
                         * 第三方登陆的时候判断商家用户是否进行过申请入驻
                         */
                        isRegister = userInfo.IsRegister;
                        LoginUser.getInstance().userInfo = userInfo;
                        if (null != userInfo) {
                            BaseApplication.getInstance().Token = userInfo.TokenType + " " + userInfo.AccessToken;
                        }
                        LoginUser.getInstance().setIsLogin(true);

                        getUsereInfo();
                    }
                    Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        });
    }

    /**
     * 判断用户是否注册
     */
    private void isSignup(String phoneNumber) {
        WebRequestHelper.post(URLText.IS_SIGNUP, RequestParamsPool.isSignup(phoneNumber), new MyAsyncHttpResponseHandler(LoginActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.optString("Message");
                    Boolean isScuess = object.optBoolean("IsSuccess");

                    if (TextUtils.equals("该手机号尚未注册！", message)) {//没有注册去提示
                        //判断选择的是个人还是商家  2是个人3是商家
                        if (2 == type) {
                            peopleDialog();
                        } else {
                            businessDialog();
                        }
                    } else {//已经注册就直接去登录
                        login(name.getText().toString(), password.getText().toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 个人dialog
     */
    public void peopleDialog() {
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                register(type);
            }

            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "用户，您好！请先办理入驻手续，在搜索促销商品", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 商家dialog
     */
    public void businessDialog() {
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                register(type);
            }

            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "商户，您好！请先办理入驻手续，再发布促销信息", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /**
     * 账号密码登录
     *
     * @param name
     * @param password
     */
    private void login(final String name, final String password) {
        showLoading(this, "正在加载");
        WebRequestHelper.post(URLText.LOGIN_URL, RequestParamsPool.getLoginParams(name, password, JPushInterface.getRegistrationID(LoginActivity.this), type), new MyAsyncHttpResponseHandler(LoginActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                if (null != builder) {
                    builder.dismiss();
                }
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String message = object.optString("Message");


                    if (TextUtils.equals("验证失败，账号不存在！", message)) {//没有注册去提示
                        //判断选择的是个人还是商家  2是个人3是商家
                        if (2 == type) {
                            peopleDialog();
                        } else {
                            businessDialog();
                        }
                    } else {//已经注册就直接去登录

                        Boolean isLogin = object.optBoolean("IsSuccess");
                        if (isLogin) {
                            String MainData = object.optString("MainData");
                            UserInfo userInfo = GsonUtils.fromJson(MainData, UserInfo.class);
                            LoginUser.getInstance().userInfo = userInfo;
                            if (null != userInfo) {
                                BaseApplication.getInstance().Token = userInfo.TokenType + " " + userInfo.AccessToken;
                            }
                            LoginUser.getInstance().setIsLogin(true);
                            /**
                             * 保存 - 密码和账户
                             */
                            if (null != name && null != password && 0 != type) {
                                saveLoginUserAccountAndPwd(name, password, type);
                            }
//                        getUsereInfo();
                            String ShowData = object.optString("ShowData");
                            MyUserInfoUtils.getInstance().myUserInfo = GsonUtils.fromJson(ShowData, MyUserInfo.class);

                            if (null == MyUserInfoUtils.getInstance().myUserInfo.IMUserName && null == MyUserInfoUtils.getInstance().myUserInfo.IMPassword) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("first_login", true);
                                startActivity(intent);
                                finish();
                            } else {
                                requestLoginEaseChat(MyUserInfoUtils.getInstance().myUserInfo.IMUserName, MyUserInfoUtils.getInstance().myUserInfo.IMPassword);
                            }
                        }
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 得到用户的信息
     */
    private void getUsereInfo() {
        showLoading(this, "正在加载");
        WebRequestHelper.json_post(LoginActivity.this, URLText.GET_USERINFO, RequestParamsPool.getUserInfo(), new MyAsyncHttpResponseHandler(LoginActivity.this) {
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

                if (null == MyUserInfoUtils.getInstance().myUserInfo.IMUserName && null == MyUserInfoUtils.getInstance().myUserInfo.IMPassword) {
                    if (null != builder) {
                        builder.dismiss();
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("first_login", true);
                    startActivity(intent);
                    finish();
                } else {
                    requestLoginEaseChat(MyUserInfoUtils.getInstance().myUserInfo.IMUserName, MyUserInfoUtils.getInstance().myUserInfo.IMPassword);
                }
            }
        });
    }

    private void getRegisterNUmber() {
        StringEntity s = null;
        try {
            s = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(LoginActivity.this, URLText.REGISTER_NUMBER, s, new MyAsyncHttpResponseHandler(LoginActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    RegisterNumber registerNumber = GsonUtils.fromJson(new String(responseBody), RegisterNumber.class);
                    if (null != registerNumber.MainData) {
                        person_number.setText(registerNumber.MainData.TodayNormalCount);
                        today_number.setText(registerNumber.MainData.NormalCount);
                        business_number.setText(registerNumber.MainData.SellerCount);
                        good_number.setText(registerNumber.MainData.ProductCount);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void requestLoginEaseChat(final String accountStr, final String pwd) {
        EMChatManager.getInstance().login(accountStr, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                if (null != builder) {
                    builder.dismiss();
                }
                // EMGroupManager.getInstance().loadAllGroups();//加载群组 木有此功能
                EMChatManager.getInstance().loadAllConversations();
                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
                        BaseApplication.currentUserNick.trim());
                if (!updatenick) {
                    LogUtils.instance.d("update current user nick fail");
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("first_login", true);
                startActivity(intent);
                finish();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                if (null != builder) {
                    builder.dismiss();
                }
            }
        });
    }

    private void register(int type) {
        Intent intent = new Intent(LoginActivity.this, OneStepRegisterActivity.class);
        intent.putExtra("register_type", type + "");
        startActivity(intent);
    }

    /**
     * 保存帐号和密码
     */
    public void saveLoginUserAccountAndPwd(String account, String pwd, int type) {
        PreferencesUtils.putString(BaseApplication.getInstance(), "account", account);
        PreferencesUtils.putString(BaseApplication.getInstance(), "pwd", pwd);
        PreferencesUtils.putInt(BaseApplication.getInstance(), "type", type);
    }

    /**
     * 打开loading
     */
    public void showLoading(Context context, String text) {
        if (null == builder) {
            builder = new KyLoadingBuilder(context);
        }
        builder.setIcon(R.mipmap.loading);
        builder.setText(text);
        builder.setOutsideTouchable(false);
        builder.setBackTouchable(true);
        builder.show();
    }
}