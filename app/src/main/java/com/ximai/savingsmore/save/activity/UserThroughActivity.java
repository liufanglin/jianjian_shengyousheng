package com.ximai.savingsmore.save.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.ximai.savingsmore.save.adapter.UserThroughAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.fragment.SharkDataLeftFrament;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.UserThroughBean;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by luck on 2018/3/20 0020.
 * 省又省用户群
 */

public class UserThroughActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RelativeLayout back;
    private EditText search_et_inputs;
    private RelativeLayout rl_clearinputs;
    private TextView tv_userdata;
    private LinearLayout ll_defaultdata;
    private Button btn_user;
    private Button btn_business;
    private RecyclerView recycle_view;
    private UserThroughAdapter userThroughAdapter;
    private KyLoadingBuilder builder;
    private RelativeLayout rl_sexsort;
    private RelativeLayout rl_addresssort;
    private RelativeLayout rl_timesort;

    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page = 1;
    private int pageSize = 20;
    private List<UserThroughBean.MainData> mainData;
    private List<UserThroughBean.MainData> list = new ArrayList<>();
    private Boolean isRefreshing = false;
    private Boolean IsNumberDesc = false, IsSexDesc = false, IsAddressDesc = false, IsJoinTimeDesc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userthrough);

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
        btn_user = (Button) findViewById(R.id.btn_user);//用户
        btn_business = (Button) findViewById(R.id.btn_business);//商家
        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);//recycleview
        rl_sexsort = (RelativeLayout) findViewById(R.id.rl_sexsort);//性别排序
        rl_addresssort = (RelativeLayout) findViewById(R.id.rl_addresssort);//地址排序
        rl_timesort = (RelativeLayout) findViewById(R.id.rl_timesort);//时间
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.good_refresh);
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        rl_clearinputs.setOnClickListener(this);
        btn_user.setOnClickListener(this);
        btn_business.setOnClickListener(this);
        rl_sexsort.setOnClickListener(this);
        rl_addresssort.setOnClickListener(this);
        rl_timesort.setOnClickListener(this);
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
                        Toast.makeText(UserThroughActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        userThroughAdapter.notifyDataSetChanged();
                        IsNumberDesc = null;
                        IsSexDesc = null;
                        IsAddressDesc = null;
                        IsJoinTimeDesc = null;
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
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
                        Toast.makeText(UserThroughActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        userThroughAdapter.notifyDataSetChanged();
                        IsNumberDesc = null;
                        IsSexDesc = null;
                        IsAddressDesc = null;
                        IsJoinTimeDesc = null;
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
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
                        Toast.makeText(UserThroughActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        userThroughAdapter.notifyDataSetChanged();
                        IsNumberDesc = null;
                        IsSexDesc = null;
                        IsAddressDesc = null;
                        IsJoinTimeDesc = null;
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
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
                        Toast.makeText(UserThroughActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        userThroughAdapter.notifyDataSetChanged();
                        IsNumberDesc = null;
                        IsSexDesc = null;
                        IsAddressDesc = null;
                        IsJoinTimeDesc = null;
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * data
     */
    private void initData() {
        mLayoutManager = new LinearLayoutManager(UserThroughActivity.this);
        recycle_view.setOnScrollListener(new MyOnScrollListener());
        recycle_view.setLayoutManager(mLayoutManager);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        /**
         * 获取所有用户数据
         */
        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);

        userThroughAdapter = new UserThroughAdapter(this);
        recycle_view.setAdapter(userThroughAdapter);
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
                /**
                 * 获取所有用户数据
                 */
                getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
                break;
            case R.id.btn_user://用户表现
                Intent intent1 = new Intent(this, UserExpressActivity.class);
                startActivity(intent1);
                break;
            case R.id.btn_business://商品表现
                Intent intent2 = new Intent(this, BussinsExpressActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_sexsort:
                if (IsSexDesc == null) {
                    IsSexDesc = false;
                }
                page = 1;
                list.clear();
                userThroughAdapter.notifyDataSetChanged();
                if ( false == IsSexDesc){
                    IsNumberDesc = null;
                    IsSexDesc = true;
                    IsAddressDesc = null;
                    IsJoinTimeDesc = null;

                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
                    }else{
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
                    }
                }else{
                    IsNumberDesc = null;
                    IsSexDesc = false;
                    IsAddressDesc = null;
                    IsJoinTimeDesc = null;
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
                    }else{
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
                    }
                }
                break;
            case R.id.rl_addresssort:
                if (IsAddressDesc == null) {
                    IsAddressDesc = false;
                }
                page = 1;
                list.clear();
                userThroughAdapter.notifyDataSetChanged();
                if ( false == IsAddressDesc){
                    IsNumberDesc = null;
                    IsSexDesc = null;
                    IsAddressDesc = true;
                    IsJoinTimeDesc = null;
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
                    }else{
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
                    }
                }else{
                    IsNumberDesc = null;
                    IsSexDesc = null;
                    IsAddressDesc = false;
                    IsJoinTimeDesc = null;
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
                    }else{
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
                    }
                }
                break;
            case R.id.rl_timesort:
                if (IsJoinTimeDesc == null) {
                    IsJoinTimeDesc = false;
                }
                page = 1;
                list.clear();
                userThroughAdapter.notifyDataSetChanged();
                if ( false == IsJoinTimeDesc){
                    IsNumberDesc = null;
                    IsSexDesc = null;
                    IsAddressDesc = null;
                    IsJoinTimeDesc = true;
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
                    }else{
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
                    }
                }else{
                    IsNumberDesc = null;
                    IsSexDesc = null;
                    IsAddressDesc = null;
                    IsJoinTimeDesc = false;
                    if (TextUtils.isEmpty(search_et_inputs.getText())){
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
                    }else{
                        getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
                    }
                }
                break;
        }
    }

    /**
     * 获取用户群
     */
    private void getUserGroup(int PageNo,int PageSize,Boolean IsNumberDesc, Boolean IsSexDesc, Boolean IsAddressDesc, Boolean IsJoinTimeDesc,String Keyword) {
        isRefreshing = true;
        swipeRefreshLayout.setRefreshing(isRefreshing);
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(this, URLText.BUSINESS_USERGROUP, RequestParamsPool.getUserGroup(PageNo,PageSize,IsNumberDesc, IsSexDesc, IsAddressDesc, IsJoinTimeDesc,Keyword), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    UserThroughBean userThroughBean = GsonUtils.fromJson(new String(responseBody), UserThroughBean.class);
                    mainData = userThroughBean.MainData;
//                    list.clear();//这里数据不可以进行清除 - 发现每次只加载10个
                    list.addAll(mainData);
                    if (0 == list.size()){
                        recycle_view.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.VISIBLE);
                    }else{
                        recycle_view.setVisibility(View.VISIBLE);
                        ll_defaultdata.setVisibility(View.GONE);
                        userThroughAdapter.setData(list);
                        userThroughAdapter.notifyDataSetChanged();
                    }

                    tv_userdata.setText(split(userThroughBean.TotalRecordCount));

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
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        list.clear();
        userThroughAdapter.notifyDataSetChanged();
        if (!isRefreshing) {
            if (TextUtils.isEmpty(search_et_inputs.getText())){
                getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
            }else{
                getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
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
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == userThroughAdapter.getItemCount()&&userThroughAdapter.getItemCount()>4) {
                page++;
                if (TextUtils.isEmpty(search_et_inputs.getText())){
                    getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,null);
                }else{
                    getUserGroup(page,pageSize,IsNumberDesc,IsSexDesc,IsAddressDesc,IsJoinTimeDesc,search_et_inputs.getText().toString());
                }
            }
        }
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
}