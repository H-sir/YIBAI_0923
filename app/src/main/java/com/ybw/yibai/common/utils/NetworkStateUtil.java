package com.ybw.yibai.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;

import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_2G;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_3G;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_4G;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_INVALID;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_UNKNOWN;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_WIFI;

/**
 * 检测当网络环境的工具类
 *
 * @author sjl
 */
public class NetworkStateUtil {

    private static final String TAG = "NetworkStateUtil";

    /**
     * 检测当的网络状态
     *
     * @param context 上下文对象
     * @return true表示当前网络是连接的, false表示当前网络是不可用的
     */
    public static boolean isNetworkAvailable(@NonNull Context context) {
        // 获得网络连接服务
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            // 获取当前连接的可用网络
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            // info.isConnected()判断当前网络是否存在,并可用于数据传输
            if (null != info && info.isConnected() && info.isAvailable()) {
                // "NetworkInfo.State.CONNECTED"说明当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 获取手机网络类型（2G/3G/4G）
     * <p>
     * 2G 移动和联通的为GPRS或EGDE,电信的为CDMA
     * 3G 联通的为UMTS或HSDPA，电信的为EVDO
     * 4G 为LTE
     */
    public static int getNetWorkType(@NonNull Context context) {
        // 获得网络连接服务
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == connectivity) {
            return NETWORK_TYPE_UNKNOWN;
        }

        // 获取当前连接的可用网络
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        // info.isConnected()判断当前网络是否存在并可用于数据传输
        // "NetworkInfo.State.CONNECTED"说明当前网络是连接的
        if (info != null && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) {
            // 获取手机连接的网络类型（是WIFI还是手机网络[2G/3G/4G]）
            int type = info.getType();
            if (type == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_TYPE_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                // 网络类型是移动网络
                return getMobileNetWorkType(context);
            }
        } else {
            // 网络断开
            return NETWORK_TYPE_INVALID;
        }

        return NETWORK_TYPE_UNKNOWN;
    }

    /**
     * 获取手机网络类型（2G/3G/4G）
     * <p>
     * 2G 移动和联通的为GPRS或EGDE,电信的为CDMA
     * 3G 联通的为UMTS或HSDPA,电信的为EVDO
     * 4G 为LTE
     */
    private static int getMobileNetWorkType(@NonNull Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = NETWORK_TYPE_UNKNOWN;
        if (null != telephonyManager) {
            networkType = telephonyManager.getNetworkType();
        }

        if (networkType == TelephonyManager.NETWORK_TYPE_GPRS
                || networkType == TelephonyManager.NETWORK_TYPE_EDGE
                || networkType == TelephonyManager.NETWORK_TYPE_CDMA
                || networkType == TelephonyManager.NETWORK_TYPE_1xRTT
                || networkType == TelephonyManager.NETWORK_TYPE_IDEN) {

            // 网络类型是:2G
            return NETWORK_TYPE_2G;

        } else if (networkType == TelephonyManager.NETWORK_TYPE_UMTS
                || networkType == TelephonyManager.NETWORK_TYPE_EVDO_0
                || networkType == TelephonyManager.NETWORK_TYPE_EVDO_A
                || networkType == TelephonyManager.NETWORK_TYPE_HSDPA
                || networkType == TelephonyManager.NETWORK_TYPE_HSUPA
                || networkType == TelephonyManager.NETWORK_TYPE_HSPA
                || networkType == TelephonyManager.NETWORK_TYPE_EVDO_B
                || networkType == TelephonyManager.NETWORK_TYPE_EHRPD
                || networkType == TelephonyManager.NETWORK_TYPE_HSPAP) {

            // 网络类型是:3G
            return NETWORK_TYPE_3G;

        } else if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {

            // 网络类型是:4G
            return NETWORK_TYPE_4G;

        } else {

            // 网络类型未知
            return NETWORK_TYPE_UNKNOWN;
        }
    }
}
