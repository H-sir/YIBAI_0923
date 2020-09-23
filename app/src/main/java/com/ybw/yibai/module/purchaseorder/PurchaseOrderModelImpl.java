package com.ybw.yibai.module.purchaseorder;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.PurchaseOrderList;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.purchaseorder.PurchaseOrderContract.PurchaseOrderModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_PURCHASE_ORDER_LIST_METHOD;

/**
 * 已进货订单Model实现类
 *
 * @author sjl
 * @date 2019/10/14
 */
public class PurchaseOrderModelImpl implements PurchaseOrderModel {

    private ApiService mApiService;

    public PurchaseOrderModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取获取采购单列表
     *
     * @param isAll    0分页1不分页默认为1
     * @param state    状态类型: 0/空获取所有,1待付款,2待配送3待收货4已完成
     * @param callBack 回调方法
     */
    @Override
    public void getPurchaseOrderList(int isAll, int state, PurchaseOrderContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<PurchaseOrderList> observable = mApiService.getPurchaseOrderList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_PURCHASE_ORDER_LIST_METHOD),
                YiBaiApplication.getUid(),
                isAll,
                state);
        Observer<PurchaseOrderList> observer = new Observer<PurchaseOrderList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(PurchaseOrderList purchaseOrderList) {
                callBack.onGetPurchaseOrderListSuccess(purchaseOrderList);
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
