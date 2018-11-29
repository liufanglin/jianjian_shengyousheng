package com.ximai.savingsmore.library.toolbox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");
//    public static final SimpleDateFormat DATE_FORMAT_YEAR   = new SimpleDateFormat("yyyy年MM月dd号");
    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     * 
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    public static Long timeToLong(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(time);
        long longDate = date.getTime();
        return  longDate;
    }
    public static String formatTimeInString(long millis) {
        
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        
        return format.format(millis);
    }
    public static String formatTimeToCNString(long millis) {

        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");

        return format.format(millis);
    }
    
    public static String formatTimeNotYear(long millis) {

        SimpleDateFormat format=new SimpleDateFormat("MM-dd");

        return format.format(millis);
    }
    public static String formatTimeYearAndMonth(long millis) {

        SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");

        return format.format(millis);
    }

    public static String dayForWeek(String pTime) throws Exception {
        final String dayNames[] = { "周一", "周二", "周三", "周四", "周五", "周六",
                "周日" };
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayNames[dayForWeek - 1];
    }

}
