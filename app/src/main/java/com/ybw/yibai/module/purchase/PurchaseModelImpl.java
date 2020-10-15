package com.ybw.yibai.module.purchase;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AppUpdate;
import com.ybw.yibai.common.bean.PurCartBean;
import com.ybw.yibai.common.bean.QuotationData;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.purchase.PurchaseContract.CallBack;
import com.ybw.yibai.module.purchase.PurchaseContract.PurchaseModel;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.APP_UPDATE_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_PURCART_METHOD;

/**
 * 进货Fragment Model 实现类
 *
 * @author sjl
 * @date 2019/11/5
 */
public class PurchaseModelImpl implements PurchaseModel {

    private ApiService mApiService;

    public PurchaseModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取用户保存的"报价"数据
     *
     * @param callBack 回调方法
     */
    @Override
    public void getQuotationData(CallBack callBack) {
        try {
            int uid = YiBaiApplication.getUid();
            DbManager dbManager = YiBaiApplication.getDbManager();
            List<QuotationData> quotationDataList = dbManager
                    .selector(QuotationData.class)
                    .where("uid", "=", uid)
                    .findAll();
            callBack.onGetQuotationDataSuccess(quotationDataList);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPurCartData(CallBack callBack) {
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

    /**
     * 更新用户保存的"报价"数据
     *
     * @param quotationData 需要更新的"报价"数据
     * @param callBack      回调方法
     */
    @Override
    public void updateQuotationData(QuotationData quotationData, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            dbManager.update(quotationData);
            callBack.onUpdateQuotationDataFinish(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onUpdateQuotationDataFinish(false);
        }
    }

    /**
     * 删除用户保存的"报价"数据
     *
     * @param quotationData 需要删除的"报价"数据
     * @param callBack      回调方法
     */
    @Override
    public void deleteQuotationData(QuotationData quotationData, CallBack callBack) {
        try {
            DbManager dbManager = YiBaiApplication.getDbManager();
            dbManager.delete(quotationData);
            callBack.onDeleteQuotationDataFinish(true);
        } catch (DbException e) {
            e.printStackTrace();
            callBack.onDeleteQuotationDataFinish(false);
        }
    }
}
