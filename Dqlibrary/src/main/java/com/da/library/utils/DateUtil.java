package com.da.library.utils;

import android.text.TextUtils;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.text.format.DateUtils.FORMAT_NUMERIC_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_DATE;
import static android.text.format.DateUtils.FORMAT_SHOW_YEAR;
import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

/**
 * Created by baiyuliang on 2015-11-23.
 */
public class DateUtil {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    public final static String YMD = "yyyy-MM-dd";
    public final static String YMD_FORMAT = "yyyy.MM.dd";
    public final static String YMDHM = "yyyy-MM-dd HH:mm";
    public final static String REQNO = "yyyyMMddHHmmss";

    public final static String HMS = "HH:mm:ss.SSS";

    public final static String HM = "HH:mm";

    public final static String FULLFORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private final static String YMDHMFORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String YMDHMS_POINT = "yyyy.MM.dd HH:mm:ss";

    public final static String YMDHM_POINT = "yyyy.MM.dd HH:mm";

    public final static String MD_CN = "MM月dd日";

    public final static String MDHM_CN = "MM月dd日 HH:mm";

    public final static String CN_FORMAT1 = "yyyy年MM月";

    public final static String CN_FORMAT2 = "MM月dd日 HH:mm:ss";

    private short now = 0;// 时
    private short minutes = 0;// 分
    private short seconds = 0;// 秒
    private int sumSeconds;//总共耗时多扫秒


    /**
     * 获取当前时间向后一周内的日期列表
     *
     * @return
     */
    public static List<String> getDateList() {
        List<String> months = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        long current_time = System.currentTimeMillis();
        long day_ms = 24 * 60 * 60 * 1000;
        for (int i = 0; i < 7; i++) {
            c.setTimeInMillis(current_time + day_ms * i);
            if (i == 0) {
                months.add(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日(今天)");
            } else if (i == 1) {
                months.add(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日(明天)");
            } else {
                months.add(c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月" + c.get(Calendar.DAY_OF_MONTH) + "日(" + getWeek(c.get(Calendar.DAY_OF_WEEK) - 1) + ")");
            }
        }
        return months;
    }

    /*
    * 获取指定时间戳获取前七天日期
    * */
    public static List<String> getLastWeekDate(long currentTime) {

        List<String> months = new ArrayList<String>();

        Calendar c = Calendar.getInstance();
        long day_ms = 24 * 60 * 60 * 1000;

        for (int i = 0; i < 7; i++) {

            c.setTimeInMillis(currentTime - day_ms * i);

            int month=(c.get(Calendar.MONTH) + 1);
            int day= c.get(Calendar.DAY_OF_MONTH);

            months.add((month>=10?month:"0"+month)+ "-" + (day>=10?day:"0"+day));
        }
        return months;
    }

    /**
     * 根据传入的时间获取该天可预约的时间列表
     *
     * @param str
     * @return
     */
    public static List<String> getTimeList(String str) {
        Date date = null;
        if (TextUtils.isEmpty(str)) {
            date = new Date();
        } else {
            try {
                date = sdf.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }
        }
        Calendar c = Calendar.getInstance();//当前时间
        Calendar _c = Calendar.getInstance();//传进来的时间
        _c.setTime(date);
        //如果当前月<传入月，或者当前月与传入月相同但当前日<传入日，并且
        if (c.get(Calendar.MONTH) < _c.get(Calendar.MONTH) || (c.get(Calendar.MONTH) == _c.get(Calendar.MONTH) && c.get(Calendar.DAY_OF_MONTH) < _c.get(Calendar.DAY_OF_MONTH))) {
            return getTimeAllList();
        } else {
            if (c.get(Calendar.HOUR_OF_DAY) > 12) {
                return getTimePMList();
            } else {
                return getTimeAllList();
            }
        }
    }

    /**
     * 获取该天可预约的时间列表（全天）
     *
     * @return
     */
    public static List<String> getTimeAllList() {
        List<String> timeList = new ArrayList<String>();
        int hour = 5;
        for (int i = 0; i < 25; i++) {
            String sec;
            if (i % 2 == 0) {
                sec = "00";
                hour++;
            } else {
                sec = "30";
            }
            timeList.add(hour + ":" + sec);
        }
        return timeList;
    }

    /**
     * 获取该天可预约的时间列表（下午）
     *
     * @return
     */
    public static List<String> getTimePMList() {
        List<String> timeList = new ArrayList<String>();
        int hour = 12;
        for (int i = 0; i < 11; i++) {
            String sec;
            if (i % 2 == 0) {
                sec = "00";
                hour++;
            } else {
                sec = "30";
            }
            timeList.add(hour + ":" + sec);
        }
        return timeList;
    }


    /**
     * 获取星期几
     */
    public static String getWeek(int week) {
        String[] _weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        switch (week) {
            case 0:
                return _weeks[0];
            case 1:
                return _weeks[1];
            case 2:
                return _weeks[2];
            case 3:
                return _weeks[3];
            case 4:
                return _weeks[4];
            case 5:
                return _weeks[5];
            case 6:
                return _weeks[6];
        }
        return "";
    }


    public static int getPosition(List<String> list, String str) {
        int position = -1;
        for (int i = 0; i < list.size(); i++) {
            if (str.equals(list.get(i))) {
                position = i;
            }
        }
        return position;
    }


    public static List<String> getBirthYearList() {
        List<String> birthYearList = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        for (int i = 0; i < 2; i++) {
            birthYearList.add((year + i) + "年");
        }
        return birthYearList;
    }

    public static List<String> getBirthMonthList(String... start) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        List<String> birthMonthList = new ArrayList<String>();
        if(start!=null&&start.length>0&&(year+"").equals( start[0])){
            int i= c.get(Calendar.MONTH)+1;
            for (; i <= 12; i++) {
                if (i<10) {
                    birthMonthList.add("0"+i + "月");
                }else {
                    birthMonthList.add(i + "月");
                }
            }

        }else{
            for (int i=1; i <= 12; i++) {
                if (i<10) {
                    birthMonthList.add("0"+i + "月");
                }else {
                    birthMonthList.add(i + "月");
                }
            }
        }


        return birthMonthList;
    }


    public static List<String> getBirthDay28List() {
        List<String> birthDayList = new ArrayList<String>();
        for (int i = 1; i <= 28; i++) {
            if (i<10) {
                birthDayList.add("0"+i + "日");
            }else {
                birthDayList.add(i + "日");
            }
        }
        return birthDayList;
    }

    public static List<String> getBirthDay29List() {
        List<String> birthDayList = new ArrayList<String>();
        for (int i = 1; i <= 29; i++) {
            if (i<10) {
                birthDayList.add("0"+i + "日");
            }else {
                birthDayList.add(i + "日");
            }

        }
        return birthDayList;
    }


    public static List<String> getBirthDay30List() {
        List<String> birthDayList = new ArrayList<String>();
        for (int i = 1; i <= 30; i++) {
            if (i<10) {
                birthDayList.add("0"+i + "日");
            }else {
                birthDayList.add(i + "日");
            }
        }
        return birthDayList;
    }

    public static List<String> getBirthDay31List() {
        List<String> birthDayList = new ArrayList<String>();
        for (int i = 1; i <= 31; i++) {
            if (i<10) {
                birthDayList.add("0"+i + "日");
            }else {
                birthDayList.add(i + "日");
            }
        }
        return birthDayList;
    }



    public static List<String> getBirthDay28AndWeekList(String date, int i) {
        List<String> birthDayList = new ArrayList<String>();
        for (; i <= 28; i++) {
            if (i<10) {
                birthDayList.add("0"+i+getWeekOfDate(date+"-0"+i));
            }else {
                birthDayList.add(i+getWeekOfDate(date+"-"+i));
            }
        }
        return birthDayList;
    }

    public static List<String> getBirthDay29AndWeekList(String date, int i) {
        List<String> birthDayList = new ArrayList<String>();
        for (; i <= 29; i++) {
            if (i<10) {
                birthDayList.add("0"+i+getWeekOfDate(date+"-0"+i));
            }else {
                birthDayList.add(i +getWeekOfDate(date+"-"+i));
            }

        }
        return birthDayList;
    }






    public static List<String> getBirthDay30AndWeekList(String date, int i) {
        List<String> birthDayList = new ArrayList<String>();
        for (; i <= 30; i++) {
            if (i<10) {
                birthDayList.add("0"+i +getWeekOfDate(date+"-0"+i));
            }else {
                birthDayList.add(i+getWeekOfDate(date+"-"+i));
            }
        }
        return birthDayList;
    }


    public static List<String> getBirthDay31AndWeekList(String date, int i) {
        List<String> birthDayList = new ArrayList<String>();
        for (; i <= 31; i++) {
            if (i<10) {
                birthDayList.add("0"+i +getWeekOfDate(date+"-0"+i));
            }else {
                birthDayList.add(i +getWeekOfDate(date+"-"+i));
            }
        }
        return birthDayList;
    }

    /**
     * 判断是否是闰年
     *
     * @return
     */
    public static boolean isRunYear(String year) {
        try {
            int _year = Integer.parseInt(year);
            if (_year % 4 == 0 && _year % 100 != 0 || _year % 400 == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 根据日期获得星期
     * @param
     * @return
     */
    public static String getWeekOfDate(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 定义日期格式
        Date date = null;
        try {
            date = format.parse(time);// 将字符串转换为日期
        } catch (ParseException e) {
            System.out.println("输入的日期格式不合理！");
        }

        String[] weekDaysName = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }



    /**
     * 获取日期时间24小时制
     *
     * @return
     */
    public static List<String> get24HourTimeList(int i) {
        List<String> timeList = new ArrayList<String>();
        for (; i < 25; i++) {
            timeList.add(i + "时");
        }
        return timeList;
    }





    public static Date getStringToDate(String date, String format) {
        if(date!=null){
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            try {
                return formatter.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    public static Calendar getStringToCalendar(String date, String format) {
        Calendar calendar = Calendar.getInstance();
        if(date!=null){
            SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            try {
                calendar.setTime(formatter.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return calendar;
    }


    /**
     * 获取当前系统时间戳
     */
    public static String getTimeString() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis()+"";
    }

    public static String getTimeSencodString() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis()/1000+"";
    }
    public static Long getTimeSencodLong() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis()/1000L;
    }


    /**
     * 获取指定时间戳
     */
    public static String getTimeString(String time, String format) {
        long timestamp = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            Date date;
            date = df.parse(time);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            timestamp = cal.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(timestamp);

    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 当前时间 精确到 年月日
     *
     * @return 年月日
     */
    public static String currentDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(YMD, Locale.getDefault());
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 当前时间 精确到 年月日
     *
     * @return 年月日
     */
    public static String currentDateStr(String ymd) {
        SimpleDateFormat sdf = new SimpleDateFormat(ymd, Locale.getDefault());
        return sdf.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 当前时间 精确到 年月日
     *
     * @return date
     */
    public static Date currentDate() {
        return new Date(System.currentTimeMillis());
    }

    public static String getFriendlyTime(long timeStamp){
        if(timeStamp == 0l){
            return "未知";
        }
        if(System.currentTimeMillis() - timeStamp <= 1000 * 60 * 3){
            return "刚刚";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(HM, Locale.getDefault());
        return sdf.format(timeStamp);
    }

    /**
     * 获取当前的年月日 时分秒
     * @param format 格式定义
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }

    /**
     *  获取当前的年月日
     * @param
     * @return
     */
    public static String getCurrentYMD() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        return formatter.format(curDate);
    }
    public void setSumSeconds(int sumSeconds){
        this.sumSeconds=sumSeconds;
    }

    public int getSumSeconds(){
        return sumSeconds;
    }

    /**
     * 计时
     * 每调用一次加一秒，从0开始最大单位为小时超出24小时全部清零
     * @return 比如：00:01或者01:01:34
     */
    public String reckonByTime() {
        StringBuilder sb = new StringBuilder();
        seconds++;
        if (seconds > 59) {
            seconds = 0;
            minutes++;
            if (minutes > 59) {
                minutes = 0;
                now++;
                if (now > 24) {
                    now = 0;
                    minutes = 0;
                    seconds = 0;
                }
            }
        }
        boolean flag = false;
        if (now > 0) {
            if (now <= 9) {
                sb.append("0").append(now);
            } else {
                sb.append(now);
            }
            flag=true;
        }
        if (minutes > 0) {
            if(flag)
                sb.append(":");
            else
                flag=true;
            if (minutes <= 9) {
                sb.append("0").append(minutes);
            } else {
                sb.append(minutes);
            }

        }else{
            sb.append("00:");
        }
        if (seconds > 0) {
            if(flag)
                sb.append(":");
            if (seconds <= 9) {
                sb.append("0").append(seconds);
            } else {
                sb.append(seconds);
            }
        }else{
            sb.append(":00");
        }
        sumSeconds++;
        return sb.toString();
    }

    /**
     * 根据日期计算星期几
     */
    public static int dayForWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 当前时间 精确到 年月日 时分秒
     *
     * @return 年月日 时分秒
     */
    public static String currentTime() {
        long millis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat(YMDHMFORMAT,
                Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    /**
     * 将时间字符串转为时间戳
     * @return 年月日 时分秒
     */
    public static long string2Long(String dateTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YMDHMFORMAT);
        long ts = 0;
        try {
            Date date = simpleDateFormat.parse(dateTime);
            ts = date.getTime();
        }catch (ParseException e){
            e.printStackTrace();
        }
        System.out.println(ts);
        return ts;
    }


    /**
     * 判断月份有多少天
     */
    public static int monthToDay(Date date){
        Calendar c= Calendar.getInstance();
        c.setTime(date);
        int month=c.get(Calendar.MONTH)+1;
        if(month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12){
            return 31;
        }
        if(month==4 || month==6 || month==9 || month==11){
            return 30;
        }
        if(isLeapYear(c.get(Calendar.YEAR))){
            return 29;
        }else{
            return 28;
        }
    }

    /**
     * 判断是否是今天
     */
    public static boolean isToday(Date date) {
        Calendar c = Calendar.getInstance();
        int date1 = 0, date2 = 0;
        date1 += c.get(Calendar.YEAR);
        date1 += c.get(Calendar.MONTH) + 1;
        date1 += c.get(Calendar.DAY_OF_MONTH);
        c.setTime(date);
        date2 += c.get(Calendar.YEAR);
        date2 += c.get(Calendar.MONTH) + 1;
        date2 += c.get(Calendar.DAY_OF_MONTH);
        return date1 == date2;
    }

    /**
     * 根据周几数字返回对应字符
     * @param day
     * @return
     */
    public static String dayToStr(int day){
        if(day==1){
            return "周一";
        }else if(day==2){
            return "周二";
        }else if(day==3){
            return "周三";
        }else if(day==4){
            return "周四";
        }else if(day==5){
            return "周五";
        }else if(day==6){
            return "周六";
        }else if(day==7){
            return "周日";
        }
        return null;
    }

    /**
     * 判断是否是闰年
     */
    public static boolean isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }
    private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        }
    };

    private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        }
    };

    /**
     * 将字符串转位日期类型
     *
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormater.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     */
    public static String friendlyTime(String sdate) {
        Date time = toDate(sdate);
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        //判断是否是同一天
        String curDate = dateFormater2.get().format(cal.getTime());
        String paramDate = dateFormater2.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormater2.get().format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     */
    public static boolean isToday(String sdate) {
        boolean b = false;
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormater2.get().format(today);
            String timeDate = dateFormater2.get().format(time);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }


    /**
     * Get relative time for date
     *
     */
    public static CharSequence getRelativeTime(final Date date) {
        long now = System.currentTimeMillis();
        if (Math.abs(now - date.getTime()) > 60000)
            return DateUtils.getRelativeTimeSpanString(date.getTime(), now,
                    MINUTE_IN_MILLIS, FORMAT_SHOW_DATE | FORMAT_SHOW_YEAR
                            | FORMAT_NUMERIC_DATE);
        else
            return "just now";
    }


    /**
     * 将毫秒转时分秒
     *
     * @param time
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }


}
