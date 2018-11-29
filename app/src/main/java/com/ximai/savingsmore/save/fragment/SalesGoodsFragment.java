package com.ximai.savingsmore.save.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.cache.MyImageLoader;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.activity.GoodDetailsActivity;
import com.ximai.savingsmore.save.adapter.SalesGoodsAdapter;
import com.ximai.savingsmore.save.modle.BusinessMessage;
import com.ximai.savingsmore.save.modle.GoodDetial;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/11/28.
 */
//促销商品
public class SalesGoodsFragment extends Fragment {
    //private User user;
    private GoodsList goodsList = new GoodsList();
    private List<Goods> list = new ArrayList<Goods>();
    private GoodDetial good;
    private BusinessMessage businessMessage;
    private View view;
    private RecyclerView rcxycle_view;
    private SalesGoodsAdapter salesGoodsAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.business_sales_good, null);
        
        initView();

        initData();

        return view;
    }

    /**
     * init - view
     */
    private void initView() {
        rcxycle_view = (RecyclerView) view.findViewById(R.id.rcxycle_view);
        initRecycleView(rcxycle_view);
    }

    /**
     * init-data
     */
    private void initData() {
        //good = (User) getArguments().getSerializable("user");
        // user=good.User;

        salesGoodsAdapter = new SalesGoodsAdapter(getActivity());
        businessMessage= (BusinessMessage) getArguments().getSerializable("good");
        getAllGoods(businessMessage.Id);
        /**`
         * iten - click
         */
        salesGoodsAdapter.setViewClickListener(new SalesGoodsAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, String id) {
                Intent intent=new Intent(getActivity(),GoodDetailsActivity.class);
                intent.putExtra("id",list.get(postion).Id);
                startActivity(intent);
            }
        });
    }

    /**
     * 配置recycleview
     * @param recyclerView
     */
    private void initRecycleView(RecyclerView recyclerView) {
        FullyLinearLayoutManager myLayoutManager = new FullyLinearLayoutManager(getContext()) {
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
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 获取促销所有数据
     * @param SellerId
     */
    private void getAllGoods(String SellerId) {
        WebRequestHelper.json_post(getActivity(), URLText.GET_GOODS, RequestParamsPool.getSalesGoods(SellerId), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String resule = new String(responseBody);
                goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                if (goodsList.IsSuccess) {
                    if (null != goodsList.MainData) {
                        list.addAll(goodsList.MainData);
                        salesGoodsAdapter.setData(list);
                        rcxycle_view.setAdapter(salesGoodsAdapter);
                        salesGoodsAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}
