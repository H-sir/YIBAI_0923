package com.ybw.yibai.module.productusestate;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.CallBack;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.ProductUseStateModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_PRODUCT_SCREENING_PARAM_METHOD;

/**
 * 产品使用状态Model实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class ProductUseStateModelImpl implements ProductUseStateModel {

    private final String TAG = "ProductTypeModelImpl";

    private ApiService mApiService;

    public ProductUseStateModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取产品筛选参数
     *
     * @param callBack 回调方法
     */
    @Override
    public void getProductScreeningParam(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<ProductScreeningParam> observable = mApiService.getProductScreeningParam(timeStamp,
                OtherUtil.getSign(timeStamp, GET_PRODUCT_SCREENING_PARAM_METHOD),
                YiBaiApplication.getUid());
        Observer<ProductScreeningParam> observer = new Observer<ProductScreeningParam>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(ProductScreeningParam productScreeningParam) {
                callBack.onGetProductScreeningParamSuccess(productScreeningParam);
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