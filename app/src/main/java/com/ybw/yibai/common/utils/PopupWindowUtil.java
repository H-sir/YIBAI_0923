package com.ybw.yibai.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.design.DesignActivity;
import com.ybw.yibai.module.scene.SceneActivity;
import com.ybw.yibai.module.startdesign.StartDesignActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.ybw.yibai.common.constants.Preferences.DESIGN_CREATE;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.constants.Preferences.USER_VIP_URL;
import static com.ybw.yibai.common.constants.Preferences.VIP_LEVEL;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/10/13
 *     desc   :
 * </pre>
 */
public class PopupWindowUtil {

    public static void displayUpdateVipPopupWindow(Activity activity, View rootLayout) {
        View view = View.inflate(activity, R.layout.popup_window_update_vip_layout, null);
        PopupWindow mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView updateVipImg = view.findViewById(R.id.update_vip_img);
        ImageView updateImmediatelyImageView = view.findViewById(R.id.updateImmediatelyImageView);
        ImageView closeImageView = view.findViewById(R.id.closeImageView);

        ImageUtil.displayImage(activity.getApplicationContext(), updateVipImg, "http://f.100ybw.com/images/updatevip.png");
        updateVipImg.setOnClickListener(v -> {
            SharedPreferences mSharedPreferences = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE);
            String userVipUrl = mSharedPreferences.getString(USER_VIP_URL, null);
            if (!TextUtils.isEmpty(userVipUrl)) {
                Intent intent = new Intent(activity, BrowserActivity.class);
                intent.putExtra(URL, userVipUrl);
                activity.startActivity(intent);
            }
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        });
        updateImmediatelyImageView.setOnClickListener(v -> {
            SharedPreferences mSharedPreferences = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE);
            String userVipUrl = mSharedPreferences.getString(USER_VIP_URL, null);
            if (!TextUtils.isEmpty(userVipUrl)) {
                Intent intent = new Intent(activity, BrowserActivity.class);
                intent.putExtra(URL, userVipUrl);
                activity.startActivity(intent);
            }
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        });
        closeImageView.setOnClickListener(v -> {
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 设置一个动画效果
        mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

        // 在弹出PopupWindow设置屏幕透明度
        OtherUtil.setBackgroundAlpha(activity, 0.6f);
        // 添加PopupWindow窗口关闭事件
        mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }

    /**
     * 创建设计的弹窗
     */
    public static void createScenePopupWindow(Activity activity, View rootLayout, boolean flag) {
        View view = View.inflate(activity, R.layout.popup_window_create_scene_layout, null);
        PopupWindow mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView existSceneCancel = view.findViewById(R.id.existSceneCancel);
        TextView existSceneContinue = view.findViewById(R.id.existSceneContinue);

        existSceneCancel.setOnClickListener(v -> {
            SharedPreferences preferences = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE);
            int vipLevel = preferences.getInt(VIP_LEVEL, 0);
            if (1 == vipLevel) {
                PopupWindowUtil.displayUpdateVipPopupWindow(activity, rootLayout);
                return;
            }
            Intent intent = new Intent(activity, SceneActivity.class);
            intent.putExtra(DESIGN_CREATE, true);
            activity.startActivity(intent);
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        });
        existSceneContinue.setOnClickListener(v -> {
            SharedPreferences preferences = activity.getSharedPreferences(USER_INFO, MODE_PRIVATE);
            int vipLevel = preferences.getInt(VIP_LEVEL, 0);
            if (1 == vipLevel) {
                PopupWindowUtil.displayUpdateVipPopupWindow(activity, rootLayout);
                return;
            }
            Intent intent = new Intent(activity, StartDesignActivity.class);
            activity.startActivity(intent);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            if (mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);

        // 设置一个动画效果
        mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

        // 在弹出PopupWindow设置屏幕透明度
        OtherUtil.setBackgroundAlpha(activity, 0.6f);
        // 添加PopupWindow窗口关闭事件
        mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(activity, 1f));
        mPopupWindow.showAtLocation(rootLayout, Gravity.CENTER, 0, 0);
    }
}
