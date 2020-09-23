package com.ybw.yibai.common.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.classs.ConcreteWatched;
import com.ybw.yibai.common.utils.LogUtil;

import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_2G;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_3G;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_4G;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_INVALID;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_UNKNOWN;
import static com.ybw.yibai.common.constants.Encoded.NETWORK_TYPE_WIFI;


/**
 * 监控当前网络状态的广播接收器
 *
 * @author sjl
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    private final String TAG = "NetWorkStateReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        // 获得网络连接服务
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {

            // 获取当前连接的可用网络
            @SuppressLint("MissingPermission") NetworkInfo info = connectivity.getActiveNetworkInfo();

            // info.isConnected()判断当前网络是否存在并可用于数据传输
            // "NetworkInfo.State.CONNECTED"说明当前网络是连接的
            if (info != null && info.isConnected() && info.getState() == NetworkInfo.State.CONNECTED) {
                // 获取手机连接的网络类型（是WIFI还是手机网络[2G/3G/4G]）
                int type = info.getType();
                if (type == ConnectivityManager.TYPE_WIFI) {

                    // 网络类型是:WIFI
                    LogUtil.e(TAG,"网络类型是:WIFI");
                    notifyWatcherDataChange(NETWORK_TYPE_WIFI);
                } else if (type == ConnectivityManager.TYPE_MOBILE) {

                    // 网络类型是移动网络
                    getNetWorkType(context);
                }
            } else {
                // 网络断开
                LogUtil.e(TAG,"网络断开");
                notifyWatcherDataChange(NETWORK_TYPE_INVALID);
            }
        }
    }

    /**
     * 获取手机网络类型（2G/3G/4G）
     * <p>
     * 2G 移动和联通的为GPRS或EGDE,电信的为CDMA
     * 3G 联通的为UMTS或HSDPA，电信的为EVDO
     * 4G 为LTE
     */
    public void getNetWorkType(Context context) {

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
            notifyWatcherDataChange(NETWORK_TYPE_2G);

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
            notifyWatcherDataChange(NETWORK_TYPE_3G);

        } else if (networkType == TelephonyManager.NETWORK_TYPE_LTE) {

            // 网络类型是:4G
            notifyWatcherDataChange(NETWORK_TYPE_4G);

        } else {

            // 网络类型未知
            notifyWatcherDataChange(NETWORK_TYPE_UNKNOWN);

        }

    }

    /**
     * 被观察者观察到网络状态改变了,然后通知观察者对象网络状态改变了
     *
     * @param networkType 网络类型
     */
    public void notifyWatcherDataChange(int networkType) {

        NetworkType network = new NetworkType();
        network.setNetworkType(networkType);
        ConcreteWatched.getInstance().notifyWatcher(network);

    }

}
