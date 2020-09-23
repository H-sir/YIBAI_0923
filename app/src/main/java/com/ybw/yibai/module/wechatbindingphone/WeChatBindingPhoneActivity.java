package com.ybw.yibai.module.wechatbindingphone;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.ybw.yibai.R;
import com.ybw.yibai.base.BaseActivity;
import com.ybw.yibai.common.bean.LoginInfo.DataBean;
import com.ybw.yibai.common.bean.NetworkType;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.bean.WeChatBindingPhone;
import com.ybw.yibai.common.utils.ExceptionUtil;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.widget.WaitDialog;
import com.ybw.yibai.module.main.MainActivity;
import com.ybw.yibai.module.wechatbindingphone.WeChatBindingPhoneContract.WeChatBindingPhonePresenter;
import com.ybw.yibai.module.wechatbindingphone.WeChatBindingPhoneContract.WeChatBindingPhoneView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.Preferences.LOGIN_INFO;

/**
 * 微信绑定手机
 *
 * @author sjl
 * @date 2019/10/17
 */
public class WeChatBindingPhoneActivity extends BaseActivity implements
        WeChatBindingPhoneView, View.OnClickListener, TextView.OnEditorActionListener {

    /**
     *
     */
    private String openid;

    /**
     *
     */
    private String unionId;

    /*
      /\_/\
    =( °w° )=
      )   (
     (__ __)
    */

    /**
     * 返回
     */
    private ImageView mBackImageView;

    /**
     * 输入手机号
     */
    private EditText mMobileNumberEditText;

    /**
     * 输入验证码
     */
    private EditText mVerificationCodeEditText;

    /**
     * 获取短信验证码
     */
    private TextView mGetVerificationCodeTextView;

    /**
     * 确定
     */
    private Button mDetermineButton;

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
    private WeChatBindingPhonePresenter mWeChatBindingPhonePresenter;

    @Override
    protected int setLayout() {
        return R.layout.activity_we_chat_binding_phone;
    }

    @Override
    protected void initView() {
        mBackImageView = findViewById(R.id.backImageView);
        mMobileNumberEditText = findViewById(R.id.mobileNumberEditText);
        mVerificationCodeEditText = findViewById(R.id.verificationCodeEditText);
        mGetVerificationCodeTextView = findViewById(R.id.sendVerificationCodeTextView);
        mDetermineButton= findViewById(R.id.determineButton);
        mWaitDialog = new WaitDialog(this);

        // 设置状态栏成白色的背景,字体颜色为黑色
        OtherUtil.setStatusBarColor(this, ContextCompat.getColor(this, android.R.color.white));
    }

    @Override
    protected void initData() {
        DataBean data = (DataBean) getIntent().getSerializableExtra(LOGIN_INFO);
        if (null == data) {
            return;
        }
        openid = data.getOpenid();
        unionId = data.getUnionid();
    }

    @Override
    protected void initEvent() {
        mWeChatBindingPhonePresenter = new WeChatBindingPhonePresenterImpl(this);

        mBackImageView.setOnClickListener(this);
        mMobileNumberEditText.setOnEditorActionListener(this);
        mVerificationCodeEditText.setOnEditorActionListener(this);
        mGetVerificationCodeTextView.setOnClickListener(this);

        initDetermineButtonClickEvent();
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

        // 获取短信验证码
        if (id == R.id.sendVerificationCodeTextView) {
            String phoneNumber = mMobileNumberEditText.getText().toString().trim();
            mWeChatBindingPhonePresenter.getVerificationCode(mGetVerificationCodeTextView, phoneNumber);
        }
    }

    private void initDetermineButtonClickEvent() {
        // 只发送2s内第1次点击按钮的事件
        Observable<Object> observable = RxView.clicks(mDetermineButton)
                .throttleFirst(2, TimeUnit.SECONDS);
        Observer<Object> observer = new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Object o) {
                bindingPhone();
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
            bindingPhone();
            return true;
        }
        return false;
    }

    /**
     * 微信绑定手机
     */
    private void bindingPhone() {
        // 获取手机号
        String phoneNumber = mMobileNumberEditText.getText().toString().trim();
        // 获取验证码
        String verificationCode = mVerificationCodeEditText.getText().toString().trim();
        mWeChatBindingPhonePresenter.weChatBindingPhone(phoneNumber, verificationCode, unionId, openid);
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
     * 在微信绑定手机号码成功时回调
     *
     * @param weChatBindingPhone 微信绑定手机时服务器端返回的数据
     */
    @Override
    public void onWeChatBindingPhoneSuccess(WeChatBindingPhone weChatBindingPhone) {
        MessageUtil.showMessage(weChatBindingPhone.getMsg());
        if (CODE_SUCCEED == weChatBindingPhone.getCode()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
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
        if (null != mWeChatBindingPhonePresenter) {
            mWeChatBindingPhonePresenter.onDetachView();
            mWeChatBindingPhonePresenter = null;
        }
    }
}
