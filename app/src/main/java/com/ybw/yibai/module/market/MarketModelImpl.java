package com.ybw.yibai.module.market;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.SkuMarketBean;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_SKU_MARKET_METHOD;

/**
 * <pre>
 *     author : HKR
 *     time   : 2020/09/02
 *     desc   :
 * </pre>
 */
public class MarketModelImpl implements MarketContract.MarketModel {

    public static final String TAG = "MarketModelImpl";

    private ApiService mApiService;

    public MarketModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    @Override
    public void getSkuMarket(int productSkuId, MarketContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<SkuMarketBean> observable = mApiService.getSkuMarket(timeStamp,
                OtherUtil.getSign(timeStamp, GET_SKU_MARKET_METHOD),
                YiBaiApplication.getUid(),
                productSkuId);
        Observer<SkuMarketBean> observer = new Observer<SkuMarketBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(SkuMarketBean skuMarketBean) {
                callBack.onGetSkuMarketSuccess(skuMarketBean);
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
