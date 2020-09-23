package com.ybw.yibai.common.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

/**
 * 屏幕适配工具类
 * https://www.jianshu.com/p/1eeb0d8d1c86
 * http://help.lanhuapp.com/hc/kb/article/1245740/
 * https://mp.weixin.qq.com/s/d9QCoBP6kV9VSWvVldVVwA
 *
 * @author sjl
 */
public class ScreenAdaptationUtils {

    /**
     * 系统的Density
     */
    private static float sNonCompatDensity;

    /**
     * 系统的ScaledDensity
     */
    private static float sNonCompatScaledDensity;

    /**
     * 应用屏幕适配
     *
     * @param application Application对象
     * @param activity    应用屏幕适配的界面
     * @param orientation 要适配屏幕的方向
     *                    android.content.res.Configuration.ORIENTATION_PORTRAIT
     *                    android.content.res.Configuration.ORIENTATION_LANDSCAPE
     */
    public static void applyAdaptationScreen(@NonNull Application application, @NonNull Activity activity, int orientation) {
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();

        if (0 == sNonCompatDensity) {
            sNonCompatDensity = appDisplayMetrics.density;
            sNonCompatScaledDensity = appDisplayMetrics.scaledDensity;
            // 监听在系统设置中切换字体
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (null != newConfig && newConfig.fontScale > 0) {
                        // 字体改变后,将sNonCompatScaledDensity重新赋值
                        sNonCompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }

        // 360dp的设计图
        float targetDensity;
        if (orientation == ORIENTATION_PORTRAIT) {
            targetDensity = appDisplayMetrics.widthPixels / 360;
        } else {
            targetDensity = appDisplayMetrics.heightPixels / 360;
        }
        float targetScaledDensity = targetDensity * (sNonCompatScaledDensity / sNonCompatDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    /**
     * 取消屏幕适配
     *
     * @param application Application对象
     * @param activity    取消屏幕适配的界面
     */
    public static void cancelAdaptationScreen(@NonNull Application application, @NonNull Activity activity) {
        DisplayMetrics systemDisplayMetrics = Resources.getSystem().getDisplayMetrics();
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();

        appDisplayMetrics.density = systemDisplayMetrics.density;
        appDisplayMetrics.scaledDensity = systemDisplayMetrics.scaledDensity;
        appDisplayMetrics.densityDpi = systemDisplayMetrics.densityDpi;

        activityDisplayMetrics.density = systemDisplayMetrics.density;
        activityDisplayMetrics.scaledDensity = systemDisplayMetrics.scaledDensity;
        activityDisplayMetrics.densityDpi = systemDisplayMetrics.densityDpi;
    }
}
