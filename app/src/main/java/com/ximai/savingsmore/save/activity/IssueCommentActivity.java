package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
 * Created by caojian on 16/12/1.
 */
public class IssueCommentActivity extends BaseActivity implements View.OnClickListener {
    private ImageView cha, yiban, mangyi, henmangyi, qianglie;
    private ImageView kouwei1, kouwei2, kouwei3, kouwei4, kouwei5;
    private ImageView fuwu1, fuwu2, fuwu3, fuwu4, fuwu5;
    private ImageView huanjing1, huanjing2, huanjing3, huanjing4, huanjing5;
    private List<Images> images = new ArrayList<Images>();
    private MyGridView my_goods_GV;
    private String goodId;
    private String productScore;
    private String sellerScore1, sellerScore2, sellerScore3;
    private String Content;
    private boolean IsAnonymous;
    private EditText comment_conent;
    private TextView wenzi_number;
    private ImageView select;
    private RelativeLayout back;
    private RelativeLayout title＿right;

    private int screen_widthOffset;
    private GridImgAdapter gridImgAdapter;
    private ArrayList<UploadGoodsBean> img_uri = new ArrayList<>();//方格图片展示的图片数据
    private List<PhotoModel> single_photos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.issue_comment_activity);

        initView();

        initData();

        initEvent();
    }

    /**
     * view
     */
    private void initView() {
        toolbar.setVisibility(View.GONE);
        select = (ImageView) findViewById(R.id.select);
        back = (RelativeLayout) findViewById(R.id.back);
        title＿right = (RelativeLayout) findViewById(R.id.title＿right);
        comment_conent = (EditText) findViewById(R.id.comment_conent);
        wenzi_number = (TextView) findViewById(R.id.wenzi_number);
        cha = (ImageView) findViewById(R.id.cha);
        yiban = (ImageView) findViewById(R.id.yiban);
        mangyi = (ImageView) findViewById(R.id.mangyi);
        henmangyi = (ImageView) findViewById(R.id.henmangyi);
        qianglie = (ImageView) findViewById(R.id.qinglie);
        my_goods_GV = (MyGridView) findViewById(R.id.myGridview);
        kouwei1 = (ImageView) findViewById(R.id.kouwei1);
        kouwei2 = (ImageView) findViewById(R.id.kouwei2);
        kouwei3 = (ImageView) findViewById(R.id.kouwei3);
        kouwei4 = (ImageView) findViewById(R.id.kouwei4);
        kouwei5 = (ImageView) findViewById(R.id.kouwei5);
        fuwu1 = (ImageView) findViewById(R.id.fuwu1);
        fuwu2 = (ImageView) findViewById(R.id.fuwu2);
        fuwu3 = (ImageView) findViewById(R.id.fuwu3);
        fuwu4 = (ImageView) findViewById(R.id.fuwu4);
        fuwu5 = (ImageView) findViewById(R.id.fuwu5);
        huanjing1 = (ImageView) findViewById(R.id.huanjing1);
        huanjing2 = (ImageView) findViewById(R.id.huanjing2);
        huanjing3 = (ImageView) findViewById(R.id.huanjing3);
        huanjing4 = (ImageView) findViewById(R.id.huanjing4);
        huanjing5 = (ImageView) findViewById(R.id.huanjing5);
    }

    /**
     * event
     */
    private void initEvent() {
        select.setOnClickListener(this);
        title＿right.setOnClickListener(this);
        back.setOnClickListener(this);
        cha.setOnClickListener(this);
        yiban.setOnClickListener(this);
        henmangyi.setOnClickListener(this);
        mangyi.setOnClickListener(this);
        qianglie.setOnClickListener(this);
        kouwei1.setOnClickListener(this);
        kouwei2.setOnClickListener(this);
        kouwei3.setOnClickListener(this);
        kouwei4.setOnClickListener(this);
        kouwei5.setOnClickListener(this);
        fuwu1.setOnClickListener(this);
        fuwu2.setOnClickListener(this);
        fuwu3.setOnClickListener(this);
        fuwu4.setOnClickListener(this);
        fuwu5.setOnClickListener(this);
        huanjing1.setOnClickListener(this);
        huanjing2.setOnClickListener(this);
        huanjing3.setOnClickListener(this);
        huanjing4.setOnClickListener(this);
        huanjing5.setOnClickListener(this);
        comment_conent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (comment_conent.length() <= 15) {
                    wenzi_number.setText("还需要输入" + (15 - comment_conent.length()) + "个字,即可发表");
                } else {
                    wenzi_number.setText("还需要输入0个字,即可发表");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * data
     */
    private void initData() {
        goodId = getIntent().getStringExtra("id");
        gridImgAdapter = new GridImgAdapter();
        /**
         * grdview加载
         */
        Config.ScreenMap = Config.getScreenSize(this, this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screen_widthOffset = (display.getWidth() - (5* DbTOPxUtils.dip2px(this, 2)))/5;
        my_goods_GV.setAdapter(gridImgAdapter);
        img_uri.add(null);
        gridImgAdapter.notifyDataSetChanged();
    }

    /**
     * 事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.title＿right:
                if (null != comment_conent) {
                    Content = comment_conent.getText().toString();
                }
                if (null != Content && Content.length() > 15) {
                    if (null != productScore && !TextUtils.isEmpty(productScore) && null != Content && !TextUtils.isEmpty(Content)) {
                        comitComment(goodId, productScore, sellerScore1, sellerScore2, sellerScore3, Content, images, IsAnonymous);
                    } else {
                        Toast.makeText(IssueCommentActivity.this, "信息没有填写完整", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(IssueCommentActivity.this, "评论内容不能少于15个字", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.select:
                if (IsAnonymous) {
                    select.setImageResource(R.mipmap.kuang);
                    IsAnonymous = false;
                } else {
                    select.setImageResource(R.mipmap.select_kuang);
                    IsAnonymous = true;
                }
                break;
            case R.id.cha:
                productScore = "1";
                cha.setBackgroundResource(R.mipmap.cha3);
                yiban.setBackgroundResource(R.mipmap.yiban13);
                mangyi.setBackgroundResource(R.mipmap.manyi13);
                henmangyi.setBackgroundResource(R.mipmap.henmanyi13);
                qianglie.setBackgroundResource(R.mipmap.qianglietuijian13);
                break;
            case R.id.yiban:
                productScore = "2";
                cha.setBackgroundResource(R.mipmap.cha13);
                yiban.setBackgroundResource(R.mipmap.yiban3);
                mangyi.setBackgroundResource(R.mipmap.manyi13);
                henmangyi.setBackgroundResource(R.mipmap.henmanyi13);
                qianglie.setBackgroundResource(R.mipmap.qianglietuijian13);
                break;
            case R.id.mangyi:
                productScore = "3";
                cha.setBackgroundResource(R.mipmap.cha13);
                yiban.setBackgroundResource(R.mipmap.yiban13);
                mangyi.setBackgroundResource(R.mipmap.manyi3);
                henmangyi.setBackgroundResource(R.mipmap.henmanyi13);
                qianglie.setBackgroundResource(R.mipmap.qianglietuijian13);
                break;
            case R.id.henmangyi:
                productScore = "4";
                cha.setBackgroundResource(R.mipmap.cha13);
                yiban.setBackgroundResource(R.mipmap.yiban13);
                mangyi.setBackgroundResource(R.mipmap.manyi13);
                henmangyi.setBackgroundResource(R.mipmap.henmanyi3);
                qianglie.setBackgroundResource(R.mipmap.qianglietuijian13);
                break;
            case R.id.qinglie:
                productScore = "5";
                cha.setBackgroundResource(R.mipmap.cha13);
                yiban.setBackgroundResource(R.mipmap.yiban13);
                mangyi.setBackgroundResource(R.mipmap.manyi13);
                henmangyi.setBackgroundResource(R.mipmap.henmanyi13);
                qianglie.setBackgroundResource(R.mipmap.qianglietuijian3);
                break;
            case R.id.kouwei1:
                sellerScore1 = "1";
                kouwei1.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei2.setBackgroundResource(R.mipmap.xiaolian13);
                kouwei3.setBackgroundResource(R.mipmap.xiaolian13);
                kouwei4.setBackgroundResource(R.mipmap.xiaolian13);
                kouwei5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.kouwei2:
                sellerScore1 = "2";
                kouwei1.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei2.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei3.setBackgroundResource(R.mipmap.xiaolian13);
                kouwei4.setBackgroundResource(R.mipmap.xiaolian13);
                kouwei5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.kouwei3:
                sellerScore1 = "3";
                kouwei1.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei2.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei3.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei4.setBackgroundResource(R.mipmap.xiaolian13);
                kouwei5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.kouwei4:
                sellerScore1 = "4";
                kouwei1.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei2.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei3.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei4.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.kouwei5:
                sellerScore1 = "5";
                kouwei1.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei2.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei3.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei4.setBackgroundResource(R.mipmap.xiaolian3);
                kouwei5.setBackgroundResource(R.mipmap.xiaolian3);
                break;
            case R.id.fuwu1:
                sellerScore2 = "1";
                fuwu1.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu2.setBackgroundResource(R.mipmap.xiaolian13);
                fuwu3.setBackgroundResource(R.mipmap.xiaolian13);
                fuwu4.setBackgroundResource(R.mipmap.xiaolian13);
                fuwu5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.fuwu2:
                sellerScore2 = "2";
                fuwu1.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu2.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu3.setBackgroundResource(R.mipmap.xiaolian13);
                fuwu4.setBackgroundResource(R.mipmap.xiaolian13);
                fuwu5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.fuwu3:
                sellerScore2 = "3";
                fuwu1.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu2.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu3.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu4.setBackgroundResource(R.mipmap.xiaolian13);
                fuwu5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.fuwu4:
                sellerScore2 = "4";
                fuwu1.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu2.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu3.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu4.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.fuwu5:
                sellerScore2 = "5";
                fuwu1.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu2.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu3.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu4.setBackgroundResource(R.mipmap.xiaolian3);
                fuwu5.setBackgroundResource(R.mipmap.xiaolian3);
                break;
            case R.id.huanjing1:
                sellerScore3 = "1";
                huanjing1.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing2.setBackgroundResource(R.mipmap.xiaolian13);
                huanjing3.setBackgroundResource(R.mipmap.xiaolian13);
                huanjing4.setBackgroundResource(R.mipmap.xiaolian13);
                huanjing5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.huanjing2:
                sellerScore3 = "2";
                huanjing1.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing2.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing3.setBackgroundResource(R.mipmap.xiaolian13);
                huanjing4.setBackgroundResource(R.mipmap.xiaolian13);
                huanjing5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.huanjing3:
                sellerScore3 = "3";
                huanjing1.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing2.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing3.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing4.setBackgroundResource(R.mipmap.xiaolian13);
                huanjing5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.huanjing4:
                sellerScore3 = "4";
                huanjing1.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing2.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing3.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing4.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing5.setBackgroundResource(R.mipmap.xiaolian13);
                break;
            case R.id.huanjing5:
                sellerScore3 = "5";
                huanjing1.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing2.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing3.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing4.setBackgroundResource(R.mipmap.xiaolian3);
                huanjing5.setBackgroundResource(R.mipmap.xiaolian3);
                break;
        }
    }

    /**
     * 回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        File srcFile = null;
        File outPutFile = null;
        Bitmap bm = null;
        File temFile = null;
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
                    if (img_uri.size() < 3) {
                        img_uri.add(null);
                    }
                    gridImgAdapter.notifyDataSetChanged();
                    /**
                     * 上传照片 -获取到本地的图片路径 - 转换成file - 进行上传
                     */
                    if (paths.size() > 0) {
                        for (int i = 0; i < paths.size(); i++) {
                            upLoadImage(new File(paths.get(i)), "Comment");
                        }
                    }
                }
                break;
            }
            super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 提交评论
     * @param Id
     * @param ProductScore
     * @param SellerScore
     * @param SellerScore2
     * @param SellerScore3
     * @param Content
     * @param images
     * @param isAnonymous
     */
    private void comitComment(String Id, String ProductScore, String SellerScore, String SellerScore2, String SellerScore3,
                              String Content, final List<Images> images, boolean isAnonymous) {
        WebRequestHelper.json_post(IssueCommentActivity.this, URLText.SUBMIT_COMMENT, RequestParamsPool.submitComment(Id, ProductScore, SellerScore, SellerScore2, SellerScore3, Content, images, isAnonymous), new MyAsyncHttpResponseHandler(IssueCommentActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                JSONObject object = null;
                try {
                    object = new JSONObject(new String(responseBody));
                    String IsSuccess = object.getString("IsSuccess");
                    String message = object.optString("Message");
                    if (IsSuccess.equals("true")) {
                        Intent intent = new Intent(IssueCommentActivity.this, CommentSuccessActivity.class);
                        if (images.size() > 0) {
                            intent.putExtra("jifen", "2");
                        }
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(IssueCommentActivity.this, message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 上传图片
     * @param file
     * @param type
     */
    private void upLoadImage(File file, final String type) {
        // a++;
        WebRequestHelper.post(URLText.UPLOAD_IMAGE, RequestParamsPool.upLoad(file, type), new MyAsyncHttpResponseHandler(IssueCommentActivity.this) {
            @Override
            public void onResponse(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    // b++;
                    JSONObject jsonObject = new JSONObject(result);
                    UpPhoto upPhoto = GsonUtils.fromJson(jsonObject.optString("MainData"), UpPhoto.class);
                    Images images1 = new Images();
                    images1.ImageId = upPhoto.Id;
                    images1.ImagePath = upPhoto.FilePath;
                    images1.SortNo = upPhoto.SortNo;
                    images.add(images1);
                    gridImgAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
            convertView = LayoutInflater.from(IssueCommentActivity.this).inflate(R.layout.activity_addstory_img_item, null);
            ViewHolder holder;
            if(convertView!=null){
                holder = new ViewHolder();
                convertView = LayoutInflater.from(IssueCommentActivity.this).inflate(R.layout.activity_addstory_img_item,null);
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
                    .transform(new GlideRoundTransform(IssueCommentActivity.this,5)).diskCacheStrategy(DiskCacheStrategy.ALL);
            if (img_uri.get(position) == null) {
                holder.delete_IV.setVisibility(View.GONE);
                Glide.with(IssueCommentActivity.this).load(R.mipmap.achieve_icon_addphoto_default)
                        .apply(options).into(holder.add_IB);
                holder.add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(IssueCommentActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 3 - (img_uri.size() - 1));
                        startActivityForResult(intent, 0);
                    }
                });
            } else {
                Glide.with(IssueCommentActivity.this).load(img_uri.get(position).getUrl())
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
                        images.remove(position);//这里添加对应的店铺展示图片集合
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
                        CommonUtils.launchActivity(IssueCommentActivity.this, PhotoPreviewActivity.class, bundle);
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