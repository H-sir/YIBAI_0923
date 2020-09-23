package com.ybw.yibai.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.ybw.yibai.R;
import com.ybw.yibai.common.widget.CustomDialog;

import static com.ybw.yibai.common.constants.Encoded.REQUEST_PERMISSIONS_CODE;

/**
 * Android6.0 权限工具类
 *
 * @author sjl
 */
public class PermissionsUtil {

    /**
     * 在Activity界面检查是否拥有指定的所有权限
     *
     * @param activity    Activity对象
     * @param permissions 要检查的权限列表
     * @return false没有指定的所有权限, true有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Activity activity, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予,则直接返回false
                return false;
            }
        }
        return true;
    }

    /**
     * 在Fragment界面检查是否拥有指定的所有权限
     *
     * @param fragment    Fragment对象
     * @param permissions 要检查的权限列表
     * @return false没有指定的所有权限, true有指定的所有权限
     */
    public static boolean checkPermissionAllGranted(Fragment fragment, @NonNull String[] permissions) {
        for (String permission : permissions) {
            Context context = fragment.getContext();
            if (null != context) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    // 只要有一个权限没有被授予,则直接返回false
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 开始提交请求权限
     *
     * @param activity    Activity对象
     * @param permissions 要请求的权限列表
     */
    public static void startRequestPermission(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSIONS_CODE);
    }

    /**
     * 开始提交请求权限
     *
     * @param activity    Activity对象
     * @param permissions 要请求的权限列表
     * @param requestCode 请求代码
     */
    public static void startRequestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    /**
     * 开始提交请求权限
     *
     * @param fragment    Fragment对象
     * @param permissions 要请求的权限列表
     */
    public static void startRequestPermission(@NonNull Fragment fragment, String[] permissions) {
        fragment.requestPermissions(permissions, REQUEST_PERMISSIONS_CODE);
    }

    /**
     * 开始提交请求权限
     *
     * @param fragment    Fragment对象
     * @param permissions 要请求的权限列表
     * @param requestCode 请求代码
     */
    public static void startRequestPermission(@NonNull Fragment fragment, String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    /**
     * 判断是否所有的权限都已经授予了
     *
     * @param grantResults 申请的相应权限的授权结果
     */
    public static boolean allPermissionsAreGranted(@NonNull int[] grantResults) {
        boolean isAllGranted = true;

        if (grantResults.length > 0) {
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }
        }

        return isAllGranted;
    }

    /**
     * 初始化提示用户要申请的权限的Dialog
     *
     * @param activity      Activity对象
     * @param messageString message显示的内容
     * @param yesString     确定按钮显示的内容
     * @param noString      取消按钮显示的内容
     * @param b             是否退出APP true退出APP false不退出APP
     */
    public static void initPermissionsDialog(final Activity activity, String messageString, String yesString, String noString, final boolean b) {
        OtherUtil.setBackgroundAlpha(activity, 0.6f);
        final CustomDialog customDialog = new CustomDialog(activity);
        customDialog.setMessage(messageString);
        customDialog.setYesOnclickListener(yesString, () -> {
            OtherUtil.setBackgroundAlpha(activity, 1f);
            customDialog.dismiss();
            // 跳转到当前应用详情页面
            OtherUtil.openDetailSettingIntent(activity);
        });
        customDialog.setNoOnclickListener(noString, () -> {
            OtherUtil.setBackgroundAlpha(activity, 1f);
            customDialog.dismiss();
            new Handler().postDelayed(() -> {
                if (b) {
                    // 退出应用程序
                    StackManagerUtil.getInstance().appExit(activity);
                }
            }, 300);
        });
        customDialog.show();
    }

    /**
     * 提示应用缺少权限
     *
     * @param activity Activity对象
     */
    public static void showApplicationLacksPermissions(Activity activity) {
        PermissionsUtil.initPermissionsDialog(
                activity,
                activity.getResources().getString(R.string.the_current_application_lacks_the_necessary_permissions_please_authorize_the_permissions_required_by_the_app),
                activity.getResources().getString(R.string.manual_authorization),
                activity.getResources().getString(R.string.exit_the_app),
                true);
    }

    /**
     * 提示没有相机权限
     *
     * @param activity Activity对象
     */
    public static void showNoCameraPermissions(Activity activity) {
        PermissionsUtil.initPermissionsDialog(
                activity,
                activity.getResources().getString(R.string.camera_does_not_start_please_turn_on_camera_permission),
                activity.getResources().getString(R.string.determine),
                activity.getResources().getString(R.string.cancel),
                false);
    }
}
