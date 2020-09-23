package com.ybw.yibai.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间的工具类
 *
 * @author sjl
 */
public class TimeUtil {

    private static final String TAG = "TimeUtil";

    /**
     * 获取时间戳(13位)
     *
     * @return 时间戳
     */
    public static long getTimeStamp() {
        return Calendar.getInstance().getTimeInMillis();
    }

    /**
     * 获取时间戳(10位)
     *
     * @return 时间戳
     */
    public static int getTimestamp() {
        String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis() / 1000);
        return Integer.parseInt(timestamp);
    }

    /**
     * 返回正在运行的Java虚拟机的当前值高分辨率时间源,以纳秒为单位
     *
     * @return 纳秒
     */
    public static long getNanoTime() {
        return System.nanoTime();
    }

    /**
     * 得到现在时间
     *
     * @return 字符串 月-日
     */
    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }
}
