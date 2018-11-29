package com.ximai.savingsmore.save.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.core.CoreJob;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.fragment.BusinessFragment;
import com.ximai.savingsmore.save.fragment.MapFragment;
import com.ximai.savingsmore.save.fragment.PersonFragment;
import com.ximai.savingsmore.save.modle.MyUserInfo;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.VoiceUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by caojian on 16/11/21.
 */
public class MainActivity extends SlidingFragmentActivity implements View.OnClickListener, EMEventListener {
    private PersonFragment personFragment;
    private FragmentManager manager;
    private MapFragment mapFragment;
    public SlidingMenu sm;
    private ImageView login;
    private LinearLayout search;
    private TextView location;
    String city1;
    double Longitude1 = 0;
    double Latitude1 = 0;
    private boolean first;
    private AlertDialog alertDialog1;
    private Handler mHandler;
    private long exitTime;
    private TextToSpeech mTts;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 注册一个监听 - 用来收到订单
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.GET_SPEECH);

        /**
         * 获取用户信息
         */
        getUsereInfo();

        setContentView(R.layout.activity_main);
        CoreJob.addToActivityStack(this);

        login = (ImageView) findViewById(R.id.login);
        search = (LinearLayout) findViewById(R.id.search);
        location = (TextView) findViewById(R.id.location);
        first = getIntent().getBooleanExtra("first_login", false);

        login.setOnClickListener(this);
        search.setOnClickListener(this);


        /**
         * 注册聊天监听
         */
        registerEventListener();
        // EMChatManager.getInstance().unregisterEventListener(this);
        //  EventCenter.getInstance().addObserver(this);
        // mActivity = this;

        manager = getSupportFragmentManager();

        // check if the content frame contains the menu frame
        if (findViewById(R.id.menu_frame) == null) {
            setBehindContentView(R.layout.menu_frame);
            getSlidingMenu().setSlidingEnabled(true);
//			getSlidingMenu()
//					.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        } else {
            // add a dummy view
            View v = new View(this);
            setBehindContentView(v);
            getSlidingMenu().setSlidingEnabled(false);
            //getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }

        getSlidingMenu().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        // set the Above View Fragment
//		if (savedInstanceState != null) {
//			if(null!=manager)
//			mContent = (com.shopex.westore.activity.MainTabContentFragment)manager.getFragment(savedInstanceState, "mContent");
//		}

        if (mapFragment == null) {
            mapFragment = new MapFragment();
            mapFragment.setDate(MainActivity.this, new MapFragment.CallBack() {
                @Override
                public void location(String city, double Longitude, double Latitude) {
                    if (city.contains("null")){
                        String aNull = city.replace("null", "");
                        location.setText(aNull);
                        city1 = aNull;
                        Longitude1 = Longitude;
                        Latitude1 = Latitude;
                    }else{
                        location.setText(city);
                        city1 = city;
                        Longitude1 = Longitude;
                        Latitude1 = Latitude;
                    }
                }
            });
        }
        this.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mapFragment).commit();

        /**
         * 设置左边菜单栏
         */
        sm = getSlidingMenu();
        WindowManager wm = (WindowManager) this.getWindowManager();
        sm.setBehindOffset(wm.getDefaultDisplay().getWidth() * 1 / 5);
//		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeEnabled(false);
        sm.setBehindScrollScale(0.25f);
        sm.setFadeDegree(0.25f);
        sm.setBackgroundResource(R.mipmap.point_default);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
    }

    /**
     * 初始化显示做菜单
     * @param savedInstanceState
     */
    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().post(new Runnable() {
            public void run() {
                /**
                 * 是否登录
                 */
                if (com.ximai.savingsmore.save.modle.LoginUser.getInstance().isLogin()){
                    sm.showMenu();
                }
            }
        });
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login://左边菜单栏
                if (sm != null) {
                    if (!sm.isShown()) {
                        sm.showContent();
                    } else {
                        sm.showMenu();
                    }

                    /**
                     * 关闭popuwindow弹框
                     */
                    NotificationCenter.defaultCenter().postNotification(Constants.CLOSE_POPUWINDOW,"");
                }
                break;
            case R.id.list://搜索点击
                if (Longitude1 != 0 && 0 != Latitude1) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra("long", Longitude1);
                    intent.putExtra("lat", Latitude1);
                    intent.putExtra("title", city1);
                    startActivity(intent);
                }
                break;
            case R.id.search://商品搜索
                Intent intent1 = new Intent(MainActivity.this, SearchActivitys.class);
                startActivity(intent1);
                /**
                 * 关闭popuwindow弹框
                 */
                NotificationCenter.defaultCenter().postNotification(Constants.CLOSE_POPUWINDOW,"");
                break;
        }
    }

    /**
     * onDestroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.GET_SPEECH);//语音销毁
    }

    /**
     * 个人获的积分dialog
     */
    private void show_person() {
        alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.person_nofity, null);
        alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final ImageView queding, quxiao;
        queding = (ImageView) view.findViewById(R.id.known);
        quxiao = (ImageView) view.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        alertDialog1.setView(view);
        alertDialog1.show();
    }

    /**
     * 商家入驻成功dialog
     */
    private void show_business() {
        alertDialog1 = new AlertDialog.Builder(MainActivity.this).create();
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.business_notify, null);
        alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        final ImageView queding, quxiao;
        queding = (ImageView) view.findViewById(R.id.known);
        quxiao = (ImageView) view.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        queding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddGoodsAcitivyt.class);
                startActivity(intent);
                alertDialog1.dismiss();
            }
        });
        alertDialog1.setView(view);
        alertDialog1.show();
    }

    /**
     * 事件总线
     * @param emNotifierEvent
     */
    @Override
    public void onEvent(EMNotifierEvent emNotifierEvent) {
        int message = PreferencesUtils.getInt(MainActivity.this, MyUserInfoUtils.getInstance().myUserInfo.NickName, 0);
        message = message + 1;
        PreferencesUtils.putInt(MainActivity.this, MyUserInfoUtils.getInstance().myUserInfo.NickName, message);
        Message msg = mHandler.obtainMessage();
        if (MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("2")) {
            msg.what = 0;
        } else {
            msg.what = 1;
        }
        msg.obj = message;
        mHandler.sendMessage(msg);
    }

    /**
     * 注册一个监听
     */
    private void registerEventListener() {
        EMChatManager.getInstance().registerEventListener(this,
                new EMNotifierEvent.Event[]{EMNotifierEvent.Event.EventNewMessage, EMNotifierEvent.Event.EventOfflineMessage, EMNotifierEvent.Event.EventConversationListChanged});
    }

    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    /**
     * 再按一次退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {//System.currentTimeMillis()无论何时调用，肯定大于2000
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode , event);
    }


    /**
     * 得到用户手机号和密码的登录的信息
     */
    private void getUsereInfo() {
        WebRequestHelper.json_post(MainActivity.this, URLText.GET_USERINFO, RequestParamsPool.getUserInfo(), new MyAsyncHttpResponseHandler(MainActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String MianData = object.optString("MainData");
                MyUserInfoUtils.getInstance().myUserInfo = GsonUtils.fromJson(MianData, MyUserInfo.class);

                /**
                 * type 如果是3那就是商家登录   --  如果是2那就是个人登录 -
                 */
                if (null != MyUserInfoUtils.getInstance().myUserInfo && MyUserInfoUtils.getInstance().myUserInfo.UserType.equals("3")) {
                    if (first) {
                        show_business();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new BusinessFragment()).commit();
                } else {
                    if (first) {
                        show_person();
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new PersonFragment()).commit();
                }

                //如果是环信Id的有的话
                if (null != MyUserInfoUtils.getInstance().myUserInfo.IMUserName && null != MyUserInfoUtils.getInstance().myUserInfo.IMPassword) {
                    requestLoginEaseChat(MyUserInfoUtils.getInstance().myUserInfo.IMUserName, MyUserInfoUtils.getInstance().myUserInfo.IMPassword);
                }
            }
        });
    }

    /**
     * 注册聊天的信息
     * @param accountStr
     * @param pwd
     */
    public void requestLoginEaseChat(final String accountStr, final String pwd) {
        EMChatManager.getInstance().login(accountStr, pwd, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                // EMGroupManager.getInstance().loadAllGroups();//加载群组 木有此功能
                EMChatManager.getInstance().loadAllConversations();

                // 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
                boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(BaseApplication.currentUserNick.trim());
                if (!updatenick) {
                    LogUtils.instance.d("update current user nick fail");
                }
            }
            @Override
            public void onProgress(int progress, String status) {}
            @Override
            public void onError(int code, String message) {}
        });
    }


    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName,Object cid) {
            try {
                if (Constants.GET_SPEECH.equals(eventName)) {
                    /**
                     * 收到订单发出语音
                     */
                    VoiceUtils.getInstance().initmTts(MainActivity.this,"省又省订单有新动态了");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }
}
