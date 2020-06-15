package com.da.library.tools;

import android.annotation.SuppressLint;
import android.content.Context;

import com.da.library.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Author: 方志
 * Time: 2018/5/21 15:30
 * Description:时间工具类
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

    public static final long ONE_DATY_TMIE = 24 * 60 * 60 * 1000;
    //private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateHourMinuteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat dateHourFormat = new SimpleDateFormat("yyyyMMddHH");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static Calendar mCalendar = null;

    static {
        mCalendar = Calendar.getInstance();
    }

    /**
     * 获取当前时间
     * @return long
     */
    private static long getCurrentTime() {
        return mCalendar.getTime().getTime();
    }

    /**
     * 毫秒转化字符串时间戳
     * @param time 号码
     * @return String
     */
    public static String timeToString(long time){
        Date date = new Date(time);
        return dateHourMinuteFormat.format(date);
    }

    /**
     * 获取某年某月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    public static int getDayOfMonth(int year, int month) {
        mCalendar.set(year, month, 0); //输入类型为int类型
        return mCalendar.get(Calendar.DAY_OF_MONTH);
    }

//    /**
//     * 获取某年
//     *
//     * @return
//     */
//    public static List<String> getWheelYear(Context context,Calendar now) {
//        int years = now.get(Calendar.YEAR);
//        List<String> listYear = new ArrayList<>();
//        for (int i = 3; i >= 0; i--) {
//            listYear.add(String.valueOf(years - i)+context.getString(R.string.year));
//        }
//        return listYear;
//    }
//
//    /**
//     * 获取某月
//     *
//     * @return
//     */
//    public static List<String> getWheelMonth(Context context) {
//        List<String> listMonth = new ArrayList<>();
//        for (int i = 1; i < 13; i++) {
//            listMonth.add(String.valueOf(i)+context.getString(R.string.mouth));
//        }
//        return listMonth;
//    }
//
//    /**
//     * 获取某日
//     *
//     * @return
//     */
//    public static List<String> getWheelDate(Context context) {
//        int years = mCalendar.get(Calendar.YEAR);
//        int month= mCalendar.get(Calendar.MONTH) + 1;
//        List<String> listDate = new ArrayList<>();
//        int dayOfMonth = getDayOfMonth(years, month);
//        listDate.add("无");
//        for (int i = 1; i < dayOfMonth + 2 ; i++) {
//            listDate.add(String.valueOf(i) + context.getString(R.string.day));
//        }
//        return listDate;
//    }
//
//    /**
//     * 获取某日
//     *
//     * @return
//     */
//    public static List<String> getWheelDateList(Context context,int years,int month) {
//        List<String> listDate = new ArrayList<>();
//        int dayOfMonth = getDayOfMonth(years, month);
//        listDate.add("无");
//        for (int i = 1; i < dayOfMonth + 1; i++) {
//            listDate.add(String.valueOf(i)+context.getString(R.string.day));
//        }
//        return listDate;
//    }
//    /**
//     * 年格式化
//     */
//    public static String getFormatYear(String mYearStr){
//        return mYearStr.substring(0,mYearStr.length()-1);
//    }
//    /**
//     * 月格式化
//     */
//    public static String getFormatMonth(String mMonth){
//        String mMonths=mMonth.substring(0,mMonth.length()-1);
//        return Utils.dateFormat(mMonths);
//    }
//    /**
//     * 日格式化
//     */
//    public static String getFormatDate(Context context,String mDateStr){
//        if (context.getString(R.string.none).equals(mDateStr)){
//            return "";
//        }else{
//            String mDates=mDateStr.substring(0,mDateStr.length()-1);
//            return Utils.dateFormat(mDates);
//        }
//    }

    /**
     * 转化时间为：今天／昨天／日期
     */
    public static String transferTime(long time) {
        time = time * 1000;
        Date targetDate = new Date(time);

        long curTime = System.currentTimeMillis();
        Date curDate = new Date(curTime);
//        int todayHours = curDate.getHours() * 60 * 60;
//        int todayMinutes = curDate.getMinutes() * 60;
//        int todaySeconds = curDate.getSeconds();
        int todayHours = mCalendar.get(Calendar.HOUR_OF_DAY);
        int todayMinutes = mCalendar.get(Calendar.MINUTE);
        int todaySeconds = mCalendar.get(Calendar.SECOND);
        int todayMillis = (todayHours + todayMinutes + todaySeconds) * 1000;

        long todayStartMillis = curTime - todayMillis;
        if (time >= todayStartMillis) {
            return "今天";
        } else {
            long yesterdayStartMilis = todayStartMillis - ONE_DATY_TMIE;
            if (time >= yesterdayStartMilis) {
                return "昨天";
            }
        }
        return dateFormat.format(targetDate);
    }

    /**
     * 转化时间为：今天／昨天／日期
     *
     * @param time 秒
     */
    public static String transferTimeNew(long time) {
        time = time * 1000;
        Date targetDate = new Date(time);

        long curTime = System.currentTimeMillis();
        Date curDate = new Date(curTime);
        int todayHours = curDate.getHours() * 60 * 60;
        int todayMinutes = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHours + todayMinutes + todaySeconds) * 1000;

        long todayStartMillis = curTime - todayMillis;
        if (time >= todayStartMillis) {
            return "今天";
        } else {
            long yesterdayStartMilis = todayStartMillis - ONE_DATY_TMIE;
            if (time >= yesterdayStartMilis) {
                return "昨天";
            }
        }
        return dateFormat.format(targetDate);
    }

    /**
     * 获取当前的年月日时
     */
    public static String getCurrentYMDH(){
        Date date = new Date(getCurrentTime());
        return dateHourFormat.format(date);
    }
    /**
     * 获取某年
     *
     * @return
     */
    public static List<String> getWheelYearLayer(Context context,Calendar now) {
        int years = now.get(Calendar.YEAR);
        List<String> listYear = new ArrayList<>();
        for (int i = 0; i <= 50; i ++ ) {
            listYear.add(String.valueOf(years + i)+context.getString(R.string.year));
        }
        return listYear;
    }
    /**
     * 获取某年
     *
     * @return
     */
    public static List<String> getWheelYear(Context context,Calendar now) {
        int years = now.get(Calendar.YEAR);
        List<String> listYear = new ArrayList<>();
        for (int i = 3; i >= 0; i--) {
            listYear.add(String.valueOf(years - i)+context.getString(R.string.year));
        }
        return listYear;
    }

    public static List<String> getWheelYear(Context context,Calendar now, boolean isLastYear) {
        int years = now.get(Calendar.YEAR);
        List<String> listYear = new ArrayList<>();
        if (isLastYear) {
            for (int i = 1; i >= 0; i--) {
                listYear.add(String.valueOf(years - i)+context.getString(R.string.year));
            }
        } else {
            listYear.add(String.valueOf(years)+context.getString(R.string.year));
        }
        return listYear;
    }

    /**
     * 获取某月
     *
     * @return
     */
    public static List<String> getWheelMonth(Context context, int curMonth, boolean isLastYear) {
        List<String> listMonth = new ArrayList<>();
        if (isLastYear) {
            for (int i = 1; i < 13; i++) {
                listMonth.add(String.valueOf(i) + context.getString(R.string.mouth));
            }
        } else {
            // 仅查询最近三个月
            int target = curMonth - 3;
            if (target <= 0) {
                target = 1;
            }

            for (int i = target; i <= curMonth; i++) {
                if (i <= 0) {
                    break;
                }
                listMonth.add(String.valueOf(i)+context.getString(R.string.mouth));
            }
        }
        return listMonth;
    }

    /**
     * 获取某日
     *
     * @return
     */
    public static List<String> getWheelDate(Context context, Calendar now) {
        int years = now.get(Calendar.YEAR);
        int month= now.get(Calendar.MONTH) + 1;
        List<String> listDate = new ArrayList<>();
        int dayOfMonth = getDayOfMonth(years, month);
        listDate.add("无");
        for (int i = 1; i < dayOfMonth + 1 ; i++) {
            listDate.add(String.valueOf(i)+context.getString(R.string.date));
        }
        return listDate;
    }

    public static String dateFormat(String date){
        String  dates;
        if (date.length()==1){
            dates="0"+date;
        }else{
            dates=String.valueOf(date);
        }
        return dates;
    }
    /**
     * 获取某日
     *
     * @return
     */
    public static List<String> getWheelDateList(Context context,int years,int month) {
        List<String> listDate = new ArrayList<>();
        int dayOfMonth = getDayOfMonth(years, month);
        listDate.add("无");
        for (int i = 1; i < dayOfMonth + 1; i++) {
            listDate.add(String.valueOf(i)+context.getString(R.string.date));
        }
        return listDate;
    }
    /**
     * 年格式化
     */
    public static String getFormatYear(String mYearStr){
        return mYearStr.substring(0,mYearStr.length()-1);
    }
    /**
     * 月格式化
     */
    public static String getFormatMonth(String mMonth){
        String mMonths=mMonth.substring(0,mMonth.length()-1);
        return dateFormat(mMonths);
    }
    /**
     * 日格式化
     */
    public static String getFormatDate(Context context,String mDateStr){
        if (context.getString(R.string.none).equals(mDateStr)){
            return "";
        }else{
            String mDates=mDateStr.substring(0,mDateStr.length()-1);
            return dateFormat(mDates);
        }
    }

    /**
     * 获取某月
     */
    public static List<String> getWheelMonth(Context context) {
        List<String> listMonth = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            listMonth.add(String.valueOf(i) + context.getString(R.string.mouth));
        }
        return listMonth;
    }

    /**
     * 获取某日
     */
    public static List<String> getWheelDate(Context context) {
        int years = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        List<String> listDate = new ArrayList<>();
        int dayOfMonth = getDayOfMonth(years, month);
        listDate.add("无");
        for (int i = 1; i <= dayOfMonth; i++) {
            listDate.add(String.valueOf(i) + context.getString(R.string.date));
        }
        return listDate;
    }

}