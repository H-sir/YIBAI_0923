package com.ybw.yibai.common.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import com.ybw.yibai.R;
import com.ybw.yibai.common.broadcast.ApkInstallReceiver;

/**
 * 下载Apk的工具类
 *
 * @author sjl
 */
public class DownloadApkUtil {

    private static final String TAG = "DownloadApkUtil";

    private static ApkInstallReceiver mApkInstallReceiver;

    /**
     * 下载APK文件
     *
     * @param context     上下文对象
     * @param name        下载文件的名称
     * @param uri         下载文件的地址
     * @param dir         下载文件保存的目录
     * @param title       通知栏提示的标题
     * @param description 通知栏提示的介绍
     */
    public static long downloadApk(Context context, String name, String uri, String dir, String title, String description) {
        if (SDCardHelperUtil.isSDCardMounted()) {
            DownloadUtil downLoadUtil = DownloadUtil.getInstance();
            long id = downLoadUtil.download(name, uri, dir, title, description);

            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putLong(DownloadManager.EXTRA_DOWNLOAD_ID, id);
            editor.apply();

            return id;
        } else {
            MessageUtil.showMessage(context.getResources().getString(R.string.SD_card_is_not_installed_on_the_phone_failed_download));
            return -1L;
        }
    }

    /**
     * 注册下载广播接收器
     *
     * @param context 上下文对象
     */
    public static void registerBroadcast(@NonNull Context context) {
        mApkInstallReceiver = new ApkInstallReceiver();
        context.registerReceiver(mApkInstallReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * 注销下载广播接收器
     */
    public static void unregisterBroadcast(Context context) {
        if (null != mApkInstallReceiver) {
            context.unregisterReceiver(mApkInstallReceiver);
        }
    }
}
