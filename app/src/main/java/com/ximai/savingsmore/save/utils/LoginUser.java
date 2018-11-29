package com.ximai.savingsmore.save.utils;


import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseApplication;

public class LoginUser {

    private static LoginUser instance = null;

    private boolean isLogin = false;

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
   // public GuideInfo guideInfo = null;
   // public StoreInfo storeInfo = null;

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


}
