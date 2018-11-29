package com.ximai.savingsmore.save.view.imagepicker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ximai.savingsmore.R;
import com.ximai.savingsmore.save.utils.UIUtils;
import com.ximai.savingsmore.save.view.imagepicker.adapter.AlbumAdapter;
import com.ximai.savingsmore.save.view.imagepicker.adapter.PhotoSelectorAdapter;
import com.ximai.savingsmore.save.view.imagepicker.control.PhotoPachContentObserver;
import com.ximai.savingsmore.save.view.imagepicker.control.PhotoSelectorDomain;
import com.ximai.savingsmore.save.view.imagepicker.model.AlbumModel;
import com.ximai.savingsmore.save.view.imagepicker.model.PhotoModel;
import com.ximai.savingsmore.save.view.imagepicker.util.AnimationUtil;
import com.ximai.savingsmore.save.view.imagepicker.util.CommonUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.FileUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.ImageUtils;
import com.ximai.savingsmore.save.view.imagepicker.util.StringUtils;
import com.ximai.savingsmore.save.view.imagepicker.view.LoadingDialog;
import com.ximai.savingsmore.save.view.imagepicker.view.SelectPhotoItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by fengyongge on 2016/5/24
 * 图片选择
 * 调用的时候需要用intent传入2个值
 * limit    可以选择几张图片（默认为1张图）
 * 支持选择照片不压缩，需要传入ISCOMPRESS值为NOCOMPRESS
 */
public class PhotoSelectorActivity extends Activity implements SelectPhotoItem.onItemClickListener, SelectPhotoItem.onPhotoItemCheckedListener,
        OnItemClickListener, OnClickListener {
    public static final String RECCENT_PHOTO = "最近照片";
    public static final String ISCOMPRESS = "ISCOMPRESS";
    public static final String NOCOMPRESS = "1";
    private String isCompress;
    private List<PhotoModel> single_photos = null;
    private GridView gvPhotos;
    private ListView lvAblum;
    private TextView btnOk;
    private RelativeLayout tvAlbum_R;
    private LoadingDialog loadDialog;
    private TextView tvAlbum, tvPreview, tvTitle;
    private PhotoSelectorDomain photoSelectorDomain;
    private PhotoSelectorAdapter photoAdapter;
    private AlbumAdapter albumAdapter;
    private RelativeLayout layoutAlbum;
    public static final ArrayList<PhotoModel> selected = new ArrayList<>();
    private static final int REQUEST_CAMERA = 0;//相机请求码
    private static final int REQUEST_PHOTO = 1;//相册请求码

    //监听图库刷新类
    private PhotoPachContentObserver mPhotoPachContentObserver;
    //图库刷新回调
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x110) {
                Log.i("PhotoSelectorActivity", "message is back! " + msg.obj.toString());
                photoSelectorDomain.getReccent(reccentListener); // 更新最近照片
            }
        }
    };
    private ArrayList<String> img_path = new ArrayList<>();
    private String path_name;
    private int limit;
    private int min;//最少选几张
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent data = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("photos", img_path);
            data.putExtras(bundle);
            setResult(RESULT_OK, data);
            dialogDismiss();
            finish();
        };
    };

    public boolean isArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photoselector);

        //保证静态状态刚进来的时候数据为空
        selected.clear();
        ToolbarUtil.setColorNoTranslucent(this, 0);
        Intent data = getIntent();
        limit = data.getIntExtra("limit", 1);
        min = data.getIntExtra("min", 0);
        isCompress = data.getStringExtra(ISCOMPRESS);
        boolean mIsClean = data.getBooleanExtra("clean", false);
//        if(mIsClean){
//            FileUtils.deleteDirectory(FileUtils.SDCARD_PAHT + "/" + getPackageName() + "/.image/");
//            // 最后通知图库更新
//            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file:/" + FileUtils.SDCARD_PAHT + "/" + getPackageName() + "/")));
//        }
        photoSelectorDomain = new PhotoSelectorDomain(PhotoSelectorActivity.this); // 跟新相册信息
        //标题文本
        tvTitle = (TextView) findViewById(R.id.tv_title_lh);
        //照片GridView
        gvPhotos = (GridView) findViewById(R.id.gv_photos_ar);
        //
        lvAblum = (ListView) findViewById(R.id.lv_ablum_ar);
        btnOk = (TextView) findViewById(R.id.btn_right_lh);
        tvAlbum_R = (RelativeLayout) findViewById(R.id.tv_album);
        tvAlbum = (TextView) findViewById(R.id.tv_album_ar);
        tvPreview = (TextView) findViewById(R.id.tv_preview_ar);
        layoutAlbum = (RelativeLayout) findViewById(R.id.layout_album_ar);
        layoutAlbum.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        tvAlbum_R.setOnClickListener(this);
        tvPreview.setOnClickListener(this);
        photoAdapter = new PhotoSelectorAdapter(getApplicationContext(), new ArrayList<PhotoModel>(),
                CommonUtils.getWidthPixels(this), this, this, this, limit);
        gvPhotos.setAdapter(photoAdapter);
        albumAdapter = new AlbumAdapter(getApplicationContext(), new ArrayList<AlbumModel>());
        lvAblum.setAdapter(albumAdapter);
        lvAblum.setOnItemClickListener(this);
        findViewById(R.id.bv_back_lh).setOnClickListener(this); // 返回
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPhotoPermission(); // 权限未被授予
        } else {
            photoSelectorDomain.getReccent(reccentListener); // 更新最近照片
            photoSelectorDomain.updateAlbum(albumListener); // 跟新相册信息
        }
        listenDB();
    }

    @Override
    public void onResume() {
        super.onResume();
        WhomeHandle.getUtilsHandle().initMainBar(PhotoSelectorActivity.this, R.color.white);
    }

    public void dialogShow() {
//        ToastUtil.showGifLoading(this,"");
//        if (loadDialog == null) {
//            loadDialog = new LoadingDialog(this);
//        }
//        loadDialog.setCanceledOnTouchOutside(false);
//        loadDialog.setCancelable(true);
//        loadDialog.startAnimation();
//        if (loadDialog.isShowing()) {
//            return;
//        }
//        if (!isFinishing()) {
//            loadDialog.show();
//        }
    }



    public void dialogDismiss() {
//        ToastUtil.hideGifLoadiing();
//        if (loadDialog != null && loadDialog.isShowing()) {
//            loadDialog.dismiss();
//            loadDialog.stopAnimation();
//
//        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_right_lh) {
            if (min >= 0 && min >= selected.size()) {
                if (limit == 0){
                    Toast.makeText(this, "已达上限", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "最少要选择1张图片", Toast.LENGTH_LONG).show();
                }
            } else {
                btnOk.setEnabled(false);
                dialogShow();
                ok();// 选完照片
            }
        } else if (v.getId() == R.id.tv_album) {
            album();
        } else if (v.getId() == R.id.tv_preview_ar) {
            priview();
        } else if (v.getId() == R.id.tv_camera_vc) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestCameraPermission(); //权限未被授予
            } else {
                catchPicture();
            }
        } else if (v.getId() == R.id.bv_back_lh) {
            setResult(Activity.RESULT_OK, null);
            finish();
        }
    }

    /**
     * 申请相机权限
     */
    private void requestCameraPermission() {
        final List<String> permissionsList2 = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionsList2.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList2.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionsList2.size() > 0) {
            ActivityCompat.requestPermissions((Activity) this, permissionsList2.toArray(new String[permissionsList2.size()]), REQUEST_CAMERA);
        }
    }

    /**
     * 申请相册权限
     */
    private void requestPhotoPermission() {
        final List<String> permissionsList2 = new ArrayList<String>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList2.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionsList2.size() > 0) {
            ActivityCompat.requestPermissions(this, permissionsList2.toArray(new String[permissionsList2.size()]), REQUEST_PHOTO);
        }
    }

    /**
     * 拍照
     */
    private void catchPicture() {
        path_name = "image" + (Math.round((Math.random() * 9 + 1) * 100000)) + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //设置路径为默认相册路径
//        File dirFile = new File(Environment.getExternalStorageDirectory()+"/"+getPackageName()+"/image");
        //设置路径为默认相册路径
        File dirFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        //判断路径是否存在
        if (!dirFile.exists()) {
            //不存在 创建该路径
            dirFile.mkdirs();
        }
        //设置图片存放的名字和路径
//        String path = Environment.getExternalStorageDirectory()+"/"+getPackageName()+"/image/"+path_name;
        String path = dirFile + "/" + path_name;
        //创建文件对象
        File file = new File(path);
        try {
            //创建该文件
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri uri = null;
        // 把刚保存的图片文件插入到系统相册
//        try {
//            MediaStore.Images.Media.insertImage(PhotoSelectorActivity.this.getContentResolver(),
//                    file.getAbsolutePath(), file.getName(), null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        //获取刚刚创建的文件uri
        if (Build.VERSION.SDK_INT >= 24) {
            //通过FileProvider创建一个content类型的Uri
            uri = FileProvider.getUriForFile(PhotoSelectorActivity.this, UIUtils.getPackageName() + ".fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        //设置意图参数
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        //执行带返回值的意图
        startActivityForResult(intent, 2);
    }


    private String cropImage(InputStream is) {
        if (is == null) {
            return null;
        } else {
            String path_name = FileUtils.SDCARD_PAHT + "/whome/.image/" + System.currentTimeMillis() + ".jpg";
//            FileUtils.writeImage(cropImage, path_name, 100);
//            FileUtils.writeFile(path_name,is);
            FileUtils.writeFileFromIS(new File(path_name), is, true);
            return path_name;
        }

    }

    //注册内容观察者
    private void registerContentObserver() {

    /*之前说过，非常关键的Uri*/
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        getContentResolver().registerContentObserver(imageUri, false, mPhotoPachContentObserver);
        Log.i("PhotoSelectorActivity", "registered!---------------------------");
    }

    //监听系统内容
    private void listenDB() {
        mPhotoPachContentObserver = new PhotoPachContentObserver(PhotoSelectorActivity.this, mHandler);
        registerContentObserver();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                Log.i("path_name", "path_name " + path_name);
                if (path_name == null) {
                    Toast.makeText(PhotoSelectorActivity.this, "PhotoSelectorActivity", Toast.LENGTH_SHORT).show();
                    return;
                }
                File takefile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), path_name);
//                File takefile = new File(Environment.getExternalStorageDirectory()+"/"+getPackageName()+"/image/", path_name);
                PhotoModel photoModel = new PhotoModel(takefile.getAbsolutePath());
//                selected.clear();
//                selected.add(photoModel);
                onCheckedChanged(photoModel, null, true);
                // 最后通知图库更新
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(takefile)));


//				ok();
            }
        }
    }


    /**
     * 完成
     */
    private void ok() {
        if (selected.isEmpty()) {
            setResult(RESULT_CANCELED);
            dialogDismiss();
            finish();
        } else {
            img_path.clear();
            new Thread() {
                private String cropImage;

                @Override
                public void run() {
                    for (int i = 0; i < selected.size(); i++) {
                        if (isCompress!=null && isCompress.equals(NOCOMPRESS)) {
                            //不压缩
                            try {
                                int degree = ImageUtils.getBitmapDegree(selected.get(i).getOriginalPath());
                                if (degree == 0) {
                                    cropImage = cropImage(new FileInputStream(selected.get(i).getOriginalPath()));
                                } else {
                                    cropImage = cropImage(ImageUtils.rotateBitmapByDegree(new FileInputStream(selected.get(i).getOriginalPath()), degree));
                                }
                                if (StringUtils.isNotEmpty(cropImage)) {
                                    img_path.add(cropImage);
                                }
                            } catch (FileNotFoundException e) {
                            }
                        } else {
                            //压缩
                            //防止拍照图片角度发生变化(三星)
                            int degree = ImageUtils.getBitmapDegree(selected.get(i).getOriginalPath());
                            if (degree == 0) {
                                cropImage = cropImage(ImageUtils.getimage(getWindowManager(), selected.get(i).getOriginalPath()));
                            } else {
                                cropImage = cropImage(ImageUtils.rotateBitmapByDegree(ImageUtils.getimage(getWindowManager(), selected.get(i).getOriginalPath()), degree));
                            }
                            if (StringUtils.isNotEmpty(cropImage)) {
                                img_path.add(cropImage);
                            }
                        }
                    }
                    handler.sendEmptyMessage(0);
                }
            }.start();
        }
    }

    /**
     * 预览照片
     */
    private void priview() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("photos", selected);
        CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
    }

    private void album() {
        if (layoutAlbum.getVisibility() == View.GONE) {
            popAlbum();
        } else {
            hideAlbum();
        }
    }

    /**
     * 弹出相册列表
     */
    private void popAlbum() {
        layoutAlbum.setVisibility(View.VISIBLE);
        new AnimationUtil(getApplicationContext(), R.anim.translate_up_current).setLinearInterpolator().startAnimation(
                layoutAlbum);
    }

    /**
     * 隐藏相册列表
     */
    private void hideAlbum() {
        new AnimationUtil(getApplicationContext(), R.anim.translate_down).setLinearInterpolator().startAnimation(
                layoutAlbum);
        layoutAlbum.setVisibility(View.GONE);


    }

    /**
     * 清空选中的图片
     */
    private void reset() {
        selected.clear();
        tvPreview.setText("预览");
        tvPreview.setEnabled(false);
    }

    @Override
    /** 点击查看照片 */
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        if (tvAlbum.getText().toString().equals(RECCENT_PHOTO)) {
            bundle.putInt("position", position - 1);
        } else {
            bundle.putInt("position", position);
        }
        bundle.putString("album", tvAlbum.getText().toString());
        CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
    }


    @Override
    /** 照片选中状态改变之后 */
    public void onCheckedChanged(PhotoModel photoModel, CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            selected.add(photoModel);
            tvPreview.setEnabled(true);
        } else {
            selected.remove(photoModel);
        }
        tvPreview.setText("预览(" + selected.size() + ")");  //修改预览数量
        btnOk.setText("下一步(" + selected.size() + ")");  //修改预览数量

        if (selected.isEmpty()) {
            tvPreview.setEnabled(false);
            tvPreview.setText("预览");
            btnOk.setText("下一步");
        }
    }

    @Override
    public void onBackPressed() {
        if (layoutAlbum.getVisibility() == View.VISIBLE) {
            hideAlbum();
        } else {
            setResult(Activity.RESULT_OK, null);
            ;//图片和光影秀界面需要根据这个值直接关闭界面
            super.onBackPressed();
        }
    }

    @Override
    /** 相册列表点击事件 */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
        for (int i = 0; i < parent.getCount(); i++) {
            AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
            if (i == position) {
                album.setCheck(true);
            } else {
                album.setCheck(false);
            }
        }
        albumAdapter.notifyDataSetChanged();
        hideAlbum();
        tvAlbum.setText(current.getName());
        tvTitle.setText(current.getName());

        // 更新照片列表
        if (current.getName().equals(RECCENT_PHOTO))
            photoSelectorDomain.getReccent(reccentListener);
        else
            photoSelectorDomain.getAlbum(current.getName(), reccentListener); // 获取选中相册的照片
    }

    /**
     * 获取本地图库照片回调
     */
    public interface OnLocalReccentListener {
        public void onPhotoLoaded(List<PhotoModel> photos);
    }

    /**
     * 获取本地相册信息回调
     */
    public interface OnLocalAlbumListener {
        public void onAlbumLoaded(List<AlbumModel> albums);
    }

    private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
        @Override
        public void onAlbumLoaded(List<AlbumModel> albums) {
            albumAdapter.update(albums);
        }
    };
    private OnLocalReccentListener reccentListener = new OnLocalReccentListener() {
        @Override
        public void onPhotoLoaded(List<PhotoModel> photos) {
            if (tvAlbum.getText().equals(RECCENT_PHOTO))
                photos.add(0, new PhotoModel());

            if (single_photos == null) {
                single_photos = new ArrayList<>();
                single_photos.addAll(photos);
            } else {
//                for(int i = photos.size() - single_photos.size(); i >= 0;i--){
                if (single_photos.size() < photos.size()) {
                    if (selected.size() <= limit) {
                        photos.get(1).setChecked(true);
                        Log.e("PhotoSelectorActivity", "11111111111111111111111");
                    }
                    single_photos.add(1, photos.get(1));
//                    single_photos.addAll(0,photos);
                }
//                }
            }
            photoAdapter.update(single_photos);

//            single_photos.clear();
//            single_photos.addAll(photos);
//            photoAdapter.update(photos);
            gvPhotos.smoothScrollToPosition(0); // 滚动到顶端
//            reset();
        }
    };


}
