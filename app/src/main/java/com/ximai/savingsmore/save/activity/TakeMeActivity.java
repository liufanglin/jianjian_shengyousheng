package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.easeui.EaseConstant;
import com.luck.picture.lib.tools.Constant;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.adapter.SearchBussGoodsAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.AppInfo;
import com.ximai.savingsmore.save.modle.BusinessMessage;
import com.ximai.savingsmore.save.modle.GoodDetial;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.Location;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.User;
import com.ximai.savingsmore.save.modle.UserExtInfo;
import com.ximai.savingsmore.save.utils.APPUtil;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.SelectCarPopupWindow;
import com.ximai.savingsmore.save.view.SelectPopupWindow;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.ximai.savingsmore.save.view.XiMaiPopDialog1;
import com.ximai.savingsmore.save.view.XiMaiPopDialog3;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by caojian on 16/11/29.
 */
public class TakeMeActivity extends BaseActivity implements View.OnClickListener {
    private TextView Store_name;
    private TextView phone_number;
    private ImageView send_message, call,iv_iszhangkai;
    private TextView location;
    private TextView yingye_time;
    private TextView is_yingye;
    private LinearLayout more_good;
    private User user;
    private UserExtInfo userExtInfo;
    private LinearLayout ll_walk, ll_bus, ll_talk;
    private Location loc_now = null;
    private Location loc_end = null;
    private String type;
    private AppInfo baidu;
    private AppInfo gaode;
    private TextView mubiao_location;
    private BusinessMessage businessMessage;
    private SelectPopupWindow menuWindow;
    private int isBaiDuGaoge;
    private TextView yingye_date;
    private GoodDetial goodDetial;
    private String isgood;
    private TextView tv_addressmsg;
    private TextView tv_store_people;
    private LinearLayout ll_car;
//    private TakeMeJiaoCheDialog takeMeJiaoCheDialog;
    private String isMuBiaoClick;//1不是目标位置 2是目标位置点击
    private SelectCarPopupWindow selectCarPopupWindow;
    private RecyclerView recycle_view_shangping;
    private TextView tv_look;
    private SearchBussGoodsAdapter searchBussGoodsAdapter;
    private List<Goods> listGoods = new ArrayList<>();
    public boolean isShow=false;

    // new Location(30.862644, 103.663077, "e");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_me_activity);

        initView();

        initData();

        initEvent();
    }

    /**
     * initView
     */
    private void initView() {
        setLeftBackMenuVisibility(TakeMeActivity.this, "");
        Store_name = (TextView) findViewById(R.id.store_name);
        phone_number = (TextView) findViewById(R.id.phone_number);
        send_message = (ImageView) findViewById(R.id.send_message);
        call = (ImageView) findViewById(R.id.phone);
        mubiao_location = (TextView) findViewById(R.id.mubiao_location);
        location = (TextView) findViewById(R.id.location);
        yingye_time = (TextView) findViewById(R.id.yingye_time);
        is_yingye = (TextView) findViewById(R.id.is_yingye);
        ll_walk = (LinearLayout) findViewById(R.id.ll_walk);
        ll_bus = (LinearLayout) findViewById(R.id.ll_bus);
        ll_talk = (LinearLayout) findViewById(R.id.ll_talk);
        yingye_date = (TextView) findViewById(R.id.yingye_date);//营业日期
        tv_addressmsg = (TextView) findViewById(R.id.tv_addressmsg);
        ll_car = (LinearLayout) findViewById(R.id.ll_car);//叫车
        tv_store_people= (TextView) findViewById(R.id.tv_store_people);
        tv_look= (TextView) findViewById(R.id.tv_look);
        recycle_view_shangping = (RecyclerView) findViewById(R.id.recycle_view_shangping);
        more_good= (LinearLayout) findViewById(R.id.more_good);
        iv_iszhangkai= (ImageView) findViewById(R.id.iv_iszhangkai);
        initRecycleView(recycle_view_shangping);
        searchBussGoodsAdapter = new SearchBussGoodsAdapter(this);
        tv_look.setOnClickListener(this);


    }

    /**
     * 配置recycleview
     */
    private void initRecycleView(RecyclerView recyclerView) {
        FullyLinearLayoutManager myLayoutManager = new FullyLinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        myLayoutManager.setOrientation(FullyLinearLayoutManager.VERTICAL);
        configRecycleView(recyclerView, myLayoutManager);
    }
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * initData
     */
    private void initData() {
        isgood = getIntent().getStringExtra("isgood");//判断带我去门店还是带我去商家
        if ("true".equals(isgood)){
            setCenterTitle("带我去门店");
            tv_addressmsg.setText("门店地址");
            tv_store_people.setVisibility(View.VISIBLE);
        }else if ("false".equals(isgood)){
            setCenterTitle("带我去商家");
            tv_addressmsg.setText("商家地址");
            tv_store_people.setVisibility(View.VISIBLE);
        }

        if ("true".equals(isgood)) {//带我去门店
            user = (User) getIntent().getSerializableExtra("good");
            goodDetial = (GoodDetial) getIntent().getSerializableExtra("good1");
            userExtInfo = user.UserExtInfo;
        } else if ("false".equals(isgood)){//带我去门店
            businessMessage = (BusinessMessage) getIntent().getSerializableExtra("good");
            userExtInfo = businessMessage.UserExtInfo;
        }

        if ("true".equals(isgood)){
            tv_store_people.setText("到店人次:"+goodDetial.StoreCount);
        }else {
            if (userExtInfo!=null){
              //  tv_store_people.setText("到店人次:"+ Constant.storeCount);
                tv_store_people.setVisibility(View.GONE);
            }
        }


        if (null != user && null != userExtInfo) {
//            Store_name.setText(userExtInfo.StoreName);
            if (null != goodDetial.ChainStores){//---------------------------------------------------------进行分店的实现
                if (goodDetial.ChainStores.size() > 0){
                    String name = goodDetial.ChainStores.get(0).Name;
                    Store_name.setText(name);
                }else{
                    if (null != goodDetial.User.UserExtInfo.StoreName) {//商家店铺名称
                        Store_name.setText(goodDetial.User.UserExtInfo.StoreName);
                    }
                }
            }else{
                if (null != goodDetial.User.UserExtInfo.StoreName) {//商家店铺名称
                    Store_name.setText( goodDetial.User.UserExtInfo.StoreName);//商家名称
                }
            }

            if (null != goodDetial.ChainStores){//---------------------------------------------------------进行分店的实现
                if (goodDetial.ChainStores.size() > 0){
                    String phone = goodDetial.ChainStores.get(0).ContactWay;
                    if (!TextUtils.isEmpty(phone)){
                        phone_number.setText(phone);
                    }
                }else{
                    phone_number.setText(userExtInfo.PhoneNumber);
                }
            }else{
                phone_number.setText(userExtInfo.PhoneNumber);
            }
//            phone_number.setText(user.PhoneNumber);

            if (null != user.Province && null != user.City && null != user.Domicile){
                location.setText(user.Country.Name+" · "+user.Province.Name + user.City.Name + user.Domicile);
                mubiao_location.setText(user.Country.Name+" · "+user.Province.Name + user.City.Name + user.Domicile);
            }

            if (null != goodDetial.Province.Name && null != goodDetial.City.Name && null != goodDetial.Address){
                location.setText(goodDetial.Country.Name+" · "+goodDetial.Province.Name + goodDetial.City.Name + goodDetial.Address);
                mubiao_location.setText(goodDetial.Country.Name+" · "+goodDetial.Province.Name + goodDetial.City.Name + goodDetial.Address);
            }

//            if (null != user.Area && null != user.Province && null != user.City) {
//               location.setText(user.Province.Name + user.City.Name + user.Area.Name + user.Domicile);
//               mubiao_location.setText(user.Province.Name + user.City.Name + user.Domicile);
//            } else if (null != user.Province && null != user.City) {
//                location.setText(user.Province.Name + user.City.Name + user.Domicile);
//                mubiao_location.setText(user.Province.Name + user.City.Name + user.Domicile);
//            }

            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            int a = Integer.parseInt(str.substring(0,2));

            if (null != userExtInfo.StartHours && null != userExtInfo.EndHours){
                if (a >= Integer.parseInt(userExtInfo.StartHours.substring(0,2)) && a <= Integer.parseInt(userExtInfo.EndHours.substring(0,2))) {
                    is_yingye.setText("营业中");
                } else {
                    is_yingye.setText("暂停营业");
                }
            }else{
                is_yingye.setText("");//未设置数据
            }
            if (null != userExtInfo.StartHours && null != userExtInfo.EndHours){
                yingye_time.setText(userExtInfo.StartHours + "-" + userExtInfo.EndHours);
            }//没有数据

            if (null != userExtInfo.BusinessDate){//营业日期
                yingye_date.setText(userExtInfo.BusinessDate.Name);
            }
            System.out.print("=====================================");
            loc_end = new Location(Double.parseDouble(user.Latitude), Double.parseDouble(user.Longitude));
            loc_now = new Location(BaseApplication.getInstance().Longitude, BaseApplication.getInstance().Latitude);
            System.out.print("=====================================");
            getAllGoods("500",user.Longitude,user.Latitude);
        } else if (null != businessMessage && null != userExtInfo) {
            Store_name.setText(businessMessage.ShowName);
            phone_number.setText(userExtInfo.PhoneNumber);

            if (null != businessMessage.Province && null != businessMessage.City && null != businessMessage.Domicile) {
                location.setText(businessMessage.Country.Name+" · "+businessMessage.Province.Name + businessMessage.City.Name + businessMessage.Domicile);
                mubiao_location.setText(businessMessage.Country.Name+" · "+businessMessage.Province.Name + businessMessage.City.Name + businessMessage.Domicile);
            }
//            if (null != businessMessage.Area) {
//                location.setText(businessMessage.Province.Name + businessMessage.City.Name + businessMessage.Area.Name + businessMessage.Domicile);
//                mubiao_location.setText(businessMessage.Province.Name + businessMessage.City.Name + businessMessage.Area.Name + businessMessage.Domicile);
//            } else {
//                location.setText(businessMessage.Province.Name + businessMessage.City.Name + businessMessage.Domicile);
//                mubiao_location.setText(businessMessage.Province.Name + businessMessage.City.Name + businessMessage.Domicile);
//            }
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            int a = Integer.parseInt(str.substring(0,2));

            if (null != userExtInfo.StartHours && null != userExtInfo.EndHours){
                if (a > Integer.parseInt(userExtInfo.StartHours.substring(0,2)) && a < Integer.parseInt(userExtInfo.EndHours.substring(0,2))) {
                    is_yingye.setText("营业中");
                } else {
                    is_yingye.setText("暂停营业");
                }
            }else{
                is_yingye.setText("");//未设置数据
            }
            if (null != userExtInfo.StartHours && null != userExtInfo.EndHours){
                yingye_time.setText(userExtInfo.StartHours + "-" + userExtInfo.EndHours);
            }//没有设置数据

            if (null != userExtInfo.BusinessDate){//营业日期
                yingye_date.setText(userExtInfo.BusinessDate.Name);
            }
            //终点
            loc_end = new Location(Double.parseDouble(businessMessage.Latitude), Double.parseDouble(businessMessage.Longitude));
            System.out.print("------------------------------------------");
            System.out.print(BaseApplication.getInstance().Longitude);
            System.out.print(BaseApplication.getInstance().Latitude);
            System.out.print("------------------------------------------");//打印地图坐标信息
            loc_now = new Location(BaseApplication.getInstance().Longitude, BaseApplication.getInstance().Latitude);
            getAllGoods("500",businessMessage.Longitude,businessMessage.Latitude);

        }
        baidu = APPUtil.getAppInfoByPak(TakeMeActivity.this, "com.baidu.BaiduMap");//判断手机上是否安装地图
        gaode = APPUtil.getAppInfoByPak(TakeMeActivity.this, "com.autonavi.minimap");//判断手机上是否安装地图
    }

    /**
     * init-event
     */
    private void initEvent() {
        call.setOnClickListener(this);
        ll_walk.setOnClickListener(this);
        ll_bus.setOnClickListener(this);
        ll_talk.setOnClickListener(this);
        send_message.setOnClickListener(this);
        ll_car.setOnClickListener(this);
        mubiao_location.setOnClickListener(this);
        more_good.setOnClickListener(this);
        searchBussGoodsAdapter.setViewClickListener(new SearchBussGoodsAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, String id) {
                if (null != listGoods  && listGoods.size() > 0){
                    if (com.ximai.savingsmore.save.modle.LoginUser.getInstance().isLogin()) {
                        Intent intent=new Intent(TakeMeActivity.this,GoodDetailsActivity.class);
                        intent.putExtra("id",listGoods.get(postion).Id);
                        startActivity(intent);
                    } else {
                        Toast.makeText(TakeMeActivity.this, "温馨提示,您还没有登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TakeMeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    /**
     * 事件处理
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_message://去聊天
                if (null != businessMessage && null != userExtInfo){//商家的过来
                    if (null == MyUserInfoUtils.getInstance().myUserInfo.IMUserName || null == MyUserInfoUtils.getInstance().myUserInfo.IMPassword) {
                        Toast.makeText(this, "对方暂不支持聊天", Toast.LENGTH_SHORT).show();
                    }else {
                        //如果是自己则不能聊天
                        if (businessMessage.ShowName.equals(MyUserInfoUtils.getInstance().myUserInfo.ShowName)){
                            chatDialog();
                        }else{
                            if (null == businessMessage.IMUserName){
                                Toast.makeText(this, "对方暂不支持聊天", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent send = new Intent(TakeMeActivity.this, ChatActivity.class);
                                send.putExtra(EaseConstant.EXTRA_USER_ID, businessMessage.IMUserName);
                                startActivity(send);
                            }
                        }
                    }
                }else if (null != user && null != userExtInfo){//个人的信息过来
                    if (null == MyUserInfoUtils.getInstance().myUserInfo.IMUserName || null == MyUserInfoUtils.getInstance().myUserInfo.IMPassword) {
                        Toast.makeText(this, "对方暂不支持聊天", Toast.LENGTH_SHORT).show();
                    }else {
                        //如果是自己则不能聊天
                        if (userExtInfo.StoreName.equals(MyUserInfoUtils.getInstance().myUserInfo.ShowName)){
                            chatDialog();
                        }else{
                            if (null == user.IMUserName){
                                Toast.makeText(this, "对方暂不支持聊天", Toast.LENGTH_SHORT).show();
                            }else {
                                Intent send = new Intent(TakeMeActivity.this, ChatActivity.class);
                                send.putExtra(EaseConstant.EXTRA_USER_ID, user.IMUserName);
                                startActivity(send);
                            }
                        }
                    }
                }
                break;
            case R.id.phone://拨打电话
                if (!TextUtils.isEmpty(phone_number.getText())){
//                    initCallPhone(phone_number.getText().toString());
                    call(phone_number.getText().toString());
                }
                break;
            case R.id.ll_talk://步行
                isMuBiaoClick = "1";
                if (baidu == null && gaode == null) {
                    Toast.makeText(TakeMeActivity.this, "您手机上还没有安装导航App,请到应用市场下载", Toast.LENGTH_SHORT).show();
                } else if (baidu == null) {//选择高德
                    isBaiDuGaoge = 1;
                    type = "driving";
                    showSetIconWindowGaoBai("高德地图", "");
                } else if (gaode == null) {//选择百度
                    type = "driving";
                    isBaiDuGaoge = 2;
                    showSetIconWindowGaoBai("百度地图", "");
                } else {//百度高德都有
                    type = "driving";
                    showSetIconWindowGaoBai("百度地图", "高德地图");
                }
                break;
            case R.id.ll_bus://公交
                isMuBiaoClick = "1";
                if (baidu == null && gaode == null) {
                    Toast.makeText(TakeMeActivity.this, "您手机上还没有安装导航App,请到应用市场下载", Toast.LENGTH_SHORT).show();
                } else if (baidu == null) {//选择高德
                    type = "transit";
                    isBaiDuGaoge = 1;
                    showSetIconWindowGaoBai("高德地图", "");
                } else if (gaode == null) {//选择百度
                    type = "transit";
                    isBaiDuGaoge = 2;
                    showSetIconWindowGaoBai("百度地图", "");
                } else {//百度高德都有
                    type = "transit";
                    showSetIconWindowGaoBai("百度地图", "高德地图");
                }
                break;
            case R.id.ll_walk://汽车
                isMuBiaoClick = "1";
                if (baidu == null && gaode == null) {
                    Toast.makeText(TakeMeActivity.this, "您手机上还没有安装导航App,请到应用市场下载", Toast.LENGTH_SHORT).show();
                } else if (baidu == null) {//选择高德
                    type = "walking";
                    isBaiDuGaoge = 1;
                    showSetIconWindowGaoBai("高德地图", "");
                } else if (gaode == null) {//选择百度
                    type = "walking";
                    isBaiDuGaoge = 2;
                    showSetIconWindowGaoBai("百度地图", "");
                } else {//百度高德都有
                    type = "walking";
                    showSetIconWindowGaoBai("百度地图", "高德地图");
                }
                break;
            case R.id.mubiao_location://目的地
                isMuBiaoClick = "2";
                if (baidu == null && gaode == null) {
                    Toast.makeText(TakeMeActivity.this, "您手机上还没有安装导航App,请到应用市场下载", Toast.LENGTH_SHORT).show();
                } else if (baidu == null) {//选择高德
                    type = "walking";
                    isBaiDuGaoge = 1;
                    showSetIconWindowGaoBai("高德地图", "");
                } else if (gaode == null) {//选择百度
                    type = "walking";
                    isBaiDuGaoge = 2;
                    showSetIconWindowGaoBai("百度地图", "");
                } else {//百度高德都有
                    type = "walking";
                    showSetIconWindowGaoBai("百度地图", "高德地图");
                }
                break;
            case R.id.ll_car://叫车 - 进行弹框选择
                selectCarPopupWindow = new SelectCarPopupWindow(TakeMeActivity.this, itemsCarOnClick);
                selectCarPopupWindow.showAtLocation(ll_walk);
                break;
            case R.id.tv_look:
                if (isShow){
                    more_good.setVisibility(View.GONE);
                    isShow=false;
                    recycle_view_shangping.setVisibility(View.GONE);
                    tv_look.setBackgroundResource(R.drawable.button_sharp);
                }else {
                    more_good.setVisibility(View.VISIBLE);
                    isShow=true;
                    recycle_view_shangping.setVisibility(View.VISIBLE);
                    tv_look.setBackgroundResource(R.drawable.button_gray);
                }
                break;
            case R.id.more_good:
                isShow=false;
                recycle_view_shangping.setVisibility(View.GONE);
                tv_look.setBackgroundResource(R.drawable.button_sharp);
                more_good.setVisibility(View.GONE);
                break;

        }
    }

    /**
     * 无人驾驶的点击
     */
    public void speakCarWuRenDialog(){
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
        Dialog dialog = new XiMaiPopDialog3(TakeMeActivity.this,"","","知道了", "",R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 其他的出租电话点击
     */
    public void speakCarDialog(String name, final String phone){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call(phone);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(TakeMeActivity.this,"致电 "+name,phone,"拨打", "",R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 调用拨号界面
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 去进行聊天
     */
    public void goChat(){
        Intent send = new Intent(TakeMeActivity.this, ChatActivity.class);
        if (null != user) {
            send.putExtra(EaseConstant.EXTRA_USER_ID, user.IMUserName);
        } else {
            send.putExtra(EaseConstant.EXTRA_USER_ID, businessMessage.IMUserName);
        }
        startActivity(send);
    }

    /**
     * 不能喝自己聊天
     */
    public void chatDialog(){
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
        Dialog dialog = new XiMaiPopDialog(TakeMeActivity.this, "温馨提示", "您不能跟自己对话", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
                        AlertDialog.newBuilder(TakeMeActivity.this)
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
                        if (AndPermission.hasAlwaysDeniedPermission(TakeMeActivity.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(TakeMeActivity.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        if (requestCode == 200) {// Failure.
                            Toast.makeText(TakeMeActivity.this, "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .start();
    }

    /**
     * 打电话
     */
    private void intentToCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data = Uri.parse("tel:" + phoneNumber);
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }

    /**
     * 百度和高德地图两者都有
     */
    private void showSetIconWindowGaoBai(String baidu, String gaode) {
        menuWindow = new SelectPopupWindow(this, itemsOnClick);
        menuWindow.showAtLocation(ll_walk, baidu, gaode);
    }

    /**
     * 弹出窗口实现监听类
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_one://百度
                    //这里当用户只有一个应用的时候回调要做判断 - 判断是高德过来还是百度过来的
                    //isBaiDuGaoge = 1;   1是高德   -  百度是2
                    if (1 == isBaiDuGaoge){
                        menuWindow.dismiss();
                        if ("true".equals(isgood)){
                            if (!TextUtils.isEmpty(mubiao_location.getText())){
                                startNative_Baidu1(mubiao_location.getText().toString(),type);
                            }
                        }else if ("false".equals(isgood)){
                            startNative_Gaode(loc_end);
                        }
                    }else if (2 == isBaiDuGaoge){
                        menuWindow.dismiss();
                        if ("true".equals(isgood)){
                            if (!TextUtils.isEmpty(mubiao_location.getText())){
                                startNative_Baidu1(mubiao_location.getText().toString(),type);
                            }
                        }else if ("false".equals(isgood)){
                            startNative_Baidu(loc_now, loc_end, type);
                        }
                    }else{
                        menuWindow.dismiss();
                        if ("true".equals(isgood)){
                            if (!TextUtils.isEmpty(mubiao_location.getText())){
                                startNative_Baidu1(mubiao_location.getText().toString(),type);
                            }
                        }else if ("false".equals(isgood)){
                            startNative_Baidu(loc_now, loc_end, type);
                        }
                    }
                    break;
                case R.id.btn_two://高德
                    menuWindow.dismiss();
                    if ("true".equals(isgood)){
                        if (!TextUtils.isEmpty(mubiao_location.getText())){
                            startNative_Gaode1(mubiao_location.getText().toString());
                        }
                    }else if ("false".equals(isgood)){
                        startNative_Gaode(loc_end);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 叫车的弹框实现
     */
    private View.OnClickListener itemsCarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectCarPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.ll_car1:
                    speakCarDialog("“省又省”专车","13818799928");
                    break;
                case R.id.ll_car2:
                    speakCarDialog("强生租车","02162580000");
                    break;
                case R.id.ll_car3:
                    speakCarDialog("大众租车","96822");
                    break;
                case R.id.ll_car4:
                    speakCarDialog("上海锦江","96961");
                    break;
                case R.id.ll_car5:
                    speakCarWuRenDialog();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 调用百度
     */
    public void startNative_Baidu(Location loc1, Location loc2, String type) {
        if (loc1 == null || loc2 == null) {
            return;
        }
        if (loc1.getAddress() == null || "".equals(loc1.getAddress())) {
            loc1.setAddress("我的位置");
        }
        if (loc2.getAddress() == null || "".equals(loc2.getAddress())) {
            loc2.setAddress("目的地");
        }
        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=latlng:" + loc1.getStringLatLng() + "|name:" + loc1.getAddress() + "&destination=latlng:" + loc2.getStringLatLng() + "|name:" + loc2.getAddress() + "&mode=" + type + "&src=重庆快易科技|CC房车-车主#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(TakeMeActivity.this, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调用高德
     */
    public void startNative_Gaode(Location loc) {
        if (loc == null) {
            return;
        }
        if (loc.getAddress() == null || "".equals(loc.getAddress())) {
            loc.setAddress("目的地");
        }
        try {
            Intent intent = new Intent("android.intent.action.VIEW",
                    Uri.parse("androidamap://navi?sourceApplication=CC房车-车主&poiname=重庆快易科技&lat=" + loc.getLat() + "&lon=" + loc.getLng() + "&dev=0&style=0"));
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(TakeMeActivity.this, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 百度带我去门店 - 直接设置终点
     */
    public void startNative_Baidu1(String address, String type) {
        try {
            if ("1".equals(isMuBiaoClick)){//不是目标位置点击
                Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination="+address+"&mode="+type+"&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                startActivity(intent);
            }else if ("2".equals(isMuBiaoClick)){//是目标位置点击
//                Intent intent = Intent.getIntent("intent://map/direction?origin="+address+"&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                Intent intent = new Intent();
                intent.setData(Uri.parse("baidumap://map/geocoder?src=openApiDemo&address="+address));
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(TakeMeActivity.this, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 高德带我去门店 - 直接设置终点
     */
    public void startNative_Gaode1(String address) {
        try {
            if ("1".equals(isMuBiaoClick)){//不是目标位置点击
                Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dname="+address+"&dev=0&m=0&t=1");
                intent.setPackage("com.autonavi.minimap");
                startActivity(intent);
            }else if ("2".equals(isMuBiaoClick)){//是目标位置点击
                Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname="+address+"&dev=0&m=0&t=1");
                intent.setPackage("com.autonavi.minimap");
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(TakeMeActivity.this, "地址解析错误", Toast.LENGTH_SHORT).show();
        }
    }
    private GoodsList goodsList = new GoodsList();

    /**
     * 得到所有的商品 ----   添加是产品还是服务
     */
    private void getAllGoods(String Radius,String Longitude, String Latitude) {
        WebRequestHelper.json_post(this, URLText.GET_GOODS,
                RequestParamsPool.getAllGoods(Radius,Longitude, Latitude), new MyAsyncHttpResponseHandler(TakeMeActivity.this) {
                    @Override
                    public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                        String resule = new String(responseBody);
                        try {
                            listGoods.clear();//先将数据进行清除

                            goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                            listGoods.addAll(goodsList.MainData);
                            if (listGoods.size() == 0){
                                tv_look.setVisibility(View.GONE);
                                recycle_view_shangping.setVisibility(View.GONE);
                            }else{
                                tv_look.setVisibility(View.VISIBLE);
                                searchBussGoodsAdapter.setData(listGoods);
                                recycle_view_shangping.setAdapter(searchBussGoodsAdapter);
                                searchBussGoodsAdapter.notifyDataSetChanged();
                            }

                        }catch (Exception e){
                            e.printStackTrace();

                        }
                    }
                });
    }

}