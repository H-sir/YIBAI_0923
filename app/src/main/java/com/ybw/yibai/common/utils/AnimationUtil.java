package com.ybw.yibai.common.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;

/**
 * 动画工具类
 *
 * @author sjl
 */
public class AnimationUtil {

    /**
     * 缩放动画
     */
    public static void zoomAnimation(View view, float... values) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", values);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", values);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(scaleX).with(scaleY);
        animatorSet.setDuration(200);
        animatorSet.start();
    }

    /**
     * 以view中心为缩放点,开始缩放动画
     *
     * @param view      要开始缩放动画的View对象
     * @param beginning 起始时View的大小
     * @param end       结束时View的大小
     */
    public static void centerZoomAnimation(@NonNull View view, float beginning, float end) {
        ScaleAnimation animation = new ScaleAnimation(
                beginning,
                end,
                beginning,
                end,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        animation.setDuration(500);
        view.startAnimation(animation);
    }

    /**
     * http://m.sohu.com/a/148745016_611601
     */
    public static class CustomTransformer implements ViewPager.PageTransformer {

        final float MIN_SCALE = 0.8f;

        @Override
        public void transformPage(@NonNull View view, float position) {
            if (position < -1 || position > 1) {
                // 不可见区域
                view.setScaleX(MIN_SCALE);
                view.setScaleY(MIN_SCALE);
            } else {
                // 可见区域
                float scale = Math.max(MIN_SCALE, 1 - Math.abs(position));
                view.setScaleX(scale);
                view.setScaleY(scale);
            }
        }
    }
}
