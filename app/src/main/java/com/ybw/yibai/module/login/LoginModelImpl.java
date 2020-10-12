package com.ybw.yibai.module.login;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.LoginInfo;
import com.ybw.yibai.common.bean.LoginInfo.DataBean;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.bean.VloeaBean;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.login.LoginContract.CallBack;
import com.ybw.yibai.module.login.LoginContract.LoginModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.HttpUrls.GET_CODE;
import static com.ybw.yibai.common.constants.HttpUrls.LOGIN_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.WECHAT_LOGIN_METHOD;
import static com.ybw.yibai.common.constants.Preferences.TOKEN;
import static com.ybw.yibai.common.constants.Preferences.UID;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 登陆界面Model实现类
 *
 * @author sjl
 * @date 2018/12/10
 */
public class LoginModelImpl implements LoginModel {

    private ApiService mApiService;

    public LoginModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取验证码
     *
     * @param phoneNumber 手机号
     * @param callBack    回调方法
     */
    @Override
    public void getVerificationCode(String phoneNumber, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<VerificationCode> observable = mApiService.getVerificationCode(timeStamp,
                OtherUtil.getSign(timeStamp, GET_CODE),
                phoneNumber);
        Observer<VerificationCode> observer = new Observer<VerificationCode>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(VerificationCode verificationCode) {
                callBack.onGetVerificationCodeSuccess(verificationCode);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {
                callBack.onRequestComplete();
            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 请求登录
     *
     * @param userName 用户名
     * @param password 用户密码
     * @param isRandom 是否是验证码登陆(1是0否)(验证码登陆的没有账号则会自动生成账号)
     * @param callBack 回调方法
     */
    @Override
    public void login(String userName, String password, int isRandom, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<LoginInfo> observable = mApiService.login(timeStamp,
                OtherUtil.getSign(timeStamp, LOGIN_METHOD),
                userName,
                password,
                isRandom);
        Observer<LoginInfo> observer = new Observer<LoginInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(LoginInfo loginInfo) {
                saveDataToSharedPreferences(loginInfo);
                callBack.onRequestComplete();
                callBack.onLoginSuccess(loginInfo);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {

            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 微信登陆
     *
     * @param code     微信给的code
     * @param callBack 回调方法
     */
    @Override
    public void wechatLogin(String code, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<LoginInfo> observable = mApiService.wechatLogin(timeStamp,
                OtherUtil.getSign(timeStamp, WECHAT_LOGIN_METHOD),
                code);
        Observer<LoginInfo> observer = new Observer<LoginInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(LoginInfo loginInfo) {
                saveDataToSharedPreferences(loginInfo);
                callBack.onRequestComplete();
                callBack.onLoginSuccess(loginInfo);
            }

            @Override
            public void onError(Throwable e) {
                callBack.onRequestFailure(e);
            }

            @Override
            public void onComplete() {

            }
        };
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(observer);
    }

    /**
     * 保存数据到SharedPreferences
     *
     * @param loginInfo 登陆返回的数据
     */
    private void saveDataToSharedPreferences(LoginInfo loginInfo) {
        if (CODE_SUCCEED != loginInfo.getCode()) {
            return;
        }
        DataBean data = loginInfo.getData();
        if (null == data) {
            return;
        }
        // 登录成功后保存这些信息
        Context context = YiBaiApplication.getContext();
        SharedPreferences preferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putInt(UID, data.getUid());
        String token = data.getToken();
        if (!TextUtils.isEmpty(token)) {
            edit.putString(TOKEN, token);
        }
        edit.apply();
    }
}
