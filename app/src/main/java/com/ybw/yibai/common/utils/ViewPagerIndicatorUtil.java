package com.ybw.yibai.common.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ybw.yibai.R;

/**
 * ViewPager指示器的工具类
 *
 * @author sjl
 */
public class ViewPagerIndicatorUtil {

    private static String TAG = "ViewPagerIndicatorUtil";

    /**
     * 根据滑动图片数量动态添加图片指示的圆点
     *
     * @param context         上下文对象
     * @param dotLinearLayout 指示点父布局
     * @param size            指示的圆点数量
     */
    public static void initDots(Context context, @NonNull LinearLayout dotLinearLayout, int size) {
        dotLinearLayout.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 0, 4, 0);
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(context);
            if (i == 0) {
                imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_pager_select_dot_style));
            } else {
                imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_pager_unselect_dot_style));
            }
            dotLinearLayout.addView(imageView, params);
        }
    }

    /**
     * 设置ViewPager轮播时选中对应的圆点
     *
     * @param context         上下文对象
     * @param dotLinearLayout 指示点父布局
     * @param position        ViewPager 当前选中的页面
     */
    public static void onPageSelectedDots(Context context, @NonNull LinearLayout dotLinearLayout, int position) {
        // 设置ViewPager轮播时选中对应的圆点
        int count = dotLinearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView imageView = (ImageView) dotLinearLayout.getChildAt(i);
            if (i == position % count) {
                imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_pager_select_dot_style));
            } else {
                imageView.setBackground(ContextCompat.getDrawable(context, R.drawable.view_pager_unselect_dot_style));
            }
        }
    }
}
