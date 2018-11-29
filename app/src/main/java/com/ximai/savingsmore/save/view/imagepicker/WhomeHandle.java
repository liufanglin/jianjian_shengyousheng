package com.ximai.savingsmore.save.view.imagepicker;

import android.content.Context;

/**
 * Created by PascalGuo on 2017/3/29.
 */

public class WhomeHandle {
    private static Utils mUtilsHandle;
    private static Context mcontext;

    public static void setContext(Context context){
        mcontext = context;
    }

    public static Context getContext(){
        return mcontext;
    }

    public static Utils getUtilsHandle(){
        if(mUtilsHandle == null){
            synchronized(WhomeHandle.class){
                mUtilsHandle = new Utils();
            }
        }
        return mUtilsHandle;
    }


}
