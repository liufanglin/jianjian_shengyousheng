package com.ximai.savingsmore.library.cache;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author wangguodong "http://site.com/image.png" // from Web
 * "file:///mnt/sdcard/image.png" // from SD card
 * "file:///mnt/sdcard/video.mp4" // from SD card (video thumbnail)
 * "content://media/external/images/media/13" // from content provider
 * "content://media/external/video/media/13" // from content provider (video thumbnail)
 * "assets://image.png" // from assets
 * "drawable://" + R.drawable.img // from drawables (non-9patch images)
 */
public class MyImageLoader {

    
    
    /**
     * 显示头像  使用自定义view：RoundNetworkImageView
     *
     * @param url
     * @param image
     * @param isFemale
     */
    public static void displayAvatarImage(String url, ImageView image, boolean isFemale) {

        ImageLoader.getInstance().displayImage(url,image, MyDisplayImageOptions.getDefaultAvatarDisplayOptions(isFemale));

        ImageLoader.getInstance().displayImage(url, image, MyDisplayImageOptions.getDefaultAvatarDisplayOptions(isFemale));
    }

    /**
     * 显示普通图片
     *
     * @param url
     * @param image
     */
    public static void displayDefaultImage(String url, ImageView image) {

        ImageLoader.getInstance().displayImage(url, image, MyDisplayImageOptions.getDefaultImageDisplayOptions());

    }

    /**
     * 显示商品图片
     *
     * @param url
     * @param image
     */
    public static void displayGoodImage(String url, ImageView image) {

        ImageLoader.getInstance().displayImage(url, image, MyDisplayImageOptions.getDefaultGoodImageDisplayOptions());

    }
    
    //显示无默认图片
    public static void displayNoDefaultImage(String url, ImageView image) {

        ImageLoader.getInstance().displayImage(url, image, MyDisplayImageOptions.getNoDefaultImageDisplayOptions());

    }

    /**
     * 显示圆角图片
     *
     * @param url
     * @param image
     */
    public static void displayRoundCornerImage(String url, ImageView image, int cornerRadiusPixels, boolean isfemale) {

        ImageLoader.getInstance().displayImage(url, image, MyDisplayImageOptions.getRoundCornerAvatarDisplayOptions(cornerRadiusPixels, isfemale));


    }

    /**
     * 删除图片缓存 文件和内存
     */

    public static void clearImageCache() {

        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();

    }

    /**
     * 下载图片
     *
     * @param url
     * @param listener
     */
    public static void downloadImage(String url, ImageLoadingListener listener) {
        ImageLoader.getInstance().loadImage(url, listener);

    }
}