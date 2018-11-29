package com.ximai.savingsmore.library.toolbox;

import android.text.TextUtils;
import android.util.Log;

import com.ximai.savingsmore.library.constants.AppConstants;

/**
 * 日志记录（标签  方法 行号 消息）
 *
 * @author wangguodong
 */
public class LogUtils {

    public static LogUtils instance = new LogUtils();

    public void i(String msg) {
        if (AppConstants.DEBUG) {

            String tempStr = getFunctionName();
            if (!TextUtils.isEmpty(tempStr))
                msg = msg + "[++++++++]" + tempStr;


            Log.i(AppConstants.LOG_TAG, msg);
        }
    }

    public void d(String msg) {
        if (AppConstants.DEBUG) {
            String tempStr = getFunctionName();
            if (!TextUtils.isEmpty(tempStr))
                msg = msg + "[++++++++]" + tempStr;


            Log.d(AppConstants.LOG_TAG, msg);
        }
    }

    public void e(String msg) {
        if (AppConstants.DEBUG) {
            String tempStr = getFunctionName();
            if (!TextUtils.isEmpty(tempStr))
                msg = msg + "[++++++++]" + tempStr;


            Log.e(AppConstants.LOG_TAG, msg);
        }
    }

    public void v(String msg) {
        if (AppConstants.DEBUG) {

            String tempStr = getFunctionName();
            if (!TextUtils.isEmpty(tempStr))
                msg = msg + "[++++++++]" + tempStr;


            Log.v(AppConstants.LOG_TAG, msg);
        }
    }

    public void i(String TAG, String msg) {
        if (AppConstants.DEBUG) {
            String tempStr = getFunctionName();
            if (!TextUtils.isEmpty(tempStr))
                msg = msg + "[++++++++]" + tempStr;


            Log.i(TAG, msg);
        }
    }

    public void d(String TAG, String msg) {
        if (AppConstants.DEBUG) {
            String tempStr = getFunctionName();
            if (!TextUtils.isEmpty(tempStr))
                msg = msg + "[++++++++]" + tempStr;


            Log.d(TAG, msg);
        }
    }

    public void e(String TAG, String msg) {
        if (AppConstants.DEBUG) {
            String tempStr = getFunctionName();
            if (!TextUtils.isEmpty(tempStr))
                msg = msg + "[++++++++]" + tempStr;


            Log.e(TAG, msg);
        }
    }

    public void v(String TAG, String msg) {

        if (AppConstants.DEBUG) {

            String tempStr = getFunctionName();
            if (!TextUtils.isEmpty(tempStr))
                msg = msg + "[++++++++]" + tempStr;

            Log.v(TAG, msg);
        }


    }


    /**
     * Get The Current Function Name
     */
    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(LogUtils.this.getClass().getName())) {
                continue;
            }
            return "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " "
                    + st.getMethodName() + " ]";
        }
        return null;
    }


}
