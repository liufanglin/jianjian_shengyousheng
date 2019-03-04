package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by luck on 2018/1/26 0026.
 * 银行卡提现
 */

public class CardWithdrawActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout back;
    private EditText et_name;
    private EditText et_cardnumber;
    private EditText et_bankname;
    private EditText et_bank;
    private EditText et_phone;
    private Button btn_apply;
    private List<String> idList;
    private int TiXianNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_withdraw);
        initView();
        initData();
        initEvent();
    }

    /**
     * view
     */
    private void initView() {
        /**
         * 将标题隐藏
         */
        toolbar.setVisibility(View.GONE);
        back = (RelativeLayout) findViewById(R.id.back);

        et_name = (EditText) findViewById(R.id.et_name);
        et_cardnumber = (EditText) findViewById(R.id.et_cardnumber);
        et_bankname = (EditText) findViewById(R.id.et_bankname);
        et_bank = (EditText) findViewById(R.id.et_bank);
        et_phone = (EditText) findViewById(R.id.et_phone);
        btn_apply = (Button) findViewById(R.id.btn_apply);
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
    }

    /**
     * data
     */
    private void initData() {
        idList = (List<String>) getIntent().getExtras().getSerializable("idList");
        TiXianNum = idList.size();

        String name = PreferencesUtils.getString(CardWithdrawActivity.this, "et_names", "");
        String cardnumber = PreferencesUtils.getString(CardWithdrawActivity.this, "et_cardnumbers", "");
        String bankname = PreferencesUtils.getString(CardWithdrawActivity.this, "et_banknames", "");
        String bank = PreferencesUtils.getString(CardWithdrawActivity.this, "et_banks", "");
        String phone = PreferencesUtils.getString(CardWithdrawActivity.this, "et_phones", "");
        if (null != name){
            et_name.setText(name);
        }
        if (null != cardnumber){
            et_cardnumber.setText(cardnumber);
        }
        if (null != bankname){
            et_bankname.setText(bankname);
        }
        if (null != bank){
            et_bank.setText(bank);
        }
        if (null != phone){
            et_phone.setText(phone);
        }
    }

    /**
     * 事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_apply://确定
                if (TextUtils.isEmpty(et_name.getText().toString())){
                    Toast.makeText(this, "请输入您的银行卡所属人姓名", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_cardnumber.getText().toString())){
                    Toast.makeText(this, "请输入您储蓄卡号", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_bankname.getText().toString())){
                    Toast.makeText(this, "请输入所属银行", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_bank.getText().toString())){
                    Toast.makeText(this, "请输入您的银行卡开户行", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_phone.getText().toString())){
                    Toast.makeText(this, "请输入您的手机号", Toast.LENGTH_SHORT).show();
                    return;
                }

                Onedialog();

//                if (null != idList){
//                    for (int i = 0; i < idList.size(); i++) {
//                        final int finalI = i;
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (finalI == idList.size() - 1){
//                                    /**
//                                     * 提现 - 最后一个
//                                     */
//                                    FivalwithDrawMoney("3",et_name.getText().toString(),et_cardnumber.getText().toString(),et_bankname.getText().toString(),et_bank.getText().toString(),et_phone.getText().toString());
//                                }else{
//                                    /**
//                                     * 提现
//                                     */
//                                    withDrawMoney("3",et_name.getText().toString(),et_cardnumber.getText().toString(),et_bankname.getText().toString(),et_bank.getText().toString(),et_phone.getText().toString());
//                                }
//                            }
//                        },200);
//                    }
//                }
                /**
                 * 提现
                 */
//                withDrawMoney(null,"3",et_name.getText().toString(),et_bankname.getText().toString(),et_bank.getText().toString(),et_cardnumber.getText().toString(),et_phone.getText().toString());
                break;
        }
    }

    /**
     * 个人提现 - 银行卡
     */
    private void withDrawMoney(String TransferType,String Name,String BankCart,String BankName,String BankDeposit,String PhoneNumber) {
        WebRequestHelper.json_post(this, URLText.PLAY_TRANSFER, RequestParamsPool.withDrawMoney(TransferType,Name,BankCart,BankName,BankDeposit,PhoneNumber), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    Boolean isSuccess = object.optBoolean("IsSuccess");
                    if (isSuccess ==true){
                        TiXianNum = TiXianNum - 1;
                        if (0 == TiXianNum){
                            FivalwithDrawMoney("3",et_name.getText().toString(),et_cardnumber.getText().toString(),et_bankname.getText().toString(),et_bank.getText().toString(),et_phone.getText().toString());
                        }else{
                            withDrawMoney("3",et_name.getText().toString(),et_cardnumber.getText().toString(),et_bankname.getText().toString(),et_bank.getText().toString(),et_phone.getText().toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 个人提现 - 银行卡
     */
    private void FivalwithDrawMoney(String TransferType,String Name,String BankCart,String BankName,String BankDeposit,String PhoneNumber) {
        WebRequestHelper.json_post(this, URLText.PLAY_TRANSFER, RequestParamsPool.withDrawMoney(TransferType,Name,BankCart,BankName,BankDeposit,PhoneNumber), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    Boolean isSuccess = object.optBoolean("IsSuccess");
                    if (true == isSuccess){
                        /**
                         * 确定
                         */
                        transfer();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 退出操作新版本
     */
    public void transfer(){
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
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "您的提现申请已提交，审核中请稍后", "确认", R.style.CustomDialog_1, callBack, 1);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }



    public void Onedialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                PreferencesUtils.putString(CardWithdrawActivity.this,"et_names",et_name.getText().toString());
                PreferencesUtils.putString(CardWithdrawActivity.this,"et_cardnumbers",et_cardnumber.getText().toString());
                PreferencesUtils.putString(CardWithdrawActivity.this,"et_banknames",et_bankname.getText().toString());
                PreferencesUtils.putString(CardWithdrawActivity.this,"et_banks",et_bank.getText().toString());
                PreferencesUtils.putString(CardWithdrawActivity.this,"et_phones",et_phone.getText().toString());

                /**
                 * 提现
                 */
                FivalwithDrawMoney("3",et_name.getText().toString(),et_cardnumber.getText().toString(),et_bankname.getText().toString(),et_bank.getText().toString(),et_phone.getText().toString());

            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(CardWithdrawActivity.this, "温馨提示", "您好，提现金额 xxx 元已经完成，审核通过后，24 小时内到账。节假日 顺延!", "知道了", R.style.CustomDialog_1, callBack, 1);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}