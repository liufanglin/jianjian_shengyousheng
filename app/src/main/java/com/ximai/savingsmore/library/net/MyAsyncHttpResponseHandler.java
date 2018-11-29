package com.ximai.savingsmore.library.net;


import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ximai.savingsmore.R;


/**
 * 通用请求处理类  处理请求认证 cookie保存等
 *
 * @author wangguodong
 */
public abstract class MyAsyncHttpResponseHandler extends
        AsyncHttpResponseHandler {

    private Context mContext = null;

    private boolean isSaveCookies = false;

    public MyAsyncHttpResponseHandler(Context context) {
        setSaveCookies(false);
        mContext = context;
    }

    public MyAsyncHttpResponseHandler(Context context, boolean isSave) {
        mContext = context;
        setSaveCookies(isSave);
    }

    /**
     * 处理权限以及认证相关的问题 以及coookie 请求头等
     *
     * @param statusCode   状态码
     * @param headers      请求头
     * @param responseBody 请求结果
     * @return 处理后的结果
     * @author WANGGUODONG
     */
//    private byte[] processExtraTask(int statusCode, Header[] headers,
//                                    byte[] responseBody) {
//        if (isSaveCookies)
//            processCookiesTask(headers);// 保存cookie
//
//        if (mContext != null)
//            processPermissionTask(mContext, responseBody);
//
//        return responseBody;
//    }

    /**
     * 处理cookie失效时的逻辑
     *
     * @param context
     * @param responseBody
     */
//    private void processPermissionTask(Context context, byte[] responseBody) {
//        try {
//            String result = new String(responseBody);
//
//            JSONObject object = new JSONObject(result);
//            int code = object.optInt("code");
//
//            if (code == 1003) {//登录失效
//                Toast.makeText(context,
//                        context.getString(R.string.permission_denied),
//                        Toast.LENGTH_LONG).show();
//                Intent it = new Intent(context, LoginActivity.class);
//                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(it);
//
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//            if (null != responseBody) {
//
//                if (!TextUtils.isEmpty(new String(responseBody)))
//                    LogUtils.instance.d("数据返回格式异常!" + new String(responseBody));
//            }
//
//
//        }
//    }

    /**
     * 保存临时的cookie数据
     *
     * @param headers
     */
//    public void processCookiesTask(Header[] headers) {
//        /*
//         *  cookie 内容
//		 *  Set-Cookie:_applogin_=V2SBPEWU76IJM4AMB2LGSPL4FET23PNGAQBORQRS4HLW
//		 *  VF6IERMXMP6ZU36K5XCIUCJZGAV7VO4LOUMNUE55GU3ZUUD5LM;
//		 */
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < headers.length; i++) {
//            String valueString = headers[i].getValue();
//            if (headers[i].getName().equals("Set-Cookie") || headers[i].getName().equals("set-cookie")) {
//                if (i < headers.length - 1) {
//                    builder.append(valueString).append(",");
//
//                } else {
//                    builder.append(valueString);
//                }
//            }
//        }
//        String cookie = builder.toString();
//        if (!TextUtils.isEmpty(cookie))
//            WebRequestHelper.setCookies(cookie);
//
//        LogUtils.instance.d("network", "cookies===" + ((TextUtils.isEmpty(cookie)) ? "empty" : cookie));
//    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

       // byte[] result = processExtraTask(statusCode, headers, decryptData(responseBody));

        onResponse(statusCode, headers, responseBody);
    }


    /**
     * @param result 服务器返回数据
     *               服务器数据解密
     */

//    public byte[] decryptData(byte[] result) {
//
//
//        String url = getRequestURI().toString();
//
//        String obj = new String(result);
//
//        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(obj)) {
//            LogUtils.instance.d("您请求解密的参数错误####");
//            return "".getBytes();
//        }
//
//        String urlStr = url.substring(url.lastIndexOf("?") + 1, url.length());
//
//
//        try {
//            return DesUtils.decode(obj, urlStr).getBytes();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "".getBytes();
//        }

   // }

    /**
     * 对请求拦截后接收返回的数据
     *
     * @param statusCode   状态码
     * @param headers      请求头
     * @param responseBody 请求结果
     * @author WANGGUODONG
     */
    public abstract void onResponse(int statusCode, Header[] headers,
                                    byte[] responseBody);

    public boolean isSaveCookies() {
        return isSaveCookies;
    }

    /**
     * 设置cookie
     *
     * @author WANGGUODONG
     */
    public void setSaveCookies(boolean isSaveCookies) {
        this.isSaveCookies = isSaveCookies;
    }

    @Override
    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
        // TODO Auto-generated method stub
        if (null != mContext)
            Toast.makeText(mContext, R.string.request_error, Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onProgress(long bytesWritten, long totalSize) {
        super.onProgress(bytesWritten, totalSize);
    }


}