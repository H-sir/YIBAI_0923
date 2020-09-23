package com.ybw.yibai.module.login;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.TextView;

import com.ybw.yibai.R;
import com.ybw.yibai.base.BasePresenterImpl;
import com.ybw.yibai.common.bean.LoginInfo;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.utils.MessageUtil;
import com.ybw.yibai.common.utils.NetworkStateUtil;
import com.ybw.yibai.module.login.LoginContract.CallBack;
import com.ybw.yibai.module.login.LoginContract.LoginModel;
import com.ybw.yibai.module.login.LoginContract.LoginPresenter;
import com.ybw.yibai.module.login.LoginContract.LoginView;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;

/**
 * 登陆界面Presenter实现类
 *
 * @author sjl
 * @date 2018/12/10
 */
public class LoginPresenterImpl extends BasePresenterImpl<LoginView> implements LoginPresenter, CallBack {

    /**
     * 获取验证码TextView
     */
    private TextView mGetVerificationCodeTextView;

    /**
     * PresenterImpl 持有 View的接口引用
     */
    private LoginView mLoginView;

    /**
     * PresenterImpl 持有 Model的接口引用
     */
    private LoginModel mLoginModel;

    /**
     * 构造方法
     *
     * @param view View的对象
     */
    public LoginPresenterImpl(LoginView view) {
        super(view);

        // 调用父类的方法获取View的对象
        this.mLoginView = getView();
        mLoginModel = new LoginModelImpl();
    }

    /**
     * 获取验证码
     *
     * @param getVerificationCodeTextView 获取验证码TextView
     * @param phoneNumber                 手机号/邮箱
     */
    @Override
    public void getVerificationCode(TextView getVerificationCodeTextView, String phoneNumber) {
        Activity activity = (Activity) mLoginView;
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            // 没有网络
            MessageUtil.showNoNetwork();
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            // 手机号码为空
            MessageUtil.showMessage(activity.getResources().getString(R.string.phone_number_is_empty));
            return;
        }
        // 获取验证码
        mLoginModel.getVerificationCode(phoneNumber, this);
        mGetVerificationCodeTextView = getVerificationCodeTextView;
    }

    /**
     * 在获取验证码成功时回调
     *
     * @param verificationCode 获取验证码数据
     */
    @Override
    public void onGetVerificationCodeSuccess(VerificationCode verificationCode) {
        mLoginView.onGetVerificationCodeSuccess(verificationCode);
        if (CODE_SUCCEED != verificationCode.getCode()) {
            return;
        }
        Activity activity = (Activity) mLoginView;
        // 设置60秒计数
        final long count = 60;
        Function<Long, Long> function = new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long aLong) throws Exception {
                // 由于是倒计时,需要将倒计时的数字反过来
                return count - aLong;
            }
        };
        Consumer<Disposable> consumer = new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                mGetVerificationCodeTextView.setEnabled(false);
                int color = ContextCompat.getColor(activity, R.color.prompt_low_text_color);
                mGetVerificationCodeTextView.setTextColor(color);
            }
        };
        Observer<Long> observer = new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable disposable) {
            }

            @Override
            public void onNext(Long aLong) {
                if (aLong == 0) {
                    mGetVerificationCodeTextView.setText(activity.getResources().getString(R.string.reacquire));
                } else {
                    String string = activity.getResources().getString(R.string.reacquire) + aLong;
                    mGetVerificationCodeTextView.setText(string);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
                mGetVerificationCodeTextView.setEnabled(true);
                int color = ContextCompat.getColor(activity, R.color.colorPrimary);
                mGetVerificationCodeTextView.setTextColor(color);
            }
        };
        Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(function)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(consumer)
                .subscribe(observer);
    }

    /**
     * 请求登录
     *
     * @param account  用户名/手机号
     * @param password 用户密码
     * @param isRandom 是否是验证码登陆(1是0否)
     */
    @Override
    public void login(String account, String password, int isRandom) {
        Activity activity = (Activity) this.mLoginView;
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            // 没有网络
            MessageUtil.showNoNetwork();
            return;
        }
        if (TextUtils.isEmpty(account) && TextUtils.isEmpty(password)) {
            // 账号与密码为空
            MessageUtil.showMessage(activity.getResources().getString(R.string.account_and_password_are_empty));
        } else if (TextUtils.isEmpty(account)) {
            // 账号为空
            MessageUtil.showMessage(activity.getResources().getString(R.string.account_are_empty));
        } else if (TextUtils.isEmpty(password)) {
            // 密码为空
            MessageUtil.showMessage(activity.getResources().getString(R.string.password_are_empty));
        } else {
            // 请求登录
            mLoginModel.login(account, password, isRandom, this);
        }
    }

    /**
     * 微信登陆
     *
     * @param code 微信给的code
     */
    @Override
    public void wechatLogin(String code) {
        Activity activity = (Activity) this.mLoginView;
        if (!NetworkStateUtil.isNetworkAvailable(activity)) {
            // 没有网络
            MessageUtil.showNoNetwork();
            return;
        }
        mLoginModel.wechatLogin(code, this);
    }

    /**
     * 在请求登录成功时回调
     *
     * @param loginInfo 登陆数据
     */
    @Override
    public void onLoginSuccess(LoginInfo loginInfo) {
        mLoginView.onLoginSuccess(loginInfo);
    }
}
