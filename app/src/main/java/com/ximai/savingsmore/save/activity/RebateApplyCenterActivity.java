package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxing on 2018/1/5 0005.
 * 返利申请中心 - 不支持侧滑
 */

public class RebateApplyCenterActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout back;
    private RelativeLayout title＿right;
    private LinearLayout ll_defaultdata;
    private List<RebateApplyCenterBean.MainData> list = new ArrayList<>();//积分返利数据
    private RebateApplyCenterAdapter rebateApplyCenterAdapter;
    private RecyclerView recycle_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rebate_apply);
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
        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);
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
//                Intent intent = new Intent(RebateApplyCenterActivity.this,PointApplyActivity.class);
//                intent.putExtra("Price",list.get(postion).Price);
//                intent.putExtra("StateName",list.get(postion).StateName);
//                intent.putExtra("Point",list.get(postion).Point);
//                intent.putExtra("State",list.get(postion).State);
//                intent.putExtra("Number",list.get(postion).Number);
//                startActivity(intent);Fffff
                Intent intent = new Intent(RebateApplyCenterActivity.this,PointApplyDeatilActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) list);
                bundle.putInt("postion",postion);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        /**
         * 长按删除操作
         */
//        rebateApplyCenterAdapter.setLongClickListener(new RebateApplyCenterAdapter.OnLongClickListener() {
//            @Override
//            public void onLongClick(int postion, List<RebateApplyCenterBean.MainData> list) {
//                deleteData(postion,list);
//            }
//        });
        /**
         * iten删除按钮
         */
        rebateApplyCenterAdapter.setViewClickDeleteListener(new RebateApplyCenterAdapter.OnItenClickDeleteListener() {
            @Override
            public void onViewClickDelete(int postion, List<RebateApplyCenterBean.MainData> list) {
                deleteData(postion,list);
            }
        });
    }

    /**
     * data
     */
    private void initData() {
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
        WebRequestHelper.json_post(RebateApplyCenterActivity.this, URLText.REBATEAPPLY, RequestParamsPool.getRebateApplyData(), new MyAsyncHttpResponseHandler(RebateApplyCenterActivity.this) {
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
                } catch (Exception e) {
                    e.printStackTrace();
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
        WebRequestHelper.json_post(RebateApplyCenterActivity.this, URLText.DELETE_REBATEDATA, RequestParamsPool.deleteRebateData(id), new MyAsyncHttpResponseHandler(RebateApplyCenterActivity.this) {
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
                        Toast.makeText(RebateApplyCenterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}