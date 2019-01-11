package com.ximai.savingsmore.save.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.library.toolbox.PopupWindowFromBottomUtil;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.adapter.OrderBuyAdapter;
import com.ximai.savingsmore.save.adapter.OrderBuyChooseAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.AlipaySignResult;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.CartDetail;
import com.ximai.savingsmore.save.modle.DikouResult;
import com.ximai.savingsmore.save.modle.GoodDetial;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.ListBaseMessage;
import com.ximai.savingsmore.save.modle.WeChatSign;
import com.ximai.savingsmore.save.modle.submitOrderResults;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.PayDialog;
import com.ximai.savingsmore.save.view.XiMaiPopDialog1;
import com.ximai.savingsmore.save.view.XiMaiPopDialog2;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/29.
 */
//下单购买 - 支持侧滑
public class OrderBuyCeActivity extends BaseActivity implements View.OnClickListener {
    private EditText name, phone, address;
    private TextView addAdress;
    List<Goods> list = new ArrayList<>();
    private GoodDetial goodDetial;
    private boolean isEditor = false;
    private String PrivoceId;
    private String CityId;
    private String AreaId;
    private List<BaseMessage> list1 = new ArrayList<>();
    private GoodsList goodsList = new GoodsList();
    private List<Goods> list_good = new ArrayList<>();
    private RelativeLayout hotSales;
    private ImageView hot_up;
    private CartDetail cartDetail;
    private Button jiesuan;
    private boolean IsUsePoint = false;//是否用积分
    private TextView fapiao;
    private EditText beizhu;
    private List<String> cartListId = new ArrayList<>();//购物车Id
    private AlipaySignResult alipaySignResult;
    private static final int SDK_PAY_FLAG = 1;
    private Button dikou;
    private DikouResult dikouResult;
    private TextView allPrice, realityPrice;
    private double sallPrice = 0, srealityPrice = 0;
    private int payStyle = 1;//默认支付宝支付
    private WeChatSign weChatSign;
    private ImageView liuyan;
    private LinearLayout more_good;
    DecimalFormat df = new DecimalFormat("######0.00");
    private RelativeLayout rl_liuyan;
    private RecyclerView recycleview_goodsmsg;
    private OrderBuyAdapter orderBuyAdapter;
    private SwipeMenuRecyclerView recycleview_buygoods;
    private OrderBuyChooseAdapter orderBuyChooseAdapter;//选中的需要购买的商品
    private boolean isZhangKai = false;//是否是张开
    private boolean isShow=false;
    private String NoPayOrder = "";

    /**
     * 支付宝回调
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    // @SuppressWarnings("unchecked")
                    // PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    // String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    // String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (msg.obj.toString().contains("resultStatus={9000}")) {
//                        finish();
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(OrderBuyCeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(OrderBuyCeActivity.this, PaySuccessActivity.class);
                        if (submitOrderResult.MainData.size() > 0) {
                            intent.putExtra("Id", submitOrderResult.MainData.get(0).Id);
//                            intent.putExtra("submitOrderResult", (Serializable) submitOrderResult);这是预下单数据
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("SubmitOrderResult",submitOrderResult);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } else {
//                        finish();
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(OrderBuyCeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };

    private ImageView iv_iszhangkai;
    private TextView tv_iszhangkai;
    private String CartIdListJson;//非促销
    private TextView tv_cuxiaojia;
    private submitOrderResults submitOrderResult;
    private TextView tv_choosessq;
    private TextView tv_yunsong;
    private TextView tv_songhuo;
    private PayDialog payDialog;//支付dialog
    private NestedScrollView scrollview;
    private RelativeLayout back;
    private LinearLayoutManager mLayoutManager;
    private DefaultItemDecoration mItemDecoration;
    private TextView tvOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_buyce_activity);

        initView();

        initData();

        initEvent();
    }

    /**
     * init - view
     */
    private void initView() {
        /**
         * 将标题隐藏
         */
        toolbar.setVisibility(View.GONE);
        tvOther= (TextView) findViewById(R.id.tv1);

        scrollview = (NestedScrollView) findViewById(R.id.scrollview);//scrollview
        back = (RelativeLayout) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.address);
        addAdress = (TextView) findViewById(R.id.add_address);
        recycleview_goodsmsg = (RecyclerView) findViewById(R.id.recycleview_goodsmsg);//商品信息
        recycleview_buygoods = (SwipeMenuRecyclerView) findViewById(R.id.recycleview_buygoods);//购买商品信息
        hotSales = (RelativeLayout) findViewById(R.id.hot_sales);
        hot_up = (ImageView) findViewById(R.id.hot_up);
        jiesuan = (Button) findViewById(R.id.jiesuan);
        fapiao = (TextView) findViewById(R.id.fapiao);//发票
        tv_yunsong = (TextView) findViewById(R.id.tv_yunsong);//运送费用
        tv_songhuo = (TextView) findViewById(R.id.tv_songhuo);//送货保险费
        beizhu = (EditText) findViewById(R.id.beizhu);
        dikou = (Button) findViewById(R.id.dikou);
        allPrice = (TextView) findViewById(R.id.all_price);
        realityPrice = (TextView) findViewById(R.id.reality_price);
        liuyan = (ImageView) findViewById(R.id.liuyan);
        rl_liuyan = (RelativeLayout) findViewById(R.id.rl_liuyan);
        more_good = (LinearLayout) findViewById(R.id.more_good);
        iv_iszhangkai = (ImageView) findViewById(R.id.iv_iszhangkai);
        tv_iszhangkai = (TextView) findViewById(R.id.tv_iszhangkai);
        tv_cuxiaojia = (TextView) findViewById(R.id.tv_cuxiaojia);//我要促销价
        tv_choosessq = (TextView) findViewById(R.id.tv_choosessq);//选择市市区
        recycleview_goodsmsg.setNestedScrollingEnabled(false);//禁止recycleview事件
        recycleview_buygoods.setNestedScrollingEnabled(false);//禁止recycleview事件
        /**
         * init - recycleView
         */
        initRecycleView(recycleview_goodsmsg);
    }

    /**
     * inti - event
     */
    private void initData() {
        /**
         * 注册一个监听
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.LOADING_ORDERFRAGMENT);
        /**
         * 支付成功 - 查看物流
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.SEE_WULIU_FINISHORDERBUY);

        /**
         * 默认设置更多数据的指示方向
         */
    //    iv_iszhangkai.setImageResource(R.mipmap.zhankai);
      //  tv_iszhangkai.setText("更多促销商品");

        goodDetial = (GoodDetial) getIntent().getSerializableExtra("good");

        if (null != goodDetial){
            /**
             * 设置默认城市 - 姓名 - 手机号码
             */
            String cityAddress = PrefUtils.getString(this, "cityAddress", "");
            String xiangXiAddress = PrefUtils.getString(this, "xiangXiAddress", "");
            String userphone = PrefUtils.getString(this, "userphone", "");
            String username = PrefUtils.getString(this, "username", "");
            if (null != cityAddress || null != xiangXiAddress){
                address.setText(cityAddress+xiangXiAddress);
            }
            if ( null != userphone){
                phone.setText(userphone);
            }
            if (null != username){
                name.setText(username);
            }
            if (null != goodDetial) {//支付的时候需要使用
                PrivoceId = goodDetial.ProvinceId;
                CityId = goodDetial.CityId;
                AreaId = goodDetial.AreaId;
            }
            if (null != goodDetial.User.UserExtInfo.Invoice){//发票
                fapiao.setText(goodDetial.User.UserExtInfo.Invoice.Name);
            }
            if (null != goodDetial.User.UserExtInfo.DeliveryService ){//运送费用
                tv_yunsong.setText(goodDetial.User.UserExtInfo.DeliveryService.Name);
                PreferencesUtils.putString(OrderBuyCeActivity.this,"DeliveryService",goodDetial.User.UserExtInfo.DeliveryService.Name);
            }
            if (null != goodDetial.User.UserExtInfo.Premium ){//送货保险费
                tv_songhuo.setText(goodDetial.User.UserExtInfo.Premium.Name);
            }
            if (null != goodDetial.User.UserExtInfo.StoreName){//这里是商家如果是发货前与商家确认，需要展示的商家名称和手机号码
                PreferencesUtils.putString(OrderBuyCeActivity.this,"OrderStoreName",goodDetial.User.UserExtInfo.StoreName);
            }
            if (null != goodDetial.User.UserExtInfo.PhoneNumber){//这里是商家如果是发货前与商家确认，需要展示的商家名称和手机号码
                PreferencesUtils.putString(OrderBuyCeActivity.this,"OrderPhoneNumber",goodDetial.User.UserExtInfo.PhoneNumber);
            }
            /**
             * 商家更多商品
             */
            if (null != goodDetial.User) {
                String rebatePercent = goodDetial.User.UserExtInfo.RebatePercent;
                double v1 = Double.parseDouble(rebatePercent);
                float v = (float) (v1 * 100);
                if (0 == v){
                    dikou.setText("本店无返利");//门店消费返利
                    dikou.setEnabled(false);
                }else{
                    dikou.setText("积分返利"+v+ "%");//门店消费返利
                }

                /**
                 * 获取购物车信息
                 */
                getMyCar(goodDetial.User.UserExtInfo.IsBag.toString());

                getAllGoods(goodDetial.User.Id);
            }
        }

        /**
         * 更多商品适配器
         */
        orderBuyAdapter = new OrderBuyAdapter(this);
        recycleview_goodsmsg.setAdapter(orderBuyAdapter);

        mLayoutManager = new LinearLayoutManager(OrderBuyCeActivity.this);//设置布局参数
        mItemDecoration = new DefaultItemDecoration(ContextCompat.getColor(OrderBuyCeActivity.this, R.color.white));
        recycleview_buygoods.setLayoutManager(mLayoutManager);
        recycleview_buygoods.addItemDecoration(mItemDecoration);
//        recycleview_buygoods.setSwipeItemClickListener(this);//设置Iten的点击事件
        recycleview_buygoods.setSwipeMenuCreator(swipeMenuCreator);//创建侧边栏
        recycleview_buygoods.setSwipeMenuItemClickListener(mMenuItemClickListener);//侧边栏点击事件

        /**
         * 选中购买的商品
         */
        orderBuyChooseAdapter = new OrderBuyChooseAdapter(this);
        recycleview_buygoods.setAdapter(orderBuyChooseAdapter);
   
        /**
         * 查询商品字典
         */
        queryDicNode();
    }

    /**
     * inti - event
     */
    public void initEvent(){
        tvOther.setOnClickListener(this);
        more_good.setOnClickListener(this);
        rl_liuyan.setOnClickListener(this);
        back.setOnClickListener(this);
        hotSales.setOnClickListener(this);
        addAdress.setOnClickListener(this);
        jiesuan.setOnClickListener(this);
        dikou.setOnClickListener(this);
        name.setOnClickListener(this);
        phone.setOnClickListener(this);
        address.setOnClickListener(this);
        tv_cuxiaojia.setOnClickListener(this);
        tv_choosessq.setOnClickListener(this);
        name.setCursorVisible(false);
        name.setFocusable(false);
        name.setFocusableInTouchMode(false);
        phone.setCursorVisible(false);
        phone.setFocusable(false);
        phone.setFocusableInTouchMode(false);
        address.setCursorVisible(false);
        address.setFocusable(false);
        address.setFocusableInTouchMode(false);

        /**
         * 更多商品查看详情信息
         */
        orderBuyAdapter.setViewClickListener(new OrderBuyAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, String id) {
                Intent intent = new Intent(OrderBuyCeActivity.this, GoodDetailsActivity.class);
                intent.putExtra("isCar", "true");
                intent.putExtra("id", list_good.get(postion).Id);
                startActivity(intent);
            }
        });

        /**
         * 减
         */
        orderBuyChooseAdapter.setOnItenClickListener(new OrderBuyChooseAdapter.OnItemClickReduceListener() {
            @Override
            public void onViewClcik(int position, TextView number, List<Goods> list) {
                if (list.size() > position) {
                    list.get(position).Quantity = list.get(position).Quantity - 1;
                    if (list.get(position).Quantity == 0) {

                        if ("非促销品".equals(list.get(position).Name)){
                            needCuXiaoPrice(null,list.get(position).Id,null,null,null,"1","4");
                        }else{
                            addCat(list.get(position).ProductId, "1", "4");
                        }
//                        addCat(list.get(position).ProductId, "1", "4");
                    } else {
                        if ("非促销品".equals(list.get(position).Name)){
                            needCuXiaoPrice(null,list.get(position).Id,null,null,null,"1","2");
                            number.setText((list.get(position).Quantity + ""));
                        }else{
                            addCat(list.get(position).ProductId, "1", "2");
                            number.setText((list.get(position).Quantity + ""));
                        }

//                        addCat(list.get(position).ProductId, "1", "2");
//                        number.setText((list.get(position).Quantity + ""));
                    }
                    double price = 0;
                    for (int i = 0; i < list.size(); i++) {
                        price = price + list.get(i).Quantity * Double.parseDouble(list.get(i).Price);
                    }

                    allPrice.setText(PreferencesUtils.getString(OrderBuyCeActivity.this,"key_Currency") +df.format(price));
                    realityPrice.setText(PreferencesUtils.getString(OrderBuyCeActivity.this,"key_Currency")+df.format(price));
                    sallPrice = price;
                }
            }
        });
        /**
         * 加
         */
        orderBuyChooseAdapter.setOnAddClickLitener(new OrderBuyChooseAdapter.OnItenClickAddListener() {
            @Override
            public void onViewClcik(int position, TextView number, List<Goods> list) {
                if (list.size() > position) {
                    list.get(position).Quantity = list.get(position).Quantity + 1;

                    if ("非促销品".equals(list.get(position).Name)){
                        needCuXiaoPrice(null,list.get(position).Id,null,null,null,"1","1");
                    }else{
                        addCat(list.get(position).ProductId, "1", "1");
                    }
//                    addCat(list.get(position).ProductId, "1", "1");
                    number.setText((list.get(position).Quantity + ""));
                    double price = 0;
                    for (int i = 0; i < list.size(); i++) {
                        price = price + list.get(i).Quantity * Double.parseDouble(list.get(i).Price);
                    }
                    allPrice.setText(PreferencesUtils.getString(OrderBuyCeActivity.this,"key_Currency") +df.format(price));
                    realityPrice.setText(PreferencesUtils.getString(OrderBuyCeActivity.this,"key_Currency")+df.format(price));
                    sallPrice = price;
                    //price = price + list.get(position).Quantity * Double.parseDouble(list.get(position).Price);
                    if (list.get(position).Quantity == 0) {
                        getMyCar(goodDetial.User.UserExtInfo.IsBag + "");
                    }
                }
            }
        });
    }

    /**
     * 配置recycleview
     * @param recyclerView
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

    /**
     * 配置recycleview
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != goodDetial) {
//            getMyCar(goodDetial.User.UserExtInfo.IsBag.toString());
        }
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:

                if (isShow){
                    tvOther.setBackgroundResource(R.drawable.button_sharp);
                    orderBuyAdapter.setIsMoreGood(false);
                    orderBuyAdapter.notifyDataSetChanged();
                    isShow=false;
                    more_good.setVisibility(View.GONE);
                }else {
                    tvOther.setBackgroundResource(R.drawable.button_gray);
                    orderBuyAdapter.setIsMoreGood(true);
                    orderBuyAdapter.notifyDataSetChanged();
                    more_good.setVisibility(View.VISIBLE);
                    isShow=true;
                }

//                iv_iszhangkai.setImageResource(R.mipmap.shouqi);
//                orderBuyAdapter.setIsMoreGood(true);
//                orderBuyAdapter.notifyDataSetChanged();
//                if (isZhangKai == false){
//                    iv_iszhangkai.setImageResource(R.mipmap.shouqi);
//                    orderBuyAdapter.setIsMoreGood(true);
//                    orderBuyAdapter.notifyDataSetChanged();
//                    isZhangKai = true;
//                }else{
//                    iv_iszhangkai.setImageResource(R.mipmap.zhankai);
//                    hot_up.setVisibility(View.GONE);
//                    orderBuyAdapter.setIsMoreGood(false);
//                    orderBuyAdapter.notifyDataSetChanged();
//                    isZhangKai = false;
//                }

                break;
            case R.id.more_good://差看更多
//                if (isZhangKai == false){
//                    iv_iszhangkai.setImageResource(R.mipmap.shouqi);
//             //       tv_iszhangkai.setText("收起");
//                    orderBuyAdapter.setIsMoreGood(true);
//                    orderBuyAdapter.notifyDataSetChanged();
//                    more_good.setVisibility(View.VISIBLE);
//                    hot_up.setVisibility(View.VISIBLE);
//                    isZhangKai = true;
//                }else{
//                    iv_iszhangkai.setImageResource(R.mipmap.zhankai);
//              //      tv_iszhangkai.setText("更多促销商品");
//                    hot_up.setVisibility(View.GONE);
//                    orderBuyAdapter.setIsMoreGood(false);
//                    orderBuyAdapter.notifyDataSetChanged();
//                    isZhangKai = false;
//                }
                tvOther.setBackgroundResource(R.drawable.button_sharp);
                more_good.setVisibility(View.GONE);
                orderBuyAdapter.setIsMoreGood(false);
                orderBuyAdapter.notifyDataSetChanged();
                isShow=false;
                break;
            case R.id.rl_liuyan://给商家留言
                if (beizhu.getVisibility() == View.GONE) {
                    beizhu.setVisibility(View.VISIBLE);
                    liuyan.setBackgroundResource(R.mipmap.new_up);
                    beizhu.requestFocus();
                } else {
                    beizhu.setVisibility(View.GONE);
                    liuyan.setBackgroundResource(R.mipmap.new_down);
                }
                break;
            case R.id.dikou://抵扣
                IsUsePoint = true;
                jiefenDeduction(cartListId);
                break;
            case R.id.jiesuan://结算
                if (TextUtils.isEmpty(name.getText())){
                    Toast.makeText(this, "请输入收货人姓名、电话和地址", Toast.LENGTH_SHORT).show();
                    scrollview.fullScroll(View.FOCUS_UP);
                    name.requestFocus();
                    break;
                }

                if (TextUtils.isEmpty(phone.getText())){
                    Toast.makeText(this, "请输入收货人姓名、电话和地址", Toast.LENGTH_SHORT).show();
                    scrollview.fullScroll(View.FOCUS_UP);
                    phone.requestFocus();
                    break;
                }
                if (TextUtils.isEmpty(address.getText())){
                    Toast.makeText(this, "请输入收货人姓名、电话和地址", Toast.LENGTH_SHORT).show();
                    scrollview.fullScroll(View.FOCUS_UP);
                    address.requestFocus();
                    break;
                }

                if (0 == list.size()){
                    Toast.makeText(this, "购物车暂无商品", Toast.LENGTH_SHORT).show();
                    break;
                }

                //目前再来一单出现问题 - 先try起来
                try{
                    payDialog = new PayDialog(OrderBuyCeActivity.this, realityPrice.getText().toString().substring(1,realityPrice.getText().toString().length()));
                    Window window = payDialog.getWindow();
                    WindowManager.LayoutParams params = new WindowManager.LayoutParams();
                    //设置x坐标
                    params.x = 0;
                    //设置y坐标
                    params.y = 0;
                    window.setAttributes(params);
                    //设置点击Dialog外部任意区域关闭Dialog
                    payDialog.setCanceledOnTouchOutside(true);
                    payDialog.setOnClickHandler(new PayDialog.OnClickHandler() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.paymode_wx:
                                    payStyle = 2;
                                    if (null != PrivoceId && null != CityId){
                                        if (!TextUtils.isEmpty(NoPayOrder)){
                                            weChatPaySign(NoPayOrder);
                                        }else{
                                            submitOrder(IsUsePoint, name.getText().toString(), phone.getText().toString(), PrivoceId, CityId, AreaId, address.getText().toString(), fapiao.getText().toString(), beizhu.getText().toString(), cartListId);
                                        }
                                    }
                                    break;
                                case R.id.paymode_zfb:
                                    payStyle = 1;
                                    if (null != PrivoceId && null != CityId){
                                        if (!TextUtils.isEmpty(NoPayOrder)){
                                            alipaySign(NoPayOrder);
                                        }else{
                                            submitOrder(IsUsePoint, name.getText().toString(), phone.getText().toString(), PrivoceId, CityId, AreaId, address.getText().toString(), fapiao.getText().toString(), beizhu.getText().toString(), cartListId);
                                        }
                                    }
                                    break;
                                case R.id.paymode_md:
                                    payStyle = 3;
                                    if (null != PrivoceId && null != CityId){
                                        if (!TextUtils.isEmpty(NoPayOrder)){
                                            Intent intent = new Intent(OrderBuyCeActivity.this, PayLineActivity.class);
                                            intent.putExtra("submitOrderResult", (Serializable) submitOrderResult);
                                            startActivity(intent);
//                                            finish();
                                        }else{
                                            submitOrder(IsUsePoint, name.getText().toString(), phone.getText().toString(), PrivoceId, CityId, AreaId, address.getText().toString(), fapiao.getText().toString(), beizhu.getText().toString(), cartListId);
                                        }
                                    }
                                    break;
                                    default:
                                        break;
                            }
                        }
                    });
                    payDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(this, "请选择商品", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.hot_sales:
                more_good.setVisibility(View.GONE);
           //     iv_iszhangkai.setImageResource(R.mipmap.zhankai);
             //   tv_iszhangkai.setText("更多促销商品");
                hot_up.setVisibility(View.GONE);
                orderBuyAdapter.setIsMoreGood(false);
                orderBuyAdapter.notifyDataSetChanged();
                isZhangKai = false;
                break;
            case R.id.add_address://更新收货地址
                if (addAdress.getText().equals("更换收货地址")) {
                    isEditor = true;
                    addAdress.setText("保存");
                    tv_choosessq.setVisibility(View.VISIBLE);
                    name.setText("");
                    phone.setText("");
                    address.setText("");
                    address.setHint("请输入街道、门牌号");

                    name.setFocusable(true);
                    name.setCursorVisible(true);
                    name.setFocusableInTouchMode(true);
                    name.requestFocus();

                    phone.setFocusable(true);
                    phone.setCursorVisible(true);
                    phone.setFocusableInTouchMode(true);

                    address.setFocusable(true);
                    address.setCursorVisible(true);
                    address.setFocusableInTouchMode(true);
                } else {
                    isEditor = false;
                    if (TextUtils.isEmpty(name.getText())){
                        Toast.makeText(this, "请输入收货人姓名、电话和地址", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(phone.getText())){
                        Toast.makeText(this, "请输入收货人姓名、电话和地址", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if ("请选择省市区".equals(tv_choosessq.getText())){
                        Toast.makeText(this, "请输入收货人姓名、电话和地址", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (TextUtils.isEmpty(address.getText())){
                        Toast.makeText(this, "请输入收货人姓名、电话和地址", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    PrefUtils.setString(this, "cityAddress",tv_choosessq.getText().toString()+address.getText().toString());
                    PrefUtils.setString(this, "userphone",phone.getText().toString());
                    PrefUtils.setString(this, "username", name.getText().toString());
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    address.setText(tv_choosessq.getText().toString()+address.getText().toString());
                    addAdress.setText("更换收货地址");
                    tv_choosessq.setVisibility(View.GONE);

                    name.setCursorVisible(false);
                    name.setFocusable(false);
                    name.setFocusableInTouchMode(false);

                    phone.setCursorVisible(false);
                    phone.setFocusable(false);
                    phone.setFocusableInTouchMode(false);

                    address.setCursorVisible(false);
                    address.setFocusable(false);
                    address.setFocusableInTouchMode(false);
                }
                break;
            case R.id.tv_choosessq://选择省市区
                PopupWindowFromBottomUtil.showAddress(OrderBuyCeActivity.this, LayoutInflater.from(OrderBuyCeActivity.this).inflate(R.layout.business_my_center_activity, null), list1, new PopupWindowFromBottomUtil.Listenre1() {
                    @Override
                    public void callBack(String Id,String Id1, String Id2, String Id3, String content, PopupWindow popupWindow) {
                        tv_choosessq.setText(content);
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.id.tv_cuxiaojia://我要促销价
                needCuXiao();
                break;
            case R.id.name://姓名
                if (addAdress.getText().equals("更换收货地址")){
                    updataAddressDialog();
                }
                break;
            case R.id.phone://电话
                if (addAdress.getText().equals("更换收货地址")){
                    updataAddressDialog();
                }
                break;
            case R.id.address://地址
                if (addAdress.getText().equals("更换收货地址")){
                    updataAddressDialog();
                }
                break;
            case R.id.back://返回
                finish();
                /**
                 * 清空购物车
                 */
                clearCar();
                break;
                default:
                    break;
        }
    }

    /**
     * 我要促销价
     */
    public void needCuXiao(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                Intent intent = new Intent(OrderBuyCeActivity.this, NeedCuXiaoPriceActivity.class);
                intent.putExtra("cartListId", (Serializable) cartListId);
                intent.putExtra("SellerId",goodDetial.User.Id);
                startActivity(intent);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog2(this, "温馨提示", "请与门店对非促销品进行议价", "知道了", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 点击姓名电话地址
     */
    public void updataAddressDialog(){
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
        Dialog dialog = new XiMaiPopDialog1(this, "温馨提示", "请点击更换收货地址", "知道了", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /**
     * 查询基础字典
     */
    private void queryDicNode() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.QUERYDICNODE, stringEntity, new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                ListBaseMessage listBaseMessage = GsonUtils.fromJson(new String(responseBody), ListBaseMessage.class);
                list1 = listBaseMessage.MainData;
            }
        });
    }

    /**
     * 获取商品展示 - 这个商家的更多商品
     * @param SellerId
     */
    private void getAllGoods(String SellerId) {
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.GET_GOODS, RequestParamsPool.getSalesGoods(SellerId), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String resule = new String(responseBody);
                goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                if (goodsList.IsSuccess) {
                    if (null != goodsList.MainData) {
                        String buyGoosShow = PreferencesUtils.getString(OrderBuyCeActivity.this, "buyGoosShow", "");
                        //这里需要判断一下地址不一样的不可以显示 - 需求是一个点的情况下显示
                        for (int i = 0; i < goodsList.MainData.size(); i++) {
                            if (buyGoosShow.equals(goodsList.MainData.get(i).Country+" · "+goodsList.MainData.get(i).Province + goodsList.MainData.get(i).City + goodsList.MainData.get(i).Address)){
                                list_good.add(goodsList.MainData.get(i));
                            }
                        }
                        orderBuyAdapter.setData(list_good);
                        if (list_good.size() > 2) {
                            tvOther.setBackgroundResource(R.drawable.button_sharp);
                            tvOther.setClickable(true);
                            isShow=false;
                            more_good.setVisibility(View.GONE);
                        }else {
                            tvOther.setClickable(false);

                        }
                        orderBuyAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * 获取购物车信息 - 也是就是已选中的商品
     * @param isBag
     */
    private void getMyCar(String isBag) {
        // ls_list = list;
        list.clear();
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.GET_MYCAR, RequestParamsPool.get_car(isBag), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    cartDetail = GsonUtils.fromJson(object.optString("MainData"), CartDetail.class);
                    if (null != cartDetail) {
                        if (null != cartDetail.CartProduct && cartDetail.CartProduct.size() > 0) {
                            double price = 0;
                            for (int i = cartDetail.CartProduct.size() - 1; i < cartDetail.CartProduct.size() && i > -1; i--) {
                                if (cartDetail.CartProduct.get(i).Quantity > 0) {
                                    list.add(cartDetail.CartProduct.get(i));
//                                    idList.add(cartDetail.CartProduct.get(i).ProductId);//商品的ID字段
                                    cartListId.add(cartDetail.CartProduct.get(i).Id);//购物车Id
                                    orderBuyChooseAdapter.setAddress(cartDetail.CartProduct.get(i).Address);//设置非促销品地址
                                    BaseApplication.getInstance().noCuXiaoGoodsAddress = cartDetail.CartProduct.get(i).Address;//非促销品保存
                                    price = price + cartDetail.CartProduct.get(i).Quantity * Double.parseDouble(cartDetail.CartProduct.get(i).Price);//数量乘以价格
                                }
                            }
                            orderBuyChooseAdapter.setData(list);
                            if (list.size() > 0){
                                if (!TextUtils.isEmpty(list.get(list.size()-1).Currency)){
                                    PreferencesUtils.putString(OrderBuyCeActivity.this,"key_Currency",list.get(list.size()-1).Currency);
                                }else{
                                    PreferencesUtils.putString(OrderBuyCeActivity.this,"key_Currency","");
                                }
                            }else{
                                PreferencesUtils.putString(OrderBuyCeActivity.this,"key_Currency","");
                            }
                            allPrice.setText(PreferencesUtils.getString(OrderBuyCeActivity.this,"key_Currency") +df.format(price));
                            realityPrice.setText(PreferencesUtils.getString(OrderBuyCeActivity.this,"key_Currency")+df.format(price));
                            sallPrice = price;
                        }
                        orderBuyChooseAdapter.notifyDataSetChanged();
                    }

                    /**
                     * 删除商品
                     */
                    if (null != goodDetial.User) {
                        double v = Double.parseDouble(goodDetial.User.UserExtInfo.RebatePercent) * 100;
                        int a = (int) v;
                        if (0 == a){
                            dikou.setText("本店无返利");//门店消费返利
                            dikou.setEnabled(false);
                        }else{
                            dikou.setText("积分返利"+a+ "%");//门店消费返利
                            dikou.setEnabled(true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 是促销品的添加购物车 - 进行删除
     * @param ProductId
     * @param Quantity
     * @param CartOperaType
     */
    private void addCat(String ProductId, String Quantity, final String CartOperaType) {
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.UPDATE_CAR, RequestParamsPool.update_car(ProductId, Quantity, CartOperaType), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String success = object.optString("IsSuccess");
                    String message = object.optString("Message");
                    if (CartOperaType.equals("4")) {
                        getMyCar(goodDetial.User.UserExtInfo.IsBag + "");
                        Toast.makeText(OrderBuyCeActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    /**
                     * 删除商品
                     */
                    if (null != goodDetial.User) {
                        double v = Double.parseDouble(goodDetial.User.UserExtInfo.RebatePercent) * 100;
                        int a = (int) v;
                        if (0 == a){
                            dikou.setText("本店无返利");//门店消费返利
                            dikou.setEnabled(false);
                        }else{
                            dikou.setText("积分返利"+a+ "%");//门店消费返利
                            dikou.setEnabled(true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 非促销产品添加到购物车
     */
    private void needCuXiaoPrice(String SellerId, String CartId, String ImageId, String ImagePath, String Price, String Quantity, final String CartOperaType) {
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.NEED_CUXIAOJIA, RequestParamsPool.needCuXiaoPrice(SellerId,CartId, ImageId,ImagePath, Price,Quantity,CartOperaType), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String success = object.optString("IsSuccess");
                    String message = object.optString("Message");
                    if (CartOperaType.equals("4")) {
                        getMyCar(goodDetial.User.UserExtInfo.IsBag + "");
                        Toast.makeText(OrderBuyCeActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    /**
                     * 删除商品
                     */
                    if (null != goodDetial.User) {
                        double v = Double.parseDouble(goodDetial.User.UserExtInfo.RebatePercent) * 100;
                        int a = (int) v;
                        if (0 == a){
                            dikou.setText("本店无返利");//门店消费返利
                            dikou.setEnabled(false);
                        }else{
                            dikou.setText("积分返利"+a+ "%");//门店消费返利
                            dikou.setEnabled(true);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 提交订单
     */
    private void submitOrder(boolean IsUsePoint, String Recipients, String PhoneNumber, String ProvinceId, String CityId, String AreaId, String Address, String InvoiceTitle, String Remark, List<String> list) {
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.SUBMIT_ORDER, RequestParamsPool.submit_order(IsUsePoint, Recipients, PhoneNumber, ProvinceId, CityId, AreaId, Address, InvoiceTitle, Remark, list), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    Boolean isScuess = object.optBoolean("IsSuccess");
                    String message = object.optString("Message");
                    if (true == isScuess){
                        submitOrderResult = GsonUtils.fromJson(result, submitOrderResults.class);
                        /**
                         * 将订单号进行保存 - 如果是取消的订单那么可以进行再次支付
                         */
                        List<submitOrderResults.MainData> mainData = submitOrderResult.MainData;
                        NoPayOrder = submitOrderResult.MainData.get(0).Id;
                        if (null != submitOrderResult && submitOrderResult.MainData.size() > 0) {
                            if (payStyle == 1) {//支付宝
                                alipaySign(submitOrderResult.MainData.get(0).Id);//订单Id
                            } else if (payStyle == 2){//微信
                                weChatPaySign(submitOrderResult.MainData.get(0).Id);//订单Id
                            }else if (payStyle == 3){//门店支付
                                Intent intent = new Intent(OrderBuyCeActivity.this, PayLineActivity.class);
                                intent.putExtra("submitOrderResult", (Serializable) submitOrderResult);
                                startActivity(intent);
                            }
                        }
                    }else{
                        Toast.makeText(OrderBuyCeActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 支付宝支付签名
     * @param Id
     */
    private void alipaySign(String Id) {
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.ALIPAY_SIGN, RequestParamsPool.alipaySign(Id), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = null;
                try {
                    object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                alipaySignResult = GsonUtils.fromJson(object.optString("MainData"), AlipaySignResult.class);
                if (null != alipaySignResult) {
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(OrderBuyCeActivity.this);
                            String result = alipay.pay(aliPayParameter(alipaySignResult));
                            Log.i("msp", result.toString());
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }
            }
        });
    }

    /**
     * 微信支付
     * @param Id
     */
    private void weChatPaySign(final String Id) {
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.WECHAT_SIGN, RequestParamsPool.weChatSign(Id), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    weChatSign = GsonUtils.fromJson(jsonObject.optString("MainData"), WeChatSign.class);
                    final IWXAPI msgApi = WXAPIFactory.createWXAPI(OrderBuyCeActivity.this, null);
                    msgApi.registerApp(weChatSign.appid);
                    LogUtils.instance.d("appid=" + weChatSign.appid);
//                    WXPayEntryActivity wxPayEntryActivity = new WXPayEntryActivity();
//                    wxPayEntryActivity.setOrderId(Id);
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    BaseApplication.getInstance().OrderId = Id;
                    req.appId = weChatSign.appid;
                    req.partnerId = weChatSign.partnerid;
                    req.prepayId = weChatSign.prepayid;
                    req.nonceStr = weChatSign.noncestr;
                    req.timeStamp = weChatSign.timestamp;
                    req.packageValue = "Sign=WXPay";
                    req.sign = weChatSign.sign;
                    msgApi.sendReq(req);
//                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 构造支付宝支付参数
     * @param result
     * @return
     */
    private String aliPayParameter(AlipaySignResult result) {
        String aliPayString = "app_id=" + result.app_id + "&";
        aliPayString = aliPayString + "method=" + result.method + "&";
        aliPayString = aliPayString + "format=" + result.format + "&";
        aliPayString = aliPayString + "charset=" + result.charset + "&";
        aliPayString = aliPayString + "sign_type=" + result.sign_type + "&";
        aliPayString = aliPayString + "timestamp=" + result.timestamp + "&";
        aliPayString = aliPayString + "version=" + result.version + "&";
        aliPayString = aliPayString + "notify_url=" + result.notify_url + "&";
        aliPayString = aliPayString + "biz_content=" + result.biz_content + "&";
        aliPayString = aliPayString + "sign=" + result.sign;
        return aliPayString;
    }

    /**
     * 积分抵扣
     * @param list
     */
    private void jiefenDeduction(List<String> list) {
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.JIFEN_DIKOU, RequestParamsPool.jifenDikou(list), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
                    @Override
                    public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(new String(responseBody));
                            dikouResult = GsonUtils.fromJson(object.optString("MainData"), DikouResult.class);
                            if (object.optString("IsSuccess").equals("true")) {
                                //Point - 当前积分
                                //DeductionPoint -- 抵扣积分
                                //DeductionPrice    --抵扣金额
                                //DeductionDescription  --抵扣描述
                                Toast.makeText(OrderBuyCeActivity.this, object.optString("Message"), Toast.LENGTH_SHORT).show();
                                //实付金额 -  抵扣金额 = 应付
                                srealityPrice = sallPrice - dikouResult.DeductionPrice;
                                //实际支付
                                realityPrice.setText(PreferencesUtils.getString(OrderBuyCeActivity.this,"key_Currency")+ df.format(srealityPrice) + "");
                                //根据积分来判断
                                //抵扣积分
                                int relaseJIFen = Integer.parseInt(dikouResult.DeductionPoint);
                                //积分
                                int currentJiFen = Integer.parseInt(dikouResult.Point);
                                if (relaseJIFen < currentJiFen || relaseJIFen == currentJiFen){
                                    dikou.setText("返利" + dikouResult.DeductionPrice + "元");
                                }else{
                                    dikou.setText("积分不够");
                                }
                                //dikouResult.DeductionPoint扣多少分
                                //dikouResult.DeductionPrice优惠多少钱
                                //dikou.setText("扣" + dikouResult.DeductionPoint + "分 — " + dikouResult.DeductionPrice + "元");
                            } else {
                                dikou.setText(object.optString("Message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }

    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName,Object cid) {
            try {
                if (Constants.LOADING_ORDERFRAGMENT.equals(eventName)) {
                    /**
                     *  获取购物车信息-
                     */
                    getMyCar(goodDetial.User.UserExtInfo.IsBag.toString());
                }else if(Constants.SEE_WULIU_FINISHORDERBUY.equals(eventName)){
                    /**
                     * 支付成功
                     */
                    finish();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.LOADING_ORDERFRAGMENT);
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.SEE_WULIU_FINISHORDERBUY);
        /**
         * 清空购物车
         */
        clearCar();
    }

    /**
     * 清楚购物车
     */
    private void clearCar() {
        WebRequestHelper.json_post(OrderBuyCeActivity.this, URLText.CLEAR_CAR, RequestParamsPool.clearCar(), new MyAsyncHttpResponseHandler(OrderBuyCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                // LogUtils.instance.d("清空购物车=" + result);
                try {
                    JSONObject object = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 菜单创建器，在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(OrderBuyCeActivity.this)
                        .setBackground(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除")
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。
            }
        }
    };
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();
            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                if ("非促销品".equals(list.get(adapterPosition).Name)){
                    needCuXiaoPrice(null,list.get(adapterPosition).Id,null,null,null,"1","4");
                }else{
                    addCat(list.get(adapterPosition).ProductId, "1", "4");
                }
            }
        }
    };
}