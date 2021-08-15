package com.ybw.yibai.module.collection;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AddQuotation;
import com.ybw.yibai.common.bean.BaseBean;
import com.ybw.yibai.common.bean.CollectionListBean;
import com.ybw.yibai.common.bean.ProductScreeningParam;
import com.ybw.yibai.common.bean.UpdateSKUUseState;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.network.response.BaseResponse;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.CallBack;
import com.ybw.yibai.module.productusestate.ProductUseStateContract.ProductUseStateModel;

import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.ybw.yibai.common.constants.HttpUrls.ADD_PURCART_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DELETE_COLLECT_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_COLLECT_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_PRODUCT_SCREENING_PARAM_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.UPDATE_SKU_USE_STATE_METHOD;

/**
 * 产品使用状态Model实现类
 *
 * @author sjl
 * @date 2019/9/5
 */
public class CollectionLayoutModelImpl implements CollectionLayoutContract.CollectionLayoutModel {

    private final String TAG = "CollectionLayoutModelImpl";

    private ApiService mApiService;

    public CollectionLayoutModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取收藏列表
     */
    @Override
    public void getCollect(int type, int page, CollectionLayoutContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CollectionListBean> observable = mApiService.getCollection(timeStamp,
                OtherUtil.getSign(timeStamp, GET_COLLECT_METHOD),
                YiBaiApplication.getUid(), type, page, 10);
        Observer<CollectionListBean> observer = new Observer<CollectionListBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CollectionListBean collectionListBean) {
                callBack.onCollectionListBeanSuccess(collectionListBean);
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
    public void deleteCollection(List<String> skuOrCollectId, CollectionLayoutContract.CallBack callBack) {
        String collectId = "";
        for (Iterator<String> iterator = skuOrCollectId.iterator(); iterator.hasNext(); ) {
            String next = iterator.next();
            collectId = next + ",";
        }
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<BaseBean> observable = mApiService.deleteCollection(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_COLLECT_METHOD),
                YiBaiApplication.getUid(), collectId.substring(0, collectId.length() - 1)
                , "v2", "no");
        Observer<BaseBean> observer = new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(BaseBean baseBean) {
                callBack.onDeleteCollectionListSuccess(skuOrCollectId);
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
    public void upuseskuCollection(List<String> skuOrCollectId, CollectionLayoutContract.CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UpdateSKUUseState> observable = mApiService.updateSKUUseState(timeStamp,
                OtherUtil.getSign(timeStamp, UPDATE_SKU_USE_STATE_METHOD),
                YiBaiApplication.getUid(), Integer.parseInt(skuOrCollectId.get(0)));
        Observer<UpdateSKUUseState> observer = new Observer<UpdateSKUUseState>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UpdateSKUUseState updateSKUUseState) {
                callBack.onDeleteCollectionListSuccess(skuOrCollectId);
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