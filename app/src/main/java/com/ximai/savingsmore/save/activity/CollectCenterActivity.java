package com.ximai.savingsmore.save.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.cache.MyImageLoader;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.Business;
import com.ximai.savingsmore.save.modle.BusinessList;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.view.GlideRoundTransform;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by caojian on 16/11/29.
 */
public class CollectCenterActivity extends BaseActivity implements View.OnClickListener {
    private List<Goods> listGoods = new ArrayList<Goods>();
    private List<Business> listBusiness = new ArrayList<Business>();
    private GoodsAdapter goodsAdapter;
    private BusinessAdapter businessAdapter;
    private ListView listView1, listView2;
    private View line1, line2;
    private TextView goods, business;
    private TextView editor;
    private boolean isGoods = true, isBusiness = false, isEditor = false;
    private LinearLayout all_select;
    private ImageView all_select_image;
    private Button delect;
    private boolean isAllSelect = false;
    private LinearLayout ll_defaultdata;
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_center_activity);

        initView();

        initData();

        initEvent();
    }

    /**
     * init - view
     */
    private void initView() {
        setCenterTitle("收藏中心");
        setLeftBackMenuVisibility(CollectCenterActivity.this, "");

        listView1 = (ListView) findViewById(R.id.list1);
        listView2 = (ListView) findViewById(R.id.list2);
        goods = (TextView) findViewById(R.id.is_bag);
        all_select = (LinearLayout) findViewById(R.id.all_select);
        all_select_image = (ImageView) findViewById(R.id.all_select_image);
        delect = (Button) findViewById(R.id.delect);
        business = (TextView) findViewById(R.id.no_bag);
        line1 = findViewById(R.id.introduce);
        line2 = findViewById(R.id.cuxiao_goog);
        editor = (TextView) findViewById(R.id.editor);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);//默认数据
    }

    /**
     * init - data
     */
    private void initData() {
        /**
         * 注册一个监听
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.LOADING_COLLECTION);


        goodsAdapter = new GoodsAdapter();
        businessAdapter = new BusinessAdapter();

        /**
         * 获取店铺收藏
         */
        getCollectBusiness();

        /**
         *获取商品收藏
         */
        getCollectGoods();
    }

    /**
     * init - event
     */
    private void initEvent() {
        goods.setOnClickListener(this);
        business.setOnClickListener(this);
        listView1.setAdapter(goodsAdapter);
        listView2.setAdapter(businessAdapter);
        editor.setOnClickListener(this);
        all_select.setOnClickListener(this);
        all_select_image.setOnClickListener(this);
        delect.setOnClickListener(this);

        /**
         *商品的点击事件
         */
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CollectCenterActivity.this, GoodDetailsActivity.class);
                intent.putExtra("id", listGoods.get(position).ProductId);
                startActivity(intent);
            }
        });
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.is_bag) {
            if (isEditor == false) {//左边
                isGoods = true;
                isBusiness = false;
                line1.setVisibility(View.VISIBLE);
                line2.setVisibility(View.INVISIBLE);
                listView1.setVisibility(View.VISIBLE);
                listView2.setVisibility(View.GONE);

                if (listGoods.size() == 0){
                    ll_defaultdata.setVisibility(View.VISIBLE);
                }else{
                    ll_defaultdata.setVisibility(View.GONE);
                }
            }
        }
        if (v.getId() == R.id.no_bag) {//右边
            if (isEditor == false) {
                isBusiness = true;
                isGoods = false;
                listView2.setVisibility(View.VISIBLE);
                listView1.setVisibility(View.GONE);
                line1.setVisibility(View.INVISIBLE);
                line2.setVisibility(View.VISIBLE);

                if (listBusiness.size() == 0){
                    ll_defaultdata.setVisibility(View.VISIBLE);
                }else{
                    ll_defaultdata.setVisibility(View.GONE);
                }
            }
        }



        if (v.getId() == R.id.editor) {
            if (!isEditor) {
                all_select.setVisibility(View.VISIBLE);
                editor.setText("完成");
                delect.setVisibility(View.VISIBLE);
                isEditor = true;
                if (isGoods && listGoods != null && listGoods.size() != 0) {
                    for (int i = 0; i < listGoods.size(); i++) {
                        listGoods.get(i).IsEditor = true;
                    }
                    listView1.setAdapter(goodsAdapter);
                } else if (isBusiness && listBusiness != null && listBusiness.size() != 0) {
                    for (int i = 0; i < listBusiness.size(); i++) {
                        listBusiness.get(i).IsEditor = true;
                    }
                    listView2.setAdapter(businessAdapter);
                }
            } else {
                isAllSelect = false;
                if (isGoods) {
                    for (int i = 0; i < listGoods.size(); i++) {
                        listGoods.get(i).IsSelect = false;
                    }
                }
                if (isBusiness) {
                    for (int i = 0; i < listBusiness.size(); i++) {
                        listBusiness.get(i).IsSelect = false;
                    }
                }
                delect.setVisibility(View.GONE);
                all_select.setVisibility(View.GONE);
                isEditor = false;
                editor.setText("编辑");
                if (isGoods && listGoods != null && listGoods.size() != 0) {
                    for (int i = 0; i < listGoods.size(); i++) {
                        listGoods.get(i).IsEditor = false;
                    }
                    listView1.setAdapter(goodsAdapter);
                } else if (isBusiness && listBusiness != null && listBusiness.size() != 0) {
                    for (int i = 0; i < listBusiness.size(); i++) {
                        listBusiness.get(i).IsEditor = false;
                    }
                    listView2.setAdapter(businessAdapter);
                }
            }
            if (!isAllSelect) {
                all_select_image.setBackgroundResource(R.mipmap.noselect);
            } else {
                all_select_image.setBackgroundResource(R.mipmap.select);
            }
        }
        if (v.getId() == R.id.all_select_image) {
            if (isGoods) {
                int a = 0;
                for (int i = 0; i < listGoods.size(); i++) {
                    if (listGoods.get(i).IsSelect) {
                        a++;
                    }
                }
                if (a == listGoods.size()) {
                    isAllSelect = true;
                } else {
                    isAllSelect = false;
                }
            } else if (isBusiness) {
                int a = 0;
                for (int i = 0; i < listBusiness.size(); i++) {
                    if (listBusiness.get(i).IsSelect) {
                        a++;
                    }
                }
                if (a == listBusiness.size()) {
                    isAllSelect = true;
                } else {
                    isAllSelect = false;
                }
            }
            if (isAllSelect) {
                all_select_image.setBackgroundResource(R.mipmap.noselect);
            } else {
                all_select_image.setBackgroundResource(R.mipmap.select);
            }
            if (isGoods) {
                if (isAllSelect) {
                    for (int i = 0; i < listGoods.size(); i++) {
                        listGoods.get(i).IsSelect = false;
                    }
                    goodsAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < listGoods.size(); i++) {
                        listGoods.get(i).IsSelect = true;
                    }
                    goodsAdapter.notifyDataSetChanged();
                }
            } else if (isBusiness) {
                if (isAllSelect) {
                    for (int i = 0; i < listBusiness.size(); i++) {
                        listBusiness.get(i).IsSelect = false;
                    }
                    businessAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < listBusiness.size(); i++) {
                        listBusiness.get(i).IsSelect = true;
                    }
                    businessAdapter.notifyDataSetChanged();
                }
            }
        }
        if (v.getId() == R.id.delect) {
            if (isGoods) {
                List<String> list = new ArrayList<>();
                List<Goods> goodsList1 = new ArrayList<Goods>();
                for (int i = 0; i < listGoods.size(); i++) {
                    if (listGoods.get(i).IsSelect) {
                        list.add(listGoods.get(i).Id);
                    } else {
                        goodsList1.add(listGoods.get(i));
                    }
                }
                cancelCollect(list);
                listGoods = goodsList1;
            }
            if (isBusiness) {
                List<String> list = new ArrayList<>();
                List<Business> goodsList1 = new ArrayList<Business>();
                for (int i = 0; i < listBusiness.size(); i++) {
                    if (listBusiness.get(i).IsSelect) {
                        list.add(listBusiness.get(i).Id);
                    } else {
                        goodsList1.add(listBusiness.get(i));
                    }
                }
                cancelBusiness(list);
                listBusiness = goodsList1;
            }
        }
    }

    /**
     * 商品的适配器
     */
    private class GoodsAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listGoods.size();
        }
        @Override
        public Object getItem(int position) {
            return listGoods.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            GoodsViewHodel viewHodel = null;
            if (null == convertView) {
                viewHodel = new GoodsViewHodel();
                convertView = LayoutInflater.from(CollectCenterActivity.this).inflate(R.layout.good_editor_item, null);
                viewHodel.image = (ImageView) convertView.findViewById(R.id.image);
                viewHodel.name = (TextView) convertView.findViewById(R.id.name);
                viewHodel.tv_assesss = (TextView) convertView.findViewById(R.id.tv_assesss);//收藏
                viewHodel.tv_comments = (TextView) convertView.findViewById(R.id.tv_comments);//评论
                viewHodel.tv_sharks = (TextView) convertView.findViewById(R.id.tv_sharks);//分享
                viewHodel.tv_business = (TextView) convertView.findViewById(R.id.tv_business);//公司
                viewHodel.tv_address = (TextView) convertView.findViewById(R.id.tv_address);//地址和
                viewHodel.statr_time = (TextView) convertView.findViewById(R.id.statr_time);//开水时间
                viewHodel.end_time = (TextView) convertView.findViewById(R.id.end_time);//结束时间
                viewHodel.tv_price = (TextView) convertView.findViewById(R.id.tv_price);//价格
                viewHodel.tv_agoprice = (TextView) convertView.findViewById(R.id.tv_agoprice);//以前价格
                viewHodel.tv_favourable = (TextView) convertView.findViewById(R.id.tv_favourable);//优惠
                viewHodel.tv_volume = (TextView) convertView.findViewById(R.id.tv_volume);//销量
                viewHodel.tv_lookthroughs = (TextView) convertView.findViewById(R.id.tv_lookthroughs);//浏览
                viewHodel.tv_agoprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                viewHodel.isEditor = (ImageView) convertView.findViewById(R.id.isSelect);
                viewHodel.select = (LinearLayout) convertView.findViewById(R.id.select);
                convertView.setTag(viewHodel);
            }
            viewHodel = (GoodsViewHodel) convertView.getTag();
            viewHodel.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < listGoods.size(); i++) {
                        if (i == position) {
                            if (listGoods.get(i).IsSelect) {
                                listGoods.get(i).IsSelect = false;
                            } else {
                                listGoods.get(i).IsSelect = true;
                            }
                        }
                    }
                    goodsAdapter.notifyDataSetChanged();
                }
            });
            Glide.with(CollectCenterActivity.this).load(URLText.img_url + listGoods.get(position).Image).into(viewHodel.image);
            if (listGoods.get(position).IsEditor) {
                viewHodel.select.setVisibility(View.VISIBLE);
            } else {
                viewHodel.select.setVisibility(View.GONE);
            }
            if (listGoods.get(position).IsSelect) {
                viewHodel.isEditor.setBackgroundResource(R.mipmap.select);
            }
            if (null == listGoods.get(position).CommentCount ){
                viewHodel.tv_comments.setText("0");//评论
            }else{
                viewHodel.tv_comments.setText(listGoods.get(position).CommentCount);//评论
            }

            if (!listGoods.get(position).IsSelect) {
                viewHodel.isEditor.setBackgroundResource(R.mipmap.noselect);
            }
            if (null != listGoods.get(position).Name) {
                viewHodel.name.setText(listGoods.get(position).Name);
            }


//            if (null != listGoods.get(position).StoreName) {
//                viewHodel.tv_business.setText(listGoods.get(position).StoreName);
//            }
            if (null != listGoods.get(position).ChainStores){//---------------------------------------------------------进行分店的实现
                if (listGoods.get(position).ChainStores.size() > 0){
                    String name = listGoods.get(position).ChainStores.get(0).Name;
                    viewHodel.tv_business.setText(name);
                }else{
                    if (null != listGoods.get(position).StoreName) {//商家店铺名称
                        viewHodel.tv_business.setText(listGoods.get(position).StoreName);
                    }
                }
            }else{
                if (null != listGoods.get(position).StoreName) {//商家店铺名称
                    viewHodel.tv_business.setText(listGoods.get(position).StoreName);
                }
            }


            if (null == listGoods.get(position).FavouriteCount) {
                viewHodel.tv_assesss.setText("0");
            }else{
                viewHodel.tv_assesss.setText(listGoods.get(position).FavouriteCount);
            }

            if (null == listGoods.get(position).SaleCount){
                viewHodel.tv_volume.setText("0");
            }else{
                viewHodel.tv_volume.setText("销 " + listGoods.get(position).SaleCount);
            }

            if (null == listGoods.get(position).SharedCount){
                viewHodel.tv_sharks.setText("0");
            }else{
                viewHodel.tv_sharks.setText(listGoods.get(position).SharedCount);
            }

            if (null == listGoods.get(position).HitCount ){
                viewHodel.tv_lookthroughs.setText("0");//浏览
            }else{
                viewHodel.tv_lookthroughs.setText(listGoods.get(position).HitCount);//浏览
            }

            if (null != listGoods.get(position).Address && null != listGoods.get(position).Province && null != listGoods.get(position).City) {
                viewHodel.tv_address.setText(listGoods.get(position).Country+" · "+listGoods.get(position).Province+listGoods.get(position).City+listGoods.get(position).Address);//促销地点
            }
            if (null != listGoods.get(position).StartTimeName && !TextUtils.isEmpty(listGoods.get(position).StartTimeName)) {
                viewHodel.statr_time.setText(listGoods.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != listGoods.get(position).EndTimeName && !TextUtils.isEmpty(listGoods.get(position).EndTimeName)) {
                viewHodel.end_time.setText(listGoods.get(position).EndTimeName.split(" ")[0]);
            }
            if (listGoods.get(position).Preferential.length() > 5){
                viewHodel.tv_favourable.setText(listGoods.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.tv_favourable.setText(listGoods.get(position).Preferential);
            }

            viewHodel.tv_price.setText(listGoods.get(position).Currency+" " +listGoods.get(position).Price);
            viewHodel.tv_agoprice.setText(listGoods.get(position).Currency+" " +listGoods.get(position).OriginalPrice);
            return convertView;
        }
    }

    /**
     * 店铺的适配器
     */
    private class BusinessAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listBusiness.size();
        }
        @Override
        public Object getItem(int position) {
            return listBusiness.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            BusinessViewHodel businessViewHodel = null;
            if (convertView == null) {
                businessViewHodel = new BusinessViewHodel();
                convertView = LayoutInflater.from(CollectCenterActivity.this).inflate(R.layout.business_editor_item, null);
                businessViewHodel.isEditor = (ImageView) convertView.findViewById(R.id.isSelect);
                businessViewHodel.select = (LinearLayout) convertView.findViewById(R.id.select);
                businessViewHodel.headIamge = (ImageView) convertView.findViewById(R.id.business_image);
                businessViewHodel.StoreName = (TextView) convertView.findViewById(R.id.business_name);
                businessViewHodel.Location = (TextView) convertView.findViewById(R.id.business_address);
                businessViewHodel.into_shop = (TextView) convertView.findViewById(R.id.into_shop);
                convertView.setTag(businessViewHodel);
            }
            businessViewHodel = (BusinessViewHodel) convertView.getTag();
            /**
             * 查看店铺详情
             */
            businessViewHodel.into_shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefUtils.setString(CollectCenterActivity.this,"collection","1");
                    Intent intent = new Intent(CollectCenterActivity.this, BusinessMessageActivity.class);
                    intent.putExtra("id", listBusiness.get(position).UserId);
                    startActivity(intent);
                }
            });
            businessViewHodel.select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < listBusiness.size(); i++) {
                        if (i == position) {
                            if (listBusiness.get(i).IsSelect) {
                                listBusiness.get(i).IsSelect = false;
                            } else {
                                listBusiness.get(i).IsSelect = true;
                            }
                        }
                    }
                    businessAdapter.notifyDataSetChanged();
                }
            });
            if (listBusiness.get(position).IsEditor) {
                businessViewHodel.select.setVisibility(View.VISIBLE);
            } else {
                businessViewHodel.select.setVisibility(View.GONE);
            }
            if (listBusiness.get(position).IsSelect) {
                businessViewHodel.isEditor.setBackgroundResource(R.mipmap.select);
            }
            if (!listBusiness.get(position).IsSelect) {
                businessViewHodel.isEditor.setBackgroundResource(R.mipmap.noselect);
            }
            /**
             * 加载圆角图片
             */
            RequestOptions options = new RequestOptions()
                    .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH)
                    .transform(new GlideRoundTransform(CollectCenterActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

            Glide.with(CollectCenterActivity.this).load(URLText.img_url + listBusiness.get(position).PhotoPath)
                    .apply(options).into(businessViewHodel.headIamge);

            businessViewHodel.StoreName.setText(listBusiness.get(position).StoreName);
            businessViewHodel.Location.setText(listBusiness.get(position).Province + " " + listBusiness.get(position).City);
            return convertView;
        }
    }
    static class GoodsViewHodel {
        public LinearLayout select;
        public ImageView isEditor;
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
    }
    static class BusinessViewHodel {
        TextView into_shop;
        public LinearLayout select;
        public ImageView isEditor;
        ImageView headIamge;
        TextView StoreName;
        TextView Location;
    }

    /**
     * 得到收藏的商品
     */
    private void getCollectGoods() {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(CollectCenterActivity.this, URLText.COLLECT_GOODS, RequestParamsPool.collectGoods(), new MyAsyncHttpResponseHandler(CollectCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    //JSONObject object=new JSONObject();
                    GoodsList goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                    listGoods = goodsList.MainData;

                    if (listGoods.size() == 0){
                        listView1.setVisibility(View.GONE);
                        listView2.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.VISIBLE);
                    }else{
                        listView1.setVisibility(View.VISIBLE);
                        listView2.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.GONE);
                    }

                    /**
                     *对收藏进行排序
                     */
                    Collections.sort(listGoods, new Comparator<Goods>() {
                        @Override
                        public int compare(Goods goods, Goods t1) {
                            int i = Integer.parseInt(t1.FavouriteCount) - Integer.parseInt(goods.FavouriteCount);
                            goodsAdapter.notifyDataSetChanged();
                            return i;
                        }
                    });

                    goodsAdapter.notifyDataSetChanged();

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
     * 得到收藏的店铺
     */
    private void getCollectBusiness() {
        WebRequestHelper.json_post(CollectCenterActivity.this, URLText.COLLECT_BUSINESS, RequestParamsPool.collectBusiness(), new MyAsyncHttpResponseHandler(CollectCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                BusinessList businessList = GsonUtils.fromJson(new String(responseBody), BusinessList.class);
                listBusiness = businessList.MainData;
                businessAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 取消收藏商品
     * @param list
     */
    public void cancelCollect(List<String> list) {
        WebRequestHelper.json_post(CollectCenterActivity.this, URLText.CANCEL_COLLECT, RequestParamsPool.cancelColect("", list), new MyAsyncHttpResponseHandler(CollectCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.optString("Message");
                    Toast.makeText(CollectCenterActivity.this, message, Toast.LENGTH_SHORT).show();
                    goodsAdapter.notifyDataSetChanged();

                    /**
                     * 获取店铺收藏
                     */
                    getCollectBusiness();//---------------------刷新

                    /**
                     *获取商品收藏
                     */
                    getCollectGoods();//---------------------刷新

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 移除收藏店铺
     * @param list
     */
    private void cancelBusiness(List<String> list) {
        WebRequestHelper.json_post(CollectCenterActivity.this, URLText.CANCEL_COLLECT_BUSINESS, RequestParamsPool.cancelBusiness("", list), new MyAsyncHttpResponseHandler(CollectCenterActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String message = jsonObject.optString("Message");
                    Toast.makeText(CollectCenterActivity.this, message, Toast.LENGTH_SHORT).show();
                    businessAdapter.notifyDataSetChanged();

                    /**
                     * 获取店铺收藏
                     */
                    getCollectBusiness();//---------------------刷新

                    /**
                     *获取商品收藏
                     */
                    getCollectGoods();//---------------------刷新

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName,Object cid) {
            try {
                if (Constants.LOADING_COLLECTION.equals(eventName)) {
                    /**
                     * 获取店铺收藏
                     */
                    getCollectBusiness();

                    /**
                     *获取商品收藏
                     */
                    getCollectGoods();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.LOADING_COLLECTION);
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