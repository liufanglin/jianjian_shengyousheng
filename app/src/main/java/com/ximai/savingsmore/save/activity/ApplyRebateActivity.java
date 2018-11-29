package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.net.MyAsyncHttpResponseHandler;
import com.ximai.savingsmore.library.net.RequestParamsPool;
import com.ximai.savingsmore.library.net.URLText;
import com.ximai.savingsmore.library.net.WebRequestHelper;
import com.ximai.savingsmore.library.toolbox.GsonUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.common.BaseApplication;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.UpPhoto;
import com.ximai.savingsmore.save.modle.UploadGoodsBean;
import com.ximai.savingsmore.save.view.GlideRoundTransform;
import com.ximai.savingsmore.save.view.MyGridView;
import com.ximai.savingsmore.save.view.imagepicker.PhotoPreviewActivity;
import com.ximai.savingsmore.save.view.imagepicker.PhotoSelectorActivity;
import com.ximai.savingsmore.save.view.imagepicker.model.PhotoModel;
import com.ximai.savingsmore.save.view.imagepicker.util.CommonUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.Config;
import com.ximai.savingsmore.save.view.imagepicker.util.DbTOPxUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.FileUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luxing on 2018/1/5 0005.
 * 申请返利
 */

public class ApplyRebateActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back;
    private MyGridView myGridview_1;
    private MyGridView myGridview_2;
    private EditText et_price;
    private EditText tv_location;
    private Button btn_apply;
    private Button btn_moneyapply;
    private ArrayList<UploadGoodsBean> fristList = new ArrayList<>();//方格图片展示的图片数据
    private ArrayList<UploadGoodsBean> secondList = new ArrayList<>();//方格图片展示的图片数据

    private List<Images> Fristimages = new ArrayList<>();//用来存储图片
    private List<Images> Secondimages = new ArrayList<>();//用来存储图片

    private List<PhotoModel> Fristsinglephotos = new ArrayList<>();//用来传递图片
    private List<PhotoModel> Secondsinglephotos = new ArrayList<>();//用来传递图片
    private int screen_widthOffset;
    private FristGridImgAdapter fristGridImgAdapter;
    private SecondGridImgAdapter secondGridImgAdapter;

    int a = 0, b = 0;

    private boolean isFrist = false;//是否是第一个方格进行图片上传
    private boolean isSecond = false;//是否是第一个方格进行图片上传

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_rebate);
        
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
        myGridview_1 = (MyGridView) findViewById(R.id.myGridview_1);//第一个方格
        myGridview_2 = (MyGridView) findViewById(R.id.myGridview_2);//第二个方格
        et_price = (EditText) findViewById(R.id.et_price);//输入金额
        tv_location = (EditText) findViewById(R.id.tv_location);//当钱的定位
        btn_apply = (Button) findViewById(R.id.btn_apply);//积分返利
        btn_moneyapply = (Button) findViewById(R.id.btn_moneyapply);//现金返利
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        btn_moneyapply.setOnClickListener(this);
    }

    /**
     * data
     */
    private void initData() {
        fristGridImgAdapter = new FristGridImgAdapter();//第一个方格适配器
        secondGridImgAdapter = new SecondGridImgAdapter();//第二个方格适配器

        /**
         * grdview加载
         */
        Config.ScreenMap = Config.getScreenSize(this, this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screen_widthOffset = (display.getWidth() - (5* DbTOPxUtils.dip2px(this, 2)))/5;

        myGridview_1.setAdapter(fristGridImgAdapter);
        myGridview_2.setAdapter(secondGridImgAdapter);
        fristList.add(null);
        secondList.add(null);
        fristGridImgAdapter.notifyDataSetChanged();
        secondGridImgAdapter.notifyDataSetChanged();

        /**
         * 设置地址信息
         */
        if (!TextUtils.isEmpty(BaseApplication.getInstance().userAddress)){
            tv_location.setText(BaseApplication.getInstance().userAddress);
        }
    }

    /**
     * event
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back://返回
                finish();
                break;
            case R.id.btn_apply://申请返利 - 积分返利
                if (Fristimages.size() == 0){
                    Toast.makeText(this, "请上传已购买商品图片", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (Secondimages.size() == 0){
                    Toast.makeText(this, "请上传消费账单图片", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (TextUtils.isEmpty(et_price.getText())){
                    Toast.makeText(this, "请输入本次消费金额", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    et_price.getText().toString();
                }

                if (TextUtils.isEmpty(tv_location.getText())){
                    Toast.makeText(this, "请手工输入具体地址", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    tv_location.getText().toString();
                }
                /**
                 * 保存信息
                 */
                save_message(et_price.getText().toString(),Fristimages,Secondimages,tv_location.getText().toString(),"1");
                break;
            case R.id.btn_moneyapply://现金返利
                if (Fristimages.size() == 0){
                    Toast.makeText(this, "请上传已购买商品图片", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (Secondimages.size() == 0){
                    Toast.makeText(this, "请上传消费账单图片", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (TextUtils.isEmpty(et_price.getText())){
                    Toast.makeText(this, "请输入本次消费金额", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    et_price.getText().toString();
                }

                if (TextUtils.isEmpty(tv_location.getText())){
                    Toast.makeText(this, "请手工输入具体地址", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    tv_location.getText().toString();
                }
                /**
                 * 保存信息
                 */
                save_message(et_price.getText().toString(),Fristimages,Secondimages,tv_location.getText().toString(),"2");
                break;
        }
    }

    /**
     * 方格店铺照片
     */
    class FristGridImgAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return fristList.size();
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
            convertView = LayoutInflater.from(ApplyRebateActivity.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if(convertView!=null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ApplyRebateActivity.this).inflate(R.layout.activity_addstory_img_item,null);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            holder.delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);
            if (fristList.get(position) == null) {
                holder.delete_IV.setVisibility(View.GONE);

                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(ApplyRebateActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(ApplyRebateActivity.this).load(R.mipmap.achieve_icon_addphoto_default).apply(options).into(holder.add_IB);

                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(ApplyRebateActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 4 - (fristList.size() - 1));
                        startActivityForResult(intent, 0);
                    }
                });
            } else {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(ApplyRebateActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(ApplyRebateActivity.this).load(fristList.get(position).getUrl())
                        .apply(options).into(holder.add_IB);

                holder.delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;
                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = fristList.remove(position).getUrl();
                        for (int i = 0; i < fristList.size(); i++) {
                            if (fristList.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            fristList.add(null);
                        }
                        FileUtils.DeleteFolder(img_url);//删除在emulate/0文件夹生成的图片
                        try{
                            Fristimages.remove(position);//这里添加对应的店铺展示图片集合
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                        fristGridImgAdapter.notifyDataSetChanged();
                    }
                });
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fristsinglephotos.clear();
                        for (int i = 0; i < Fristimages.size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(Fristimages.get(i).FilePath);
                            Fristsinglephotos.add(photoModel);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)Fristsinglephotos);
                        bundle.putInt("position", position);
                        bundle.putBoolean("isSave",false);
                        CommonUtils.launchActivity(ApplyRebateActivity.this, PhotoPreviewActivity.class, bundle);
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
     * 方格店铺照片第二个适配器
     */
    class SecondGridImgAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return secondList.size();
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
            convertView = LayoutInflater.from(ApplyRebateActivity.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if(convertView!=null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(ApplyRebateActivity.this).inflate(R.layout.activity_addstory_img_item,null);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            holder.delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);
            if (secondList.get(position) == null) {
                holder.delete_IV.setVisibility(View.GONE);
                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(ApplyRebateActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

                Glide.with(ApplyRebateActivity.this).load(R.mipmap.achieve_icon_addphoto_default).apply(options).into(holder.add_IB);
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(ApplyRebateActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 4 - (secondList.size() - 1));
                        startActivityForResult(intent, 1);
                    }
                });
            } else {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
//                        .placeholder(R.mipmap.head_image)
//                .error(R.mipmap.ic_launcher)
                        .priority(Priority.HIGH)
                        .transform(new GlideRoundTransform(ApplyRebateActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
                Glide.with(ApplyRebateActivity.this).load(secondList.get(position).getUrl()).apply(options).into(holder.add_IB);
                holder.delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;
                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = secondList.remove(position).getUrl();
                        for (int i = 0; i < secondList.size(); i++) {
                            if (secondList.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            secondList.add(null);
                        }
                        FileUtils.DeleteFolder(img_url);//删除在emulate/0文件夹生成的图片
                        try{
                            Secondimages.remove(position);//这里添加对应的店铺展示图片集合
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        secondGridImgAdapter.notifyDataSetChanged();
                    }
                });
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Secondsinglephotos.clear();
                        for (int i = 0; i < Secondimages.size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(Secondimages.get(i).FilePath);
                            Secondsinglephotos.add(photoModel);
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)Secondsinglephotos);
                        bundle.putInt("position", position);
                        bundle.putBoolean("isSave",false);
                        CommonUtils.launchActivity(ApplyRebateActivity.this, PhotoPreviewActivity.class, bundle);
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
     * activity回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:     //店铺方格图片进行回调
                isFrist = true;
                if (data != null) {
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");
                    if (fristList.size() > 0) {
                        fristList.remove(fristList.size() - 1);
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        fristList.add(new UploadGoodsBean(paths.get(i), false));
                        //上传参数
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.setOriginalPath(paths.get(i));
                        photoModel.setChecked(true);
                        Fristsinglephotos.add(photoModel);
                    }
                    if (fristList.size() < 9) {
                        fristList.add(null);
                    }
                    fristGridImgAdapter.notifyDataSetChanged();
                    /**
                     * 上传照片 -获取到本地的图片路径 - 转换成file - 进行上传
                     */
                    if (paths.size() > 0) {
                        for (int i = 0; i < paths.size(); i++) {
                            upLoadImage(new File(paths.get(i)), "Rebate");
                        }
                    }
                }
                break;
            case 1:     //店铺方格图片进行回调
                isSecond = true;
                if (data != null) {
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");
                    if (secondList.size() > 0) {
                        secondList.remove(secondList.size() - 1);
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        secondList.add(new UploadGoodsBean(paths.get(i), false));
                        //上传参数
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.setOriginalPath(paths.get(i));
                        photoModel.setChecked(true);
                        Secondsinglephotos.add(photoModel);
                    }
                    if (secondList.size() < 9) {
                        secondList.add(null);
                    }
                    secondGridImgAdapter.notifyDataSetChanged();
                    /**
                     * 上传照片 -获取到本地的图片路径 - 转换成file - 进行上传
                     */
                    if (paths.size() > 0){
                        for (int i = 0; i < paths.size(); i++) {
                            upLoadImage(new File(paths.get(i)),"Rebate");
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 上传照片
     * @param file      /storage/emulated/0/ximai/2017-12-20-11-12-20_11:14:57.png
     * @param type
     */
    private void upLoadImage(File file, final String type) {
        a++;
        WebRequestHelper.post(URLText.UPLOAD_IMAGE, RequestParamsPool.upLoad(file, type), new MyAsyncHttpResponseHandler(ApplyRebateActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    b++;
                    JSONObject jsonObject = new JSONObject(result);
                    UpPhoto upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                    if (isFrist == true){//第一个方格照片
                        isFrist = false;
                        Images images1 = new Images();
                        images1.Id = upPhoto.Id;
                        images1.FilePath = upPhoto.FilePath;
                        Fristimages.add(images1);
                        fristGridImgAdapter.notifyDataSetChanged();
                    }

                    if (isSecond == true){//第二个方格粘片上传
                        isSecond = false;
                        Images images1 = new Images();
                        images1.Id = upPhoto.Id;
                        images1.FilePath = upPhoto.FilePath;
                        Secondimages.add(images1);
                        secondGridImgAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tag","上传照片有误 - upLoadImage");
                }
            }
        });
    }

    /**
     * 保存信息 - 提交非促销品返利申请
     */
    private void save_message(String price, List<Images> Fristimages, List<Images> Secondimages, String location, final String RebateWay) {
        WebRequestHelper.json_post(ApplyRebateActivity.this, URLText.REBATEAPPLYDATA, RequestParamsPool.rebateApply(price,Fristimages,Secondimages,location,RebateWay), new MyAsyncHttpResponseHandler(ApplyRebateActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String IsSuccess = object.getString("IsSuccess");
                    String message = object.optString("Message");
                    JSONObject jsonObject = object.optJSONObject("MainData");//maindata

                    if (IsSuccess.equals("true")) {
                        if ("1".equals(RebateWay)){//积分返利
                            Intent intent = new Intent(ApplyRebateActivity.this,PointApplyActivity.class);
                            intent.putExtra("Price",jsonObject.optString("Price"));
                            intent.putExtra("StateName",jsonObject.optString("StateName"));
                            intent.putExtra("Point",jsonObject.optString("Point"));
                            intent.putExtra("State",jsonObject.optString("State"));
                            startActivity(intent);
                            finish();
                        }else if ("2".equals(RebateWay)){//现金返利
                            Intent intent8 = new Intent(ApplyRebateActivity.this,PersonalRewardActivity.class);
                            startActivity(intent8);
                            finish();
                        }
                    }
                    Toast.makeText(ApplyRebateActivity.this, "申请成功", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}