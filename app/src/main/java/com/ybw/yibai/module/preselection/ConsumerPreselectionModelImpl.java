package com.ybw.yibai.module.preselection;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.preselection.ConsumerPreselectionContract.CallBack;
import com.ybw.yibai.module.preselection.ConsumerPreselectionContract.ConsumerPreselectionModel;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.DELETE_QUOTATION_METHOD;

/**
 * 客户预选Model实现类
 *
 * @author sjl
 * @date 2019/11/18
 */
public class ConsumerPreselectionModelImpl implements ConsumerPreselectionModel{

    public static final String TAG = "ConsumerPreselectionModelImpl";

    private ApiService mApiService;

    public ConsumerPreselectionModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 删除待摆放清单列表
     *
     * @param quoteIds 清单产品id,多个用英文逗号分隔
     * @param callBack 回调方法
     */
    @Override
    public void deletePlacementList(String quoteIds, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DeletePlacement> observable = mApiService.deletePlacement(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_QUOTATION_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                quoteIds);
        Observer<DeletePlacement> observer = new Observer<DeletePlacement>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DeletePlacement deletePlacement) {
                callBack.onDeletePlacementListSuccess(deletePlacement);
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
