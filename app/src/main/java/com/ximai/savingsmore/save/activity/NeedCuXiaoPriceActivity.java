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
import com.ximai.savingsmore.save.common.Constants;
import com.ximai.savingsmore.save.modle.Images;
import com.ximai.savingsmore.save.modle.UpPhoto;
import com.ximai.savingsmore.save.modle.UploadGoodsBean;
import com.ximai.savingsmore.save.utils.NotificationCenter;
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
 * Created by luxing on 2018/1/8 0008.
 * 我要促销价
 */

public class NeedCuXiaoPriceActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout back;
    private MyGridView myGridview;
    private EditText et_price;
    private Button btn_cancel;
    private Button btn_commit;

    private int screen_widthOffset;
    private ArrayList<UploadGoodsBean> img_uri = new ArrayList<>();//方格图片展示的图片数据
    private List<Images> images = new ArrayList<>();//上传图片数据
    private List<PhotoModel> single_photos = new ArrayList<>();
    private GridImgAdapter gridImgAdapter;
    private int a;
    private int b;
    private List<String> cartListId;
    private Images images1;
    private String sellerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need_cuxiaojia);
        initView();
        initEvent();
        initData();
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
        myGridview = (MyGridView) findViewById(R.id.myGridview);//gridview
        et_price = (EditText) findViewById(R.id.et_price);//输入的价格
        btn_cancel = (Button) findViewById(R.id.btn_cancel);//取消申请
        btn_commit = (Button) findViewById(R.id.btn_commit);//提交申请
    }

    /**
     * event
     */
    private void initEvent() {
        back.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
    }

    /**
     * data
     */
    private void initData() {
        //购物车Id - 添加到购物车不需要，修改hi需要
        cartListId = (List<String>) getIntent().getSerializableExtra("cartListId");

        sellerId = getIntent().getStringExtra("SellerId");//商家Id

        gridImgAdapter = new GridImgAdapter();

        /**
         * grdview加载
         */
        Config.ScreenMap = Config.getScreenSize(this, this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screen_widthOffset = (display.getWidth() - (5* DbTOPxUtils.dip2px(this, 2)))/5;
        myGridview.setAdapter(gridImgAdapter);
        img_uri.add(null);
        gridImgAdapter.notifyDataSetChanged();
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
            case R.id.btn_cancel://取消
                finish();
                break;
            case R.id.btn_commit://提交申请
                if (images.size() == 0){
                    Toast.makeText(this, "请上传商品正面照片", Toast.LENGTH_SHORT).show();
                    break;
                }

                if (TextUtils.isEmpty(et_price.getText())){
                    Toast.makeText(this, "请输入商品价格", Toast.LENGTH_SHORT).show();
                    break;
                }

                /**
                 * 调用接口
                 */
                needCuXiaoPrice(sellerId,null,images1.Id ,images1.FilePath,et_price.getText().toString(),"1","1");
                break;
            default:
                break;
        }
    }

    /**
     * 方格店铺照片
     */
    class GridImgAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return img_uri.size();
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
            convertView = LayoutInflater.from(NeedCuXiaoPriceActivity.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if(convertView!=null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(NeedCuXiaoPriceActivity.this).inflate(R.layout.activity_addstory_img_item,null);
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
                    .transform(new GlideRoundTransform(NeedCuXiaoPriceActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);

            if (img_uri.get(position) == null) {
                holder.delete_IV.setVisibility(View.GONE);
                Glide.with(NeedCuXiaoPriceActivity.this).load(R.mipmap.achieve_icon_addphoto_default)
                        .apply(options).into(holder.add_IB);
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(NeedCuXiaoPriceActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 1 - (img_uri.size() - 1));
                        startActivityForResult(intent, 0);
                    }
                });
            } else {
                Glide.with(NeedCuXiaoPriceActivity.this).load(img_uri.get(position).getUrl())
                        .apply(options).into(holder.add_IB);
                holder.delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;
                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = img_uri.remove(position).getUrl();
                        for (int i = 0; i < img_uri.size(); i++) {
                            if (img_uri.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            img_uri.add(null);
                        }
                        FileUtils.DeleteFolder(img_url);//删除在emulate/0文件夹生成的图片
                        try{
                            images.remove(position);//这里添加对应的店铺展示图片集合
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        gridImgAdapter.notifyDataSetChanged();
                    }
                });
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putBoolean("isSave",false);
                        CommonUtils.launchActivity(NeedCuXiaoPriceActivity.this, PhotoPreviewActivity.class, bundle);
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
     * 方格照片进行回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:     //店铺方格图片进行回调
                if (data != null) {
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");
                    if (img_uri.size() > 0) {
                        img_uri.remove(img_uri.size() - 1);
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        img_uri.add(new UploadGoodsBean(paths.get(i), false));
                        //上传参数
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.setOriginalPath(paths.get(i));
                        photoModel.setChecked(true);
                        single_photos.add(photoModel);
                    }
                    if (img_uri.size() < 9) {
                        img_uri.add(null);
                    }
                    gridImgAdapter.notifyDataSetChanged();
                    /**
                     * 上传照片 -获取到本地的图片路径 - 转换成file - 进行上传
                     */
                    if (paths.size() > 0) {
                        for (int i = 0; i < paths.size(); i++) {
                            upLoadImage(new File(paths.get(i)), "Order");
                        }
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 上传照片
     * @param file
     * @param type
     */
    private void upLoadImage(File file, final String type) {
        a++;
        WebRequestHelper.post(URLText.UPLOAD_IMAGE, RequestParamsPool.upLoad(file, type), new MyAsyncHttpResponseHandler(NeedCuXiaoPriceActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    b++;
                    JSONObject jsonObject = new JSONObject(result);
                    UpPhoto upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                    images1 = new Images();
                    images1.Id = upPhoto.Id;
                    images1.FilePath = upPhoto.FilePath;
                    images.add(images1);
                    gridImgAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("tag","上传照片有误 - upLoadImage");
                }
            }
        });
    }

    /**
     * 非促销产品添加到购物车
     */
    private void needCuXiaoPrice(String SellerId,String CartId, String ImageId,String ImagePath,String Price,String Quantity, String CartOperaType) {
        WebRequestHelper.json_post(NeedCuXiaoPriceActivity.this, URLText.NEED_CUXIAOJIA, RequestParamsPool.needCuXiaoPrice(SellerId,CartId, ImageId,ImagePath, Price,Quantity,CartOperaType), new MyAsyncHttpResponseHandler(NeedCuXiaoPriceActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject object = new JSONObject(new String(responseBody));
                    String success = object.optString("IsSuccess");
                    String message = object.optString("Message");
                    Toast.makeText(NeedCuXiaoPriceActivity.this, message, Toast.LENGTH_SHORT).show();
                    NotificationCenter.defaultCenter().postNotification(Constants.LOADING_ORDERFRAGMENT,"");
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}