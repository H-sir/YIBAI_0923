package com.ybw.yibai.module.quotation;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.AddQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationLocation;
import com.ybw.yibai.common.bean.CreateQuotationOrder;
import com.ybw.yibai.common.bean.DeletePlacement;
import com.ybw.yibai.common.bean.DeleteQuotationLocation;
import com.ybw.yibai.common.bean.PlacementQrQuotationList;
import com.ybw.yibai.common.bean.QuotationLocation;
import com.ybw.yibai.common.bean.UpdateQuotationInfo;
import com.ybw.yibai.common.bean.UpdateQuotationLocation;
import com.ybw.yibai.common.bean.UpdateQuotationPrice;
import com.ybw.yibai.common.bean.VerifyPassword;
import com.ybw.yibai.common.helper.SceneHelper;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.quotation.QuotationContract.CallBack;
import com.ybw.yibai.module.quotation.QuotationContract.QuotationModel;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.HttpUrls.ADD_QUOTATION_LOCATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.CREATE_QUOTATION_LOCATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.CREATE_QUOTATION_ORDER_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DELETE_QUOTATION_LOCATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.DELETE_QUOTATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_QUOTATION_LIST_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.GET_QUOTATION_LOCATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.UPDATE_QUOTATION_INFO_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.UPDATE_QUOTATION_LOCATION_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.UPDATE_QUOTATION_PRICE_METHOD;
import static com.ybw.yibai.common.constants.HttpUrls.VERIFY_PASSWORD_METHOD;

/**
 * 报价Model实现类
 *
 * @author sjl
 * @date 2019/10/30
 */
public class QuotationModelImpl implements QuotationModel {

    private ApiService mApiService;

    public QuotationModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 获取报价位置明细列表
     *
     * @param callBack 回调方法
     */
    @Override
    public void getQuotationLocation(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<QuotationLocation> observable = mApiService.getQuotationLocation(timeStamp,
                OtherUtil.getSign(timeStamp, GET_QUOTATION_LOCATION_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid());
        Observer<QuotationLocation> observer = new Observer<QuotationLocation>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(QuotationLocation quotationPlace) {
                callBack.onGetQuotationLocationSuccess(quotationPlace);
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
     * 获取摆放清单列表
     *
     * @param callBack 回调方法
     */
    @Override
    public void getPlacementList(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<PlacementQrQuotationList> observable = mApiService.getPlacementQrQuotationList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_QUOTATION_LIST_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                1);
        Observer<PlacementQrQuotationList> observer = new Observer<PlacementQrQuotationList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(PlacementQrQuotationList placementQrQuotationList) {
                callBack.onGetPlacementListSuccess(placementQrQuotationList);
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
     * 获取报价清单列表
     *
     * @param callBack 回调方法
     */
    @Override
    public void getQuotationList(CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<PlacementQrQuotationList> observable = mApiService.getPlacementQrQuotationList(timeStamp,
                OtherUtil.getSign(timeStamp, GET_QUOTATION_LIST_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                2);
        Observer<PlacementQrQuotationList> observer = new Observer<PlacementQrQuotationList>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(PlacementQrQuotationList placementQrQuotationList) {
                callBack.onGetQuotationListSuccess(placementQrQuotationList);
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
     * 创建报价位置
     *
     * @param name     位置名称
     * @param callBack 回调方法
     */
    @Override
    public void createQuotationLocation(String name, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CreateQuotationLocation> observable = mApiService.createQuotationLocation(timeStamp,
                OtherUtil.getSign(timeStamp, CREATE_QUOTATION_LOCATION_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                name);
        Observer<CreateQuotationLocation> observer = new Observer<CreateQuotationLocation>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CreateQuotationLocation createQuotationLocation) {
                callBack.onCreateQuotationLocationSuccess(createQuotationLocation);
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
     * 删除报价位置
     *
     * @param quotePlaceId 报价位置id
     * @param callBack     回调方法
     */
    @Override
    public void deleteQuotationLocation(int quotePlaceId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<DeleteQuotationLocation> observable = mApiService.deleteQuotationLocation(timeStamp,
                OtherUtil.getSign(timeStamp, DELETE_QUOTATION_LOCATION_METHOD),
                YiBaiApplication.getUid(),
                quotePlaceId);
        Observer<DeleteQuotationLocation> observer = new Observer<DeleteQuotationLocation>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(DeleteQuotationLocation deleteQuotationLocation) {
                callBack.onDeleteQuotationLocationSuccess(deleteQuotationLocation);
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
     * 修改报价位置信息
     *
     * @param quotePlaceId 报价位置id
     * @param name         位置名称
     * @param params       设计图
     * @param callBack     回调方法
     */
    @Override
    public void updateQuotationLocation(int quotePlaceId, String name, Map<String, RequestBody> params, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UpdateQuotationLocation> observable;
        if (null == params) {
            observable = mApiService.updateQuotationLocation(timeStamp,
                    OtherUtil.getSign(timeStamp, UPDATE_QUOTATION_LOCATION_METHOD),
                    YiBaiApplication.getUid(),
                    quotePlaceId,
                    name);
        } else {
            observable = mApiService.updateQuotationLocation(timeStamp,
                    OtherUtil.getSign(timeStamp, UPDATE_QUOTATION_LOCATION_METHOD),
                    YiBaiApplication.getUid(),
                    quotePlaceId,
                    name,
                    params);
        }
        Observer<UpdateQuotationLocation> observer = new Observer<UpdateQuotationLocation>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UpdateQuotationLocation updateQuotationLocation) {
                callBack.onUpdateQuotationLocationSuccess(updateQuotationLocation);
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
     * 修改报价位置中产品数量
     *
     * @param quotePlaceId 报价位置id
     * @param quoteId      清单产品id
     * @param number       产品数量
     * @param callBack     回调方法
     */
    @Override
    public void updateQuotationInfo(int quotePlaceId, int quoteId, int number, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UpdateQuotationInfo> observable = mApiService.updateQuotationInfo(timeStamp,
                OtherUtil.getSign(timeStamp, UPDATE_QUOTATION_INFO_METHOD),
                YiBaiApplication.getUid(),
                quotePlaceId,
                quoteId,
                number);
        Observer<UpdateQuotationInfo> observer = new Observer<UpdateQuotationInfo>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UpdateQuotationInfo updateQuotationInfo) {
                callBack.onUpdateQuotationInfoSuccess(updateQuotationInfo);
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
     * 待摆放清单加入到报价位置
     *
     * @param quoteId      清单id
     * @param quotePlaceId 报价位置id
     * @param callBack     回调方法
     */
    @Override
    public void addQuotationLocation(int quoteId, int quotePlaceId, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<AddQuotationLocation> observable = mApiService.addQuotationLocation(timeStamp,
                OtherUtil.getSign(timeStamp, ADD_QUOTATION_LOCATION_METHOD),
                YiBaiApplication.getUid(),
                quoteId,
                quotePlaceId);
        Observer<AddQuotationLocation> observer = new Observer<AddQuotationLocation>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(AddQuotationLocation addQuotationLocation) {
                callBack.onAddQuotationLocationSuccess(addQuotationLocation);
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
     * 创建报价单
     *
     * @param payType       支付模式id(支付模式0购买1月租2年租)默认为1
     * @param taxRate       税点
     * @param discountMoney 优惠金额
     * @param callBack      回调方法
     */
    @Override
    public void createQuotationOrder(int payType, double taxRate, double discountMoney, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<CreateQuotationOrder> observable = mApiService.createQuotationOrder(timeStamp,
                OtherUtil.getSign(timeStamp, CREATE_QUOTATION_ORDER_METHOD),
                YiBaiApplication.getUid(),
                YiBaiApplication.getCid(),
                payType,
                taxRate,
                discountMoney);
        Observer<CreateQuotationOrder> observer = new Observer<CreateQuotationOrder>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(CreateQuotationOrder createQuotationOrder) {
                callBack.onCreateQuotationOrderSuccess(createQuotationOrder);
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
     * 验证修改价格密码
     *
     * @param viewPassword 查看密码
     * @param callBack     回调方法
     */
    @Override
    public void verifyPassword(String viewPassword, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<VerifyPassword> observable = mApiService.verifyPassword(timeStamp,
                OtherUtil.getSign(timeStamp, VERIFY_PASSWORD_METHOD),
                YiBaiApplication.getUid(),
                viewPassword);
        Observer<VerifyPassword> observer = new Observer<VerifyPassword>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(VerifyPassword verifyPassword) {
                callBack.onVerifyPasswordSuccess(verifyPassword);
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
     * 修改清单产品价格
     *
     * @param quoteId  清单id
     * @param price    新价格
     * @param type     1售价2月租价
     * @param callBack 回调方法
     */
    @Override
    public void updateQuotationPrice(int quoteId, double price, int type, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UpdateQuotationPrice> observable = mApiService.updateQuotationPrice(timeStamp,
                OtherUtil.getSign(timeStamp, UPDATE_QUOTATION_PRICE_METHOD),
                YiBaiApplication.getUid(),
                quoteId,
                price,
                type);
        Observer<UpdateQuotationPrice> observer = new Observer<UpdateQuotationPrice>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UpdateQuotationPrice updateQuotationPrice) {
                callBack.onUpdateQuotationPriceSuccess(updateQuotationPrice);
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
