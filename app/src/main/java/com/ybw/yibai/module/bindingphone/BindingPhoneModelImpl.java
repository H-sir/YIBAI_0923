package com.ybw.yibai.module.bindingphone;


import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BindingPhone;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.bindingphone.BindingPhoneContract.CallBack;
import com.ybw.yibai.module.bindingphone.BindingPhoneContract.BindingPhoneModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.BINDING_PHONE_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_CODE;

/**
 * 修改绑定的手机号码Model实现类
 *
 * @author sjl
 */
public class BindingPhoneModelImpl implements BindingPhoneModel {

    private ApiService mApiService;

    public BindingPhoneModelImpl() {
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
     * 绑定手机号
     *
     * @param phoneNumber      手机号
     * @param verificationCode 短信验证码
     * @param callBack         回调方法
     */
    @Override
    public void bindingPhone(String phoneNumber, String verificationCode, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BindingPhone> observable = mApiService.bindingPhone(timeStamp,
                OtherUtil.getSign(timeStamp, BINDING_PHONE_METHOD),
                YiBaiApplication.getUid(),
                phoneNumber,
                verificationCode);
        Observer<BindingPhone> observer = new Observer<BindingPhone>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BindingPhone bindingPhone) {
                callBack.onRequestComplete();
                callBack.onBindingPhoneSuccess(bindingPhone);
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
}
