package com.ybw.yibai.common.utils;

import android.app.Activity;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnGuideChangedListener;
import com.app.hubert.guide.model.GuidePage;

/**
 * 引导层Util
 *
 * @author sjl
 */
public class GuideUtil {

    /**
     * Activity界面显示引导层
     *
     * @param activity Activity 对象
     * @param resId    引导层资源文件
     */
    public static void showGuideView(Activity activity, @LayoutRes int resId) {
        OnGuideChangedListener onGuideChangedListener = new OnGuideChangedListener() {
            @Override
            public void onShowed(Controller controller) {
                // 引导层显示
            }

            @Override
            public void onRemoved(Controller controller) {

            }
        };
        GuidePage guidePage = GuidePage.newInstance()
                // 是否点击任意位置消失引导页
                .setEverywhereCancelable(true)
                // 设置引导页布局
                .setLayoutRes(resId);
        NewbieGuide
                .with(activity)
                // 引导层显示隐藏监听
                .setOnGuideChangedListener(onGuideChangedListener)
                // 添加设置引导页的标签
                .setLabel(activity.getClass().getSimpleName())
                // 添加一页引导页
                .addGuidePage(guidePage)
                // 总是显示,调试时可以打开
                .alwaysShow(false)
                // 显示引导层
                .show();
    }

    /**
     * Fragment界面显示引导层
     *
     * @param fragment Fragment 对象
     * @param resId    引导层资源文件
     */
    public static void showGuideView(Fragment fragment, @LayoutRes int resId) {
        OnGuideChangedListener onGuideChangedListener = new OnGuideChangedListener() {
            @Override
            public void onShowed(Controller controller) {
                // 引导层显示
            }

            @Override
            public void onRemoved(Controller controller) {

            }
        };
        GuidePage guidePage = GuidePage.newInstance()
                // 是否点击任意位置消失引导页
                .setEverywhereCancelable(true)
                // 设置引导页布局
                .setLayoutRes(resId);
        NewbieGuide
                .with(fragment)
                // 引导层显示隐藏监听
                .setOnGuideChangedListener(onGuideChangedListener)
                // 添加设置引导页的标签
                .setLabel(fragment.getClass().getSimpleName())
                // 添加一页引导页
                .addGuidePage(guidePage)
                // 总是显示,调试时可以打开
                .alwaysShow(false)
                // 显示引导层
                .show();
    }

    /**
     * Fragment界面显示引导层
     *
     * @param fragment Fragment 对象
     * @param resId    引导层资源文件
     * @param label    引导页的标签名称
     */
    public static void showGuideView(Fragment fragment, @LayoutRes int resId, String label) {
        OnGuideChangedListener onGuideChangedListener = new OnGuideChangedListener() {
            @Override
            public void onShowed(Controller controller) {
                // 引导层显示
            }

            @Override
            public void onRemoved(Controller controller) {

            }
        };
        GuidePage guidePage = GuidePage.newInstance()
                // 是否点击任意位置消失引导页
                .setEverywhereCancelable(true)
                // 设置引导页布局
                .setLayoutRes(resId);
        NewbieGuide
                .with(fragment)
                // 引导层显示隐藏监听
                .setOnGuideChangedListener(onGuideChangedListener)
                // 添加设置引导页的标签
                .setLabel(label)
                // 添加一页引导页
                .addGuidePage(guidePage)
                // 总是显示,调试时可以打开
                .alwaysShow(false)
                // 显示引导层
                .show();
    }
}
