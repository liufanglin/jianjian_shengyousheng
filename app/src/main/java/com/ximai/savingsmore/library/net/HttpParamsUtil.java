package com.ximai.savingsmore.library.net;

import android.text.TextUtils;
import android.util.Base64;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * @author wangguodong
 */
public class HttpParamsUtil {


    /*
 *添加登录参数
 */
    public static String getLoginJsonBody(String phone, String password,String PushId,int type) {

        final JSONObject json = new JSONObject();
        try {
            json.put("UserName", phone);
            json.put("Password", password);
           // json.put("PushId", PushId);
            json.put("UserType", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();

    }
}
