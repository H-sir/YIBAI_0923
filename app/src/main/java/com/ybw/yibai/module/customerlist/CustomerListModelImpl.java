package com.ybw.yibai.module.customerlist;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.customerlist.CustomerListContract.CallBack;
import com.ybw.yibai.module.customerlist.CustomerListContract.CustomerListModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.CLIENT_LIST_METHOD;

/**
 * 客户列表Model实现类
 *
 * @author sjl
 * @date 2019/10/14
 */
public class CustomerListModelImpl implements CustomerListModel {

    private ApiService mApiService;

    public CustomerListModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取客户列表
     *
     * @param callBack 回调方法
     */
    @Override
    public void getCustomerList(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CustomerList> observable = mApiService.getCustomerList(timeStamp,
                OtherUtil.getSign(timeStamp, CLIENT_LIST_METHOD),
                YiBaiApplication.getUid());
        Observer<CustomerList> observer = new Observer<CustomerList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CustomerList customersList) {
                callBack.onGetCustomerListSuccess(customersList);
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
