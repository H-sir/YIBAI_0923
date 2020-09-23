package com.ybw.yibai.common.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;

/**
 * 常用单位转换的辅助类
 *
 * @author sjl
 */
public class DensityUtil {

    private DensityUtil() {
        throw new UnsupportedOperationException("不能被实例化");
    }

    /**
     * dp转px
     */
    public static int dpToPx(@NonNull Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     */
    public static int spToPx(@NonNull Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     */
    public static float pxToDp(@NonNull Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     */
    public static float pxToSp(@NonNull Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}
