package com.ybw.yibai.module.city;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_CITY_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.SET_USER_POSITION_METHOD;

/**
 * 首页界面Model实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class CityModelImpl implements CityContract.CityModel {

    private ApiService mApiService;

    public CityModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 设置货源城市
     * */
    @Override
    public void setUserPosition(String code, CityContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UserPosition> observable = mApiService.setCity(timeStamp,
                OtherUtil.getSign(timeStamp, SET_USER_POSITION_METHOD),
                YiBaiApplication.getUid(),
                code);
        Observer<UserPosition> observer = new Observer<UserPosition>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UserPosition userPosition) {
                callBack.onSetUserPositionSuccess(userPosition);
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
     * 获取城市列表
     * */
    @Override
    public void getCity(CityContract.CallBack callBack) {
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
