package com.ybw.yibai.module.modifypassword;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.ModifyPassword;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.modifypassword.ModifyPasswordContract.CallBack;
import com.ybw.yibai.module.modifypassword.ModifyPasswordContract.ModifyPasswordModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_CODE;
import static com.ybw.yibai.common.constants.HttpUrls.MODIFY_PASSWORD_METHOD;

/**
 * 通过手机号码修改密码Model实现类
 *
 * @author sjl
 */
public class ModifyPasswordModelImpl implements ModifyPasswordModel {

    private ApiService mApiService;

    public ModifyPasswordModelImpl() {
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
     * 修改密码
     *
     * @param verificationCode 短信验证码
     * @param newPassword      新密码
     * @param callBack         回调方法
     */
    @Override
    public void modifyPassword(String verificationCode, String newPassword, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<ModifyPassword> observable = mApiService.modifyPassword(timeStamp,
                OtherUtil.getSign(timeStamp, MODIFY_PASSWORD_METHOD),
                YiBaiApplication.getUid(),
                null,
                newPassword,
                2,
                verificationCode);
        Observer<ModifyPassword> observer = new Observer<ModifyPassword>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(ModifyPassword modifyPassword) {
                callBack.onRequestComplete();
                callBack.onModifyPasswordSuccess(modifyPassword);
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
