package com.ybw.yibai.module.setting;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BindingWechat;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.setting.SettingContract.CallBack;
import com.ybw.yibai.module.setting.SettingContract.SettingModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.BINDING_WECHAT_METHOD;

/**
 * 设置界面Model实现类
 *
 * @author sjl
 * @date 2019/10/25
 */
public class SettingModelImpl implements SettingModel {

    private ApiService mApiService;

    public SettingModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 绑定微信
     *
     * @param code     微信给的code
     * @param callBack 回调方法
     */
    @Override
    public void bindingWechat(String code, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BindingWechat> observable = mApiService.bindingWechat(timeStamp,
                OtherUtil.getSign(timeStamp, BINDING_WECHAT_METHOD),
                YiBaiApplication.getUid(),
                code);
        Observer<BindingWechat> observer = new Observer<BindingWechat>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BindingWechat bindingWechat) {
                callBack.onBindingWechatSuccess(bindingWechat);
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
}
