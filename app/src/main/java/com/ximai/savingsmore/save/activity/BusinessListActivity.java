package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.cache.MyImageLoader;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.view.HorizontalListView;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.Business;
import com.ximai.savingsmore.save.modle.BusinessList;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.LoginUser;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/13.
 */
public class BusinessListActivity extends BaseActivity {
    private ListView listView;
    private List<Business> listBusiness = new ArrayList<Business>();
    private BusinessAdapter businessAdapter;
    private List<Images> imagesList = new ArrayList<Images>();
    private String keyWord;
    private String ProvceId, CityId, AreaId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_list_activity);
        setCenterTitle("商家店铺");
        setLeftBackMenuVisibility(BusinessListActivity.this, "");
        listView = (ListView) findViewById(R.id.listview);
        businessAdapter = new BusinessAdapter();
        listView.setAdapter(businessAdapter);
        Intent intent = getIntent();
        keyWord = getIntent().getStringExtra("key");
        ProvceId = intent.getStringExtra("shengId");
        CityId = intent.getStringExtra("shiId");
        AreaId = intent.getStringExtra("xianId");
        getBUsinessList(keyWord, ProvceId, CityId, AreaId);

    }

    class BusinessViewHodel {
        TextView into_shop;
        HorizontalListView horizontalListView;
        ImageView headIamge;
        TextView StoreName;
    }

    class GridViewViewHodel {
        ImageView imageView;
    }

    private class GridViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return imagesList.size();
        }

        @Override
        public Object getItem(int position) {
            return imagesList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GridViewViewHodel gridViewViewHodel;
            if (convertView == null) {
                convertView = LayoutInflater.from(BusinessListActivity.this).inflate(R.layout.commen_gridview_item, null);
                gridViewViewHodel = new GridViewViewHodel();
                gridViewViewHodel.imageView = (ImageView) convertView.findViewById(R.id.iamge);
                convertView.setTag(gridViewViewHodel);
            }
            gridViewViewHodel = (GridViewViewHodel) convertView.getTag();
            if (imagesList.size() > position) {
                MyImageLoader.displayDefaultImage(URLText.img_url + imagesList.get(position).ImagePath, gridViewViewHodel.imageView);
            }
            return convertView;
        }
    }

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
                convertView = LayoutInflater.from(BusinessListActivity.this).inflate(R.layout.business_list_item, null);
                businessViewHodel.headIamge = (ImageView) convertView.findViewById(R.id.business_image);
                businessViewHodel.StoreName = (TextView) convertView.findViewById(R.id.business_name);
                businessViewHodel.into_shop = (TextView) convertView.findViewById(R.id.into_shop);
                businessViewHodel.horizontalListView = (HorizontalListView) convertView.findViewById(R.id.image_list);
                convertView.setTag(businessViewHodel);
            }
            businessViewHodel = (BusinessViewHodel) convertView.getTag();
            businessViewHodel.into_shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (LoginUser.getInstance().isLogin()) {
                        Intent intent = new Intent(BusinessListActivity.this, BusinessMessageActivity.class);
                        intent.putExtra("id", listBusiness.get(position).UserId);
                        startActivity(intent);
                    } else {
                        Toast.makeText(BusinessListActivity.this, "您还没有登录", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(BusinessListActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
            imagesList = listBusiness.get(position).Images;
            GridViewAdapter gridViewAdapter = new GridViewAdapter();
            businessViewHodel.horizontalListView.setAdapter(gridViewAdapter);
            MyImageLoader.displayDefaultImage(URLText.img_url + listBusiness.get(position).PhotoPath, businessViewHodel.headIamge);
            businessViewHodel.StoreName.setText(listBusiness.get(position).StoreName);
            // businessViewHodel.Location.setText(listBusiness.get(position).Province + " " + listBusiness.get(position).City);
            return convertView;
        }
    }

    private void getBUsinessList(String keyWord, String ProiveId, String CityId, String AreaId) {
        WebRequestHelper.json_post(BusinessListActivity.this, URLText.BUSINESS_LIST, RequestParamsPool.businessList(keyWord,ProiveId,CityId,AreaId), new MyAsyncHttpResponseHandler(BusinessListActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    BusinessList businessList = GsonUtils.fromJson(new String(responseBody), BusinessList.class);
                    listBusiness = businessList.MainData;
                    if (listBusiness.size() == 0) {
                        Toast.makeText(BusinessListActivity.this, "请调整搜索条件，再搜！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    businessAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
