package com.ximai.savingsmore.save.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.constants.AppConstants;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.save.activity.GoodDetailsActivity;
import com.ximai.savingsmore.save.activity.LoginActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.*;
import com.ximai.savingsmore.save.view.imagepicker.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by caojian on 16/11/21.
 */
public class PopuWindowsUtils implements View.OnClickListener {
    private Context context;
    private PopupWindow popupWindow;
    private Button goBtn; //到这里去按钮
    private ViewPager viewPager;
    private List<Goods> list;
//    private List<View> viewList;
    private MyAdapter myAdapter;
    private callBack callBack;
    private boolean isScoll = false;
    private LayoutInflater inflater=null;

    public static final int MIN_CLICK_DELAY_TIME = 3000;//禁止重复点击事件
    private long lastClickTime = 0;

    public PopuWindowsUtils(final Context context, List<Goods> list, final callBack callBack) {
        this.callBack = callBack;
        this.context = context;
        this.list = list;
        inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_share_layout, null);
//        viewList = new ArrayList<View>();
//        for (int i = 0; i < list.size(); i++) {
//            View item = LayoutInflater.from(context).inflate(R.layout.salegood_item, null);
//            viewList.add(item);
//        }
        myAdapter = new MyAdapter();
        viewPager = (ViewPager) view.findViewById(R.id.viewPage);
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled( int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(final int position) {
              //  callBack.call(position);
//延迟两秒跳转
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        callBack.call(position);
                    }
                }, 500);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources()));
        //viewPager= (ViewPager) view.findViewById(R.id.viewPager);
        //goBtn = (Button) view.findViewById(R.id.go_to_hotel_btn);
        // goBtn.setOnClickListener(this);
    }

    /**
     * 下拉式 弹出 pop菜单 parent 右下角
     * @param parent
     * @param position
     */
    public void showAsDropDown(View parent, int position) {
        if (Build.VERSION.SDK_INT >= 24) {
            viewPager.setCurrentItem(position);
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            Log.e(getClass().getSimpleName(), "x : " + x + ", y : " + y);
            popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
//            popupWindow.update(); // 刷新状态
        } else {
            viewPager.setCurrentItem(position);
            popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);// 保证尺寸是根据屏幕像素密度来的
            popupWindow.setFocusable(false);// 使其聚集
            popupWindow.setOutsideTouchable(true);// 设置允许在外点击消失
//        popupWindow.setAnimationStyle(R.style.PopupWindowAnimStyle);// 设置动画
            popupWindow.update(); // 刷新状态
        }
    }


    public void setDismissListener(PopupWindow.OnDismissListener onDismissListener) {
        popupWindow.setOnDismissListener(onDismissListener);
    }

    /**
     * 隐藏菜单
     */
    public void dismixss() {
        popupWindow.dismiss();
    }

    /**
     * 是否显示
     * @return
     */
    public boolean isShowing() {
        return popupWindow.isShowing();
    }

    @Override
    public void onClick(View v) {
    }

    public interface callBack {
        void call(int posiiton);
    }

    /**
     * 适配器
     */
    class MyAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return list.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            View itemView =inflater.inflate(R.layout.salegood_item, null);
            TextView sales_number;
            ImageView imageView;
            TextView shop_name;
            TextView name;
            TextView shou_chang;
            TextView liulan;
            TextView share;
            TextView location;
            TextView start_time;
            TextView end_time;
            TextView price;
            TextView dazhe_style;
            TextView high_price;
            TextView tv_lookthroughs;
            TextView tv_store_count;
            TextView tv_care;
            ImageView iv_endtime;
            tv_care= (TextView) itemView.findViewById(R.id.tv_care);
            tv_store_count= (TextView) itemView.findViewById(R.id.tv_store_count);

            tv_care.setText("关注"+list.get(position).CareCount);
            tv_store_count.setText("到店人次"+list.get(position).StoreCount);

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
            iv_endtime= (ImageView) itemView.findViewById(R.id.iv_endtime);
            high_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            MyImageLoader.displayDefaultImage(URLText.img_url + list.get(position).Image, imageView);
            Glide.with(context).load(URLText.img_url + list.get(position).Image).into(imageView);
            if (null != list.get(position).Name) {
                name.setText(list.get(position).Name);
            }

            if (null != list.get(position).SaleCount) {
                if ("0".equals(list.get(position).SaleCount)){
                    sales_number.setVisibility(View.GONE);
                }else {
                    sales_number.setText("销 " + list.get(position).SaleCount);
                    sales_number.setVisibility(View.GONE);
                }
            }


//            if (null != list.get(position).StoreName) {
//                shop_name.setText(list.get(position).StoreName);
//            }
            if (null != list.get(position).ChainStores){//---------------------------------------------------------进行分店的实现
                if (list.get(position).ChainStores.size() > 0){
                    String names = list.get(position).ChainStores.get(0).Name;
                    shop_name.setText(names);
                }else{
                    if (null != list.get(position).StoreName) {//商家店铺名称
                        shop_name.setText(list.get(position).StoreName);
                    }
                }
            }else{
                if (null != list.get(position).StoreName) {//商家店铺名称
                    shop_name.setText(list.get(position).StoreName);
                }
            }

            if (null != list.get(position).FavouriteCount) {
                shou_chang.setText(list.get(position).FavouriteCount);
            }

            if (null == list.get(position).CommentCount ){
                liulan.setText("0");//评论
            }else{
                liulan.setText(list.get(position).CommentCount);//评论
            }

            share.setText(list.get(position).SharedCount);

            if (null == list.get(position).HitCount ){
               tv_lookthroughs.setText("0");//浏览
            }else{
               tv_lookthroughs.setText(list.get(position).HitCount);//浏览
            }

            if (null != list.get(position).Address) { //促销地点
//                location.setText(list.get(position).Address);
                location.setText(list.get(position).Country+" · "+list.get(position).Province+list.get(position).City+list.get(position).Address);
            }
            if (null != list.get(position).StartTimeName && !TextUtils.isEmpty(list.get(position).StartTimeName)) {
                start_time.setText(list.get(position).StartTimeName.split(" ")[0]);
            }
            if (null != list.get(position).EndTimeName && !TextUtils.isEmpty(list.get(position).EndTimeName)) {
                end_time.setText(list.get(position).EndTimeName.split(" ")[0]);
                try {
                    long time=Utils.dateToStamp(list.get(position).EndTimeName)-System.currentTimeMillis();
                    if (time<3*24*60*60*1000&&time>0){
                        Glide.with(context).load(R.mipmap.end_time_git).into(iv_endtime);
                        iv_endtime.setVisibility(View.VISIBLE);
                    }else {
                        iv_endtime.setVisibility(View.GONE);

                    }
                }catch (Exception e){

                }
            }


            price.setText(list.get(position).Currency+" "+ list.get(position).Price);
//            dazhe_style.setText(list.get(position).Preferential);
            if (list.get(position).Preferential.length() > 5){
                dazhe_style.setText(list.get(position).Preferential.substring(0,5)+"...");
            }else{
                dazhe_style.setText(list.get(position).Preferential);
            }
            high_price.setText(list.get(position).Currency+" "+ list.get(position).OriginalPrice);
            container.addView(itemView);
            //地图商品点击
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (com.ximai.savingsmore.save.modle.LoginUser.getInstance().isLogin()) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                            lastClickTime = currentTime;

                            Intent intent = new Intent(context, GoodDetailsActivity.class);
                            intent.putExtra("id", list.get(position).Id);
                            context.startActivity(intent);
//                            dismixss();
                        }
                    } else {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
                            lastClickTime = currentTime;

                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                            Toast.makeText(context, "温馨提示,您还没有登录", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
            return itemView;
        }
    }
}
