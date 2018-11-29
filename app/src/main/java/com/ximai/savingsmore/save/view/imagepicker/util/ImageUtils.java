package com.ximai.savingsmore.save.view.imagepicker.util;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageUtils {

    public static final int GET_IMAGE_BY_CAMERA = 5001;
    public static final int GET_IMAGE_FROM_PHONE = 5002;
    public static Uri imageUriFromCamera;
    private static File image_file;

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */
    public static Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaColumns.DISPLAY_NAME, imageName);
        values.put(ImageColumns.DATE_TAKEN, time);
        values.put(MediaColumns.MIME_TYPE, "image/jpeg");
        if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }
        Log.i("fyg", "生成的照片输出路径：" + imageFilePath.toString());
        return imageFilePath;
    }

    public static void loadImage(final Context context, String mImageUrl) {

        ImageLoader.getInstance().loadImage(mImageUrl,
                new ImageLoadingListener() {

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {

                        try {

                            if (Environment.getExternalStorageState().equals(
                                    Environment.MEDIA_MOUNTED)) {
                                image_file = new File(
                                        Environment
                                                .getExternalStorageDirectory()+"/imagepicker",
                                        (Math.round((Math.random() * 9 + 1) * 100000))
                                                + ".jpg");

                            } else {
                                image_file = new File(
                                        "/data/data/com.zzti.fsuper/imagepicker",
                                        (Math.round((Math.random() * 9 + 1) * 100000))
                                                + ".jpg");
                                if (!image_file.exists()) {
                                    image_file.mkdirs();
                                }
                            }

                            FileOutputStream out = new FileOutputStream(image_file);

                            if (loadedImage != null) {
                                loadedImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            }
                            out.flush();
                            out.close();

                            loadedImage.recycle();
                            loadedImage = null;
                            scanPhoto(context, image_file.getAbsolutePath());  //刷新到相册
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        // TODO Auto-generated method stub

                    }
                });
            }


    //压缩图片
    public static InputStream getimage(WindowManager windowManager,String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        double picHeight = newOpts.outWidth;
        double picWidth = newOpts.outHeight;
        //获取屏的宽度和高度
        Display display = windowManager.getDefaultDisplay();
        double screenHeight = display.getHeight();//这里获取屏幕高度
        double screenWidth = display.getWidth();//这里获取屏幕宽度

        ////isSampleSize是表示对图片的缩放程度，比如值为2图片的宽度和高度都变为以前的1/2
        newOpts.inSampleSize = 1;

        //根据屏的大小和图片大小计算出缩放比例
//        if (picWidth > picHeight) {
//            if (picWidth > screenWidth)
//                newOpts.inSampleSize = Double.valueOf((picWidth / screenWidth) + 0.5).intValue();
//        } else {
//            if (picHeight > screenHeight)
//                newOpts.inSampleSize = Double.valueOf((picHeight / screenHeight) + 0.5).intValue();
//        }
        if (picWidth > picHeight) {
            if (picHeight > screenHeight)
                newOpts.inSampleSize = Double.valueOf((picHeight / screenHeight) + 0.99).intValue();
        } else {
            if (picWidth > screenWidth)
                newOpts.inSampleSize = Double.valueOf((picWidth / screenWidth) + 0.99).intValue();
        }
//        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        newOpts.inPurgeable = true;
//        newOpts.inInputShareable = true;

        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        newOpts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }



    public static InputStream compressImage(Bitmap bitmap) {
        Bitmap c_bitmap = null ;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //压缩bitmap对象
//            Bitmap mBitmap = Bitmap.createScaledBitmap(bitmap, 160, 160, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            if(!(baos.toByteArray().length/1024 < 300)){
                baos.reset();
                //将图片转换成字节流
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//            }

//            int options = 100;
//            while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//                baos.reset();//重置baos即清空baos
//                image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//                options -= 10;//每次都减少10
//            }
            //将输出流转为输入流
            InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
//            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//            c_bitmap = BitmapFactory.decodeStream(sbs, null, null);
            return sbs;
        } catch (Exception e) {
            Log.e("ImageUtils",e.getMessage());
            return null;
        }

    }


    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
    /**
     * 将图片按照某个角度进行旋转
     *
     * @param is
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static InputStream rotateBitmapByDegree(InputStream is, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap bm = BitmapFactory.decodeStream(is);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (Exception e) {
            // TODO: handle exception
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        returnBm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }


    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }



    // URI转绝对路径
    public static String getAbsoluteImagePath(Activity activity,Uri uri) {
        // can post image
        String[] proj = { MediaColumns.DATA };
        Cursor cursor = activity.managedQuery(uri, proj,null,null,null);

        int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }


    public static Bitmap convertToBitmap(String path, int w, int h) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.f, scaleHeight = 0.f;
        if (width > w || height > h) {
            // 缩放
            scaleWidth = ((float) width) / w;
            scaleHeight = ((float) height) / h;
        }
        opts.inJustDecodeBounds = false;
        float scale = Math.max(scaleWidth,scaleHeight);
        opts.inSampleSize = (int)scale;
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(path, opts));
        return Bitmap.createScaledBitmap(weak.get(), w, h, true);
    }


    public static void scanPhoto(Context context,String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

}
