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
import com.ximai.savingsmore.save.adapter.BussPressAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.BeanPressBean;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2018/3/22 0022.
 * 商户表现
 */

public class BussinsExpressActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RelativeLayout back;
    private EditText search_et_inputs;
    private RelativeLayout rl_clearinputs;
    private TextView tv_userdata;
    private LinearLayout ll_defaultdata;
    private Button btn_cancel;
    private Button btn_push;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recycle_view;
    private Boolean isRefreshing = false;
    private KyLoadingBuilder builder;
    private int page = 1;
    private int pageSize = 20;
    private Boolean IsNameDesc = null, IsOnlinePayDesc = null, IsLinePayDesc = null, IsFavouriteDesc = null, IsSharedDesc = null, IsHitDesc = null;
    private List<BeanPressBean.MainData> mainData;
    private List<BeanPressBean.MainData> list = new ArrayList<>();

    private TextView tv_linipay;
    private TextView tv_mendianpay;
    private TextView tv_collect;
    private TextView tv_shark;
    private TextView tv_through;
    private BussPressAdapter bussPressAdapter;
    private LinearLayoutManager mLayoutManager;
    private int lastVisibleItem;

    private int isHaveData1;
    private int isHaveData2;
    private int isHaveData3;
    private int isHaveData4;
    private int isHaveData5;
    private int isHaveData6;
    private ImageView iv_spmc;
    private ImageView iv_zxzf;
    private ImageView iv_mdzf;
    private ImageView iv_sc;
    private ImageView iv_fx;
    private ImageView iv_ll;

    private Boolean IsAllSelect1 = false;
    private Boolean IsAllSelect2 = false;
    private Boolean IsAllSelect3 = false;
    private Boolean IsAllSelect4 = false;
    private Boolean IsAllSelect5 = false;
    private Boolean IsAllSelect6 = false;
    private RelativeLayout rl_spmc;
    private RelativeLayout rl_zxzf;
    private RelativeLayout rl_mdzf;
    private RelativeLayout rl_sc;
    private RelativeLayout rl_fx;
    private RelativeLayout rl_ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businestexpress);

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

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.good_refresh);//刷新
        recycle_view = (RecyclerView) findViewById(R.id.recycle_view);//recycleview

        iv_spmc = (ImageView) findViewById(R.id.iv_spmc);
        iv_zxzf = (ImageView) findViewById(R.id.iv_zxzf);
        iv_mdzf = (ImageView) findViewById(R.id.iv_mdzf);
        iv_sc = (ImageView) findViewById(R.id.iv_sc);
        iv_fx = (ImageView) findViewById(R.id.iv_fx);
        iv_ll = (ImageView) findViewById(R.id.iv_ll);

        rl_spmc = (RelativeLayout) findViewById(R.id.rl_spmc);
        rl_zxzf = (RelativeLayout) findViewById(R.id.rl_zxzf);
        rl_mdzf = (RelativeLayout) findViewById(R.id.rl_mdzf);
        rl_sc = (RelativeLayout) findViewById(R.id.rl_sc);
        rl_fx = (RelativeLayout) findViewById(R.id.rl_fx);
        rl_ll = (RelativeLayout) findViewById(R.id.rl_ll);
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        rl_clearinputs.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_push.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        rl_spmc.setOnClickListener(this);
        rl_zxzf.setOnClickListener(this);
        rl_mdzf.setOnClickListener(this);
        rl_sc.setOnClickListener(this);
        rl_fx.setOnClickListener(this);
        rl_ll.setOnClickListener(this);

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
                        Toast.makeText(BussinsExpressActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        bussPressAdapter.notifyDataSetChanged();
                        getBussExpressData(page,1000,null,null,null,null,null,true,search_et_inputs.getText().toString());
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
                        Toast.makeText(BussinsExpressActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        bussPressAdapter.notifyDataSetChanged();
                        getBussExpressData(page,1000,null,null,null,null,null,true,search_et_inputs.getText().toString());
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
                        Toast.makeText(BussinsExpressActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        bussPressAdapter.notifyDataSetChanged();
                        getBussExpressData(page,1000,null,null,null,null,null,true,search_et_inputs.getText().toString());
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
                        Toast.makeText(BussinsExpressActivity.this, "请输入搜索关键字", Toast.LENGTH_SHORT).show();
                    }else{
                        page = 1;
                        list.clear();
                        bussPressAdapter.notifyDataSetChanged();
                        getBussExpressData(page,1000,null,null,null,null,null,true,search_et_inputs.getText().toString());
                    }
                    return true;
                }
                return false;
            }
        });

        /**
         * iten点击事件1
         */
        bussPressAdapter.setViewClickListener1(new BussPressAdapter.OnItemClickListener1() {
            @Override
            public void onViewClcik1(int postion, List<BeanPressBean.MainData> list) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == postion) {
                        if (list.get(i).IsSelect1) {
                            list.get(i).IsSelect1 = false;
                            IsAllSelect1 = false;
                            isHaveData1++;
                        } else {
                            list.get(i).IsSelect1 = true;
                            IsAllSelect1 = true;
                            isHaveData1--;
                        }
                    }
                }
                bussPressAdapter.notifyDataSetChanged();

                if (0 == isHaveData1){
                    iv_spmc.setVisibility(View.GONE);
                    if (isHaveData2 ==0 && isHaveData3 ==0 && isHaveData4 ==0 && isHaveData5 ==0 && isHaveData6 ==0) {
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }else{
                    iv_spmc.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }
            }
        });

        /**
         * iten点击事2
         */
        bussPressAdapter.setViewClickListener2(new BussPressAdapter.OnItemClickListener2() {
            @Override
            public void onViewClcik2(int postion, List<BeanPressBean.MainData> list) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == postion) {
                        if (list.get(i).IsSelect2) {
                            list.get(i).IsSelect2 = false;
                            IsAllSelect2 = false;
                            isHaveData2++;
                        } else {
                            list.get(i).IsSelect2 = true;
                            IsAllSelect2 = true;
                            isHaveData2--;
                        }
                    }
                }
                bussPressAdapter.notifyDataSetChanged();

                if (0 == isHaveData2){
                    iv_zxzf.setVisibility(View.GONE);
                    if (isHaveData1 ==0 && isHaveData3 ==0 && isHaveData4 ==0 && isHaveData5 ==0 && isHaveData6 ==0){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }else{
                    iv_zxzf.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }
            }
        });

        /**
         * iten点击事3
         */
        bussPressAdapter.setViewClickListener3(new BussPressAdapter.OnItemClickListener3() {
            @Override
            public void onViewClcik3(int postion, List<BeanPressBean.MainData> list) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == postion) {
                        if (list.get(i).IsSelect3) {
                            list.get(i).IsSelect3 = false;
                            IsAllSelect3 = false;
                            isHaveData3++;
                        } else {
                            list.get(i).IsSelect3 = true;
                            IsAllSelect3 = true;
                            isHaveData3--;
                        }
                    }
                }
                bussPressAdapter.notifyDataSetChanged();

                if (0 == isHaveData3){
                    iv_mdzf.setVisibility(View.GONE);
                    if (isHaveData1 ==0 && isHaveData2 ==0 && isHaveData4 ==0 && isHaveData5 ==0 && isHaveData6 ==0){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }else{
                    iv_mdzf.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }
            }
        });

        /**
         * iten点击事4
         */
        bussPressAdapter.setViewClickListener4(new BussPressAdapter.OnItemClickListener4() {
            @Override
            public void onViewClcik4(int postion, List<BeanPressBean.MainData> list) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == postion) {
                        if (list.get(i).IsSelect4) {
                            list.get(i).IsSelect4 = false;
                            IsAllSelect4 = false;
                            isHaveData4++;
                        } else {
                            list.get(i).IsSelect4 = true;
                            IsAllSelect4 = true;
                            isHaveData4--;
                        }
                    }
                }
                bussPressAdapter.notifyDataSetChanged();

                if (0 == isHaveData4){
                    iv_sc.setVisibility(View.GONE);
                    if (isHaveData1 ==0 && isHaveData2 ==0 && isHaveData3 ==0 && isHaveData5 ==0 && isHaveData6 ==0){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }else{
                    iv_sc.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }
            }
        });

        /**
         * iten点击事5
         */
        bussPressAdapter.setViewClickListener5(new BussPressAdapter.OnItemClickListener5() {
            @Override
            public void onViewClcik5(int postion, List<BeanPressBean.MainData> list) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == postion) {
                        if (list.get(i).IsSelect5) {
                            list.get(i).IsSelect5 = false;
                            IsAllSelect5 = false;
                            isHaveData5++;
                        } else {
                            list.get(i).IsSelect5 = true;
                            IsAllSelect5 = true;
                            isHaveData5--;
                        }
                    }
                }
                bussPressAdapter.notifyDataSetChanged();

                if (0 == isHaveData5){
                    iv_fx.setVisibility(View.GONE);
                    if (isHaveData1 ==0 && isHaveData2 ==0 && isHaveData3 ==0 && isHaveData4 ==0 && isHaveData6 ==0){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }

                }else{
                    iv_fx.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }
            }
        });

        /**
         * iten点击事6
         */
        bussPressAdapter.setViewClickListener6(new BussPressAdapter.OnItemClickListener6() {
            @Override
            public void onViewClcik6(int postion, List<BeanPressBean.MainData> list) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == postion) {
                        if (list.get(i).IsSelect6) {
                            list.get(i).IsSelect6 = false;
                            IsAllSelect6 = false;
                            isHaveData6++;
                        } else {
                            list.get(i).IsSelect6 = true;
                            IsAllSelect6 = true;
                            isHaveData6--;
                        }
                    }
                }
                bussPressAdapter.notifyDataSetChanged();

                if (0 == isHaveData6){
                    iv_ll.setVisibility(View.GONE);
                    if (isHaveData1 ==0 && isHaveData2 ==0 && isHaveData3 ==0 && isHaveData5 ==0 && isHaveData5 ==0){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }else{
                    iv_ll.setVisibility(View.VISIBLE);
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

        mLayoutManager = new LinearLayoutManager(BussinsExpressActivity.this);
        recycle_view.setOnScrollListener(new MyOnScrollListener());
        recycle_view.setLayoutManager(mLayoutManager);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_red_light);

        /**
         * 获取商户表现数据
         */
        getBussExpressData(page,pageSize,null,null,null,null,null,true,null);


        bussPressAdapter = new BussPressAdapter(this);
        recycle_view.setAdapter(bussPressAdapter);
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
//                getBussExpressData(page,pageSize,IsNameDesc,IsOnlinePayDesc,IsLinePayDesc,IsFavouriteDesc,IsSharedDesc,IsHitDesc,null);
                getBussExpressData(page,pageSize,null,null,null,null,null,true,null);
                break;
            case R.id.btn_cancel://取消选项
                if (0 != isHaveData1 || 0 != isHaveData2 || 0 != isHaveData3 || 0 != isHaveData4 || 0 != isHaveData5 || 0 != isHaveData6){
                    IsAllSelect1 = false;
                    IsAllSelect2 = false;
                    IsAllSelect3 = false;
                    IsAllSelect4 = false;
                    IsAllSelect5 = false;
                    IsAllSelect6 = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect1 = false;
                        mainData.get(i).IsSelect2 = false;
                        mainData.get(i).IsSelect3 = false;
                        mainData.get(i).IsSelect4 = false;
                        mainData.get(i).IsSelect5 = false;
                        mainData.get(i).IsSelect6 = false;
                    }
                    isHaveData1 = 0;
                    isHaveData2 = 0;
                    isHaveData3 = 0;
                    isHaveData4 = 0;
                    isHaveData5 = 0;
                    isHaveData6 = 0;
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_spmc.setVisibility(View.GONE);
                    iv_zxzf.setVisibility(View.GONE);
                    iv_mdzf.setVisibility(View.GONE);
                    iv_sc.setVisibility(View.GONE);
                    iv_fx.setVisibility(View.GONE);
                    iv_ll.setVisibility(View.GONE);

                    btn_cancel.setBackgroundResource(R.drawable.button_gray);
                    btn_push.setBackgroundResource(R.drawable.button_gray);
                }
                break;
            case R.id.btn_push://一键推送
                if (0 != isHaveData1 || 0 != isHaveData2 || 0 != isHaveData3 || 0 != isHaveData4 || 0 != isHaveData5 || 0 != isHaveData6){
                    pushDialog();
                }
                break;
            case R.id.rl_spmc://商品名称
                if (false ==IsAllSelect1){//一个没有选中进行全选
                    IsAllSelect1 = true;
                    for (int i = 0; i < mainData.size(); i++) {//全选
                        mainData.get(i).IsSelect1 = true;
                    }
                    isHaveData1 = mainData.size();
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_spmc.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }else{
                    IsAllSelect1 = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect1 = false;
                    }
                    isHaveData1 = 0;
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_spmc.setVisibility(View.GONE);

                    if (IsAllSelect2 != true && IsAllSelect3 != true && IsAllSelect4 != true && IsAllSelect5 != true && IsAllSelect6 != true){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }
                break;
            case R.id.rl_zxzf://在线师傅
                if (false ==IsAllSelect2){//一个没有选中进行全选
                    IsAllSelect2 = true;
                    for (int i = 0; i < mainData.size(); i++) {//全选
                        mainData.get(i).IsSelect2 = true;
                    }
                    isHaveData2 = mainData.size();
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_zxzf.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }else{
                    IsAllSelect2 = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect2 = false;
                    }
                    isHaveData2 = 0;
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_zxzf.setVisibility(View.GONE);
                    if (IsAllSelect1 != true && IsAllSelect3 != true && IsAllSelect4 != true && IsAllSelect5 != true && IsAllSelect6 != true){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }
                break;
            case R.id.rl_mdzf://门店支付
                if (false ==IsAllSelect3){//一个没有选中进行全选
                    IsAllSelect3 = true;
                    for (int i = 0; i < mainData.size(); i++) {//全选
                        mainData.get(i).IsSelect3 = true;
                    }
                    isHaveData3 = mainData.size();
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_mdzf.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }else{
                    IsAllSelect3 = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect3 = false;
                    }
                    isHaveData3 = 0;
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_mdzf.setVisibility(View.GONE);
                    if (IsAllSelect1 != true && IsAllSelect2 != true && IsAllSelect4 != true && IsAllSelect5 != true && IsAllSelect6 != true){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }
                break;
            case R.id.rl_sc://收藏
                if (false ==IsAllSelect4){//一个没有选中进行全选
                    IsAllSelect4 = true;
                    for (int i = 0; i < mainData.size(); i++) {//全选
                        mainData.get(i).IsSelect4 = true;
                    }
                    isHaveData4 = mainData.size();
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_sc.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }else{
                    IsAllSelect4 = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect4 = false;
                    }
                    isHaveData4 = 0;
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_sc.setVisibility(View.GONE);
                    if (IsAllSelect1 != true && IsAllSelect2 != true && IsAllSelect3 != true && IsAllSelect5 != true && IsAllSelect6 != true){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }
                break;
            case R.id.rl_fx://分享
                if (false ==IsAllSelect5){//一个没有选中进行全选
                    IsAllSelect5 = true;
                    for (int i = 0; i < mainData.size(); i++) {//全选
                        mainData.get(i).IsSelect5 = true;
                    }
                    isHaveData5 = mainData.size();
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_fx.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }else{
                    IsAllSelect5 = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect5 = false;
                    }
                    isHaveData5 = 0;
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_fx.setVisibility(View.GONE);
                    if (IsAllSelect1 != true && IsAllSelect2 != true && IsAllSelect3 != true && IsAllSelect4 != true && IsAllSelect6 != true){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }
                break;
            case R.id.rl_ll://浏览
                if (false ==IsAllSelect6){//一个没有选中进行全选
                    IsAllSelect6 = true;
                    for (int i = 0; i < mainData.size(); i++) {//全选
                        mainData.get(i).IsSelect6 = true;
                    }
                    isHaveData6 = mainData.size();
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_ll.setVisibility(View.VISIBLE);
                    btn_cancel.setBackgroundResource(R.drawable.button_sharp);
                    btn_push.setBackgroundResource(R.drawable.button_blue);
                }else{
                    IsAllSelect6 = false;
                    for (int i = 0; i < mainData.size(); i++) {//全不选
                        mainData.get(i).IsSelect6 = false;
                    }
                    isHaveData6 = 0;
                    list.addAll(mainData);
                    bussPressAdapter.notifyDataSetChanged();

                    iv_ll.setVisibility(View.GONE);
                    if (IsAllSelect1 != true && IsAllSelect2 != true && IsAllSelect3 != true && IsAllSelect4 != true && IsAllSelect5 != true){
                        btn_cancel.setBackgroundResource(R.drawable.button_gray);
                        btn_push.setBackgroundResource(R.drawable.button_gray);
                    }
                }
                break;
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

    /**
     * 获取用户表现数据--,Boolean IsNameDesc, Boolean IsOnlinePayDesc, Boolean IsLinePayDesc, Boolean IsFavouriteDesc,Boolean IsSharedDesc,Boolean IsHitDesc
     */
    private void getBussExpressData(int PageNo,int PageSize,Boolean IsNameDesc,Boolean IsOnlinePayDesc,Boolean IsLinePayDesc,Boolean IsFavouriteDesc,Boolean IsSharedDesc,Boolean IsHitDesc,String Keyword) {
        isRefreshing = true;
        swipeRefreshLayout.setRefreshing(isRefreshing);
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(this, URLText.BUSS_EXPRESS, RequestParamsPool.getBussExpressData(PageNo,PageSize,IsNameDesc,IsOnlinePayDesc,IsLinePayDesc,IsFavouriteDesc,IsSharedDesc,IsHitDesc,Keyword), new MyAsyncHttpResponseHandler(this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    BeanPressBean beanPressBean = GsonUtils.fromJson(new String(responseBody), BeanPressBean.class);
                    mainData = beanPressBean.MainData;

                    for (int i = 0; i < mainData.size(); i++) {//默认将数据设置false
                        mainData.get(i).IsSelect1 = false;
                        mainData.get(i).IsSelect2 = false;
                        mainData.get(i).IsSelect3 = false;
                        mainData.get(i).IsSelect4 = false;
                        mainData.get(i).IsSelect5 = false;
                        mainData.get(i).IsSelect6 = false;
                    }

                    tv_linipay.setText(beanPressBean.ShowData.OnlinePay);
                    tv_mendianpay.setText(beanPressBean.ShowData.LinePay);
                    tv_collect.setText(beanPressBean.ShowData.Favourite);
                    tv_shark.setText(beanPressBean.ShowData.Shared);
                    tv_through.setText(beanPressBean.ShowData.Hit);

                    tv_userdata.setText(split(beanPressBean.TotalRecordCount));//用户总人数

//                    list.clear();//这里数据不可以进行清除 - 发现每次只加载10个
                    list.addAll(mainData);

                    if (0 == list.size()){
                        recycle_view.setVisibility(View.GONE);
                        ll_defaultdata.setVisibility(View.VISIBLE);
                    }else{
                        recycle_view.setVisibility(View.VISIBLE);
                        ll_defaultdata.setVisibility(View.GONE);
                        bussPressAdapter.setData(list);
                        bussPressAdapter.notifyDataSetChanged();
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
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        list.clear();
        bussPressAdapter.notifyDataSetChanged();
        if (!isRefreshing) {
            if (TextUtils.isEmpty(search_et_inputs.getText())){
                getBussExpressData(page,pageSize,null,null,null,null,null,true,null);
            }else{
                page = 1;
                list.clear();
                bussPressAdapter.notifyDataSetChanged();
                getBussExpressData(page,pageSize,null,null,null,null,null,true,search_et_inputs.getText().toString());
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
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == bussPressAdapter.getItemCount()&&bussPressAdapter.getItemCount()>4) {
                page++;
                if (TextUtils.isEmpty(search_et_inputs.getText())){
                    getBussExpressData(page,pageSize,null,null,null,null,null,true,null);
                }else{
                    page = 1;
                    list.clear();
                    bussPressAdapter.notifyDataSetChanged();
                    getBussExpressData(page,pageSize,null,null,null,null,null,true,search_et_inputs.getText().toString());
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