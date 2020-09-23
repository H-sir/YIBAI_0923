package com.ybw.yibai.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 自定义ViewPager,禁止左右滑动
 *
 * @author sjl
 */
public class ViewPagerNoSlide extends ViewPager {

    /**
     * 是否可以进行滑动
     */
    private boolean isSlide = true;

    public ViewPagerNoSlide(@NonNull Context context) {
        super(context);
    }

    public ViewPagerNoSlide(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isSlide) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!isSlide) {
            return true;
        } else {
            return false;
        }
    }

    public void setSlide(boolean slide) {
        isSlide = slide;
    }
}
