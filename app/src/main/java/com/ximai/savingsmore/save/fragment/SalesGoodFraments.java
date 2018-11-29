package com.ximai.savingsmore.save.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.library.toolbox.PreferencesUtils;
import com.ximai.savingsmore.save.activity.AddGoodsAcitivyt;
import com.ximai.savingsmore.save.activity.GoodsCommentActiviyt;
import com.ximai.savingsmore.save.activity.IssuGoodActivity;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.Comment;
import com.ximai.savingsmore.save.modle.CommentList;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.ximai.savingsmore.save.view.XiMaiPopDialog1;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/12/9.
 * 促销中 - 支持侧滑的实现
 */
public class SalesGoodFraments extends Fragment implements SwipeItemClickListener {
    private SwipeMenuRecyclerView recyclerView;
    private List<Goods> list = new ArrayList<>();
    private GoodsList goodsList = new GoodsList();
    private MyAdapter myAdapter;
    private boolean isComment, isHit, isCollect;
    private List<Comment> commentList = new ArrayList<>();
//    private List<Goods> all_list = new ArrayList<>();
    private View view;
    private LinearLayout ll_defaultdata;
    private int i;
    private LinearLayoutManager mLayoutManager;
    private DefaultItemDecoration mItemDecoration;
    private String isSucess;
    private int adapterPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.no_bag_goods_ce, null);

        initView();

        initData();

        return view;
    }

    /**
     * init - view
     */
    private void initView() {
        ll_defaultdata = (LinearLayout) view.findViewById(R.id.ll_defaultdata);
        recyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.recycle_view);
    }

    /**
     * init - data
     */
    private void initData() {
        /**
         * 注册一个监听
         */
        NotificationCenter.defaultCenter().addObserver(notificationCenterObserver, Constants.LOADING_FABUCUXIAO);

        mLayoutManager = new LinearLayoutManager(getActivity());//设置布局参数
        mItemDecoration = new DefaultItemDecoration(ContextCompat.getColor(getActivity(), R.color.white));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(mItemDecoration);
        recyclerView.setSwipeItemClickListener(this);//设置Iten的点击事件
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);//创建侧边栏
        recyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener);//侧边栏点击事件

        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        /**
         * 获取促销商品
         */
        getAllGoods();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getArguments().getString("isComment")) {
            if (getArguments().getString("isComment").equals("true")) {
                isComment = true;
            }
        }
        if (null != getArguments().getString("hit")) {
            if (getArguments().getString("hit").equals("true")) {
                isHit = true;
            }
        }
        if (null != getArguments().getString("collect")) {
            if (getArguments().getString("collect").equals("true")) {
                isCollect = true;
            }
        }
    }

    /**
     * 适配器操作
     */
    private class MyAdapter extends RecyclerView.Adapter {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.salegood_item, null);
            return new MyViewHodel(layout);
        }
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHodel viewHodel = (MyViewHodel) holder;
            Glide.with(getContext()).load(URLText.img_url + list.get(position).Image).into(viewHodel.imageView);
            if (null != list.get(position).Name) {
                viewHodel.name.setText(list.get(position).Name);
            }
            if (null != list.get(position).SaleCount) {
                viewHodel.sales_number.setText("销 " + list.get(position).SaleCount);
            }
            if (null != list.get(position).ChainStores){//---------------------------------------------------------进行分店的实现
                if (list.get(position).ChainStores.size() > 0){
                    String name = list.get(position).ChainStores.get(0).Name;
                    viewHodel.shop_name.setText(name);
                }else{
                    if (null != list.get(position).StoreName) {//商家店铺名称
                        viewHodel.shop_name.setText(list.get(position).StoreName);
                    }
                }
            }else{
                if (null != list.get(position).StoreName) {//商家店铺名称
                    viewHodel.shop_name.setText(list.get(position).StoreName);
                }
            }
            if (null != list.get(position).FavouriteCount) {
                viewHodel.shou_chang.setText(list.get(position).FavouriteCount);
            }
            if (null == list.get(position).CommentCount ){
                viewHodel.liulan.setText("0");//评论
            }else{
                viewHodel.liulan.setText(list.get(position).CommentCount);//评论
            }
            viewHodel.share.setText(list.get(position).SharedCount);
            if (null == list.get(position).HitCount ){
                viewHodel.tv_lookthroughs.setText("0");//浏览
            }else{
                viewHodel.tv_lookthroughs.setText(list.get(position).HitCount);//浏览
            }
            if (null != list.get(position).Address && null != list.get(position).Province && null != list.get(position).City) {//促销地点
                viewHodel.location.setText(list.get(position).Country+" · "+list.get(position).Province+list.get(position).City+list.get(position).Address);
            }
            if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)) {
                viewHodel.start_time.setText(list.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)) {
                viewHodel.end_time.setText(list.get(position).EndTimeName.split(" ")[0]);
            }
            //币种和价格
            viewHodel.price.setText(list.get(position).Currency+" "+list.get(position).Price);
//            viewHodel.dazhe_style.setText(list.get(position).Preferential);
            if (list.get(position).Preferential.length() > 5){
                viewHodel.dazhe_style.setText(list.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.dazhe_style.setText(list.get(position).Preferential);
            }
            viewHodel.high_price.setText(list.get(position).Currency+" "+list.get(position).OriginalPrice);
        }
        @Override
        public int getItemCount() {
            return list.size();
        }


    }
    static class MyViewHodel extends RecyclerView.ViewHolder {
        public TextView sales_number;
        public ImageView imageView;
        public TextView shop_name;
        public TextView name;
        public TextView shou_chang;
        public TextView liulan;
        public TextView share;
        public TextView location;
        public TextView start_time;
        public TextView end_time;
        public TextView price;
        public TextView dazhe_style;
        public TextView high_price;
        public TextView tv_lookthroughs;
        public MyViewHodel(View itemView) {
            super(itemView);
            sales_number = (TextView) itemView.findViewById(R.id.tv_volume);
            name = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            shou_chang = (TextView) itemView.findViewById(R.id.tv_assesss);
            liulan = (TextView) itemView.findViewById(R.id.tv_comments);
            share = (TextView) itemView.findViewById(R.id.tv_sharks);
            shop_name = (TextView) itemView.findViewById(R.id.tv_business);
            location = (TextView) itemView.findViewById(R.id.tv_address);
            start_time = (TextView) itemView.findViewById(R.id.statr_time);
            end_time = (TextView) itemView.findViewById(R.id.end_time);
            price = (TextView) itemView.findViewById(R.id.tv_price);
            dazhe_style = (TextView) itemView.findViewById(R.id.tv_favourable);
            high_price = (TextView) itemView.findViewById(R.id.tv_agoprice);
            tv_lookthroughs = (TextView) itemView.findViewById(R.id.tv_lookthroughs);
            high_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
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
            adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                isYesDeletedialog();
            }
        }
    };

    /**
     * iten的点击事件
     */
    @Override
    public void onItemClick(View itemView, int position) {
        if (LoginUser.getInstance().isLogin()) {
            if (isComment) {
                if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("1")){
                    Onedialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("2")){
                    Twodialog();
                } else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("3")){
                    Intent intent = new Intent(getActivity(), GoodsCommentActiviyt.class);
                    intent.putExtra("id", list.get(position).Id);
                    intent.putExtra("score", list.get(position).Score);
                    intent.putExtra("isComment", "true");
                    startActivity(intent);
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("4")){
                    /**
                     * 打电话
                     */
                    Fourdialog();
                }
            } else {//进入发布促销修改界面
                if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("1")){
                    Onedialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("2")){
                    Twodialog();
                } else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("3")){
                    Intent intent = new Intent(getActivity(), AddGoodsAcitivyt.class);
                    intent.putExtra("id", list.get(position).Id);
                    startActivity(intent);
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("4")){
                    /**
                     * 打电话
                     */
                    Fourdialog();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 得到所有的商品
     */
    private void getAllGoods() {
        WebRequestHelper.json_post(getActivity(), URLText.GET_SALES_GOODS, RequestParamsPool.getMySalesGoods(false, isComment, isHit, isCollect), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                list.clear();
                Log.e("tag","获取到添加商品更新接口");
                String resule = new String(responseBody);
                goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                if (goodsList.IsSuccess) {
                    if (null != goodsList.MainData && goodsList.MainData.size() != 0) {
                        list = goodsList.MainData;
                    }

                    if (isComment && list.size() > 0) {//收藏
                        for (int i = 0; i < list.size(); i++) {
                            getComment(list.get(i).Id, list.get(i));
                        }
                    } else {
                        Log.e("tag","list-----------"+list.size());
                        if (list.size() == 0){
                            ll_defaultdata.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }else{
                            ll_defaultdata.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                    /**
                     * 判断是否是从侧边栏进来
                     */
                    String cebianlan = PreferencesUtils.getString(getActivity(), "cebianlan", "");
                    //侧边栏进来需要进行一个弹框提示
                    if ("1".equals(cebianlan) && !TextUtils.isEmpty(cebianlan)){
                        //判断是否有数据
                        if (null != list && list.size() == 0){//没有数据
                            noDataDialog();
                        }else if (null != list && list.size() > 0){//有数据
                            yesDataDialog();
                        }
                        PreferencesUtils.putString(getActivity(),"cebianlan","0");
                    }
                }
            }
        });
    }

    /**
     * 移除商品
     */
    private void removeGood(String Id) {
        WebRequestHelper.json_post(getActivity(), URLText.REMOVE_MYGOODS, RequestParamsPool.removeGoods(Id), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject object = null;
                try {
                    String result = new String(responseBody);
                    object = new JSONObject(result);
                    isSucess = object.optString("IsSuccess");
                    if (isSucess.equals("true")) {
                        Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取商品评论
     * @param id
     * @param goods
     */
    private void getComment(String id, final Goods goods) {
        WebRequestHelper.json_post(getActivity(), URLText.GOODS_COMMENT, RequestParamsPool.getGoodsComment(id), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                CommentList commentList1 = GsonUtils.fromJson(result, CommentList.class);
                commentList = commentList1.MainData;
                if (commentList.size() > 0) {
                    list.add(goods);
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 未申请
     */
    public void Onedialog(){
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
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "请完成注册商家信息，再发布促销。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }


    /**
     * 未申请
     */
    public void Twodialog(){
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
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "您提交的入驻申请正在审核，请耐心等待。", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 未通过审核 - 进行一个弹框操作
     */
    public void Fourdialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                call("02158366991");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "您的入驻申请未通过审核，请联系我们 021-58366991。", "确认", R.style.CustomDialog_1, callBack, 2);
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
     * 观察者
     */
    NotificationCenter.NotificationCenterObserver notificationCenterObserver = new NotificationCenter.NotificationCenterObserver(){
        @Override
        public void onReceive(String eventName,Object cid) {
            try {
                if (Constants.LOADING_FABUCUXIAO.equals(eventName)) {
                    Log.e("tag","收到添加商品通知");
                    /**
                     * 获取商家收藏店铺的取消
                     */
                    getAllGoods();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotificationCenter.defaultCenter().removeObserver(notificationCenterObserver, Constants.LOADING_FABUCUXIAO);
    }

    /**
     * 发布促销dialog - 有数据
     */
    public void yesDataDialog(){
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
        Dialog dialog = new XiMaiPopDialog1(getActivity(), "温馨提示", "点击商品，实现更新", "知道了", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 发布促销dialog - 没有数据
     */
    public void noDataDialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("1")){
                    Onedialog();
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("2")){
                    Twodialog();
                } else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("3")){
                    Intent intent = new Intent(getActivity(), AddGoodsAcitivyt.class);
                    startActivity(intent);
                }else if (MyUserInfoUtils.getInstance().myUserInfo.ApprovalState.equals("4")){
                    /**
                     * 打电话
                     */
                    Fourdialog();
                }
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog1(getActivity(), "温馨提示", "暂无商品，赶紧发布", "知道了", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 未申请
     */
    public void isYesDeletedialog(){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                removeGood(list.get(adapterPosition).Id);
                list.remove(adapterPosition);
                myAdapter.notifyDataSetChanged();
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "是否确认删除？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }
}