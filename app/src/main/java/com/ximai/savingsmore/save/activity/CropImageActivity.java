package com.ximai.savingsmore.save.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.ximai.savingsmore.R;
import com.ximai.savingsmore.library.FileSystem.FileSystem;
import com.ximai.savingsmore.library.toolbox.ImageUtils;
import com.ximai.savingsmore.library.toolbox.LogUtils;
import com.ximai.savingsmore.save.common.BaseActivity;
import com.ximai.savingsmore.save.modle.LoginUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * 裁剪图片
 */
public class CropImageActivity extends BaseActivity {

    private static final int SIZE_DEFAULT = 512;
    private static final int SIZE_LIMIT = 1024;
    CropImageView cropImageView = null;

    private Uri sourceUri;
    private Uri saveUri;
    private int sampleSize;

    boolean isSaving = false;
    private Handler myhandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private void saveImage(Bitmap croppedImage) {
        if (croppedImage != null) {
            final Bitmap b = croppedImage;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    saveOutput(b);
                }
            }).start();

        }
    }


    private void saveOutput(Bitmap croppedImage) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException e) {
                setResult(RESULT_CANCELED);
            } finally {
                ImageUtils.closeSilently(outputStream);
            }

            ImageUtils.copyExifRotation(
                    ImageUtils.getFromMediaUri(this, getContentResolver(), sourceUri),
                    ImageUtils.getFromMediaUri(this, getContentResolver(), saveUri)
            );

            setResultUri(saveUri);
        }

        final Bitmap b = croppedImage;
        myhandle.post(new Runnable() {
            public void run() {
                b.recycle();
            }
        });
        finish();
    }


    private void setResultUri(Uri uri) {
        setResult(RESULT_OK, new Intent().putExtra(MediaStore.EXTRA_OUTPUT, uri));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropper);
        setLeftBackMenuVisibility(CropImageActivity.this, "返回");
        cropImageView = (CropImageView) findViewById(R.id.CropImageView);

        if (getIntent().getBooleanExtra("isCrop", true)) {
            cropImageView.setFixedAspectRatio(true);
            cropImageView.setAspectRatio(30, 30);
            cropImageView.setGuidelines(CropImageView.GUIDELINES_ON);
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            cropImageView.setGuidelines(CropImageView.GUIDELINES_OFF);
        }

        addRightTextMenu("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cropImageView == null || isSaving)
                    return;
                try {
                    Bitmap croppedImage = cropImageView.getCroppedImage();
                    saveImage(croppedImage);
                } catch (Exception e) {
                    LoginUser.showToast(CropImageActivity.this, "图片过大，请重试！", Toast.LENGTH_SHORT);
                }
            }
        });


        Bitmap photo = null;
        Uri uri = getIntent().getData();
        if (uri == null) {
            //use bundle to get data  
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                photo = (Bitmap) bundle.get("data"); //get bitmap
            } else {
                LoginUser.showToast(getApplicationContext(), "获取图片失败!", Toast.LENGTH_LONG);
                setResult(RESULT_CANCELED);
                finish();
            }
        }

        if (uri != null) {
            try {
                photo = loadInput(uri);
            } catch (Exception e) {
               // LoginUser.showToast(getApplicationContext(), "解析失败!!", Toast.LENGTH_LONG);
                setResult(RESULT_CANCELED);
                finish();
            }
        }

        if (null != photo) {
            cropImageView.setImageBitmap(photo);
            saveUri = Uri.fromFile(new File(FileSystem.getCachesDir(CropImageActivity.this, true).getAbsolutePath(), System.currentTimeMillis() + ".jpg"));
        } else {
            finish();
        }
    }


    private Bitmap loadInput(Uri sourceUri) {
        if (sourceUri != null) {
            InputStream is = null;
            try {
                sampleSize = calculateBitmapSampleSize(sourceUri);
                is = getContentResolver().openInputStream(sourceUri);
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = sampleSize;

                return BitmapFactory.decodeStream(is, null, option);

            } catch (IOException e) {
                return null;

            } catch (OutOfMemoryError e) {
                return null;
            } finally {
                ImageUtils.closeSilently(is);
            }
        }

        return null;
    }

    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            //异常
            is = getContentResolver().openInputStream(bitmapUri);
            BitmapFactory.decodeStream(is, null, options); // Just get image size
        } catch (FileNotFoundException e) {
            LogUtils.instance.d("错误提示=" + e.getMessage());
        } finally {
            ImageUtils.closeSilently(is);
        }

        int maxSize = getMaxImageSize();
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private int getMaxImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return SIZE_DEFAULT;
        } else {
            return Math.min(textureLimit, SIZE_LIMIT);
        }
    }

    private int getMaxTextureSize() {
        // The OpenGL texture size is the maximum size that can be drawn in an ImageView
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }
}
