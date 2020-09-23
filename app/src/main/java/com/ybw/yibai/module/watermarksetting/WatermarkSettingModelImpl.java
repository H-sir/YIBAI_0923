package com.ybw.yibai.module.watermarksetting;

import com.ybw.yibai.base.YiBaiApplication;
import com.ybw.yibai.common.bean.UpdateWatermark;
import com.ybw.yibai.common.interfaces.ApiService;
import com.ybw.yibai.common.utils.OtherUtil;
import com.ybw.yibai.common.utils.RetrofitManagerUtil;
import com.ybw.yibai.common.utils.TimeUtil;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingContract.CallBack;
import com.ybw.yibai.module.watermarksetting.WatermarkSettingContract.WatermarkSettingModel;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

import static com.ybw.yibai.common.constants.HttpUrls.UPDATE_WATERMARK_METHOD;

/**
 * 水印设置界面Model实现类
 *
 * @author sjl
 * @date 2019/11/21
 */
public class WatermarkSettingModelImpl implements WatermarkSettingModel {

    private ApiService mApiService;

    public WatermarkSettingModelImpl() {
        RetrofitManagerUtil instance = RetrofitManagerUtil.getInstance();
        mApiService = instance.getApiService();
    }

    /**
     * 修改水印图片
     *
     * @param params   水印图片
     * @param callBack 回调方法
     */
    @Override
    public void updateWatermark(Map<String, RequestBody> params, CallBack callBack) {
        String timeStamp = String.valueOf(TimeUtil.getTimestamp());
        Observable<UpdateWatermark> observable = mApiService.updateWatermark(timeStamp,
                OtherUtil.getSign(timeStamp, UPDATE_WATERMARK_METHOD),
                YiBaiApplication.getUid(),
                params);
        Observer<UpdateWatermark> observer = new Observer<UpdateWatermark>() {
            @Override
            public void onSubscribe(Disposable d) {
                callBack.onRequestBefore(d);
            }

            @Override
            public void onNext(UpdateWatermark updateWatermark) {
                callBack.onUpdateWatermarkSuccess(updateWatermark);
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
