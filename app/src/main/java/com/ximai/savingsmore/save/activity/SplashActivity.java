package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.UserInfo;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.utils.SpeechUtils;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by caojian on 16/11/18.
 */
public class SplashActivity extends Activity {
    private int WRITE_COARSE_LOCATION_REQUEST_CODE = 0;
    private int READ_SMS = 0;
    private SpeechUtils instance;

    public static final String NOT_FIRST_OPEN = "not_first_open";
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!PreferencesUtils.getBoolean(SplashActivity.this, NOT_FIRST_OPEN)) {
                Intent it = new Intent(SplashActivity.this, GuidePageActivity.class);
                startActivity(it);
                finish();
            } else {
                String account = PreferencesUtils.getString(BaseApplication.getInstance(), "account", null);
                String pwd = PreferencesUtils.getString(BaseApplication.getInstance(), "pwd", null);
                int type = PreferencesUtils.getInt(BaseApplication.getInstance(), "type");
                /**
                 * 保存一个三方的Id
                 */
                String OpenId = PreferencesUtils.getString(BaseApplication.getInstance(), "OpenId", null);

                if (null != account && null != pwd && type != 0) {
                    login(account, pwd, type);
                } else {
                    if ( null == OpenId){
                        finish();
                        boolean isAutoLogin = false;
                        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
                            isAutoLogin = true;
                        }
                        Intent it = new Intent(SplashActivity.this, NoLoginMainactivity.class);
                        // it.putExtra("isAutoLogin",isAutoLogin);
                        startActivity(it);
                    }else{
                        String userId = PreferencesUtils.getString(BaseApplication.getInstance(), "userId", null);
                        String userName = PreferencesUtils.getString(BaseApplication.getInstance(), "userName", null);
                        String ExternalSigninType = PreferencesUtils.getString(BaseApplication.getInstance(), "ExternalSigninType", null);
                        String pushId = PreferencesUtils.getString(BaseApplication.getInstance(), "PushId", null);
                        int type1 = PreferencesUtils.getInt(BaseApplication.getInstance(), "type");

                        /**
                         * 将数据绑定上传
                         */
                        ThirdLogin(userId, userName, ExternalSigninType, pushId,type1 + "");
                    }
                }
            }
        }
    };
    private KyLoadingBuilder builder;


    /**
     *第三方登录信息返回上传到网络
     */
    private void ThirdLogin(String OpenId, String NickName, String ExternalSigninType,String PushId,String UserType) {
        showLoading(this,"正在加载");
        WebRequestHelper.post(URLText.THIRD_LOGIN, RequestParamsPool.thirldLogin(OpenId, NickName, ExternalSigninType,PushId, UserType), new MyAsyncHttpResponseHandler(SplashActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String message = object.optString("Message");
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
                         * 获取用户信息 - 之前是获取用户信息 - 为了解决卡顿的现象
                         */
//                        getUsereInfo();
                        //异步获取当前用户的昵称和头像(从自己服务器获取...)
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
                        if (null != builder){
                            builder.dismiss();
                        }
                        finish();
                    }else{
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        if (null != builder){
                            builder.dismiss();
                        }
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
                super.onFailure(arg0, arg1, arg2, arg3);}
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        PreferencesUtils.putString(BaseApplication.getInstance(), "number", "不限");
        /**
         * 6．0登陆的时候请求定位权限
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }

        /**
         * 6．0登陆的时候请求定位权限
         */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS}, READ_SMS);//自定义的code
        }
        myHandler.sendEmptyMessageDelayed(0, 400);
    }

    /**
     * 手机号密码登录
     * @param name
     * @param password
     * @param type
     */
    private void login(final String name, final String password, int type) {
        showLoading(this,"正在加载");
        WebRequestHelper.post(URLText.LOGIN_URL, RequestParamsPool.getLoginParams(name, password, JPushInterface.getRegistrationID(SplashActivity.this), type), new MyAsyncHttpResponseHandler(SplashActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String message = object.optString("Message");
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
                         * 获取用户信息- 之前是获取用户信息 - 为了解决卡顿的现象
                         */
//                        getUsereInfo();

                        //异步获取当前用户的昵称和头像(从自己服务器获取...)
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        if (null != builder){
                            builder.dismiss();
                        }

                        finish();
                    }
                    //Toast.makeText(SplashActivity.this, message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
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
        myHandler.removeCallbacksAndMessages(null);
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