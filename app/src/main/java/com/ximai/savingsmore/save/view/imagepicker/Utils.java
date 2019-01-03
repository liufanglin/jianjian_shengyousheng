package com.ximai.savingsmore.save.view.imagepicker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ximai.savingsmore.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;



/**
 * Created by PascalGuo on 2017/3/29.
 */

public class Utils {

    public void initMainBarUsedImage( Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);


            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
    }

    public void initMainBar(
            Activity activity,int color )
    {
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
        {
            setTranslucentStatus( activity , true );
        }
        SystemBarTintManager tintManager = new SystemBarTintManager( activity );
        tintManager.setStatusBarTintEnabled( true );
        // 使用颜色资源
        tintManager.setStatusBarTintResource( color );
        if(color == R.color.white) {
            setMiuiStatusBarDarkMode(activity, true);
            setMeizuStatusBarDarkIcon(activity, true);
        }else{
            setMiuiStatusBarDarkMode(activity, false);
            setMeizuStatusBarDarkIcon(activity, false);
        }
    }

    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素) ,再四舍五入
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public void setFullScreen(
            Activity activity, boolean enable)
    {
        if (enable)
        {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        else
        {
            WindowManager.LayoutParams attr = activity.getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attr);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
    @TargetApi( 19 )
    private void setTranslucentStatus(
            Activity activity ,
            boolean on )
    {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if( on )
        {
            winParams.flags |= bits;
        }
        else
        {
            winParams.flags &= ~bits;
        }
        win.setAttributes( winParams );
    }

    /**
     *用在提交数组的时候.把"转化成转意字符
     */
    public String getSumitArrString(String value){
        if(value == null)
            return value;
        return value.replaceAll("\"", "\\\\\"");
    }

    public static String formatContentDate(long time)
    {
        SimpleDateFormat format = new SimpleDateFormat( "MM-dd HH:mm" );
        format.setTimeZone( TimeZone.getTimeZone( "GMT+08:00" ) );
        String data = format.format( new Date( time ) );
        return data;
    }
    /*
    *  专业解析服务器时间,服务器返回的格式是yyyyMMdd hhmmss
    *  返回时间MM-dd mm:ss
    * */
    public static String serverDateFormat(String datestr){
        if(datestr == null){
            return "";
        }
        SimpleDateFormat informat = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat outformat = new SimpleDateFormat("MM-dd HH:mm");
        String outStr = "";
        try {
            Date date = informat.parse(datestr);
            outStr = outformat.format(date);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return outStr;
    }
    /*
    * 需求里面上传给服务器的时间为yyyyMMdd
    * */
    public static String toServerDateFormat(String datestr){
        if(datestr == null){
            return "";
        }
        SimpleDateFormat informat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat outformat = new SimpleDateFormat("yyyyMMdd");
        String outStr = "";
        try {
            Date date = informat.parse(datestr);
            outStr = outformat.format(date);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return outStr;
    }
    /*
     * 需求里面上传给服务器的时间为yyyyMMdd
     * */
    public static String toServerDateFormat2(String datestr){
        if(datestr == null){
            return "";
        }
        SimpleDateFormat informat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat outformat = new SimpleDateFormat("yyyyMMdd");
        String outStr = "";
        try {
            Date date = informat.parse(datestr);
            outStr = outformat.format(date);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return outStr;
    }
    /*
     * 需求里面上传给服务器的时间HHmmss
     * */
    public static String toServerDateFormat3(String datestr){
        if(datestr == null){
            return "";
        }
        SimpleDateFormat informat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat outformat = new SimpleDateFormat("HHmmss");
        String outStr = "";
        try {
            Date date = informat.parse(datestr);
            outStr = outformat.format(date);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return outStr;
    }
    /*
   * 需求里面上传给服务器的时间HHmmss
   * */
    public static String toServerDateFormat4(String datestr){
        if(datestr == null){
            return "";
        }
        SimpleDateFormat informat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat outformat = new SimpleDateFormat("MM-dd");
        String outStr = "";
        try {
            Date date = informat.parse(datestr);
            outStr = outformat.format(date);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return outStr;
    }

    // 比较时间
    //curTime .就传手机的时候,为System.currentTimeMillis(),
    //serverTime 为yyyyMMddHHmmss
    private static  boolean compare(Activity activity,Long curTime,String ServerTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date dt1 = sdf.parse(ServerTime);
            Date curDate = new Date(curTime);
            if (dt1.getTime() > curDate.getTime()) {
                return true;
            } else if (dt1.getTime() < curDate.getTime()) {
                return false;
            } else {// 相等
                System.out.println("相等");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));
    }

    /*
         * 将时间转换为时间戳
         */
    public static long dateToStamp(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long ts = date.getTime();
        return ts;
    }




}
