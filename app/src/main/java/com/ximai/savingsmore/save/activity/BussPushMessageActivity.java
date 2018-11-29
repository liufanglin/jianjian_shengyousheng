package com.ximai.savingsmore.save.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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
import com.ximai.savingsmore.save.adapter.BussPushMessageAdapter;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.BUssPushMessageBean;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class BussPushMessageActivity extends BaseActivity implements View.OnClickListener, SwipeItemClickListener {

    private RelativeLayout back;
    private SwipeMenuRecyclerView recycle_view;
    private LinearLayout ll_defaultdata;
    private KyLoadingBuilder builder;
    private List<BUssPushMessageBean.MainData> mainData;
    private BussPushMessageAdapter bussPushMessageAdapter;
    private LinearLayoutManager mLayoutManager;
    private DefaultItemDecoration mItemDecoration;
    private int adapterPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buss_push_activity);

        initView();

        initData();

        initEvent();
    }

    private void initView() {
        /**
         * 将标题隐藏
         */
        toolbar.setVisibility(View.GONE);
        back = (RelativeLayout) findViewById(R.id.back);
        recycle_view = (SwipeMenuRecyclerView) findViewById(R.id.recycle_view);
        ll_defaultdata = (LinearLayout) findViewById(R.id.ll_defaultdata);
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
    }

    /**
     * data
     */
    private void initData() {
        mLayoutManager = new LinearLayoutManager(BussPushMessageActivity.this);//设置布局参数
        mItemDecoration = new DefaultItemDecoration(ContextCompat.getColor(BussPushMessageActivity.this, R.color.white));
        recycle_view.setLayoutManager(mLayoutManager);
        recycle_view.addItemDecoration(mItemDecoration);
        recycle_view.setSwipeItemClickListener(this);//设置Iten的点击事件
        recycle_view.setSwipeMenuCreator(swipeMenuCreator);//创建侧边栏
        recycle_view.setSwipeMenuItemClickListener(mMenuItemClickListener);//侧边栏点击事件

        bussPushMessageAdapter = new BussPushMessageAdapter(this);
        recycle_view.setAdapter(bussPushMessageAdapter);
        /**
         * 获取推送数据
         */
        getPushData();
    }

    /**
     * 获取数据
     */
    private void getPushData() {
        showLoading(this,"正在加载");
        WebRequestHelper.json_post(BussPushMessageActivity.this, URLText.BUSS_PUSH, RequestParamsPool.getPushData(), new MyAsyncHttpResponseHandler(BussPushMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String s = new String(responseBody);
                    BUssPushMessageBean bUssPushMessageBean = GsonUtils.fromJson(new String(responseBody), BUssPushMessageBean.class);
                    mainData = bUssPushMessageBean.MainData;
                    if (mainData.size() == 0){
                        ll_defaultdata.setVisibility(View.VISIBLE);
                        recycle_view.setVisibility(View.GONE);
                    }else{
                        ll_defaultdata.setVisibility(View.GONE);
                        recycle_view.setVisibility(View.VISIBLE);
                        bussPushMessageAdapter.setData(mainData);
                        bussPushMessageAdapter.notifyDataSetChanged();
                    }
                    if (null != builder){
                        builder.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if (null != builder){
                        builder.dismiss();
                    }
                }
            }
        });
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
     * click
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    /**
     * iten 的点击事件
     * @param itemView
     * @param position
     */
    @Override
    public void onItemClick(View itemView, int position) {

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
                SwipeMenuItem deleteItem = new SwipeMenuItem(BussPushMessageActivity.this)
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
            adapterPosition = menuBridge.getAdapterPosition();// RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                isYesDeletedialog();
            }
        }
    };

    /**
     * 是否确认删除订单
     */
    public void isYesDeletedialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                removeBussPushGoods(mainData.get(adapterPosition).Id);//删除接口
                mainData.remove(adapterPosition);
                bussPushMessageAdapter.notifyDataSetChanged();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(BussPushMessageActivity.this, "温馨提示", "是否确认删除？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 删除推送消息
     * @param Id
     */
    private void removeBussPushGoods(String Id) {
        WebRequestHelper.json_post(BussPushMessageActivity.this, URLText.REMOVE_PUSH_BUSS, RequestParamsPool.removeBussPushGoods(Id), new MyAsyncHttpResponseHandler(BussPushMessageActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject object = new JSONObject(result);
                    String isSuccess = object.optString("IsSuccess");
                    if (isSuccess.equals("true")) {
                        Toast.makeText(BussPushMessageActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        if (0 == mainData.size()){
                            ll_defaultdata.setVisibility(View.VISIBLE);
                            recycle_view.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}