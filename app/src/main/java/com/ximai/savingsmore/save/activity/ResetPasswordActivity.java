package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.save.common.BaseActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by caojian on 16/11/18.
 */
public class ResetPasswordActivity  extends BaseActivity implements View.OnClickListener{
    private EditText code,newPassword,confirmation_password;
    private String number;
    private Button submit;
    private LinearLayout ll_send;
    private TextView tv_send;

    private Thread thread;
    private int time = 120;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                tv_send.setText(time + " 秒可重发");
            }
            if (time == 0) {
                time = 120;
                ll_send.setVisibility(View.GONE);
            }
        }
    };

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password_activity);
        setLeftBackMenuVisibility(ResetPasswordActivity.this,"");
        setCenterTitle("重置密码");
        code= (EditText) findViewById(R.id.code);
        newPassword= (EditText) findViewById(R.id.password);
        confirmation_password= (EditText) findViewById(R.id.confirm);
        submit= (Button) findViewById(R.id.submit);

        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        tv_send = (TextView) findViewById(R.id.tv_send);

        submit.setOnClickListener(this);
        Intent intent=getIntent();
        if(null!=intent){
            number=intent.getStringExtra("phone");
        }

        if (time == 120) {
            thread = new Thread(new ThreadShow());
            thread.start();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.submit){
            if(!TextUtils.isEmpty(code.getText())&&!code.getText().equals("请输入6位验证码")){
                if(newPassword.getText().toString().equals(confirmation_password.getText().toString())){
                    submit(number,newPassword.getText().toString().toString(),code.getText().toString());
                }
                else {
                    Toast.makeText(ResetPasswordActivity.this,"两次输入的密码不一致",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(ResetPasswordActivity.this,"请输入验证码",Toast.LENGTH_LONG).show();
            }
        }
    }

    private  void submit(String PhoneNumber,String NewPassword,String Code){
        WebRequestHelper.post(URLText.RESET_PASSWORD, RequestParamsPool.getResetPassword(PhoneNumber, NewPassword, Code), new MyAsyncHttpResponseHandler(ResetPasswordActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object=new JSONObject(new String(responseBody));
                    String message=object.optString("Message");
                    Boolean isLogin=object.optBoolean("IsSuccess");
                    Toast.makeText(ResetPasswordActivity.this,message,Toast.LENGTH_LONG).show();
                    if(isLogin){
                        Intent intent=new Intent(ResetPasswordActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
