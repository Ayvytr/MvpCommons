package com.base.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * 日期工具类
 */
public class DateUtil {

    public final static String FORMAT_YEAR = "yyyy";
    public final static String FORMAT_DATE = "yyyy/MM/dd";
    public final static String FORMAT_DATE_CN = "yyyy年MM月dd日";
    public final static String FORMAT_DATE_MOUTH = "yyyy-MM";
    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_TIMES = "HH:mm:ss";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE_BEJI = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_DATE_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public final static String FORMAT_DATE_TIMES = "yyyy-MM-dd HH";
    public final static String FORMAT_MONTH_DAY_TIME = "MM月dd日 HH:mm";
    public final static String FORMAT_MONTH_DAY_TIMES = "MM-dd HH:mm:ss";
    public final static String FORMAT_DATE_UNDER_LINE = "yyyy_MM_dd_HHmmss";
    public final static String FORMAT_DATE_DEFAULT = "yyyy-MM-dd";

    private final static long minute = 60 * 1000;// 1分钟
    private final static long hour = 60 * minute;// 1小时
    private final static long day = 24 * hour;// 1天
    private final static long month = 31 * day;// 月
    private final static long year = 12 * month;// 年

    /**
     * 锁对象
     */
    private static final Object lockObj = new Object();

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     *
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }


    /**
     * 将date日期转为自己需要的日期格式
     *
     * @param date       要转换的日期
     * @param dateFormat 日期格式 列如.年 “yyyy” ，年月日 “yyyy/MM/dd”
     * @return 转换完的日期
     */
    public static String dateFormat(Date date, String dateFormat) {
        if (null == date || TextUtils.isEmpty(dateFormat)) {
            return "";
        }
        SimpleDateFormat format = getSdf(dateFormat);
        return format.format(date);
    }

    /**
     * 获取当前时间，转换为日期格式
     *
     * @return
     */
    public static String getCurrentDate() {
        SimpleDateFormat formatter = getSdf(FORMAT_DATE_TIME);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static String getCurrentDate(String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            return "";
        }
        SimpleDateFormat formatter = getSdf(pattern);
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    public static long getDate() {
        return new Date().getTime();
    }

    /**
     * date格式转换成string
     *
     * @param currentTime
     * @param formatType
     * @return
     */
    public static String toString(long currentTime, String formatType) {
        if (TextUtils.isEmpty(formatType)) {
            return "";
        }
        Date date = null; // long类型转成Date类型
        String strTime = null;
        date = toDate(currentTime, formatType);
        strTime = dateFormat(date, formatType); // date类型转成String
        return strTime;
    }

    /**
     * long格式转换成自己想要的date日期格式
     *
     * @param currentTime 要转换的毫秒数
     * @param formatType  日期格式
     * @return
     */
    public static Date toDate(long currentTime, String formatType) {
        if (TextUtils.isEmpty(formatType)) {
            return null;
        }
        Date oldData = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
        String sDateTime = dateFormat(oldData, formatType); // 把date类型的时间转换为string
        Date date = toDate(sDateTime, formatType); // 把String类型转换为Date类型
        return date;
    }

    /**
     * 得到当前时间
     *
     * @param dateFormat 时间格式
     * @return 转换后的时间格式
     */
    public static String getCurrentTime(String dateFormat) {
        if (TextUtils.isEmpty(dateFormat)) {
            return "";
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = getSdf(dateFormat);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 将字符串型日期转换成日期
     *
     * @param dateStr    字符串型日期
     * @param dateFormat 日期格式
     * @return
     */
    public static Date toDate(String dateStr, String dateFormat) {
        if (TextUtils.isEmpty(dateStr) || TextUtils.isEmpty(dateFormat)) {
            return null;
        }
        SimpleDateFormat formatter = getSdf(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date toDate(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return null;
        }
        SimpleDateFormat formatter = getSdf(FORMAT_DATE_BEJI);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }

    }

    /**
     * 时间戳转日期
     */
    public static String formatDate(String timeStamp) {
        if (TextUtils.isEmpty(timeStamp)) {
            return "";
        }
        long time = Long.parseLong(timeStamp);
        SimpleDateFormat format = getSdf(FORMAT_DATE_BEJI);
        return format.format(new Date(time));
    }

    /**
     * 将10 or 13 位时间戳转为时间字符串
     **/
    public static String timestamp2Date(String str_num) {
        if (TextUtils.isEmpty(str_num)) {
            return "";
        }
        SimpleDateFormat sdf = getSdf(FORMAT_DATE_BEJI);
        if (str_num.length() == 13) {
            String date = sdf.format(new Date(Long.parseLong(str_num)));
            return date == null ? "" : date;
        } else {
            String date = sdf.format(new Date(Integer.parseInt(str_num) * 1000L));
            return date;
        }
    }

    /**
     * 时间戳转日期
     */
    public static String formatDate(String timeStamp, String timeFormat) {
        if (TextUtils.isEmpty(timeStamp) || TextUtils.isEmpty(timeFormat)) {
            return "";
        }
        long time = Long.parseLong(timeStamp);
        SimpleDateFormat format = getSdf(timeFormat);
        return format.format(new Date(time));
    }

    public static String formatDate(long time, String timeFormat) {
        if (TextUtils.isEmpty(timeFormat)) {
            return "";
        }
        SimpleDateFormat format = getSdf(timeFormat);
        return format.format(new Date(time));
    }

    /**
     * 两个时间点的间隔时长（天数）
     * 得到的差值是微秒级别
     *
     * @param before 开始时间
     * @param after  结束时间
     * @return 两个时间点的间隔时长（天数）
     */
    public static long compareDay(Date before, Date after) {
        if (before == null || after == null) {
            return 0;
        }
        long dif = 0;
        if (after.getTime() >= before.getTime()) {
            dif = after.getTime() - before.getTime();
        } else if (after.getTime() < before.getTime()) {
            dif = after.getTime() + 86400000 - before.getTime();
        }
        dif = Math.abs(dif);
        return dif / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取指定时间间隔分钟后的时间
     *
     * @param date 指定的时间
     * @param min  间隔分钟数
     * @return 间隔分钟数后的时间
     */
    public static Date addMinutes(Date date, int min) {
        if (date == null) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    /**
     * 根据时间返回指定术语，自娱自乐，可自行调整
     *
     * @param hourday 小时
     * @return
     */
    public static String showTimeView(int hourday) {
        if (hourday >= 22 && hourday <= 24) {
            return "晚上";
        } else if (hourday >= 0 && hourday <= 6) {
            return "凌晨";
        } else if (hourday > 6 && hourday <= 12) {
            return "上午";
        } else if (hourday > 12 && hourday < 22) {
            return "下午";
        }
        return null;
    }


    /**
     * utc时间转北京时间
     */
    public static String utc2cn(String utc) {
        if (TextUtils.isEmpty(utc)) {
            return "";
        }
        SimpleDateFormat s = getSdf(FORMAT_DATE_UTC);
        s.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = s.parse(utc);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        s = getSdf(FORMAT_DATE_BEJI);
        s.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return s.format(date);
    }


    /**
     * utc时间转系统设置时区
     *
     * @param timeStr 2018-01-22T09:12:43Z
     * @return
     */
    public static String utcTimeToSystem(String timeStr) {
        if (TextUtils.isEmpty(timeStr)) {
            return "";
        }
        SimpleDateFormat formatter = getSdf(FORMAT_DATE_UTC);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = formatter.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat localFormater = getSdf(FORMAT_DATE_BEJI);
        localFormater.setTimeZone(TimeZone.getDefault());
        return localFormater.format(date);
    }

    /**
     * 获取当前UTC时间
     *
     * @return
     */
    public static String getCurrentUtcTime() {
        Calendar cal = Calendar.getInstance();
        //本地时间毫秒数
        long timeInMillis = cal.getTimeInMillis();
        //时间偏移量
        int z = cal.get(Calendar.ZONE_OFFSET);
        //夏令时差
        int d = cal.get(Calendar.DST_OFFSET);
        //utc时间毫秒值
        long l = timeInMillis - (z + d);

        String utcTime = getSdf(FORMAT_DATE_UTC).format(l);

        return utcTime;
    }

    /**
     * 获取自定义时间格式
     *
     * @param timeStr         当前时间"2018-12-15 20:50:25"
     * @param timeZoneCurrent 当前时间时区        列如：TimeZone.getTimeZone("GTM+8");
     * @param formatCurrent   当前时间格式        yyyy-MM-dd HH:mm:ss
     * @param timeZoneFinish  转换后时间时区      列如：TimeZone.getTimeZone("UTC");或 TimeZone.getDefault()；
     * @param formatFinish    转换后时间格式      yyyy-MM-dd
     * @return
     */
    public static String getTime(String timeStr, TimeZone timeZoneCurrent, String formatCurrent, TimeZone timeZoneFinish, String formatFinish) {
        if (TextUtils.isEmpty(timeStr) || null == timeZoneCurrent || TextUtils.isEmpty(formatCurrent) || null == timeZoneFinish || TextUtils.isEmpty(formatFinish)) {
            return "";
        }
        SimpleDateFormat formatter = getSdf(formatCurrent);
        formatter.setTimeZone(timeZoneCurrent);
        Date date = null;
        try {
            date = formatter.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

        SimpleDateFormat localFormater = getSdf(formatFinish);
        localFormater.setTimeZone(timeZoneFinish);
        return localFormater.format(date);
    }

    /**
     * 获取自定义时间格式
     *
     * @param timeStr         当前时间"2018-12-15 20:50:25"
     * @param timeZoneCurrent 当前时间时区        列如：TimeZone.getTimeZone("GTM+8");
     * @param formatCurrent   当前时间格式        yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getTime(String timeStr, TimeZone timeZoneCurrent, String formatCurrent) {
        return getTime(timeStr, timeZoneCurrent, formatCurrent, TimeZone.getDefault(), FORMAT_DATE_DEFAULT);
    }

    /**
     * 获取自定义时间格式
     *
     * @param timeStr 当前时间"2018-12-15 20:50:25"
     * @return
     */
    public static String getTime(String timeStr) {
        return getTime(timeStr, TimeZone.getDefault(), FORMAT_DATE_TIME, TimeZone.getTimeZone("UTC"), FORMAT_DATE_TIME);
    }
}
