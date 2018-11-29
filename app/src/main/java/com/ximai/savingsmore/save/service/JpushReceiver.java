package com.ximai.savingsmore.save.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.save.activity.BusinessMessageActivity;
import com.ximai.savingsmore.save.activity.BusinessRewardActivity;
import com.ximai.savingsmore.save.activity.BussPushMessageActivity;
import com.ximai.savingsmore.save.activity.GoodDetailsActivity;
import com.ximai.savingsmore.save.activity.MainActivity;
import com.ximai.savingsmore.save.activity.OrderCenterActivity;
import com.ximai.savingsmore.save.activity.OrderCenterCeActivity;
import com.ximai.savingsmore.save.activity.PersonalRewardActivity;
import com.ximai.savingsmore.save.activity.PointManagerActivity;
import com.ximai.savingsmore.save.activity.PushMessageActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.utils.AppShortCutUtil;
import com.ximai.savingsmore.save.utils.EventCenter;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.utils.SpeechUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    int a=0;
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        a++;
        AppShortCutUtil.addNumShortCut(BaseApplication.getInstance(), MainActivity.class, true, a+"",false);
        EventCenter.getInstance().addEvent(EventCenter.ADD_RED_DOT_ON_DISCOVERYTAB);

        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);//通知Id
            String string = bundle.getString(JPushInterface.EXTRA_ALERT);//通知消息

            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的--ID: " + notifactionId);//列如：Bundle[{cn.jpush.android.ALERT=您发布了一个促销，获得一个奖赏，点击领取。, cn.jpush.android.EXTRA={"type":5}, cn.jpush.android.NOTIFICATION_ID=456786742, cn.jpush.android.NOTIFICATION_CONTENT_TITLE=您发布了一个促销，获得一个奖赏，点击领取。, cn.jpush.android.MSG_ID=2251802630545154}]
            if (!TextUtils.isEmpty(string)){//发布促销
                if ("您关心的商品开始促销了，快去吧！".equals(string)){//发布商品的通知
                    /**
                     * 接收到商家发布商品实现数量的显示
                     */
                    Log.e("tag","8888888888888888888888888");
                    NotificationCenter.defaultCenter().postNotification(Constants.BUSSINESS_FABU,"");//发送消息铃铛显示
                }else if (string.contains("您收到一个订单")){//您收到一个订单：订单号2018030218285152991402 ,金额1.00元。 - 买家收到订单
                    NotificationCenter.defaultCenter().postNotification(Constants.GET_SPEECH,"");//语音提醒
                    Log.e("tag","11111111111111111111");
                }else if(string.contains("您购买的商品已发货")){//商家发货
                    NotificationCenter.defaultCenter().postNotification(Constants.GET_SPEECH,"");//语音提醒
                    Log.e("tag","11111111111111111111");
                }else if(string.contains("订单") && string.contains("完成")){//个人确认收货-商家取消订单，请申请退款。
                    NotificationCenter.defaultCenter().postNotification(Constants.GET_SPEECH,"");//语音提醒
                    Log.e("tag","11111111111111111111");
                }else if(string.contains("商家取消订单，请申请退款")){//个人取消订单
                    NotificationCenter.defaultCenter().postNotification(Constants.GET_SPEECH,"");//语音提醒
                    Log.e("tag","11111111111111111111");
                }else if(string.contains("退款申请审核已通过")){//后台退款操作收货通知
                    NotificationCenter.defaultCenter().postNotification(Constants.GET_SPEECH,"");//语音提醒
                    Log.e("tag","11111111111111111111");
                }else if(string.contains("收货通知")){//个人确认收货商家收到信息
                    NotificationCenter.defaultCenter().postNotification(Constants.GET_SPEECH,"");//语音提醒
                    Log.e("tag","11111111111111111111");
                } else if (string.contains("正在促销")) {//附近的商家正在促销
                    NotificationCenter.defaultCenter().postNotification(Constants.FUJIN_BUSINESS,string);//语音提醒
                    Log.e("tag","11111111111111111111");
                }
            }

            //商家后台推送消息显示
            JSONObject object = null;
            try {
                String string1 = bundle.getString(JPushInterface.EXTRA_EXTRA.toString());
                object = new JSONObject(string1);
                String type = object.optString("type");
                if ("7".equals(type)){
                    NotificationCenter.defaultCenter().postNotification(Constants.BUSINESS_SERVICEEDIT,"");//发送消息铃铛显示
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            JSONObject object = null;
            try {
                object = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA.toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LogUtils.instance.d("推送的类型=" + object.optString("type"));
            if (object.optString("type").equals("1")) {
                Intent i = new Intent(context, PushMessageActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else if (object.optString("type").equals("3")) {
                Intent i = new Intent(context, OrderCenterCeActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            } else if (object.optString("type").equals("2")) {
                Intent i = new Intent(context, PointManagerActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }else if (object.optString("type").equals("5")){//奖赏中心的推送
                String isPeopleAndBusiness = PrefUtils.getString(context, "isPeopleAndBusiness", "");
                if ("2".equals(isPeopleAndBusiness)){
                    Intent i = new Intent(context, PersonalRewardActivity.class);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }else if ("3".equals(isPeopleAndBusiness)){
                    Intent i = new Intent(context, BusinessRewardActivity.class);
                    i.putExtras(bundle);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                }
            }else if (object.optString("type").equals("4")) {//商家的信息中心
                Intent intent1 = new Intent(context, BusinessMessageActivity.class);
                String id = object.optString("id");//商家Id
                intent1.putExtra("id", id);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent1);
            }else if (object.optString("type").equals("7")) {//商家推送消息
                Intent i = new Intent(context, BussPushMessageActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
     /*   if (MainActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (extraJson.length() > 0) {
                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);
        }*/
    }
}


