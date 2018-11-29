package com.ximai.savingsmore.save.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.PrefUtils;

/**
 * Created by caojian on 16/11/17.
 */
public class OneStepRegisterActivity extends BaseActivity implements View.OnClickListener {
    private EditText number;
    private Button nextStep;
    private String type;
    private ImageView select;
    private TextView xieyi;
    private boolean select_state;
    private AlertDialog alertDialog;
    private String isPeopleAndBusiness;
    private View view;

    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver() {
        @Override
        public void onReceive(String eventName, Object cid) {
            if (Constants.ONE_STEP.equals(eventName)) {
                finish();
            }
        }
    };
    private TextView mTvZone;
    private String cityCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.one_register_activity);
        setLeftBackMenuVisibility(OneStepRegisterActivity.this, "");

        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.ONE_STEP);

        /**
         * 判断是个人注册还是商家注册
         */
        isPeopleAndBusiness = PrefUtils.getString(this, "isPeopleAndBusiness", "");
        if ("2".equals(isPeopleAndBusiness)) {//个人
            setCenterTitle("注册");
        } else if ("3".equals(isPeopleAndBusiness)) {//商家
            setCenterTitle("欢迎入驻省又省");
        }
        number = (EditText) findViewById(R.id.input_number);
        nextStep = (Button) findViewById(R.id.next_step);
        select = (ImageView) findViewById(R.id.select);
        xieyi = (TextView) findViewById(R.id.xieyi);
        mTvZone = (TextView) findViewById(R.id.tv_zone);
        nextStep.setOnClickListener(this);
        select.setOnClickListener(this);
        xieyi.setOnClickListener(this);
        mTvZone.setOnClickListener(this);

        cityCode = "86";
        mTvZone.setText("+"+ cityCode);

        if (null != getIntent()) {
            type = getIntent().getStringExtra("register_type");
            if (type.equals("2")) {
                xieyi.setText("已阅读并同意了《省又省用户服务协议》");
            } else {
                xieyi.setText("已阅读并同意了《省又省实体商户服务协议》");
            }
        }

        /**
         * 默认是同意的
         */
        select_state = true;
        select.setBackgroundResource(R.mipmap.select_kuang);

        Show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_step) {
            Intent intent = new Intent(OneStepRegisterActivity.this, TwoStepRegisterActivity.class);
            if (!TextUtils.isEmpty(number.getText())) {
                if (select_state) {
                    intent.putExtra("number", number.getText().toString());
                    intent.putExtra("type", type);
                    intent.putExtra("CountryCode", cityCode);
                    startActivity(intent);
                    finish();
                    PreferencesUtils.putString(BaseApplication.getInstance(), "account", number.getText().toString());
                } else {
                    if (type.equals("2")) {
                        Toast.makeText(OneStepRegisterActivity.this, "您还没有同意《省又省用户服务协议》", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(OneStepRegisterActivity.this, "您还没有同意《省又省实体商户服务协议》", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(OneStepRegisterActivity.this, "请输入您的手机号码", Toast.LENGTH_LONG).show();
            }
        }
        if (v.getId() == R.id.xieyi) {
            if (type.equals("2")) {
                Intent intent = new Intent(OneStepRegisterActivity.this, PersonalAgreement.class);
                intent.putExtra("title", "《省又省个人用户服务协议》");
                intent.putExtra("type", "1");
                startActivity(intent);
            } else if (type.equals("3")) {
                Intent intent = new Intent(OneStepRegisterActivity.this, BUsinessAgreement.class);
                intent.putExtra("title", "“省又省”APP发布促销");
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        }
        if (v.getId() == R.id.select) {
            if (true == select_state) {
                select_state = false;
                select.setBackgroundResource(R.mipmap.kuang);
            } else {
                select_state = true;
                select.setBackgroundResource(R.mipmap.select_kuang);
            }
        }
        //区号选择
        if (v.getId() == R.id.tv_zone) {
            Intent intent = new Intent(OneStepRegisterActivity.this, ZoneNumberActivity.class);
            startActivityForResult(intent, 1);
        }
    }

    /**
     * 注册的dialog
     */
    private void Show() {
        alertDialog = new AlertDialog.Builder(OneStepRegisterActivity.this).create();
        //个人
        if ("2".equals(isPeopleAndBusiness)) {
            view = LayoutInflater.from(OneStepRegisterActivity.this).inflate(R.layout.register_nofity, null);
            //商家
        } else if ("3".equals(isPeopleAndBusiness)) {
            view = LayoutInflater.from(OneStepRegisterActivity.this).inflate(R.layout.register_nofity_busines, null);
        }
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final ImageView queding, quxiao;
        queding = (ImageView) view.findViewById(R.id.known);
        quxiao = (ImageView) view.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.ONE_STEP);
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
                        mTvZone.setText("+"+ cityCode);
                        break;
                    default:
                        break;
                }
            }
        }
    }
}
