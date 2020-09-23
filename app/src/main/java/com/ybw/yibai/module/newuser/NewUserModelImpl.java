package com.ybw.yibai.module.newuser;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CustomersInfo;
import com.ybw.yibai.common.bean.CreateCustomers;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.newuser.NewUserContract.CallBack;
import com.ybw.yibai.module.newuser.NewUserContract.NewUserModel;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.HttpUrls.CREATE_CLIENT_METHOD;

/**
 * 创建新用户Model实现类
 *
 * @author sjl
 * @date 2019/9/19
 */
public class NewUserModelImpl implements NewUserModel {

    private ApiService mApiService;

    public NewUserModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 创建客户
     *
     * @param customersInfo 客户信息
     * @param callBack      回调方法
     */
    @Override
    public void newCustomers(CustomersInfo customersInfo, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Map<String, RequestBody> params = customersInfo.getParams();
        Observable<CreateCustomers> observable;
        if (null == params || params.size() == 0) {
            observable = mApiService.createCustomer(timeStamp,
                    OtherUtil.getSign(timeStamp, CREATE_CLIENT_METHOD),
                    YiBaiApplication.getUid(),
                    customersInfo.getName(),
                    customersInfo.getPhone(),
                    customersInfo.getAddress());
        } else {
            observable = mApiService.createCustomer(timeStamp,
                    OtherUtil.getSign(timeStamp, CREATE_CLIENT_METHOD),
                    YiBaiApplication.getUid(),
                    customersInfo.getName(),
                    customersInfo.getPhone(),
                    customersInfo.getAddress(),
                    params);
        }
        Observer<CreateCustomers> observer = new Observer<CreateCustomers>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CreateCustomers createCustomers) {
                callBack.onRequestComplete();
                callBack.onNewCustomersSuccess(createCustomers);
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
