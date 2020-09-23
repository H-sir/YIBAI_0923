package com.ybw.yibai.module.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BindingWechat;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.ImageUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.about.AboutActivity;
import com.ybw.yibai.module.bindingphone.BindingPhoneActivity;
import com.ybw.yibai.module.browser.BrowserActivity;
import com.ybw.yibai.module.feedback.FeedBackActivity;
import com.ybw.yibai.module.headportrait.HeadPortraitActivity;
import com.ybw.yibai.module.modifypassword.ModifyPasswordActivity;
import com.ybw.yibai.module.nickname.NicknameActivity;
import com.ybw.yibai.module.setting.SettingContract.SettingPresenter;
import com.ybw.yibai.module.setting.SettingContract.SettingView;
import com.ybw.yibai.module.shippingaddress.ShippingAddressActivity;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingActivity;
import com.ybw.yibai.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.HttpUrls.USING_HELP_URL;
import static com.ybw.yibai.common.constants.Preferences.HEAD;
import static com.ybw.yibai.common.constants.Preferences.IS_BIND_WX;
import static com.ybw.yibai.common.constants.Preferences.NICK_NAME;
import static com.ybw.yibai.common.constants.Preferences.TELEPHOTO;
import static com.ybw.yibai.common.constants.Preferences.URL;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 设置界面
 *
 * @author sjl
 * @date 2019/10/12
 */
public class SettingActivity extends BaseActivity implements SettingView, View.OnClickListener {

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 头像
     */
    private RelativeLayout mHeadPortraitLayout;

    /**
     * 头像
     */
    private ImageView mHeadPortraitImageView;

    /**
     * 昵称
     */
    private RelativeLayout mNickNameLayout;

    /**
     * 昵称
     */
    private TextView mNicknameTextView;

    /**
     * 绑定手机
     */
    private RelativeLayout mMobilePhoneBindingLayout;

    /**
     * 绑定手机
     */
    private TextView mMobilePhoneBindingTextView;

    /**
     * 绑定微信
     */
    private RelativeLayout mWechatBindingLayout;

    /**
     * 绑定微信
     */
    private TextView mWechatBindingTextView;

    /**
     * 收货地址
     */
    private TextView mShippingAddressTextView;

    /**
     * 问题反馈
     */
    private TextView mFeedbackTextView;

    /**
     * 水印设置
     */
    private TextView mWatermarkSettingTextView;

    /**
     * 关于易摆
     */
    private TextView mAboutYibaiTextView;

    /**
     * 使用帮助
     */
    private TextView mUsingHelpTextView;

    /**
     * 修改密码
     */
    private TextView mModifyPasswordTextView;

    /**
     * 切换账号
     */
    private Button mSwitchAccountButton;

    /**
     * 自定义等待Dialog
     */
    private WaitDialog mWaitDialog;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    private SharedPreferences mPreferences;

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private SettingPresenter mSettingPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mHeadPortraitLayout = findViewById(R.id.headPortraitLayout);
        mHeadPortraitImageView = findViewById(R.id.headPortraitImageView);
        mNickNameLayout = findViewById(R.id.nickNameLayout);
        mNicknameTextView = findViewById(R.id.nicknameTextView);
        mMobilePhoneBindingLayout = findViewById(R.id.mobilePhoneBindingLayout);
        mMobilePhoneBindingTextView = findViewById(R.id.mobilePhoneBindingTextView);
        mWechatBindingLayout = findViewById(R.id.wechatBindingLayout);
        mWechatBindingTextView = findViewById(R.id.wechatBindingTextView);
        mShippingAddressTextView = findViewById(R.id.shippingAddressTextView);

        mFeedbackTextView = findViewById(R.id.feedbackTextView);
        mWatermarkSettingTextView = findViewById(R.id.watermarkSettingTextView);
        mAboutYibaiTextView = findViewById(R.id.aboutYibaiTextView);
        mUsingHelpTextView = findViewById(R.id.usingHelpTextView);

        mModifyPasswordTextView = findViewById(R.id.modifyPasswordTextView);

        mSwitchAccountButton = findViewById(R.id.switchAccountButton);

        mWaitDialog = new WaitDialog(this);
        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        mPreferences = getSharedPreferences(USER_INFO, MODE_PRIVATE);
    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mSettingPresenter = new SettingPresenterImpl(this);
        mBackImageView.setOnClickListener(this);

        mHeadPortraitLayout.setOnClickListener(this);
        mNickNameLayout.setOnClickListener(this);
        mMobilePhoneBindingLayout.setOnClickListener(this);
        mWechatBindingLayout.setOnClickListener(this);
        mShippingAddressTextView.setOnClickListener(this);

        mFeedbackTextView.setOnClickListener(this);
        mWatermarkSettingTextView.setOnClickListener(this);
        mAboutYibaiTextView.setOnClickListener(this);
        mUsingHelpTextView.setOnClickListener(this);

        mModifyPasswordTextView.setOnClickListener(this);

        mSwitchAccountButton.setOnClickListener(this);
    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        String icon = mPreferences.getString(HEAD, null);
        String nickName = mPreferences.getString(NICK_NAME, null);
        String telephone = mPreferences.getString(TELEPHOTO, null);
        int bindWechat = mPreferences.getInt(IS_BIND_WX, 0);

        if (!TextUtils.isEmpty(icon)) {
            ImageUtil.displayImage(this, mHeadPortraitImageView, icon);
        }
        if (!TextUtils.isEmpty(nickName)) {
            mNicknameTextView.setText(nickName);
        }
        if (!TextUtils.isEmpty(telephone)) {
            mMobilePhoneBindingTextView.setText(telephone);
        }
        if (0 == bindWechat) {
            mWechatBindingTextView.setText(getResources().getString(R.string.no_binding));
        } else {
            mWechatBindingTextView.setText(getResources().getString(R.string.already_binding));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 返回
        if (id == R.id.backImageView) {
            onBackPressed();
        }

        // 头像
        if (id == R.id.headPortraitLayout) {
            Intent intent = new Intent(this, HeadPortraitActivity.class);
            startActivity(intent);
        }

        // 昵称
        if (id == R.id.nickNameLayout) {
            Intent intent = new Intent(this, NicknameActivity.class);
            startActivity(intent);
        }

        // 手机绑定
        if (id == R.id.mobilePhoneBindingLayout) {
            Intent intent = new Intent(this, BindingPhoneActivity.class);
            startActivity(intent);
        }

        // 微信绑定
        if (id == R.id.wechatBindingLayout) {
            IWXAPI iWxApi = YiBaiApplication.getIWXAPI();
            if (null == iWxApi || !iWxApi.isWXAppInstalled()) {
                MessageUtil.showMessage(getResources().getString(R.string.wechat_is_not_installed));
                return;
            }
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "yibai";
            iWxApi.sendReq(req);
        }

        // 收货地址
        if (id == R.id.shippingAddressTextView) {
            Intent intent = new Intent(this, ShippingAddressActivity.class);
            startActivity(intent);
        }

        // 问题反馈
        if (id == R.id.feedbackTextView) {
            Intent intent = new Intent(this, FeedBackActivity.class);
            startActivity(intent);
        }

        // 水印设置
        if (id == R.id.watermarkSettingTextView) {
            Intent intent = new Intent(this, WatermarkSettingActivity.class);
            startActivity(intent);
        }

        // 关于易摆
        if (id == R.id.aboutYibaiTextView) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        // 使用帮助
        if (id == R.id.usingHelpTextView) {
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra(URL, USING_HELP_URL);
            startActivity(intent);
        }

        // 修改密码
        if (id == R.id.modifyPasswordTextView) {
            Intent intent = new Intent(this, ModifyPasswordActivity.class);
            startActivity(intent);
        }

        // 切换账号
        if (id == R.id.switchAccountButton) {
            mSettingPresenter.displaySwitchAccountDialog();
        }
    }

    /**
     * EventBus
     * 接收用户从{@link WXEntryActivity#onResp(BaseResp)}
     * 发送过来的数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void weChatEntryActivitySendData(BaseResp baseResp) {
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                // 发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) baseResp;
                String code = sendResp.code;
                mSettingPresenter.bindingWechat(code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                // 发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                // 发送被拒绝
                break;
            default:
                // 发送返回
                break;
        }
    }

    /**
     * 在绑定微信成功时回调
     *
     * @param bindingWechat 在绑定微信成功时服务器端返回的数据
     */
    @Override
    public void onBindingWechatSuccess(BindingWechat bindingWechat) {
        if (!TextUtils.isEmpty(bindingWechat.getMsg())) {
            MessageUtil.showMessage(bindingWechat.getMsg());
        }
        if (CODE_SUCCEED == bindingWechat.getCode()) {
            // 保存已经绑定微信了的数据
            SharedPreferences.Editor edit = mPreferences.edit();
            edit.putInt(IS_BIND_WX, 1);
            edit.apply();
            mWechatBindingTextView.setText(getResources().getString(R.string.already_binding));
        }
    }

    /**
     * 在请求网络数据之前显示Loading界面
     */
    @Override
    public void onShowLoading() {
        if (!mWaitDialog.isShowing()) {
            mWaitDialog.setWaitDialogText(getResources().getString(R.string.loading));
            mWaitDialog.show();
        }
    }

    /**
     * 在请求网络数据完成隐藏Loading界面
     */
    @Override
    public void onHideLoading() {
        if (mWaitDialog.isShowing()) {
            mWaitDialog.dismiss();
        }
    }

    /**
     * 在请求网络数据失败时进行一些操作,如显示错误信息...
     *
     * @param throwable 异常类型
     */
    @Override
    public void onLoadDataFailure(Throwable throwable) {
        ExceptionUtil.handleException(throwable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mSettingPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mSettingPresenter.onDetachView();
            mSettingPresenter = null;
        }
    }
}
