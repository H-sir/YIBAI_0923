package com.ybw.yibai.module.picturecase;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.CaseClassify;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.picturecase.PictureCaseContract.CallBack;
import com.ybw.yibai.module.picturecase.PictureCaseContract.PictureCaseModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.GET_CASE_CLASSIFY_METHOD;

/**
 * 图片案例 Model实现类
 *
 * @author sjl
 * @date 2019/11/9
 */
public class PictureCaseModelImpl implements PictureCaseModel{

    private ApiService mApiService;

    public PictureCaseModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取公司案例
     *
     * @param callBack 回调方法
     */
    @Override
    public void getCaseClassify(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CaseClassify> observable = mApiService.getCaseClassify(timeStamp,
                OtherUtil.getSign(timeStamp, GET_CASE_CLASSIFY_METHOD),
                YiBaiApplication.getUid());
        Observer<CaseClassify> observer = new Observer<CaseClassify>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CaseClassify caseClassify) {
                callBack.onGetCaseClassifySuccess(caseClassify);
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
