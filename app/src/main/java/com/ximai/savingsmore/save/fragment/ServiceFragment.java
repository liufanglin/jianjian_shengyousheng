package com.ximai.savingsmore.save.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.activity.SearchBussGoodsActivity;
import com.ximai.savingsmore.save.adapter.ServiceFragmentAdapter;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.modle.ProduceBean;
import com.ximai.savingsmore.save.utils.ActivityHelper;
import com.ximai.savingsmore.save.utils.PrefUtils;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/15 0015.
 */

public class ServiceFragment extends Fragment{


    private int [] image = {
            R.mipmap.tuoban, R.mipmap.zuyu, R.mipmap.hunqin, R.mipmap.ruxue, R.mipmap.fangwu, R.mipmap.peixun,
            R.mipmap.fudao, R.mipmap.vfuwu, R.mipmap.weixiu, R.mipmap.ertong, R.mipmap.youshanwanshui, R.mipmap.yundong,
            R.mipmap.dingzhi, R.mipmap.chuguoyouxue, R.mipmap.xiuxianyule, R.mipmap.meirong, R.mipmap.jiudian,
            R.mipmap.yiliao, R.mipmap.ganxi, R.mipmap.gaojicanyin, R.mipmap.qiye, R.mipmap.zufang,
            R.mipmap.xingqu, R.mipmap.meija, R.mipmap.yanglao
    };

    private String [] title = {"托班早教","足浴按摩","婚庆仪式","入学指导","房屋装修","认证培训","课外辅导",
            "生活服务","维修保养","儿童娱乐","游山玩水","运动健身","定制服装","出国游学","休闲娱乐","美容美发",
            "酒店宾馆","医疗保健","衣帽干洗","特色餐饮","企业服务","租房租车","培养兴趣","纹身美甲","养老服务"};

    private String [] ParentId = {
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
    private String [] Id   = {
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
    };
    private String [] SortNo    = {"28","29","30","32","34","35","36","37","38","39","40","41","42","43","44","45","46","47","48","49","50","51","52","53","54"};
    private String [] IsBag  = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};

    private View view;
    private RecyclerView recycle_view;
    private List<ProduceBean> list;
    private ServiceFragmentAdapter serviceFragmentAdapter;
    private GoodsList goodsList  = new GoodsList();
    private List<Goods> listGoods = new ArrayList<>();
    private String goodsId;
    private String goodsfont;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_service, null);
        
        initView();
        
        initData();

        return view;
    }


    /**
     * init - view
     */
    private void initView() {
        recycle_view = (RecyclerView) view.findViewById(R.id.recycle_view);
    }

    /**
     * init - data
     */
    private void initData() {
        //将本地数据添加到集合
        list = new ArrayList<>();
        serviceFragmentAdapter = new ServiceFragmentAdapter(getContext());
        for (int i = 0; i < image.length; i++){
            ProduceBean produceBean = new ProduceBean(image[i],title[i],ParentId[i],Id[i],SortNo[i],IsBag[i]);
            list.add(produceBean);
        }
        serviceFragmentAdapter.setData(list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recycle_view.setLayoutManager(gridLayoutManager);
        recycle_view.setAdapter(serviceFragmentAdapter);
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
                        getAllGoods(MyUserInfoUtils.getInstance().myUserInfo.ProvinceId,null,false,goodsId);
                    }
                },200);
            }
        });
    }

    /**
     * 得到所有的商品
     */
    private void getAllGoods(String Provice,String Keyword,boolean isBag,String FirstClassId) {
        WebRequestHelper.json_post(getActivity(), URLText.GET_GOODS,
                RequestParamsPool.getAllGoods(Provice,Keyword,isBag,FirstClassId), new MyAsyncHttpResponseHandler(getActivity()) {
                    @Override
                    public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                        String resule = new String(responseBody);
                        try {
                            goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                            listGoods.addAll(goodsList.MainData);

//                            if (listGoods.size() == 0) {
//                                Toast.makeText(getActivity(), "暂无更多匹配数据", Toast.LENGTH_SHORT).show();
//                            } else if (listGoods.size() > 0) {
                                PrefUtils.setString(getContext(), "search", "2");
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("goodsList", goodsList);
                                ActivityHelper.init(getActivity()).startActivity(SearchBussGoodsActivity.class, bundle);
//                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null){
            handler.removeCallbacksAndMessages(null);
        }
    }
}