package com.ybw.yibai.module.cases;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.Case;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.cases.CaseContract.*;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_CASE_METHOD;

/**
 * 案例界面Model实现类
 *
 * @author sjl
 * @date 2019/5/16
 */
public class CaseModelImpl implements CaseModel {

    private ApiService mApiService;

    public CaseModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取公司案例
     *
     * @param projectClassifyId 案例分类Id
     * @param callBack          回调方法
     */
    @Override
    public void getCase(int projectClassifyId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<Case> observable = mApiService.getCase(timeStamp,
                OtherUtil.getSign(timeStamp, GET_CASE_METHOD),
                YiBaiApplication.getUid(),
                projectClassifyId);
        Observer<Case> observer = new Observer<Case>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(Case case_) {
                callBack.onGetCaseSuccess(case_);
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
