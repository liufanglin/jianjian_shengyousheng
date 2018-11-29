package com.ximai.savingsmore.save.common;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.easeui.controller.EaseUI;
import com.easemob.easeui.model.EaseNotifier;
import com.easemob.easeui.utils.EaseCommonUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.core.CoreJob;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.activity.MessageCenterActivity;
import com.ximai.savingsmore.save.modle.GoodDetial;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.utils.SpeechUtils;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.OkHttpClient;


public class BaseApplication extends Application {

    private static BaseApplication instance = null;

    protected EMEventListener eventListener = null;

    public static double Longitude;
    public static double Latitude;

    //微信支付订单号
    public String OrderId;

    //支付成功后再来一单的
    public static GoodDetial GoodDetial;


    //因为非促销品没有地址
    public static String noCuXiaoGoodsAddress;

    //将定位后的地址保存下来
    public static String userAddress;


    // 当前用户nickname,为了苹果推送不是userid而是昵称

    public static String currentUserNick = "";
    public static String registrationId = "";
    //protected EMEventListener eventListener = null;

    public static final String IS_MSG_NOTIFY_ALLOWED = "isMsgNotifyAllowed";
    public static final String IS_MSG_SOUND_ALLOWED = "isMsgSoundAllowed";
    public static final String IS_MSG_VIBRATE_ALLOWED = "isMsgVibrateAllowed";

    public static String Token;

    public ArrayList<Integer> listNewOrderNotificationId = null;
    public static SpeechUtils tts;

    public static List<Goods> listGoods = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = BaseApplication.this;
        //  ShareSDK.initSDK(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);


        //项目初始化，文件系统，缓存，网络,数据库等
        CoreJob.init(getApplicationContext());

        //初始化用户信息
        //LoginUser.getInstance();/初始化ShareSDK
        ShareSDK.initSDK(getApplicationContext());
      /*  JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
*/
        //初始化环信
        EaseUI.getInstance().init(getApplicationContext());
        setChatoptions();
        registerEventListener();

        //debugMode == true 时为打开，sdk 会在log里输入调试信息
        //在做代码混淆的时候需要设置成false
        //EMChat.getInstance().setDebugMode(AppConstants.DEBUG);//在做打包混淆时，要关闭debug模式，避免消耗不必要的资源

        //极光推送
        //JPushInterface.setDebugMode(AppConstants.DEBUG);
        //JPushInterface.init(this);

        // reInitJpushNotification();
        listNewOrderNotificationId = new ArrayList<Integer>();

        /**
         * 语音及逆行初始化
         */
//        tts = SpeechUtils.getInstance(instance);
        /**
         * 科大讯飞语音
         */
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID +"=5ab89d64");


        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }


    public void resetNotifySettings(final boolean isnotify, final boolean isound, final boolean isvibrate) {


        PreferencesUtils.putBoolean(getApplicationContext(), IS_MSG_NOTIFY_ALLOWED, isnotify);
        PreferencesUtils.putBoolean(getApplicationContext(), IS_MSG_SOUND_ALLOWED, isound);
        PreferencesUtils.putBoolean(getApplicationContext(), IS_MSG_VIBRATE_ALLOWED, isvibrate);

        //  reInitJpushNotification();

    }

//    public void reInitJpushNotification(){
//        //设置极光推送
//        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(getApplicationContext());
//
////        if(!PreferencesUtils.getBoolean(getApplicationContext(),IS_MSG_NOTIFY_ALLOWED,true))
////        {
////            JPushInterface.setSilenceTime(getApplicationContext(), 0, 0, 23, 59);
////        }
////
//        if(PreferencesUtils.getBoolean(getApplicationContext(),IS_MSG_SOUND_ALLOWED,true)&&PreferencesUtils.getBoolean(getApplicationContext(),IS_MSG_VIBRATE_ALLOWED,true))
//            builder.notificationDefaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;  // 设置为铃声与震动都要
//        else if(PreferencesUtils.getBoolean(getApplicationContext(),IS_MSG_SOUND_ALLOWED,true))
//            builder.notificationDefaults = Notification.DEFAULT_SOUND;
//        else if(PreferencesUtils.getBoolean(getApplicationContext(),IS_MSG_VIBRATE_ALLOWED,true))
//            builder.notificationDefaults = Notification.DEFAULT_VIBRATE;
//
//
//        JPushInterface.setDefaultPushNotificationBuilder(builder);
//    }


    public static BaseApplication getInstance() {

        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    public static void LogoutEasaChat() {//退出聊天登录
        EMChatManager.getInstance().logout();
    }

    public EaseNotifier getNotifier() {
        return EaseUI.getInstance().getNotifier();
    }

    private void setChatoptions() {


        EaseUI.getInstance().setSettingsProvider(new EaseUI.EaseSettingsProvider() {
            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isSpeakerOpened() {
                return false;
            }
        });

        EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {

                try {
                    String name = message.getStringAttribute("name");
                    String alias = message.getStringAttribute("alias");
                    //String avatar = message.getStringAttribute("avatar");

                    String result = "";
                    if (!TextUtils.isEmpty(alias)) {
                        result = alias;
                    } else
                        result = name;

                    return result;


                } catch (Exception e) {
                    return "";
                }

            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //设置小图标，这里为默认
                return R.mipmap.icon;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, getApplicationContext());
                if (message.getType() == EMMessage.Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }


                try {
                    String name = message.getStringAttribute("name");
                    String alias = message.getStringAttribute("alias");
                    //String avatar = message.getStringAttribute("avatar");

                    String result = "";
                    if (!TextUtils.isEmpty(alias)) {
                        result = alias;
                    } else
                        result = name;

                    return result + ":" + ticker;


                } catch (Exception e) {
                    return "" + ":" + ticker;
                }

            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {

                return fromUsersNum + "个好友，发来了" + messageNum + "条消息";
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //设置点击通知栏跳转事件
                Intent intent = new Intent(getApplicationContext(), MessageCenterActivity.class);
                // intent.putExtra(MainActivity.CURRENT_INDEX, MainActivity.INDEX_MESSAGE);
                return null;
            }
        });


    }

    protected void registerEventListener() {
        eventListener = new EMEventListener() {
            private BroadcastReceiver broadCastReceiver = null;

            @Override
            public void onEvent(EMNotifierEvent event) {
                EMMessage message = null;
                if (event.getData() instanceof EMMessage) {
                    message = (EMMessage) event.getData();
                    // EMLog.d(TAG, "receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
                }

                switch (event.getEvent()) {
                    case EventNewMessage:
                        //应用在后台，不需要刷新UI,通知栏提示新消息
                        //    if (!EaseUI.getInstance().hasForegroundActivies()) {
                        getNotifier().onNewMsg(message);
                        //    }
                        break;
                    case EventOfflineMessage:
                        if (!EaseUI.getInstance().hasForegroundActivies()) {
                            // EMLog.d(TAG, "received offline messages");
                            List<EMMessage> messages = (List<EMMessage>) event.getData();
                            getNotifier().onNewMesg(messages);
                        }
                        break;
                    // below is just giving a example to show a cmd toast, the app should not follow this
                    // so be careful of this
                    case EventNewCMDMessage: {

                        // EMLog.d(TAG, "收到透传消息");
//                        //获取消息body
//                        CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
//                        final String action = cmdMsgBody.action;//获取自定义action
//
//                        //获取扩展属性 此处省略
//                        //message.getStringAttribute("");
//                       // EMLog.d(TAG, String.format("透传消息：action:%s,message:%s", action,message.toString()));
//                        final String str = appContext.getString(R.string.receive_the_passthrough);
//
//                        final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
//                        IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);
//
//                        if(broadCastReceiver == null){
//                            broadCastReceiver = new BroadcastReceiver(){
//
//                                @Override
//                                public void onReceive(Context context, Intent intent) {
//                                    // TODO Auto-generated method stub
//                                    Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
//                                }
//                            };
//
//                            //注册广播接收者
//                            appContext.registerReceiver(broadCastReceiver,cmdFilter);
//                        }
//
//                        Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
//                        broadcastIntent.putExtra("cmd_value", str+action);
//                        appContext.sendBroadcast(broadcastIntent, null);

                        break;
                    }
                    case EventDeliveryAck:
                        message.setDelivered(true);
                        break;
                    case EventReadAck:
                        message.setAcked(true);
                        break;
                    // add other events in case you are interested in
                    default:
                        break;
                }

            }
        };

        EMChatManager.getInstance().registerEventListener(eventListener);
    }

    private static Handler mHandler;
    private static long mMainThreadId;
    /**
     * 得到主线程Handler
     */
    public static Handler getHandler() {
        return mHandler;
    }

    /**
     * 得到主线程id
     */
    public static long getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 打开loading
     */
    public void showLoading(Context context,String text){
        KyLoadingBuilder builder = new KyLoadingBuilder(context);
        builder.setIcon(R.mipmap.loading);
        builder.setText(text);
        //builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        builder.show();
    }
}
