package com.ybw.yibai.common.utils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.ybw.yibai.base.YiBaiApplication;

import static android.app.DownloadManager.Request.NETWORK_WIFI;
import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * 封装DownLoadManager下载工具类
 *
 * @author sjl
 */
public class DownloadUtil {

    public static final String TAG = "DownloadUtil";

    private Context mContext;

    private DownloadManager mDownloadManager;

    private static volatile DownloadUtil instance;

    /**
     * 获取DownLoadUtils单例对象
     *
     * @return DownLoadUtils单例对象
     */
    public static DownloadUtil getInstance() {
        if (null == instance) {
            // 使用synchronized防止多个线程同时访问一个对象时发生异常
            synchronized (DownloadUtil.class) {
                if (null == instance) {
                    instance = new DownloadUtil();
                }
            }
        }
        return instance;
    }

    private DownloadUtil() {
        this.mContext = YiBaiApplication.getContext();
        mDownloadManager = (DownloadManager) mContext.getSystemService(DOWNLOAD_SERVICE);
    }

    /**
     * 下载文件
     *
     * @param name        下载文件的名称
     * @param uri         下载文件的地址
     * @param dir         下载文件保存的目录
     * @param title       通知栏提示的标题
     * @param description 通知栏提示的介绍
     * @return downloadId 系统为当前的下载请求分配的一个唯一的ID
     */
    public long download(String name, String uri, String dir, String title, String description) {
        // 创建下载请求
        DownloadManager.Request downloadRequest = new DownloadManager.Request(Uri.parse(uri));
        // 设置显示通知栏
        downloadRequest.setVisibleInDownloadsUi(true);
        // 设置通知栏是否可见
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 设置下载中通知栏提示的标题
        downloadRequest.setTitle(title);
        // 设置下载中通知栏提示的介绍
        downloadRequest.setDescription(description);
        // 更改服务器返回的MimeType为android包类型
        downloadRequest.setMimeType("application/vnd.android.package-archive");
        // 设置移动网络情况下是否允许漫游
        downloadRequest.setAllowedOverRoaming(false);
        // 设置为可被媒体扫描器找到
        downloadRequest.allowScanningByMediaScanner();
        // 设置为可见和可管理
        downloadRequest.setVisibleInDownloadsUi(true);
        // 表示下载允许的网络类型,默认在任何网络下都允许下载,有NETWORK_MOBILE、NETWORK_WIFI、NETWORK_BLUETOOTH三种及其组合可供选择
        downloadRequest.setAllowedNetworkTypes(NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        // 表示下载进行中和下载完成的通知栏是否显示,默认只显示下载中通知,VISIBILITY_HIDDEN表示不显示任何通知栏提示
        downloadRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置下载后文件存放的位置
        downloadRequest.setDestinationInExternalPublicDir(dir, name);
        // 将请求放入下载队列中
        return mDownloadManager.enqueue(downloadRequest);
    }

    /**
     * 判断下载管理程序是否可用
     *
     * @return true可用, false不可用
     */
    public boolean canDownload() {
        try {
            int state = mContext.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
