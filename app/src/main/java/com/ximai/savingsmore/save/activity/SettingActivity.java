package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.core.CoreJob;
import com.ximai.savingsmore.library.toolbox.PopupWindowFromBottomUtil;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.library.view.Form_item;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/15.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private Form_item number;
    private TextView xiugai_password, about_we, falu, toushu, login_out;
    private List<String> marker_number = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        initView();
        
        initEvent();
        
        initData();
    }

    /**
     * view
     */
    private void initView() {
        setCenterTitle("设置");
        setLeftBackMenuVisibility(SettingActivity.this, "");
        number = (Form_item) findViewById(R.id.number);
        xiugai_password = (TextView) findViewById(R.id.xiugai_password);
        about_we = (TextView) findViewById(R.id.about_we);
        falu = (TextView) findViewById(R.id.falu);
        toushu = (TextView) findViewById(R.id.toushu);
        login_out = (TextView) findViewById(R.id.login_out);
    }

    /**
     * event
     */
    private void initEvent() {
        login_out.setOnClickListener(this);
        xiugai_password.setOnClickListener(this);
        about_we.setOnClickListener(this);
        falu.setOnClickListener(this);
        toushu.setOnClickListener(this);
        number.setOnClickListener(this);
    }

    /**
     * data
     */
    private void initData() {
        marker_number.add("不限");
        marker_number.add("30");
        marker_number.add("50");
        marker_number.add("100");
        number.setmTvRight(PreferencesUtils.getString(SettingActivity.this, "number"));
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.xiugai_password:
                Intent intent = new Intent(SettingActivity.this, ForgetPasswordActivity.class);
                intent.putExtra("title", "修改密码");
                startActivity(intent);
                break;
            case R.id.about_we:
                Intent intent1 = new Intent(SettingActivity.this, AboutWeActivity.class);
                startActivity(intent1);
                break;
            case R.id.falu:
                Intent intent2 = new Intent(SettingActivity.this, LowStateActivity.class);
                startActivity(intent2);
                break;
            case R.id.toushu:
                Intent intent3 = new Intent(SettingActivity.this, ComplainActivity.class);
                startActivity(intent3);
                break;
            case R.id.login_out://退出登录
                exit();
                break;
            case R.id.number:
                PopupWindowFromBottomUtil.shouWindowWithWheel(SettingActivity.this, LayoutInflater.from(SettingActivity.this).inflate(R.layout.business_my_center_activity, null), marker_number, new PopupWindowFromBottomUtil.Listener() {
                    @Override
                    public void confirm(String content, PopupWindow window) {
                        number.setmTvRight(content);
                        PreferencesUtils.putString(BaseApplication.getInstance(), "number", content);
                        window.dismiss();
                        /**
                         * 发送一个消息进行地图页面的数据更新
                         */
                        NotificationCenter.defaultCenter().postNotification(Constants.LOADING_MAPDATA,"");//不进行设置了
                    }
                });
                break;
                default:
                    break;
        }
    }

    /**
     * 退出操作新版本
     */
    public void exit(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                CoreJob.exitApplication();
                finish();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "确定退出登录吗？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}