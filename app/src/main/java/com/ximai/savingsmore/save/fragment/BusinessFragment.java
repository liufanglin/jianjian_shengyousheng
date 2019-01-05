package com.ximai.savingsmore.save.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.ximai.savingsmore.save.activity.BrowseActivity;
import com.ximai.savingsmore.save.activity.BusinessMyCenterActivity;
import com.ximai.savingsmore.save.activity.BusinessRewardActivity;
import com.ximai.savingsmore.save.activity.BussPushMessageActivity;
import com.ximai.savingsmore.save.activity.CollectionAllAcitvity;
import com.ximai.savingsmore.save.activity.FourStepRegisterActivity;
import com.ximai.savingsmore.save.activity.HotSalesGoods;
import com.ximai.savingsmore.save.activity.IssuGoodActivity;
import com.ximai.savingsmore.save.activity.LookThroughActivity;
import com.ximai.savingsmore.save.activity.MainActivity;
import com.ximai.savingsmore.save.activity.MessageCenterActivity;
import com.ximai.savingsmore.save.activity.MyCommentCenterActivity;
import com.ximai.savingsmore.save.activity.OrderCenterCeActivity;
import com.ximai.savingsmore.save.activity.SearchActivitys;
import com.ximai.savingsmore.save.activity.SettingActivity;
import com.ximai.savingsmore.save.activity.UserThroughActivity;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.MenuNumber;
import com.ximai.savingsmore.save.modle.MyUserInfo;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.ShareData;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.ShareUtils;
import com.ximai.savingsmore.save.utils.VoiceUtils;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.ximai.savingsmore.save.view.XiMaiPopDialog1;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by caojian on 16/11/25.
 */
public class BusinessFragment extends Fragment implements View.OnClickListener {
    private RoundImageView head;
    private TextView name;
    private RelativeLayout hot_sales;
    private RelativeLayout search;
    private RelativeLayout myCenter;
    private RelativeLayout fabu;
    private RelativeLayout comment_center;
    private LinearLayout ll_setting;
    private RelativeLayout message_center;
    private String result;
    private TextView hot, product, comment, share, order, liulan_number, collect_number;
    private ShareUtils shareUtils = null;
    private RelativeLayout share_app;
    private RelativeLayout orderCeter;
    private RelativeLayout liulan;
    private RelativeLayout collect;
    private TextView message_number;
    private MainActivity mActivity;
    private TextView tv_whocuxioa;
    private View view;
    private KyLoadingBuilder builder;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                /**
                 * 收到订单发出语音
                 */
                VoiceUtils.getInstance().initmTts(getContext(),"省又省来消息了");
            }
        }
    };

    private RelativeLayout rl_fbcxreward;
    private RelativeLayout rl_fxcxreward;
    private ImageView iv_fbcxreward;
    private ImageView iv_fxcxreward;
    private SmsObserver smsObserver;
    private int READ_SMS = 0;
    private MenuNumber menuNumber;
    private RelativeLayout rl_liulan;
    private TextView tv_liulan;
    private RelativeLayout rl_usergroup;
    private TextView tv_usergroup;
    private LinearLayout ll_push;
    private RelativeLayout rl_pushdata;
    private TextView tv_pushnum;
    private GoodsList goodsList;
    private boolean isComment, isHit, isCollect;
    public Goods shareGoods;
    public View view_dot1;
    public View view_dot2;
    public View view_dot3;
    public View view_dot4;
    public View view_dot5;
    public View view_dot6;
    public View view_dot7;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.business_side_fragment, null);
        /**
         * view
         */
        initView();
        /**
         * data
         */
        initData();
        /**
         * event
         */
        initEvent();
        return view;
    }

    /**
     * view
     */
    private void initView() {
        //为content://sms的数据改变注冊监听器
        smsObserver = new SmsObserver(new Handler());
        getActivity().getContentResolver().registerContentObserver(Uri.parse("content://sms"), true, smsObserver);

        head = (RoundImageView) view.findViewById(R.id.user_head);
        name = (TextView) view.findViewById(R.id.name);
        hot_sales = (RelativeLayout) view.findViewById(R.id.hot_sales);
        order = (TextView) view.findViewById(R.id.order_number);
        search = (RelativeLayout) view.findViewById(R.id.search);
        myCenter = (RelativeLayout) view.findViewById(R.id.my_center);
        comment_center = (RelativeLayout) view.findViewById(R.id.comment_center);
        ll_setting = (LinearLayout) view.findViewById(R.id.ll_setting);
        message_center = (RelativeLayout) view.findViewById(R.id.message_center);
        message_number = (TextView) view.findViewById(R.id.message_number);
        hot = (TextView) view.findViewById(R.id.hot);
        product = (TextView) view.findViewById(R.id.product);
        comment = (TextView) view.findViewById(R.id.comment);
        share = (TextView) view.findViewById(R.id.shared);
        share_app = (RelativeLayout) view.findViewById(R.id.share_app);
        orderCeter = (RelativeLayout) view.findViewById(R.id.order_center);
        liulan = (RelativeLayout) view.findViewById(R.id.liulan);
        collect = (RelativeLayout) view.findViewById(R.id.collect);
        liulan_number = (TextView) view.findViewById(R.id.liulan_number);
        collect_number = (TextView) view.findViewById(R.id.collect_number);
        fabu = (RelativeLayout) view.findViewById(R.id.fabu_cuxiao);
        tv_whocuxioa = (TextView) view.findViewById(R.id.tv_whocuxioa);//谁在促销
        rl_fbcxreward = (RelativeLayout) view.findViewById(R.id.rl_fbcxreward);//发布促销奖赏
        rl_fxcxreward = (RelativeLayout) view.findViewById(R.id.rl_fxcxreward);//分享促销奖赏
        iv_fbcxreward = (ImageView) view.findViewById(R.id.iv_fbcxreward);//发布促销赏
        iv_fxcxreward = (ImageView) view.findViewById(R.id.iv_fxcxreward);//分享app赏
        rl_liulan = (RelativeLayout) view.findViewById(R.id.rl_liulan);//浏览汇总
        tv_liulan = (TextView) view.findViewById(R.id.tv_liulan);
        rl_usergroup = (RelativeLayout) view.findViewById(R.id.rl_usergroup);//省又省用户群
        tv_usergroup = (TextView) view.findViewById(R.id.tv_usergroup);
        ll_push = (LinearLayout) view.findViewById(R.id.ll_push);//推送
        rl_pushdata = (RelativeLayout) view.findViewById(R.id.rl_pushdata);
        tv_pushnum = (TextView) view.findViewById(R.id.tv_pushnum);
        view_dot1=view.findViewById(R.id.view_dot1);
        view_dot2=view.findViewById(R.id.view_dot2);
        view_dot3=view.findViewById(R.id.view_dot3);
        view_dot4=view.findViewById(R.id.view_dot4);
        view_dot5=view.findViewById(R.id.view_dot5);
        view_dot6=view.findViewById(R.id.view_dot6);
        view_dot7=view.findViewById(R.id.view_dot7);


    }

    /**
     * 一个继承自ContentObserver的监听器类
     */
    class SmsObserver extends ContentObserver {
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
     * data
     */
    private void initData() {
        /**
         * 注册一个监听
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.BUSINESS_CEBIANLAN);

        /**
         * 注册一个监听 - 商家后台消息的推送
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.BUSINESS_SERVICEEDIT);

        RequestOptions options = new RequestOptions()
                .centerCrop()
//                        .placeholder(R.mipmap.head_image)
                .error(R.mipmap.head_image)
                .priority(Priority.HIGH);

        if (null != MyUserInfoUtils.getInstance().myUserInfo) {
            Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.PhotoPath).apply(options).into(head);
            if (11 != MyUserInfoUtils.getInstance().myUserInfo.ShowName.length()){
                name.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
            }
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
     * event
     */
    private void initEvent() {
        hot_sales.setOnClickListener(this);
        search.setOnClickListener(this);
        orderCeter.setOnClickListener(this);
        liulan.setOnClickListener(this);
        collect.setOnClickListener(this);
        share_app.setOnClickListener(this);
        message_center.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        comment_center.setOnClickListener(this);
        myCenter.setOnClickListener(this);
        fabu.setOnClickListener(this);
        rl_fbcxreward.setOnClickListener(this);
        rl_fxcxreward.setOnClickListener(this);
        rl_liulan.setOnClickListener(this);
        rl_usergroup.setOnClickListener(this);
        ll_push.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        menu_number();

        //环信的Id可能为空
        try{
            /**
             * 获取未读消息
             */
            EMConversation conversation = EMChatManager.getInstance().getConversation(MyUserInfoUtils.getInstance().myUserInfo.IMId);
            int unreadMsgCount = conversation.getUnreadMsgCount();//未读消息不准确

            Hashtable<String, EMConversation> allConversations = EMChatManager.getInstance().getAllConversations();
            if (null != allConversations){
                message_number.setText(allConversations.size() + "");//获取消息的聊天列表
            }
        }catch (Exception e){
            e.printStackTrace();
        }

//        message_number.setText(unreadMsgCount + "");
//        message_number.setText(PreferencesUtils.getInt(getActivity(), MyUserInfoUtils.getInstance().myUserInfo.NickName, 0) + "");

        if (null != MyUserInfoUtils.getInstance() && null != MyUserInfoUtils.getInstance().myUserInfo) {
            if (null != name && null != head) {
                if (null != MyUserInfoUtils.getInstance().myUserInfo) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
//                        .placeholder(R.mipmap.head_image)
                            .error(R.mipmap.head_image)
                            .priority(Priority.HIGH);
                    Glide.with(this).load(URLText.img_url + MyUserInfoUtils.getInstance().myUserInfo.PhotoPath).apply(options).into(head);
                    if (11 == MyUserInfoUtils.getInstance().myUserInfo.ShowName.length() && "1".equals(MyUserInfoUtils.getInstance().myUserInfo.ShowName.substring(0,1))){
                        //手机号号码不显示
                    }else{
                        name.setText(MyUserInfoUtils.getInstance().myUserInfo.ShowName);
                    }
                }
            }
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        mActivity.setHandler(handler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_app://分享app    1 - 是   2- 不是
                getAllGoods();
                break;
            case R.id.ll_setting://设置
                Intent intent0 = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent0);
                break;
            case R.id.hot_sales://热门促销
                Intent intent = new Intent(getActivity(), HotSalesGoods.class);
                intent.putExtra("title", "热门促销");
                startActivity(intent);
                break;
            case R.id.search://谁咋促销
                Intent intent1 = new Intent(getActivity(), SearchActivitys.class);
                startActivity(intent1);
                break;
            case R.id.my_center://我的中心
                Intent intent2 = new Intent(getActivity(), BusinessMyCenterActivity.class);
                intent2.putExtra("title", "我的中心");
                startActivity(intent2);
                break;
            case R.id.fabu_cuxiao://发布促销
                /**
                 * 获取用户信息
                 */
                getUsereInfo();

                if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("1")){
                    Onedialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("2")){
                    Twodialog();
                } else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("3")){
                    faBuCuXiaoDialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("4")){
                    /**
                     * 打电话
                     */
                    dialog();
                }
                break;
            case R.id.comment_center://评论汇总
                Intent intent4 = new Intent(getActivity(), MyCommentCenterActivity.class);
                startActivity(intent4);
                break;
            case R.id.message_center://消息中心
                Intent intent6 = new Intent(getActivity(), MessageCenterActivity.class);
                intent6.putExtra("list", result);
                startActivity(intent6);
                break;
            case R.id.order_center://订单中心
                Intent intent7 = new Intent(getActivity(), OrderCenterCeActivity.class);
                intent7.putExtra("title", "订单中心");
                startActivity(intent7);
                break;
            case R.id.collect://收藏汇总
                Intent intent8 = new Intent(getActivity(), CollectionAllAcitvity.class);
                startActivity(intent8);
                break;
            case R.id.liulan://分享汇总
                Intent intent9 = new Intent(getActivity(), BrowseActivity.class);
                startActivity(intent9);
                break;
            case R.id.rl_fbcxreward://发布促销奖赏
                if (true == menuNumber.ProductRedPacket){//发布商品是否有未打开的红包
                    Intent intent3 = new Intent(getActivity(), BusinessRewardActivity.class);
                    startActivity(intent3);
                }else{
                    /**
                     * 红包对话框
                     */
                    pointDialog();
                }
                break;
            case R.id.rl_fxcxreward://分享促销奖赏
                if (true == menuNumber.SharedRedPacket){//分享app是否有未打开的红包
                    Intent intent5 = new Intent(getActivity(), BusinessRewardActivity.class);
                    startActivity(intent5);
                }else{
                    /**
                     * 红包对话框
                     */
                    pointDialog();
                }
                break;
            case R.id.rl_liulan://浏览汇总
                Intent intent10 = new Intent(getActivity(), LookThroughActivity.class);
                startActivity(intent10);
                break;
            case R.id.rl_usergroup://省又省用户群
                userGroupDialog();
//                Intent intent11 = new Intent(getActivity(), UserThroughActivity.class);
//                startActivity(intent11);
                break;
            case R.id.ll_push://推送
                Intent intent12 = new Intent(getActivity(), BussPushMessageActivity.class);
                startActivity(intent12);

                rl_pushdata.setVisibility(View.GONE);
                tv_pushnum.setText("");//现在用数量显示
                PreferencesUtils.putString(getContext(),"bussinessPushNum","");
                break;
                default:
                    break;
        }
    }

    /**
     * 分享 - 之前是分享app，现在是分享商品
     * @param id
     */
    private void shark(String id) {
//        ShareData data = new ShareData();
//        data.setTitleUrl("http://login.savingsmore.com/Home/Download");
//        data.setUrl("http://login.savingsmore.com/Home/Download");
//        data.setTitle("省又省-实体门店促销APP");
//        data.setImagePath(FileSystem.getCachesDir(getActivity(), true).getAbsolutePath() + File.separator + "icon.jpg");
//        if (null == MyUserInfoUtils.getInstance().myUserInfo.ShowName){
//            data.setText("您的朋友向您推荐促销商品，快去看！促销结束就无效了！");
//        }else{
//            data.setText("您的朋友"+MyUserInfoUtils.getInstance().myUserInfo.ShowName +"向您推荐促销商品，快去看！促销结束就无效了！");
//        }
//        shareUtils = new ShareUtils(data, getActivity());
//        shareUtils.show(share_app);

        //这里是对商品的进行一个分享
        if (shareGoods==null){
            return;
        }
        PreferencesUtils.putString(getActivity(),"isGoodsShark","1");
        ShareData data = new ShareData();
        data.setTitleUrl("http://www.savingsmore.com/Product/SharedProductDetail/"+id);
        data.setUrl("http://www.savingsmore.com/Product/SharedProductDetail/"+id);
        data.setTitle(shareGoods.Name+"-"+shareGoods.Preferential+"！");
//        data.setImagePath(FileSystem.getCachesDir(getActivity(), true).getAbsolutePath() + File.separator + "icon.jpg");
//        if (null == MyUserInfoUtils.getInstance().myUserInfo.ShowName){
//            data.setText("您的朋友分享了一个促销商品，快去看！促销结束就无效了！");
//        }else{
//            data.setText("您的朋友"+ MyUserInfoUtils.getInstance().myUserInfo.ShowName +"分享了一个促销商品，快去看！促销结束就无效了！");
//        }
        data.setImageUrl(URLText.img_url+shareGoods.Image);
        data.setText(shareGoods.StoreName+"开始促销了！货比三家，该商品最优惠！欢迎您来店！");
        shareUtils = new ShareUtils(data, getActivity(),id);
        shareUtils.show(share);
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
                    menuNumber = GsonUtils.fromJson(object.optString("MainData"), MenuNumber.class);
                    if (null != menuNumber) {
                        product.setText(menuNumber.Product);//发布促销
                        order.setText(menuNumber.Receipting);//订单数量
                        tv_whocuxioa.setText(menuNumber.TotalProduct);//谁在促销
                        hot.setText(menuNumber.TotalProduct);//热门促销
                        comment.setText(menuNumber.Comment);//评论数量
                        liulan_number.setText(menuNumber.Shared);//分享
                        collect_number.setText(menuNumber.Favourite);//收藏数量
                        tv_liulan.setText(menuNumber.Hit);//浏览汇总
//                        tv_usergroup.setText(menuNumber.NormalUserCount);//显示客户总人数-------
                        if (true == menuNumber.ProductRedPacket){//发布商品是否有未打开的红包
                            iv_fbcxreward.setBackgroundResource(R.mipmap.iv_shang);
                        }else{
                            iv_fbcxreward.setBackgroundResource(R.mipmap.iv_shang_default);
                        }
                        if (true == menuNumber.SharedRedPacket){//分享app是否有未打开的红包
                            iv_fxcxreward.setBackgroundResource(R.mipmap.iv_shang);
                        }else{
                            iv_fxcxreward.setBackgroundResource(R.mipmap.iv_shang_default);
                        }
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

    String orderNumHot="";
    String cuxiaoNumHot="";
    String pinjiaNumHot="";
    String kehuduihuaNumHot="";
    String collectNumHot="";
    String liulanNumHot="";
    public void isshow1(String orderNum){
        orderNumHot=orderNum;
        String number = PreferencesUtils.getString(getContext(), "orderNum_dot_buss", null);
        if (orderNum!=null&&!orderNum.equals(number)){
            view_dot1.setVisibility(View.VISIBLE);
        }else {
            view_dot1.setVisibility(View.GONE);
        }
    }
    public void isshow2(String cuxiaoNum){
        cuxiaoNumHot=cuxiaoNum;
        String number = PreferencesUtils.getString(getContext(), "cuxiaoNumHot_dot_buss", null);
        if (cuxiaoNum!=null&&!cuxiaoNum.equals(number)){
            view_dot2.setVisibility(View.VISIBLE);
        }else {
            view_dot2.setVisibility(View.GONE);
        }

    }
    public void isshow3(String kehuduihuaNum){
        kehuduihuaNumHot=kehuduihuaNum;
        String number = PreferencesUtils.getString(getContext(), "kehuduihuaNum_dot_buss", null);
        if (kehuduihuaNum!=null&&!kehuduihuaNum.equals(number)){
            view_dot3.setVisibility(View.VISIBLE);
        }else {
            view_dot3.setVisibility(View.GONE);
        }

    }
    public void isshow4(String pinjiaNum){
        pinjiaNumHot=pinjiaNum;
        String number = PreferencesUtils.getString(getContext(), "pinjiaNum_dot_buss", null);
        if (pinjiaNum!=null&&!pinjiaNum.equals(number)){
            view_dot4.setVisibility(View.VISIBLE);
        }else {
            view_dot4.setVisibility(View.GONE);
        }

    }
    public void isshow5(String collectNum){
        collectNumHot=collectNum;
        String number = PreferencesUtils.getString(getContext(), "collectNum_dot_buss", null);
        if (collectNum!=null&&!collectNum.equals(number)){
            view_dot5.setVisibility(View.VISIBLE);
        }else {
            view_dot5.setVisibility(View.GONE);
        }

    }
    public void isshow6(String liulanNum){
        liulanNumHot=liulanNum;
        String number = PreferencesUtils.getString(getContext(), "liulanNum_dot_buss", null);
        if (liulanNum!=null&&!liulanNum.equals(number)){
            view_dot6.setVisibility(View.VISIBLE);
        }else {
            view_dot6.setVisibility(View.GONE);
        }

    }

    /**
     * 得到用户的信息
     */
    private void getUsereInfo() {
        WebRequestHelper.json_post(getActivity(), URLText.GET_USERINFO, RequestParamsPool.getUserInfo(), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject object  = new JSONObject(result);
                    String MianData = object.optString("MainData");
                    MyUserInfoUtils.getInstance().myUserInfo = GsonUtils.fromJson(MianData, MyUserInfo.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 未通过审核 - 进行一个弹框操作
     */
    public void userGroupDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
//                initCallPhone("02158366991");
                call("02138687133");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "请联系“省又省”客服", "021-38687133", "拨打", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 未通过审核 - 进行一个弹框操作
     */
    public void dialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
//                initCallPhone("02158366991");
                call("02158366991");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "您的入驻申请未通过审核，请联系我们 021-58366991。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 打电话
     *
     * @param phoneNumber
     */
    public void initCallPhone(final String phoneNumber) {
        AndPermission.with(this)
                .requestCode(300)
                .permission(Manifest.permission.CALL_PHONE)
                .rationale(new RationaleListener() {

                    @Override
                    public void showRequestPermissionRationale(int i, final Rationale rationale) {
                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
//                        AndPermission.rationaleDialog(mActivity, rationale).show();
                        com.yanzhenjie.alertdialog.AlertDialog.newBuilder(getActivity())
                                .setTitle("友好提醒")
                                .setMessage("你已拒绝过定位权限，沒有定位定位权限无法为你推荐附近的商品，你看着办！")
                                .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rationale.resume();
                                    }
                                })
                                .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        rationale.cancel();
                                    }
                                }).show();
                    }
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, List<String> grantedPermissions) {
                        if (requestCode == 300) { // Successfully.
                            intentToCall(phoneNumber);
                        }
                    }
                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {
                            AndPermission.defaultSettingDialog(getActivity(), 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        if (requestCode == 200) {// Failure.
                            Toast.makeText(getActivity(), "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .start();
    }

    /**
     * 打电话
     * @param phoneNumber
     */
    private void intentToCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }

    /**
     * 未申请
     */
    public void Onedialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), FourStepRegisterActivity.class);
                startActivity(intent);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "请完成注册商家信息，再发布促销。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 待审核
     */
    public void Twodialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "您提交的入驻申请正在审核，请耐心等待。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 发布促销dialog
     */
    public void faBuCuXiaoDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                Intent intent3 = new Intent(getActivity(), IssuGoodActivity.class);
                startActivity(intent3);
                PreferencesUtils.putString(getActivity(),"cebianlan","1");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog1(getActivity(), "", "新品上市、销量下降、新店开张" + "\n" +"无人入店、库存积压、临近质期", "赶快去促销", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

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

    /**
     * 没有红包的对话框
     */
    public void pointDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "Ooops，还没有奖赏", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
     * 获取正在促销中的商品
     */
    private void getAllGoods() {
        showLoading(getActivity(),"正在加载");
        WebRequestHelper.json_post(getActivity(), URLText.GET_SALES_GOODS, RequestParamsPool.getMySalesGoods(false, isComment, isHit, isCollect), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                    List<Goods> mainData = goodsList.MainData;
                    if (null != mainData && mainData.size() > 0 ){//有数据 - 进行分享第一个数据
                        String id = mainData.get(0).Id;
                        shareGoods=mainData.get(0);
                        shark(id);
                        Log.e("tag","1111111111111111111111111111111111111111111111111111111111");
                    }else{//没有数据
                        shareDataDialog();
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                }catch (Exception e){
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
     * 分享没有数据弹框显示
     */
    public void shareDataDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog1(getActivity(), "温馨提示", "对不起，请先发布促销，再与您客户分享！", "知道了", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName,Object cid) {
            try {
                if (Constants.BUSINESS_CEBIANLAN.equals(eventName)) {
                    /**
                     *  左侧侧边栏数据
                     */
                    menu_number();
                }else if (Constants.BUSINESS_SERVICEEDIT.equals(eventName)){//商家后台编辑数据
                    if (rl_pushdata.getVisibility() == View.GONE){
                        rl_pushdata.setVisibility(View.VISIBLE);
                        tv_pushnum.setText("1");
                    }else{
                        rl_pushdata.setVisibility(View.VISIBLE);
                        String s = tv_pushnum.getText().toString();
                        tv_pushnum.setText(Integer.parseInt(s)+1+"");
                    }
                    //不管是多少 - 都进行一个保存为了下一次进来的还是显示
                    PreferencesUtils.putString(getContext(),"bussinessPushNum",tv_pushnum.getText().toString());
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}