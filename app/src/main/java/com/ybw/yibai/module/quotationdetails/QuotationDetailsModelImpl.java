package com.ybw.yibai.module.quotationdetails;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.QuotationAgain;
import com.ybw.yibai.common.bean.QuotationList;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsContract.CallBack;
import com.ybw.yibai.module.quotationdetails.QuotationDetailsContract.QuotationDetailsModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.QUOTATION_AGAIN_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.QUOTATION_LIST_METHOD;

/**
 * 报价列表界面Model实现类
 *
 * @author sjl
 * @date 2019/09/28
 */
public class QuotationDetailsModelImpl implements QuotationDetailsModel {

    private ApiService mApiService;

    public QuotationDetailsModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取报价列表
     *
     * @param type     0/空获取所有,1待客户确定,2待跟进3已成交4已失效
     * @param isAll    0分页1不分页默认为1
     * @param callBack 回调方法
     */
    @Override
    public void getQuotationList(int type, int isAll, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<QuotationList> observable = mApiService.getQuotationList(timeStamp,
                OtherUtil.getSign(timeStamp, QUOTATION_LIST_METHOD),
                YiBaiApplication.getUid(),
                type,
                isAll);
        Observer<QuotationList> observer = new Observer<QuotationList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(QuotationList quotationList) {
                callBack.onGetQuotationListSuccess(quotationList);
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
     * 报价单再次报价
     *
     * @param orderNumber 订单编号
     * @param clearOk     是否清除未完成报价
     * @param callBack    回调方法
     */
    @Override
    public void quotationAgain(String orderNumber, int clearOk, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<QuotationAgain> observable = mApiService.quotationAgain(timeStamp,
                OtherUtil.getSign(timeStamp, QUOTATION_AGAIN_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                orderNumber,
                clearOk);
        Observer<QuotationAgain> observer = new Observer<QuotationAgain>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(QuotationAgain quotationAgain) {
                callBack.onRequestComplete();
                callBack.onQuotationAgainSuccess(quotationAgain);
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
