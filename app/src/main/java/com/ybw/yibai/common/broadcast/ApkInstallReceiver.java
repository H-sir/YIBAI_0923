package com.ybw.yibai.common.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.ybw.yibai.R;
import com.ybw.yibai.common.utils.MessageUtil;

/**
 * 接收DownloadManager下载完成后发出的广播,调用系统安装程序安装Apk
 *
 * @author sjl
 */
public class ApkInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (!TextUtils.isEmpty(action) && action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            installApk(context, downloadApkId);
        }
    }

    /**
     * 安装Apk
     */
    private void installApk(Context context, long downloadApkId) {
        // 获取存储ID
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        long downId = sp.getLong(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
        if (downloadApkId != downId) {
            return;
        }
        DownloadManager downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (null == downManager) {
            return;
        }
        Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
        if (null != downloadFileUri) {
            // 跳转到系统的安装应用页面
            Intent intent = new Intent(Intent.ACTION_VIEW);
            // Android7.0以上URI
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
            context.startActivity(intent);
        } else {
            MessageUtil.showMessage(context.getResources().getString(R.string.download_failed));
        }
    }
}
