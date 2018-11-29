package com.ximai.savingsmore.library.net;


import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.library.toolbox.NetWorkUtils;
import com.ximai.savingsmore.save.common.BaseApplication;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;

import java.io.File;
import java.util.Locale;
import java.util.Map;




/**
 * AsyncHttpClient 请求类
 *
 * @author WANGGUODONG
 *         使用文档参考 :http://loopj.com/android-async-http/
 */

public class WebRequestHelper {

    public static final String CHAR_SET = "UTF-8";
    private static String cookies;
    private static String userAgent = "";
    private static AsyncHttpClient asyncclient = new AsyncHttpClient();



   // 统一组织header请求头
    private static void generateHeaders(boolean... args) {//参数0：是否需要cookie 参数2 是否需要user_agent
        if(!TextUtils.isEmpty(BaseApplication.getInstance().Token)&&null!=BaseApplication.getInstance().Token){
            String token=BaseApplication.getInstance().Token;
            asyncclient.addHeader("Authorization ",BaseApplication.getInstance().Token);
//            asyncclient.addHeader("charset", HTTP.UTF_8);         //这行很重要
        }

       // if (args.length == 0) {
            //清空headers
       //     asyncclient.removeAllHeaders();

//            if (TextUtils.isEmpty(userAgent))
           //asyncclient.addHeader("content-type", "application/json");

//            else
//                asyncclient.addHeader("user-agent", userAgent);

//            //添加cookies
//            if (!TextUtils.isEmpty(cookies)) {
//                asyncclient.addHeader("Cookie", cookies);
//            } else {
//                LogUtils.instance.d("wangguodong", "cookies not exists!");
//            }
       // } else if (args.length == 1) {
            //清空headers
            //asyncclient.removeAllHeaders();

          //  asyncclient.addHeader("content-type", content_type);

//            //添加cookies
//            if (!TextUtils.isEmpty(cookies) && args[0]) {
//                asyncclient.addHeader("Cookie", cookies);
//            } else {
//                LogUtils.instance.d("wangguodong", "cookies not exists or not need cookies");
//            }
       // } else if (args.length == 2) {
            //清空headers
          //  asyncclient.removeAllHeaders();

//            if (args[1]) {
//                if (TextUtils.isEmpty(userAgent))
//                    asyncclient.addHeader("user-agent", getCurrentUserAgent());
//                else
//                    asyncclient.addHeader("user-agent", userAgent);
//            }
//            //添加cookies
//            if (!TextUtils.isEmpty(cookies) && args[0]) {
//                asyncclient.addHeader("Cookie", cookies);
//            } else {
//                LogUtils.instance.d("wangguodong", "cookies not exists or not need cookies");
//            }
//        }


    }


    /**
     * 异步GET请求 携带参数 默认携带cookie
     *
     * @param url
     * @param params
     * @param responseHandler 请使用MyAsyncHttpResponseHandler 对象实例 方便请求控制
     */
    public static void get(String url, RequestParams params,
                           AsyncHttpResponseHandler responseHandler) {

        setTimeout();
        generateHeaders();
        asyncclient.get(url, params, responseHandler);
        
        LogUtils.instance.d("network", "#######request url(get) is " + url);
        LogUtils.instance.d("network", "#######request params is " + params.toString());

        if (!TextUtils.isEmpty(cookies))
            LogUtils.instance.d("network", "#######cookie is " + cookies);
        else
            LogUtils.instance.d("network", "#######cookie is not exists!");
    }




    /**
     * 异步GET请求 携带参数并设置是否需要cookie
     *
     * @param url
     * @param params
     * @param responseHandler 请使用MyAsyncHttpResponseHandler 对象实例 方便请求控制
     */
    public static void get(String url, RequestParams params,
                           boolean addCookies, AsyncHttpResponseHandler responseHandler) {

        setTimeout();
        generateHeaders();
        asyncclient.get(url, params, responseHandler);

        LogUtils.instance.d("network", "#######request url(get) is " + url);
        LogUtils.instance.d("network", "#######request params is " + params.toString());

        if (!TextUtils.isEmpty(cookies))
            LogUtils.instance.d("network", "#######cookie is " + cookies);
        else
            LogUtils.instance.d("network", "#######cookie is not exists!");
    }


    /**
     * 异步POST请求 携带参数并设置是否需要cookie
     *
     * @param url
     * @param params
     * @param responseHandler 请使用MyAsyncHttpResponseHandler 对象实例 方便请求控制
     */
    public static void post(String url, RequestParams params,
                            boolean addCookies, AsyncHttpResponseHandler responseHandler) {

        setTimeout();

       generateHeaders();
        asyncclient.post(url, params, responseHandler);


        LogUtils.instance.d("network", "#######request url(get) is " + url);
        LogUtils.instance.d("network", "#######request params is " + params.toString());

        if (!TextUtils.isEmpty(cookies))
            LogUtils.instance.d("network", "#######cookie is " + cookies);
        else
            LogUtils.instance.d("network", "#######cookie is not exists!");

    }

    /**
     * 异步POST请求 携带参数 默认带cookie
     *
     * @param url
     * @param params
     * @param responseHandler 请使用MyAsyncHttpResponseHandler 对象实例 方便请求控制
     */
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        setTimeout();
        generateHeaders();
        asyncclient.post(url, params, responseHandler);

        LogUtils.instance.d("network", "#######request url(post) is " + url);
        LogUtils.instance.d("network", "#######request params is " + params.toString());

        if (!TextUtils.isEmpty(cookies))
            LogUtils.instance.d("network", "#######cookie is " + cookies);
        else
            LogUtils.instance.d("network", "#######cookie is not exists!");


    }
    public  static  void json_post(Context context, String url, StringEntity entity,AsyncHttpResponseHandler responseHandler){
        String s=entity.toString();
        setTimeout();
        generateHeaders();
        asyncclient.post(context,url,entity,"application/json",responseHandler);
    }


    /**
     * 异步GET请求 未携带参数
     *
     * @param url
     * @param responseHandler 请使用MyAsyncHttpResponseHandler 对象实例 方便请求控制
     */
    public static void get(String url, AsyncHttpResponseHandler responseHandler) {

        setTimeout();
        generateHeaders();
        asyncclient.get(url, responseHandler);

        LogUtils.instance.d("network", "#######request url(get) is " + url);
    }

    /**
     * 异步POST请求 未携带参数
     *
     * @param url
     * @param responseHandler 请使用MyAsyncHttpResponseHandler 对象实例 方便请求控制
     */
    public static void post(String url, AsyncHttpResponseHandler responseHandler) {
        setTimeout();
        generateHeaders();
        asyncclient.post(url, responseHandler);
        LogUtils.instance.d("network", "#######request url(post) is " + url);
    }

    /**
     * 异步DELETE请求
     *
     * @param url
     * @param responseHandler 请使用MyAsyncHttpResponseHandler 对象实例 方便请求控制
     */
    public static void delete(String url,
                              AsyncHttpResponseHandler responseHandler) {
        setTimeout();
        generateHeaders();
        asyncclient.delete(url, responseHandler);
        LogUtils.instance.d("network", "#######request url(delete) is " + url);
    }


    /**
     * 取消上下文相关的请求
     */
    public static void cancleRequest(Context context) {


        asyncclient.cancelRequests(context, true);
    }

    /**
     * 设置cookie 字符串
     */
    public static void setCookies(String str) {
        cookies = str;
    }

    /**
     * 获取cookie 字符串
     *
     * @return cookie
     */
    public static String getCookies() {
        return cookies;
    }

    /**
     * 设置超时时间
     */
    public static void setTimeout() {

        if (NetWorkUtils.isWifiConnected(BaseApplication.getInstance()))
            asyncclient.setTimeout(20000);
        else
            asyncclient.setTimeout(60000);
    }

//
//    private static String getCurrentUserAgent() {
//
//        Locale locale = Locale.getDefault();
//
//        StringBuffer buffer = new StringBuffer();
//        // Add version
//        final String version = Build.VERSION.RELEASE;
//        if (version.length() > 0) {
//            buffer.append(version);
//        } else {
//            // default to "1.0"
//            buffer.append("1.0");
//        }
//        buffer.append("; ");
//        final String language = locale.getLanguage();
//        if (language != null) {
//            buffer.append(language.toLowerCase());
//            final String country = locale.getCountry();
//            if (country != null) {
//                buffer.append("-");
//                buffer.append(country.toLowerCase());
//            }
//        } else {
//            // default to "en"
//            buffer.append("en");
//        }
//        // add the model for the release build
//        if ("REL".equals(Build.VERSION.CODENAME)) {
//            final String model = Build.MODEL;
//            if (model.length() > 0) {
//                buffer.append("; ");
//                buffer.append(model);
//            }
//        }
//        final String id = Build.ID;
//        if (id.length() > 0) {
//            buffer.append(" Build/");
//            buffer.append(id);
//        }
//
//
//       // buffer.append(";penkr/" + PackageUtils.getAppVersionName(BaseApplication.getInstance()) + " ");
//
//        String base = BaseApplication.getInstance().getResources().getString(
//                R.string.app_name);
//        return String.format(base, version, buffer);
//    }

}
