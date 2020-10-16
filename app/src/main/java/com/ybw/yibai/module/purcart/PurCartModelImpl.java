package com.ybw.yibai.module.purcart;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_PURCART_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.UP_ALL_CART_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.UP_CARTGATE_METHOD;

/**
 * 进货Fragment Model 实现类
 *
 * @author sjl
 * @date 2019/11/5
 */
public class PurCartModelImpl implements PurCartContract.PurCartModel {

    private ApiService mApiService;

    public PurCartModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    @Override
    public void getPurCartData(PurCartContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<PurCartBean> observable = mApiService.getPurCart(timeStamp,
                OtherUtil.getSign(timeStamp, GET_PURCART_METHOD),
                YiBaiApplication.getUid());
        Observer<PurCartBean> observer = new Observer<PurCartBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(PurCartBean purCartBean) {
                if (purCartBean.getCode() == 200) {
                    callBack.onGetPurCartDataSuccess(purCartBean);
                } else {
                    callBack.onRequestFailure(new Throwable(purCartBean.getMsg()));
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
    public void updateCartGate(int cartId, int num, PurCartContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.upCartGate(timeStamp,
                OtherUtil.getSign(timeStamp, UP_CARTGATE_METHOD),
                YiBaiApplication.getUid(), cartId, num);
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getCode() == 200) {
                    callBack.onUpdateCartGateSuccess(num);
                } else {
                    callBack.onRequestFailure(new Throwable(baseBean.getMsg()));
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
    public void updateCartGateCheck(int cartId, int check, PurCartContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.updateCartGateCheck(timeStamp,
                OtherUtil.getSign(timeStamp, UP_CARTGATE_METHOD),
                YiBaiApplication.getUid(), cartId, check);
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getCode() == 200) {
                    callBack.onUpdateCartGateSuccess(check);
                } else {
                    callBack.onRequestFailure(new Throwable(baseBean.getMsg()));
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
    public void upAllCart(String cartIds, int type, int isCheck, PurCartContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable;
        if (isCheck == 3) {
            observable = mApiService.upAllCart(timeStamp,
                    OtherUtil.getSign(timeStamp, UP_ALL_CART_METHOD),
                    YiBaiApplication.getUid(), cartIds, type, 1);
        } else {
            observable = mApiService.upAllCart(timeStamp,
                    OtherUtil.getSign(timeStamp, UP_ALL_CART_METHOD),
                    YiBaiApplication.getUid(), cartIds, type, isCheck);
        }
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if (baseBean.getCode() == 200) {
                    if (isCheck == 3) {
                        callBack.onDeleteSuccess();
                    } else {
                        callBack.onUpAllCartSuccess(isCheck);
                    }
                } else {
                    callBack.onRequestFailure(new Throwable(baseBean.getMsg()));
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
