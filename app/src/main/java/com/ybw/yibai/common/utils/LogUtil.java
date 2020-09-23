package com.ybw.yibai.common.utils;

import android.util.Log;

/**
 * 日志的工具类
 *
 * @author sjl
 */
public class LogUtil {

    /**
     * 是否需要打印Log(true要打印Log false不要打印Log) 可以在Application的onCreate函数里面初始化
     */
    private static boolean isShowLog = true;

    public static void showLog(boolean showLog) {
        isShowLog = showLog;
    }

    /**
     * Log.v 的输出颜色为黑色,任何消息都会输出,这里的v代表verbose啰嗦的意思
     */
    public static void v(String tag, String message) {
        if (isShowLog) {
            Log.v(tag, message);
        }
    }

    /**
     * Log.d 的输出颜色为蓝色,仅输出debug调试的意思,但他会输出上层的信息
     */
    public static void d(String tag, String message) {
        if (isShowLog) {
            Log.d(tag, message);
        }
    }

    /**
     * Log.i 的输出颜色为绿色,一般提示性的消息information,它不会输出Log.v和Log.d的信息,但会显示i,w和e的信息
     */
    public static void i(String tag, String message) {
        if (isShowLog) {
            Log.i(tag, message);
        }
    }

    /**
     * Log.w 的输出颜色为橙色,可以看作为warning警告,一般需要我们注意优化Android代码,同时选择它后还会输出Log.e的信息
     */
    public static void w(String tag, String message) {
        if (isShowLog) {
            Log.w(tag, message);
        }
    }

    /**
     * Log.e 的输出颜色为红色,可以想到error错误,这里仅显示红色的错误信息
     */
    public static void e(String tag, String message) {
        if (isShowLog) {
            Log.e(tag, message);
        }
    }
}
