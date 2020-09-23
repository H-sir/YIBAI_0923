package com.ybw.yibai.module.about;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.about.AboutUsContract.AboutUsModel;
import com.ybw.yibai.module.about.AboutUsContract.CallBack;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.APP_UPDATE_METHOD;

/**
 * 关于我们界面Model实现类
 *
 * @author sjl
 */
public class AboutUsModelImpl implements AboutUsModel {

    private ApiService mApiService;

    public AboutUsModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 请求应用更新
     *
     * @param callBack 回调方法
     */
    @Override
    public void appUpdate(final CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<AppUpdate> observable = mApiService.appUpdate(timeStamp,
                OtherUtil.getSign(timeStamp, APP_UPDATE_METHOD),
                YiBaiApplication.getUid());
        Observer<AppUpdate> observer = new Observer<AppUpdate>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(AppUpdate value) {
                callBack.onAppUpdateSuccess(value);
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
