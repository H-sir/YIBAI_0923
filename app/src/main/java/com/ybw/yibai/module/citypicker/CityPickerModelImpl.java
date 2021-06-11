package com.ybw.yibai.module.citypicker;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CityListBean;
import com.ybw.yibai.common.bean.CityListHotBean;
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

/**
 * 首页界面Model实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class CityPickerModelImpl implements CityPickerContract.CityPickerModel {

    private ApiService mApiService;

    public CityPickerModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    @Override
    public void getCity(int type,CityPickerContract.CallBack callBack) {
        if(type ==1){
            String timeStamp = String.valueOf(TimeUtil.getTimestamp());
            Observable<CityListHotBean> observable = mApiService.getHotCity(timeStamp,
                    OtherUtil.getSign(timeStamp, GET_CITY_METHOD),
                    YiBaiApplication.getUid(),type);
            Observer<CityListHotBean> observer = new Observer<CityListHotBean>() {
                @Override
                public void onSubscribe(Disposable d) {
                    callBack.onRequestBefore(d);
                }

                @Override
                public void onNext(CityListHotBean cityListBean) {
                    callBack.onGetCityHotSuccess(type,cityListBean);
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
        }else {
            String timeStamp = String.valueOf(TimeUtil.getTimestamp());
            Observable<CityListBean> observable = mApiService.getCity(timeStamp,
                    OtherUtil.getSign(timeStamp, GET_CITY_METHOD),
                    YiBaiApplication.getUid(),type);
            Observer<CityListBean> observer = new Observer<CityListBean>() {
                @Override
                public void onSubscribe(Disposable d) {
                    callBack.onRequestBefore(d);
                }

                @Override
                public void onNext(CityListBean cityListBean) {
                    callBack.onGetCitySuccess(type,cityListBean);
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
}
