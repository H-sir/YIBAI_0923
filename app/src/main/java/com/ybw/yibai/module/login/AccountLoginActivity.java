package com.ybw.yibai.module.login;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.LoginInfo;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.login.LoginContract.LoginPresenter;
import com.ybw.yibai.module.login.LoginContract.LoginView;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.wechatbindingphone.WeChatBindingPhoneActivity;
import com.ybw.yibai.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.LOGIN_INFO;

/**
 * 账号密码登录
 *
 * @author sjl
 * @date 2019/09/11
 */
public class AccountLoginActivity extends BaseActivity implements
        LoginView, View.OnClickListener, TextView.OnEditorActionListener {

    /**
     * 输入账号
     */
    private EditText mAccountEditText;

    /**
     * 输入密码
     */
    private EditText mPasswordEditText;

    /**
     * 登陆
     */
    private Button mLoginButton;

    /**
     * 手机快捷登录
     */
    private RelativeLayout mPhoneLoginLayout;

    /**
     * 微信登陆
     */
    private ImageView mWeChatImageView;

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

    /**
     * View 持有 Presenter的接口引用
     * <p>
     * PresenterImpl 作用为触发加载数据
     */
    private LoginPresenter mLoginPresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_account_login;
    }

    @Override
    protected void initView() {
        mAccountEditText = findViewById(R.id.accountEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mLoginButton = findViewById(R.id.loginButton);

        mPhoneLoginLayout = findViewById(R.id.phoneLoginLayout);
        mWeChatImageView = findViewById(R.id.weChatImageView);
        mWaitDialog = new WaitDialog(this);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initEvent() {
        // 注册事件
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mLoginPresenter = new LoginPresenterImpl(this);
        mAccountEditText.setOnEditorActionListener(this);
        mPasswordEditText.setOnEditorActionListener(this);
        mPhoneLoginLayout.setOnClickListener(this);

        initLoginButtonClickEvent();
        initWeChatImageViewClickEvent();

    }

    @Override
    public void onNetworkStateChange(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // 手机快捷登录
        if (id == R.id.phoneLoginLayout) {
            Intent intent = new Intent(this, PhoneLoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    private void initLoginButtonClickEvent() {
        // 只发送2s内第1次点击按钮的事件
        Observable<Object> observable = RxView.clicks(mLoginButton)
                .throttleFirst(2, TimeUnit.SECONDS);
        Observer<Object> observer = new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                login();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
    }

    private void initWeChatImageViewClickEvent() {
        // 只发送2s内第1次点击按钮的事件
        Observable<Object> observable = RxView.clicks(mWeChatImageView)
                .throttleFirst(2, TimeUnit.SECONDS);
        Observer<Object> observer = new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                weChat();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        observable.subscribe(observer);
    }

    /**
     * 判断用户是否点击了"确定"按钮
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            login();
            return true;
        }
        return false;
    }

    /**
     * 登录操作
     */
    private void login() {
        // 获取用户名/手机号
        String account = mAccountEditText.getText().toString().trim();
        // 获取密码
        String password = mPasswordEditText.getText().toString().trim();
        // 请求登录
        mLoginPresenter.login(account, password, 0);
    }

    /**
     * 与微信相关的操作
     */
    private void weChat() {
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

    /**
     * 在获取验证码成功时回调
     *
     * @param verificationCode 获取验证码数据
     */
    @Override
    public void onGetVerificationCodeSuccess(VerificationCode verificationCode) {
        if (!TextUtils.isEmpty(verificationCode.getMsg())) {
            MessageUtil.showMessage(verificationCode.getMsg());
        }
    }

    /**
     * 在请求登录成功时回调
     *
     * @param loginInfo 登陆数据
     */
    @Override
    public void onLoginSuccess(LoginInfo loginInfo) {
        MessageUtil.showMessage(loginInfo.getMsg());
        if (CODE_SUCCEED == loginInfo.getCode()) {
            // 友盟统计登录
            MobclickAgent.onProfileSignIn(String.valueOf(loginInfo.getData().getUid()));
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (202 == loginInfo.getCode()) {
            Intent intent = new Intent(this, WeChatBindingPhoneActivity.class);
            intent.putExtra(LOGIN_INFO, loginInfo.getData());
            startActivity(intent);
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
                mLoginPresenter.wechatLogin(code);
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
        if (null != mLoginPresenter) {
            if (EventBus.getDefault().isRegistered(this)) {
                // 解除注册
                EventBus.getDefault().unregister(this);
            }
            mLoginPresenter.onDetachView();
            mLoginPresenter = null;
        }
    }
}
