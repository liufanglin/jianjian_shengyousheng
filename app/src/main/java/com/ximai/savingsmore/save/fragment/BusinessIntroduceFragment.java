package com.ximai.savingsmore.save.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.easemob.easeui.EaseConstant;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.activity.ChatActivity;
import com.ximai.savingsmore.save.activity.LeaveMessageActivity;
import com.ximai.savingsmore.save.activity.TakeMeActivity;
import com.ximai.savingsmore.save.adapter.GridImageAdapterNoDelete;
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.BusinessMessage;
import com.ximai.savingsmore.save.modle.GoodSalesType;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.ServiceList;
import com.ximai.savingsmore.save.utils.CallBack.DialogCallBack;
import com.ximai.savingsmore.save.utils.NotificationCenter;
import com.ximai.savingsmore.save.view.FullyGridLayoutManager;
import com.ximai.savingsmore.save.view.GlideRoundTransform;
import com.ximai.savingsmore.save.view.Html5Activity;
import com.ximai.savingsmore.save.view.MyGridView;
import com.ximai.savingsmore.save.view.SelectPopupWindow;
import com.ximai.savingsmore.save.view.XiMaiPopDialog;
import com.ximai.savingsmore.save.view.imagepicker.PhotoPreviewActivity;
import com.ximai.savingsmore.save.view.imagepicker.model.PhotoModel;
import com.ximai.savingsmore.save.view.imagepicker.util.CommonUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.Config;
import com.ximai.savingsmore.save.view.imagepicker.util.DbTOPxUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caojian on 16/11/26.
 */
//商家介绍 - 商家信息介绍
public class BusinessIntroduceFragment extends Fragment implements View.OnClickListener {
    private BusinessMessage businessMessage;
    private Button flow_me;
    private Button seiverse;
    private List<GoodSalesType> Serive_list = new ArrayList<GoodSalesType>();
    private View view;

    private TextView store_name;
    private TextView pizhun_time;
    private TextView xixiang_adress;
    private TextView address;
    private TextView yingye_start;
    private TextView position;
    private ImageView zhengshu_iamge;
    private TextView weChat;
    private TextView yingye_end;
    private TextView phone_number;
    private TextView create_date;
    private ImageView slience_image;
    private TextView good_type;
    private TextView good_range;
    private TextView xiliren_number;
    private TextView lianxiren;
    private TextView apply_time;

    private int screen_widthOffset;
    private GridImgAdapter gridImgAdapter;
    private MyGridView my_goods_GV;
    private List<Images> images;
    private TextView tv_businessdate;

    private TextView tv_fapiao;
    private TextView tv_songhuofw;
    private TextView tv_songhuobx;
    private SelectPopupWindow menuWindow;
    private List<PhotoModel> single_photos = new ArrayList<>();
    private TextView tv_mendianfali;

    private RecyclerView recycler_movie;//视频相关
    private GridImageAdapterNoDelete adapter;
    private List<LocalMedia> selectList = new ArrayList<>();//视频方面
    private int maxSelectNum = 1;//视频方面
    private LinearLayout ll_movie;
    private ImageView zxing_image;
    private TextView tv_zhaoping;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private TextView website;
    private String mUrl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.business_introduce, null);

        /**
         * init
         */
        initView();

        /**
         *data
         */
        initData();

        /**
         *event
         */
        initEvent();

        return view;
    }

    private void initView() {
        seiverse = (Button) view.findViewById(R.id.servise);
        flow_me = (Button) view.findViewById(R.id.flow_me);
        my_goods_GV = (MyGridView) view.findViewById(R.id.myGridview);

        store_name = (TextView) view.findViewById(R.id.store_name);
        address = (TextView) view.findViewById(R.id.adress);
        xixiang_adress = (TextView) view.findViewById(R.id.xiangxi_adress);
        yingye_start = (TextView) view.findViewById(R.id.yingye_start);
        yingye_end = (TextView) view.findViewById(R.id.yingye_end);
        website = (TextView) view.findViewById(R.id.website);
        phone_number = (TextView) view.findViewById(R.id.phone_number);
        weChat = (TextView) view.findViewById(R.id.wechat);
        create_date = (TextView) view.findViewById(R.id.create_time);
        slience_image = (ImageView) view.findViewById(R.id.liscense_image);
        zhengshu_iamge = (ImageView) view.findViewById(R.id.zhegnshu_image);
        good_type = (TextView) view.findViewById(R.id.good_type);
        good_range = (TextView) view.findViewById(R.id.tv_rang);
        lianxiren = (TextView) view.findViewById(R.id.linkman_name);
        position = (TextView) view.findViewById(R.id.linkman_position);
        xiliren_number = (TextView) view.findViewById(R.id.linkman_number);
        apply_time = (TextView) view.findViewById(R.id.apply_time);
        pizhun_time = (TextView) view.findViewById(R.id.pizhun_time);
        tv_businessdate = (TextView) view.findViewById(R.id.tv_businessdate);//商家营业日

        tv_fapiao = (TextView) view.findViewById(R.id.tv_fapiao);//商品发票
        tv_songhuofw = (TextView) view.findViewById(R.id.tv_songhuofw);//送货服务
        tv_songhuobx = (TextView) view.findViewById(R.id.tv_songhuobx);//送货保险
        tv_mendianfali = (TextView) view.findViewById(R.id.tv_mendianfali);//门店消费返利

        ll_movie = (LinearLayout) view.findViewById(R.id.ll_movie);//视频布局
        recycler_movie = (RecyclerView) view.findViewById(R.id.recycler_movie);//视频

        zxing_image = (ImageView) view.findViewById(R.id.zxing_image);//微信图片
        tv_zhaoping = (TextView) view.findViewById(R.id.tv_zhaoping);//招聘兼职转发
    }

    private void initData() {

        /**
         * 视频适配器
         */
        FullyGridLayoutManager manager = new FullyGridLayoutManager(getActivity(), 5, GridLayoutManager.VERTICAL, false);
        recycler_movie.setLayoutManager(manager);
        adapter = new GridImageAdapterNoDelete(getActivity(), onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recycler_movie.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapterNoDelete.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (2) {
                        case 2:
                            // 预览视频
                            PictureSelector.create(getActivity()).externalPictureVideo(media.getPath());
                            break;
                    }
                }
            }
        });
        //图片适配器
        gridImgAdapter = new GridImgAdapter();

        try{
            /**
             * 获取商家信息
             */
            businessMessage= (BusinessMessage) getArguments().getSerializable("good");

            if (null !=  businessMessage.UserExtInfo.Images) {
                images =  businessMessage.UserExtInfo.Images;
                gridImgAdapter.notifyDataSetChanged();
            }
            store_name.setText(businessMessage.ShowName);
            address.setText(businessMessage.Province.Name + " " + businessMessage.City.Name);
            if (null != businessMessage.UserExtInfo) {
                if (TextUtils.isEmpty(businessMessage.UserExtInfo.WebSite)){
                    website.setText("www.savingsmore.com");
                }else{
                    website.setText(businessMessage.UserExtInfo.WebSite);
                }
                /**
                 * 加载圆角图片
                 */
                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(getContext(),5)).diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(getContext()).load(URLText.img_url + businessMessage.UserExtInfo.LicenseKeyPath)
                        .apply(options).into(zhengshu_iamge);
                /**
                 * 加载圆角图片
                 */
                Glide.with(getContext()).load(URLText.img_url + businessMessage.UserExtInfo.BusinessLicensePath)
                        .apply(options).into(slience_image);

                /**
                 * 微信图片
                 */
                if (null != businessMessage.WeChatImagePath && !TextUtils.isEmpty(businessMessage.WeChatImagePath)){
                    Glide.with(getContext()).load(URLText.img_url + businessMessage.WeChatImagePath)
                            .apply(options).into(zxing_image);
                }

                if (businessMessage.UserExtInfo.IsBag) {
                    good_type.setText("产品类商品");
                } else {
                    good_type.setText("服务类商品");
                }
                phone_number.setText(businessMessage.UserExtInfo.OfficePhone);
                yingye_start.setText(businessMessage.UserExtInfo.StartHours);
                yingye_end.setText(businessMessage.UserExtInfo.EndHours);

                if (null != businessMessage.UserExtInfo.FirstClass) {//主营商品
                    good_range.setText(businessMessage.UserExtInfo.FirstClass.Name);//主营商品
                }

                if (null != businessMessage.UserExtInfo.BusinessDate) {//商家营业日期
                    tv_businessdate.setText(businessMessage.UserExtInfo.BusinessDate.Name);//商家营业日期
                }

                if (null != businessMessage.UserExtInfo.Invoice) {//商品发票
                    tv_fapiao.setText(businessMessage.UserExtInfo.Invoice.Name);
                }

                if (null != businessMessage.UserExtInfo.DeliveryService) {//送货服务
                    tv_songhuofw.setText(businessMessage.UserExtInfo.DeliveryService.Name);
                }

                if (null != businessMessage.UserExtInfo.Premium) {//送货保险
                    tv_songhuobx.setText(businessMessage.UserExtInfo.Premium.Name);
                }
            }
            xixiang_adress.setText(businessMessage.Domicile);//商家信息
            weChat.setText(businessMessage.Email);
            create_date.setText(businessMessage.UserExtInfo.FoundingDateName);
            apply_time.setText(businessMessage.UserExtInfo.FoundingDateName);
            pizhun_time.setText(businessMessage.ApprovalDateName);
            lianxiren.setText(businessMessage.UserDisplayName);
            position.setText(businessMessage.Post);
            xiliren_number.setText(businessMessage.UserExtInfo.PhoneNumber);
            if (null == businessMessage.UserExtInfo.RebatePercent){
                tv_mendianfali.setText("0.00");//门店消费返利
            }else{
                double v1 = Double.parseDouble(businessMessage.UserExtInfo.RebatePercent);
                float v = (float) (v1 * 100);
                tv_mendianfali.setText(v+ "");//门店消费返利
            }

            if (null != businessMessage.BusinessScopes && businessMessage.BusinessScopes.size() > 0) {
                good_range.setText(businessMessage.BusinessScopes.get(0).DictNode.Name);
            }

            if (null == businessMessage.UserExtInfo.SharedRedPack){
                tv_zhaoping.setText("0.00");//招聘兼职转发
            }else{
                tv_zhaoping.setText(businessMessage.UserExtInfo.SharedRedPack);//招聘兼职转发
            }

            /**
             * 店铺照片
             */
            Config.ScreenMap = Config.getScreenSize(getActivity(), getContext());
            WindowManager windowManager = getActivity().getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            screen_widthOffset = (display.getWidth() - (5* DbTOPxUtils.dip2px(getContext(), 2)))/5;
            my_goods_GV.setAdapter(gridImgAdapter);
            gridImgAdapter.notifyDataSetChanged();


            //如果有视频就显示视频的栏目数据 - 如果没有就不显示
            /**
             * 视频加载
             */
            if (null == businessMessage.UserExtInfo.VideoPath){
                ll_movie.setVisibility(View.GONE);
            }else{
                ll_movie.setVisibility(View.VISIBLE);
                LocalMedia localMedia = new LocalMedia();
                localMedia.setPath(URLText.img_url+businessMessage.UserExtInfo.VideoPath);
                selectList.add(localMedia);
                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
            }
            //如果招聘转发大于0那么久弹框
            if (null != businessMessage.UserExtInfo.SharedRedPack && 0 < Double.parseDouble(businessMessage.UserExtInfo.SharedRedPack)){
                tongzhi(businessMessage.UserExtInfo.SharedRedPack);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 招募启示
     */
    public void tongzhi(String sharedRedPack){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
                NotificationCenter.defaultCenter().postNotification(Constants.BUSINESS_MSG,"");
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "招募启示", "转发我们促销广告，每次" + sharedRedPack + "元。", "转发","下次", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void initEvent() {
        seiverse.setOnClickListener(this);
        flow_me.setOnClickListener(this);
        phone_number.setOnClickListener(this);//商家负责人电话
        xiliren_number.setOnClickListener(this);//联系人手机
        slience_image.setOnClickListener(this);//证件照片
        zhengshu_iamge.setOnClickListener(this);//证件照片
        zxing_image.setOnClickListener(this);//微信图片
        website.setOnClickListener(this);//网址
    }

    /**
     * 店铺图片展示
     */
    class GridImgAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return images.size();
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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_addstory_img_item, null);
            GridImgAdapter.ViewHolder holder;
            if(convertView!=null){
                holder = new GridImgAdapter.ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_addstory_img_item,null);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            holder.delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);

            if (images.get(position).ImagePath == null){
                holder.delete_IV.setVisibility(View.GONE);
                my_goods_GV.setVisibility(View.GONE);
            }else{
                holder.delete_IV.setVisibility(View.GONE);
                /**
                 * 加载圆角图片
                 */
                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(getContext(),5)).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(getContext()).load(URLText.img_url +images.get(position).ImagePath)
                        .apply(options).into(holder.add_IB);
                /**
                 * 查看大图
                 */
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        single_photos.clear();
                        for (int i = 0; i < images.size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(images.get(i).ImagePath);
                            single_photos.add(photoModel);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putBoolean("isSave",false);
                        CommonUtils.launchActivity(getActivity(), PhotoPreviewActivity.class, bundle);
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
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flow_me://带我去商家
                Intent intent1 = new Intent(getActivity(), TakeMeActivity.class);
                intent1.putExtra("isgood", "false");
                intent1.putExtra("good", businessMessage);
                startActivity(intent1);
                break;
            case R.id.servise://客服
                showSetIconWindow();
                break;
            case R.id.phone_number://商家负责人电话
                if (TextUtils.isEmpty(phone_number.getText())){
                    Toast.makeText(getActivity(), "商家负责人电话不存在", Toast.LENGTH_SHORT).show();
                }else{
                    dialog(phone_number.getText().toString());
                }
                break;
            case R.id.linkman_number:
                if (TextUtils.isEmpty(xiliren_number.getText())){
                    Toast.makeText(getActivity(), "联系人电话不存在", Toast.LENGTH_SHORT).show();
                }else{
                    dialog(xiliren_number.getText().toString());
                }
                break;
            case R.id.liscense_image://图片上面信息
                if (null != businessMessage.UserExtInfo.BusinessLicensePath){
                    single_photos.clear();
                    PhotoModel photoModel = new PhotoModel();
                    photoModel.setOriginalPath(businessMessage.UserExtInfo.BusinessLicensePath);
                    single_photos.add(photoModel);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photos",(Serializable)single_photos);
                    bundle.putInt("position", 0);
                    bundle.putBoolean("isSave",false);
                    CommonUtils.launchActivity(getActivity(), PhotoPreviewActivity.class, bundle);
                }

//                Glide.with(getContext()).load(URLText.img_url + businessMessage.UserExtInfo.BusinessLicensePath)
//                        .transform(new CenterCrop(getContext()), new GlideRoundTransform(getContext(),5))
//                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                        .crossFade().into(slience_image);
                break;
            case R.id.zhegnshu_image:
                if (null != businessMessage.UserExtInfo.LicenseKeyPath){
                    single_photos.clear();
                    PhotoModel photoModel = new PhotoModel();
                    photoModel.setOriginalPath(businessMessage.UserExtInfo.LicenseKeyPath);
                    single_photos.add(photoModel);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photos",(Serializable)single_photos);
                    bundle.putInt("position", 0);
                    bundle.putBoolean("isSave",false);
                    CommonUtils.launchActivity(getActivity(), PhotoPreviewActivity.class, bundle);
                }
                break;
            case R.id.zxing_image://微信点击查看大图
                if (null != businessMessage.WeChatImagePath){
                    single_photos.clear();
                    PhotoModel photoModel = new PhotoModel();
                    photoModel.setOriginalPath(businessMessage.WeChatImagePath);
                    single_photos.add(photoModel);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photos",(Serializable)single_photos);
                    bundle.putInt("position", 0);
                    bundle.putBoolean("isSave",false);
                    CommonUtils.launchActivity(getActivity(), PhotoPreviewActivity.class, bundle);
                }
                break;
                //网址
            case R.id.website:
                sreachUrl();
                break;
            default:
                break;
        }
    }

    private void sreachUrl() {
        String edt = website.getText().toString().trim();
        if (edt.startsWith("https") || edt.startsWith("http")) {
            mUrl = website.getText().toString();
        } else {
            mUrl = "http://" + edt;
        }
        Intent intent = new Intent(getActivity(), Html5Activity.class);
        if (!TextUtils.isEmpty(edt)) {
            Bundle bundle = new Bundle();
            bundle.putString("url", mUrl);
            intent.putExtra("bundle", bundle);
        }
        startActivity(intent);
    }

    /**
     * 点击客服 - 选择计时交流 - 留言
     */
    private void showSetIconWindow() {
        menuWindow = new SelectPopupWindow(getActivity(), itemsOnClick);
        menuWindow.showAtLocation(seiverse, "及时交流", "留言");
    }

    /**
     * 弹出窗口实现监听类
     */
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
            menuWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_one://及时交流
//                    getServiceList();
                    nowPlayPhone();
                    menuWindow.dismiss();
                    break;
                case R.id.btn_two://留言
                    Intent leave = new Intent(getActivity(), LeaveMessageActivity.class);
                    leave.putExtra("Id", businessMessage.Id);
                    startActivity(leave);
                    menuWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 状态及时交流
     */
    public void nowPlayPhone(){
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
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "是否确认拨打021-58366991？", "确认", R.style.CustomDialog_1, callBack, 2);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * 得到客服列表
     */
    private void getServiceList() {
        StringEntity stringEntity = null;
        try {
            stringEntity = new StringEntity("");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        WebRequestHelper.json_post(getActivity(), URLText.SERVICE_LIST, stringEntity, new MyAsyncHttpResponseHandler(getActivity()) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String string = new String(responseBody);
                ServiceList serviceList = GsonUtils.fromJson(string, ServiceList.class);
                Serive_list = serviceList.MainData;
                if (Serive_list.size() > 0 && null != Serive_list) {
                    Intent send = new Intent(getActivity(), ChatActivity.class);
                    send.putExtra(EaseConstant.EXTRA_USER_ID, Serive_list.get(0).Id);
                    startActivity(send);
                }
            }
        });
    }

    /**
     * 未通过审核 - 进行一个弹框操作
     */
    public void dialog(final String phone){
        DialogCallBack callBack = new DialogCallBack() {
            @Override
            public void OkDown(Dialog dialog) {
                dialog.cancel();
                dialog.dismiss();
//                initCallPhone(phone);
                if (null != phone){
                    call(phone);
                }
            }
            @Override
            public void CancleDown(Dialog dialog) {
                dialog.cancel();
            }
        };
        Dialog dialog = new XiMaiPopDialog(getActivity(), "温馨提示", "是否确认拨打 "+phone+" ?", "确认", R.style.CustomDialog_1, callBack, 2);
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
     * 视频展示的适配器
     */
    private GridImageAdapterNoDelete.onAddPicClickListener onAddPicClickListener = new GridImageAdapterNoDelete.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
//            boolean mode = cb_mode.isChecked();
            if (true) {
                // 进入相册 以下是例子：不需要的api可以不写
                PictureSelector.create(getActivity())
                        .openGallery(PictureMimeType.ofVideo())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_white_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(4)// 每行显示个数
//                        .selectionMode(cb_choose_mode.isChecked() ? PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
                        .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
//                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
                        .previewImage(true)// 是否可预览图片
//                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
                        .previewVideo(true)// 是否可预览视频
//                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                        .enablePreviewAudio(true) // 是否可播放音频
//                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                        //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
//                        .enableCrop(cb_crop.isChecked())// 是否裁剪
                        .enableCrop(true)// 是否裁剪
//                        .compress(cb_compress.isChecked())// 是否压缩
                        .compress(true)// 是否压缩
                        .synOrAsy(true)//同步true或异步false 压缩 默认同步
                        //.compressSavePath(getPath())//压缩图片保存地址
                        //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(false)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(false)// 裁剪框是否可拖拽
                        .circleDimmedLayer(false)// 是否圆形裁剪
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .openClickSound(false)// 是否开启点击声音
                        .selectionMedia(selectList)// 是否传入已选图片
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                        //.cropCompressQuality(90)// 裁剪压缩质量 默认100
                        .minimumCompressSize(100)// 小于100kb的图片不压缩
                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                        //.rotateEnabled() // 裁剪是否可旋转图片
                        //.scaleEnabled()// 裁剪是否可放大缩小图片
                        //.videoQuality()// 视频录制质量 0 or 1
//                        .videoSecond()//显示多少秒以内的视频or音频也可适用
                        .recordVideoSecond(14)//录制视频秒数 默认60s
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            } else {
                // 单独拍照
//                PictureSelector.create(AddGoodsAcitivyt.this)
//                        .openCamera(chooseMode)// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
//                        .theme(themeId)// 主题样式设置 具体参考 values/styles
//                        .maxSelectNum(maxSelectNum)// 最大图片选择数量
//                        .minSelectNum(1)// 最小选择数量
//                        .selectionMode(cb_choose_mode.isChecked() ?
//                                PictureConfig.MULTIPLE : PictureConfig.SINGLE)// 多选 or 单选
//                        .previewImage(cb_preview_img.isChecked())// 是否可预览图片
//                        .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
//                        .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
//                        .isCamera(cb_isCamera.isChecked())// 是否显示拍照按钮
//                        .enableCrop(cb_crop.isChecked())// 是否裁剪
//                        .compress(cb_compress.isChecked())// 是否压缩
//                        .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                        .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
//                        .isGif(cb_isGif.isChecked())// 是否显示gif图片
//                        .freeStyleCropEnabled(cb_styleCrop.isChecked())// 裁剪框是否可拖拽
//                        .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                        .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                        .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                        .openClickSound(cb_voice.isChecked())// 是否开启点击声音
//                        .selectionMedia(selectList)// 是否传入已选图片
//                        .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
//                        //.previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
//                        //.cropCompressQuality(90)// 裁剪压缩质量 默认为100
//                        .minimumCompressSize(100)// 小于100kb的图片不压缩
//                        //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
//                        //.rotateEnabled() // 裁剪是否可旋转图片
//                        //.scaleEnabled()// 裁剪是否可放大缩小图片
//                        //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()////显示多少秒以内的视频or音频也可适用
//                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
            }
        }
    };
}