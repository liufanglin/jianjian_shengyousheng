package com.ximai.savingsmore.save.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.FileSystem.FileSystem;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.library.view.RoundImageView;
import com.ximai.savingsmore.library.view.ScrollViewWithListView;
import com.ximai.savingsmore.save.activity.CollectCenterActivity;
import com.ximai.savingsmore.save.activity.HotSalesGoods;
import com.ximai.savingsmore.save.activity.MainActivity;
import com.ximai.savingsmore.save.activity.MessageCenterActivity;
import com.ximai.savingsmore.save.activity.OrderCenterCeActivity;
import com.ximai.savingsmore.save.activity.PersonalMyMessageActivity;
import com.ximai.savingsmore.save.activity.PointManagerActivity;
import com.ximai.savingsmore.save.activity.PushMessageActivity;
import com.ximai.savingsmore.save.activity.RebateApplyCenterCeActivity;
import com.ximai.savingsmore.save.activity.SearchActivitys;
import com.ximai.savingsmore.save.activity.SettingActivity;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.GoodSalesType;
import com.ximai.savingsmore.save.modle.GoodSalesTypeList;
import com.ximai.savingsmore.save.modle.ListBaseMessage;
import com.ximai.savingsmore.save.modle.MenuNumber;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.PushSettings;
import com.ximai.savingsmore.save.modle.ShareData;
import com.ximai.savingsmore.save.utils.EventCenter;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.ShareUtils;
import com.ximai.savingsmore.save.utils.VoiceUtils;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by caojian on 16/11/21.
 * 个人侧栏
 */
public class PersonFragment extends Fragment implements View.OnClickListener,Observer {
    private RoundImageView head;
    private RelativeLayout hot_sales;
    private TextView name;
    private RelativeLayout search, collect, message_center, point_manager, order_center;
    private String result;
    private List<BaseMessage> list = new ArrayList<>();
    private List<BaseMessage> isbag_good_one_classify = new ArrayList<>();
    private List<BaseMessage> nobag_good_one_classify = new ArrayList<>();
    private ScrollViewWithListView is_bag_list, no_bag_list;
    private MyAdpter1 myAdpter1;
    private MyAdpter2 myAdpter2;
    List<PushSettings> push_list = new ArrayList<>();
    private RelativeLayout rl_pushdata;
    private ImageView Pack_image;
    private LinearLayout sales_good_push;
    private ImageView is_bag_pack;
    private ImageView no_bag_pack;
    private List<GoodSalesType> goodSalesTypes = new ArrayList<>();
    private TextView distance;
    private String distance_Id;
    private TextView hot, comment, share, order, jifen;
    private RelativeLayout share_app;
    private ShareUtils shareUtils = null;
    private ImageView jifen_image;
    private LinearLayout jifen_push;
    private ImageView share_pack, comment_pack, buy_pack;
    private boolean share_open, comment_open, buy_open;
    private TextView message_number;
    private MainActivity mActivity;
    private RelativeLayout business_item;
    private LinearLayout business_distance;
    private RelativeLayout buxian,wubai,yiqian,sanqian,more,rl_mecenter,rl_pack,rl_jifen;
    private ImageView buxian_image,wubai_image,yiqian_image,sanqian_iamge,more_image,business_images;
    private View view;
    private List<String> businessNum = new ArrayList<>();
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                /**
                 * 收到订单发出语音
                 */
                VoiceUtils.getInstance().initmTts(getContext(),"省又省来消息了");
            }
        }
    };

    private TextView tv_searchcuxiao;
    private RelativeLayout rl_rebate;
    private TextView tv_rebate;
    private RelativeLayout rl_jfflshang;
    private RelativeLayout rl_wyflshang;
    private ImageView iv_jfflreward;
    private ImageView iv_wyflreward;
    private SmsObserver smsObserver;
    private int READ_SMS = 0;
    private ScrollView scroll_view;
    private TextView tv_pushnum;
    private KyLoadingBuilder builder;
    private LinearLayout ll_setting;
    private LinearLayout ll_push;
    private TextView tv_lianxibus;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.person_side_fragment, null);

        /**
         * init - view
         */
        initView();
        /**
         * init- data
         */
        initData();
        /**
         * init - event
         */
        initEvent();
        /**
         * 查询数据
         */
        queryDicNode();


        /**
         * 默认将推送打开
         */
        for (int i = 0; i < isbag_good_one_classify.size(); i++) {
            isbag_good_one_classify.get(i).isSelect = true;
        }
        is_bag_list.setAdapter(myAdpter1);


        for (int i = 0; i < nobag_good_one_classify.size(); i++) {
            nobag_good_one_classify.get(i).isSelect = true;
        }

        distance_Id = "1";
        no_bag_list.setAdapter(myAdpter2);
        share_open = true;
        comment_open = true;
        buy_open = true;
        getPushList();
        return view;
    }

    /**
     * init - view
     */
    private void initView() {
        //为content://sms的数据改变注冊监听器
        smsObserver = new SmsObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsObserver);

        scroll_view = (ScrollView) view.findViewById(R.id.scroll_view);
        head = (RoundImageView) view.findViewById(R.id.user_head);//头像
        name = (TextView) view.findViewById(R.id.name);
        search = (RelativeLayout) view.findViewById(R.id.search);
        collect = (RelativeLayout) view.findViewById(R.id.collect);
        message_center = (RelativeLayout) view.findViewById(R.id.message_center);
        Pack_image = (ImageView) view.findViewById(R.id.pack_image);
        sales_good_push = (LinearLayout) view.findViewById(R.id.sales_good_push);
        is_bag_pack = (ImageView) view.findViewById(R.id.is_bag_pack);
        no_bag_pack = (ImageView) view.findViewById(R.id.no_bag_pack);
        distance = (TextView) view.findViewById(R.id.distance);
        hot = (TextView) view.findViewById(R.id.hot);
        comment = (TextView) view.findViewById(R.id.comment);
        share = (TextView) view.findViewById(R.id.share);
        share_app = (RelativeLayout) view.findViewById(R.id.share_app);
        point_manager = (RelativeLayout) view.findViewById(R.id.jifen_manager);
        order_center = (RelativeLayout) view.findViewById(R.id.order_center);
        order = (TextView) view.findViewById(R.id.order);
        jifen = (TextView) view.findViewById(R.id.jifen);
        jifen_image = (ImageView) view.findViewById(R.id.jifen_image);
        jifen_push = (LinearLayout) view.findViewById(R.id.jifen_push);
        share_pack = (ImageView) view.findViewById(R.id.share_pack);
        comment_pack = (ImageView) view.findViewById(R.id.comment_pack);
        buy_pack = (ImageView) view.findViewById(R.id.buy_pack);
        message_number = (TextView) view.findViewById(R.id.message_number);
        business_item= (RelativeLayout) view.findViewById(R.id.business_distance);
        business_images= (ImageView) view.findViewById(R.id.business_image);
        business_distance= (LinearLayout) view.findViewById(R.id.distance_item);
        buxian= (RelativeLayout) view.findViewById(R.id.buxian);
        yiqian= (RelativeLayout) view.findViewById(R.id.yiqian);
        wubai= (RelativeLayout) view.findViewById(R.id.wubai);
        sanqian= (RelativeLayout) view.findViewById(R.id.sanqian);
        more= (RelativeLayout) view.findViewById(R.id.more);
        buxian_image= (ImageView) view.findViewById(R.id.buxian_image);
        wubai_image= (ImageView) view.findViewById(R.id.wubai_image);
        yiqian_image= (ImageView) view.findViewById(R.id.yiqian_image);
        sanqian_iamge= (ImageView) view.findViewById(R.id.sanqian_image);
        more_image= (ImageView) view.findViewById(R.id.more_image);
        rl_mecenter = (RelativeLayout) view.findViewById(R.id.rl_mecenter);//我的中心
        rl_pack = (RelativeLayout) view.findViewById(R.id.rl_pack);//促销品推送
        rl_jifen = (RelativeLayout) view.findViewById(R.id.rl_jifen);//积分推送
        tv_searchcuxiao = (TextView) view.findViewById(R.id.tv_searchcuxiao);//搜索促销
        hot_sales = (RelativeLayout) view.findViewById(R.id.hot_sales);
        ll_setting = (LinearLayout) view.findViewById(R.id.ll_setting);
        is_bag_list = (ScrollViewWithListView) view.findViewById(R.id.is_bag_list);
        no_bag_list = (ScrollViewWithListView) view.findViewById(R.id.no_bag_list);
        rl_rebate = (RelativeLayout) view.findViewById(R.id.rl_rebate);//我要返利
        tv_rebate = (TextView) view.findViewById(R.id.tv_rebate);
        rl_jfflshang = (RelativeLayout) view.findViewById(R.id.rl_jfflshang);//积分返利赏
        rl_wyflshang = (RelativeLayout) view.findViewById(R.id.rl_wyflshang);//我要返利赏
        iv_jfflreward = (ImageView) view.findViewById(R.id.iv_jfflreward);
        iv_wyflreward = (ImageView) view.findViewById(R.id.iv_wyflreward);
        rl_pushdata = (RelativeLayout) view.findViewById(R.id.rl_pushdata);//推送数据
        tv_pushnum = (TextView) view.findViewById(R.id.tv_pushnum);//推送数量
        ll_push = (LinearLayout) view.findViewById(R.id.ll_push);
        tv_lianxibus = (TextView) view.findViewById(R.id.tv_lianxibus);
    }

    /**
     * data
     */
    private void initData() {
        /**
         * 注册一个监听 - 数据刷新
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.PERSONAL_CEBIANLAN);
        /**
         * 注册一个监听 - 商家发布商品显示发布数据
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.BUSSINESS_FABU);
        /**
         * 附近商家
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.FUJIN_BUSINESS);

        if (null != MyUserInfoUtils.getInstance().myUserInfo) {
            RequestOptions options = new RequestOptions()               //Glide升级
                    .centerCrop()
//                    .placeholder(com.easemob.easeui.R.drawable.ease_default_expression)
                        .error(R.mipmap.head_image)
                    .priority(Priority.HIGH);
            Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.PhotoPath).apply(options).into(head);

            name.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
        }
        myAdpter1 = new MyAdpter1();
        myAdpter2 = new MyAdpter2();
        is_bag_list.setAdapter(myAdpter1);
        no_bag_list.setAdapter(myAdpter2);

        String pushNum = PreferencesUtils.getString(getContext(), "pushNum", "");//设置未读的推送消息
        if (!TextUtils.isEmpty(pushNum)){
            Log.e("pushNum",pushNum);
            rl_pushdata.setVisibility(View.VISIBLE);
            tv_pushnum.setText(pushNum);
        }

        /**
         * 如果没有给读取短信的权限那么进行权限的申请
         */
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, READ_SMS);//自定义的code
        }
    }

    /**
     * 一个继承自ContentObserver的监听器类
     */
    class SmsObserver extends ContentObserver{
        public SmsObserver(Handler handler) {
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange) {
            try{
                /**
                 * 6．0登陆的时候请求定位权限
                 */
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, READ_SMS);//自定义的code
                }else{
                    //查询发送向箱中的短信
                    Cursor cursor=getActivity().getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
                    //遍历查询结果获取用户正在发送的短信
                    while (cursor.moveToNext()) {
                        String sms = PreferencesUtils.getString(getActivity(), "SMS", "");
    //                StringBuffer sb=new StringBuffer();
                        //获取短信的发送地址
    //                sb.append("发送地址："+cursor.getString(cursor.getColumnIndex("address")));
                        //获取短信的标题
    //                sb.append("\n标题："+cursor.getString(cursor.getColumnIndex("subject")));
                        //获取短信的内容
    //                sb.append("\n内容："+cursor.getString(cursor.getColumnIndex("body")));
                        //获取短信的发送时间
    //                Date date=new Date(cursor.getLong(cursor.getColumnIndex("date")));
    //                //格式化以秒为单位的日期
    //                SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日 hh时mm分ss秒");
    //                sb.append("\n时间："+sdf.format(date));
    //                System.out.println("查询到的正在发送的短信："+sb.toString());
    //                Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();发送地址：18655280203
                        if ("1".equals(sms)){
                            String s = "各品牌实体门店都在促销";
                            String s1 = "您的朋友";
                            if (cursor.getString(cursor.getColumnIndex("body")).contains(s) || cursor.getString(cursor.getColumnIndex("body")).contains(s1)){//是否包含
                                PreferencesUtils.putString(getActivity(),"SMS","2");
                                shareApp("短信");
                            }
                        }
                        break;
                    }
    //            super.onChange(selfChange);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 取消注册监听
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getContentResolver().unregisterContentObserver(smsObserver);
    }

    /**
     * event
     */
    private void initEvent() {
        rl_pack.setOnClickListener(this);
        rl_jifen.setOnClickListener(this);
        buxian.setOnClickListener(this);
        yiqian.setOnClickListener(this);
        wubai.setOnClickListener(this);
        sanqian.setOnClickListener(this);
        more.setOnClickListener(this);
        business_item.setOnClickListener(this);
        share_pack.setOnClickListener(this);
        comment_pack.setOnClickListener(this);
        buy_pack.setOnClickListener(this);
        jifen_image.setOnClickListener(this);
        point_manager.setOnClickListener(this);
        order_center.setOnClickListener(this);
        share_app.setOnClickListener(this);
        distance.setOnClickListener(this);
        is_bag_pack.setOnClickListener(this);
        no_bag_pack.setOnClickListener(this);
        Pack_image.setOnClickListener(this);
        ll_push.setOnClickListener(this);
        collect.setOnClickListener(this);
        search.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        hot_sales.setOnClickListener(this);
        message_center.setOnClickListener(this);
        head.setOnClickListener(this);
        rl_mecenter.setOnClickListener(this);
        tv_lianxibus.setOnClickListener(this);
        rl_rebate.setOnClickListener(this);//我要返利
        rl_jfflshang.setOnClickListener(this);//积分返利赏
        rl_wyflshang.setOnClickListener(this);//我要返利赏

        is_bag_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//是否推送点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < isbag_good_one_classify.size(); i++) {
                    if (i == position) {
                        if (isbag_good_one_classify.get(position).isSelect) {
                            isbag_good_one_classify.get(position).isSelect = false;
                            Toast.makeText(getActivity(), "取消推送", Toast.LENGTH_SHORT).show();
                        } else {
                            isbag_good_one_classify.get(position).isSelect = true;
                            Toast.makeText(getActivity(), "开启推送", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                myAdpter1.notifyDataSetChanged();
                getPushList();
            }
        });
        no_bag_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {//是否推送点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < nobag_good_one_classify.size(); i++) {
                    if (i == position) {
                        if (nobag_good_one_classify.get(position).isSelect) {
                            nobag_good_one_classify.get(position).isSelect = false;
                            Toast.makeText(getActivity(), "取消推送", Toast.LENGTH_SHORT).show();
                        } else {
                            nobag_good_one_classify.get(position).isSelect = true;
                            Toast.makeText(getActivity(), "开启推送", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
                getPushList();
                myAdpter2.notifyDataSetChanged();
            }
        });
        EventCenter.getInstance().addObserver(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        menu_number();
        /**
         * 环信Id可能为空
         */
        try{
            EMConversation conversation = EMChatManager.getInstance().getConversation(MyUserInfoUtils.getInstance().myUserInfo.IMId);
            int unreadMsgCount = conversation.getUnreadMsgCount();//聊天信息未读
            Hashtable<String, EMConversation> allConversations = EMChatManager.getInstance().getAllConversations();
            if (null != allConversations){
                message_number.setText(allConversations.size() + "");//获取聊天信息的列表数据
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        message_number.setText(PreferencesUtils.getInt(getActivity(), MyUserInfoUtils.getInstance().myUserInfo.NickName, 0) + "");

        try{
//            if (null != MyUserInfoUtils.getInstance().myUserInfo.NickName){
////                message_number.setText(PreferencesUtils.getInt(getActivity(), MyUserInfoUtils.getInstance().myUserInfo.NickName, 0) + "");
////                EMConversation conversation = EMChatManager.getInstance().getConversation(MyUserInfoUtils.getInstance().myUserInfo.NickName);
////                int msgCount = conversation.getUnreadMsgCount();
//                message_number.setText(PreferencesUtils.getInt(getActivity(), MyUserInfoUtils.getInstance().myUserInfo.NickName, 0) + "");
//            }
            if (null != MyUserInfoUtils.getInstance() && null != MyUserInfoUtils.getInstance().myUserInfo) {
                if (null != name && null != head) {
                    if (null != MyUserInfoUtils.getInstance().myUserInfo) {
                        RequestOptions options = new RequestOptions()               //Glide升级
                                .centerCrop()
//                    .placeholder(com.easemob.easeui.R.drawable.ease_default_expression)
                                .error(R.mipmap.head_image)
                                .priority(Priority.HIGH);

                        Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.PhotoPath).apply(options).into(head);
                        name.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        mActivity.setHandler(handler);
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buxian://距离
                distance_Id = "0";
                getPushList();
                buxian_image.setBackgroundResource(R.mipmap.select);
                wubai_image.setBackgroundResource(R.mipmap.noselect);
                yiqian_image.setBackgroundResource(R.mipmap.noselect);
                sanqian_iamge.setBackgroundResource(R.mipmap.noselect);
                more_image.setBackgroundResource(R.mipmap.noselect);
                break;
            case R.id.wubai://距离
                distance_Id ="1";
                getPushList();
                buxian_image.setBackgroundResource(R.mipmap.noselect);
                wubai_image.setBackgroundResource(R.mipmap.select);
                yiqian_image.setBackgroundResource(R.mipmap.noselect);
                sanqian_iamge.setBackgroundResource(R.mipmap.noselect);
                more_image.setBackgroundResource(R.mipmap.noselect);
                break;
            case R.id.yiqian://距离
                distance_Id = "2";
                getPushList();
                buxian_image.setBackgroundResource(R.mipmap.noselect);
                wubai_image.setBackgroundResource(R.mipmap.noselect);
                yiqian_image.setBackgroundResource(R.mipmap.select);
                sanqian_iamge.setBackgroundResource(R.mipmap.noselect);
                more_image.setBackgroundResource(R.mipmap.noselect);
                break;
            case R.id.sanqian://距离
                distance_Id = "3";
                getPushList();
                buxian_image.setBackgroundResource(R.mipmap.noselect);
                wubai_image.setBackgroundResource(R.mipmap.noselect);
                yiqian_image.setBackgroundResource(R.mipmap.noselect);
                sanqian_iamge.setBackgroundResource(R.mipmap.select);
                more_image.setBackgroundResource(R.mipmap.noselect);
                break;
            case R.id.more://距离
                distance_Id = "4";
                getPushList();
                buxian_image.setBackgroundResource(R.mipmap.noselect);
                wubai_image.setBackgroundResource(R.mipmap.noselect);
                yiqian_image.setBackgroundResource(R.mipmap.noselect);
                sanqian_iamge.setBackgroundResource(R.mipmap.noselect);
                more_image.setBackgroundResource(R.mipmap.select);
                break;
            case R.id.business_distance://商家距离
                if(business_distance.getVisibility()==View.VISIBLE){
                    business_distance.setVisibility(View.GONE);
                    business_images.setBackgroundResource(R.mipmap.search_up3);
                } else {
                    business_distance.setVisibility(View.VISIBLE);
                    business_images.setBackgroundResource(R.mipmap.search_dowm3);
                    /**
                     * 需要注意的是，该方法不能直接被调用
                     因为Android很多函数都是基于消息队列来同步，所以需要一部操作，
                     addView完之后，不等于马上就会显示，而是在队列中等待处理，虽然很快，但是如果立即调用fullScroll， view可能还没有显示出来，所以会失败
                     应该通过handler在新线程中更新
                     */
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                break;
            case R.id.share_app:
                PreferencesUtils.putString(getActivity(),"isGoodsShark","2");// 1 - 是   2- 不是
                ShareData data = new ShareData();
                data.setTitleUrl("http://login.savingsmore.com/Home/Download");
                data.setUrl("http://login.savingsmore.com/Home/Download");
                data.setTitle("门店在促销,每天\"11.11\"");
                data.setBitmap(getBitmapFormResources(getActivity(),R.raw.ximaiicon));
               // data.setImagePath(FileSystem.getCachesDir(getActivity(), true).getAbsolutePath() + File.separator + "icon.jpg");
//                if (null == MyUserInfoUtils.getInstance().myUserInfo.ShowName){
//                    data.setText("您的好友给您推荐了一个促销专用APP，快去看！更多促销，一搜就知道。");
//                }else{
//                    data.setText("您的好友"+ MyUserInfoUtils.getInstance().myUserInfo.ShowName +"给您推荐了一个促销专用APP，快去看！更多促销，一搜就知道。");
//                }
                data.setText("各家门店做促销，省又省App全有！样样有品质，件件有折扣！下载一个App，足够！");

                shareUtils = new ShareUtils(data, getActivity());
                shareUtils.show(share_app);
                break;
            case R.id.distance://--------------------------------------------------------------------
//                PopupWindowFromBottomUtil.shouSalesType(getActivity(), LayoutInflater.from(getActivity()).inflate(R.layout.business_my_center_activity, null), goodSalesTypes, new PopupWindowFromBottomUtil.Listenrt3() {
//                    @Override
//                    public void callback(GoodSalesType goodSalesType, PopupWindow popupWindow) {
//                        distance.setText(goodSalesType.Value);
//                        distance_Id = goodSalesType.Id;
//                        getPushList();
//                        popupWindow.dismiss();
//                    }
//                });
//                break;
            case R.id.share_pack://分享积分
                if (share_open) {
                    share_open = false;
                    share_pack.setBackgroundResource(R.mipmap.is_bag_pack);
                    Toast.makeText(getActivity(), "取消推送", Toast.LENGTH_SHORT).show();
                } else {
                    share_open = true;
                    share_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                    Toast.makeText(getActivity(), "开启推送", Toast.LENGTH_SHORT).show();
                }
                getPushList();
                break;
            case R.id.comment_pack://品论积分
                if (comment_open) {
                    comment_open = false;
                    comment_pack.setBackgroundResource(R.mipmap.is_bag_pack);
                    Toast.makeText(getActivity(), "取消推送", Toast.LENGTH_SHORT).show();
                } else {
                    comment_open = true;
                    comment_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                    Toast.makeText(getActivity(), "开启推送", Toast.LENGTH_SHORT).show();
                }
                getPushList();
                break;
            case R.id.buy_pack://购买积分
                if (buy_open) {
                    buy_open = false;
                    buy_pack.setBackgroundResource(R.mipmap.is_bag_pack);
                    Toast.makeText(getActivity(), "取消推送", Toast.LENGTH_SHORT).show();
                } else {
                    buy_open = true;
                    buy_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                    Toast.makeText(getActivity(), "开启推送", Toast.LENGTH_SHORT).show();
                }
                getPushList();
                break;
            case R.id.rl_jifen://积分推送-------------------------------------------------------------
                if (jifen_push.getVisibility() == View.GONE) {
                    jifen_push.setVisibility(View.VISIBLE);
                    jifen_image.setBackgroundResource(R.mipmap.search_dowm3);
                } else {
                    jifen_push.setVisibility(View.GONE);
                    jifen_image.setBackgroundResource(R.mipmap.search_up3);
                }
                break;
            case R.id.is_bag_pack://产品类商品---------------------------------------------------------
                if (is_bag_list.getVisibility() == View.VISIBLE) {
                    is_bag_list.setVisibility(View.GONE);
                    is_bag_pack.setBackgroundResource(R.mipmap.is_bag_pack);
                    for (int i = 0; i < isbag_good_one_classify.size(); i++) {
                        isbag_good_one_classify.get(i).isSelect = false;
                    }
                    is_bag_list.setAdapter(myAdpter1);
                    getPushList();
                    Toast.makeText(getActivity(), "取消推送", Toast.LENGTH_SHORT).show();
                } else {
                    is_bag_list.setVisibility(View.VISIBLE);
                    is_bag_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                    for (int i = 0; i < isbag_good_one_classify.size(); i++) {
                        isbag_good_one_classify.get(i).isSelect = true;
                    }
                    is_bag_list.setAdapter(myAdpter1);
                    getPushList();
                    Toast.makeText(getActivity(), "开启推送", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.no_bag_pack://非产品类商品-----------------------------------------------------
                if (no_bag_list.getVisibility() == View.VISIBLE) {
                    no_bag_list.setVisibility(View.GONE);
                    no_bag_pack.setBackgroundResource(R.mipmap.is_bag_pack);
                    for (int i = 0; i < nobag_good_one_classify.size(); i++) {
                        nobag_good_one_classify.get(i).isSelect = false;
                    }
                    no_bag_list.setAdapter(myAdpter2);
                    getPushList();
                    Toast.makeText(getActivity(), "取消推送", Toast.LENGTH_SHORT).show();
                } else {
                    no_bag_list.setVisibility(View.VISIBLE);
                    no_bag_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                    for (int i = 0; i < nobag_good_one_classify.size(); i++) {
                        nobag_good_one_classify.get(i).isSelect = true;
                    }
                    no_bag_list.setAdapter(myAdpter2);
                    getPushList();
                    Toast.makeText(getActivity(), "开启推送", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_pack://促销品推送------------------------------------------------------------
                if (sales_good_push.getVisibility() == View.VISIBLE) {
                    Pack_image.setBackgroundResource(R.mipmap.search_up3);
                    sales_good_push.setVisibility(View.GONE);
                } else {
                    Pack_image.setBackgroundResource(R.mipmap.search_dowm3);
                    sales_good_push.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.ll_push://推送消息
                Intent intent7 = new Intent(getActivity(), PushMessageActivity.class);
                startActivity(intent7);
                rl_pushdata.setVisibility(View.GONE);
                tv_pushnum.setText("");//现在用数量显示
                PreferencesUtils.putString(getContext(),"pushNum","");
                break;
            case R.id.ll_setting://设置
                Intent intent0 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent0);
                break;
            case R.id.hot_sales:
                Intent intent = new Intent(getActivity(), HotSalesGoods.class);
                intent.putExtra("title", "最热门促销");
                startActivity(intent);
                break;
            case R.id.search://搜索促销
                Intent intent1 = new Intent(getActivity(), SearchActivitys.class);
                startActivity(intent1);
                break;
            case R.id.collect://收藏中心
                Intent intent2 = new Intent(getActivity(), CollectCenterActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_mecenter://我的中心
                Intent intent3 = new Intent(getActivity(), PersonalMyMessageActivity.class);
                startActivity(intent3);
                break;
            case R.id.message_center://信息中心
                Intent intent6 = new Intent(getActivity(), MessageCenterActivity.class);
                intent6.putExtra("list", result);
                startActivity(intent6);
                break;
            case R.id.jifen_manager://积分返利
                Intent intent4 = new Intent(getActivity(), PointManagerActivity.class);
                startActivity(intent4);
                break;
            case R.id.order_center://订单中心
                Intent intent5 = new Intent(getActivity(), OrderCenterCeActivity.class);
                intent5.putExtra("title", "收货中心");
                startActivity(intent5);
                break;
            case R.id.rl_rebate://我要返利
                startActivity(new Intent(getActivity(), RebateApplyCenterCeActivity.class));
                break;
            case R.id.tv_lianxibus://联系商家
                Intent intent8 = new Intent(getActivity(), SearchActivitys.class);
                intent8.putExtra("SearchActivitys","SearchResultActivity");
                startActivity(intent8);
                break;
//            case R.id.rl_jfflshang://积分返利 - 赏
//                Intent intent8 = new Intent(getActivity(),PersonalRewardActivity.class);
//                startActivity(intent8);
//                break;
//            case R.id.rl_wyflshang://我要返利 - 赏
//                Intent intent9 = new Intent(getActivity(),PersonalRewardActivity.class);
//                startActivity(intent9);
//                break;
            default:
                break;
        }
    }

    /**
     * 查询基础字典queryDicNode()
     */
    private void queryDicNode() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(getActivity(), URLText.QUERYDICNODE, stringEntity, new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                ListBaseMessage listBaseMessage = GsonUtils.fromJson(new String(responseBody), ListBaseMessage.class);
                list = listBaseMessage.MainData;
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("ba335639-52c2-4e8d-8d2b-faf8ed097418") && list.get(i).IsBag.equals("true")) {
                        isbag_good_one_classify.add(list.get(i));
                    } else if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("ba335639-52c2-4e8d-8d2b-faf8ed097418") && list.get(i).IsBag.equals("false")) {
                        nobag_good_one_classify.add(list.get(i));
                    }
                }
                if (null != MyUserInfoUtils.getInstance().myUserInfo.PushSettings) {
                    for (int i = 0; i < MyUserInfoUtils.getInstance().myUserInfo.PushSettings.size(); i++) {
                        for (int j = 0; j < isbag_good_one_classify.size(); j++) {
                            if (MyUserInfoUtils.getInstance().myUserInfo.PushSettings.get(i).PushValue.equals(isbag_good_one_classify.get(j).Id)) {
                                isbag_good_one_classify.get(j).isSelect = true;
                                is_bag_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                                is_bag_list.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    for (int i = 0; i < MyUserInfoUtils.getInstance().myUserInfo.PushSettings.size(); i++) {
                        for (int j = 0; j < nobag_good_one_classify.size(); j++) {
                            if (MyUserInfoUtils.getInstance().myUserInfo.PushSettings.get(i).PushValue.equals(nobag_good_one_classify.get(j).Id)) {
                                nobag_good_one_classify.get(j).isSelect = true;
                                no_bag_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                                no_bag_list.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                queryDicNode2();
                myAdpter2.notifyDataSetChanged();
                myAdpter1.notifyDataSetChanged();
            }
        });
    }

    /**
     *推送消息
     * @param observable
     * @param data
     */
    @Override
    public void update(Observable observable, Object data) {
        if (EventCenter.isContainsEnent(EventCenter.ADD_RED_DOT_ON_DISCOVERYTAB)) {
            //先不显示
        }
    }

    /**
     * 侧边栏产品类商品
     */
    public class MyAdpter1 extends BaseAdapter {
        @Override
        public int getCount() {
            return isbag_good_one_classify.size();
        }
        @Override
        public Object getItem(int position) {
            return isbag_good_one_classify.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodel viewHodel;
            if (null == convertView) {
                viewHodel = new ViewHodel();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.push_select_item, null);
                viewHodel.name = (TextView) convertView.findViewById(R.id.push_item_name);
                viewHodel.iamge = (ImageView) convertView.findViewById(R.id.push_item_image);
                viewHodel.view_line = convertView.findViewById(R.id.view_line);
                convertView.setTag(viewHodel);
            }
            viewHodel = (ViewHodel) convertView.getTag();
            viewHodel.name.setText(isbag_good_one_classify.get(position).Name);
            if (isbag_good_one_classify.get(position).isSelect) {
                viewHodel.iamge.setImageResource(R.mipmap.select);
            } else {
                viewHodel.iamge.setImageResource(R.mipmap.noselect);
            }

            if ((position+1) % 4 == 0){//推送消息每隔4项就分开
                viewHodel.view_line.setVisibility(View.VISIBLE);
            }else{
                viewHodel.view_line.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    public class MyAdpter2 extends BaseAdapter {
        @Override
        public int getCount() {
            return nobag_good_one_classify.size();
        }
        @Override
        public Object getItem(int position) {
            return nobag_good_one_classify.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHodel viewHodel;
            if (null == convertView) {
                viewHodel = new ViewHodel();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.push_select_item, null);
                viewHodel.name = (TextView) convertView.findViewById(R.id.push_item_name);
                viewHodel.iamge = (ImageView) convertView.findViewById(R.id.push_item_image);
                viewHodel.view_line = convertView.findViewById(R.id.view_line);
                convertView.setTag(viewHodel);
            }
            viewHodel = (ViewHodel) convertView.getTag();
            viewHodel.name.setText(nobag_good_one_classify.get(position).Name);
            if (nobag_good_one_classify.get(position).isSelect) {
                viewHodel.iamge.setImageResource(R.mipmap.select);
            } else {
                viewHodel.iamge.setImageResource(R.mipmap.noselect);
            }

            if ((position+1) % 4 == 0){//推送消息每隔4项就分开
                viewHodel.view_line.setVisibility(View.VISIBLE);
            }else{
                viewHodel.view_line.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
    static class ViewHodel {
        TextView name;
        ImageView iamge;
        View view_line;
    }

    /**
     * 设置推送
     */
    private void setPush(boolean IsPush, List<PushSettings> list) {
        WebRequestHelper.json_post(getActivity(), URLText.SET_PUSH, RequestParamsPool.savePush(IsPush, list), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = new JSONObject();
                //Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getPushList() {
        push_list.clear();
        for (int i = 0; i < isbag_good_one_classify.size(); i++) {
            if (isbag_good_one_classify.get(i).isSelect) {
                PushSettings pushSettings = new PushSettings();
                pushSettings.PushType = "1";
                pushSettings.PushValue = isbag_good_one_classify.get(i).Id;
                push_list.add(pushSettings);
            }
        }
        for (int i = 0; i < nobag_good_one_classify.size(); i++) {
            if (nobag_good_one_classify.get(i).isSelect) {
                PushSettings pushSettings = new PushSettings();
                pushSettings.PushType = "1";
                pushSettings.PushValue = nobag_good_one_classify.get(i).Id;
                push_list.add(pushSettings);
            }
        }
        if (null != distance_Id) {
            PushSettings pushSettings = new PushSettings();
            pushSettings.PushType = "2";
            pushSettings.PushValue = distance_Id;
            push_list.add(pushSettings);
        }
        if (share_open) {
            PushSettings pushSettings = new PushSettings();
            pushSettings.PushType = "4";
            pushSettings.PushValue = "true";
            push_list.add(pushSettings);
        }
        if (comment_open) {
            PushSettings pushSettings = new PushSettings();
            pushSettings.PushType = "5";
            pushSettings.PushValue = "true";
            push_list.add(pushSettings);
        }
        if (buy_open) {
            PushSettings pushSettings = new PushSettings();
            pushSettings.PushType = "6";
            pushSettings.PushValue = "true";
            push_list.add(pushSettings);
        }
        setPush(true, push_list);
    }

    /**
     * 查询促销形式
     */
    private void queryDicNode2() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(getActivity(), URLText.QUERYDICNODE3, stringEntity, new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                GoodSalesTypeList goodSalesTypeList = GsonUtils.fromJson(new String(responseBody), GoodSalesTypeList.class);
                goodSalesTypes = goodSalesTypeList.ShowData;
                for (int i = 0; i < MyUserInfoUtils.getInstance().myUserInfo.PushSettings.size(); i++) {
                    for (int j = 0; j < goodSalesTypes.size(); j++) {
                        if (MyUserInfoUtils.getInstance().myUserInfo.PushSettings.get(i).PushValue.equals(goodSalesTypes.get(j).Id)) {
                            distance_Id = goodSalesTypes.get(j).Id;
                            if(distance_Id.equals("0")){
                                buxian_image.setBackgroundResource(R.mipmap.select);
                                wubai_image.setBackgroundResource(R.mipmap.noselect);
                                yiqian_image.setBackgroundResource(R.mipmap.noselect);
                                sanqian_iamge.setBackgroundResource(R.mipmap.noselect);
                                more_image.setBackgroundResource(R.mipmap.noselect);
                            }
                            if(distance_Id.equals("1")){
                                buxian_image.setBackgroundResource(R.mipmap.noselect);
                                wubai_image.setBackgroundResource(R.mipmap.select);
                                yiqian_image.setBackgroundResource(R.mipmap.noselect);
                                sanqian_iamge.setBackgroundResource(R.mipmap.noselect);
                                more_image.setBackgroundResource(R.mipmap.noselect);
                            }
                            if(distance_Id.equals("2")){
                                buxian_image.setBackgroundResource(R.mipmap.noselect);
                                wubai_image.setBackgroundResource(R.mipmap.noselect);
                                yiqian_image.setBackgroundResource(R.mipmap.select);
                                sanqian_iamge.setBackgroundResource(R.mipmap.noselect);
                                more_image.setBackgroundResource(R.mipmap.noselect);
                            }
                            if(distance_Id.equals("3")){
                                buxian_image.setBackgroundResource(R.mipmap.noselect);
                                wubai_image.setBackgroundResource(R.mipmap.noselect);
                                yiqian_image.setBackgroundResource(R.mipmap.noselect);
                                sanqian_iamge.setBackgroundResource(R.mipmap.select);
                                more_image.setBackgroundResource(R.mipmap.noselect);
                            }
                            if(distance_Id.equals("4")){
                                buxian_image.setBackgroundResource(R.mipmap.noselect);
                                wubai_image.setBackgroundResource(R.mipmap.noselect);
                                yiqian_image.setBackgroundResource(R.mipmap.noselect);
                                sanqian_iamge.setBackgroundResource(R.mipmap.noselect);
                                more_image.setBackgroundResource(R.mipmap.select);
                            }
                        }
                    }
                    if (MyUserInfoUtils.getInstance().myUserInfo.PushSettings.get(i).PushType.equals("4")) {
                        share_open = true;
                        share_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                    }
                    if (MyUserInfoUtils.getInstance().myUserInfo.PushSettings.get(i).PushType.equals("5")) {
                        comment_open = true;
                        comment_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                    }
                    if (MyUserInfoUtils.getInstance().myUserInfo.PushSettings.get(i).PushType.equals("6")) {
                        buy_open = true;
                        buy_pack.setBackgroundResource(R.mipmap.is_bag_pack_red);
                    }
                }
            }
        });
    }

    /**
     * 侧栏统计
     */
    private void menu_number() {
//        showLoading(getActivity(),"正在加载");
        WebRequestHelper.json_post(getActivity(), URLText.MENU_NUMBER, RequestParamsPool.getMenuNumber(), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    MenuNumber menuNumber = GsonUtils.fromJson(object.optString("MainData"), MenuNumber.class);
                    if (null != menuNumber) {
                        tv_searchcuxiao.setText(menuNumber.Product);//搜索促销的数据
                        hot.setText(menuNumber.Product);//热门促销
                        order.setText(menuNumber.Receipting);
                        jifen.setText(menuNumber.Point);
                        comment.setText(menuNumber.Favourite);
                        share.setText(menuNumber.Shared);
                        tv_rebate.setText(menuNumber.Rebate);
                    }
                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (null != builder){
                        builder.dismiss();
                    }
                }finally {
                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }

    /**
     * 发送短信
     * @param Remark
     */
    private void shareApp(String Remark) {
        WebRequestHelper.json_post(getActivity(), URLText.SHARE_APP, RequestParamsPool.sahreApp(Remark), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                //   Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
                JSONObject object = new JSONObject();
            }
        });
    }

    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName, final Object cid) {
            try {
                if (Constants.PERSONAL_CEBIANLAN.equals(eventName)) {
                    /**
                     *  左侧侧边栏数据
                     */
                    menu_number();
                }else if (Constants.BUSSINESS_FABU.equals(eventName)){
                    Log.e("tag","333333333333333333333333");
                    if (rl_pushdata.getVisibility() == View.GONE){
                        rl_pushdata.setVisibility(View.VISIBLE);
                        tv_pushnum.setText("1");
                    }else{
                        rl_pushdata.setVisibility(View.VISIBLE);
                        String s = tv_pushnum.getText().toString();
                        tv_pushnum.setText(Integer.parseInt(s)+1+"");
                    }
                    //不管是多少 - 都进行一个保存为了下一次进来的还是显示
                    PreferencesUtils.putString(getContext(),"pushNum",tv_pushnum.getText().toString());
                    /**
                     * 发布的促销进行语音
                     */
                    VoiceUtils.getInstance().initmTts(getContext(),"您关注商品开始促销了");

                }else if (Constants.FUJIN_BUSINESS.equals(eventName)){//附近商家正在促销
                    businessNum.add(cid.toString());
                    Log.e("businessNum",businessNum.size()+"");

                    for (int i = 0; i < businessNum.size(); i++) {
//                        BaseApplication.getInstance().tts.speakTexts(cid.toString());
                        VoiceUtils.getInstance().initmTts(getContext(),businessNum.get(i));
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };


    /**
     * 打开loading
     */
    public void showLoading(Context context, String text){
        if (null == builder){
            builder = new KyLoadingBuilder(context);
        }
        builder.setIcon(R.mipmap.loading);
        builder.setText(text);
        builder.setOutsideTouchable(true);
//        builder.setBackTouchable(true);
        builder.show();
    }

    public static Bitmap getBitmapFormResources(Context context, int resId){
        return BitmapFactory.decodeResource(context.getResources(),resId);
    }
}