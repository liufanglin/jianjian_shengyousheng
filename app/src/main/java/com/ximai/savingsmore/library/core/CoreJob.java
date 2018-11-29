package com.ximai.savingsmore.library.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ximai.savingsmore.library.FileSystem.FileSystem;
import com.ximai.savingsmore.library.cache.ImageCachePool;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.activity.LoginActivity;
import com.ximai.savingsmore.save.activity.MainActivity;
import com.ximai.savingsmore.save.activity.NoLoginMainactivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.utils.PrefUtils;

//import org.apache.http.Header;

import org.apache.http.Header;

import java.util.Vector;

/**
 * 核心处理类
 *
 * @author wangguodong
 */
public class CoreJob {

    private static Vector<Activity> activities = new Vector<Activity>();
    public static boolean isExiting = false;

    public static void init(Context context) {
        //初始化文件系
        FileSystem.init(context);
        //初始化图片缓存
        ImageCachePool.initImageLoader(context);
        //初始化数据库
    }

    /**
     * 添加activity到工作站
     * @param activity
     */
    public static synchronized void addToActivityStack(Activity activity) {
        if (activities == null)
            activities = new Vector<Activity>();
        if (!isExiting)
            activities.add(activity);
    }

    public static synchronized void removeFormActivityStack(Activity activity) {
        if (activities != null && !isExiting)
            activities.remove(activity);
    }


    // 退出应用
    /**
     */
    public static void exitApplication() {
        isExiting = true;
        toDoOnExit();//处理退出前的工作
        if (activities != null) {
            for (Activity activity : activities) {
                activity.finish();
            }
        }
        Intent it = new Intent(BaseApplication.getInstance(), NoLoginMainactivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        BaseApplication.getInstance().startActivity(it);
    }


    public static void logout() {

        WebRequestHelper.json_post(BaseApplication.getInstance(), URLText.LOGIN_OUT, RequestParamsPool.loginOut(), new MyAsyncHttpResponseHandler(BaseApplication.getInstance()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {

            }
        });

    }


    //退出app要处理的事情
    private static void toDoOnExit() {
        logout();
        //   WebRequestHelper.setCookies(null);
        PreferencesUtils.putString(BaseApplication.getInstance(), "account", null);
        PreferencesUtils.putString(BaseApplication.getInstance(), "pwd", null);
        PreferencesUtils.putInt(BaseApplication.getInstance(), "type", 0);
        // LoginUser.getInstance().saveLoginUserPwd("");
        //清除loginuser
        LoginUser.clearInstance();
        MyUserInfoUtils.getInstance().myUserInfo = null;
        //退出聊天
        BaseApplication.LogoutEasaChat();
    }


}
