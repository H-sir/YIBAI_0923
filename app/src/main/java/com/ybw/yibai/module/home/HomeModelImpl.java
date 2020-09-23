package com.ybw.yibai.module.home;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CustomerList;
import com.ybw.yibai.common.bean.HotScheme;
import com.ybw.yibai.common.bean.HotSchemeCategory;
import com.ybw.yibai.common.bean.HotSchemes;
import com.ybw.yibai.common.bean.RecommendProductList;
import com.ybw.yibai.common.bean.UserPosition;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.home.HomeContract.CallBack;
import com.ybw.yibai.module.home.HomeContract.HomeModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.CLIENT_LIST_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_HOT_SCHEMES_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_HOT_SCHEME_CATEGORY_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_HOT_SCHEME_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_RECOMMEND_LIST_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.SET_USER_POSITION_METHOD;

/**
 * 首页界面Model实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class HomeModelImpl implements HomeModel {

    private ApiService mApiService;

    public HomeModelImpl() {
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

    /**
     * 获取方案分类
     *
     * @param callBack 回调方法
     */
    @Override
    public void getHotSchemeCategory(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<HotSchemeCategory> observable = mApiService.getHotSchemeCategory(timeStamp,
                OtherUtil.getSign(timeStamp, GET_HOT_SCHEME_CATEGORY_METHOD),
                YiBaiApplication.getUid());
        Observer<HotSchemeCategory> observer = new Observer<HotSchemeCategory>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(HotSchemeCategory hotSchemeCategory) {
                callBack.onGetHotSchemeCategorySuccess(hotSchemeCategory);
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
     * 获取热门方案列表
     *
     * @param cateId   方案分类id
     * @param callBack 回调方法
     */
    @Override
    public void getHotScheme(int cateId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<HotScheme> observable = mApiService.getHotScheme(timeStamp,
                OtherUtil.getSign(timeStamp, GET_HOT_SCHEME_METHOD),
                YiBaiApplication.getUid(),
                cateId);
        Observer<HotScheme> observer = new Observer<HotScheme>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(HotScheme hotScheme) {
                callBack.onGetHotSchemeSuccess(hotScheme);
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
     * 获取新热门方案列表
     *
     * @param cateId   方案分类id
     * @param callBack 回调方法
     */
    @Override
    public void getHotSchemes(int cateId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<HotSchemes> observable = mApiService.getHotSchemes(timeStamp,
                OtherUtil.getSign(timeStamp, GET_HOT_SCHEMES_METHOD),
                YiBaiApplication.getUid(),
                cateId);
        Observer<HotSchemes> observer = new Observer<HotSchemes>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(HotSchemes hotSchemes) {
                callBack.onGetHotSchemesSuccess(hotSchemes);
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
     * 获取推荐产品列表
     */
    @Override
    public void getRecommendProductList(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<RecommendProductList> observable = mApiService.getRecommendProductList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_RECOMMEND_LIST_METHOD),
                YiBaiApplication.getUid());
        Observer<RecommendProductList> observer = new Observer<RecommendProductList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(RecommendProductList recommendProductList) {
                callBack.onGetRecommendProductListSuccess(recommendProductList);
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
     * 设置货源城市
     *
     * @param position 城市字符串（广东省广州市）
     * @param callBack 回调方法
     */
    @Override
    public void setUserPosition(String position, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UserPosition> observable = mApiService.setUserPosition(timeStamp,
                OtherUtil.getSign(timeStamp, SET_USER_POSITION_METHOD),
                YiBaiApplication.getUid(),
                position);
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
}
