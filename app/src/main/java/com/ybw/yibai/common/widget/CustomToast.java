package com.ybw.yibai.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ybw.yibai.R;
import com.ybw.yibai.common.utils.DensityUtil;

/**
 * 自定义Toast
 *
 * @author sjl
 */
public class CustomToast {

    private static Toast toast;

    /**
     * Toast的边框宽度
     */
    private static final int STROKE_WIDTH = 0;

    /**
     * Toast的圆角半径
     */
    private static final float ROUND_RADIUS = 5f;

    /**
     * @param context               上下文对象（使用ApplicationContext避免内存泄漏）
     * @param string                在Toast上显示的字符串
     * @param textColorString       字符串文字颜色
     * @param backgroundColorString Toast背景颜色
     */
    public static void toast(Context context, String string, String textColorString, String backgroundColorString) {
        // 避免Toast长时间显示
        if (toast != null) {
            toast.cancel();
        }

        // 创建一个Toast对象
        toast = new Toast(context.getApplicationContext());

        // 加载一个自定义的布局
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        View view = inflater.inflate(R.layout.my_toast_layout, null);

        // 从自定义的Toast布局中找到TextView的对象
        TextView textView = view.findViewById(R.id.textView);

        // 设置Toast的显示的文本内容
        textView.setText(string);

        // 设置Toast的显示的文本内容的颜色
        textView.setTextColor(Color.parseColor(textColorString));

        // 设置Toast的背景颜色和圆角半径
        // 创建drawable对象
        GradientDrawable gd = new GradientDrawable();
        // 设置内部填充颜色
        gd.setColor(Color.parseColor(backgroundColorString));
        // 设置圆角半径
        gd.setCornerRadius(DensityUtil.dpToPx(context.getApplicationContext(), ROUND_RADIUS));
        // 设置边框的宽度和颜色
        gd.setStroke(STROKE_WIDTH, Color.parseColor(backgroundColorString));
        textView.setBackground(gd);

        // 设置Toast显示的位置
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, DensityUtil.dpToPx(context.getApplicationContext(), 149.5f));

        // 设置弹出Toast显示的时间
        toast.setDuration(Toast.LENGTH_SHORT);

        // 设置View的对象给Toast的对象
        toast.setView(view);

        // 显示Toast
        toast.show();
    }

    /**
     * @param context               上下文对象（使用ApplicationContext避免内存泄漏）
     * @param string                在Toast上显示的字符串
     * @param textColorString       字符串文字颜色
     * @param backgroundColorString Toast背景颜色
     */
    public static void toast(Context context, String string, int textColorString, int backgroundColorString) {
        // 避免Toast长时间显示
        if (toast != null) {
            toast.cancel();
        }

        // 创建一个Toast对象
        toast = new Toast(context.getApplicationContext());

        // 加载一个自定义的布局
        LayoutInflater inflater = LayoutInflater.from(context.getApplicationContext());

        View view = inflater.inflate(R.layout.my_toast_layout, null);

        // 从自定义的Toast布局中找到TextView的对象
        TextView textView = view.findViewById(R.id.textView);

        // 设置Toast的显示的文本内容
        textView.setText(string);

        // 设置Toast的显示的文本内容的颜色
        textView.setTextColor(ContextCompat.getColor(context, textColorString));

        // 设置Toast的背景颜色和圆角半径
        // 创建drawable对象
        GradientDrawable gd = new GradientDrawable();
        // 设置内部填充颜色
        gd.setColor(ContextCompat.getColor(context, backgroundColorString));
        // 设置圆角半径
        gd.setCornerRadius(DensityUtil.dpToPx(context.getApplicationContext(), ROUND_RADIUS));
        // 设置边框的宽度和颜色
        gd.setStroke(STROKE_WIDTH, ContextCompat.getColor(context, backgroundColorString));
        textView.setBackground(gd);

        // 设置Toast显示的位置
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, DensityUtil.dpToPx(context.getApplicationContext(), 149.5f));

        // 设置弹出Toast显示的时间
        toast.setDuration(Toast.LENGTH_SHORT);

        // 设置View的对象给Toast的对象
        toast.setView(view);

        // 显示Toast
        toast.show();
    }

    /**
     * 提供一个方法使Toast不在显示
     */
    public static void cancelToast() {
        if (null != toast) {
            toast.cancel();
        }
    }
}
