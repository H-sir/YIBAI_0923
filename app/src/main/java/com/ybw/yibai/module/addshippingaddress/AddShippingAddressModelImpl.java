package com.ybw.yibai.module.addshippingaddress;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CityAreaList;
import com.ybw.yibai.common.bean.DeleteShippingAddress;
import com.ybw.yibai.common.bean.ShippingAddress;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressContract.AddShippingAddressModel;
import com.ybw.yibai.module.addshippingaddress.AddShippingAddressContract.CallBack;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.CREATE_OR_MODIFY_SHIPPING_ADDRESS_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DELETE_SHIPPING_ADDRESS_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_CITY_AREA_LIST_METHOD;

/**
 * 添加收货地址Model实现类
 *
 * @author sjl
 * @date 2019/10/11
 */
public class AddShippingAddressModelImpl implements AddShippingAddressModel {

    private ApiService mApiService;

    public AddShippingAddressModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取城市区域列表
     *
     * @param callBack 回调方法
     */
    @Override
    public void getCityAreaList(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CityAreaList> observable = mApiService.getCityAreaList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_CITY_AREA_LIST_METHOD),
                YiBaiApplication.getUid());
        Observer<CityAreaList> observer = new Observer<CityAreaList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CityAreaList cityAreaList) {
                callBack.onGetCityAreaListSucceed(cityAreaList);
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
     * 创建/修改收货地址
     *
     * @param areaId    区id
     * @param consignee 收货人
     * @param mobile    联系电话
     * @param address   详细地址
     * @param isDefault 是否默认(1是0否)
     * @param callBack  回调方法
     */
    @Override
    public void createOrModifyShippingAddress(Integer addressId, int areaId, String consignee, String mobile, String address, int isDefault, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<ShippingAddress> observable = mApiService.createOrModifyTheShippingAddress(timeStamp,
                OtherUtil.getSign(timeStamp, CREATE_OR_MODIFY_SHIPPING_ADDRESS_METHOD),
                YiBaiApplication.getUid(),
                addressId,
                areaId,
                consignee,
                mobile,
                address,
                isDefault);
        Observer<ShippingAddress> observer = new Observer<ShippingAddress>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(ShippingAddress shippingAddress) {
                callBack.onRequestComplete();
                callBack.onCreateOrModifyShippingAddressSucceed(shippingAddress);
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
     * 删除收货地址
     *
     * @param addressId 收货地址id
     */
    @Override
    public void deleteShippingAddress(int addressId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DeleteShippingAddress> observable = mApiService.deleteShippingAddress(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_SHIPPING_ADDRESS_METHOD),
                YiBaiApplication.getUid(),
                addressId);
        Observer<DeleteShippingAddress> observer = new Observer<DeleteShippingAddress>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DeleteShippingAddress deleteShippingAddress) {
                callBack.onRequestComplete();
                callBack.onDeleteShippingAddressSucceed(deleteShippingAddress);
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
