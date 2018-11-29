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

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.activity.SearchBussGoodsActivity;
import com.ximai.savingsmore.save.adapter.ProduceFragmentAdapter;
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

public class ProduceFragment extends Fragment {

    private int [] image = {
            R.mipmap.jujia, R.mipmap.nanzhuang, R.mipmap.jiangju, R.mipmap.yanjiu, R.mipmap.xiemao,
            R.mipmap.qicheyongpin, R.mipmap.shipin, R.mipmap.gehu, R.mipmap.huangjin, R.mipmap.yiyap,
            R.mipmap.jiadian, R.mipmap.tushu, R.mipmap.yunfu, R.mipmap.shouji, R.mipmap.wanju,
            R.mipmap.jinkou, R.mipmap.zhongbiao, R.mipmap.tiyu, R.mipmap.shechi, R.mipmap.diannao,
            R.mipmap.neiyi, R.mipmap.ertong, R.mipmap.chongwu, R.mipmap.xianhua, R.mipmap.baoxian,
            R.mipmap.fangwu, R.mipmap.qiche
    };

    private String [] title = {"居家百货","男装女装","家具建材","烟酒商品","鞋帽箱包","汽车用品","食品生鲜",
            "个护化妆","黄金珠宝","医药保健","家用电器","图书音像","孕妇用品","手机数码","玩具乐器","进口商品",
            "钟表眼镜","体育用品","奢侈珍品","电脑办公","内衣配饰","儿童用品","宠物用品","鲜花用品","保险理财",
            "商品房屋","各类汽车"};

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
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418",
            "ba335639-52c2-4e8d-8d2b-faf8ed097418"};
    private String [] Id   = {
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
            "72ae8162-0ee7-45d4-a83f-3049af7c43de"};
    private String [] SortNo    = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","19","20","21","22","23","24","25","26","27"};
    private String [] IsBag  = {"1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1"};

    private View view;
    private RecyclerView recycle_view;
    private ProduceFragmentAdapter produceFragmentAdapter;
    private List<ProduceBean> list;
    private GoodsList goodsList  = new GoodsList();
    private List<Goods> listGoods = new ArrayList<>();
    private String goodsId;
    private String goodsfont;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_produce, null);

        initView();

        initData();

        return view;
    }

    /**
     * init - view  */
    private void initView() {
        recycle_view = (RecyclerView) view.findViewById(R.id.recycle_view);
    }

    private void initData() {
        //将本地数据添加到集合
        list = new ArrayList<>();
        for (int i = 0; i < image.length; i++){
            ProduceBean produceBean = new ProduceBean(image[i],title[i],ParentId[i],Id[i],SortNo[i],IsBag[i]);
            list.add(produceBean);
        }
        //方格设置数据
        produceFragmentAdapter = new ProduceFragmentAdapter(getContext());
        produceFragmentAdapter.setData(list);
        //设置适配器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recycle_view.setLayoutManager(gridLayoutManager);
        recycle_view.setAdapter(produceFragmentAdapter);
        //设置iten的点击事件
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
                        getAllGoods(MyUserInfoUtils.getInstance().myUserInfo.ProvinceId,null,true,goodsId);
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
