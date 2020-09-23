package com.ybw.yibai.module.customerdetails;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.DeleteCustomer;
import com.ybw.yibai.common.bean.EditCustomer;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.customerdetails.CustomerDetailsContract.CallBack;
import com.ybw.yibai.module.customerdetails.CustomerDetailsContract.CustomerDetailsModel;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.HttpUrls.DELETE_CLIENT_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.EDIT_CLIENT_METHOD;

/**
 * 客户详情Model实现类
 *
 * @author sjl
 * @date 2019/10/25
 */
public class CustomerDetailsModelImpl implements CustomerDetailsModel {

    private ApiService mApiService;

    public CustomerDetailsModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 修改客户信息
     *
     * @param customersInfo 客户信息
     * @param callBack      回调方法
     */
    @Override
    public void editCustomer(CustomersInfo customersInfo, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Map<String, RequestBody> params = customersInfo.getParams();
        Observable<EditCustomer> observable;
        if (null == params || params.size() == 0) {
            observable = mApiService.editCustomer(timeStamp,
                    OtherUtil.getSign(timeStamp, EDIT_CLIENT_METHOD),
                    YiBaiApplication.getUid(),
                    customersInfo.getId(),
                    customersInfo.getName(),
                    customersInfo.getPhone(),
                    customersInfo.getAddress());
        } else {
            observable = mApiService.editCustomer(timeStamp,
                    OtherUtil.getSign(timeStamp, EDIT_CLIENT_METHOD),
                    YiBaiApplication.getUid(),
                    customersInfo.getId(),
                    customersInfo.getName(),
                    customersInfo.getPhone(),
                    customersInfo.getAddress(),
                    params);
        }
        Observer<EditCustomer> observer = new Observer<EditCustomer>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(EditCustomer editCustomer) {
                callBack.onRequestComplete();
                callBack.onEditCustomerSuccess(editCustomer);
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
     * 删除客户
     *
     * @param cid      客户ID
     * @param callBack 回调方法
     */
    @Override
    public void deleteCustomer(int cid, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DeleteCustomer> observable = mApiService.deleteCustomer(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_CLIENT_METHOD),
                YiBaiApplication.getUid(),
                cid);
        Observer<DeleteCustomer> observer = new Observer<DeleteCustomer>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DeleteCustomer deleteCustomer) {
                callBack.onRequestComplete();
                callBack.onDeleteCustomerSuccess(deleteCustomer);
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
}
