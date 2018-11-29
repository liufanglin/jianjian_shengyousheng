package com.ximai.savingsmore.library.cache;

import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.ximai.savingsmore.library.constants.AppConstants;


/**
 * @author wangguodong
 */
public class MyDisplayImageOptions {



    /**
     * 获得圆角的图片显示选项
     * @return
     */
    public static DisplayImageOptions getRoundCornerAvatarDisplayOptions(int cornerRadiusPixels,boolean isFemale) {

        int defaultResId=0;

        if (isFemale)
            defaultResId = AppConstants.DEFAULT_GOOD_IMAGE;
        else
            defaultResId = AppConstants.DEFAULT_GOOD_IMAGE;

        
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultResId) // 加载时的图片
                .showImageForEmptyUri(defaultResId) // uri空的时候图片
                .showImageOnFail(defaultResId) // 加载失败的图片
                .resetViewBeforeLoading(false) // 默认配置
                .delayBeforeLoading(10).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在sdcard中
                .considerExifParams(false) // 默认配置
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)) // 默认配置
                .build();
        return options;
    }




    /**
     * 获得头像的图片显示选项
     * @return
     */
    public static DisplayImageOptions getDefaultAvatarDisplayOptions(boolean isFemale) {

        int defaultResId=0;

        if (isFemale)
            defaultResId = AppConstants.DEFAULT_GOOD_IMAGE;
        else
            defaultResId = AppConstants.DEFAULT_GOOD_IMAGE;



        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defaultResId) // 加载时的图片
                .showImageForEmptyUri(defaultResId) // uri空的时候图片
                .showImageOnFail(defaultResId) // 加载失败的图片
                .resetViewBeforeLoading(false) // 默认配置
                .delayBeforeLoading(10).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在sdcard中
                .considerExifParams(false) // 默认配置
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).displayer(new SimpleBitmapDisplayer()) // 默认配置
                .build();
        return options;
    }

    public static DisplayImageOptions getNoDefaultImageDisplayOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .resetViewBeforeLoading(false) // 默认配置
                .delayBeforeLoading(10).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在sdcard中
                .considerExifParams(false) // 默认配置
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).displayer(new SimpleBitmapDisplayer()) // 默认配置
                .build();
        return options;
    }
    /**
     * 获得普通的图片显示选项
     * @return
     */
    public static DisplayImageOptions getDefaultImageDisplayOptions() {


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(AppConstants.DEFAULT_DISPLAY_IMAGE) // 加载时的图片
                .showImageForEmptyUri(AppConstants.DEFAULT_DISPLAY_IMAGE) // uri空的时候图片
                .showImageOnFail(AppConstants.DEFAULT_DISPLAY_IMAGE) // 加载失败的图片
                .resetViewBeforeLoading(false) // 默认配置
                .delayBeforeLoading(10).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在sdcard中
                .considerExifParams(false) // 默认配置
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).displayer(new SimpleBitmapDisplayer()) // 默认配置
                .build();
        return options;
    }


    /**
     * 获得商品的图片显示选项
     * @return
     */
    public static DisplayImageOptions getDefaultGoodImageDisplayOptions() {


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(AppConstants.DEFAULT_GOOD_IMAGE) // 加载时的图片
                .showImageForEmptyUri(AppConstants.DEFAULT_GOOD_IMAGE) // uri空的时候图片
                .showImageOnFail(AppConstants.DEFAULT_GOOD_IMAGE) // 加载失败的图片
                .resetViewBeforeLoading(false) // 默认配置
                .delayBeforeLoading(10).bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .cacheInMemory(true)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在sdcard中
                .considerExifParams(false) // 默认配置
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT).displayer(new SimpleBitmapDisplayer()) // 默认配置
                .build();
        return options;
    }

}
