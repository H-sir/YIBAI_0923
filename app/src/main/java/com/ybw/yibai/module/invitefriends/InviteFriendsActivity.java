package com.ybw.yibai.module.invitefriends;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.EncryptionUtil;
import com.ybw.yibai.common.utils.FileUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.QRCodeUtil;
import com.ybw.yibai.module.browser.BrowserActivity;

import java.io.File;
import java.io.IOException;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.ybw.yibai.common.constants.Folder.PICTURES_PATH;
import static com.ybw.yibai.common.constants.Folder.QR_CODE_IMAGE_PREFIX;
import static com.ybw.yibai.common.constants.Folder.SHARE_IMAGE_PREFIX;
import static com.ybw.yibai.common.constants.Preferences.HEAD;
import static com.ybw.yibai.common.constants.Preferences.INVITE_BG;
import static com.ybw.yibai.common.constants.Preferences.INVITE_RULE_BG;
import static com.ybw.yibai.common.constants.Preferences.INVITE_URL;
import static com.ybw.yibai.common.constants.Preferences.NICK_NAME;
import static com.ybw.yibai.common.constants.Preferences.RECORD_URL;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.getSDCardPrivateFilesDir;
import static com.ybw.yibai.common.utils.SDCardHelperUtil.isSDCardMounted;

/**
 * 邀请好友
 *
 * @author sjl
 * @date 2019/10/16
 */
public class InviteFriendsActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 邀请注册链接
     */
    private String inviteUrl;

    /**
     * 邀请记录地址
     */
    private String recordUrl;

    /**
     * 邀请规则图片
     */
    private String inviteRuleBg;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * Root Layout
     */
    private RelativeLayout mRootLayout;

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 详细规则
     */
    private TextView mDetailedRulesTextView;

    /**
     * 查看我的邀请
     */
    private TextView mCheckMyInvitationTextView;

    /**
     *
     */
    private LinearLayout mShareLinearLayout;

    /**
     * 邀请说明图片
     */
    private ImageView mImageView;

    /**
     * 用户头像
     */
    private ImageView mHeadPortraitImageView;

    /**
     * 用户名称
     */
    private TextView mUserNameTextView;

    /**
     * 二维码
     */
    private ImageView mQrCodeImageView;

    /**
     * 保存到手机
     */
    private TextView mSaveToYourPhoneTextView;

    /**
     * 微信好友
     */
    private LinearLayout mWechatFriendsLayout;

    /**
     * 朋友圈
     */
    private LinearLayout mFriendsCircleLayout;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     *
     */
    private PopupWindow mPopupWindow;

    @Override
    protected int setLayout() {
        return R.layout.activity_invite_friends;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {
        mRootLayout = findViewById(R.id.rootLayout);
        mBackImageView = findViewById(R.id.backImageView);
        mDetailedRulesTextView = findViewById(R.id.detailedRulesTextView);
        mCheckMyInvitationTextView = findViewById(R.id.checkMyInvitationTextView);

        mShareLinearLayout = findViewById(R.id.shareLinearLayout);
        mImageView = findViewById(R.id.imageView);
        mHeadPortraitImageView = findViewById(R.id.headPortraitImageView);
        mUserNameTextView = findViewById(R.id.userNameTextView);
        mQrCodeImageView = findViewById(R.id.qrCodeImageView);

        mSaveToYourPhoneTextView = findViewById(R.id.saveToYourPhoneTextView);
        mWechatFriendsLayout = findViewById(R.id.wechatFriendsLayout);
        mFriendsCircleLayout = findViewById(R.id.friendsCircleLayout);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        SharedPreferences preferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
        String inviteBg = preferences.getString(INVITE_BG, null);
        String headPortrait = preferences.getString(HEAD, null);
        String nickName = preferences.getString(NICK_NAME, null);
        inviteUrl = preferences.getString(INVITE_URL, null);
        recordUrl = preferences.getString(RECORD_URL, null);
        inviteRuleBg = preferences.getString(INVITE_RULE_BG, null);
        if (!TextUtils.isEmpty(inviteBg)) {
            ImageUtil.displayImage(this, mImageView, inviteBg);
        }
        if (!TextUtils.isEmpty(inviteBg)) {
            ImageUtil.displayImage(this, mHeadPortraitImageView, headPortrait);
        }
        if (!TextUtils.isEmpty(nickName)) {
            mUserNameTextView.setText(nickName);
        }
        if (TextUtils.isEmpty(inviteUrl)) {
            return;
        }
        String qrCodeFilePath = getSDCardPrivateFilesDir(this, DIRECTORY_PICTURES) +
                File.separator + QR_CODE_IMAGE_PREFIX + EncryptionUtil.sha1(inviteUrl) + ".png";
        if (isSDCardMounted()) {
            QRCodeUtil.createQRImage(inviteUrl, 400, 400, null, qrCodeFilePath);
        }
        File file = new File(qrCodeFilePath);
        ImageUtil.displayImage(this, mQrCodeImageView, file);
    }

    @Override
    protected void initEvent() {
        mBackImageView.setOnClickListener(this);
        mDetailedRulesTextView.setOnClickListener(this);

        mCheckMyInvitationTextView.setOnClickListener(this);

        mSaveToYourPhoneTextView.setOnClickListener(this);
        mWechatFriendsLayout.setOnClickListener(this);
        mFriendsCircleLayout.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 详细规则
        if (id == R.id.detailedRulesTextView) {
            displayDetailedRulesPopupWindow();
        }

        // 查看我的邀请
        if (id == R.id.checkMyInvitationTextView) {
            if (TextUtils.isEmpty(recordUrl)) {
                return;
            }
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra(URL, recordUrl);
            startActivity(intent);
        }

        // 保存到手机
        if (id == R.id.saveToYourPhoneTextView) {
            handleShare(1);
        }

        // 微信好友
        if (id == R.id.wechatFriendsLayout) {
            handleShare(2);
        }

        // 朋友圈
        if (id == R.id.friendsCircleLayout) {
            handleShare(3);
        }
    }

    private void handleShare(int type) {
        IWXAPI iWxApi = YiBaiApplication.getIWXAPI();
        if (null == iWxApi || !iWxApi.isWXAppInstalled()) {
            MessageUtil.showMessage(getResources().getString(R.string.wechat_is_not_installed));
            return;
        }

        Bitmap bitmap = ImageUtil.viewConversionBitmap(mShareLinearLayout);
        if (TextUtils.isEmpty(inviteUrl) || null == bitmap) {
            return;
        }
        String fileName = SHARE_IMAGE_PREFIX + EncryptionUtil.sha1(inviteUrl);
        String shareImageFilePath = ImageUtil.saveImage(bitmap, fileName);
        if (TextUtils.isEmpty(shareImageFilePath)) {
            return;
        }
        File file = new File(shareImageFilePath);
        if (!file.exists()) {
            return;
        }
        if (1 == type) {
            String path = FileUtil.createExternalStorageFile(PICTURES_PATH);
            if (TextUtils.isEmpty(path)) {
                MessageUtil.showMessage(getResources().getString(R.string.failed_to_save_image));
            }
            try {
                FileUtil.copyFile(shareImageFilePath, path + file.getName());
                // 发送广播通知相册更新图片
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(new File(path + file.getName()));
                intent.setData(uri);
                sendBroadcast(intent);
                MessageUtil.showMessage(getResources().getString(R.string.image_saved_to) + path + getResources().getString(R.string.folder));
            } catch (IOException e) {
                e.printStackTrace();
                MessageUtil.showMessage(getResources().getString(R.string.failed_to_save_image));
            }
        } else if (2 == type) {
            ImageUtil.shareImageToWeChatFriend(bitmap);
        } else {
            ImageUtil.shareImageToWeChatMoment(bitmap);
        }
    }

    private void displayDetailedRulesPopupWindow() {
        if (null == mPopupWindow) {
            View view = getLayoutInflater().inflate(R.layout.popup_window_detailed_rules_layout, null);
            mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            ImageView closeImageView = view.findViewById(R.id.closeImageView);
            ImageView imageView = view.findViewById(R.id.imageView);
            closeImageView.setOnClickListener(v -> {
                if (null != mPopupWindow && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            });
            ImageUtil.displayImage(this, imageView, inviteRuleBg);

            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setFocusable(true);

            // 设置一个动画效果
            mPopupWindow.setAnimationStyle(R.style.PopupWindow_Anim);

            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(this, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(this, 1f));
            mPopupWindow.showAtLocation(mRootLayout, Gravity.CENTER, 0, 0);
        } else {
            // 在弹出PopupWindow设置屏幕透明度
            OtherUtil.setBackgroundAlpha(this, 0.6f);
            // 添加PopupWindow窗口关闭事件
            mPopupWindow.setOnDismissListener(OtherUtil.popupDismissListener(this, 1f));
            mPopupWindow.showAtLocation(mRootLayout, Gravity.CENTER, 0, 0);
        }
    }
}
