package com.ximai.savingsmore.save.modle;

/**
 * Created by caojian on 16/11/25.
 */
public class MyUserInfoUtils {
    private static MyUserInfoUtils userInfo = null;


    public static MyUserInfoUtils getInstance() {

        if (null == userInfo)
            userInfo = new MyUserInfoUtils();

        return userInfo;

    }
    public MyUserInfo myUserInfo;
}
