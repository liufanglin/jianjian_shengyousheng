package com.ximai.savingsmore.save.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.adapter.BusinessRewardAdapter;
import com.ximai.savingsmore.save.adapter.PersonalRewardAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.PersonalRewardBean;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2018/1/26 0026.
 * 商家奖赏中心
 */

public class BusinessRewardActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout back;
    private LinearLayout ll_aliwithdraw;
    private LinearLayout ll_cardwithdraw;
    private LinearLayout ll_defaultdata;
    private RecyclerView recycle_view;
    private PersonalRewardBean personalRewardBean;
    private List<PersonalRewardBean.MainData> rewardList;
    private BusinessRewardAdapter businessRewardAdapter;
    private TextView tv_todaymoney;
    private TextView tv_bigmoney;
    private List<String> IdList = new ArrayList<>();//存储打开的红包的Id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_reward);
        initView();
        initData();
        initEvent();
    }

    /**
     * view
     */
    private void initView() {
        /**
         * 将标题隐藏
         */
        toolbar.setVisibility(View.GONE);
        back = (RelativeLayout) findViewById(R.id.back);

        ll_aliwithdraw = (LinearLayout) findViewById(R.id.ll_aliwithdraw);//支付宝提现
        ll_cardwithdraw = (LinearLayout) findViewById(R.id.ll_cardwithdraw);//银行卡提现
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);
        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);//recycleview
        tv_todaymoney = (TextView) findViewById(R.id.tv_todaymoney);//今日金额
        tv_bigmoney = (TextView) findViewById(R.id.tv_bigmoney);//总计金额
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
        ll_aliwithdraw.setOnClickListener(this);
        ll_cardwithdraw.setOnClickListener(this);

        /**
         * 打开红包
         */
        businessRewardAdapter.setOnClickListener(new BusinessRewardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, List<PersonalRewardBean.MainData> rewardList) {
                if ("1".equals(rewardList.get(position).State)){//如果是未打开的才可以进行打开操作
                    playReward(rewardList.get(position).Id,null,position);
                }else if ("5".equals(rewardList.get(position).State)){//驳回
                    bohui();
                }else if ("3".equals(rewardList.get(position).State)){//红包正咋申请中
                    applyReward();
                }
            }
        });
    }

    /**
     * data
     */
    private void initData() {
        businessRewardAdapter = new BusinessRewardAdapter(this);
        recycle_view.setAdapter(businessRewardAdapter);
//        /**
//         * 获取红包列表
//         */
//        getRewardData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 获取红包列表
         */
        getRewardData();
    }

    /**
     * 事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.ll_aliwithdraw://支付宝提现
                if (null != rewardList){
                    if (Double.parseDouble(personalRewardBean.ShowData) >0){
                        IdList.clear();
                        for (int i = 0; i < rewardList.size(); i++) {
                            if ("2".equals(rewardList.get(i).State)){
                                IdList.add(rewardList.get(i).Id);
                            }
                        }
                        Intent intent = new Intent(this,AliWithdrawActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("money",personalRewardBean.ShowData);
                        bundle.putSerializable("idList", (Serializable) IdList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        Toast.makeText(this, "您的今日奖赏金额为0，不可提现。", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.ll_cardwithdraw://银行卡提现
                if (Double.parseDouble(personalRewardBean.ShowData) >0){
                    if (null != rewardList){
                        IdList.clear();
                        for (int i = 0; i < rewardList.size(); i++) {
                            if ("2".equals(rewardList.get(i).State)){
                                IdList.add(rewardList.get(i).Id);
                            }
                        }
                        Intent intent = new Intent(this,CardWithdrawActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("money",personalRewardBean.ShowData);
                        bundle.putSerializable("idList", (Serializable) IdList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }else{
                    Toast.makeText(this, "您的今日奖赏金额为0，不可提现。", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
        }
    }

    /**
     * 获取红包数据
     */
    private void getRewardData() {
        WebRequestHelper.json_post(this, URLText.GET_REWADRDATA, RequestParamsPool.getRewardData(), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    personalRewardBean = GsonUtils.fromJson(object.toString(), PersonalRewardBean.class);
                    //今日金额和累计金额
                    tv_todaymoney.setText("¥" + UIUtils.formatPrice(Double.parseDouble(personalRewardBean.ShowData)));
                    tv_bigmoney.setText("¥" +UIUtils.formatPrice(Double.parseDouble(personalRewardBean.OtherData)));

                    rewardList = personalRewardBean.MainData;
                    if (0 == rewardList.size()) {
                        ll_defaultdata.setVisibility(View.VISIBLE);//暂无数据
                        recycle_view.setVisibility(View.GONE);
                    } else {
                        businessRewardAdapter.setDdata(rewardList);
                        businessRewardAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 刷新今日金额和奖赏累计
     */
    private void updataMoney() {
        WebRequestHelper.json_post(this, URLText.GET_REWADRDATA, RequestParamsPool.getRewardData(), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    personalRewardBean = GsonUtils.fromJson(object.toString(), PersonalRewardBean.class);
                    //今日金额和累计金额
                    tv_todaymoney.setText("¥" + UIUtils.formatPrice(Double.parseDouble(personalRewardBean.ShowData)));
                    tv_bigmoney.setText("¥" +UIUtils.formatPrice(Double.parseDouble(personalRewardBean.OtherData)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 打开红包
     */
    private void playReward(String Id, String Remark, final int position) {
        WebRequestHelper.json_post(this, URLText.PLAY_REWARD, RequestParamsPool.playReward(Id,Remark), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    Boolean isSuccess = object.optBoolean("IsSuccess");
                    String message = object.optString("Message");
                    if (true == isSuccess){
                        Toast.makeText(BusinessRewardActivity.this, "打开成功", Toast.LENGTH_SHORT).show();
                        //在这里进行红包打开的操作 - 不进行列表的刷新的 - 直接让item进行变换
                        rewardList.get(position).setOpen(true);
                        businessRewardAdapter.notifyItemChanged(position);
                        //更新价格
                        updataMoney();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

//    /**
//     * 状态 - 3
//     */
//    public void apply(){
//        DialogCallBack callBack = new DialogCallBack() {
//            @Override
//            public void OkDown(Dialog dialog) {
//                dialog.cancel();
//                dialog.dismiss();
//            }
//            @Override
//            public void CancleDown(Dialog dialog) {
//                dialog.cancel();
//            }
//        };
//        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "正在审核中，请稍后", "确认", R.style.CustomDialog_1, callBack, 2);
//        dialog.setCanceledOnTouchOutside(true);
//        dialog.show();
//    }

    /**
     * 状态 - 红包提现申请正在审核中
     */
    public void applyReward(){
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
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "提现申请正在审核中，请稍后", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 状态 - 5
     */
    public void bohui(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
//                initCallPhone("02158366991");
                call("02158366991");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "温馨提示", "您的申请被驳回，请联系我们 021-58366991。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
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
     *
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
                        com.yanzhenjie.alertdialog.AlertDialog.newBuilder(BusinessRewardActivity.this)
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
                        if (AndPermission.hasAlwaysDeniedPermission(BusinessRewardActivity.this, deniedPermissions)) {
                            AndPermission.defaultSettingDialog(BusinessRewardActivity.this, 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        if (requestCode == 200) {// Failure.
                            Toast.makeText(BusinessRewardActivity.this, "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            System.out.println("call phone error");
            e.printStackTrace();
        }
    }
}