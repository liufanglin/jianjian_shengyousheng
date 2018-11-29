package com.ximai.savingsmore.save.modle;

import android.content.Context;
import android.widget.Toast;

import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseApplication;

/**
 * Created by caojian on 16/11/24.
 */
public class LoginUser {

    private boolean isLogin = false;
    private static LoginUser instance = null;


    public static LoginUser getInstance() {

        if (null == instance)
            instance = new LoginUser();

        return instance;

    }

    public static void clearInstance() {
        instance = null;
    }


    private LoginUser() {

        //获取本地帐号
        getLoginUserAccountAndPwd();

    }

    public String pwd = null;

    public UserInfo userInfo = null;

    public String account = null;

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    /**
     * 保存帐号和密码
     *
     * @param account
     * @param pwd
     */
    public void saveLoginUserAccountAndPwd(String account, String pwd, boolean isSavePwd) {

        PreferencesUtils.putString(BaseApplication.getInstance(), "account", account);
        if (isSavePwd)
            PreferencesUtils.putString(BaseApplication.getInstance(), "pwd", pwd);
    }

    public void saveLoginUserPwd(String pwd) {

        PreferencesUtils.putString(BaseApplication.getInstance(), "pwd", pwd);
    }

    /**
     * 获取帐号和密码
     */
    public void getLoginUserAccountAndPwd() {
        account = PreferencesUtils.getString(BaseApplication.getInstance(), "account", null);
        pwd = PreferencesUtils.getString(BaseApplication.getInstance(), "pwd", null);
    }

    public static Toast toast = null;

    public static void showToast(Context context, String msg, int lastTime) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, lastTime);
        } else {
            toast.cancel();//关闭吐司显示
            toast = Toast.makeText(context, msg, lastTime);
        }
        toast.show();//重新显示吐司

    }

}
