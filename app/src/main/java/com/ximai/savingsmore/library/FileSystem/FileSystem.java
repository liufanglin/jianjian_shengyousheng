package com.ximai.savingsmore.library.FileSystem;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
/**
 * @author wangguodong
 */
public class FileSystem {

    private static final String FileDir = "file";
    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static void init(Context context) {
        try {


            File fileDir = getFileDir(context);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }

            File cacheDir = getCachesDir(context, true);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getFileDir(Context context) {
        return context.getExternalFilesDir(FileDir);
    }


    public static File getCachesDir(Context context, boolean preferExternal) {

        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            externalStorageState = "";
        }
        if (preferExternal && Environment.MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = context.getExternalCacheDir();
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
