package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.ListBaseMessage;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.SelectSearchPopupWindow;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.ximai.savingsmore.save.view.imagepicker.Utils;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/11/21.
 * 列表结果
 */
public class SearchResultActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private List<Goods> list = new ArrayList<Goods>();
    private OnItemClickEventListener listener = null;
    private GoodsList goodsList = new GoodsList();
    private MyAdapter myAdapter;
    private LinearLayoutManager mLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page = 1;
    private int pageSize = 10;
    private Boolean isRefreshing = false;
    private int lastVisibleItem;
    private LinearLayout zhekou, jiage, date, juli;
    private ImageView zhekou_image, jiage_image, date_image, juli_image;
    private Boolean IsRebateDesc = false, IsPriceDesc = true, IsStartTimeDesc = true, IsDistanceDesc = true;
    private EditText search;
    private String isBag, state;
    private String classify1, classify2, brand, typeId, keyWord;
    String Longitude1;
    String Latitude1;
    String Area;
    private List<BaseMessage> qu_list = new ArrayList<BaseMessage>();
    private String AreaId;
    String Sheng;
    String ProvceId;
    String Shi;
    String CityId;
    boolean isSearch;
    private String IsPromotion = "true";
    private LinearLayout ll_defaultdata;
    private String isPeopleAndBusiness;
    private LinearLayout ll_businessdata;
    private ImageView iv_callphone;
    private TextView tv_businessfabu;
    private KyLoadingBuilder builder;
    private LinearLayout ll_callphone;
    private SelectSearchPopupWindow selectSearchPopupWindow;
    private String number;
    private TextView tv_bottom1,tv_bottom2,tv_bottom3,tv_bottom4;
    private int totalPage=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list_activity);
        initView();
        
        initData();
        
        initEvent();
    }

    /**
     * init - view
     */
    private void initView() {
        setLeftBackMenuVisibility(SearchResultActivity.this, "");
        recyclerView = (RecyclerView) findViewById(R.id.list);
        zhekou = (LinearLayout) findViewById(R.id.zhekou);
        jiage = (LinearLayout) findViewById(R.id.jiage);
        date = (LinearLayout) findViewById(R.id.riqi);
        juli = (LinearLayout) findViewById(R.id.juli);
        zhekou_image = (ImageView) findViewById(R.id.zhekou_direction);
        jiage_image = (ImageView) findViewById(R.id.jiage_direction);
        date_image = (ImageView) findViewById(R.id.riqi_direction);
        juli_image = (ImageView) findViewById(R.id.juli_direction);
        search = (EditText) findViewById(R.id.search);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.good_refresh);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);
        ll_businessdata = (LinearLayout) findViewById(R.id.ll_businessdata);
        iv_callphone = (ImageView) findViewById(R.id.iv_callphone);
        tv_businessfabu = (TextView) findViewById(R.id.tv_businessfabu);
        ll_callphone = (LinearLayout) findViewById(R.id.ll_callphone);
        mLayoutManager = new LinearLayoutManager(SearchResultActivity.this);
        tv_bottom1= (TextView) findViewById(R.id.tv_bottom1);
        tv_bottom2= (TextView) findViewById(R.id.tv_bottom2);
        tv_bottom3= (TextView) findViewById(R.id.tv_bottom3);
        tv_bottom4= (TextView) findViewById(R.id.tv_bottom4);
    }

    /**
     * init - data
     */
    private void initData() {
        //判断是个人还是商家
        isPeopleAndBusiness = PrefUtils.getString(this, "isPeopleAndBusiness", "");

        recyclerView.setOnScrollListener(new MyOnScrollListener());
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(myAdapter);

        String title = getIntent().getStringExtra("title");
        Sheng = getIntent().getStringExtra("sheng");
        Shi = getIntent().getStringExtra("shi");
        Area = getIntent().getStringExtra("qu");
        Longitude1 = getIntent().getStringExtra("long");
        Latitude1 = getIntent().getStringExtra("lat");
        //设置标题
        setCenterTitle(title);
        if (null == Longitude1) {
            Longitude1 = BaseApplication.getInstance().Longitude + "";
        }
        if (null == Latitude1) {
            Latitude1 = BaseApplication.getInstance().Latitude + "";
        }
        isSearch = getIntent().getBooleanExtra("search", false);
        if (null != getIntent()) {
            IsPromotion = getIntent().getStringExtra("state");
            if (null != IsPromotion) {
                if (IsPromotion.equals("all")) {
                    IsPromotion = null;
                }
            }
            isBag = getIntent().getStringExtra("isBag");
            keyWord = getIntent().getStringExtra("keyStore");
            classify1 = getIntent().getStringExtra("classify1");
            classify2 = getIntent().getStringExtra("classify2");
            state = getIntent().getStringExtra("state");
            brand = getIntent().getStringExtra("brand");
            typeId = getIntent().getStringExtra("typeId");
            ProvceId = getIntent().getStringExtra("shengId");
            CityId = getIntent().getStringExtra("shiId");
            AreaId = getIntent().getStringExtra("xianId");
        }
        if (null != Area) {
            queryDicNode(Area);
        } else {
            getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
        }
    }

    /**
     * init - event
     */
    private void initEvent() {
        zhekou.setOnClickListener(this);
        jiage.setOnClickListener(this);
        date.setOnClickListener(this);
        juli.setOnClickListener(this);
        iv_callphone.setOnClickListener(this);
        ll_callphone.setOnClickListener(this);
        tv_businessfabu.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {//软键盘进行搜索
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    page = 1;
                    list.clear();
                    myAdapter.notifyDataSetChanged();
                    keyWord = v.getText().toString();
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
                }
                return true;
            }
        });
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        list.clear();
        myAdapter.notifyDataSetChanged();
        if (!isRefreshing) {
            getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
        }
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zhekou:
                setCenterTitle("同品对比-人气");
                if (IsRebateDesc == null) {
                    IsRebateDesc = false;
                }
                page = 1;
                list.clear();
                myAdapter.notifyDataSetChanged();
                if (IsRebateDesc) {
                    IsRebateDesc = false;
                    IsPriceDesc = null;
                    IsStartTimeDesc = null;
                    IsDistanceDesc = null;
                    tv_bottom1.setText("最多关注");
                    zhekou_image.setBackgroundResource(R.mipmap.up3);
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
                } else {
                    IsRebateDesc = true;
                    IsPriceDesc = null;
                    IsStartTimeDesc = null;
                    IsDistanceDesc = null;
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
                    tv_bottom1.setText("最少关注");
                    zhekou_image.setBackgroundResource(R.mipmap.down3);
                }
                break;
            case R.id.jiage://价格
                setCenterTitle("同品对比-价格");

                if (IsPriceDesc == null) {
                    IsPriceDesc = true;
                }
                page = 1;
                list.clear();
                myAdapter.notifyDataSetChanged();
                if (IsPriceDesc) {
                    IsPriceDesc = false;
                    IsRebateDesc = null;
                    IsStartTimeDesc = null;
                    IsDistanceDesc = null;
                    jiage_image.setBackgroundResource(R.mipmap.down3);
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
                    tv_bottom2.setText("最高价格");

                } else {
                    IsPriceDesc = true;
                    IsRebateDesc = null;
                    IsStartTimeDesc = null;
                    IsDistanceDesc = null;
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
                    jiage_image.setBackgroundResource(R.mipmap.up3);
                    tv_bottom2.setText("最低价格");

                }
                break;
            case R.id.riqi://日期
                setCenterTitle("同品对比-时间");
                if (IsStartTimeDesc == null) {
                    IsStartTimeDesc = true;
                }
                page = 1;
                list.clear();
                myAdapter.notifyDataSetChanged();
                if (IsStartTimeDesc) {
                    IsStartTimeDesc = false;
                    IsPriceDesc = null;
                    IsRebateDesc = null;
                    IsDistanceDesc = null;
                    tv_bottom3.setText("最晚结束");
                    date_image.setBackgroundResource(R.mipmap.down3);
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);

                } else {
                    IsStartTimeDesc = true;
                    IsPriceDesc = null;
                    IsRebateDesc = null;
                    IsDistanceDesc = null;
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
                    tv_bottom3.setText("最快结束");
                    date_image.setBackgroundResource(R.mipmap.up3);

                }
                break;
            case R.id.juli://距离
                setCenterTitle("同品对比-距离");

                if (IsDistanceDesc == null) {
                    IsDistanceDesc = true;
                }
                page = 1;
                list.clear();
                myAdapter.notifyDataSetChanged();
                if (IsDistanceDesc) {
                    IsDistanceDesc = false;
                    IsStartTimeDesc = null;
                    IsPriceDesc = null;
                    IsRebateDesc = null;
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
                    juli_image.setBackgroundResource(R.mipmap.down3);
                    tv_bottom4.setText("最远距离");

                } else {
                    IsDistanceDesc = true;
                    IsStartTimeDesc = null;
                    IsPriceDesc = null;
                    IsRebateDesc = null;
                    getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
                    juli_image.setBackgroundResource(R.mipmap.up3);
                    tv_bottom4.setText("最近距离");

                }
                break;
            case R.id.iv_callphone://个人搜后没有数据
                searchNoGoods();
                break;
            case R.id.tv_businessfabu://商家搜索没有数据
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
            case R.id.ll_callphone://打电话
                selectSearchPopupWindow = new SelectSearchPopupWindow(SearchResultActivity.this, itemsCarOnClick);
                selectSearchPopupWindow.showAtLocation(ll_defaultdata);
                break;
                default:
                    break;
        }
    }

    /**
     * 叫车的弹框实现
     */
    private View.OnClickListener itemsCarOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            selectSearchPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.ll_car1://省又省促销导购热线
                    searchNoGoods();
                    break;
                case R.id.ll_car2://与商家直接联系
                    Intent intent = new Intent(SearchResultActivity.this, SearchActivitys.class);
                    intent.putExtra("SearchActivitys","SearchResultActivity");
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 数据适配器
     */
    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(SearchResultActivity.this).inflate(R.layout.searchmsg_iten, null);
            return new MyViewHodel(layout, new OnItemClickEventListener() {
                @Override
                public void onItemClick(int position) {
                    if (LoginUser.getInstance().isLogin()) {
                        Intent intent = new Intent(SearchResultActivity.this, GoodDetailsActivity.class);
                        intent.putExtra("id", list.get(position).Id);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SearchResultActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(SearchResultActivity.this, "温馨提示,请您完成登录", Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onItemLongClick(int position) {
                }
            });
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHodel viewHodel = (MyViewHodel) holder;
            Glide.with(SearchResultActivity.this).load(URLText.img_url + list.get(position).Image).into(viewHodel.image);
//            viewHodel.tv_favourable.setText(list.get(position).Preferential);
            if (list.get(position).Preferential.length() > 5){
                viewHodel.tv_favourable.setText(list.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.tv_favourable.setText(list.get(position).Preferential);
            }
            if(!TextUtils.isEmpty(list.get(position).Currency)){
                viewHodel.tv_price.setText(list.get(position).Currency+" "+list.get(position).Price);
                viewHodel.tv_agoprice.setText(list.get(position).Currency+" "+list.get(position).OriginalPrice);
            }
            viewHodel.name.setText(list.get(position).Name);
            viewHodel.tv_assesss.setText(list.get(position).FavouriteCount);

            if (null == list.get(position).CommentCount ){
                viewHodel.tv_comments.setText("0");//评论
            }else{
                viewHodel.tv_comments.setText(list.get(position).CommentCount);//评论
            }
            viewHodel.tv_sharks.setText(list.get(position).SharedCount);
            if (null == list.get(position).HitCount ){
                viewHodel.tv_lookthroughs.setText("0");//浏览
            }else{
                viewHodel.tv_lookthroughs.setText(list.get(position).HitCount);//浏览
            }
//            viewHodel.tv_business.setText(list.get(position).StoreName);
            if (null != list.get(position).ChainStores){//---------------------------------------------------------进行分店的实现
                if (list.get(position).ChainStores.size() > 0){
                    String name = list.get(position).ChainStores.get(0).Name;
                    viewHodel.tv_business.setText(name);
                }else{
                    if (null != list.get(position).StoreName) {//商家店铺名称
                        viewHodel.tv_business.setText(list.get(position).StoreName);
                    }
                }
            }else{
                if (null != list.get(position).StoreName) {//商家店铺名称
                    viewHodel.tv_business.setText(list.get(position).StoreName);
                }
            }
//            viewHodel.tv_address.setText(list.get(position).Province+list.get(position).Address);
            if (null !=list.get(position).Province && null != list.get(position).City && null != list.get(position).Address){
                if (TextUtils.isEmpty(list.get(position).Country)){
                    viewHodel.tv_address.setText("中国 · "+list.get(position).Province+list.get(position).City+list.get(position).Address);
                }else{
                    viewHodel.tv_address.setText(list.get(position).Country+" · "+list.get(position).Province+list.get(position).City+list.get(position).Address);
                }
            }
            if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)){
                viewHodel.statr_time.setText(list.get(position).StartTimeName.substring(0,10));
            }
            if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)){
                viewHodel.end_time.setText(list.get(position).EndTimeName.substring(0,10));
                try {
                    long time= Utils.dateToStamp(list.get(position).EndTimeName)-System.currentTimeMillis();
                    if (time<3*24*60*60*1000&&time>0){
                        Glide.with(SearchResultActivity.this).load(R.mipmap.end_time_git).into(viewHodel.iv_endtime);
                        viewHodel.iv_endtime.setVisibility(View.VISIBLE);
                    }else {
                        viewHodel.iv_endtime.setVisibility(View.GONE);

                    }
                }catch (Exception e){

                }
            }
            if (TextUtils.isEmpty(list.get(position).SaleCount)||"0".equals(list.get(position).SaleCount)){
                viewHodel.tv_volume.setVisibility(View.GONE);
            }else {
                viewHodel.tv_volume.setText("销 " + list.get(position).SaleCount);//销量
                viewHodel.tv_volume.setVisibility(View.GONE);
            }
            viewHodel.tv_care.setText("关注"+list.get(position).CareCount);
            viewHodel.tv_store_count.setText("到店人次"+list.get(position).StoreCount);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    /**
     * 提供接口
     */
    public interface OnItemClickEventListener {
        void onItemClick(int position);
        void onItemLongClick(int position);
    }
    public void setOnItemClickEventListener(OnItemClickEventListener listener) {
        this.listener = listener;
    }

    static class MyViewHodel extends RecyclerView.ViewHolder {
        private TextView tv_assesss;
        private TextView tv_comments;
        private TextView tv_sharks;
        private TextView tv_business;
        private TextView tv_address;
        private TextView statr_time;
        private TextView end_time;
        private TextView tv_volume;
        private ImageView image;

        private TextView name;
        private TextView tv_price;
        private TextView tv_agoprice;
        private TextView tv_favourable;
        private TextView tv_lookthroughs;
        public TextView tv_store_count;
        public TextView tv_care;
        public ImageView iv_endtime;

        public MyViewHodel(View itemView, final OnItemClickEventListener lis) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            tv_assesss = (TextView) itemView.findViewById(R.id.tv_assesss);//收藏
            tv_comments = (TextView) itemView.findViewById(R.id.tv_comments);//评论
            tv_sharks = (TextView) itemView.findViewById(R.id.tv_sharks);//分享
            tv_business = (TextView) itemView.findViewById(R.id.tv_business);//公司
            tv_address = (TextView) itemView.findViewById(R.id.tv_address);//地址和
            statr_time = (TextView) itemView.findViewById(R.id.statr_time);//开水时间
            end_time = (TextView) itemView.findViewById(R.id.end_time);//结束时间
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);//价格
            tv_agoprice = (TextView) itemView.findViewById(R.id.tv_agoprice);//以前价格
            tv_favourable = (TextView) itemView.findViewById(R.id.tv_favourable);//优惠
            tv_volume = (TextView) itemView.findViewById(R.id.tv_volume);//销量
            tv_lookthroughs = (TextView) itemView.findViewById(R.id.tv_lookthroughs);//浏览
            tv_agoprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tv_store_count= (TextView) itemView.findViewById(R.id.tv_store_count);
            tv_care= (TextView) itemView.findViewById(R.id.tv_care);
            iv_endtime= (ImageView) itemView.findViewById(R.id.iv_endtime);


            if (null != lis) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lis.onItemClick(getAdapterPosition());
                    }
                });
            }
        }
    }

//    /**
//     * 得到所有的商品
//     */
//    private void getAllGoods(String Provice, String City, String AreaId, String Longitude, String Latitude, String isBag, String isState, String class1, String class2, String brand, String type, int pageNo, int pageSize, Boolean IsRebateDesc, Boolean IsPriceDesc, Boolean IsStartTimeDesc, Boolean IsDistanceDesc, String keyword) {
//        isRefreshing = true;
//        swipeRefreshLayout.setRefreshing(isRefreshing);
//
//        showLoading(SearchResultActivity.this,"正在加载");
//
////        if (null != number ){
////            pageSize = Integer.parseInt(number);
////        }else{
////            pageSize = 30;
////        }
//
//        WebRequestHelper.json_post(this, URLText.GET_GOODS, RequestParamsPool.getAllGoods("true", null, null, null, Longitude, Latitude, 1, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc, "", "", "", "", "", "", ""), new MyAsyncHttpResponseHandler(this) {
//            @Override
//            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
//                    if (goodsList.IsSuccess) {
//                        if (null != goodsList.MainData) {
//                            if (page == 1){
//                                list.clear();
//                            }
//                            list.addAll(goodsList.MainData);
//
////                            if (Integer.parseInt(number) < goodsList.MainData.size()){
////                                for (int i = 0; i < Integer.parseInt(number); i++) {//最多展示的数据
////                                    list.add(goodsList.MainData.get(i));
////                                }
////                            }else{
////                                for (int i = 0; i < goodsList.MainData.size(); i++) {//最多展示的数据
////                                    list.add(goodsList.MainData.get(i));
////                                }
////                            }
//
//                            if (list.size() == 0){
//                                if ("2".equals(isPeopleAndBusiness)) {//2是个人
//                                    ll_defaultdata.setVisibility(View.VISIBLE);
//                                    recyclerView.setVisibility(View.GONE);
//                                    ll_businessdata.setVisibility(View.GONE);
//                                }else{
//                                    ll_defaultdata.setVisibility(View.GONE);
//                                    recyclerView.setVisibility(View.GONE);
//                                    ll_businessdata.setVisibility(View.VISIBLE);
//                                }
//                            }else{
//                                ll_defaultdata.setVisibility(View.GONE);
//                                ll_businessdata.setVisibility(View.GONE);
//                                recyclerView.setVisibility(View.VISIBLE);
//                            }
////                        if (list.size() == 0 && isSearch) {
////                            ll_defaultdata.setVisibility(View.VISIBLE);
////                            recyclerView.setVisibility(View.GONE);
////                            Toast.makeText(SearchResultActivity.this, "请重新搜索", Toast.LENGTH_SHORT).show();
////                            finish();
////                        }
//                            myAdapter.notifyDataSetChanged();
//                        }
//                    }
//                    isRefreshing = false;
//                    swipeRefreshLayout.setRefreshing(isRefreshing);
//
//                    if (null != builder){
//                        builder.dismiss();
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//
//                    if (null != builder){
//                        builder.dismiss();
//                    }
//                }
//            }
//        });
//    }

    /**
     * 得到所有的商品 - 获取所有的 - 和地图外面的不一样
     */
    private void getAllGoods(String Provice, String City, String AreaId, String Longitude, String Latitude, String isBag, String isState, String class1, String class2, String brand, String type, int pageNo, int pageSize, Boolean IsRebateDesc, Boolean IsPriceDesc, Boolean IsStartTimeDesc, Boolean IsDistanceDesc,Boolean IsCareCountDesc,String keyword) {
        if (pageNo>totalPage){
            return;
        }
        isRefreshing = true;
        swipeRefreshLayout.setRefreshing(isRefreshing);

      //  showLoading(SearchResultActivity.this,"正在加载");

        if (null != number ){
            pageSize = Integer.parseInt(number);
        }else{
            pageSize = 30;
        }

        WebRequestHelper.json_post(SearchResultActivity.this, URLText.GET_GOODS, RequestParamsPool.getAllGoods(IsPromotion, Provice, City, AreaId, Longitude1, Latitude1, pageNo, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc, IsCareCountDesc,keyword, isBag, isState, class1, class2, brand, type), new MyAsyncHttpResponseHandler(SearchResultActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                    totalPage=goodsList.TotalPageCount;
                    if (goodsList.IsSuccess) {
                        if (null != goodsList.MainData) {
//                            list.clear();
                            list.addAll(goodsList.MainData);
                            if (list.size() == 0){
                                if ("2".equals(isPeopleAndBusiness)) {//2是个人
                                    ll_defaultdata.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                    ll_businessdata.setVisibility(View.GONE);
                                }else{
                                    ll_defaultdata.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.GONE);
                                    ll_businessdata.setVisibility(View.VISIBLE);
                                }
                            }else{
                                ll_defaultdata.setVisibility(View.GONE);
                                ll_businessdata.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
//                        if (list.size() == 0 && isSearch) {
//                            ll_defaultdata.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//                            Toast.makeText(SearchResultActivity.this, "请重新搜索", Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                    isRefreshing = false;
                    swipeRefreshLayout.setRefreshing(isRefreshing);

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
     * 分页加载
     */
    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            swipeRefreshLayout.setEnabled(((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition() == 0);
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == myAdapter.getItemCount()&&myAdapter.getItemCount()>4) {
                page++;
                getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
            }
        }
    }

    /**
     * 查询基础字典
     * @param Area
     */
    private void queryDicNode(final String Area) {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(SearchResultActivity.this, URLText.QUERYDICNODE, stringEntity, new MyAsyncHttpResponseHandler(SearchResultActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                ListBaseMessage listBaseMessage = GsonUtils.fromJson(new String(responseBody), ListBaseMessage.class);
                qu_list = listBaseMessage.MainData;
                for (int i = 0; i < qu_list.size(); i++) {
                    if (Sheng.equals(Shi)) {
                        if (null != qu_list.get(i).Name && null != qu_list.get(i).ParentId && null != qu_list.get(i).Id && Sheng.contains(qu_list.get(i).Name)) {
                            ProvceId = qu_list.get(i).Id;
                        }
                        if (null != qu_list.get(i).Name && null != qu_list.get(i).ParentId && null != qu_list.get(i).Id && qu_list.get(i).Name.equals(Area)) {
                            CityId = qu_list.get(i).Id;
                        }
                    } else {
                        if (null != qu_list.get(i).Name && null != qu_list.get(i).ParentId && null != qu_list.get(i).Id && Sheng.contains(qu_list.get(i).Name)) {
                            ProvceId = qu_list.get(i).Id;
                        }
                        if (null != qu_list.get(i).Name && null != qu_list.get(i).ParentId && null != qu_list.get(i).Id && Shi.contains(qu_list.get(i).Name)) {
                            CityId = qu_list.get(i).Id;
                        }
                        if (null != qu_list.get(i).Name && null != qu_list.get(i).ParentId && null != qu_list.get(i).Id && qu_list.get(i).Name.equals(Area)) {
                            AreaId = qu_list.get(i).Id;
                        }
                    }
                }
                getAllGoods(ProvceId, CityId, AreaId, Longitude1, Latitude1, isBag, state, classify1, classify2, brand, typeId, page, pageSize, IsRebateDesc, IsPriceDesc, IsStartTimeDesc, IsDistanceDesc,IsRebateDesc, keyWord);
            }
        });
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
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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
                Intent intent = new Intent(SearchResultActivity.this, FourStepRegisterActivity.class);
                startActivity(intent);
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(SearchResultActivity.this, "温馨提示", "请完成注册商家信息，再发布促销。", "确认", R.style.CustomDialog_1, callBack, 2);
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
        Dialog dialog = new XiMaiPopDialog(SearchResultActivity.this, "温馨提示", "您提交的入驻申请正在审核，请耐心等待。", "确认", R.style.CustomDialog_1, callBack, 2);
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
        Dialog dialog = new XiMaiPopDialog(SearchResultActivity.this, "温馨提示", "您的入驻申请未通过审核，请联系我们 021-58366991。", "确认", R.style.CustomDialog_1, callBack, 2);
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
        builder.setOutsideTouchable(false);
        //builder.setBackTouchable(true);
        builder.show();
    }
}