package com.ybw.yibai.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * 自定义ViewPager,去除切换时动画效果
 *
 * @author sjl
 */
public class ViewPagerNoAnim extends ViewPager {

    public ViewPagerNoAnim(@NonNull Context context) {
        super(context);
    }

    public ViewPagerNoAnim(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentItem(int item) {
        // 第二个参数就是是否需要动画,这里禁止
        super.setCurrentItem(item, false);
    }
}
