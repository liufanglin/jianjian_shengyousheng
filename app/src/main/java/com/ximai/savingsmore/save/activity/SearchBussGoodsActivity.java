package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.adapter.SearchBussGoodsAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.utils.PrefUtils;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2017/12/21 0021.
 * 搜索公司的适配器展示数据 - 也是搜索商品的展示列表
 * 搜索展示
 */


public class SearchBussGoodsActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recycle_view;
    private GoodsList goodsList;
    private List<Goods> list = new ArrayList<>();//搜索商家信息中商家促销商品
    private SearchBussGoodsAdapter searchBussGoodsAdapter;
    private String search;
    private List<Goods> listGoods;//搜索商品过来的的数据
    private LinearLayout ll_defaultdata;
    private ImageView iv_callphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bussgoods);

        initView();

        initData();

        initEvent();
    }


    /**
     * init - view
     */
    private void initView() {
        search = PrefUtils.getString(this, "search", "0");
        setLeftBackMenuVisibility(SearchBussGoodsActivity.this, "");
        if ("1".equals(search)){
            setCenterTitle("促销商品列表");
        }else if ("2".equals(search)){
            setCenterTitle("商品列表");
            GoodsList goodsList = (GoodsList) getIntent().getBundleExtra("bundle").getSerializable("goodsList");
            listGoods= goodsList.MainData;
        }
        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);
        iv_callphone = (ImageView) findViewById(R.id.iv_callphone);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);//暂无数据
        initRecycleView(recycle_view);
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
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * init - data
     */
    private void initData() {
        searchBussGoodsAdapter = new SearchBussGoodsAdapter(this);
        recycle_view.setAdapter(searchBussGoodsAdapter);

        if ("1".equals(search)){
            String id = getIntent().getStringExtra("id");
            getAllGoods(id);//获取商品数据信息
        }else if ("2".equals(search)){
            if (listGoods.size() == 0){
                recycle_view.setVisibility(View.GONE);
                ll_defaultdata.setVisibility(View.VISIBLE);
            }else{
                searchBussGoodsAdapter.setData(listGoods);
                searchBussGoodsAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * init - event
     */
    private void initEvent() {
        searchBussGoodsAdapter.setViewClickListener(new SearchBussGoodsAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, String id) {
                if ("1".equals(search)){
                    Intent intent=new Intent(SearchBussGoodsActivity.this,GoodDetailsActivity.class);
                    intent.putExtra("id",list.get(postion).Id);
                    startActivity(intent);
                }else if ("2".equals(search)){
                    Intent intent=new Intent(SearchBussGoodsActivity.this,GoodDetailsActivity.class);
                    intent.putExtra("id",listGoods.get(postion).Id);
                    startActivity(intent);
                }
            }
        });
        iv_callphone.setOnClickListener(this);
    }

    /**
     * 事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_callphone://拨打电话
//                initCallPhone("02158366991");
                call("02158366991");
                break;
        }
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /**
     * 打电话
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
                        AlertDialog.newBuilder(SearchBussGoodsActivity.this)
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
                        if (AndPermission.hasAlwaysDeniedPermission(SearchBussGoodsActivity.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(SearchBussGoodsActivity.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        if (requestCode == 200) {// Failure.
                            Toast.makeText(SearchBussGoodsActivity.this, "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .start();
    }
    /**
     * 打电话
     * @param phoneNumber
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
        }catch (Exception e){
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }

    /**
     * 获取促销所有数据
     * @param SellerId
     */
    private void getAllGoods(String SellerId) {
        WebRequestHelper.json_post(this, URLText.GET_GOODS, RequestParamsPool.getSalesGoods(SellerId), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String resule = new String(responseBody);
                goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                if (goodsList.IsSuccess) {
                    if (null != goodsList.MainData) {
                        list.addAll(goodsList.MainData);
                        searchBussGoodsAdapter.setData(list);
                        searchBussGoodsAdapter.notifyDataSetChanged();
                    }
                    if (null != goodsList.ShowData) {
                        list.addAll(goodsList.ShowData);
                        searchBussGoodsAdapter.setData(list);
                        searchBussGoodsAdapter.notifyDataSetChanged();
                    }
                    if (goodsList.MainData == null && goodsList.ShowData == null){
                        recycle_view.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}