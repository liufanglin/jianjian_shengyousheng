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
import com.ximai.savingsmore.save.common.BaseActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by caojian on 16/11/17.
 */
public class ForgetPasswordActivity  extends BaseActivity implements View.OnClickListener{
    private EditText phonenumber;
    private Button button;
    private RelativeLayout rl_uoloading_photo;
    private ImageView iv_code;
    private ImageView iv_photo_code;
    private EditText et_photo_code;
    private String num;
    private TextView tv_zone;
    private String cityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);
        setLeftBackMenuVisibility(ForgetPasswordActivity.this,"");
        setCenterTitle(getIntent().getStringExtra("title"));
        phonenumber= (EditText) findViewById(R.id.phone_number);
        button= (Button) findViewById(R.id.find_password);

        rl_uoloading_photo = (RelativeLayout) findViewById(R.id.rl_uoloading_photo);
        iv_code = (ImageView) findViewById(R.id.iv_code);
        iv_photo_code = (ImageView) findViewById(R.id.iv_photo_code);
        et_photo_code = (EditText) findViewById(R.id.et_photo_code);
        tv_zone = (TextView) findViewById(R.id.tv_zone);
        
        button.setOnClickListener(this);
        tv_zone.setOnClickListener(this);
        rl_uoloading_photo.setOnClickListener(this);
        iv_photo_code.setOnClickListener(this);

        initData();
    }

    private void initData() {
        cityCode = "86";
        tv_zone.setText("+"+ cityCode);

        initLoadingPhoto();
    }

    /**
     * 展示图片验证码
     */
    private void initLoadingPhoto() {
        RequestOptions options = new RequestOptions().signature(new ObjectKey(UUID.randomUUID().toString())).diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(this).load(URLText.PHOTO_CODE + randomNum()).apply(options).into(iv_code);
    }

    private String randomNum(){
        num = UUID.randomUUID().toString() + System.currentTimeMillis();
        return num;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.find_password:
                if(!TextUtils.isEmpty(phonenumber.getText())){
                    if (!TextUtils.isEmpty(et_photo_code.getText())){
                        sendCode(phonenumber.getText().toString(),et_photo_code.getText().toString(),num,cityCode);
                    }else{
                        Toast.makeText(ForgetPasswordActivity.this,"请输入图形验证码",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ForgetPasswordActivity.this,"请输入您的手机号码",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.rl_uoloading_photo:
                initLoadingPhoto();
                break;
            case R.id.iv_photo_code:
                et_photo_code.setText(null);
            case R.id.tv_zone:
                Intent intent = new Intent(this, ZoneNumberActivity.class);
                startActivityForResult(intent, 1);
                break;
                default:
                    break;
        }
    }

    /**
     * 选择的城市信息
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data){
            if(!TextUtils.isEmpty(data.getStringExtra("cityCode"))){
                cityCode = data.getStringExtra("cityCode");
                switch (resultCode) {
                    case RESULT_OK:
                        tv_zone.setText("+"+ cityCode);
                        break;
                    default:
                        break;
                }
            }
        }
    }
    /**
     * 发送验证码
     * @param number
     * @param photoCode
     * @param DeviceId
     * @param CountryCode
     */
    private void sendCode(String number,String photoCode,String DeviceId,String CountryCode) {
        WebRequestHelper.post(URLText.SEND_CODE, RequestParamsPool.getCodeParams2(number,photoCode,2,DeviceId,CountryCode), new MyAsyncHttpResponseHandler(ForgetPasswordActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    String message = object.optString("Message");
                    Boolean isScuess = object.optBoolean("IsSuccess");
                    if (isScuess){
                        Intent intent=new Intent(ForgetPasswordActivity.this,ResetPasswordActivity.class);
                        intent.putExtra("phone",phonenumber.getText().toString());
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(ForgetPasswordActivity.this,message,Toast.LENGTH_LONG).show();
                        initLoadingPhoto();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}