package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.RebateApplyCenterBean;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.GlideRoundTransform;
import com.ximai.savingsmore.save.view.MyGridView;
import com.ximai.savingsmore.save.view.imagepicker.PhotoPreviewActivity;
import com.ximai.savingsmore.save.view.imagepicker.model.PhotoModel;
import com.ximai.savingsmore.save.view.imagepicker.util.CommonUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.Config;
import com.ximai.savingsmore.save.view.imagepicker.util.DbTOPxUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luck on 2018/1/25 0025.
 * 积分详情
 */

public class PointApplyDeatilActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back;
    private ImageView iv_state;
    private TextView tv_state;
    private MyGridView myGridview_1;
    private MyGridView myGridview_2;
    private TextView tv_price;
    private TextView tv_location;
    private TextView tv_number;
    private TextView tv_date;
    private TextView tv_rebateprice;
    private TextView tv_point;
    private RelativeLayout rl_seepoint;
    private Button btn_apply;
    private LinearLayout ll_pointmsg;
    private List<RebateApplyCenterBean.MainData> list;
    private int postion;
    private List<PhotoModel> single_photos = new ArrayList<>();
    private int screen_widthOffset;
    private GridImgAdapter1 gridImgAdapter1;
    private GridImgAdapter2 gridImgAdapter2;
    private List<Images> productImages;
    private List<Images> receiptImages;
    private TextView cen_title;
    private TextView tv_nowmsg;
    private TextView tv_pointmsg;
    private TextView tv_pointmsg1;
    private TextView tv_pointmsg2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rebate_applydetail);
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

        cen_title = (TextView) findViewById(R.id.cen_title);//title
        tv_nowmsg = (TextView) findViewById(R.id.tv_nowmsg);
        iv_state = (ImageView) findViewById(R.id.iv_state);//详情图片
        tv_state = (TextView) findViewById(R.id.tv_state);//详情状态
        myGridview_1 = (MyGridView) findViewById(R.id.myGridview_1);
        myGridview_2 = (MyGridView) findViewById(R.id.myGridview_2);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_location = (TextView) findViewById(R.id.tv_location);
        tv_number = (TextView) findViewById(R.id.tv_number);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_rebateprice = (TextView) findViewById(R.id.tv_rebateprice);
        tv_point = (TextView) findViewById(R.id.tv_point);
        rl_seepoint = (RelativeLayout) findViewById(R.id.rl_seepoint);
        btn_apply = (Button) findViewById(R.id.btn_apply);
        ll_pointmsg = (LinearLayout) findViewById(R.id.ll_pointmsg);
        tv_pointmsg = (TextView) findViewById(R.id.tv_pointmsg);
        tv_pointmsg1 = (TextView)findViewById(R.id.tv_pointmsg1);
        tv_pointmsg2 = (TextView)findViewById(R.id.tv_pointmsg2);
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
    }

    /**
     * data
     */
    private void initData() {
        gridImgAdapter1 = new GridImgAdapter1();
        gridImgAdapter2 = new GridImgAdapter2();
        /**
         * 返利详情数据
         */
        postion =getIntent().getExtras().getInt("postion");//点击的iten
        list = (List<RebateApplyCenterBean.MainData>) getIntent().getExtras().getSerializable("list");

        if (null != list){
            if ("现金返利".equals(list.get(postion).RebateWayName)){
                cen_title.setText("现金返利申请");
                if ("1".equals(list.get(postion).State)){//申请中
                    iv_state.setBackgroundResource(R.mipmap.iv_apply);
                    tv_state.setText("现金返利申请审核中");
                    rl_seepoint.setVisibility(View.GONE);
                    ll_pointmsg.setVisibility(View.GONE);
                    tv_number.setText(list.get(postion).Number); //进行数据设置
                    tv_date.setText(list.get(postion).CreateTimeName);
                    tv_price.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_rebateprice.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_nowmsg.setText("本次奖赏");
                    tv_point.setText("红包1个");
                    tv_location.setText(list.get(postion).Address);
                }else if ("2".equals(list.get(postion).State)){//通过
                    iv_state.setBackgroundResource(R.mipmap.fanli_success);
                    tv_state.setText("现金返利申请成功");
                    rl_seepoint.setVisibility(View.VISIBLE);
                    btn_apply.setText("查看奖赏");
                    ll_pointmsg.setVisibility(View.VISIBLE);
                    tv_number.setText(list.get(postion).Number);//进行数据设置
                    tv_date.setText(list.get(postion).CreateTimeName);
                    tv_price.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_rebateprice.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_nowmsg.setText("本次奖赏");
                    tv_point.setText("红包1个");
                    tv_location.setText(list.get(postion).Address);
                    tv_pointmsg.setText("现金返利说明");
                    tv_pointmsg1.setText("1、现金返利申请已经收到，正在申核中");
                    tv_pointmsg2.setText("2、申请成功后现金返利进入您的奖赏中心");
                }else if ("3".equals(list.get(postion).State)){//未通过
                    iv_state.setBackgroundResource(R.mipmap.fanli_over);
                    tv_state.setText("现金返利申请未通过");
                    rl_seepoint.setVisibility(View.GONE);
                    ll_pointmsg.setVisibility(View.GONE);
                    tv_number.setText(list.get(postion).Number); //进行数据设置
                    tv_date.setText(list.get(postion).CreateTimeName);
                    tv_price.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_rebateprice.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_nowmsg.setText("本次奖赏");
                    tv_point.setText("无");
                    tv_location.setText(list.get(postion).Address);
                }
            }else if ("积分返利".equals(list.get(postion).RebateWayName)){//-------------------------积分返利
                cen_title.setText("积分返利申请");
                if ("1".equals(list.get(postion).State)){//申请中
                    iv_state.setBackgroundResource(R.mipmap.iv_apply);
                    tv_state.setText("积分返利申请审核中");
                    rl_seepoint.setVisibility(View.GONE);
                    ll_pointmsg.setVisibility(View.GONE);
                    tv_number.setText(list.get(postion).Number); //进行数据设置
                    tv_date.setText(list.get(postion).CreateTimeName);
                    tv_price.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_rebateprice.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_nowmsg.setText("本次积分");
                    tv_point.setText(list.get(postion).Point+"分");
                    tv_location.setText(list.get(postion).Address);
                }else if ("2".equals(list.get(postion).State)){//通过
                    iv_state.setBackgroundResource(R.mipmap.fanli_success);
                    tv_state.setText("积分返利申请成功");
                    rl_seepoint.setVisibility(View.VISIBLE);
                    btn_apply.setText("查看积分");
                    ll_pointmsg.setVisibility(View.VISIBLE);
                    tv_number.setText(list.get(postion).Number);//进行数据设置
                    tv_date.setText(list.get(postion).CreateTimeName);
                    tv_price.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_rebateprice.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_nowmsg.setText("本次积分");
                    tv_point.setText(list.get(postion).Point+"分");
                    tv_location.setText(list.get(postion).Address);
                    tv_pointmsg.setText("积分返利说明");
                    tv_pointmsg1.setText("1、本次返利积分已进入您的返利积分中心");
                    tv_pointmsg2.setText("2、积分兑换现金的比例按照省又省积分返利规则执行");
                }else if ("3".equals(list.get(postion).State)){//未通过
                    iv_state.setBackgroundResource(R.mipmap.fanli_over);
                    tv_state.setText("积分返利申请未通过");
                    rl_seepoint.setVisibility(View.GONE);
                    ll_pointmsg.setVisibility(View.GONE);
                    tv_number.setText(list.get(postion).Number); //进行数据设置
                    tv_date.setText(list.get(postion).CreateTimeName);
                    tv_price.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_rebateprice.setText("¥"+ UIUtils.formatPrice(Double.parseDouble(list.get(postion).Price)));
                    tv_nowmsg.setText("本次积分");
                    tv_point.setText("无");
                    tv_location.setText(list.get(postion).Address);
                }
            }

            productImages = list.get(postion).ProductImages;//商品照片
            receiptImages = list.get(postion).ReceiptImages;//消费账单

            /**
             * 店铺照片
             */
            Config.ScreenMap = Config.getScreenSize(this,this);
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            screen_widthOffset = (display.getWidth() - (5* DbTOPxUtils.dip2px(this, 2)))/5;
            myGridview_1.setAdapter(gridImgAdapter1);
            myGridview_2.setAdapter(gridImgAdapter2);
            gridImgAdapter1.notifyDataSetChanged();
            gridImgAdapter2.notifyDataSetChanged();
        }
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
            case R.id.btn_apply://查看积分
                if ("现金返利".equals(list.get(postion).RebateWayName)){
                    startActivity(new Intent(this,PersonalRewardActivity.class));
                }else{
                    startActivity(new Intent(this,PointManagerActivity.class));
                }
                break;
        }
    }

    /**
     * 店铺图片展示
     */
    class GridImgAdapter1 extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return productImages.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(PointApplyDeatilActivity.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if(convertView!=null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(PointApplyDeatilActivity.this).inflate(R.layout.activity_addstory_img_item,null);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            holder.delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);

            RequestOptions options = new RequestOptions()
                    .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                    .priority(Priority.HIGH)
                    .transform(new GlideRoundTransform(PointApplyDeatilActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

            if (productImages.get(position).ImagePath == null){
                holder.delete_IV.setVisibility(View.GONE);
                myGridview_1.setVisibility(View.GONE);
            }else{
                holder.delete_IV.setVisibility(View.GONE);
                /**
                 * 加载圆角图片
                 */
                Glide.with(PointApplyDeatilActivity.this).load(URLText.img_url +productImages.get(position).ImagePath)
                        .apply(options).into(holder.add_IB);
                /**
                 * 查看大图
                 */
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        single_photos.clear();
                        for (int i = 0; i < productImages.size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(productImages.get(i).ImagePath);
                            single_photos.add(photoModel);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putBoolean("isSave",false);
                        CommonUtils.launchActivity(PointApplyDeatilActivity.this, PhotoPreviewActivity.class, bundle);
                    }
                });
            }
            return convertView;
        }
        class ViewHolder {
            ImageView add_IB;
            ImageView delete_IV;
        }
    }


    /**
     * 店铺图片展示
     */
    class GridImgAdapter2 extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return receiptImages.size();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(PointApplyDeatilActivity.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if(convertView!=null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(PointApplyDeatilActivity.this).inflate(R.layout.activity_addstory_img_item,null);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            holder.delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);

            if (receiptImages.get(position).ImagePath == null){
                holder.delete_IV.setVisibility(View.GONE);
                myGridview_2.setVisibility(View.GONE);
            }else{
                holder.delete_IV.setVisibility(View.GONE);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(PointApplyDeatilActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
                /**
                 * 加载圆角图片
                 */
                Glide.with(PointApplyDeatilActivity.this).load(URLText.img_url +receiptImages.get(position).ImagePath)
                        .apply(options).into(holder.add_IB);
                /**
                 * 查看大图
                 */
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        single_photos.clear();
                        for (int i = 0; i < receiptImages.size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(receiptImages.get(i).ImagePath);
                            single_photos.add(photoModel);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putBoolean("isSave",false);
                        CommonUtils.launchActivity(PointApplyDeatilActivity.this, PhotoPreviewActivity.class, bundle);
                    }
                });
            }
            return convertView;
        }
        class ViewHolder {
            ImageView add_IB;
            ImageView delete_IV;
        }
    }
}