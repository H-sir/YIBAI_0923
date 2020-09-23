package com.ybw.yibai.module.shippingaddress;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.Address;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.shippingaddress.ShippingAddressContract.CallBack;
import com.ybw.yibai.module.shippingaddress.ShippingAddressContract.ShippingAddressModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_ADDRESS_METHOD;

/**
 * 收货地址界面Model实现类
 *
 * @author sjl
 */
public class ShippingAddressModelImpl implements ShippingAddressModel {

    private ApiService mApiService;

    public ShippingAddressModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取收货地址
     *
     * @param callBack 回调方法
     */
    @Override
    public void getShippingAddress(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Address> observable = mApiService.getReceiptAddress(timeStamp,
                OtherUtil.getSign(timeStamp, GET_ADDRESS_METHOD),
                YiBaiApplication.getUid());
        Observer<Address> observer = new Observer<Address>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(Address receiptAddress) {
                callBack.onGetShippingAddressSuccess(receiptAddress);
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
