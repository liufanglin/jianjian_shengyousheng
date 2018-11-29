package com.ximai.savingsmore.save.view.imagepicker.control;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;

/**
 * 监听系统图库变化
 * Created by Administrator on 2017/6/2 0002.
 */

public class PhotoPachContentObserver extends ContentObserver {
    private final Context context;
    private final Handler handler;

    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public PhotoPachContentObserver(Context context, Handler handler) {
        super(handler);
        this.context = context;
        this.handler = handler;
    }

    /**
     * 主要在onChange中响应数据库变化，并进行相应处理
     */
    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        /*---------------进行相应处理----------------*/
        Log.i("PhotoPachContentObserver", "database is changed!------------------------------------------");

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, "_data desc");

        if (cursor != null) {
            Log.i("PhotoPachContentObserver", "The number of data is:" + cursor.getCount());

            StringBuffer sb = new StringBuffer();

            while (cursor.moveToNext()) {
                String fileName = cursor.getString(cursor.getColumnIndex("_data"));
                String[] a = fileName.split("/");
                Log.i("PhotoPachContentObserver", a[a.length - 2] + a[a.length - 1]);  //观察输出地目录名/文件名
                sb.append("目录名称：" + a[a.length - 2]);
            }
            cursor.close();
        /*将消息传递给主线程，消息中绑定了目录信息*/
            handler.obtainMessage(0x110, sb.toString()).sendToTarget();
        }
    }
}
