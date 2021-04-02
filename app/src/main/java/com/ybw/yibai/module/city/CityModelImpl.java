package com.ybw.yibai.module.city;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

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
public class CityModelImpl implements CityContract.CityModel {

    private ApiService mApiService;

    public CityModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 设置货源城市
     */
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
                if (userPosition.getCode() == 200) {
                    callBack.onSetUserPositionSuccess(userPosition);
                }
//                else {
//                    callBack.onRequestFailure(new Throwable(userPosition.getMsg()));
//                }
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
     */
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
                if (cityListBean.getCode() == 200) {
                    callBack.onGetCitySuccess(cityListBean);
                } else {
                    callBack.onRequestFailure(new Throwable(cityListBean.getMsg()));
                }
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

    @Override
    public void getLocation(double latitude, double longitude, CityContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<PlaceBean> observable = mApiService.getPlace(timeStamp,
                OtherUtil.getSign(timeStamp, GET_CITY_PLACE_METHOD),
                YiBaiApplication.getUid(), String.valueOf(longitude), String.valueOf(latitude));
        Observer<PlaceBean> observer = new Observer<PlaceBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(PlaceBean placeBean) {
                if (placeBean.getCode() == 200) {
                    callBack.onGetLocationSuccess(placeBean);
                } else {
                    callBack.onRequestFailure(new Throwable(placeBean.getMsg()));
                }
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

    @Override
    public void onSetProduct(int comOpen, CityContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<EditUserInfo> observable = mApiService.editUserProductInfo(timeStamp,
                OtherUtil.getSign(timeStamp, EDIT_USER_INFO_METHOD),
                YiBaiApplication.getUid(), comOpen);
        Observer<EditUserInfo> observer = new Observer<EditUserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(EditUserInfo editUserInfo) {
                if (editUserInfo.getCode() == 200) {
                    Context context = YiBaiApplication.getContext();
                    SharedPreferences sharedPreferences = context.getSharedPreferences(USER_INFO, MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString(COM_OPEN, String.valueOf(comOpen));
                    edit.apply();

                    callBack.onSetProductSuccess(editUserInfo);
                } else {
                    callBack.onRequestFailure(new Throwable(editUserInfo.getMsg()));
                }
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

    @Override
    public void getMarketList(double latitude, double longitude, CityContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<MarketListBean> observable = mApiService.getMarketList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_MARKET_LIST_METHOD),
                YiBaiApplication.getUid(), longitude, latitude, "no", "v3");
        Observer<MarketListBean> observer = new Observer<MarketListBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(MarketListBean marketListBean) {
                if (marketListBean.getCode() == 200) {
                    callBack.onGetMarketListSuccess(marketListBean);
                } else {
                    callBack.onRequestFailure(new Throwable(marketListBean.getMsg()));
                }
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

    @Override
    public void bindMarket(MarketListBean marketListBean, int marketId, CityContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.bindMarket(timeStamp,
                OtherUtil.getSign(timeStamp, BIND_MARKET_METHOD),
                YiBaiApplication.getUid(), marketId);
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean mBaseBean) {
                if (mBaseBean.getCode() == 200) {
                    callBack.onBindMarketSuccess(marketListBean, marketId);
                } else {
                    callBack.onRequestFailure(new Throwable(mBaseBean.getMsg()));
                }
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

    @Override
    public void setEditUser(double latitude, double longitude, CityContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<EditUserInfo> observable = mApiService.editUserProductInfo(timeStamp,
                OtherUtil.getSign(timeStamp, EDIT_USER_INFO_METHOD),
                YiBaiApplication.getUid(), (float) latitude, (float) longitude);
        Observer<EditUserInfo> observer = new Observer<EditUserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(EditUserInfo editUserInfo) {
                if (editUserInfo.getCode() == 200) {
                    callBack.onSetEditUserSuccess(editUserInfo);
                } else {
                    callBack.onRequestFailure(new Throwable(editUserInfo.getMsg()));
                }
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
