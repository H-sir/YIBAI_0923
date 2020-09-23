package com.ybw.yibai.module.quotationset;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.SetIncrease;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.quotationset.QuotationSetContract.CallBack;
import com.ybw.yibai.module.quotationset.QuotationSetContract.QuotationSetModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.SET_INCREASE_METHOD;

/**
 * 报价设置Model实现类
 *
 * @author sjl
 * @date 2019/12/2
 */
public class QuotationSetModelImpl implements QuotationSetModel {

    private ApiService mApiService;

    public QuotationSetModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 设置价格增幅
     *
     * @param increaseRent 租价幅度%
     * @param increaseSell 售价幅度%
     * @param callBack     回调方法
     */
    @Override
    public void setIncrease(String increaseRent, String increaseSell, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SetIncrease> observable = mApiService.setIncrease(timeStamp,
                OtherUtil.getSign(timeStamp, SET_INCREASE_METHOD),
                YiBaiApplication.getUid(),
                increaseRent,
                increaseSell);
        Observer<SetIncrease> observer = new Observer<SetIncrease>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SetIncrease setIncrease) {
                callBack.onSetIncreaseSuccess(setIncrease);
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
