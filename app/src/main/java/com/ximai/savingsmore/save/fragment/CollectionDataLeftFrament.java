package com.ximai.savingsmore.save.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.ximai.savingsmore.save.activity.AddGoodsAcitivyt;
import com.ximai.savingsmore.save.activity.GoodsCommentActiviyt;
import com.ximai.savingsmore.save.modle.Comment;
import com.ximai.savingsmore.save.modle.CommentList;
import com.ximai.savingsmore.save.modle.Goods;
import com.ximai.savingsmore.save.modle.GoodsList;
import com.ximai.savingsmore.save.modle.LoginUser;
import com.ximai.savingsmore.save.modle.MyUserInfoUtils;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.view.FullyLinearLayoutManager;
import com.ximai.savingsmore.save.view.KyLoadingBuilder;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by caojian on 16/12/9.
 * 收藏汇总 -
 */
public class CollectionDataLeftFrament extends Fragment {
    private RecyclerView recyclerView;
    private List<Goods> list = new ArrayList<Goods>();
    private OnItemClickEventListener listener = null;
    private GoodsList goodsList = new GoodsList();
    private MyAdapter myAdapter;
    private boolean isComment, isHit, isCollect;
    private AlertDialog classity_dialog;
    private List<Comment> commentList = new ArrayList<Comment>();
    private List<Goods> all_list = new ArrayList<Goods>();
    private View view;
    private LinearLayout ll_defaultdata;
    private int i;
    private KyLoadingBuilder builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.no_bag_goods, null);

        initView();

        initData();

        return view;
    }

    /**
     * init - view
     */
    private void initView() {
        ll_defaultdata = (LinearLayout) view.findViewById(R.id.ll_defaultdata);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        initRecycleView(recyclerView);
    }

    /**
     * init - data
     */
    private void initData() {
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
    }

    /**
     * 配置recycleview
     *
     * @param recyclerView
     */
    private void initRecycleView(RecyclerView recyclerView) {
        FullyLinearLayoutManager myLayoutManager = new FullyLinearLayoutManager(getContext()) {
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
     *
     * @param recyclerView
     * @param layoutManager
     */
    private void configRecycleView(RecyclerView recyclerView, RecyclerView.LayoutManager layoutManager) {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
            return new MyViewHodel(layout, new OnItemClickEventListener() {
                @Override
                public void onItemClick(int position) {
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
                        } else {
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

                /**
                 * 长按操作
                 * @param position
                 */
                @Override
                public void onItemLongClick(final int position) {
                    classity_dialog = new AlertDialog.Builder(getActivity()).create();
                    View view = LayoutInflater.from(getActivity()).inflate(R.layout.delect_good_dialog, null);
                    final TextView queding, quxiao;
                    final EditText content;
                    queding = (TextView) view.findViewById(R.id.commit);
                    quxiao = (TextView) view.findViewById(R.id.cancel);
                    queding.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeGood(list.get(position).Id);
                            list.remove(position);
                            myAdapter.notifyDataSetChanged();
                            classity_dialog.dismiss();
                        }
                    });
                    quxiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            classity_dialog.dismiss();
                        }
                    });
                    classity_dialog.setView(view);
                    classity_dialog.show();
                }
            });
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHodel viewHodel = (MyViewHodel) holder;
            Glide.with(getContext()).load(URLText.img_url + list.get(position).Image).into(viewHodel.imageView);
            if (null != list.get(position).Name) {
                viewHodel.name.setText(list.get(position).Name);
            }
            if (null != list.get(position).SaleCount) {
                if ("0".equals(list.get(position).SaleCount)){
                    viewHodel.sales_number.setVisibility(View.GONE);
                }else {
                    viewHodel.sales_number.setText("销 " + list.get(position).SaleCount);
                    viewHodel.sales_number.setVisibility(View.VISIBLE);
                }
            }
//            if (null != list.get(position).StoreName) {
//                viewHodel.shop_name.setText(list.get(position).StoreName);
//            }
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
            if (null != list.get(position).Address && null != list.get(position).Province && null != list.get(position).City) {
                viewHodel.location.setText(list.get(position).Country+" · "+list.get(position).Province + list.get(position).City + list.get(position).Address);
            }
            if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)) {
                viewHodel.start_time.setText(list.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)) {
                viewHodel.end_time.setText(list.get(position).EndTimeName.split(" ")[0]);
            }
            viewHodel.price.setText(list.get(position).Currency+" "+ list.get(position).Price);
//            viewHodel.dazhe_style.setText(list.get(position).Preferential);
            if (list.get(position).Preferential.length() > 5){
                viewHodel.dazhe_style.setText(list.get(position).Preferential.substring(0,5)+"...");
            }else{
                viewHodel.dazhe_style.setText(list.get(position).Preferential);
            }
            viewHodel.high_price.setText(list.get(position).Currency+" "+ list.get(position).OriginalPrice);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    /**
     * 提供接口
     */
    public interface OnItemClickEventListener {
        void onItemClick(int position);

        void onItemLongClick(int position);
    }

    /**
     * 设置iten的点击事件
     *
     * @param listener
     */
    public void setOnItemClickEventListener(OnItemClickEventListener listener) {
        this.listener = listener;
    }

    class MyViewHodel extends RecyclerView.ViewHolder {
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

        public MyViewHodel(View itemView, final OnItemClickEventListener lis) {
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

            if (null != lis) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lis.onItemClick(getAdapterPosition());
                    }
                });
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        lis.onItemLongClick(getAdapterPosition());
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllGoods();
    }

    /**
     * 得到所有的商品
     */
    private void getAllGoods() {
        showLoading(getActivity(),"正在加载");
//        isRefreshing = true;
//        swipeRefreshLayout.setRefreshing(isRefreshing);
        WebRequestHelper.json_post(getActivity(), URLText.GET_SALES_GOODS, RequestParamsPool.getMySalesGoods(false, isComment, isHit, isCollect), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    goodsList = GsonUtils.fromJson(new String(responseBody), GoodsList.class);
                    if (goodsList.IsSuccess) {
                        if (null != goodsList.MainData && goodsList.MainData.size() != 0) {
                            all_list = goodsList.MainData;
                        }
                        if (isComment && all_list.size() > 0) {
                            list.clear();
                            for (int i = 0; i < all_list.size(); i++) {
                                getComment(all_list.get(i).Id, all_list.get(i));
                            }
                        } else {
                            list = all_list;
                            if (null != list && list.size() != 0){
                                myAdapter.notifyDataSetChanged();
                            }else{
                                ll_defaultdata.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        }

                        /**
                         *对收藏进行排序
                         */
                        Collections.sort(all_list, new Comparator<Goods>() {
                            @Override
                            public int compare(Goods goods, Goods t1) {
                                if (null != t1.FavouriteCount && null != goods.FavouriteCount){
                                    i = Integer.parseInt(t1.FavouriteCount) - Integer.parseInt(goods.FavouriteCount);
                                    myAdapter.notifyDataSetChanged();
                                }
                                return i;
                            }
                        });
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
//                isRefreshing = false;
//                swipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
    }

    /**
     * 移除商品
     *
     * @param Id
     */
    private void removeGood(String Id) {
        WebRequestHelper.json_post(getActivity(), URLText.REMOVE_MYGOODS, RequestParamsPool.removeGoods(Id), new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                JSONObject object = new JSONObject();
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
//                initCallPhone("02158366991");
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
                        com.yanzhenjie.alertdialog.AlertDialog.newBuilder(getActivity())
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
                        if (AndPermission.hasAlwaysDeniedPermission(getActivity(), deniedPermissions)) {
                            AndPermission.defaultSettingDialog(getActivity(), 400)
                                    .setTitle("权限申请失败")
                                    .setMessage("您拒绝了我们必要的一些权限，请去设置打开拨打电话权限")
                                    .setPositiveButton("好，去设置")
                                    .show();
                        }
                        if (requestCode == 200) {// Failure.
                            Toast.makeText(getActivity(), "请去设置打开拨打电话权限", Toast.LENGTH_SHORT).show();
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
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(intent);
        } catch (Exception e) {
            System.out.println("call phone error");
            e.printStackTrace();
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
