package com.ybw.yibai.module.confirmorder;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.PurchaseOrder;
import com.ybw.yibai.common.bean.Address;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.confirmorder.ConfirmOrderContract.*;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.CREATE_PURCHASE_ORDER_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_ADDRESS_METHOD;

/**
 * 确认进货订单Model实现类
 *
 * @author sjl
 * @date 2019/10/10
 */
public class ConfirmOrderModelImpl implements ConfirmOrderModel {

    private ApiService mApiService;

    public ConfirmOrderModelImpl() {
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

    /**
     * 创建采购订单
     *
     * @param json      商品信息
     * @param addressId 收货地址id
     * @param callBack  回调方法
     */
    @Override
    public void createPurchaseOrder(String json, int addressId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<PurchaseOrder> observable = mApiService.createPurchaseOrder(timeStamp,
                OtherUtil.getSign(timeStamp, CREATE_PURCHASE_ORDER_METHOD),
                YiBaiApplication.getUid(),
                json,
                addressId);
        Observer<PurchaseOrder> observer = new Observer<PurchaseOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(PurchaseOrder purchaseOrder) {
                callBack.onCreatePurchaseOrderSuccess(purchaseOrder);
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
