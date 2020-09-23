package com.ybw.yibai.common.widget.magicindicator.buildins;

import android.content.Context;
import android.util.TypedValue;

public final class Util {

    /**
     * dp转px
     */
    public static int dipToPx(Context context, double dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) dpVal, context.getResources().getDisplayMetrics());
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }
}