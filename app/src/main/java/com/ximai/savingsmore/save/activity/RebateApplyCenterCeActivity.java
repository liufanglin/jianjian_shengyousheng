package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.adapter.RebateApplyCenterAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.RebateApplyCenterBean;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxing on 2018/1/5 0005.
 * 返利申请中心 - 支持侧滑
 */

public class RebateApplyCenterCeActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout back;
    private RelativeLayout title＿right;
    private LinearLayout ll_defaultdata;
    private List<RebateApplyCenterBean.MainData> list = new ArrayList<>();//积分返利数据
    private RebateApplyCenterAdapter rebateApplyCenterAdapter;
    private SwipeMenuRecyclerView recycle_view;
    private LinearLayoutManager mLayoutManager;
    private DefaultItemDecoration mItemDecoration;
    private KyLoadingBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rebate_applyce);
        initView();
        initData();
        initEvent();
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
        title＿right = (RelativeLayout) findViewById(R.id.title＿right);
        recycle_view = (SwipeMenuRecyclerView) findViewById(R.id.recycle_view);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);//暂无数据
    }
    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        title＿right.setOnClickListener(this);

        /**
         * 点击详情的数据
         */
        rebateApplyCenterAdapter.setViewClickListener(new RebateApplyCenterAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, List<RebateApplyCenterBean.MainData> list) {
                Intent intent = new Intent(RebateApplyCenterCeActivity.this,PointApplyDeatilActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) list);
                bundle.putInt("postion",postion);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * data
     */
    private void initData() {
        mLayoutManager = new LinearLayoutManager(RebateApplyCenterCeActivity.this);//设置布局参数
        mItemDecoration = new DefaultItemDecoration(ContextCompat.getColor(RebateApplyCenterCeActivity.this, R.color.white));
        recycle_view.setLayoutManager(mLayoutManager);
        recycle_view.addItemDecoration(mItemDecoration);
//        recycle_view.setSwipeItemClickListener(this);//设置Iten的点击事件
        recycle_view.setSwipeMenuCreator(swipeMenuCreator);//创建侧边栏
        recycle_view.setSwipeMenuItemClickListener(mMenuItemClickListener);//侧边栏点击事件

        rebateApplyCenterAdapter = new RebateApplyCenterAdapter(this);
        recycle_view.setAdapter(rebateApplyCenterAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 获取返利中心的数据
         */
        getRebateApplyData();
    }

    /**
     * 事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back://返回
                finish();
                break;
            case R.id.title＿right://申请返利
                startActivity(new Intent(this,ApplyRebateActivity.class));
                break;
        }
    }

    /**
     * 获取返利申请中心的数据
     */
    private void getRebateApplyData() {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(RebateApplyCenterCeActivity.this, URLText.REBATEAPPLY, RequestParamsPool.getRebateApplyData(), new MyAsyncHttpResponseHandler(RebateApplyCenterCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String s = new String(responseBody);
                    RebateApplyCenterBean bean = GsonUtils.fromJson(s, RebateApplyCenterBean.class);
                    if (bean.MainData.size() == 0){
                        ll_defaultdata.setVisibility(View.VISIBLE);
                        recycle_view.setVisibility(View.GONE);
                    }else{
                        list.clear();
                        for (int i = 0; i < bean.MainData.size(); i++) {//过滤一下数据只需要
                            list.add(bean.MainData.get(i));
                        }
                        if (list.size() == 0){
                            ll_defaultdata.setVisibility(View.VISIBLE);
                            recycle_view.setVisibility(View.GONE);
                        }else{
                            ll_defaultdata.setVisibility(View.GONE);
                            recycle_view.setVisibility(View.VISIBLE);
                        }
                    }
                    /**
                     * 设置适配器数据
                     */
                    rebateApplyCenterAdapter.setData(list);
                    rebateApplyCenterAdapter.notifyDataSetChanged();

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
     * 删除操作
     */
    public void deleteData(final int postion, final List<RebateApplyCenterBean.MainData> list){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                if (null != list.get(postion).Id){
                    deleteRebateData(list.get(postion).Id);
                }
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "确定删除吗？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 返利数据中心数据删除
     */
    private void deleteRebateData(String id) {
        WebRequestHelper.json_post(RebateApplyCenterCeActivity.this, URLText.DELETE_REBATEDATA, RequestParamsPool.deleteRebateData(id), new MyAsyncHttpResponseHandler(RebateApplyCenterCeActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String resule = new String(responseBody);
                    JSONObject object = new JSONObject(resule);
                    String message = object.optString("Message");
                    String is = object.optString("IsSuccess");
                    if (is.equals("true")) {
                        /**
                         * 获取返利中心的数据
                         */
                        getRebateApplyData();
                        Toast.makeText(RebateApplyCenterCeActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(RebateApplyCenterCeActivity.this)
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
                deleteData(adapterPosition,list);
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
}