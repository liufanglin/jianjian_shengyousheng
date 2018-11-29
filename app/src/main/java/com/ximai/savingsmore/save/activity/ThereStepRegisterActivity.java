package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyUserInfo;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.UserInfo;
import com.ximai.savingsmore.save.utils.NotificationCenter;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by caojian on 16/11/17.
 */
public class ThereStepRegisterActivity extends BaseActivity implements View.OnClickListener {
    private TextView send, phoneNumber;
    private Button next_step;
    private String number;
    private String password;
    private String photoCode;
    private EditText code;
    //个人或商家
    private int type;
    private int time = 120;
    private Thread thread;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                send.setText(time + " 秒可重发");
            }
            if (time == 0) {
                time = 120;
                send.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.there_register_activity);
        setLeftBackMenuVisibility(ThereStepRegisterActivity.this, "");
        setCenterTitle("注册");
        send = (TextView) findViewById(R.id.send);
        phoneNumber = (TextView) findViewById(R.id.number);
        next_step = (Button) findViewById(R.id.next_step);
        code = (EditText) findViewById(R.id.code);
        next_step.setOnClickListener(this);
        Intent intent = getIntent();
        if (null != intent) {
            type = Integer.parseInt(intent.getStringExtra("type"));
            number = intent.getStringExtra("number");
            password = intent.getStringExtra("password");
            photoCode = intent.getStringExtra("photoCode");
        }

        if (time == 120) {
            phoneNumber.setText("已发送验证码至:" + number);
            thread = new Thread(new ThreadShow());
            thread.start();
        }
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.send) {
//            if (time == 60) {
//                phoneNumber.setText("已发送验证码至:" + number);
//                thread = new Thread(new ThreadShow());
//                thread.start();
//                if (null != number && !TextUtils.isEmpty(number)) {
////                    sendCode(number,photoCode);
//                }
//            }
//        }
        if (v.getId() == R.id.next_step) {
            if (!TextUtils.isEmpty(code.getText()) && !code.getText().toString().equals("请输入您的验证码")) {
                register(number, password, password, code.getText().toString(), type);
            } else {
                Toast.makeText(ThereStepRegisterActivity.this, "请输入您的验证码", Toast.LENGTH_LONG).show();
            }
        }
    }

//    /**
//     * 发送验证码
//     * @param number
//     */
//    private void sendCode(String number,String photoCode) {
//        WebRequestHelper.post(URLText.SEND_CODE, RequestParamsPool.getCodeParams1(number, 1,photoCode), new MyAsyncHttpResponseHandler(ThereStepRegisterActivity.this) {
//            @Override
//            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
//                String result = new String(responseBody);
//                try {
//                    JSONObject object = new JSONObject(result);
//                    String message = object.optString("Message");
//                    Boolean isScuess = object.optBoolean("IsSuccess");
//                    if (false == isScuess){
//                        Toast.makeText(ThereStepRegisterActivity.this,message,Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    /**
     * 注册
     * @param PhoneNumber
     * @param PassWord
     * @param ConfirmPassword
     * @param Code
     * @param UserType
     */
    private void register(final String PhoneNumber, final String PassWord, String ConfirmPassword, String Code, int UserType) {
        WebRequestHelper.post(URLText.REGISTER_CODE, RequestParamsPool.getRegristes(PhoneNumber, PassWord, ConfirmPassword, Code, UserType), new MyAsyncHttpResponseHandler(ThereStepRegisterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.optString("Message");
                    Boolean isScuess = object.optBoolean("IsSuccess");
                    if (isScuess) {
                        if (type == 2) {
                            Toast.makeText(ThereStepRegisterActivity.this, message, Toast.LENGTH_LONG).show();
                            //个人去登录
                            Intent intent = new Intent(ThereStepRegisterActivity.this, LoginActivity.class);
                            intent.putExtra("phone", number.toString());
                            startActivity(intent);
                        }
                        if (type == 3) {
//                            login(PhoneNumber, PassWord, type);
                            //商家去登录
                            Intent intent = new Intent(ThereStepRegisterActivity.this, LoginActivity.class);
                            intent.putExtra("phone", number.toString());
                            startActivity(intent);
                        }
                        NotificationCenter.defaultCenter().postNotification(Constants.ONE_STEP,"");
                        NotificationCenter.defaultCenter().postNotification(Constants.TWO_STEP,"");
                        finish();
                    }
                    Toast.makeText(ThereStepRegisterActivity.this,message,Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 原来的商家登录在跳转到第四步
     * @param name
     * @param password
     * @param type
     */
    private void login(String name, String password, int type) {
        WebRequestHelper.post(URLText.LOGIN_URL, RequestParamsPool.getLoginParams(name, password, "", type), new MyAsyncHttpResponseHandler(ThereStepRegisterActivity.this) {
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
                        getUsereInfo();
                    }
                    //Toast.makeText(ThereStepRegisterActivity.this, message, Toast.LENGTH_LONG).show();
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
        WebRequestHelper.json_post(ThereStepRegisterActivity.this, URLText.GET_USERINFO, RequestParamsPool.getUserInfo(), new MyAsyncHttpResponseHandler(ThereStepRegisterActivity.this) {
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
                Intent intent = new Intent(ThereStepRegisterActivity.this, FourStepRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    class ThreadShow implements Runnable {
        @Override
        public void run() {
            while (time > 0) {
                try {
                    Thread.sleep(1000);
                    time--;
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
