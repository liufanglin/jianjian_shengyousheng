package com.ximai.savingsmore.save.activity;

import android.os.Bundle;
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
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by caojian on 16/12/19.
 */
public class LeaveMessageActivity extends BaseActivity implements View.OnClickListener {
    private EditText name, city, phone, email, qq_wechat, content;
    private Button submit;
    private String id, t_name, t_city, t_phone, t_email, t_qq, t_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.liuyan_msg);
        setCenterTitle("留言");
        setLeftBackMenuVisibility(LeaveMessageActivity.this, "");

        initView();
        initData();
    }

    private void initView() {

        name = (EditText) findViewById(R.id.name);
        city = (EditText) findViewById(R.id.l_city);
        phone = (EditText) findViewById(R.id.number);
        email = (EditText) findViewById(R.id.email);
        qq_wechat = (EditText) findViewById(R.id.qq_wechat);
        content = (EditText) findViewById(R.id.leave_message);
        submit = (Button) findViewById(R.id.submit_message);

        submit.setOnClickListener(this);
    }

    private void initData(){
        try{
            id = getIntent().getStringExtra("Id");

            String leave_name = PreferencesUtils.getString(LeaveMessageActivity.this, "leave_name", "");
            String leave_city = PreferencesUtils.getString(LeaveMessageActivity.this, "leave_city", "");
            String leave_phone = PreferencesUtils.getString(LeaveMessageActivity.this, "leave_phone", "");
            String leave_email = PreferencesUtils.getString(LeaveMessageActivity.this, "leave_email", "");
            String leave_wx = PreferencesUtils.getString(LeaveMessageActivity.this, "leave_wx", "");
            String leave_msg = PreferencesUtils.getString(LeaveMessageActivity.this, "leave_msg", "");
            name.setText(leave_name);
            city.setText(leave_city);
            phone.setText(leave_phone);
            email.setText(leave_email);
            qq_wechat.setText(leave_wx);
            content.setText(leave_msg);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_message://提交留言
                if (TextUtils.isEmpty(name.getText())){
                    Toast.makeText(this, "请输入您的姓名", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (TextUtils.isEmpty(phone.getText())){
                    Toast.makeText(this, "请输入您的电话", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (TextUtils.isEmpty(content.getText())){
                    Toast.makeText(this, "请输入留言内容", Toast.LENGTH_SHORT).show();
                    break;
                }

                t_name = name.getText().toString();
                t_city = city.getText().toString();
                t_phone = phone.getText().toString();
                t_email = email.getText().toString();
                t_qq = qq_wechat.getText().toString();
                t_content = content.getText().toString();

                PreferencesUtils.putString(LeaveMessageActivity.this,"leave_name",t_name);
                PreferencesUtils.putString(LeaveMessageActivity.this,"leave_city",t_city);
                PreferencesUtils.putString(LeaveMessageActivity.this,"leave_phone",t_phone);
                PreferencesUtils.putString(LeaveMessageActivity.this,"leave_email",t_email);
                PreferencesUtils.putString(LeaveMessageActivity.this,"leave_wx",t_qq);
                PreferencesUtils.putString(LeaveMessageActivity.this,"leave_msg",t_content);

                /**
                 * 提交
                 */
                leave_message();
                break;
        }
    }

    /**
     * 提交留言
     */
    private void leave_message() {
        WebRequestHelper.json_post(LeaveMessageActivity.this, URLText.SEND_MESSAGE, RequestParamsPool.sendMessage(id, t_name, t_city, t_phone, t_email, t_qq, t_content), new MyAsyncHttpResponseHandler(LeaveMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String message = object.optString("Message");
                    String isSucess = object.optString("IsSuccess");
                    if ((isSucess.equals("true"))) {
                        finish();
                    }
                    Toast.makeText(LeaveMessageActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
