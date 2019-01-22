package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.PopupWindowFromBottomUtil;
import com.ximai.savingsmore.save.adapter.ProduceFragmentAdapter;
import com.ximai.savingsmore.save.adapter.SearchAddressAdapter;
import com.ximai.savingsmore.save.adapter.SearchBusinessAdapter;
import com.ximai.savingsmore.save.adapter.SearchBussGoodsAdapter;
import com.ximai.savingsmore.save.adapter.ServiceFragmentAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.Business;
import com.ximai.savingsmore.save.modle.BusinessList;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.ListBaseMessage;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.ProduceBean;
import com.ximai.savingsmore.save.modle.ProductParamModle;
import com.ximai.savingsmore.save.modle.SearchLocationModle;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.SelectSearchPopupWindow;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by luxing on 16/11/21.最终版
 */
public class SearchActivitys extends BaseActivity implements View.OnClickListener {

    /**
     *产品类商品
     */
    private int [] chnagpingimage = {
            R.mipmap.jujia, R.mipmap.nanzhuang, R.mipmap.jiangju, R.mipmap.yanjiu, R.mipmap.xiemao,
            R.mipmap.qicheyongpin, R.mipmap.shipin, R.mipmap.gehu, R.mipmap.huangjin, R.mipmap.yiyap,
            R.mipmap.jiadian, R.mipmap.tushu, R.mipmap.yunfu, R.mipmap.shouji, R.mipmap.wanju,
            R.mipmap.jinkou, R.mipmap.zhongbiao, R.mipmap.tiyu, R.mipmap.shechi, R.mipmap.diannao,
            R.mipmap.neiyi, R.mipmap.ertong, R.mipmap.chongwu, R.mipmap.xianhua, R.mipmap.baoxian,
            R.mipmap.fangwu, R.mipmap.qiche,R.mipmap.ershou,R.mipmap.changjia
    };
    private String [] chnagpingtitle = {"居家百货","男装女装","家具建材","烟酒商品","鞋帽箱包","汽车用品","食品生鲜",
            "个护化妆","黄金珠宝","医药保健","家用电器","图书音像","孕妇用品","手机数码","玩具乐器","进口商品",
            "钟表眼镜","体育用品","奢侈珍品","电脑办公","内衣配饰","儿童用品","宠物用品","鲜花礼品","保险理财",
            "商品房屋","各类汽车","二手寄卖","厂家直销"};
    private String [] chnagpingParentId = {
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418"};
    private String [] chnagpingId   = {
            "d412dc20-b5c1-46e5-a5d6-6f77c46504f9",
            "280620b5-416d-453d-b29a-df640f70b6e4",
            "577d68fa-84b7-4fc1-bbeb-4124bbad7019",
            "404d435a-0342-4de8-8cff-e5616b96c1dd",
            "a85cf134-3002-48d6-8005-d5c349baea8b",
            "a13ef634-13d3-45d2-9c92-ea37678bb20e",
            "6dd8f12d-64fc-4b40-a655-e928a4a00ea1",
            "00655e8b-60ed-43e7-bfd0-74e833b68a32",
            "d7cdcb94-6511-4533-82bc-af22ab9b768f",
            "5a518b0c-d525-4142-b300-47240fd2640b",
            "60245191-39bc-4b4c-97f2-ee77171d6a9c",
            "b638d80d-9616-4bd3-bc65-75356c1f9c8c",
            "b3018235-1910-475b-8cf6-691cf81c1c10",
            "624930e2-cfcd-4520-911e-7fbdb38a0d3c",
            "ca09850e-617b-4b3c-a122-7b67a4977cbd",
            "8aa13ef2-0f10-4ea6-8e6b-e5c754c8fbfb",
            "e0525093-3ae1-4a51-b418-05db6494453a",
            "24dcf2d9-852d-40b0-8fc8-2f3d441a84a8",
            "8a659023-ab74-4ebc-b7be-0dab426aafef",
            "730e47bb-a0eb-4300-9455-f1a0fadfbd2b",
            "4e57313a-bfd2-426d-a25f-4601cf296bf8",
            "5158e510-ca36-42ad-9d58-34b97cd19643",
            "7533b2c3-5870-4472-a4bb-4bf7ce946f3c",
            "e74123c8-b987-45d5-a93f-7946e65ae0d6",
            "c607bf1e-9a35-4776-966c-d9dd4c7f4ea0",
            "253e2bb4-6815-49f7-9978-15171b005b8a",
            "72ae8162-0ee7-45d4-a83f-3049af7c43de",
            "7666175e-bdec-42eb-bbd2-6381a5037e97",
            "09889f8b-99ea-4755-b99d-983bb9dcd88a"};
    private String [] chnagpingSortNo    = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","19","20","21","22","23","24","25","26","27","28","29"};
    private String [] chnagpingIsBag  = {"1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};
    /**
     * 商品数据
     */
    private int [] shangpingimage = {
            R.mipmap.tuoban, R.mipmap.zuyu, R.mipmap.hunqin, R.mipmap.ruxue, R.mipmap.fangwu, R.mipmap.peixun,
            R.mipmap.fudao, R.mipmap.vfuwu, R.mipmap.weixiu, R.mipmap.ertong, R.mipmap.youshanwanshui, R.mipmap.yundong,
            R.mipmap.dingzhi, R.mipmap.chuguoyouxue, R.mipmap.xiuxianyule, R.mipmap.meirong, R.mipmap.jiudian,
            R.mipmap.yiliao, R.mipmap.ganxi, R.mipmap.gaojicanyin, R.mipmap.qiye, R.mipmap.zufang,
            R.mipmap.xingqu, R.mipmap.meija, R.mipmap.yanglao,R.mipmap.bocai,
            R.mipmap.gonggongsheshi,R.mipmap.qingrenyuehui,R.mipmap.chongdian,R.mipmap.gongxiangche,R.mipmap.tingchechangdi,R.mipmap.wurenlingshou,R.mipmap.yedianfuwu,R.mipmap.paimaidiandang,R.mipmap.jiazheng,
            R.mipmap.kuaidi,R.mipmap.gongjiaochuxing,R.mipmap.tuoguanjicun,R.mipmap.youdihuoyun,R.mipmap.yuezihuisuo,R.mipmap.yanchusaishi
    };

    private String [] shangpingtitle = {"托班早教","足浴按摩","婚庆仪式","入学指导","房屋装修","认证培训","课外辅导",
            "生活服务","维修保养","儿童娱乐","游山玩水","运动健身","定制服装","出国游学","休闲娱乐","美容美发",
            "酒店宾馆","医疗保健","衣帽干洗","特色餐饮","企业服务","租房租车","培养兴趣","纹身美甲","养老服务",
            "博彩服务","公共设施","情人约会","各种充电","共享车辆","停车场地","无人零售","夜店服务","拍卖典当","家政服务","快递闪送","公交出行","托管寄存","邮递货运","月子会所","演出赛事"};

    private String [] shangpingParentId = {
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418"};
    private String [] shangpingId   = {
            "68393df6-ae02-4507-861d-716a2b6e5c27",
            "8e7da709-f9bf-4a72-873c-d8e79b5000a1",
            "486ec489-0292-425e-87dc-3d568926af77",
            "30662608-cb82-4750-9696-67dc5fe34c0d",
            "5d49aced-830a-43a6-864b-829e2883e09e",
            "9b87d5d2-49f5-4cc6-aeb7-1aab931f9f0a",
            "e9d5f6a6-131d-47e2-a09b-0e1252cc04ea",
            "6ae3fb54-1a47-4832-82bc-4dbce8cdcb67",
            "0db32cbc-5e14-4e7f-a47f-9ef16a451711",
            "13ab7750-ffb6-48d5-aa06-2733355e3269",
            "ba7ef5a2-88bf-4072-98e4-33a46a9a0ce2",
            "ecbdb4f3-a62f-4a30-b18e-95389964cd7a",
            "724a3029-f218-487f-a60b-046765dfc64d",
            "afe5dc8e-adda-4c04-82c6-66a5e97b9514",
            "1b60c46c-be1c-48a1-9aba-17dc0f76980d",
            "98241c56-9fa4-4200-a094-e343317a6d71",
            "42d7a004-3f62-41f5-97e2-e4afd3eaa4ec",
            "4e9ebbb9-5d29-4280-b373-a4c163b68084",
            "235b02ee-5b19-426d-8125-ad18a2ee65cb",
            "0b266ee3-34b6-4f77-8fd8-2ec920b9c567",
            "af969a9f-2e34-4445-9f4e-f4fe75fe7a81",
            "ff12e72c-4817-4e0b-96ad-15c1b9d77722",
            "e2a9c892-44fa-49be-a7ae-cf521d81949a",
            "e6476a6a-1a48-4cd4-829b-d09e67b6166e",
            "cd402eda-0605-4a04-bb8c-62a71c794120",
            "c66a7298-e8a6-4132-a2de-2501432efe65",
            "c0503153-0b97-41cb-b2c5-a2d6e0885d6e",
            "47e0bf11-05e1-4c96-a02f-a95452070dcf",
            "ce00e592-cb03-4246-b2b6-f2344bc24c95",
            "8699b66c-acf2-4042-8eae-8ffa27831e28",
            "38434208-ab1a-4685-8a9f-f7137b9c20bf",
            "da2e2103-1aab-4fcd-b7f4-b9e2ac63203f",
            "8bed9b04-1e6f-4431-ba13-6932f520072f",
            "aabd6f8f-9b20-4f54-9ed9-56c5c8dc6883",
            "e54e11cf-c087-407b-9b81-b8a9dc77a25b",
            "791751da-400b-4ff5-a422-691552672e84",
            "8b186842-5e63-45ce-a545-ef375868dc40",
            "432acb37-e6d2-4a85-8922-7eadc32fcd00",
            "193b0f29-ffe1-43ad-a929-9347bcdf32be",
            "e60ac3dc-a103-4e1d-bf72-a85568debf7b",
            "a01c72ed-dee1-4e94-a412-59a9bb037a4e"
    };
    private String [] shangpingSortNo    = {"28","29","30","32","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54","55","56","57","58","59","60","61","62","63","64","65","66","67","68","69","70"};
    private String [] shangpingIsBag  = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};
    private RelativeLayout back;
    private TextView cen_title;
    private EditText etInput;
    private LinearLayout ll_citys;
    private LinearLayout ll_choosebusines;
    private List<ProduceBean> changpinglist = new ArrayList<>();
    private List<ProduceBean> shangpinglist = new ArrayList<>();
    private RecyclerView recycle_view_fg;
    /**
     * 方格展示适配器
     */
    private ProduceFragmentAdapter produceFragmentAdapter;
    private String goodsId;
    private String goodsfont;
    private Handler handler;
    /**
     * 搜索到的商品
     */
    private GoodsList goodsList = new GoodsList();
    private List<Goods> listGoods = new ArrayList<>();
    private LinearLayout ll_cpgoods;
    private LinearLayout ll_fugoods;
    private View view_cpgoodsline;
    private View view_fugoodsline;
    private ServiceFragmentAdapter serviceFragmentAdapter;
    private boolean isGoodsAndBUsiness = true;
    private TextView tv_chooses;
    private RelativeLayout rl_clearinputs;
    private LinearLayout ll_search;
    private LinearLayout show_chanping;
    private LinearLayout ll_defaultdata;
    private RecyclerView recycle_view_shangping;
    private LinearLayout ll_paixu;
    private SearchBussGoodsAdapter searchBussGoodsAdapter;
    /**
     * 基础数据
     */
    private List<BaseMessage> allList = new ArrayList<>();
    private TextView tv_citys;
    /**
     * 用来记录选中的城市ID
     */
    private String ProvinceId,CityId,AreaId;
    private RecyclerView recycle_view_shangjia;
    private SearchBusinessAdapter searchBusinessAdapter;
    /**
     * 商家信息
     */
    private List<Business> listBusiness;
    private TextView tv_liebiao;
    private TextView tv_cpgoods;
    private TextView tv_fugoods;
    private LinearLayout ll_businessdata;
    private TextView tv_businessfabu;

    private LinearLayout xiaoliang;
    private LinearLayout jiage;
    private LinearLayout riqi;
    private LinearLayout juli;

    private boolean xiaoliangbool = false;
    private boolean jiagebool = true;
    private boolean riqibool = false;
    private boolean julibool = true;
    private ImageView iv_xiaoliang;
    private ImageView iv_jiage;
    private ImageView iv_riqi;
    private ImageView iv_juli;
    private LinearLayout ll_businessNoDada;
    private LinearLayout ll_choosebusines_down;
    private String isPeopleAndBusiness;
    private KyLoadingBuilder builder;
    private SelectSearchPopupWindow selectSearchPopupWindow;
    private LinearLayout ll_callphone;
    private String searchActivitys;
    private EditText mEtLocationAddress;
    private RelativeLayout mRlClearAddress;
    private RecyclerView recycle_view_address;
    private SearchAddressAdapter searchAddressAdapter;
    private TextView mTvSearchMsg1;
    private TextView mTvSearchMsg2;
    private TextView tv_bottom1,tv_bottom2,tv_bottom3,tv_bottom4;
    private boolean isFirst=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activitys);

        initView();
        
        initData();

        initEvent();

        queryDicNode();
    }

    /**
     * view
     */
    private void initView() {
        /**
         * 将继承标记进行删除
         */
        toolbar.setVisibility(View.GONE);
        back = (RelativeLayout) findViewById(R.id.back);
        cen_title = (TextView) findViewById(R.id.cen_title);
        //输入框--------搜索框
        ll_search = (LinearLayout) findViewById(R.id.ll_search);
        etInput = (EditText) findViewById(R.id.search_et_inputs);
        //城市选中
        ll_citys = (LinearLayout) findViewById(R.id.ll_citys);
        tv_citys = (TextView) findViewById(R.id.tv_citys);
        //商家的点击事件
        ll_choosebusines = (LinearLayout) findViewById(R.id.ll_choosebusines);
        //删除
        rl_clearinputs = (RelativeLayout) findViewById(R.id.rl_clearinputs);
        recycle_view_fg = (RecyclerView) findViewById(R.id.recycle_view_fg);
        tv_chooses = (TextView) findViewById(R.id.tv_chooses);
        show_chanping = (LinearLayout) findViewById(R.id.show_chanping);
        //商品----------产品和服务
        ll_cpgoods = (LinearLayout) findViewById(R.id.ll_cpgoods);
        //服务
        ll_fugoods = (LinearLayout) findViewById(R.id.ll_fugoods);
        tv_cpgoods = (TextView) findViewById(R.id.tv_cpgoods);
        tv_fugoods = (TextView) findViewById(R.id.tv_fugoods);
        //横线
        view_cpgoodsline = findViewById(R.id.view_cpgoodsline);
        //横线
        view_fugoodsline = findViewById(R.id.view_fugoodsline);
        //暂无数据 - 商品
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);
        //搜索商家暂无数据
        ll_businessNoDada = (LinearLayout) findViewById(R.id.ll_businessNoDada);
        ll_choosebusines_down = (LinearLayout) findViewById(R.id.ll_choosebusines_down);
        ll_businessdata = (LinearLayout) findViewById(R.id.ll_businessdata);
        tv_businessfabu = (TextView) findViewById(R.id.tv_businessfabu);
        //----展示商品的recycleview
        recycle_view_shangping = (RecyclerView) findViewById(R.id.recycle_view_shangping);
        //排序
        ll_paixu = (LinearLayout) findViewById(R.id.ll_paixu);
        xiaoliang = (LinearLayout) findViewById(R.id.xiaoliang);
        jiage = (LinearLayout) findViewById(R.id.jiage);
        riqi = (LinearLayout) findViewById(R.id.riqi);
        juli = (LinearLayout) findViewById(R.id.juli);
        iv_xiaoliang = (ImageView) findViewById(R.id.iv_xiaoliang);
        iv_jiage = (ImageView) findViewById(R.id.iv_jiage);
        iv_riqi = (ImageView) findViewById(R.id.iv_riqi);
        iv_juli = (ImageView) findViewById(R.id.iv_juli);
        //展示商家的recycleview
        recycle_view_shangjia = (RecyclerView) findViewById(R.id.recycle_view_shangjia);
        //地图
        tv_liebiao = (TextView) findViewById(R.id.tv_liebiao);
        ll_callphone = (LinearLayout) findViewById(R.id.ll_callphone);
        //地址搜索
        mEtLocationAddress = (EditText) findViewById(R.id.et_location_address);
        //清除地址
        mRlClearAddress = (RelativeLayout) findViewById(R.id.rl_clear_address);
        //地址展示
        recycle_view_address = (RecyclerView) findViewById(R.id.recycle_view_address);
        mTvSearchMsg1 = (TextView) findViewById(R.id.tv_search_msg1);
        mTvSearchMsg2 = (TextView) findViewById(R.id.tv_search_msg2);
        tv_bottom1= (TextView) findViewById(R.id.tv_bottom1);
        tv_bottom2= (TextView) findViewById(R.id.tv_bottom2);
        tv_bottom3= (TextView) findViewById(R.id.tv_bottom3);
        tv_bottom4= (TextView) findViewById(R.id.tv_bottom4);

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
     *event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        ll_citys.setOnClickListener(this);
        ll_choosebusines.setOnClickListener(this);
        ll_choosebusines_down.setOnClickListener(this);
        ll_cpgoods.setOnClickListener(this);
        ll_fugoods.setOnClickListener(this);
        rl_clearinputs.setOnClickListener(this);
        ll_callphone.setOnClickListener(this);
        tv_liebiao.setOnClickListener(this);
        tv_businessfabu.setOnClickListener(this);
        xiaoliang.setOnClickListener(this);
        jiage.setOnClickListener(this);
        riqi.setOnClickListener(this);
        juli.setOnClickListener(this);
        mRlClearAddress.setOnClickListener(this);

        /**
         * 产品的iten点击事件
         */
        produceFragmentAdapter.setOnItenClickListener(new ProduceFragmentAdapter.OnItenClickListener() {
            @Override
            public void onItenClick(int position, List<ProduceBean> list) {
                goodsId = list.get(position).getId();
                goodsfont = list.get(position).getFont();
                for (int i = 0; i < list.size(); i++) {
                    if (i == position) {
                        list.get(i).setChecked(true);
                    } else {
                        list.get(i).setChecked(false);
                    }
                }
                produceFragmentAdapter.notifyDataSetChanged();
                /**
                 * 点击按钮进行自动搜索实现
                 */
                if (null == handler){
                    handler = new Handler();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirst=true;
                        if (TextUtils.isEmpty(ProvinceId)){
                            getAllGoods(null,null,true,goodsId);
                        }else{
                            getAllGoods(ProvinceId,null,true,goodsId);
                        }
                    }
                },200);
            }
        });

        /**
         * 服务的点击事件
         */
        serviceFragmentAdapter.setOnItenClickListener(new ServiceFragmentAdapter.OnItenClickListener() {
            @Override
            public void onItenClick(int position, List<ProduceBean> list) {
                goodsId = list.get(position).getId();
                goodsfont = list.get(position).getFont();
                for (int i = 0; i < list.size(); i++) {
                    if (i == position) {
                        list.get(i).setChecked(true);
                    } else {
                        list.get(i).setChecked(false);
                    }
                }
                serviceFragmentAdapter.notifyDataSetChanged();
                /**
                 * 点击按钮进行自动搜索实现
                 */
                if (null == handler){
                    handler = new Handler();
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isFirst=true;
                        if (TextUtils.isEmpty(ProvinceId)){
                            getAllGoods(null,null,false,goodsId);
                        }else{
                            getAllGoods(ProvinceId,null,false,goodsId);
                        }
                    }
                },200);
            }
        });

        /**
         * 搜手到的商品点击事件 - 这里需要判断是否登录
         */
        searchBussGoodsAdapter.setViewClickListener(new SearchBussGoodsAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, String id) {
                if (null != listGoods  && listGoods.size() > 0){
                    if (com.ximai.savingsmore.save.modle.LoginUser.getInstance().isLogin()) {
                        Intent intent=new Intent(SearchActivitys.this,GoodDetailsActivity.class);
                        intent.putExtra("id",listGoods.get(postion).Id);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SearchActivitys.this, "温馨提示,您还没有登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SearchActivitys.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
        /**
         * 商家的点击事件- 这里需要判断是否登录
         */
        searchBusinessAdapter.setOnItenClickListener(new SearchBusinessAdapter.OnItenClickListener() {
            @Override
            public void onItenClick(int position, List<Business> list) {
                if (com.ximai.savingsmore.save.modle.LoginUser.getInstance().isLogin()) {
                    Intent intent = new Intent(SearchActivitys.this, BusinessMessageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("id", list.get(position).Id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Toast.makeText(SearchActivitys.this, "温馨提示,您还没有登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SearchActivitys.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        /**
         * 输入框完成，对软键盘的监听 - 这里是商品的输入
         */
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 /*判断是否是“GO”键*///2
                if(actionId == EditorInfo.IME_ACTION_GO){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    /**
                     * 开始搜索
                     */
                    startSearch();
                    return true;
                }
                //6
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    /**
                     * 开始搜索
                     */
                    startSearch();
                    return true;
                }
                //3
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    /**
                     * 开始搜索
                     */
                    startSearch();
                    return true;
                }
                //5
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    /**
                     * 开始搜索
                     */
                    startSearch();
                    return true;
                }
                return false;
            }
        });
        /**
         * 输入框完成，对软键盘的监听 - 这里是地址的输入
         */
        mEtLocationAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*///2
                if(actionId == EditorInfo.IME_ACTION_GO){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    /**
                     * 开始搜索
                     */
                    startSearch();
                    return true;
                }
                //6
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    /**
                     * 开始搜索
                     */
                    startSearch();
                    return true;
                }
                //3
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    /**
                     * 开始搜索
                     */
                    startSearch();
                    return true;
                }
                //5
                if(actionId == EditorInfo.IME_ACTION_NEXT){
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    /**
                     * 开始搜索
                     */
                    startSearch();
                    return true;
                }
                return false;
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (isFirst){
//            if (TextUtils.isEmpty(ProvinceId)){
//                getAllGoods(null,null,false,goodsId);
//            }else{
//                getAllGoods(ProvinceId,null,false,goodsId);
//            }
//        }
//    }

    /**
     * 开始搜索
     */
    private void startSearch(){
        //商品
        if (isGoodsAndBUsiness == true){
//            if (TextUtils.isEmpty(ProvinceId)){
//                getAllGoodsNoIsBag(null,null,etInput.getText().toString(),null);
//            }else{
//                //如果是上海等等个地区那么直接按市区搜索
//                if ("6ac80738-455d-4e3b-b537-36c03913f8bf".equals(ProvinceId) ||
//                        "6139de2a-e24c-453e-8fba-18a9f9b8740d".equals(ProvinceId) ||
//                        "9d3c53fa-b4d0-4739-b73f-040f66420f06".equals(ProvinceId) ||
//                        "48e6dedd-c623-4ebd-af43-fcc94b6331be".equals(ProvinceId) ||
//                        "3a1b8e57-95a0-4e93-8892-ca1b514a1a5e".equals(ProvinceId) ||
//                        "72f64e42-c590-4648-b2c5-3b58132560ac".equals(ProvinceId) ){
//                    getAllGoodsNoIsBag(ProvinceId,null,etInput.getText().toString(),null);
//                }else{
//                    getAllGoodsNoIsBag(ProvinceId,CityId,etInput.getText().toString(),null);
//                }
//            }

            //两个都为空
            if (TextUtils.isEmpty(mEtLocationAddress.getText()) && TextUtils.isEmpty(etInput.getText())){
                Toast.makeText(this, "请输入查询条件", Toast.LENGTH_SHORT).show();
                return;
            }

            //两个都不为空
            if (!TextUtils.isEmpty(mEtLocationAddress.getText()) && !TextUtils.isEmpty(etInput.getText())){
                //先根据输入地址展示输入结果数据
                getAllGoodsNoIsBag1(null,null,null,etInput.getText().toString(),mEtLocationAddress.getText().toString(),goodsId);
            }

            //商品为空 - 地址不为空
            if (TextUtils.isEmpty(etInput.getText()) && !TextUtils.isEmpty(mEtLocationAddress.getText())){
                getAllGoodsNoIsBag1(null,null,null,null,mEtLocationAddress.getText().toString(),goodsId);
            }

            //商品不为空 - 地址为空
            if (!TextUtils.isEmpty(etInput.getText()) && TextUtils.isEmpty(mEtLocationAddress.getText())){
                getAllGoodsNoIsBag1(null,null,null,etInput.getText().toString(),null,goodsId);
            }
        }else{//商家
//            if (TextUtils.isEmpty(ProvinceId)){
//                getBUsinessList(etInput.getText().toString(),null,null,null);
//            }else{
//                //如果是上海等等个地区那么
//                if ("6ac80738-455d-4e3b-b537-36c03913f8bf".equals(ProvinceId) ||
//                        "6139de2a-e24c-453e-8fba-18a9f9b8740d".equals(ProvinceId) ||
//                        "9d3c53fa-b4d0-4739-b73f-040f66420f06".equals(ProvinceId) ||
//                        "48e6dedd-c623-4ebd-af43-fcc94b6331be".equals(ProvinceId) ||
//                        "3a1b8e57-95a0-4e93-8892-ca1b514a1a5e".equals(ProvinceId) ||
//                        "72f64e42-c590-4648-b2c5-3b58132560ac".equals(ProvinceId) ){
//                    getBUsinessList(etInput.getText().toString(),ProvinceId,null,null);
//                }else{
//                    getBUsinessList(etInput.getText().toString(),ProvinceId,CityId,null);
//                }
//            }

            //两个都为空
            if (TextUtils.isEmpty(mEtLocationAddress.getText()) && TextUtils.isEmpty(etInput.getText())){
                Toast.makeText(this, "请输入查询条件", Toast.LENGTH_SHORT).show();
                return;
            }

            //两个都不为空
            if (!TextUtils.isEmpty(mEtLocationAddress.getText()) && !TextUtils.isEmpty(etInput.getText())){
                //先根据输入地址展示输入结果数据
                getBUsinessList1(null,null,null,etInput.getText().toString(),mEtLocationAddress.getText().toString());
            }

            //商品为空 - 地址不为空
            if (TextUtils.isEmpty(etInput.getText()) && !TextUtils.isEmpty(mEtLocationAddress.getText())){
                getBUsinessList1(null,null,null,null,mEtLocationAddress.getText().toString());
            }

            //商品不为空 - 地址为空
            if (!TextUtils.isEmpty(etInput.getText()) && TextUtils.isEmpty(mEtLocationAddress.getText())){
                getBUsinessList1(null,null,null,etInput.getText().toString(),null);
            }
        }
    }



    /**
     * data
     */
    private void initData() {
        //将数据添加到商品集合和产品集合
        for (int i = 0; i < chnagpingimage.length; i++){
            ProduceBean produceBean = new ProduceBean(chnagpingimage[i],chnagpingtitle[i],chnagpingParentId[i],chnagpingId[i],chnagpingSortNo[i],chnagpingIsBag[i]);
            changpinglist.add(produceBean);
        }
        //将数据添加到商品集合和产品集合
        for (int i = 0; i < shangpingimage.length; i++){
            ProduceBean produceBean = new ProduceBean(shangpingimage[i],shangpingtitle[i],shangpingParentId[i],shangpingId[i],shangpingSortNo[i],shangpingIsBag[i]);
            shangpinglist.add(produceBean);
        }
        //方格设置数据
        produceFragmentAdapter = new ProduceFragmentAdapter(this);
        serviceFragmentAdapter = new ServiceFragmentAdapter(this);
        //搜索到的商品用来展示数据
        searchBussGoodsAdapter = new SearchBussGoodsAdapter(this);
        //搜索到的商家进行显示
        searchBusinessAdapter = new SearchBusinessAdapter(this);
        //地址搜索结果
        searchAddressAdapter = new SearchAddressAdapter(this);

        searchActivitys = getIntent().getStringExtra("SearchActivitys");
        //是从列表搜索过来的
        if ("SearchResultActivity".equals(searchActivitys)){
            tv_chooses.setText("选择商品");
            cen_title.setText("促销商家搜索");
            etInput.setHint("商家店铺名称");
            //产品和服务按钮
            show_chanping.setVisibility(View.GONE);
            //方格
            recycle_view_fg.setVisibility(View.GONE);
            //暂无数据
            ll_defaultdata.setVisibility(View.GONE);
            ll_businessdata.setVisibility(View.GONE);
            ll_businessNoDada.setVisibility(View.VISIBLE);
            tv_liebiao.setVisibility(View.GONE);
            ll_paixu.setVisibility(View.GONE);
            recycle_view_shangping.setVisibility(View.GONE);
            isGoodsAndBUsiness = false;
            getBUsinessList(null,null,null,null);
        }else{
            //默认设置商品的数据适配器
            produceFragmentAdapter.setData(changpinglist);
            tv_cpgoods.setTextColor(Color.parseColor("#CE2020"));
            view_cpgoodsline.setVisibility(View.VISIBLE);
            view_fugoodsline.setVisibility(View.INVISIBLE);
        }
        //判断是个人还是商家
        isPeopleAndBusiness = PrefUtils.getString(this, "isPeopleAndBusiness", "");
        if ("2".equals(isPeopleAndBusiness)){
            tv_businessfabu.setVisibility(View.GONE);
        }else if ("3".equals(isPeopleAndBusiness)){
            tv_businessfabu.setVisibility(View.VISIBLE);
        }
        //设置适配器
        recycle_view_fg.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        recycle_view_fg.setLayoutManager(gridLayoutManager);
        recycle_view_fg.setAdapter(produceFragmentAdapter);
        //初始化recycleview - 商品和商家
        initRecycleView(recycle_view_shangping);
        initRecycleView(recycle_view_shangjia);
        initRecycleView(recycle_view_address);
    }

    /**
     *事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //返回键
            case R.id.back:
                finish();
                break;
            case R.id.rl_clear_address:
                mEtLocationAddress.setText(null);
                break;
            //城市选择
            case R.id.ll_citys:
                chooseCity();
                break;
                //商品和商家切换
            case R.id.ll_choosebusines_down:
                //商家或者是商品选择
            case R.id.ll_choosebusines:
                if (isGoodsAndBUsiness == true){
                    tv_chooses.setText("选择商品");
                    cen_title.setText("促销商家搜索");
                    etInput.setHint("商家店铺名称");
                    mTvSearchMsg1.setText("商品");
                    mTvSearchMsg2.setText("促销");
                    //产品和服务按钮
                    show_chanping.setVisibility(View.GONE);
                    //方格
                    recycle_view_fg.setVisibility(View.GONE);
                    //暂无数据
                    ll_defaultdata.setVisibility(View.GONE);
                    ll_businessdata.setVisibility(View.GONE);
                    ll_businessNoDada.setVisibility(View.VISIBLE);
                    tv_liebiao.setVisibility(View.GONE);
                    ll_paixu.setVisibility(View.GONE);
                    recycle_view_shangping.setVisibility(View.GONE);
                    isGoodsAndBUsiness = false;
                    //定位显示地址
//                    if (!TextUtils.isEmpty(BaseApplication.getInstance().userAddress)) {//保存分店地址实现定位详细地址
//                        String userAddress = BaseApplication.getInstance().userAddress;
//                        String substring = userAddress.substring(0, 2);
//                        Log.e("tag",userAddress);
//                        Log.e("tag",substring);
//                    }
                    getBUsinessList(null,null,null,null);//-------------------------------------------------------------------------------------------------------------------------------
//                    getLocationSearch("上海","上海");
                }else{
                    tv_chooses.setText("选择商家");
                    cen_title.setText("促销商品搜索");
                    etInput.setHint("商品名称/品牌/促销号");
                    mTvSearchMsg1.setText("商圈");
                    mTvSearchMsg2.setText("商家");
                    //产品和服务按钮
                    show_chanping.setVisibility(View.VISIBLE);
                    //方格
                    recycle_view_fg.setVisibility(View.VISIBLE);
                    //暂无数据
                    ll_defaultdata.setVisibility(View.GONE);
                    //暂无数据
                    ll_businessdata.setVisibility(View.GONE);
                    tv_liebiao.setVisibility(View.GONE);
                    ll_paixu.setVisibility(View.GONE);
                    ll_businessNoDada.setVisibility(View.GONE);
                    recycle_view_shangping.setVisibility(View.GONE);
                    recycle_view_shangjia.setVisibility(View.GONE);
                    isGoodsAndBUsiness = true;
                    //是从列表搜索过来的
                    if ("SearchResultActivity".equals(searchActivitys)) {
                        //默认设置商品的数据适配器
                        produceFragmentAdapter.setData(changpinglist);
                        tv_cpgoods.setTextColor(Color.parseColor("#CE2020"));
                        view_cpgoodsline.setVisibility(View.VISIBLE);
                        view_fugoodsline.setVisibility(View.INVISIBLE);
                    }
                }
                break;
            //清除
            case R.id.rl_clearinputs:
                //商品的清除
                if (isGoodsAndBUsiness == true){
                    etInput.setText(null);
                    show_chanping.setVisibility(View.VISIBLE);
                    recycle_view_fg.setVisibility(View.VISIBLE);
                    ll_defaultdata.setVisibility(View.GONE);
                    ll_businessdata.setVisibility(View.GONE);
                    recycle_view_shangping.setVisibility(View.GONE);
                    tv_liebiao.setVisibility(View.GONE);
                    recycle_view_shangjia.setVisibility(View.GONE);
                    ll_paixu.setVisibility(View.GONE);
                    ll_businessdata.setVisibility(View.GONE);
                }else{
                    etInput.setText(null);
                    ll_businessNoDada.setVisibility(View.VISIBLE);
                    ll_businessdata.setVisibility(View.GONE);
                    show_chanping.setVisibility(View.GONE);
                    recycle_view_fg.setVisibility(View.GONE);
                    ll_defaultdata.setVisibility(View.GONE);
                    recycle_view_shangping.setVisibility(View.GONE);
                    recycle_view_shangjia.setVisibility(View.GONE);
                    ll_paixu.setVisibility(View.GONE);
                }
                break;
            //打电话
            case R.id.ll_callphone:
                selectSearchPopupWindow = new SelectSearchPopupWindow(SearchActivitys.this, itemsCarOnClick);
                selectSearchPopupWindow.showAtLocation(ll_defaultdata);
//                searchNoGoods();
                break;
            //产品
            case R.id.ll_cpgoods:
                view_cpgoodsline.setVisibility(View.VISIBLE);
                view_fugoodsline.setVisibility(View.INVISIBLE);
                tv_cpgoods.setTextColor(Color.parseColor("#CE2020"));
                tv_fugoods.setTextColor(Color.parseColor("#000000"));
                //设置产品数据
                produceFragmentAdapter.setData(changpinglist);
                recycle_view_fg.setAdapter(produceFragmentAdapter);
                produceFragmentAdapter.notifyDataSetChanged();
                break;
            //服务商品
            case R.id.ll_fugoods:
                view_cpgoodsline.setVisibility(View.INVISIBLE);
                view_fugoodsline.setVisibility(View.VISIBLE  );
                tv_cpgoods.setTextColor(Color.parseColor("#000000"));
                tv_fugoods.setTextColor(Color.parseColor("#CE2020"));
                //设置服务数据
                serviceFragmentAdapter.setData(shangpinglist);
                recycle_view_fg.setAdapter(serviceFragmentAdapter);
                serviceFragmentAdapter.notifyDataSetChanged();
                break;
            //地图列表 - 也需要判断是否注册
            case R.id.tv_liebiao:
                if (com.ximai.savingsmore.save.modle.LoginUser.getInstance().isLogin()) {
                    if (listGoods.size() > 0){
                        BaseApplication.getInstance().listGoods = listGoods;
                        Intent intent = new Intent(this, SearchDataActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("listGoods", (Serializable) listGoods);
//                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(SearchActivitys.this, "温馨提示,您还没有登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SearchActivitys.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            //商家我要去促销
            case R.id.tv_businessfabu:
                if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("1")){
                    Onedialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("2")){
                    Twodialog();
                } else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("3")){
                    Intent intent = new Intent(this, AddGoodsAcitivyt.class);
                    startActivity(intent);
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("4")){
                    /**
                     * 打电话
                     */
                    Fourdialog();
                }
                break;
            //销量排序
            case R.id.xiaoliang:
                if (null != listGoods){
                    if (xiaoliangbool == true){
                        Collections.sort(listGoods, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                int i =Integer.parseInt(t1.CareCount) -  Integer.parseInt(goods.CareCount);
                                searchBussGoodsAdapter.notifyDataSetChanged();
                                return i;
                            }
                        });
                        xiaoliangbool = false;
                        tv_bottom1.setText("最少关注");
                        iv_xiaoliang.setBackgroundResource(R.mipmap.up3);

                    }else{
                        Collections.sort(listGoods, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                int i = Integer.parseInt(goods.CareCount) - Integer.parseInt(t1.CareCount);
                                searchBussGoodsAdapter.notifyDataSetChanged();
                                return i;
                            }
                        });
                        xiaoliangbool = true;
                        iv_xiaoliang.setBackgroundResource(R.mipmap.down3);
                        tv_bottom1.setText("最多关注");
                    }
                }
                cen_title.setText("同品对比-人气");
                break;
            //价格排序
            case R.id.jiage:
                if (null != listGoods){
                    if (jiagebool == true){
                        Collections.sort(listGoods, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                int i = new Double(goods.Price).compareTo(new Double(t1.Price));
                                searchBussGoodsAdapter.notifyDataSetChanged();
                                return i;
                            }
                        });
                        jiagebool = false;
                        iv_jiage.setBackgroundResource(R.mipmap.down3);
                        tv_bottom2.setText("最高价格");


                    }else{
                        Collections.sort(listGoods, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                int i = new Double(t1.Price).compareTo(new Double(goods.Price));
                                searchBussGoodsAdapter.notifyDataSetChanged();
                                return i;
                            }
                        });
                        jiagebool = true;
                        iv_jiage.setBackgroundResource(R.mipmap.up3);
                        tv_bottom2.setText("最低价格");

                    }
                }
                cen_title.setText("同品对比-价格");
                break;
            //日期排序
            case R.id.riqi:
                if (null != listGoods){
                    if (riqibool == true){
                        Collections.sort(listGoods, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String time1=goods.EndTimeName;
                                    String time2=t1.EndTimeName;
                                    Date parse1 = format.parse(time1);
                                    Date parse2 = format.parse(time2);
                                    int i =parse2.compareTo(parse1);
                                    searchBussGoodsAdapter.notifyDataSetChanged();
                                    return i;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return 0;
                            }
                        });
                        riqibool = false;
                        iv_riqi.setBackgroundResource(R.mipmap.up3);
                        tv_bottom3.setText("最快结束");

                    }else{
                        Collections.sort(listGoods, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                try {
                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                    String time1=goods.EndTimeName;
                                    String time2=t1.EndTimeName;
                                    Date parse1 = format.parse(time1);
                                    Date parse2 = format.parse(time2);
                                    int i =parse1.compareTo(parse2);
                                    searchBussGoodsAdapter.notifyDataSetChanged();
                                    return i;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return 0;
                            }
                        });
                        riqibool = true;
                        iv_riqi.setBackgroundResource(R.mipmap.down3);
                        tv_bottom3.setText("最晚结束");
                    }
                }
                cen_title.setText("同品对比-时间");

                break;
            //距离排序
            case R.id.juli:
                if (null != listGoods){
                    if (julibool == true){
                        Collections.sort(listGoods, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                int i = new Double(t1.Latitude).compareTo(new Double(goods.Latitude));
                                searchBussGoodsAdapter.notifyDataSetChanged();
                                return i;
                            }
                        });
                        julibool = false;
                        iv_juli.setBackgroundResource(R.mipmap.down3);
                        tv_bottom4.setText("最远距离");

                    }else{
                        Collections.sort(listGoods, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                int i = new Double(goods.Latitude).compareTo(new Double(t1.Latitude));
                                searchBussGoodsAdapter.notifyDataSetChanged();
                                return i;
                            }
                        });
                        julibool = true;
                        iv_juli.setBackgroundResource(R.mipmap.up3);
                        tv_bottom4.setText("最近距离");
                    }
                }
                cen_title.setText("同品对比-距离");

                break;
                default:
                    break;
        }
    }

    /**
     * 选择城市
     */
    private void chooseCity() {
        if (isGoodsAndBUsiness == true){
            //城市选择商品
            PopupWindowFromBottomUtil.showAddresss(SearchActivitys.this, LayoutInflater.from(SearchActivitys.this).inflate(R.layout.business_my_center_activity, null), allList, new PopupWindowFromBottomUtil.Listenre1_1() {
                @Override
                public void callBack(String Id1, String Id2, String Id3, String sheng,String city, String content, PopupWindow popupWindow) {
                    ProvinceId = Id1;
                    CityId = Id2;
                    AreaId = Id3;
                    if (sheng.equals("上海")){
                        tv_citys.setText("上海");
                    }else if (sheng.equals("天津")){
                        tv_citys.setText("天津");
                    }else if (sheng.equals("北京")){
                        tv_citys.setText("北京");
                    }else if (sheng.equals("重庆")){
                        tv_citys.setText("重庆");
                    }else if (sheng.equals("中国香港")){
                        tv_citys.setText("中国香港");
                    }else if (sheng.equals("中国澳门")){
                        tv_citys.setText("中国澳门");
                    }else{
                        tv_citys.setText(city);
                    }
                    popupWindow.dismiss();
                    //如果是上海等等个地区那么
                    if ("6ac80738-455d-4e3b-b537-36c03913f8bf".equals(ProvinceId) ||
                            "6139de2a-e24c-453e-8fba-18a9f9b8740d".equals(ProvinceId) ||
                            "9d3c53fa-b4d0-4739-b73f-040f66420f06".equals(ProvinceId) ||
                            "48e6dedd-c623-4ebd-af43-fcc94b6331be".equals(ProvinceId) ||
                            "3a1b8e57-95a0-4e93-8892-ca1b514a1a5e".equals(ProvinceId) ||
                            "72f64e42-c590-4648-b2c5-3b58132560ac".equals(ProvinceId) ){
                        if (TextUtils.isEmpty(etInput.getText())){
                            getAllGoodsNoIsBag(ProvinceId,null,null,null);
                        }else{
                            getAllGoodsNoIsBag(ProvinceId,null,etInput.getText().toString(),null);
                        }
                    }else{
                        if (TextUtils.isEmpty(etInput.getText())){
                            getAllGoodsNoIsBag(ProvinceId,CityId,null,null);
                        }else{
                            getAllGoodsNoIsBag(ProvinceId,CityId,etInput.getText().toString(),null);
                        }
                    }
                }
            });
        }else{
            //城市选择商家
            PopupWindowFromBottomUtil.showAddresss(SearchActivitys.this, LayoutInflater.from(SearchActivitys.this).inflate(R.layout.business_my_center_activity, null), allList, new PopupWindowFromBottomUtil.Listenre1_1() {
                @Override
                public void callBack(String Id1, String Id2, String Id3, String sheng,String city, String content, PopupWindow popupWindow) {
                    ProvinceId = Id1;
                    CityId = Id2;
                    AreaId = Id3;
                    if (sheng.equals("上海")){
                        tv_citys.setText("上海");
                    }else if (sheng.equals("天津")){
                        tv_citys.setText("天津");
                    }else if (sheng.equals("北京")){
                        tv_citys.setText("北京");
                    }else if (sheng.equals("重庆")){
                        tv_citys.setText("重庆");
                    }else if (sheng.equals("中国香港")){
                        tv_citys.setText("中国香港");
                    }else if (sheng.equals("中国澳门")){
                        tv_citys.setText("中国澳门");
                    }else{
                        tv_citys.setText(city);
                    }
                    popupWindow.dismiss();
                    //如果是上海等等个地区那么
                    if ("6ac80738-455d-4e3b-b537-36c03913f8bf".equals(ProvinceId) ||
                            "6139de2a-e24c-453e-8fba-18a9f9b8740d".equals(ProvinceId) ||
                            "9d3c53fa-b4d0-4739-b73f-040f66420f06".equals(ProvinceId) ||
                            "48e6dedd-c623-4ebd-af43-fcc94b6331be".equals(ProvinceId) ||
                            "3a1b8e57-95a0-4e93-8892-ca1b514a1a5e".equals(ProvinceId) ||
                            "72f64e42-c590-4648-b2c5-3b58132560ac".equals(ProvinceId) ){
                        if (TextUtils.isEmpty(etInput.getText())){
                            getBUsinessList(null,ProvinceId,null,null);
                        }else{
                            getBUsinessList(etInput.getText().toString(),ProvinceId,null,null);
                        }
                    }else{
                        if (TextUtils.isEmpty(etInput.getText())){
                            getBUsinessList(null,ProvinceId,CityId,null);
                        }else{
                            getBUsinessList(etInput.getText().toString(),ProvinceId,CityId,null);
                        }
                    }
                }
            });
        }
    }

    /**
     * 打电话
     */
    public void initCallPhone() {
        AndPermission.with(this)
                .requestCode(300)
                .permission(Manifest.permission.CALL_PHONE)
                .rationale(new RationaleListener() {

                    @Override
                    public void showRequestPermissionRationale(int i, final Rationale rationale) {
                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
                        //AndPermission.rationaleDialog(mActivity, rationale).show();
                        AlertDialog.newBuilder(SearchActivitys.this)
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
                        // Successfully.
                        if (requestCode == 300) {
                            intentToCall();
                        }
                    }
                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(SearchActivitys.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(SearchActivitys.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        // Failure.
                        if (requestCode == 200) {
                            Toast.makeText(SearchActivitys.this, "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .start();
    }
    /**
     * 打电话
     */
    private void intentToCall() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri data = Uri.parse("tel:" + "02158366991");
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        }catch (Exception e){
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }

    /**
     * 得到所有的商品 ----   添加是产品还是服务
     */
    private void getAllGoods(String Provice,String Keyword,boolean isBag,String FirstClassId) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(this, URLText.GET_GOODS,
                RequestParamsPool.getAllGoods(Provice,Keyword,isBag,FirstClassId), new MyAsyncHttpResponseHandler(SearchActivitys.this) {
                    @Override
                    public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                        String resule = new String(responseBody);
                        try {
                            listGoods.clear();//先将数据进行清除

                            goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                            listGoods.addAll(goodsList.MainData);
                            if (listGoods.size() == 0){
                                //2是个人
                                if ("2".equals(isPeopleAndBusiness)){
                                    ll_defaultdata.setVisibility(View.VISIBLE);
                                    recycle_view_fg.setVisibility(View.GONE);
                                    show_chanping.setVisibility(View.GONE);
                                    tv_liebiao.setVisibility(View.GONE);
                                    ll_businessdata.setVisibility(View.GONE);
                                    ll_businessNoDada.setVisibility(View.GONE);
                                    //3是商家
                                }else if ("3".equals(isPeopleAndBusiness)){
                                    ll_defaultdata.setVisibility(View.GONE);
                                    recycle_view_fg.setVisibility(View.GONE);
                                    show_chanping.setVisibility(View.GONE);
                                    tv_liebiao.setVisibility(View.GONE);
                                    ll_businessdata.setVisibility(View.VISIBLE);
                                    ll_businessNoDada.setVisibility(View.GONE);
                                }
                            }else{
                                //如果商品有数据
                                tv_liebiao.setVisibility(View.VISIBLE);
                                show_chanping.setVisibility(View.GONE);
                                ll_defaultdata.setVisibility(View.GONE);
                                recycle_view_fg.setVisibility(View.GONE);
                                ll_businessdata.setVisibility(View.GONE);
                                ll_businessNoDada.setVisibility(View.GONE);
                                recycle_view_shangping.setVisibility(View.VISIBLE);
                                ll_paixu.setVisibility(View.VISIBLE);
                                searchBussGoodsAdapter.setData(listGoods);
                                recycle_view_shangping.setAdapter(searchBussGoodsAdapter);
                                searchBussGoodsAdapter.notifyDataSetChanged();
                            }

                            if (null != builder){
                                builder.dismiss();
                            }
                        }catch (Exception e){
                            e.printStackTrace();

                            if (null != builder){
                                builder.dismiss();
                            }
                        }
                    }
                });
    }

    /**
     * 得到所有的商品
     */
    private void getAllGoodsNoIsBag(String Provice,String City,String Keyword,String FirstClassId) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(this, URLText.GET_GOODS,
                RequestParamsPool.getAllGoodsNoIsBag(Provice,City,Keyword,FirstClassId), new MyAsyncHttpResponseHandler(SearchActivitys.this) {
                    @Override
                    public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            listGoods.clear();//先将数据进行清除
                            goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                            if (null != goodsList.MainData){
                                listGoods.addAll(goodsList.MainData);
                            }
                            if (listGoods.size() == 0){
                                //2是个人
                                if ("2".equals(isPeopleAndBusiness)) {
                                    ll_defaultdata.setVisibility(View.VISIBLE);
                                    recycle_view_fg.setVisibility(View.GONE);
                                    show_chanping.setVisibility(View.GONE);
                                    tv_liebiao.setVisibility(View.GONE);
                                    ll_businessdata.setVisibility(View.GONE);
                                    ll_businessNoDada.setVisibility(View.GONE);
                                }else if ("3".equals(isPeopleAndBusiness)){
                                    ll_defaultdata.setVisibility(View.GONE);
                                    recycle_view_fg.setVisibility(View.GONE);
                                    show_chanping.setVisibility(View.GONE);
                                    tv_liebiao.setVisibility(View.GONE);
                                    ll_businessdata.setVisibility(View.VISIBLE);
                                    ll_businessNoDada.setVisibility(View.GONE);
                                }
                            }else{
                                //如果商品有数据
                                tv_liebiao.setVisibility(View.VISIBLE);
                                show_chanping.setVisibility(View.GONE);
                                ll_defaultdata.setVisibility(View.GONE);
                                recycle_view_fg.setVisibility(View.GONE);
                                recycle_view_shangjia.setVisibility(View.GONE);
                                ll_businessdata.setVisibility(View.GONE);
                                ll_businessNoDada.setVisibility(View.GONE);
                                recycle_view_shangping.setVisibility(View.VISIBLE);
                                ll_paixu.setVisibility(View.VISIBLE);
                                searchBussGoodsAdapter.setData(listGoods);
                                recycle_view_shangping.setAdapter(searchBussGoodsAdapter);
                                searchBussGoodsAdapter.notifyDataSetChanged();
                            }

                            if (null != builder){
                                builder.dismiss();
                            }
                        }catch (Exception e){
                            e.printStackTrace();

                            if (null != builder){
                                builder.dismiss();
                            }
                        }
                    }
                });
    }

    /**
     * 得到所有的商品 - -新添加参数
     */
    private void getAllGoodsNoIsBag1(String Provice,String City,String Area,String Keyword,String Address,String FirstClassId) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(this, URLText.GET_GOODS,
                RequestParamsPool.getAllGoodsNoIsBag1(Provice,City,Area,Keyword,Address,FirstClassId), new MyAsyncHttpResponseHandler(SearchActivitys.this) {
                    @Override
                    public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            listGoods.clear();//先将数据进行清除
                            goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                            if (null != goodsList.MainData){
                                listGoods.addAll(goodsList.MainData);
                            }
                            if (listGoods.size() == 0){
                                //2是个人
                                if ("2".equals(isPeopleAndBusiness)) {
                                    ll_defaultdata.setVisibility(View.VISIBLE);
                                    recycle_view_fg.setVisibility(View.GONE);
                                    show_chanping.setVisibility(View.GONE);
                                    tv_liebiao.setVisibility(View.GONE);
                                    ll_businessdata.setVisibility(View.GONE);
                                    ll_businessNoDada.setVisibility(View.GONE);
                                    recycle_view_address.setVisibility(View.GONE);
                                }else if ("3".equals(isPeopleAndBusiness)){
                                    ll_defaultdata.setVisibility(View.GONE);
                                    recycle_view_fg.setVisibility(View.GONE);
                                    show_chanping.setVisibility(View.GONE);
                                    tv_liebiao.setVisibility(View.GONE);
                                    ll_businessdata.setVisibility(View.VISIBLE);
                                    ll_businessNoDada.setVisibility(View.GONE);
                                    recycle_view_address.setVisibility(View.GONE);
                                }
                            }else{
                                //如果商品有数据
                                tv_liebiao.setVisibility(View.VISIBLE);
                                show_chanping.setVisibility(View.GONE);
                                ll_defaultdata.setVisibility(View.GONE);
                                recycle_view_fg.setVisibility(View.GONE);
                                recycle_view_shangjia.setVisibility(View.GONE);
                                recycle_view_address.setVisibility(View.GONE);
                                ll_businessdata.setVisibility(View.GONE);
                                ll_businessNoDada.setVisibility(View.GONE);
                                recycle_view_shangping.setVisibility(View.VISIBLE);
                                ll_paixu.setVisibility(View.VISIBLE);
                                searchBussGoodsAdapter.setData(listGoods);
                                recycle_view_shangping.setAdapter(searchBussGoodsAdapter);
                                searchBussGoodsAdapter.notifyDataSetChanged();
                            }

                            if (null != builder){
                                builder.dismiss();
                            }
                        }catch (Exception e){
                            e.printStackTrace();

                            if (null != builder){
                                builder.dismiss();
                            }
                        }
                    }
                });
    }


    /**
     * 获取商家列表信息
     * @param keyWord
     */
    private void getBUsinessList(String keyWord,String ProviceId, String CityId, String AreaId) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(SearchActivitys.this, URLText.BUSINESS_LIST, RequestParamsPool.businessList(keyWord,ProviceId,CityId,AreaId), new MyAsyncHttpResponseHandler(SearchActivitys.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    BusinessList businessList = GsonUtils.fromJson(new String(responseBody), BusinessList.class);
                    if (null != businessList.MainData){
                        listBusiness = businessList.MainData;
                    }
                    if (listBusiness.size() == 0){
                        ll_defaultdata.setVisibility(View.GONE);
                        ll_businessdata.setVisibility(View.GONE);
                        ll_businessNoDada.setVisibility(View.VISIBLE);
                        recycle_view_fg.setVisibility(View.GONE);
                        show_chanping.setVisibility(View.GONE);
                        recycle_view_shangjia.setVisibility(View.GONE);
                    }else{
                        show_chanping.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.GONE);
                        ll_businessdata.setVisibility(View.GONE);
                        recycle_view_fg.setVisibility(View.GONE);
                        recycle_view_shangping.setVisibility(View.GONE);
                        ll_paixu.setVisibility(View.GONE);
                        tv_liebiao.setVisibility(View.GONE);
                        ll_businessNoDada.setVisibility(View.GONE);
                        recycle_view_shangjia.setVisibility(View.VISIBLE);
                        searchBusinessAdapter.setData(listBusiness);
                        recycle_view_shangjia.setAdapter(searchBusinessAdapter);
                        searchBusinessAdapter.notifyDataSetChanged();
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
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
        WebRequestHelper.json_post(SearchActivitys.this, URLText.QUERYDICNODE, stringEntity, new MyAsyncHttpResponseHandler(SearchActivitys.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                ListBaseMessage listBaseMessage = GsonUtils.fromJson(new String(responseBody), ListBaseMessage.class);
                //城市信息
                allList = listBaseMessage.MainData;
            }
        });
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
                Intent intent = new Intent(SearchActivitys.this, FourStepRegisterActivity.class);
                startActivity(intent);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(SearchActivitys.this, "温馨提示", "请完成注册商家信息，再发布促销。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 未申请
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
        Dialog dialog = new XiMaiPopDialog(SearchActivitys.this, "温馨提示", "您提交的入驻申请正在审核，请耐心等待。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 未通过审核 - 进行一个弹框操作
     */
    public void Fourdialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call("02158366991");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(SearchActivitys.this, "温馨提示", "您的入驻申请未通过审核，请联系我们 021-58366991。", "确认", R.style.CustomDialog_1, callBack, 2);
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
     * 打电话
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
                        com.yanzhenjie.alertdialog.AlertDialog.newBuilder(SearchActivitys.this)
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
                        // Successfully.
                        if (requestCode == 300) {
                            intentToCall(phoneNumber);
                        }
                    }
                    @Override
                    public void onFailed(int requestCode, List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(SearchActivitys.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(SearchActivitys.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        // Failure.
                        if (requestCode == 200) {
                            Toast.makeText(SearchActivitys.this, "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
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
            if (ActivityCompat.checkSelfPermission(SearchActivitys.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }

    /**
     * 搜索暂无商品的时候 - 电话
     */
    public void searchNoGoods(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call("02138687133");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "“省又省”促销导购热线", "021-38687133", "拨打", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 叫车的弹框实现
     */
    private View.OnClickListener itemsCarOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            selectSearchPopupWindow.dismiss();
            switch (v.getId()) {
                //省又省促销导购热线
                case R.id.ll_car1:
                    searchNoGoods();
                    break;
                //与商家直接联系
                case R.id.ll_car2:
                    Intent leave = new Intent(SearchActivitys.this, LeaveMessageActivity.class);
                    startActivity(leave);
//                    tv_chooses.setText("选择商品");
//                    cen_title.setText("促销商家搜索");
//                    etInput.setHint("商家店铺名称");
//                    //产品和服务按钮
//                    show_chanping.setVisibility(View.GONE);
//                    //方格
//                    recycle_view_fg.setVisibility(View.GONE);
//                    //暂无数据
//                    ll_defaultdata.setVisibility(View.GONE);
//                    ll_businessdata.setVisibility(View.GONE);
//                    ll_businessNoDada.setVisibility(View.VISIBLE);
//                    tv_liebiao.setVisibility(View.GONE);
//                    ll_paixu.setVisibility(View.GONE);
//                    recycle_view_shangping.setVisibility(View.GONE);
//                    isGoodsAndBUsiness = false;
//                    getBUsinessList(null,null,null,null);

                    break;
                default:
                    break;
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
        builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        builder.show();
    }

    /**
     * 商品的地址搜索 - 显示出地址 - 现在不使用
     */
    private void getGoodAddressSearch(String address){
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(SearchActivitys.this, URLText.SEARCH_LOCATION_DATA, RequestParamsPool.getGoodAddressSearch(address), new MyAsyncHttpResponseHandler(SearchActivitys.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    SearchLocationModle searchLocationModle = GsonUtils.fromJson(new String(responseBody), SearchLocationModle.class);
                    List<SearchLocationModle.MainDataBean> mainData = searchLocationModle.getMainData();

                    if (mainData.size() > 0){
                        show_chanping.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.GONE);
                        ll_businessdata.setVisibility(View.GONE);
                        recycle_view_fg.setVisibility(View.GONE);
                        recycle_view_shangping.setVisibility(View.GONE);
                        ll_paixu.setVisibility(View.GONE);
                        tv_liebiao.setVisibility(View.GONE);
                        ll_businessNoDada.setVisibility(View.GONE);
                        recycle_view_shangjia.setVisibility(View.GONE);
                        recycle_view_address.setVisibility(View.VISIBLE);
                        searchAddressAdapter.setData(mainData);
                        recycle_view_address.setAdapter(searchAddressAdapter);
                    }else{
                        if ("2".equals(isPeopleAndBusiness)) {
                            ll_defaultdata.setVisibility(View.VISIBLE);
                            recycle_view_fg.setVisibility(View.GONE);
                            show_chanping.setVisibility(View.GONE);
                            tv_liebiao.setVisibility(View.GONE);
                            ll_businessdata.setVisibility(View.GONE);
                            ll_businessNoDada.setVisibility(View.GONE);
                            recycle_view_address.setVisibility(View.GONE);
                        }else if ("3".equals(isPeopleAndBusiness)){
                            ll_defaultdata.setVisibility(View.GONE);
                            recycle_view_fg.setVisibility(View.GONE);
                            show_chanping.setVisibility(View.GONE);
                            tv_liebiao.setVisibility(View.GONE);
                            ll_businessdata.setVisibility(View.VISIBLE);
                            ll_businessNoDada.setVisibility(View.GONE);
                            recycle_view_address.setVisibility(View.GONE);
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
                }
            }
        });
    }

    /**
     * 商家地址搜索 - 也是先显示地址
     */
    private void getBussinessLocationSearch(String keyWord) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(SearchActivitys.this, URLText.SEARCH_BUSINESS_LOCATION_DATA, RequestParamsPool.getBussinessLocationSearch(keyWord), new MyAsyncHttpResponseHandler(SearchActivitys.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    SearchLocationModle searchLocationModle = GsonUtils.fromJson(new String(responseBody), SearchLocationModle.class);
                    List<SearchLocationModle.MainDataBean> mainData = searchLocationModle.getMainData();

                    if (mainData.size() > 0){
                        show_chanping.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.GONE);
                        ll_businessdata.setVisibility(View.GONE);
                        recycle_view_fg.setVisibility(View.GONE);
                        recycle_view_shangping.setVisibility(View.GONE);
                        ll_paixu.setVisibility(View.GONE);
                        tv_liebiao.setVisibility(View.GONE);
                        ll_businessNoDada.setVisibility(View.GONE);
                        recycle_view_shangjia.setVisibility(View.GONE);
                        recycle_view_address.setVisibility(View.VISIBLE);
                        searchAddressAdapter.setData(mainData);
                        recycle_view_address.setAdapter(searchAddressAdapter);
                    }else{
                        if ("2".equals(isPeopleAndBusiness)) {
                            ll_defaultdata.setVisibility(View.VISIBLE);
                            recycle_view_fg.setVisibility(View.GONE);
                            show_chanping.setVisibility(View.GONE);
                            tv_liebiao.setVisibility(View.GONE);
                            ll_businessdata.setVisibility(View.GONE);
                            ll_businessNoDada.setVisibility(View.GONE);
                            recycle_view_address.setVisibility(View.GONE);
                        }else if ("3".equals(isPeopleAndBusiness)){
                            ll_defaultdata.setVisibility(View.GONE);
                            recycle_view_fg.setVisibility(View.GONE);
                            show_chanping.setVisibility(View.GONE);
                            tv_liebiao.setVisibility(View.GONE);
                            ll_businessdata.setVisibility(View.VISIBLE);
                            ll_businessNoDada.setVisibility(View.GONE);
                            recycle_view_address.setVisibility(View.GONE);
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
                }
            }
        });
    }

    /**
     * 获取商家列表信息 - 添加地址的
     */
    private void getBUsinessList1(String Provice,String City,String Area,String Keyword,String Address) {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(SearchActivitys.this, URLText.BUSINESS_LIST, RequestParamsPool.businessList1(Provice,City,Area,Keyword,Address), new MyAsyncHttpResponseHandler(SearchActivitys.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    BusinessList businessList = GsonUtils.fromJson(new String(responseBody), BusinessList.class);
                    if (null != businessList.MainData){
                        listBusiness = businessList.MainData;
                    }
                    if (listBusiness.size() == 0){
                        ll_defaultdata.setVisibility(View.GONE);
                        ll_businessdata.setVisibility(View.GONE);
                        ll_businessNoDada.setVisibility(View.VISIBLE);
                        recycle_view_fg.setVisibility(View.GONE);
                        show_chanping.setVisibility(View.GONE);
                        recycle_view_address.setVisibility(View.GONE);
                        recycle_view_shangjia.setVisibility(View.GONE);
                    }else{
                        show_chanping.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.GONE);
                        ll_businessdata.setVisibility(View.GONE);
                        recycle_view_fg.setVisibility(View.GONE);
                        recycle_view_shangping.setVisibility(View.GONE);
                        ll_paixu.setVisibility(View.GONE);
                        tv_liebiao.setVisibility(View.GONE);
                        ll_businessNoDada.setVisibility(View.GONE);
                        recycle_view_shangjia.setVisibility(View.VISIBLE);
                        searchBusinessAdapter.setData(listBusiness);
                        recycle_view_shangjia.setAdapter(searchBusinessAdapter);
                        recycle_view_address.setVisibility(View.GONE);
                        searchBusinessAdapter.notifyDataSetChanged();
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }
}