package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.view.ScrollViewWithListView;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.BaseMessage;
import com.ximai.savingsmore.save.modle.GoodSalesType;
import com.ximai.savingsmore.save.modle.GoodSalesTypeList;
import com.ximai.savingsmore.save.modle.ListBaseMessage;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/11/21.
 * 废弃 -
 */
//  商品搜索
public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private View introduce, sales_good;
    private TextView isBag, noBag;
    private String bag = "true";
    private List<BaseMessage> list = new ArrayList<BaseMessage>();
    private LinearLayout good_title;
    private TextView good_to_business;
    private boolean isGood = true, isBusiness = false;
    private EditText editText;
    private LinearLayout goodsClassicty;
    private List<BaseMessage> base = new ArrayList<BaseMessage>();
    private List<BaseMessage> isbag_good_one_classify = new ArrayList<BaseMessage>();
    private List<BaseMessage> nobag_good_one_classify = new ArrayList<BaseMessage>();
    private List<BaseMessage> good_one_classify = new ArrayList<BaseMessage>();
    private List<BaseMessage> good_two_classify = new ArrayList<BaseMessage>();
    private List<BaseMessage> brand_list = new ArrayList<BaseMessage>();
    private List<BaseMessage> sheng_list = new ArrayList<BaseMessage>();
    private List<BaseMessage> shi_list = new ArrayList<BaseMessage>();
    private List<BaseMessage> xian_list = new ArrayList<BaseMessage>();

    private List<String> salesState = new ArrayList<String>();
    private List<GoodSalesType> goodSalesTypes = new ArrayList<GoodSalesType>();
    private ScrollViewWithListView good_one, sales_state, good_two, brand_listview, sales_type;
    private OneClassityAdapter oneClassityAdapter;
    private TwoClassityAdapter twoClassityAdapter;
    private SalesStateAdapter salesStateAdapter;
    private BrandAdapter brandAdapter;
    private GoodSalesAdapter goodSalesAdapter;
    private RelativeLayout good_classify, state, good_brand, comment_state, type;
    private LinearLayout good_fenlei;
    private ImageView classify_iamge, state_image, brand_image, comment_image, type_image;
    private TextView classity_text, state_text, brand_text, comment_text, type_text;
    private TextView commit;
    private BaseMessage classify1, classify2, brand;
    private boolean isSales;
    private GoodSalesType commentId, typeId;
    private RelativeLayout quyu_item;
    private TextView quyu_text;
    private ImageView quyu_image;
    private LinearLayout quyu_list;
    private ScrollViewWithListView sheng_listview, shi_listview, xian_listview;
    private ShengAdapter shengAdapter = new ShengAdapter();
    private ShiAdapter shiAdapter = new ShiAdapter();
    private XianAdapter xianAdapter = new XianAdapter();
    private String ShnegId, ShiId, XianId;
    private String Shneg, Shi, Xian;
    BaseMessage baseMessage = new BaseMessage();
    private String IsPromotion = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activty);
        setLeftBackMenuVisibility(SearchActivity.this, "");
        setCenterTitle(getIntent().getStringExtra("title"));
        isBag = (TextView) findViewById(R.id.is_bag);
        noBag = (TextView) findViewById(R.id.no_bag);
        introduce = findViewById(R.id.introduce);
        sales_good = findViewById(R.id.cuxiao_goog);
        goodsClassicty = (LinearLayout) findViewById(R.id.is_good);
        editText = (EditText) findViewById(R.id.search);
        good_title = (LinearLayout) findViewById(R.id.good_title);
        good_to_business = (TextView) findViewById(R.id.good_to_business);
        good_to_business.setOnClickListener(this);
        good_one = (ScrollViewWithListView) findViewById(R.id.good_one);
        good_two = (ScrollViewWithListView) findViewById(R.id.good_two);
        sales_state = (ScrollViewWithListView) findViewById(R.id.sales_list);
        brand_listview = (ScrollViewWithListView) findViewById(R.id.brand_list);
        sales_type = (ScrollViewWithListView) findViewById(R.id.type_list);


        baseMessage.Name = "不限";
        baseMessage.Id = "";
        baseMessage.ParentId = "";
        good_one_classify.add(baseMessage);
        brand_list.add(baseMessage);

        classify_iamge = (ImageView) findViewById(R.id.driection);
        state_image = (ImageView) findViewById(R.id.sales_driection);
        brand_image = (ImageView) findViewById(R.id.brand_driection);
        type_image = (ImageView) findViewById(R.id.type_driection);

        good_classify = (RelativeLayout) findViewById(R.id.good_classify);
        state = (RelativeLayout) findViewById(R.id.sales_state);
        good_brand = (RelativeLayout) findViewById(R.id.brand);
        comment_state = (RelativeLayout) findViewById(R.id.comment);
        type = (RelativeLayout) findViewById(R.id.sales_type);
        good_fenlei = (LinearLayout) findViewById(R.id.good_classfiy_list);
        good_classify.setOnClickListener(this);
        state.setOnClickListener(this);
        good_brand.setOnClickListener(this);
        type.setOnClickListener(this);

        classity_text = (TextView) findViewById(R.id.classify_text);
        state_text = (TextView) findViewById(R.id.state_text);
        brand_text = (TextView) findViewById(R.id.brand_text);
        type_text = (TextView) findViewById(R.id.type_text);

        commit = (TextView) findViewById(R.id.budiness_comfirm);

        quyu_item = (RelativeLayout) findViewById(R.id.quyu_item);
        quyu_image = (ImageView) findViewById(R.id.quyu_driection);
        quyu_text = (TextView) findViewById(R.id.quyu_text);
        quyu_item.setOnClickListener(this);
        quyu_list = (LinearLayout) findViewById(R.id.quyu_list);
        sheng_listview = (ScrollViewWithListView) findViewById(R.id.sheng_list);
        shi_listview = (ScrollViewWithListView) findViewById(R.id.shi_list);
        xian_listview = (ScrollViewWithListView) findViewById(R.id.xian_list);
        sheng_listview.setAdapter(shengAdapter);
        shi_listview.setAdapter(shiAdapter);
        xian_listview.setAdapter(xianAdapter);


        commit.setOnClickListener(this);
        oneClassityAdapter = new OneClassityAdapter();
        salesStateAdapter = new SalesStateAdapter();
        twoClassityAdapter = new TwoClassityAdapter();
        goodSalesAdapter = new GoodSalesAdapter();
        brandAdapter = new BrandAdapter();
        sales_type.setAdapter(goodSalesAdapter);
        brand_listview.setAdapter(brandAdapter);
        good_one.setAdapter(oneClassityAdapter);
        good_two.setAdapter(twoClassityAdapter);
        sales_state.setAdapter(salesStateAdapter);
        salesState.add("全部");
        salesState.add("热卖中");
        salesState.add("促销结束");
        isBag.setOnClickListener(this);
        noBag.setOnClickListener(this);
        good_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classity_text.setText("不限");
                classify2 = null;
                good_two_classify.clear();
                good_two_classify.add(baseMessage);
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && good_one_classify.get(position).Id.equals(list.get(i).ParentId)) {
                        good_two_classify.add(list.get(i));
                    }
                }
                classify1 = good_one_classify.get(position);
                twoClassityAdapter.notifyDataSetChanged();
            }
        });
        good_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                classity_text.setText(good_two_classify.get(position).Name);
                classify2 = good_two_classify.get(position);
            }
        });
        sales_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                state_text.setText(salesState.get(position));
                if (salesState.get(position).equals("全部")) {
                    IsPromotion = "all";
                } else if (salesState.get(position).equals("热卖中")) {
                    IsPromotion = "true";
                } else if (salesState.get(position).equals("促销结束")) {
                    IsPromotion = "false";
                }

            }
        });
        brand_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brand_text.setText(brand_list.get(position).Name);
                brand = brand_list.get(position);
            }
        });
        sales_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                type_text.setText(goodSalesTypes.get(position).Value);
                typeId = goodSalesTypes.get(position);
            }
        });
        sheng_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shi_list.clear();
                shi_list.add(baseMessage);
                Shneg = sheng_list.get(position).Name;
                quyu_text.setText(Shneg);
                ShnegId = sheng_list.get(position).Id;
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Id && null != list.get(i).ParentId && list.get(i).ParentId.equals(ShnegId)) {
                        shi_list.add(list.get(i));
                    }
                }
                shiAdapter.notifyDataSetChanged();
            }
        });
        shi_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shi = shi_list.get(position).Name;
                xian_list.clear();
                xian_list.add(baseMessage);
                quyu_text.setText(Shneg + " " + Shi);
                ShiId = shi_list.get(position).Id;
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Id && null != list.get(i).ParentId && list.get(i).ParentId.equals(ShiId)) {
                        xian_list.add(list.get(i));
                    }
                }
                xianAdapter.notifyDataSetChanged();
            }
        });
        xian_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Xian = xian_list.get(position).Name;
                XianId = xian_list.get(position).Id;
                quyu_text.setText(Shneg + " " + Shi + " " + Xian);
            }
        });
        queryDicNode();
        queryDicNode2();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.quyu_item:
                if (quyu_list.getVisibility() == View.VISIBLE) {
                    quyu_image.setBackgroundResource(R.mipmap.search_up3);
                    quyu_list.setVisibility(View.GONE);
                } else {
                    quyu_image.setBackgroundResource(R.mipmap.search_dowm3);
                    quyu_list.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.is_bag:
                bag = "true";
                good_one_classify = isbag_good_one_classify;
                good_two_classify.clear();
                twoClassityAdapter.notifyDataSetChanged();
                introduce.setVisibility(View.VISIBLE);
                sales_good.setVisibility(View.INVISIBLE);
                oneClassityAdapter.notifyDataSetChanged();
                break;
            case R.id.no_bag:
                bag = "false";
                good_one_classify = nobag_good_one_classify;
                good_two_classify.clear();
                twoClassityAdapter.notifyDataSetChanged();
                introduce.setVisibility(View.INVISIBLE);
                sales_good.setVisibility(View.VISIBLE);
                oneClassityAdapter.notifyDataSetChanged();
                break;
            case R.id.good_to_business:
                if (isGood) {
                    goodsClassicty.setVisibility(View.GONE);
                    good_to_business.setText("商品");
                    setCenterTitle("促销商铺搜索");
                    editText.setHint("寻找你喜欢的商铺或商圈");
                    good_title.setVisibility(View.GONE);
                    isGood = false;
                    isBusiness = true;
                } else {
                    goodsClassicty.setVisibility(View.VISIBLE);
                    good_to_business.setText("店铺");
                    setCenterTitle("促销商品搜索");
                    editText.setHint("寻找你喜欢的商品");
                    good_title.setVisibility(View.VISIBLE);
                    isGood = true;
                    isBusiness = false;
                }
                break;
            case R.id.good_classify:
                if (good_fenlei.getVisibility() == View.VISIBLE) {
                    good_fenlei.setVisibility(View.GONE);
                    classify_iamge.setBackgroundResource(R.mipmap.search_up3);
                } else if (good_fenlei.getVisibility() == View.GONE) {
                    good_fenlei.setVisibility(View.VISIBLE);
                    classify_iamge.setBackgroundResource(R.mipmap.search_dowm3);
                }
                break;
            case R.id.sales_state:
                if (sales_state.getVisibility() == View.VISIBLE) {
                    sales_state.setVisibility(View.GONE);
                    state_image.setBackgroundResource(R.mipmap.search_up3);
                } else if (sales_state.getVisibility() == View.GONE) {
                    sales_state.setVisibility(View.VISIBLE);
                    state_image.setBackgroundResource(R.mipmap.search_dowm3);
                }
                break;
            case R.id.brand:
                if (brand_listview.getVisibility() == View.VISIBLE) {
                    brand_listview.setVisibility(View.GONE);
                    brand_image.setBackgroundResource(R.mipmap.search_up3);
                } else if (brand_listview.getVisibility() == View.GONE) {
                    brand_listview.setVisibility(View.VISIBLE);
                    brand_image.setBackgroundResource(R.mipmap.search_dowm3);
                }
                break;

            case R.id.sales_type:
                if (sales_type.getVisibility() == View.VISIBLE) {
                    sales_type.setVisibility(View.GONE);
                    type_image.setBackgroundResource(R.mipmap.search_up3);
                } else if (sales_type.getVisibility() == View.GONE) {
                    sales_type.setVisibility(View.VISIBLE);
                    type_image.setBackgroundResource(R.mipmap.search_dowm3);
                }
                break;
            case R.id.budiness_comfirm:
                String key = editText.getText().toString();
                if (isGood) {
                    Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                    intent.putExtra("title", "促销商品搜索");
                    intent.putExtra("keyStore", editText.getText().toString());
                    if (state_text.getText().equals("热卖中")) {
                        isSales = true;
                        intent.putExtra("state", isSales + "");
                    } else if (state_text.getText().equals("促销结束")) {
                        isSales = false;
                        intent.putExtra("state", isSales);
                    }
                    if (null != classify1 && !classify1.Name.equals("不限")) {
                        intent.putExtra("classify1", classify1.Id);
                    }
                    if (null != classify2 && !classify2.Name.equals("不限")) {
                        intent.putExtra("classify2", classify2.Id);
                    }
                    if (null != brand && !brand.Name.equals("不限")) {
                        intent.putExtra("brand", brand.Id);
                    }
                    if (null != typeId) {
                        intent.putExtra("typeId", typeId.Id);
                    }
                    if (null != ShnegId) {
                        intent.putExtra("shengId", ShnegId);
                    }
                    if (null != state) {
                        intent.putExtra("state", IsPromotion);
                    }
                    if (null != ShiId) {
                        intent.putExtra("shiId", ShiId);
                    }
                    if (null != XianId) {
                        intent.putExtra("xianId", XianId);
                    }
                    intent.putExtra("isBag", bag);
                    intent.putExtra("search", true);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SearchActivity.this, BusinessListActivity.class);
                    if (null != ShnegId) {
                        intent.putExtra("shengId", ShnegId);
                    }
                    if (null != ShiId) {
                        intent.putExtra("shiId", ShiId);
                    }
                    if (null != XianId) {
                        intent.putExtra("xianId", XianId);
                    }
                    intent.putExtra("key", editText.getText().toString());
                    startActivity(intent);
                }


        }
    }

    //查询基础字典
    private void queryDicNode() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(SearchActivity.this, URLText.QUERYDICNODE, stringEntity, new MyAsyncHttpResponseHandler(SearchActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                ListBaseMessage listBaseMessage = GsonUtils.fromJson(new String(responseBody), ListBaseMessage.class);
                list = listBaseMessage.MainData;
                for (int i = 0; i < list.size(); i++) {
                    if (null == list.get(i).ParentId) {
                        base.add(list.get(i));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("ba335639-52c2-4e8d-8d2b-faf8ed097418") && list.get(i).IsBag.equals("true")) {
                        isbag_good_one_classify.add(list.get(i));
                    } else if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("ba335639-52c2-4e8d-8d2b-faf8ed097418") && list.get(i).IsBag.equals("false")) {
                        nobag_good_one_classify.add(list.get(i));
                    }
                    if (null != list.get(i).Name && null != list.get(i).ParentId && null != list.get(i).Id && list.get(i).ParentId.equals("a390a2ff-40a2-487d-a719-c9ae5980fbae")) {
                        brand_list.add(list.get(i));
                    }
                }
                sheng_list.add(baseMessage);
                for (int i = 0; i < list.size(); i++) {
                    if (null != list.get(i).Id && null != list.get(i).ParentId && list.get(i).ParentId.equals("72183c02-e051-4f6f-8524-c5fc9a9aa419")) {
                        sheng_list.add(list.get(i));

                    }
                }
                shengAdapter.notifyDataSetChanged();
                oneClassityAdapter.notifyDataSetChanged();
                brandAdapter.notifyDataSetChanged();
                good_one_classify = isbag_good_one_classify;
            }
        });
    }

    //查询促销形式
    private void queryDicNode2() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(SearchActivity.this, URLText.QUERYDICNODE2, stringEntity, new MyAsyncHttpResponseHandler(SearchActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                GoodSalesTypeList goodSalesTypeList = GsonUtils.fromJson(new String(responseBody), GoodSalesTypeList.class);
                goodSalesTypes = goodSalesTypeList.ShowData;
                goodSalesAdapter.notifyDataSetChanged();
            }
        });
    }

    private class OneClassityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return good_one_classify.size();
        }

        @Override
        public Object getItem(int position) {
            return good_one_classify.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneClassityViewHodle viewHodle;
            if (convertView == null) {
                viewHodle = new OneClassityViewHodle();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHodle.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHodle);
            }
            viewHodle = (OneClassityViewHodle) convertView.getTag();
            viewHodle.title.setText(good_one_classify.get(position).Name);
            return convertView;
        }
    }


    private class TwoClassityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return good_two_classify.size();
        }

        @Override
        public Object getItem(int position) {
            return good_two_classify.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneClassityViewHodle viewHodle;
            if (convertView == null) {
                viewHodle = new OneClassityViewHodle();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHodle.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHodle);
            }
            viewHodle = (OneClassityViewHodle) convertView.getTag();
            viewHodle.title.setText(good_two_classify.get(position).Name);
            return convertView;
        }
    }

    private class BrandAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return brand_list.size();
        }

        @Override
        public Object getItem(int position) {
            return brand_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneClassityViewHodle viewHodle;
            if (convertView == null) {
                viewHodle = new OneClassityViewHodle();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHodle.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHodle);
            }
            viewHodle = (OneClassityViewHodle) convertView.getTag();
            viewHodle.title.setText(brand_list.get(position).Name);
            return convertView;
        }
    }

    private class GoodSalesAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return goodSalesTypes.size();
        }

        @Override
        public Object getItem(int position) {
            return goodSalesTypes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneClassityViewHodle viewHodle;
            if (convertView == null) {
                viewHodle = new OneClassityViewHodle();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHodle.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHodle);
            }
            viewHodle = (OneClassityViewHodle) convertView.getTag();
            viewHodle.title.setText(goodSalesTypes.get(position).Value);
            return convertView;
        }
    }


    private class SalesStateAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return salesState.size();
        }

        @Override
        public Object getItem(int position) {
            return salesState.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneClassityViewHodle viewHodle;
            if (convertView == null) {
                viewHodle = new OneClassityViewHodle();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHodle.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHodle);
            }
            viewHodle = (OneClassityViewHodle) convertView.getTag();
            viewHodle.title.setText(salesState.get(position));
            return convertView;
        }
    }

    private class ShengAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sheng_list.size();
        }

        @Override
        public Object getItem(int position) {
            return sheng_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneClassityViewHodle viewHodle;
            if (convertView == null) {
                viewHodle = new OneClassityViewHodle();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHodle.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHodle);
            }
            viewHodle = (OneClassityViewHodle) convertView.getTag();
            viewHodle.title.setText(sheng_list.get(position).Name);
            return convertView;
        }
    }

    private class ShiAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return shi_list.size();
        }

        @Override
        public Object getItem(int position) {
            return shi_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneClassityViewHodle viewHodle;
            if (convertView == null) {
                viewHodle = new OneClassityViewHodle();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHodle.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHodle);
            }
            viewHodle = (OneClassityViewHodle) convertView.getTag();
            viewHodle.title.setText(shi_list.get(position).Name);
            return convertView;
        }
    }

    private class XianAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return xian_list.size();
        }

        @Override
        public Object getItem(int position) {
            return xian_list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            OneClassityViewHodle viewHodle;
            if (convertView == null) {
                viewHodle = new OneClassityViewHodle();
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item, null);
                viewHodle.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(viewHodle);
            }
            viewHodle = (OneClassityViewHodle) convertView.getTag();
            viewHodle.title.setText(xian_list.get(position).Name);
            return convertView;
        }
    }


    class OneClassityViewHodle {
        TextView title;
    }
}
