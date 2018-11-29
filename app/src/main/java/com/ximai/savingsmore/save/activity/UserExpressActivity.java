package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.ximai.savingsmore.save.adapter.UserPressAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.UserPressBean;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by luck on 2018/3/22 0022.
 * 用户表现
 */

public class UserExpressActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RelativeLayout back;
    private EditText search_et_inputs;
    private RelativeLayout rl_clearinputs;
    private TextView tv_userdata;
    private LinearLayout ll_defaultdata;
    private Button btn_cancel;
    private Button btn_push;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycle_view;
    private int page = 1;
    private int pageSize = 20;
    private Boolean IsNameDesc = null, IsOnlinePayDesc = null, IsLinePayDesc = null, IsFavouriteDesc = null, IsSharedDesc = null, IsHitDesc = null;
    private List<UserPressBean.MainData> mainData;
    private List<UserPressBean.MainData> list = new ArrayList<>();
    private UserPressAdapter userPressAdapter;
    private TextView tv_linipay;
    private TextView tv_mendianpay;
    private TextView tv_collect;
    private TextView tv_shark;
    private TextView tv_through;
    private Boolean isRefreshing = false;
    private Boolean IsAllSelect = false;
    private KyLoadingBuilder builder;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private ImageView iv_chooses;
    private int isHaveData;
    private RelativeLayout rl_vipname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertexpress);

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
        search_et_inputs = (EditText) findViewById(R.id.search_et_inputs);//用户输入
        rl_clearinputs = (RelativeLayout) findViewById(R.id.rl_clearinputs);//叉号
        tv_userdata = (TextView) findViewById(R.id.tv_userdata);//客户总人数
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);//默认缺省
        btn_cancel = (Button) findViewById(R.id.btn_cancel);//取消选项
        btn_push = (Button) findViewById(R.id.btn_push);//一键推送

        tv_linipay = (TextView) findViewById(R.id.tv_linipay);
        tv_mendianpay = (TextView) findViewById(R.id.tv_mendianpay);
        tv_collect = (TextView) findViewById(R.id.tv_collect);
        tv_shark = (TextView) findViewById(R.id.tv_shark);
        tv_through = (TextView) findViewById(R.id.tv_through);
        rl_vipname = (RelativeLayout) findViewById(R.id.rl_vipname);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.good_refresh);//刷新
        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);//recycleview
        iv_chooses = (ImageView) findViewById(R.id.iv_chooses);
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        rl_clearinputs.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_push.setOnClickListener(this);
        rl_vipname.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        /**
         * 输入框完成，对软键盘的监听
         */
        search_et_inputs.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                 /*判断是否是“GO”键*/
                if(actionId == EditorInfo.IME_ACTION_GO){//2
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        Toast.makeText(UserExpressActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        userPressAdapter.notifyDataSetChanged();
                        getUserExpressData(page,pageSize,null,null,null,null,null,true,search_et_inputs.getText().toString());
                    }
                    return true;
                }
                if(actionId == EditorInfo.IME_ACTION_DONE){//6
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        Toast.makeText(UserExpressActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        userPressAdapter.notifyDataSetChanged();
                        getUserExpressData(page,pageSize,null,null,null,null,null,true,search_et_inputs.getText().toString());
                    }
                    return true;
                }
                if(actionId == EditorInfo.IME_ACTION_SEARCH){//3
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        Toast.makeText(UserExpressActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        userPressAdapter.notifyDataSetChanged();
                        getUserExpressData(page,pageSize,null,null,null,null,null,true,search_et_inputs.getText().toString());
                    }
                    return true;
                }
                if(actionId == EditorInfo.IME_ACTION_NEXT){//5
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        Toast.makeText(UserExpressActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        userPressAdapter.notifyDataSetChanged();
                        getUserExpressData(page,pageSize,null,null,null,null,null,true,search_et_inputs.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
        /**
         * 会员名点击事件
         */
        userPressAdapter.setViewClickListener(new UserPressAdapter.OnItemClickListener() {
            @Override
            public void onViewClcik(int postion, List<UserPressBean.MainData> list, ImageView iv_choose) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == postion) {
                        if (list.get(i).IsSelect) {
                            list.get(i).IsSelect = false;
                            IsAllSelect = false;
                            isHaveData++;
                        } else {
                            list.get(i).IsSelect = true;
                            IsAllSelect = true;
                            isHaveData--;
                        }
                    }
                }
                userPressAdapter.notifyDataSetChanged();

                if (0 == isHaveData){
                    iv_chooses.setVisibility(View.GONE);
                    btn_cancel.setBackgroundResource(R.drawable.button_gray);
                    btn_push.setBackgroundResource(R.drawable.button_gray);
                }else{
                    iv_chooses.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }
            }
        });
    }

    /**
     * data
     */
    private void initData() {
        mLayoutManager = new LinearLayoutManager(UserExpressActivity.this);
        recycle_view.setOnScrollListener(new MyOnScrollListener());
        recycle_view.setLayoutManager(mLayoutManager);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        /**
         * 获取用户表现数据
         */
        getUserExpressData(page,pageSize,null,null,null,null,null,true,null);

        userPressAdapter = new UserPressAdapter(this);
        recycle_view.setAdapter(userPressAdapter);
    }

    /**
     * onclick
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rl_clearinputs:
                page = 1;
                list.clear();
                search_et_inputs.setText("");
                getUserExpressData(page,pageSize,null,null,null,null,null,true,null);
                break;
            case R.id.btn_cancel://取消选项
                if (0 != isHaveData){
                    IsAllSelect = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect = false;
                    }
                    isHaveData = 0;
                    list.addAll(mainData);
                    userPressAdapter.notifyDataSetChanged();

                    iv_chooses.setVisibility(View.GONE);
                    btn_cancel.setBackgroundResource(R.drawable.button_gray);
                    btn_push.setBackgroundResource(R.drawable.button_gray);
                }
                break;
            case R.id.btn_push://一键推送
                if (0 != isHaveData){
                    pushDialog();
                }
                break;
            case R.id.rl_vipname://会员名称
                if (false ==IsAllSelect){//一个没有选中进行全选
                    IsAllSelect = true;
                    for (int i = 0; i < mainData.size(); i++) {//全选
                        mainData.get(i).IsSelect = true;
                    }
                    isHaveData = mainData.size();
                    list.addAll(mainData);
                    userPressAdapter.notifyDataSetChanged();

                    iv_chooses.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }else{
                    IsAllSelect = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect = false;
                    }
                    isHaveData = 0;
                    list.addAll(mainData);
                    userPressAdapter.notifyDataSetChanged();

                    iv_chooses.setVisibility(View.GONE);
                    btn_cancel.setBackgroundResource(R.drawable.button_gray);
                    btn_push.setBackgroundResource(R.drawable.button_gray);
                }
                break;
        }
    }

    /**
     * 获取用户表现数据
     */
    private void getUserExpressData(int PageNo,int PageSize,Boolean IsNameDesc,Boolean IsOnlinePayDesc,Boolean IsLinePayDesc,Boolean IsFavouriteDesc,Boolean IsSharedDesc,Boolean IsHitDesc,String Keyword) {
        isRefreshing = true;
        swipeRefreshLayout.setRefreshing(isRefreshing);
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(this, URLText.USER_EXPRESS, RequestParamsPool.getUserExpressData(PageNo,PageSize,IsNameDesc,IsOnlinePayDesc,IsLinePayDesc,IsFavouriteDesc,IsSharedDesc,IsHitDesc,Keyword), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    UserPressBean userPressBean = GsonUtils.fromJson(new String(responseBody), UserPressBean.class);
                    mainData = userPressBean.MainData;

                    for (int i = 0; i < mainData.size(); i++) {//默认将数据设置false
                        mainData.get(i).IsSelect = false;
                    }

                    tv_linipay.setText(userPressBean.ShowData.OnlinePay);
                    tv_mendianpay.setText(userPressBean.ShowData.LinePay);
                    tv_collect.setText(userPressBean.ShowData.Favourite);
                    tv_shark.setText(userPressBean.ShowData.Shared);
                    tv_through.setText(userPressBean.ShowData.Hit);

                    tv_userdata.setText(split(userPressBean.TotalRecordCount));//用户总人数

//                    list.clear();//这里数据不可以进行清除 - 发现每次只加载10个
                    list.addAll(mainData);

                    if (0 == list.size()){
                        recycle_view.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.VISIBLE);
                    }else{
                        recycle_view.setVisibility(View.VISIBLE);
                        ll_defaultdata.setVisibility(View.GONE);
                        userPressAdapter.setData(list);
                        userPressAdapter.notifyDataSetChanged();
                    }

                    if (null != builder){
                        builder.dismiss();
                    }
                    isRefreshing = false;
                    swipeRefreshLayout.setRefreshing(isRefreshing);
                }catch (Exception e){
                    e.printStackTrace();

                    if (null != builder){
                        builder.dismiss();
                    }
                }finally {
                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
    }

    /**
     * 将金额三位划分
     * @param s
     * @return
     */
    private String split(String s){
        String s1 = new StringBuilder(s).reverse().toString();//先将字符串颠倒顺序
        String str2 = "";
        for(int i=0;i<s1.length();i++){
            if(i*3+3>s1.length()){
                str2 += s1.substring(i*3, s1.length());
                break;
            }
            str2 += s1.substring(i*3, i*3+3)+",";
        }
        if(str2.endsWith(",")){
            str2 = str2.substring(0, str2.length()-1);
        }
        return new StringBuilder(str2).reverse().toString();//进行一个反序
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

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        list.clear();
        userPressAdapter.notifyDataSetChanged();
        if (!isRefreshing) {
            if (TextUtils.isEmpty(search_et_inputs.getText())){
                getUserExpressData(page,pageSize,null,null,null,null,null,true,null);
            }else{
                getUserExpressData(page,pageSize,null,null,null,null,null,true,search_et_inputs.getText().toString());
            }
        }
    }

    /**
     * 分页加载
     */
    private class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            swipeRefreshLayout.setEnabled(((LinearLayoutManager) mLayoutManager).findFirstCompletelyVisibleItemPosition() == 0);
        }
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == userPressAdapter.getItemCount()&&userPressAdapter.getItemCount()>4) {
                page++;
                if (TextUtils.isEmpty(search_et_inputs.getText())){
                    getUserExpressData(page,pageSize,null,null,null,null,null,true,null);
                }else{
                    getUserExpressData(page,pageSize,null,null,null,null,null,true,search_et_inputs.getText().toString());
                }
            }
        }
    }

    /**
     * 一键推送
     */
    public void pushDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call("02138687133");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(this, "请联系 “省又省”客服", "021-38687133", "拨打", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}