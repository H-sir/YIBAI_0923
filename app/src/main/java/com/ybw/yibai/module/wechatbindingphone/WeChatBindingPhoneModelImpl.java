package com.ybw.yibai.module.wechatbindingphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.VerificationCode;
import com.ybw.yibai.common.bean.WeChatBindingPhone;
import com.ybw.yibai.common.bean.WeChatBindingPhone.DataBean;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.wechatbindingphone.WeChatBindingPhoneContract.CallBack;
import com.ybw.yibai.module.wechatbindingphone.WeChatBindingPhoneContract.WeChatBindingPhoneModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.ybw.yibai.common.constants.Encoded.CODE_SUCCEED;
import static com.ybw.yibai.common.constants.HttpUrls.GET_CODE;
import static com.ybw.yibai.common.constants.HttpUrls.WECHAT_BIND_MOBILE_PHONE_METHOD;
import static com.ybw.yibai.common.constants.Preferences.TOKEN;
import static com.ybw.yibai.common.constants.Preferences.UID;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 微信绑定手机Model实现类
 *
 * @author sjl
 * @date 2019/10/17
 */
public class WeChatBindingPhoneModelImpl implements WeChatBindingPhoneModel {

    private ApiService mApiService;

    public WeChatBindingPhoneModelImpl() {
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
     * 微信绑定手机
     *
     * @param mobile   手机号码
     * @param code     微信给的code
     * @param unionId  当且仅当该移动应用已获得该用户的 userinfo 授权时,才会出现该字段
     * @param openid   授权用户唯一标识
     * @param callBack 回调方法
     */
    @Override
    public void weChatBindingPhone(String mobile, String code, String unionId, String openid, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<WeChatBindingPhone> observable = mApiService.weChatBindingPhone(timeStamp,
                OtherUtil.getSign(timeStamp, WECHAT_BIND_MOBILE_PHONE_METHOD),
                mobile,
                code,
                unionId,
                openid);
        Observer<WeChatBindingPhone> observer = new Observer<WeChatBindingPhone>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(WeChatBindingPhone weChatBindingPhone) {
                saveDataToSharedPreferences(weChatBindingPhone);
                callBack.onRequestComplete();
                callBack.onWeChatBindingPhoneSuccess(weChatBindingPhone);
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
     * @param weChatBindingPhone 微信绑定手机时服务器端返回的数据
     */
    private void saveDataToSharedPreferences(WeChatBindingPhone weChatBindingPhone) {
        if (CODE_SUCCEED != weChatBindingPhone.getCode()) {
            return;
        }
        DataBean data = weChatBindingPhone.getData();
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
