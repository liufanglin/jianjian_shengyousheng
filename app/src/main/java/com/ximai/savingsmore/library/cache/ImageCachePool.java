package com.ximai.savingsmore.library.cache;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.ximai.savingsmore.library.FileSystem.FileSystem;

import java.io.File;
import java.io.IOException;

public class ImageCachePool {


    private static UsingFreqLimitedMemoryCache memoryCache = null;
    private static int memoryCacheSize = (int) (Runtime.getRuntime()
            .maxMemory() / 1024 / 10);
    private static int diskCacheSize = 100 * 1024 * 1024;


    public static void initImageLoader(Context context) {
        File cacheDir = FileSystem.getCachesDir(context, true);

        LruDiscCache discCache = null;
        try {

            discCache = new LruDiscCache(cacheDir, new Md5FileNameGenerator(),
                    diskCacheSize);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).memoryCacheExtraOptions(480, 800)
                // 不配置使用默认手机尺寸
                .threadPoolSize(3)
                        // 线程池大小3
                .threadPriority(Thread.NORM_PRIORITY - 2)
                        // 默认配置
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                        // 默认配置
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(getMemoryCache())// 如果缓存的图片总量超过限定值，先删除使用频率最小的bitmap
                .memoryCacheSize(memoryCacheSize)
                .diskCache(
                        discCache == null ? new UnlimitedDiscCache(cacheDir)
                                : discCache).diskCacheSize(diskCacheSize)
                        // .diskCacheFileCount(300)// 缓存的文件数量
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // 将保存的时候的URI名称用MD5加密
                        // .writeDebugLogs()// 正式版本要移除此处
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static UsingFreqLimitedMemoryCache getMemoryCache() {
        if (memoryCache == null)
            memoryCache = new UsingFreqLimitedMemoryCache(memoryCacheSize);
        return memoryCache;

    }

}
