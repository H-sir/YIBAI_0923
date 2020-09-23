package com.ybw.yibai.module.company;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.UpdateCompany;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.company.CompanyInfoEditContract.CallBack;
import com.ybw.yibai.module.company.CompanyInfoEditContract.CompanyInfoEditModel;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.HttpUrls.UPDATE_COMPANY_INFO_METHOD;

/**
 * 公司资料编辑Model实现类
 *
 * @author sjl
 */
public class CompanyInfoEditModelImpl implements CompanyInfoEditModel {

    private ApiService mApiService;

    public CompanyInfoEditModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 修改用户公司信息
     *
     * @param name             公司名称
     * @param logoParams       公司logo
     * @param detailsPicParams 介绍图
     * @param details          描述
     * @param callBack         回调方法
     */
    @Override
    public void updateCompanyInfo(String name, Map<String, RequestBody> logoParams, Map<String, RequestBody> detailsPicParams, String details, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UpdateCompany> observable;
        if (null == logoParams && null == detailsPicParams) {
            observable = mApiService.updateCompany(timeStamp,
                    OtherUtil.getSign(timeStamp, UPDATE_COMPANY_INFO_METHOD),
                    YiBaiApplication.getUid(),
                    name,
                    details);
        } else if (null == logoParams) {
            observable = mApiService.updateCompany(timeStamp,
                    OtherUtil.getSign(timeStamp, UPDATE_COMPANY_INFO_METHOD),
                    YiBaiApplication.getUid(),
                    name,
                    detailsPicParams,
                    details);
        } else if (null == detailsPicParams) {
            observable = mApiService.updateCompany(timeStamp,
                    OtherUtil.getSign(timeStamp, UPDATE_COMPANY_INFO_METHOD),
                    YiBaiApplication.getUid(),
                    name,
                    logoParams,
                    details);
        } else {
            observable = mApiService.updateCompany(timeStamp,
                    OtherUtil.getSign(timeStamp, UPDATE_COMPANY_INFO_METHOD),
                    YiBaiApplication.getUid(),
                    name,
                    logoParams,
                    detailsPicParams,
                    details);
        }
        Observer<UpdateCompany> observer = new Observer<UpdateCompany>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UpdateCompany updateCompany) {
                callBack.onRequestComplete();
                callBack.onUpdateCompanyInfoSuccess(updateCompany);
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
