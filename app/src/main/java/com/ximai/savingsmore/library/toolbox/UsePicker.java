package com.ximai.savingsmore.library.toolbox;


import android.content.Context;
import android.widget.TimePicker;

import com.ximai.savingsmore.library.pickerview.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * Created by cx on 16/5/8.
 */
public class UsePicker {
    public static TimePickerView pvTime;

    /**
     * 只拿年月日
     * @param context
     */

    public static void showYearMonthDay(Context context ,final  CallBack call,String text){
        pvTime = new TimePickerView(context, TimePickerView.Type.YEAR_MONTH_DAY,text);
       // Date date = new Date();
        pvTime.show();
       // pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);

        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                String strAll = getTime(date); //这里显示的是yyyy-mm-dd 00：00 分，去掉00:00
                final String[] strYear = strAll.split(" ");
                call.callBack(strYear[0]);
            }
        });
    }

    /**
     * 年月日时分秒都显示
     * @param context
     *
     */

    public static void showAll(Context context,final CallBack call,String text){

        pvTime = new TimePickerView(context, TimePickerView.Type.ALL,text);
        pvTime.show();

    //  pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {

                call.callBack(getTime(date));
            }
        });
    }
    public static void showHuors(Context context,final CallBack call,String text){

        pvTime = new TimePickerView(context, TimePickerView.Type.HOURS_MINS,text);
        pvTime.show();
        //  pvTime.setTime(new Date());
        pvTime.setCyclic(true);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                call.callBack(getDate(date));
            }
        });
    }

    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }
    public static  String getDate(Date date){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public interface CallBack{
        public void callBack(String time);
    }

}

