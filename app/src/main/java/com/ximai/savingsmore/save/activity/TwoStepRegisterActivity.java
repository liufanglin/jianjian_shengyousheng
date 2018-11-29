package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.utils.NotificationCenter;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by caojian on 16/11/17.
 */
public class TwoStepRegisterActivity extends BaseActivity implements View.OnClickListener{
    private TextView phone;
    private EditText password,confin;
    private Button nextStep;
    private String type;
    private String CountryCode;
    private ImageView iv_fristpsw;
    private ImageView iv_seconepsw;
    private EditText et_photo_code;
    private ImageView iv_photo_code;
    private ImageView iv_code;
    private RelativeLayout rl_uoloading_photo;

    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName,Object cid) {
            if (Constants.TWO_STEP.equals(eventName)) {
                finish();
            }
        }
    };
    private String num;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.TWO_STEP);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_register_activity);
        setLeftBackMenuVisibility(TwoStepRegisterActivity.this,"");

        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.TWO_STEP);

        setCenterTitle("注册");
        phone= (TextView) findViewById(R.id.number);
        password= (EditText) findViewById(R.id.password);
        confin= (EditText) findViewById(R.id.confirm);
        nextStep= (Button) findViewById(R.id.next_step);
        iv_fristpsw = (ImageView) findViewById(R.id.iv_fristpsw);
        iv_seconepsw = (ImageView) findViewById(R.id.iv_seconepsw);
        et_photo_code = (EditText) findViewById(R.id.et_photo_code);
        iv_photo_code = (ImageView) findViewById(R.id.iv_photo_code);
        iv_code = (ImageView) findViewById(R.id.iv_code);
        rl_uoloading_photo = (RelativeLayout) findViewById(R.id.rl_uoloading_photo);


        nextStep.setOnClickListener(this);
        rl_uoloading_photo.setOnClickListener(this);
        iv_fristpsw.setOnClickListener(this);
        iv_seconepsw.setOnClickListener(this);
        iv_photo_code.setOnClickListener(this);
        Intent intent=getIntent();
        if(null!=intent&&null!=intent.getStringExtra("number")){
            phone.setText(intent.getStringExtra("number"));
            type=intent.getStringExtra("type");
            CountryCode=intent.getStringExtra("CountryCode");
        }
        initLoadingPhoto();
    }

    /**
     * 展示图片验证码
     */
    private void initLoadingPhoto() {
        RequestOptions options = new RequestOptions().signature(new ObjectKey(UUID.randomUUID().toString()))
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(URLText.PHOTO_CODE + randomNum()).apply(options).into(iv_code);
    }

    private String randomNum(){
        num = UUID.randomUUID().toString() + System.currentTimeMillis();
        return num;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.next_step:
                if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(this, "请设置密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(confin.getText())){
                    Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText().length() < 6 || password.getText().length() > 18){
                    Toast.makeText(this, "请重新输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.equals(confin.getText(),password.getText())){
                    Toast.makeText(this, "密码输入不一致", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_photo_code.getText())){
                    Toast.makeText(this, "请输入图形验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendCode(phone.getText().toString(),et_photo_code.getText().toString(),num,CountryCode);

//                if(!TextUtils.isEmpty(phone.getText())&&phone.getText().toString().length()==11){
//                    if(!TextUtils.isEmpty(password.getText())&&!TextUtils.isEmpty(confin.getText())&&!TextUtils.isEmpty(confin.getText())&&password.getText().toString().equals(confin.getText().toString())){
//                        Intent intent=new Intent(TwoStepRegisterActivity.this,ThereStepRegisterActivity.class);
//                        intent.putExtra("number",phone.getText().toString());
//                        intent.putExtra("type",type);
//                        intent.putExtra("password",password.getText().toString());
//                        startActivity(intent);
//                        PreferencesUtils.putString(BaseApplication.getInstance(), "pwd", password.getText().toString());
//                        finish();
//                    } else {
//                        Toast.makeText(TwoStepRegisterActivity.this,"密码和确认密码不一致",Toast.LENGTH_LONG).show();
//                    }
//                } else {
//                    Toast.makeText(TwoStepRegisterActivity.this,"请输入您的手机号码",Toast.LENGTH_LONG).show();
//                }
                break;
            case R.id.iv_fristpsw:
                password.setText(null);
                break;
            case R.id.iv_seconepsw:
                confin.setText(null);
                break;
            case R.id.iv_photo_code:
                et_photo_code.setText(null);
                break;
            case R.id.rl_uoloading_photo:
                initLoadingPhoto();
                break;
            default:
                break;
        }
    }

    /**
     * 校验图形码
     * @param number
     */
    private void sendCode(String number,String photoCode,String DeviceId,String CountryCode) {
        WebRequestHelper.post(URLText.SEND_CODE, RequestParamsPool.getCodeParams1(number, 1,photoCode,DeviceId,CountryCode), new MyAsyncHttpResponseHandler(TwoStepRegisterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.optString("Message");
                    Boolean isScuess = object.optBoolean("IsSuccess");
                    if (isScuess){
                        Intent intent=new Intent(TwoStepRegisterActivity.this,ThereStepRegisterActivity.class);
                        intent.putExtra("number",phone.getText().toString());
                        intent.putExtra("type",type);
                        intent.putExtra("password",password.getText().toString());
                        intent.putExtra("photoCode",et_photo_code.getText().toString());
                        startActivity(intent);
                        PreferencesUtils.putString(BaseApplication.getInstance(), "pwd", password.getText().toString());
                    }else {
//                        if (TextUtils.equals("当天验证码发送已达上限，请明天发送！",message)){
//                            Toast.makeText(TwoStepRegisterActivity.this,"当天验证码发送已达上限，请明天发送",Toast.LENGTH_SHORT).show();
//                        }else if (TextUtils.equals("图形验证码有误，请再输入！",message)){
//                            Toast.makeText(TwoStepRegisterActivity.this,"图形码有误，请重新输入",Toast.LENGTH_SHORT).show();
//                            initLoadingPhoto();
//                        }else{
                            Toast.makeText(TwoStepRegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                            initLoadingPhoto();
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
