package com.ybw.yibai.module.change;

import android.content.Context;
import android.content.SharedPreferences;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.EditUserInfo;
import com.ybw.yibai.common.bean.MarketListBean;
import com.ybw.yibai.common.bean.PlaceBean;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.city.CityContract;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;
import static com.ybw.yibai.common.constants.HttpUrls.BIND_MARKET_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.EDIT_USER_INFO_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_CITY_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_CITY_PLACE_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_MARKET_LIST_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.SET_USER_POSITION_METHOD;
import static com.ybw.yibai.common.constants.Preferences.COM_OPEN;
import static com.ybw.yibai.common.constants.Preferences.USER_INFO;

/**
 * 首页界面Model实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class ChangeAddressModelImpl implements ChangeAddressContract.ChangeAddressModel {

    private ApiService mApiService;

    public ChangeAddressModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    @Override
    public void getCity(ChangeAddressContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CityListBean> observable = mApiService.getCity(timeStamp,
                OtherUtil.getSign(timeStamp, GET_CITY_METHOD),
                YiBaiApplication.getUid());
        Observer<CityListBean> observer = new Observer<CityListBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CityListBean cityListBean) {
                callBack.onGetCitySuccess(cityListBean);
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
